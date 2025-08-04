from fastapi import HTTPException

class IncorrectNewlyError(HTTPException):
    def __init__(self, *args, **kwargs):
        super().__init__(
            status_code=400,
            detail='You can perform this with only newly created exchange request'
        )

class IncorrectStatusError(HTTPException):
    def __init__(self, *args, **kwargs):
        super().__init__(
            status_code=400,
            detail='You can perform this with only accepted/declined exchange requests'
        )
