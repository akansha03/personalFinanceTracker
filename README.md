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

## 🛠️ How to Run Locally

### Prerequisites
- JDK 17 or higher
- Maven 3.6+
- PostgreSQL 16 or Docker

### Option 1: Using Docker (Recommended)

1. **Start PostgreSQL Database:**
   ```bash
   docker-compose up -d
   ```

2. **Build and Run the Application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Option 2: Local PostgreSQL Setup

1. **Install PostgreSQL** and create a database:
   ```sql
   CREATE DATABASE pfin;
   CREATE USER pfin_user WITH PASSWORD 'password';
   GRANT ALL PRIVILEGES ON DATABASE pfin TO pfin_user;
   ```

2. **Update Database Configuration** in `src/main/resources/application.yml` if needed

3. **Run the Application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Application Configuration

The application will start on `http://localhost:8080` with the following default configuration:

- **Database**: PostgreSQL on localhost:5432
- **Database Name**: pfin
- **Username**: pfin_user
- **Password**: password
- **Flyway**: Enabled with automatic migrations

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
- **Mapping**: ModelMapper
- **Build Tool**: Maven
- **Java Version**: 17

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
