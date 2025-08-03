from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles
from contextlib import asynccontextmanager
from starlette.middleware.cors import CORSMiddleware

from api import get_api_routers
from webhooks import get_webhooks
from core.config import Settings, configure_logging
from scheduler import init_scheduler


config = Settings() # pyright: ignore[reportCallIssue]
configure_logging()

@asynccontextmanager
async def lifespan(app: FastAPI):    
    yield


app = FastAPI(
    lifespan=lifespan,
    title='Hackathon',
    debug=True
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
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:5173",
        "https://unique-banoffee-accf48.netlify.app",
    ],
    allow_methods=['*'],
    allow_headers=['*'],
    allow_credentials=True
)
