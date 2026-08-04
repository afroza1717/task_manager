# Task API

A Spring Boot REST API application for task management with PostgreSQL database integration. The application can be run locally, using Docker, Docker Compose, or deployed on Kubernetes.

## Technology Stack

* Java 25
* Spring Boot
* Spring Data JPA / Hibernate
* PostgreSQL 16
* Maven
* Docker
* Kubernetes
* Kind (Kubernetes in Docker)
* kubectl
* Spring Boot Actuator

---

# Features

* Create tasks
* Retrieve tasks
* Update tasks
* Delete tasks
* PostgreSQL database persistence
* REST API endpoints
* Containerized deployment using Docker
* Kubernetes deployment with health probes
* Resource requests and limits configured

---

# Project Structure

```
task-api
│
├── src
│   ├── main
│   │   ├── java
│   │   └── resources
│   │
│   └── test
│
├── k8s
│   ├── namespace.yaml
│   ├── postgres-secret.yaml
│   ├── postgres-deployment.yaml
│   ├── postgres-service.yaml
│   ├── task-api-deployment.yaml
│   └── task-api-service.yaml
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

# Prerequisites

Install the following tools:

* Java 25
* Maven 3.8+
* Docker
* kubectl
* Kind

Verify installations:

```bash
java -version
```

```bash
mvn -version
```

```bash
docker --version
```

```bash
kubectl version --client
```

```bash
kind version
```

---

# Local Development

## Clone Repository

```bash
git clone https://github.com/<username>/task-api.git

cd task_manager
```

---

## Configure Database

Update database configuration in:

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/task_database
spring.datasource.username=myuser
spring.datasource.password=secret

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Run PostgreSQL Locally Using Docker

Start PostgreSQL:

```bash
docker run -d \
--name postgres-db \
-e POSTGRES_DB=task_database \
-e POSTGRES_USER=myuser \
-e POSTGRES_PASSWORD=password \
-p 5432:5432 \
postgres:16
```

Check PostgreSQL:

```bash
docker ps
```

---

## Run Application Locally

Build application:

```bash
mvn clean install
```

Run Spring Boot:

```bash
mvn spring-boot:run
```

Application starts:

```
http://localhost:8080
```

---

# Running Tests

Execute unit tests:

```bash
mvn test
```

Run full build:

```bash
mvn clean install
```

---

# Run Using Docker

## Build Docker Image

```bash
docker build -t task-api:latest .
```

Verify image:

```bash
docker images
```

---

## Run Docker Container

```bash
docker run -d \
--name task-api \
-p 8080:8080 \
task-api:latest
```

Check container:

```bash
docker ps
```

View logs:

```bash
docker logs task-api
```

Application:

```
http://localhost:8080
```

---

# Run Using Docker Compose

Start services:

```bash
docker compose up -d
```

Check services:

```bash
docker compose ps
```

View logs:

```bash
docker compose logs -f
```

Stop services:

```bash
docker compose down
```

---

# Kubernetes Deployment (Kind)

## Create Kind Cluster

```bash
kind create cluster --name task-cluster
```

Verify:

```bash
kubectl cluster-info
```

---

## Load Docker Image Into Kind

Build image:

```bash
docker build -t task-api:latest .
```

Load image:

```bash
kind load docker-image task-api:latest --name task-cluster
```

---

## Deploy Application

Apply Kubernetes manifests:

```bash
kubectl apply -f k8s/
```

---

## Verify Deployment

Check namespace:

```bash
kubectl get namespace
```

Check pods:

```bash
kubectl get pods -n task-app
```

Check services:

```bash
kubectl get services -n task-app
```

Check all resources:

```bash
kubectl get all -n task-app
```

---

# Kubernetes Health Checks

The application uses Spring Boot Actuator probes:

## Startup Probe

Checks application startup:

```
/actuator/health
```

## Readiness Probe

Determines if the pod can receive traffic:

```
/actuator/health/readiness
```

## Liveness Probe

Restarts unhealthy containers:

```
/actuator/health/liveness
```

---

# Kubernetes Resource Management

The application container is configured with:

```yaml
resources:
  requests:
    cpu: "500m"
    memory: "512Mi"

  limits:
    cpu: "1"
    memory: "1Gi"
```

---

# Access Application From Kubernetes

Port forward service:

```bash
kubectl port-forward service/task-api 8080:8080 -n task-app
```

Access:

```
http://localhost:8080
```

---

# Database Access

Connect to PostgreSQL pod:

```bash
kubectl exec -it \
-n task-app \
deployment/postgres \
-- psql -U myuser -d task_database
```

List tables:

```sql
\dt
```

View data:

```sql
SELECT * FROM task;
```

---

# API Endpoints

## Create Task

```
POST /create_task
```

Example:

```json
{
  "title": "Learn Kubernetes",
  "author": "John",
  "status": "OPEN",
  "description": "Deploy Spring Boot application"
}
```

---

## Get Task

```
GET /task_list/{taskId}
```

---

## Update Task

```
PUT /update_task/{taskId}
```

---

## Delete Task

```
DELETE /delete_task/{taskId}
```

---

# Cleanup Kubernetes

Delete resources:

```bash
kubectl delete -f k8s/
```

Delete Kind cluster:

```bash
kind delete cluster --name task-cluster
```

---

# Author

Your Name

# License

This project is for learning and demonstration purposes.
