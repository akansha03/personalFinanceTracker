# Stage 1: build
FROM maven:3.9.2-eclipse-temurin-17 as builder
WORKDIR /build
COPY pom.xml mvnw ./
COPY .mvn .mvn
# download dependencies
RUN mvn -B -ntp -q dependency:go-offline

COPY src src
RUN mvn -B -ntp -DskipTests package

# Stage 2: runtime
FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=/build/target/*.jar
WORKDIR /app

# non-root user
RUN useradd -ms /bin/bash appuser
COPY --from=builder ${JAR_FILE} app.jar
RUN chown appuser:appuser app.jar
USER appuser

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]