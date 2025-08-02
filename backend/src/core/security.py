from typing import Annotated
from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer

from domain.auth import Role
from database.relational_db import User, UoW, get_uow
from service.auth import TokenService, get_token_service
from service.users import UserService, get_user_service


security = HTTPBearer(
    # auto_error=False,
    description='Access token must be passed as Bearer to authorize request'
)


async def auth_user(
    creds: Annotated[HTTPAuthorizationCredentials, Depends(security)],
    token_svc: Annotated[TokenService, Depends(get_token_service)],
    user_svc: Annotated[UserService, Depends(get_user_service)],
) -> User:
    # if creds is None:
    #     raise HTTPException(status.HTTP_401_UNAUTHORIZED, "Not Authorized")
    
    payload = await token_svc.verify_access(creds.credentials)
    if payload is None:
        raise HTTPException(status.HTTP_401_UNAUTHORIZED, "Bad access token passed")
    
    user_id = payload['sub']
    user = await user_svc.get_user(user_id)
    if user is None:
        raise HTTPException(401, detail="Not Authorized")
    
    return user


# async def auth_admin(
#     request: Request,
#     service: Annotated[UserService, Depends(get_user_service)]
# ) -> User:
#     user = await service.get_me(request)
#     if not user:
#         raise HTTPException(401, detail="Not authorized")
#     if user.role.value < Role.ADMIN.value:
#         raise HTTPException(403, detail="You don't have permission to do this")
    
#     return user
