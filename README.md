# ASD-LCN-System

A modern **Local Community Network System** built with Java and Quarkus, designed to facilitate community engagement and local network connectivity.

## Overview

The ASD-LCN-System is a robust backend application that provides services for managing local community networks. It leverages modern Java technologies and cloud-native patterns to deliver scalable, performant services.

## Technology Stack

- **Runtime Framework**: [Quarkus](https://quarkus.io/) - A Kubernetes-native Java stack designed for Java virtual machines (JVMs) and native compilation
- **Language**: Java 21
- **Build Tool**: Maven 3.x
- **Database**: PostgreSQL 17
- **ORM**: Hibernate with Panache
- **REST API**: Quarkus REST with Jackson for JSON serialization
- **Authentication**: SmallRye JWT
- **API Documentation**: SmallRye OpenAPI (Swagger)
- **Container**: Docker & Docker Compose
- **Testing**: JUnit 5 + REST Assured

## Features

-  **Fast Startup & Low Memory**: Optimized for containerized deployments
-  **JWT-based Authentication**: Secure token-based auth with SmallRye JWT
-  **Database Support**: Primary PostgreSQL with MySQL compatibility
-  **API Documentation**: Auto-generated OpenAPI/Swagger documentation

## Prerequisites

Before you begin, ensure you have the following installed:

- Java Development Kit (JDK) 21 or later
- Maven 3.9+ (or use the included Maven wrapper)
- Docker and Docker Compose
- PostgreSQL 17 (or use Docker Compose)

## Quick Start

### Using Docker Compose (Recommended)

1. **Clone the repository:**
   ```bash
   git clone https://github.com/HannaXTX/ASD-LCN-System.git
   cd ASD-LCN-System
   ```

2. **Configure environment variables:**
   ```bash
   export QUARKUS_DATASOURCE_USERNAME=postgres
   export QUARKUS_DATASOURCE_PASSWORD=postgres
   export DB_NAME=asd_db
   ```

3. **Start the application with Docker Compose:**
   ```bash
   docker-compose up
   ```

   The application will be available at `http://localhost:8080`

### Local Development

1. **Clone the repository:**
   ```bash
   git clone https://github.com/HannaXTX/ASD-LCN-System.git
   cd ASD-LCN-System
   ```

2. **Set up PostgreSQL locally** or use Docker:
   ```bash
   docker run -d \
     -e POSTGRES_DB=asd_db \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=postgres \
     -p 5432:5432 \
     postgres:17
   ```

3. **Configure environment variables:**
   ```bash
   export QUARKUS_DATASOURCE_USERNAME=postgres
   export QUARKUS_DATASOURCE_PASSWORD=postgres
   export QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://localhost:5432/asd_db
   ```

4. **Build and run the application:**
   ```bash
   ./mvnw clean quarkus:dev
   ```

   The application will start in development mode at `http://localhost:8080`

## Building the Project

### Development Build

```bash
./mvnw clean package
```

### Native Executable Build

For GraalVM native image compilation:

```bash
./mvnw clean package -Dnative
```

This creates a native binary with minimal startup time and memory footprint.

### JAR Build

```bash
./mvnw clean package -DskipTests=true
java -jar target/quarkus-app/quarkus-run.jar
```

## API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/q/swagger-ui
- **OpenAPI Schema**: http://localhost:8080/q/openapi

## Configuration

Environment variables for configuration:

| Variable | Default | Description |
|----------|---------|-------------|
| `QUARKUS_DATASOURCE_USERNAME` | - | Database username |
| `QUARKUS_DATASOURCE_PASSWORD` | - | Database password |
| `AES_KEY` | - | AES KEY |
| `DB_NAME` | - | Database name |

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/              # Application source code
│   │   ├── resources/         # Configuration files
│   │   └── docker/            # Docker build files
│   └── test/
│       └── java/              # Test classes
├── pom.xml                    # Maven configuration
├── compose.yaml               # Docker Compose configuration
├── mvnw                       # Maven wrapper (Unix/Linux/Mac)
├── mvnw.cmd                   # Maven wrapper (Windows)
└── README.md                  # This file
```

## Development Workflow

### Live Reload

When running with `./mvnw quarkus:dev`, the application will automatically reload when you change your source files.

## Deployment

### Docker

Build and run the Docker image:

```bash
docker build -f src/main/docker/Dockerfile.jvm -t asd-lcn-system:latest .
docker run -p 8080:8080 -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://host:5432/asd_db asd-lcn-system:latest
```

### Docker Compose

Simply run:

```bash
docker-compose up -d
```

