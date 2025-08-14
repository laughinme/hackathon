from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles
from contextlib import asynccontextmanager
from starlette.middleware.cors import CORSMiddleware
from starlette.middleware.trustedhost import TrustedHostMiddleware
from starlette.middleware.httpsredirect import HTTPSRedirectMiddleware

from api import get_api_routers
from webhooks import get_webhooks
from core.config import Settings, configure_logging
from scheduler import init_scheduler
from core.middlewares.security_headers import SecurityHeadersMiddleware


config = Settings() # pyright: ignore[reportCallIssue]
configure_logging()

@asynccontextmanager
async def lifespan(app: FastAPI):    
	yield


app = FastAPI(
	lifespan=lifespan,
	title='Hackathon',
	debug=config.DEBUG
)

# Mount static
app.mount('/media', StaticFiles(directory=config.MEDIA_DIR, check_dir=False), 'media')

# Including routers
app.include_router(get_api_routers())
app.include_router(get_webhooks())

@app.get('/')
@app.get('/ping')
async def ping():
	return {'status': 'operating'}


# Adding middlewares
if config.TRUSTED_HOSTS:
	app.add_middleware(TrustedHostMiddleware, allowed_hosts=config.TRUSTED_HOSTS)

if config.ENABLE_HTTPS_REDIRECT:
	app.add_middleware(HTTPSRedirectMiddleware)

app.add_middleware(
	CORSMiddleware,
	allow_origins=config.ALLOWED_ORIGINS,
	allow_methods=config.ALLOWED_METHODS,
	allow_headers=config.ALLOWED_HEADERS,
	allow_credentials=True
)

if config.ENABLE_SECURITY_HEADERS:
	app.add_middleware(
		SecurityHeadersMiddleware,
		csp=config.SECURITY_HEADERS_CSP,
		enable_hsts=config.ENABLE_HSTS,
	)
