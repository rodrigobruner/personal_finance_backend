# Personal Finance API

This project is a Personal Finance API built with Spring Boot. It provides endpoints to manage personal finance data.

## Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher
- MySQL database

## Getting Started

### 1. Clone the Repository

```sh
git clone git@github.com:rodrigobruner/personal_finance_backend.git
cd ./personal_finance_backend
````

### 2. Configure the Database

2.1 Create the database, the model is in /doc

2.2 Rename the src/main/resources/application.properties-sample to src/main/resources/application.properties file and make your database configuration:

```sh
# Database configuration
spring.datasource.url=jdbc:mysql://[IP/HOST]:[PORT]/[DATABASE]ces?useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=[USERNAME]
spring.datasource.password=[PASSWORD]
````

### 3. Running the Back-End Project

3.1. Install Dependencies:
    * Navigate to the back-end project directory and install the dependencies using Maven:

```sh
mvn clean install
```

3.2. Run the application:
    * Run the Spring Boot application:

```sh
mvn spring-boot:run
```