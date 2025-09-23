# Personal Finance Tracker

A robust Spring Boot REST API for managing personal finances. This application allows users to track income and expenses by category, date, and account type, providing a solid foundation for personal financial management.

## 🎯 Project Overview

Personal Finance Tracker is a backend REST API built with Spring Boot that helps users manage their personal finances effectively. It provides endpoints for:

- **Category Management**: Create and retrieve expense/income categories
- **Transaction Tracking**: Add, update, and delete financial transactions
- **Financial Analytics**: Calculate totals by date and transaction type
- **Data Organization**: Categorize transactions with custom categories and predefined enums

The application uses PostgreSQL for data persistence with Flyway migrations, ensuring reliable database schema management.

## 📁 Project Structure

```
src/main/java/com/finance/personalTracker/
├── controller/          # REST API endpoints
│   ├── CategoryController.java
│   └── TransactionController.java
├── service/             # Business logic layer
│   ├── CategoryService.java
│   ├── TransactionService.java
│   └── impl/           # Service implementations
├── repositories/        # JPA repositories for data access
│   ├── CategoryRepo.java
│   └── TransactionRepo.java
├── dto/                # Data Transfer Objects for API requests/responses
│   ├── CategoryDTO.java
│   ├── TransactionDTO.java
│   ├── AmountDTO.java
│   └── UpdateTransactionDTO.java
├── entity/             # JPA entities mapped to database tables
│   ├── Category.java
│   └── Transaction.java
├── enums/              # Enumeration classes
│   ├── CategoryType.java
│   ├── Account.java
│   ├── ExpenseCategory.java
│   └── IncomeCategory.java
├── exception/          # Custom exception classes
└── config/            # Configuration classes
    └── ModelMapperConfig.java
```

## 🚀 API Documentation

### Categories API

#### Create Category
```http
POST /api/categories
Content-Type: application/json

{
  "name": "Groceries",
  "type": "Expense"
}
```

**Response:**
```json
{
  "name": "Groceries",
  "type": "Expense"
}
```

#### Get Categories by Type
```http
GET /api/categories/Expense
```

**Response:**
```json
[
  {
    "name": "Groceries",
    "type": "Expense"
  },
  {
    "name": "Transportation",
    "type": "Expense"
  }
]
```

#### Get All Categories
```http
GET /api/categories
```

**Response:**
```json
[
  {
    "name": "Salary",
    "type": "Income"
  },
  {
    "name": "Groceries",
    "type": "Expense"
  }
]
```

### Transactions API

#### Create Transaction
```http
POST /api/transactions
Content-Type: application/json

{
  "date": "2024-01-15",
  "amount": 1500.00,
  "type": "Income",
  "category": {
    "id": 1,
    "name": "Salary",
    "type": "Income"
  },
  "account": "BANK",
  "note": "Monthly salary",
  "description": "January 2024 salary payment"
}
```

**Response:**
```json
{
  "transactionId": "550e8400-e29b-41d4-a716-446655440000",
  "date": "2024-01-15",
  "amount": 1500.00,
  "type": "Income",
  "category": {
    "id": 1,
    "name": "Salary",
    "type": "Income"
  },
  "account": "BANK",
  "note": "Monthly salary",
  "description": "January 2024 salary payment",
  "imageUrl": null
}
```

#### Update Transaction
```http
PUT /api/transactions/550e8400-e29b-41d4-a716-446655440000
Content-Type: application/json

{
  "amount": 1600.00,
  "account": "BANK",
  "note": "Updated salary amount",
  "description": "January 2024 salary with bonus"
}
```

#### Get Total Amount
```http
GET /api/transactions/total?date=2024-01-15&type=Income
```

**Response:**
```json
{
  "amount": 1600.00
}
```

**Get Net Amount (Income - Expenses):**
```http
GET /api/transactions/total?date=2024-01-15
```

**Response:**
```json
{
  "amount": 500.00
}
```

#### Delete Transaction
```http
DELETE /api/transactions/550e8400-e29b-41d4-a716-446655440000
```

## 🗄️ Database Setup

The application uses PostgreSQL with Flyway migrations for database schema management.

### Database Schema

**Categories Table:**
- `id` (BIGSERIAL PRIMARY KEY)
- `name` (VARCHAR(100) NOT NULL)
- `type` (VARCHAR(10) NOT NULL) - 'Income' or 'Expense'

**Transactions Table:**
- `transaction_id` (UUID PRIMARY KEY) - Auto-generated using `gen_random_uuid()`
- `date` (TIMESTAMPTZ NOT NULL)
- `amount` (NUMERIC(12,2) NOT NULL)
- `type` (VARCHAR(10) NOT NULL) - 'Income' or 'Expense'
- `category_id` (BIGINT REFERENCES categories(id))
- `account` (VARCHAR(30))
- `note` (TEXT)
- `image_url` (TEXT)
- `description` (TEXT)
- `created_at` (TIMESTAMPTZ NOT NULL)
- `updated_at` (TIMESTAMPTZ NOT NULL)

### Key Features
- **UUID Generation**: Transaction IDs are automatically generated using PostgreSQL's `gen_random_uuid()`
- **Automatic Timestamps**: `created_at` and `updated_at` are automatically managed
- **Data Validation**: Amount must be non-negative, types are constrained to 'Income'/'Expense'
- **Indexes**: Optimized queries with indexes on date, type, and category_id

## 🛠️ Setup and Running Instructions

### Prerequisites
- **Java**: JDK 17 (required)
- **Maven**: 3.6+ 
- **Docker**: For database or full containerized setup
- **PostgreSQL**: 16+ (if running database locally)

### 🚀 Quick Start (Recommended)

#### Option 1: Full Docker Setup (Easiest)

Run everything with Docker - both database and application:

```bash
# Clone and navigate to project
git clone <repository-url>
cd personalTracker

# Start everything with Docker
docker-compose up

# Application will be available at http://localhost:8080
```

#### Option 2: Local Development Setup

For local development with Docker database:

```bash
# 1. Setup Java 17 environment (macOS with Homebrew)
brew install openjdk@17
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

# 2. Start PostgreSQL database only
docker-compose up -d db

# 3. Run the application locally
mvn clean compile
mvn spring-boot:run

# Application will be available at http://localhost:8080
```

**Or use the provided setup script:**
```bash
# Make script executable
chmod +x setup-local.sh

# Run setup script
./setup-local.sh

# Then run the application
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
mvn spring-boot:run
```

### 🔧 Detailed Setup Instructions

#### Java 17 Setup

**macOS (using Homebrew):**
```bash
# Install Java 17
brew install openjdk@17

# Set environment variables (add to ~/.zshrc for persistence)
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

# Verify installation
java -version
```

**Linux (Ubuntu/Debian):**
```bash
# Install OpenJDK 17
sudo apt update
sudo apt install openjdk-17-jdk

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

**Windows:**
1. Download OpenJDK 17 from [Adoptium](https://adoptium.net/)
2. Install and set `JAVA_HOME` environment variable
3. Add `%JAVA_HOME%\bin` to your PATH

#### Database Setup Options

**Option A: Docker Database (Recommended for Development)**
```bash
# Start PostgreSQL container
docker-compose up -d db

# Verify database is running
docker exec postgres-pfin psql -U pfin_user -d pfin -c "SELECT 1;"
```

**Option B: Local PostgreSQL Installation**
```bash
# Install PostgreSQL (macOS)
brew install postgresql@16
brew services start postgresql@16

# Create database and user
psql postgres
CREATE DATABASE pfin;
CREATE USER pfin_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE pfin TO pfin_user;
\q
```

### 📁 Configuration Files

The project uses Spring profiles for different environments:

- **`application.yml`** - Default configuration (uses local profile)
- **`application-local.yml`** - Local development configuration
- **`application-docker.yml`** - Docker environment configuration

#### Local Development Configuration
```yaml
# application-local.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/pfin
    username: pfin_user
    password: password
```

#### Docker Configuration
```yaml
# application-docker.yml  
spring:
  datasource:
    url: jdbc:postgresql://db:5432/pfin
    username: pfin_user
    password: password
```

### 🏃‍♂️ Running the Application

#### Local Development
```bash
# Set Java 17 environment
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

# Start database
docker-compose up -d db

# Run application
mvn spring-boot:run

# Or run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

#### Docker Deployment
```bash
# Build and run everything
docker-compose up --build

# Or run in background
docker-compose up -d
```

#### Production Build
```bash
# Build JAR file
mvn clean package

# Run JAR
java -jar target/personalFinanceTracker-0.0.1-SNAPSHOT.jar
```

### 🔍 Verifying Setup

1. **Check Application Health:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

2. **Test Categories API:**
   ```bash
   curl http://localhost:8080/api/categories
   ```

3. **Check Database Connection:**
   ```bash
   # If using Docker database
   docker exec postgres-pfin psql -U pfin_user -d pfin -c "SELECT COUNT(*) FROM categories;"
   ```

### 🐛 Troubleshooting

#### Common Issues

**Java Version Issues:**
```bash
# Check current Java version
java -version

# If not Java 17, set correct environment
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
```

**Database Connection Issues:**
```bash
# Check if PostgreSQL container is running
docker ps | grep postgres

# Check database logs
docker logs postgres-pfin

# Restart database if needed
docker-compose restart db
```

**MapStruct Compilation Issues:**
```bash
# Clean and recompile to generate MapStruct implementations
mvn clean compile

# Check if generated classes exist
ls target/generated-sources/annotations/com/finance/personalTracker/dto/
```

**Port Conflicts:**
```bash
# Check if port 8080 is in use
lsof -i :8080

# Kill process if needed
kill -9 <PID>
```

### 📊 Application Configuration

The application starts with these default settings:

- **Port**: 8080
- **Database**: PostgreSQL on localhost:5432 (local) or db:5432 (Docker)
- **Database Name**: pfin
- **Username**: pfin_user
- **Password**: password
- **Flyway**: Enabled with automatic migrations
- **Profiles**: 
  - `local` - For local development
  - `docker` - For Docker environment

## 🧪 Testing

The project includes comprehensive testing with JUnit 5 and Mockito:

- **Unit Tests**: Service layer testing with mocked dependencies
- **Repository Tests**: Database integration tests using `@DataJpaTest`
- **Controller Tests**: REST API endpoint testing

Run tests with:
```bash
mvn test
```

## 🔧 Technology Stack

- **Framework**: Spring Boot 3.5.4
- **Database**: PostgreSQL 16
- **ORM**: Spring Data JPA with Hibernate
- **Migration**: Flyway
- **Validation**: Jakarta Validation API
- **Mapping**: MapStruct + ModelMapper
- **Build Tool**: Maven
- **Java Version**: 17
- **Containerization**: Docker & Docker Compose
- **Profiles**: Spring Profiles (local, docker)

## 🚧 Future Improvements

### Planned Features
- **Authentication & Authorization**: Spring Security with JWT tokens
- **Health Monitoring**: Spring Boot Actuator for application health checks
- **Budget Management**: Set and track spending limits by category
- **Recurring Transactions**: Automatically create periodic transactions
- **Financial Reports**: Monthly/yearly summaries and analytics
- **Data Export**: CSV/PDF export functionality
- **Multi-User Support**: User management and data isolation

### Technical Enhancements
- **API Documentation**: OpenAPI/Swagger documentation
- **Caching**: Redis integration for improved performance
- **Audit Logging**: Track changes to transactions and categories
- **Rate Limiting**: API rate limiting for security
- **Containerization**: Docker image for easy deployment

## 📝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Note**: This is an MVP (Minimum Viable Product) version focused on core functionality. The application provides a solid foundation for personal finance tracking with room for future enhancements and scaling.
