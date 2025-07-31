from pydantic import BaseModel, Field, EmailStr


class UserModel(BaseModel):
    """User account representation."""

    email: EmailStr = Field(..., description="User e-mail")
    
    username: str | None = Field(None, description="User's display name")
    avatar_url: str | None = Field(None)
