import uvicorn
from fastapi import FastAPI
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
    ],
    allow_methods=['*'],
    allow_headers=['*'],
    allow_credentials=True
)

# if __name__ == "__main__":
#     uvicorn.run(app, host=config.API_HOST, port=config.API_PORT)
