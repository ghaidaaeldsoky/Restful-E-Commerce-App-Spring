# 🧴 MISK – E-Commerce Backend for Perfume Store

✨ **MISK** is a full-stack e-commerce platform for selling perfumes. This repository contains only the **backend project**; the frontend is implemented separately in an **Angular project**: [MISK Ecommerce Angular Frontend](https://github.com/ghaidaaeldsoky/MiskApp-frontend.git)

---

## 🚀 Features

- 🔐 **Authentication & Authorization:** Secure JWT-based login with role-based access (User/Admin).
- 👤 **User Profile Management:** View and edit personal information.  
- 📦 **Product Management:** View all products, product details, stock tracking, filtering by name or category, and pagination.
- 🛒 **Cart & Billing:** Add/remove product quantities, real-time total cost updates.
- 🧾 **Order Management:** Cash or Visa payment, payment validation, automatic confirmation emails.
- 🛠️ **Admin Dashboard:** Manage products, users, and orders efficiently.
- 📝 **API Documentation:** All endpoints documented using Swagger / Springdoc OpenAPI.

---

## 🧰 Tech Stack

- **Language:** Java 21  
- **Frameworks:** Spring Boot (including MVC, AOP, Security)  
- **Database & ORM:** MySQL, JPA/Hibernate  
- **Build & Dependency Management:** Maven  
- **Libraries & Tools:** REST APIs, Lombok, MapStruct, JWT, Swagger/OpenAPI, Spring Mail
- **Logging:** SLF4J + Logback

---

## ✅ Prerequisites

Before running the project, make sure you have the following installed:

- Java 21
- MySQL
- Maven

---

## ⚡ Getting Started

Follow these steps to run the backend locally:

1. **Clone the repository**:
```bash
git clone https://github.com/ghaidaaeldsoky/Restful-E-Commerce-App-Spring.git
cd Restful-E-Commerce-App-Spring
```

2. **Create a MySQL database** (e.g., `misk_db`) and update the database configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/misk_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

3. **Build and run the application** using Maven:

```bash
mvn clean install
mvn spring-boot:run
```
> The application runs on port **8085** by default.

4. **Access the application** at: [http://localhost:8085](http://localhost:8085)

5. **Test the REST APIs**
   - Use **Postman** or any REST client.
   - API documentation is available via Swagger: http://localhost:8085/swagger-ui/index.html

---
