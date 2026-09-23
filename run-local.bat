@echo off
if not exist .env (
  echo ERROR: .env file not found.
  echo Copy .env.example to .env and put your MongoDB Atlas URI in MONGODB_URI.
  pause
  exit /b 1
)
call mvnw.cmd spring-boot:run
