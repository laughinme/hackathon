from typing import Annotated
from fastapi import APIRouter, Depends, Header, Request, Response, HTTPException
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials

from service.auth import TokenService, get_token_service
from domain.auth import TokenPair
from core.config import Settings
from core.middlewares.rate_limit import rate_limit

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]
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
	path='/refresh',
	response_model=TokenPair,
	summary='Rotate tokens'
)
async def refresh_tokens(
	request: Request,
	response: Response,
	creds: Annotated[HTTPAuthorizationCredentials | None, Depends(security)],
	svc: Annotated[TokenService, Depends(get_token_service)],
	x_csrf: str | None = Header(
		default=None, alias="X-CSRF-Token", 
		description='Must only be passed for requests from browsers'
	),
	_: Annotated[None, Depends(rate_limit("auth_refresh", 10, 60))] = None,
) -> TokenPair:
	cookie_refresh = request.cookies.get("refresh_token")
	
	if cookie_refresh:
		if not x_csrf:
			raise HTTPException(status_code=403, detail="Missing CSRF token")
		if not _origin_allowed(request, config.ALLOWED_ORIGINS):
			raise HTTPException(status_code=403, detail="Invalid request origin")
		
		result = await svc.refresh_tokens(cookie_refresh, x_csrf)
		if result is None:
			# token invalid or csrf mismatch
			response.delete_cookie("refresh_token", samesite="lax")
			response.delete_cookie("csrf_token", samesite="lax")
			raise HTTPException(status_code=401, detail="Invalid refresh token")
		
		new_access, new_refresh, new_csrf = result
		
		# set fresh cookies
		response.set_cookie(
			"refresh_token", new_refresh,
			max_age=config.REFRESH_TTL,
			httponly=True, 
			secure=True,
			samesite="lax"
		)
		response.set_cookie(
			"csrf_token", new_csrf,
			max_age=config.REFRESH_TTL,
			httponly=False, 
			secure=True, 
			samesite="lax"
		)

		# body: only short-lived access token
		return TokenPair(access_token=new_access, refresh_token=None)
		
	if not creds or creds.scheme.lower() != "bearer":
		raise HTTPException(status_code=401, detail="Missing refresh token")
	
	result = await svc.refresh_tokens(creds.credentials)
	if result is None:
		raise HTTPException(status_code=401, detail="Invalid refresh token")

	new_access, new_refresh, _ = result
	return TokenPair(access_token=new_access, refresh_token=new_refresh)
