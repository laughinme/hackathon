from typing import Annotated, Literal
from fastapi import APIRouter, Depends, Response, Request, Header, HTTPException
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials

from core.config import Settings
from service.auth import CredentialsService, get_credentials_service
from domain.auth import UserLogin, TokenPair
from core.middlewares.rate_limit import rate_limit

settings = Settings() # pyright: ignore[reportCallIssue]
router = APIRouter()
security = HTTPBearer(
	auto_error=False, 
	description='Send refresh token as Bearer for non-browser clients'
)


def _origin_allowed(request: Request, allowed: list[str]) -> bool:
	origin = request.headers.get('origin')
	referer = request.headers.get('referer')
	candidates = [origin or '', referer or '']
	return any(c and any(c.startswith(a) for a in allowed) for c in candidates)


@router.post(
	path="/login",
	response_model=TokenPair,
	summary="Authenticate user and issue tokens",
	responses={        
		200: {
			'description': 'refresh_token field varies depending on the source of request. ' \
			'For web it is always null and being set as httponly cookie, ' \
			'however any other platform gets access and refresh tokens in response body. ' \
			'It is made to protect web from xss and csrf attacks'
		},
		401: {"description": "Wrong credentials"}
	}
)
async def login_user(
	response: Response,
	payload: UserLogin,
	svc: Annotated[CredentialsService, Depends(get_credentials_service)],
	client: Literal['web', 'mobile'] = Header('web', alias='X-Client'),
	request: Request = None,
	_: Annotated[None, Depends(rate_limit("auth_login", 5, 60))] = None,
) -> TokenPair:
	if client == 'web' and not _origin_allowed(request, settings.ALLOWED_ORIGINS):
		raise HTTPException(status_code=403, detail="Invalid request origin")
	access, refresh, csrf = await svc.login(payload, client)
	
	if client == 'web':
		response.set_cookie(
			"refresh_token",
			refresh,
			max_age=settings.REFRESH_TTL,
			httponly=True,
			secure=True,
			samesite="lax",
		)
		response.set_cookie(
			'csrf_token',
			csrf,
			max_age=settings.REFRESH_TTL,
			secure=True,
			httponly=False,
			samesite='lax'
		)
	
		return TokenPair(access_token=access, refresh_token=None)
	
	return TokenPair(access_token=access, refresh_token=refresh)


@router.post(
	path="/logout",
	responses={401: {"description": "Not authorized"}}
)
async def logout(
	request: Request,
	response: Response,
	svc: Annotated[CredentialsService, Depends(get_credentials_service)],
	creds: Annotated[HTTPAuthorizationCredentials, Depends(security)]
) -> dict:
	refresh_cookie = request.cookies.get("refresh_token")
	
	refresh_header = (
		creds.credentials if creds and creds.scheme.lower() == "bearer" else None
	)

	token = refresh_cookie or refresh_header
	if token is None:
		raise HTTPException(401, 'Refresh token is not passed')
	
	await svc.logout(token)
	
	if refresh_cookie:
		response.delete_cookie("refresh_token", samesite="lax")
		response.delete_cookie("csrf_token", samesite="lax")
		
	return {'message': 'Logged out successfully'}
