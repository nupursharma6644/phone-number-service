# Phone Number Service

## Overview

A RESTful API for managing customer phone numbers in a telecom system. Built with Java and Spring Boot.

## Features

- Retrieve all phone numbers
- Retrieve phone numbers by customer ID
- Activate a phone number

## API Endpoints

### `GET /v{version}/phoneNumbers`

Fetch all phone numbers.

### `GET /v{version}/customers/{customerId}/phone-numbers`

Fetch phone numbers associated with a specific customer.

### `PATCH /v{version}/phone-numbers/{phoneNumber}/activate`

Activate a specified phone number.

## Technologies

- Java 17+
- Spring Boot
- Gradle
- Springdoc

## Setup

Clone the repository:

```bash
git clone https://github.com/nupursharma6644/phone-number-service.git
 ```

Navigate to the project directory:
```bash
cd phone-number-service
```
Build and run the application:
```bash
./gradlew bootRun
```
Access the API at http://localhost:8080

## Testing
Run unit and integration tests:
```bash
./gradlew test
```

## API Documentation

You can explore and test the REST APIs using Swagger UI:

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- The UI provides interactive documentation for all available endpoints, including request and response models.
