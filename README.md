# Food Ordering System - Backend API

This repository contains the backend API for a complete full-stack food ordering application. It is built with Java, Spring Boot, and Spring Security to provide a robust, secure, and scalable RESTful API.

The API handles all core business logic, including user authentication (JWT), restaurant and food management, user shopping carts, order processing, and payment integration with Stripe.

## 💻 Tech Stack

* **Backend:** Java, Spring Boot, Spring MVC
* **Security:** Spring Security (with JWT Authentication)
* **Database:** MySQL
* **Payments:** Stripe API
* **Build Tool:** Maven

---

## ✨ Features

* **User Authentication:** Secure user registration and login with JWT.
* **User Profiles:** Users can view and manage their profile information.
* **Restaurant API:** Full CRUD (Create, Read, Update, Delete) functionality for restaurants.
* **Food API:** Full CRUD for food items, linked to specific restaurants.
* **Menu Management:** Functionality for managing food categories and ingredients.
* **Shopping Cart:** A complete cart system to add, remove, and view items before checkout.
* **Order API:** System for creating, viewing, and managing user orders.
* **Payment Integration:** Secure and user-friendly payment processing using Stripe.

---

## 🚀 Getting Started

To get the backend server up and running on your local machine, follow these simple steps.

### Prerequisites

You will need the following tools installed on your machine:
* Java (JDK 17 or newer)
* Apache Maven
* MySQL Server
* A Stripe account (to get your API keys)

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/your-username/your-repository.git](https://github.com/your-username/your-repository.git)
    ```
2.  **Navigate to the project directory:**
    ```sh
    cd your-repository-name
    ```
3.  **Configure your database and environment variables:**
    Open `src/main/resources/application.properties`. The project is configured to use environment variables for sensitive data.

    ```properties
   
    spring.datasource.url=jdbc:mysql://${MYSQL_HOST}?createDatabaseIfNotExist=true
    spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
    spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
    server.port=${SERVER_PORT}
    
    # JWT Configuration
    jwt.secret=${JWT_SECRET}
    jwt.expiration=${JWT_EXPIRATION}
    ```
    You will need to set these environment variables in your operating system or IDE before running the application.

4.  **Run the application:**
    ```sh
    mvn spring-boot:run
    ```
    The backend server will start and be accessible at `http://localhost:8080` (or the `SERVER_PORT` you defined).

---

## 📚 API Endpoints

Here is a brief overview of the main API endpoints available. You can test these using a tool like Postman or Insomnia.

* `POST /auth/register`: Register a new user.
* `POST /auth/login`: Authenticate a user and receive a JWT.
* `GET /api/user/profile`: Get the logged-in user's profile.
* `GET /api/restaurants`: Fetch all restaurants.
* `GET /api/food/restaurant/{id}`: Fetch all food items for a specific restaurant.
* `POST /api/cart/add`: Add an item to the cart.
* `GET /api/cart`: View the user's cart.
* `POST /api/orders/create`: Create a new order.
* `GET /api/orders/user`: Get all orders for the logged-in user.
