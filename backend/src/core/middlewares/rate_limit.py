from typing import Callable, Awaitable
from fastapi import Request, HTTPException
from database.redis.redis_client import get_redis


def rate_limit(scope: str, limit: int, window_seconds: int) -> Callable[[Request], Awaitable[None]]:
	async def _limiter(request: Request) -> None:
		redis = get_redis()
		client = request.client.host if request.client else 'unknown'
		key = f"rl:{scope}:{client}"
		current = await redis.incr(key)
		if current == 1:
			await redis.expire(key, window_seconds)
		if current > limit:
			raise HTTPException(status_code=429, detail="Too many requests, slow down")
	return _limiter