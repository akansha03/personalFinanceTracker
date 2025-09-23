#!/bin/bash

# Setup script for local development
echo "Setting up local development environment..."

# Set Java 17 environment
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

echo "Java version:"
java -version

# Start PostgreSQL database using Docker
echo "Starting PostgreSQL database..."
docker-compose up -d db

# Wait for database to be ready
echo "Waiting for database to be ready..."
sleep 10

# Test database connection
echo "Testing database connection..."
docker exec postgres-pfin psql -U pfin_user -d pfin -c "SELECT 1;" > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo "✅ Database is ready!"
else
    echo "❌ Database connection failed. Please check Docker and try again."
    exit 1
fi

echo "✅ Local development environment is ready!"
echo ""
echo "To run the application locally:"
echo "1. Make sure PostgreSQL is running: docker-compose up -d db"
echo "2. Run the Spring Boot app: mvn spring-boot:run"
echo ""
echo "To run everything with Docker:"
echo "docker-compose up"
