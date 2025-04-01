# Unicorn Manager Application

A Spring Boot application for managing unicorns, built with Java 8 and Maven.

## Features

- Create, read, update, and delete unicorns
- Search unicorns by color, age, and name
- RESTful API endpoints
- In-memory H2 database for development
- Comprehensive unit tests

## Tech Stack

- Java 8
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database
- JUnit 5
- Mockito
- Lombok
- Maven

## Project Structure

```
UnicornDemo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── unicorn/
│   │   │           └── manager/
│   │   │               ├── controller/
│   │   │               ├── exception/
│   │   │               ├── model/
│   │   │               ├── repository/
│   │   │               ├── service/
│   │   │               └── UnicornManagerApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── unicorn/
│                   └── manager/
│                       ├── controller/
│                       ├── repository/
│                       ├── service/
│                       └── UnicornManagerApplicationTests.java
└── pom.xml
```

## Getting Started

### Prerequisites

- Java 8 JDK
- Maven 3.6+

### Building the Application

```bash
mvn clean install
```

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080 by default.

### Accessing the H2 Console

The H2 database console is available at:

```
http://localhost:8080/h2-console
```

Connection details:
- JDBC URL: `jdbc:h2:mem:unicorndb`
- Username: `sa`
- Password: `password`

## API Endpoints

### Unicorn Management

- `GET /api/unicorns` - Get all unicorns
- `GET /api/unicorns/{id}` - Get unicorn by ID
- `POST /api/unicorns` - Create a new unicorn
- `PUT /api/unicorns/{id}` - Update an existing unicorn
- `DELETE /api/unicorns/{id}` - Delete a unicorn

### Search Operations

- `GET /api/unicorns/color/{color}` - Find unicorns by color
- `GET /api/unicorns/young/{age}` - Find unicorns younger than specified age
- `GET /api/unicorns/search?name={nameFragment}` - Search unicorns by name

## Running Tests

```bash
mvn test
```

## Sample Unicorn JSON

```json
{
  "name": "Sparkles",
  "color": "Pink",
  "age": 5,
  "magicalAbility": "Rainbow Generation",
  "birthDate": "2020-01-01"
}
```
