# Gatekeeper

![License](https://img.shields.io/github/license/oliverknox/gatekeeper)

## Overview

An authentication and authorisation API.

## Table of Contents

1. [Features](#features)
2. [Requirements](#requirements)
3. [Installation](#installation)
4. [Configuration](#configuration)
5. [Usage](#usage)
6. [API Documentation](#api-documentation)
7. [Testing](#testing)
8. [Deployment](#deployment)
9. [Contributing](#contributing)
10. [License](#license)
11. [Contact](#contact)

## Features

- CRUD operations for Users.
- Granting and refreshing JWTs.

## Requirements

- Java 21
- [Maven](https://maven.apache.org)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [PostgreSQL](https://www.postgresql.org/)
- [Docker](https://www.docker.com)

## Installation

### Prerequisites

Ensure you have the following installed:

- [Java 21 JDK](https://jdk.java.net/21)
- [Maven](https://maven.apache.org/install.html)

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/oliverknox/gatekeeper.git
   ```
2. Navigate to the project directory:
   ```bash
   cd gatekeeper
   ```
3. Build the project:
   ```bash
   ./mvnw clean install
   ```

## Configuration

Instructions to configure the microservice.

- **application-local.properties**
    - HTTPS is required to provide a secure cookie containing a refresh token. This can be achieved by setting the
      following properties and generating the `keystore.p12` file
      using [keytool](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html).
        ```properties
        server.ssl.key-store=classpath:keystore.p12
        server.ssl.key-store-password=root1#
        server.ssl.key-store-type=PKCS12
        server.ssl.key-alias=tomcat
        ```
  - This requires running the jar with `--spring.profiles.active=local` or using the `docker-compose-local.yml` file.

- **Environment Variables**
    - `JWT_SECRET_BASE64`: A 256 byte key encoded in Base 64. Used to generate JWT tokens.
  - `DB_USER`: The username of a database user for this microservice.
  - `DB_PASSWORD`: The password of a database user for this microservice.

## Usage

Instructions to start and interact with the microservice.

### Starting the Service

To run the service locally:

```bash
./mvnw spring-boot:run
```

### Example Request

An example of a create user request which can be used to check if the microservice is working.

```bash
curl -X 'POST' \
  'https://localhost:8080/users' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "Username",
  "password": "Password"
}'
```

## API Documentation

The API is documented using [OpenAPI V3](https://swagger.io/specification/),
the [Swagger UI](https://gatekeeper.knox.gb.net/swagger-ui/index.html) is publicly available.

## Testing

Instructions for running tests.

### Unit Tests

To run unit tests:

```bash
./mvnw test
```

### Integration Tests

To run integration tests:
N/a

## Deployment

Instructions for deploying the microservice to different environments.

### Local Deployment

Instructions for running the microservice locally.

Docker is used to run the microservice and database. First we need to build the microservice, set up the initialisation
scripts and then run docker
compose to set up the database and microservice.

To build:

```bash
./mvnw clean package
```

To set up the initialisation scripts:

Go to the [init SQL file](scripts/init.sql) and change the `username` and `password` field to the desired database user
for the microservice.
This should be equal to `DB_USER` and `DB_PASSWORD` environment variables. Once the database service starts it will
automatically copy and run the SQL queries in this file.

To start docker:

```bash
export POSTGRES_USER=postgres;      # The default user for PostgreSQL superuser.
export POSTGRES_PASSWORD=password;  # The default password for PostgreSQL superuser.
export DB_USER=gatekeeper;          # The PostgreSQL user dedicated to this microservice.
export DB_PASSWORD=password;        # The password for the dedicated PostgreSQL user of this microservice.
export JWT_SECRET_BASE64=x52/...;   # A 256 byte key encoded in Base 64. Used to generate JWT tokens.

docker compose up
```

After the docker image has started the microservice will be accessible on port 8080.

### Production Deployment

Instructions for deploying the microservice to production, including any CI/CD steps.

Docker is also used to run the microservice and database in production. Similar steps as a local deployment are followed
but some additional work is required to move the neccessary files to the server.

To build:

```bash
./mvnw clean package # build jar.
docker build -t $DOCKER_USERNAME/gatekeeper:latest . # build docker image.
docker login # login to docker hub.
docker push $DOCKER_USERNAME/gatekeeper:latest # publish docker image.
```

Then to transfer files to the server:

```bash
ssh $DROPLET_USERNAME@$DROPLET_HOST # login to server via ssh.
mkdir -p services/gatekeeper/scripts # set up directory structure.
exit # exit ssh for now.

scp docker-compose.yml $DROPLET_USERNAME@$DROPLET_HOST:/services/gatekeeper
scp scripts/init.sql $DROPLET_USERNAME@$DROPLET_HOST:/services/gatekeeper/scripts
```

Make sure to set all the necessary environment variables as documented above plus an additional one:

```bash
export DOCKER_USERNAME=<your-docker-username>
```

Then the following commands will configure and spin up a new microservice and dependencies:

```bash
docker compose down # remove an existing containers.
sed -i -e "s/username/${DB_USER}/g" \ 
    -i -e "s/password/${DB_PASSWORD}/g" \
    scripts/init.sql # replace default username and password in init.sql with environment variable on server.
docker pull ${DOCKER_USERNAME}/gatekeeper:latest # pull latest docker image.
docker compose up -d # run detatched instance of docker compose.
```

After the docker image has started the microservice will be accessible on port 8080.

## Contributing

Guidelines for contributing to the project.

- Fork the repository
- Create a new branch
- Make your changes
- Submit a pull request

## License

This project is licensed under the [MIT License](LICENSE).

## Contact

- **Author**: Oliver Knox
- **Email**: oliver@knox.gb.net
- **GitHub**: [oliverknox](https://github.com/oliverknox)

---