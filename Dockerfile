# ---------- Build Stage ----------
FROM maven:3.9.11-eclipse-temurin-25 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN useradd -ms /bin/bash spring

USER spring

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]