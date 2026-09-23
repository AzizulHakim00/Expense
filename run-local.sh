#!/usr/bin/env sh
set -e
if [ ! -f .env ]; then
  echo "ERROR: .env file not found. Copy .env.example to .env and set MONGODB_URI."
  exit 1
fi
./mvnw spring-boot:run
