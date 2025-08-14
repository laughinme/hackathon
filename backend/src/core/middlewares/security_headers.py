from starlette.middleware.base import BaseHTTPMiddleware
from starlette.types import ASGIApp, Receive, Scope, Send
from starlette.responses import Response


class SecurityHeadersMiddleware(BaseHTTPMiddleware):
	def __init__(self, app: ASGIApp, csp: str | None = None, enable_hsts: bool = False):
		super().__init__(app)
		self.csp = csp
		self.enable_hsts = enable_hsts

	async def dispatch(self, request: Scope, call_next):
		response: Response = await call_next(request)
		# Basic hardening headers
		response.headers.setdefault('X-Content-Type-Options', 'nosniff')
		response.headers.setdefault('X-Frame-Options', 'DENY')
		response.headers.setdefault('Referrer-Policy', 'no-referrer')
		if self.csp:
			response.headers.setdefault('Content-Security-Policy', self.csp)
		if self.enable_hsts and request.get('scheme') == 'https':
			response.headers.setdefault('Strict-Transport-Security', 'max-age=31536000; includeSubDomains')
		return response