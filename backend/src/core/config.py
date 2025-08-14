import logging

from pathlib import Path
from pydantic import SecretStr

from pydantic_settings import BaseSettings, SettingsConfigDict

BASE_DIR  = Path(__file__).resolve().parent.parent.parent

class Settings(BaseSettings):
	"""
	Project dependencies config
	"""
	model_config = SettingsConfigDict(
		env_file=f'{BASE_DIR}/.env',
		extra='ignore'
	)
	
	# API settings
	API_PORT: int = 8080
	API_HOST: str = '0.0.0.0'
	DEBUG: bool = False
	
	# CORS / Hosts
	ALLOWED_ORIGINS: list[str] = [
		"http://localhost:5173",
		"https://unique-banoffee-accf48.netlify.app",
	]
	ALLOWED_HEADERS: list[str] = [
		"Authorization", "Content-Type", "Accept", "X-Client", "X-CSRF-Token"
	]
	ALLOWED_METHODS: list[str] = ["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"]
	TRUSTED_HOSTS: list[str] = ["*"]
	ENABLE_HTTPS_REDIRECT: bool = False
	ENABLE_SECURITY_HEADERS: bool = True
	ENABLE_HSTS: bool = False
	SECURITY_HEADERS_CSP: str = "default-src 'none'; frame-ancestors 'none'; base-uri 'none'"
	
	# Auth Settings    
	JWT_PRIVATE_KEY: str
	JWT_PUBLIC_KEY: str
	JWT_ALGO: str = 'RS256'
	JWT_ISSUER: str = 'hackathon'
	JWT_AUDIENCE: str = 'hackathon-client'
	ACCESS_TTL: int = 60 * 15
	REFRESH_TTL: int = 60 * 60 * 24 * 7
	CSRF_HMAC_KEY: bytes
	
	# Database settings
	DATABASE_URL: str
	REDIS_URL: str
	SQL_ECHO: bool = False

	# Site data (url, paths)
	SITE_URL: str = ''
	MEDIA_DIR: str = 'media'
	
	# Upload constraints
	MAX_BOOK_PHOTOS: int = 6
	MAX_IMAGE_SIZE_MB: int = 5
	MAX_AVATAR_SIZE_MB: int = 5


def configure_logging():
	logging.basicConfig(
		level=logging.INFO,
		format="%(asctime)s %(levelname)s [%(filename)s:%(lineno)d] %(message)s",
	)
