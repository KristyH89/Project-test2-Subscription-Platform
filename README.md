# 📦 Subscription API — Telecom Subscription Management System

## 📑 Table of Contents

- [Overview](#-overview)
- [Project instructions](#-project-instructions)
- [API Documentation](#-api-documentation-swagger)
- [Project Structure](#-project-structure)
- [Features](#-features)
- [Seed Data](#-seed-data)
- [Authentication](#-authentication-flow)
- [Roles](#-roles)
- [Endpoints](#-endpoints-overview)
- [Business Rules](#-business-rules)
- [How to Run](#-how-to-run)
- [Project Status](#-project-status)
- [Tech Stack](#-tech-stack)


## Overview
A Spring Boot–based backend application for managing telecom operators, subscription plans, customers, and subscriptions.
The system uses JWT authentication, role‑based authorization, DTO mapping, validation, and clean service‑layer architecture.

This project was built as part of a backend workshop and demonstrates professional API design with full Swagger documentation.

---


## 📘 Project Instructions

👉 [View project instructions](README.md)

---

## 📘 API Documentation (Swagger)
Once the application is running, open:
`http://localhost:8080/swagger-ui/index.html`

Use the Authorize button and enter:
`Bearer <your_access_token> `

```
se.lexicon.subscriptionapi
│
├── config/                 # Seed data, OpenAPI config
├── controller/             # REST controllers (Auth, Customer, Plan, Operator, Subscription)
├── domain/
│   ├── entity/             # JPA entities
│   └── enums/              # ServiceType, Roles, etc.
│
├── dto/
│   ├── request/            # Incoming request DTOs
│   └── response/           # Outgoing response DTOs
│
├── repository/             # Spring Data JPA repositories
├── security/               # JWT provider, filters, blacklist
├── service/                # Service interfaces
│   └── impl/               # Service implementations
│
└── SubscriptionApiApplication.java
```
---

# ⚙️ Features

### 🔐 Authentication & Authorization
- JWT login & logout
- Token blacklist on logout
- Role‑based access (USER / ADMIN)
- Public registration endpoint

### 🏢 Operator Management
- Create operator (ADMIN)
- Search by name
- Get all operators

### 📦 Plan Management
- Create, update, delete plans (ADMIN)
- Get active plans
- Filter by service type
- Get plans by operator

### 📄 Subscription Management
- Create subscription
- Cancel subscription
- Change plan (same operator + same service type)
- View subscriptions per customer
- Admin: view/delete all subscriptions

### 👤 Customer Management
- Register customer
- Update profile
- Get customer by ID or email
- Admin: list all customers

---
## 🌱 Seed Data
On startup, the application seeds:

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

```
{
  "email": "admin@example.com",
  "password": "password"
}
```

Resonse:
```
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer"
}
```

Register:
`POST /api/v1/auth/register`

Logout:
`POST /api/v1/auth/logout`
→ Token is blacklisted until expiration.

--- 

## 🧑‍💼 Roles
### USER
- View active plans
- Subscribe / cancel / change plan
- View own subscriptions
- View plans by operator

### ADMIN
- Everything USER can do, plus:
- Manage operators
- Manage plans
- View all customers
- View/delete all subscriptions

---

## 📚 Endpoints Overview

### 🔑 Auth
| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /auth/login | Public |
| POST | /auth/register | Public |
| POST | /auth/logout | USER/ADMIN |

### 👤 Customers
| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /customers | Public |
| GET | /customers/{id} | USER/ADMIN |
| GET | /customers/email/{email} | USER/ADMIN |
| GET | /customers | USER/ADMIN |
| PUT | /customers/{id}/profile | USER/ADMIN |

### 🏢 Operators

| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /operators | ADMIN |
| GET | /operators/{id} | USER/ADMIN |
| GET | /operators/search?name= | USER/ADMIN |
| GET | /operators | USER/ADMIN |

### 📦 Plans
| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /plans | ADMIN |
| PUT | /plans/{id} | ADMIN |
| DELETE | /plans/{id} | ADMIN |
| GET | /plans | ADMIN |
| GET | /plans/{id} | USER/ADMIN |
| GET | /plans/active | USER/ADMIN |
| GET | /plans/active/type | USER/ADMIN |
| GET | /plans/operator/{operatorId} | USER/ADMIN |

### 📄 Subscriptions
| Method | Endpoint | Role |
| --- | --- | --- |
| POST | /subscriptions | USER/ADMIN |
| GET | /subscriptions/{id} | USER/ADMIN |
| GET | /subscriptions/customer/{customerId} | USER/ADMIN |
| GET | /subscriptions | ADMIN |
| DELETE | /subscriptions/{id} | ADMIN |
| PUT | /subscriptions/{id}/cancel | USER/ADMIN |
| PUT | /subscriptions/{id}/change-plan/{newPlanId} | USER/ADMIN |

---
## ⚙️ Business Rules
### Subscription Creation
- Cannot subscribe twice to the same service type
- Only active plans can be subscribed to
- Customer must exist
- Plan must exist

### Plan Change
- Must be same operator
- Must be same service type
- Old subscription becomes inactive

### Cancellation
- Marks subscription as cancelled
- Admin can delete subscriptions

---

## ▶️ How to Run

### 1. Clone the repository
`git clone <your-repo-url> `

### 2. Configure MySQL
Create database:  
`subscription_api `

Set credentials in application.properties.

### 3. Run the application
`mvn spring-boot:run`

### 4. Open Swagger
`http://localhost:8080/swagger-ui/index.html`

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

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT%20Auth-orange)
![Hibernate](https://img.shields.io/badge/Hibernate-JPA-red)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Lombok](https://img.shields.io/badge/Lombok-Enabled-green)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203.0-lightgrey)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-C71A36)
![JWT](https://img.shields.io/badge/JWT-Token%20Auth-purple)


