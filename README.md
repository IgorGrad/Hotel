# Hotel Data Management Application

## Project Overview

This application provides:

1. **CRUD Interface** for hotel data management. Required hotel data include:
    - **Hotel Name**: The name of the hotel.
    - **Hotel Price**: The cost to stay at the hotel.
    - **Hotel Geo-location**: The location of the hotel (latitude and longitude).

2. **Search Interface** that allows users to search for hotels based on their current location. It returns a list of hotels ordered by proximity and price.
    - **Input**: User’s current geolocation (latitude and longitude).
    - **Output**: A list of hotels that includes:
        - Hotel name.
        - Hotel price.
        - The distance of the hotel from the user’s current location.
    - The results are ordered such that hotels that are cheaper and closer to the user’s location appear at the top, while more expensive and distant hotels appear at the bottom.

3. **Paging** The search interface supports pagination to handle large result sets efficiently.

---

## Technology Stack

The application is built using the following technologies:

- **Java 17**: The core programming language used for development.
- **Spring Boot**: A Java-based framework used for building REST APIs and backend services.
    - **Spring Web**: Used for building the RESTful services.
    - **Spring Validation**: For validating input data in DTOs.
- **Jakarta Validation API**: Used for validating the input objects such as `HotelModificationDTO`.
- **Lombok**: To reduce boilerplate code for getters, setters, constructors, and logging.
- **Swagger & Springdoc-OpenAPI**: For API documentation and interaction.
- **MapStruct**: For mapping between entity and DTO objects.
- **Maven**: The build tool used to manage dependencies and build the project.
- **JUnit**: For unit testing.
- **Spring Data JPA**: For data access and persistence.

---
## Persistence Layer

The application employs **Spring Data JPA** to manage the persistence layer, enabling easy interaction with the underlying database. The key features include:

- **Repositories**: Custom repository interfaces extend `JpaRepository` to provide CRUD operations for the `Hotel` entity, allowing for simple data access without boilerplate code.

- **Entity Mapping**: The `Hotel` entity is mapped to the corresponding database table, ensuring that the application reflects the database schema accurately.

- **Query Methods**: Method names in the repository interfaces can be used to automatically generate SQL queries based on naming conventions, simplifying data retrieval.

- **Soft Delete Support**: The application supports soft deletion of hotel records through an AOP-based approach, marking entities as deleted without removing them from the database.

---

## API Documentation (Swagger)

This project uses **Swagger** for API documentation and interactive testing. Swagger helps developers and users understand the structure of the API and test the various endpoints in a user-friendly interface.

### How to Use Swagger

1. **Accessing the Swagger UI**:
    - After running the application, Swagger UI will be available at:
      ```
      http://localhost:8080/api/swagger-ui/index.html#/
      ```
    - Swagger UI provides an interactive API documentation where you can test all the API endpoints directly from your browser.

2. **Swagger Endpoints**:
    - **GET /hotels**: Fetches a list of all hotels.
    - **POST /hotels**: Adds a new hotel using the provided hotel details.
    - **GET /hotels/search**: Searches for hotels based on the user's current geolocation.

3. **Testing API Endpoints**:
    - Use the Swagger interface to send test requests, inspect responses, and review input/output formats.
    - Each API method will include the following details:
        - **Parameters**: Any input parameters needed for the API call.
        - **Responses**: The possible response codes (e.g., 200 OK, 400 Bad Request, 404 Not Found) along with the response body.

4. **Swagger JSON/YAML**:
    - You can also access the raw OpenAPI documentation at the following URL if needed for external integration:
        - JSON format:
          ```
          http://localhost:8080/api/v3/api-docs
          ```

---

## How to Run the Project

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repo/hotel-management-app.git
   ```

2. **Navigate to the project directory**:
   ```bash
   cd hotel-management-app
   ```
3. **Set up PostgreSQL Database**: 
    - **Database configuration**: The application is set up to use PostgreSQL as its database. You will need to have a PostgreSQL database instance running. The database settings are defined in the application.properties file, including the necessary configurations such as driver class name, URL, port, username, and password.
    - **Install PostgreSQL**: Make sure you have PostgreSQL installed on your computer. If you need assistance with the installation, check the official PostgreSQL installation guide online.
    - **Create a New Database**: For the application, create a new database. For example, to create a database named `hotel-db`, you can execute the following command in the PostgreSQL command line or use a graphical tool like pgAdmin:
      ```sql
      CREATE DATABASE hotel-db;
      ```
    - **Create a User (if necessary)**: If required, create a user and set a password. You can execute the following SQL commands to create a user named postgres and grant the necessary privileges:
      ```sql
      CREATE USER postgres WITH PASSWORD 'password';
      GRANT ALL PRIVILEGES ON DATABASE hotel-db TO postgres;
      ```
4. **Build the project**:
   ```bash
   mvn clean install
   ```

5. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

6. **Access the application**:
    - Swagger UI: `hhttp://localhost:8080/api/swagger-ui/index.html#/`
    - Hotel API Endpoints will be accessible under the `/api/hotels` path.

---

## Features

- **CRUD Operations**: Add, update, and delete hotel information.
- **Search Interface**: Find hotels based on user location.
- **Pagination Support**: Handle large datasets with efficient paging of results.
- **Validation**: Strong validation to ensure data integrity.
- **Modular Design**: Designed with modularity in mind, allowing for easy integration with persistent storage in the future.

---
