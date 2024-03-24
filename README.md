
# Backend for Uniboard


## Installation

Install with [Docker](https://docs.docker.com/engine/install/) and [Docker-Compose](https://docs.docker.com/compose/install/)

`docker-compose.yml` file:
```yml
services:
  backend:
    image: ghcr.io/uni-board/backend:0.0.1-snapshot
    ports:
      - 80:8080 # API
      - 81:8081 # Sockets
```
## Run Locally

Run `docker-compose.yml` with:
```bash
docker-compose up
```
Or, if you want process to be in **background**:
```bash
docker-compose up -d
```