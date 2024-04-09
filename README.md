
# Backend for Uniboard


## Installation

Install with [Docker](https://docs.docker.com/engine/install/) and [Docker-Compose](https://docs.docker.com/compose/install/)

`docker-compose.yml` file:
```yml
networks:
  board:
    driver: bridge
    ipam:
      config:
        - subnet: 10.5.0.0/16
          gateway: 10.5.0.1

services:
  backend:
    image: uniboard-backend:latest
    environment:
      DB_CONNECT: mongodb://10.5.0.3:27017 # required(if NO_DB=false), connect url to MongoDB
      SOCKETS_ENABLED: "true" # optional, Sockets API
      NO_DB: "false" # optional, if true, uses in-memory db, otherwise uses MongoDB
      TRACE: "false" # optional, if true, writes all logs, system will be slower
    ports:
      - 80:8080 # API
      - 81:8081 # Sockets
    networks:
      board:
        ipv4_address: 10.5.0.2
    volumes:
      - ./data:/app/data
    depends_on:
      - db

  db:
    image: mongo:latest
    ports:
      - 27017:27017
    networks:
      board:
        ipv4_address: 10.5.0.3
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