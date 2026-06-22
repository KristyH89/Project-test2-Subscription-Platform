# 📦 Subscription API — Telecom Subscription Management System

## 📑 Table of Contents

- [Overview](#overview)
- [Project Instructions](#-project-instructions)
- [API Documentation](#-api-documentation-swagger)
- [Project Structure](#-project-structure)
- [Features](#️-features)
- [Seed Data](#-seed-data)
- [Authentication Flow](#-authentication-flow)
- [Roles](#roles)
- [Endpoints Overview](#-endpoints-overview)
- [Business Rules](#️-business-rules)
- [How to Run](#️-how-to-run)
- [Project Status](#-project-status)
- [Tech Stack](#-tech-stack)

---

## Overview

A Spring Boot–based backend application for managing telecom operators, subscription plans, customers, and subscriptions.
The system uses JWT authentication, role‑based authorization, DTO mapping, validation, and a clean service‑layer architecture.

This project was built as part of a backend workshop and demonstrates professional API design with full Swagger documentation.

---

## 📘 Project Instructions

👉 [View project instructions](ProjectTest2-Subscription-Platform.md)

---

## 📘 API Documentation (Swagger)

Once the application is running, open:

`http://localhost:8080/swagger-ui/index.html`

Use the **Authorize** button and enter:

`Bearer <your_access_token>`

---

## 📁 Project Structure

```
se.lexicon.subscriptionapi
│
├── config/                 # Security config, Swagger config, seed data
├── controller/             # REST controllers (Auth, Customer, Plan, Operator, Subscription)
├── domain/
│   ├── entity/             # JPA entities
│   ├── enums/              # ServiceType, SubscriptionStatus
│   └── constant/           # Role
│
├── dto/
│   ├── request/            # Incoming request DTOs
│   └── response/           # Outgoing response DTOs
│
├── exception/              # Custom exceptions + GlobalExceptionHandler
├── mapper/                 # Entity <-> DTO mappers
├── repository/             # Spring Data JPA repositories
├── security/               # JWT provider, filters, blacklist
├── service/                # Service interfaces
│   └── impl/               # Service implementations
│
└── SubscriptionApiApplication.java
```

---

## ⚙️ Features

### 🔐 Authentication & Authorization
- JWT login & logout
- Token blacklist on logout (Redis)
- Role‑based access (USER / ADMIN)
- Public registration endpoint

### 🏢 Operator Management
- Create operator (ADMIN)
- Search by name
- Get all operators

### 📦 Plan Management
- Create, update, delete plans (ADMIN)
- Get all plans, active and inactive (ADMIN)
- Get active plans (USER/ADMIN)
- Filter active plans by service type (USER/ADMIN)
- Get plans by operator (USER/ADMIN)

### 📄 Subscription Management
- Create subscription
- Cancel subscription
- Change plan (same operator + same service type)
- View own subscriptions
- Admin: view all subscriptions, delete subscriptions

### 👤 Customer Management
- Register customer
- Update profile
- Get customer by ID or email
- Admin: list all customers

---

## 🌱 Seed Data

On startup, the application automatically seeds the following data.

### Users

| Role  | Email | Password |
| --- | --- | --- |
| ADMIN | admin@example.com | password |
| USER | user@example.com | password |

### Operators

| ID | Name |
| --- | --- |
| 1 | FiberNet |
| 2 | MobilePlus |

### FiberNet Plans

| Name | Price (SEK) | Type | Active |
| --- | --- | --- | --- |
| Fiber 50 | 299.99 | INTERNET | true |
| Fiber 100 | 399.99 | INTERNET | true |
| Fiber 300 | 599.99 | INTERNET | false |

### MobilePlus Plans

| Name | Price (SEK) | Type | Data Limit | Active |
| --- | --- | --- | --- | --- |
| Mobile Basic | 149.99 | MOBILE | 5000 MB | true |
| Mobile Plus | 249.99 | MOBILE | 15000 MB | true |
| Mobile Unlimited | 349.99 | MOBILE | unlimited | false |

---

## 🔐 Authentication Flow

### Login

`POST /api/v1/auth/login`

Request:
```json
{
  "email": "admin@example.com",
  "password": "password"
}
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

### Register

`POST /api/v1/auth/register`

### Logout

`POST /api/v1/auth/logout`
→ Requires a valid JWT. Token is blacklisted in Redis until it expires.

---

<h2 id="roles">🧑‍💼 Roles</h2>

### USER
- View active plans
- Subscribe / cancel / change plan
- View own subscriptions
- View plans by operator

### ADMIN
Everything USER can do, plus:
- Manage operators
- Manage plans (create, update, delete, view inactive plans)
- View all customers
- View / delete all subscriptions

---

## 📚 Endpoints Overview

### 🔑 Auth

| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /api/v1/auth/login | Public |
| POST | /api/v1/auth/register | Public |
| POST | /api/v1/auth/logout | USER/ADMIN |

### 👤 Customers

| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /api/v1/customers | Public |
| GET | /api/v1/customers/{id} | USER/ADMIN |
| GET | /api/v1/customers/email/{email} | USER/ADMIN |
| GET | /api/v1/customers | USER/ADMIN |
| PUT | /api/v1/customers/{id}/profile | USER/ADMIN |

### 🏢 Operators

| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /api/v1/operators | ADMIN |
| GET | /api/v1/operators/{id} | USER/ADMIN |
| GET | /api/v1/operators/search?name= | USER/ADMIN |
| GET | /api/v1/operators | USER/ADMIN |

### 📦 Plans

| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /api/v1/plans | ADMIN |
| PUT | /api/v1/plans/{id} | ADMIN |
| DELETE | /api/v1/plans/{id} | ADMIN |
| GET | /api/v1/plans | ADMIN |
| GET | /api/v1/plans/{id} | USER/ADMIN |
| GET | /api/v1/plans/active | USER/ADMIN |
| GET | /api/v1/plans/active/type?serviceType= | USER/ADMIN |
| GET | /api/v1/plans/operator/{operatorId} | USER/ADMIN |

### 📄 Subscriptions

| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /api/v1/subscriptions | USER/ADMIN |
| GET | /api/v1/subscriptions/{id} | USER/ADMIN |
| GET | /api/v1/subscriptions/customer/{customerId} | USER/ADMIN |
| GET | /api/v1/subscriptions | ADMIN |
| DELETE | /api/v1/subscriptions/{id} | ADMIN |
| PUT | /api/v1/subscriptions/{id}/cancel | USER/ADMIN |
| PUT | /api/v1/subscriptions/{id}/change-plan/{newPlanId} | USER/ADMIN |

---

## ⚙️ Business Rules

### Subscription Creation
- Customer must exist
- Plan must exist and be active
- A customer cannot subscribe twice to the same plan
- A customer may have at most one **active** subscription per service type (Internet or Mobile)

### Plan Change
- Only active subscriptions can change plan
- The new plan must belong to the **same operator**
- The new plan must have the **same service type**
- The new plan must be active
- Cannot switch to the plan that is already active on the subscription

### Cancellation
- Marks the subscription as `CANCELLED`
- Sets the `cancellationDate` to the current date
- An already cancelled subscription cannot be cancelled again

### Deletion
- Only ADMIN can permanently delete a subscription

---

## ▶️ How to Run

### 1. Clone the repository

```
git clone <your-repo-url>
```

### 2. Start infrastructure (MySQL + Redis)

Make sure Docker Desktop is running, then from the project root (where `docker-compose.yml` is located):

```
docker-compose up -d
```

This starts MySQL on port `3307` and Redis on port `6379`. The database `subscription_db` is created automatically.

### 3. Run the application

From the `subscription-api` module, run:

```
mvn spring-boot:run
```

Or run `SubscriptionApiApplication.java` directly from your IDE.

### 4. Open Swagger

`http://localhost:8080/swagger-ui/index.html`

### 5. Log in and authorize

Use one of the seeded test accounts (see [Seed Data](#-seed-data)) on `POST /api/v1/auth/login`, copy the `accessToken` from the response, then click **Authorize** in Swagger and enter:

```
Bearer <accessToken>
```

---

## 🎉 Project Status

✔ Fully functional  
✔ All endpoints implemented  
✔ JWT security working  
✔ Seed data included  
✔ Swagger documented  
✔ Ready for submission  

---

## 🧰 Tech Stack

![Java](https://img.shields.io/badge/Java-25-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT%20Auth-orange)
![Hibernate](https://img.shields.io/badge/Hibernate-JPA-red)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Redis](https://img.shields.io/badge/Redis-Token%20Blacklist-red)
![Lombok](https://img.shields.io/badge/Lombok-Enabled-green)
![MapStruct](https://img.shields.io/badge/MapStruct-Object%20Mapping-yellowgreen)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203.0-lightgrey)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-C71A36)
![JWT](https://img.shields.io/badge/JWT-Token%20Auth-purple)
