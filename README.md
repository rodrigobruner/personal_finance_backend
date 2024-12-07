## Personal finance

This innovative project empowers users to take control of their personal finances, offering an array of features designed for comprehensive financial management. Users can easily create, read, update, and delete (CRUD) bank accounts, as well as manage expense and income categories. In addition, the platform allows users to meticulously record their incomes, expenses, and transfers between different accounts. To enhance understanding, it summarizes total income and employs visually engaging graphs to illustrate expenses and income categorized by type.


The solution comprises two distinct projects: the back-end and the front-end. The back-end functions as a REST API built with Java Spring Boot, utilizing the Java Persistence API (JPA) to manage data in a MySQL database. Meanwhile, the front-end is crafted in TypeScript, employing Next.js as the framework and Material UI for the user interface components.

<a href='https://github.com/rodrigobruner/personal_finance_web_app'>Go to the front-end project</a>

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

2.1 Create the database, the model and SQL files is in /doc

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

Windows

1.	Open the Command Prompt.
2.	Navigate to the project directory.
3.	Run the command:

```sh
mvn spring-boot:run
```

Linux/Mac

1.	Open the Terminal.
2.	Navigate to the project directory.
3.	Run the command:

```sh
./mvnw spring-boot:run
```