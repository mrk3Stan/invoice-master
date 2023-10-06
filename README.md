# Invoice-Master

## Prerequisites

- Docker
- Java 17

## Starting the application
Start docker on your machine
```bash
docker-compose up
```
This will run PostgreSQL though docker.
Then run the application using gradle wrapper.
```bash
./gradlew bootRun
```
To shutdown the application
```bash
cmd+c or ctrl+c
```
To stop and remove docker
```bash
docker-compose down
```
