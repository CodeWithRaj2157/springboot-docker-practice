# Spring Boot Application – Docker Multi-Stage Build

## Project Overview

This project demonstrates how to containerize a Java Spring Boot REST API application using Docker Multi-Stage Builds.

The application provides REST APIs to retrieve student information and check application health.

The main objective is to build an optimized, lightweight Docker image by separating the application build environment from the runtime environment.

## Technologies Used

- Java 17
- Spring Boot 3.4.5
- Maven
- Docker
- Docker Multi-Stage Build
- Linux
- Git & GitHub

## Project Structure

```text
springboot-docker-practice/
│
├── src/
│   └── main/
│       └── java/
│           └── com/example/studentapp/
│               ├── StudentApplication.java
│               └── controller/
│                   └── StudentController.java
│
├── pom.xml
├── Dockerfile
├── .dockerignore
└── README.md
```

## Application API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/students` | Returns the list of students |
| GET | `/api/health` | Checks application health |

### Sample API Responses

**1. Get Students**

```bash
curl http://localhost:5000/api/students
```

Response:

```json
["Rajendra","Amit","Rahul","Priya"]
```

**2. Health Check**

```bash
curl http://localhost:5000/api/health
```

Response:

```text
Spring Boot Application is Running!
```

## Docker Multi-Stage Build

### What is Docker Multi-Stage Build?

Docker Multi-Stage Build is a technique that allows us to use multiple `FROM` instructions in a single Dockerfile.

We use one stage to build the application and another stage to run the application.

This helps reduce the final Docker image size by excluding unnecessary build tools and dependencies from the runtime image.

### Dockerfile

```dockerfile
# Stage 1: Build Stage (Maven + JDK 17)
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests


# Stage 2: Runtime Stage (JRE 17)
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/student-app-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Explanation of Dockerfile

**Stage 1: Build Stage**

```dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS build
```
- Uses Maven with JDK 17.
- Provides the required Java and Maven tools to build the application.
- `AS build` gives this stage the name `build`.

```dockerfile
WORKDIR /app
```
- Creates and sets the working directory inside the container.

```dockerfile
COPY pom.xml .
COPY src ./src
```
- Copies the Maven configuration and application source code into the image.

```dockerfile
RUN mvn clean package -DskipTests
```
- Cleans previous build output.
- Compiles the Java application.
- Packages the application into an executable JAR file.
- Skips tests during this Docker build.

**Stage 2: Runtime Stage**

```dockerfile
FROM eclipse-temurin:17-jre
```
- Uses a lightweight Java Runtime Environment.
- Does not include Maven or the full JDK build tools.

```dockerfile
WORKDIR /app
```
- Sets the application working directory.

```dockerfile
COPY --from=build /app/target/student-app-1.0.0.jar app.jar
```
- Copies only the generated JAR file from the build stage.
- Does not copy the complete build environment.

```dockerfile
EXPOSE 8080
```
- Documents that the application listens on container port 8080.

```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
```
- Starts the Spring Boot application when the container runs.

## Why Do We Use Docker Multi-Stage Builds?

1. **Smaller Image Size:** Excludes Maven and unnecessary build tools from the final image.
2. **Improved Security:** Reduces the number of unnecessary tools available in the runtime container.
3. **Better Performance:** Smaller images can be transferred and deployed more efficiently.
4. **Separation of Concerns:** Separates application building and application execution.
5. **Easy Deployment:** Creates a portable image that can run consistently across environments.

## .dockerignore File

The `.dockerignore` file prevents unnecessary files from being copied into the Docker build context.

```text
.git
.gitignore
target/
README.md
```

## Build and Run the Docker Image

### 1. Build Docker Image

```bash
docker build -t student-app:v1 .
```

### 2. Verify Docker Image

```bash
docker images
```

### 3. Run Docker Container

```bash
docker run -d \
  -p 5000:8080 \
  --name student-container \
  student-app:v1
```

**Port Mapping:**

- Host port: `5000`
- Container port: `8080`

The Spring Boot application listens on port 8080 inside the container. We access it through port 5000 on the host machine.

### 4. Check Running Containers

```bash
docker ps
```

### 5. Check Container Logs

```bash
docker logs student-container
```

### 6. Test Application

```bash
curl http://localhost:5000/api/students
curl http://localhost:5000/api/health
```

## Useful Docker Commands

```bash
# List Docker images
docker images

# List running containers
docker ps

# List all containers
docker ps -a

# View container logs
docker logs student-container

# Stop container
docker stop student-container

# Start container
docker start student-container

# Remove container
docker rm -f student-container

# Remove Docker image
docker rmi student-app:v1
```

## Project Architecture

```text
          Developer
              |
              v
         Java Source Code
              |
              v
       Docker Multi-Stage Build
              |
       +------+------+
       |             |
       v             v
   Build Stage    Runtime Stage
   Maven + JDK       JRE 17
       |             ^
       v             |
   Generate JAR -----+
                     |
                     v
              Docker Image
                     |
                     v
              Docker Container
                     |
                     v
              Spring Boot API
                 Port 8080
                     |
                     v
              Host Port 5000
                     |
                     v
                  Client
```

## Key Learning Outcomes

- Created a Spring Boot REST API application.
- Understood Dockerfile instructions.
- Implemented Docker Multi-Stage Builds.
- Separated build and runtime environments.
- Created and managed Docker images and containers.
- Configured Docker port mapping.
- Tested REST APIs running inside a Docker container.

## Future Improvements

- Add a GitHub Actions CI/CD pipeline.
- Push Docker images to Docker Hub or Amazon ECR.
- Deploy the application on AWS EC2 or Amazon EKS.
- Add automated testing and security scanning.

## Author
CodeWithRaj2157
GitHub: https://github.com/CodeWithRaj2157
**CodeWithRaj2157**

GitHub: https://github.com/CodeWithRaj2157
