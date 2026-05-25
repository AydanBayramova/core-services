# 🚚 Delivery Application — Microservices Based Delivery Management System

A scalable and event-driven **Delivery Management Platform** built using **Spring Boot Microservices Architecture**.

This project demonstrates real-world backend architecture concepts including:

* Microservices
* Event-Driven Communication
* Distributed System Design
* API Gateway
* Service Discovery
* Asynchronous Messaging
* Dockerized Infrastructure
* Clean Service Boundaries

---

# 📌 Project Overview

The system manages the complete lifecycle of delivery orders.

It allows:

* Creating delivery orders
* Assigning couriers
* Tracking order statuses
* Managing courier availability
* Calculating courier earnings
* Tracking balances & turnover
* Sending notifications
* Inter-service communication using events

The architecture follows **Domain-Driven Service Separation** where every service owns its own responsibility and database.

---

# 🏗️ Project Architecture

```text
delivery-app
│
├── infrastructure-services
│   ├── config-server
│   ├── eureka-server
│   └── api-gateway
│
├── core-services
│   ├── order-service
│   ├── courier-service
│   ├── user-service
│   └── merchant-service
│
├── support-services
│   ├── payment-service
│   ├── notification-service
│   ├── tracking-service
│   └── rating-service
│
├── common
│   ├── DTOs
│   ├── Exceptions
│   └── Shared Events
│
└── docker-compose.yml
```

---

# 🧠 System Design Philosophy

The project is designed with the following principles:

| Principle                 | Description                                          |
| ------------------------- | ---------------------------------------------------- |
| Single Responsibility     | Each service owns one business domain                |
| Loose Coupling            | Services communicate via events                      |
| Database Per Service      | No shared database                                   |
| Event-Driven Architecture | Services react to business events                    |
| Scalability               | Independent deployment & scaling                     |
| Fault Isolation           | One service failure does not break the entire system |

---

# ⚙️ Technologies Used

| Technology              | Purpose                           |
| ----------------------- | --------------------------------- |
| Java 17                 | Backend language                  |
| Spring Boot             | Microservices framework           |
| Spring Cloud            | Distributed system support        |
| Spring Cloud Gateway    | API Gateway                       |
| Eureka Server           | Service Discovery                 |
| Spring Config Server    | Centralized configuration         |
| Spring Data JPA         | Database operations               |
| PostgreSQL              | Relational database               |
| RabbitMQ                | Event-driven communication        |
| OpenFeign               | Synchronous service communication |
| Docker & Docker Compose | Containerization                  |
| Lombok                  | Boilerplate reduction             |
| Maven                   | Dependency management             |

---

# 🔥 Core Services

---

# 📦 Order Service

### Responsibility

Owns the complete order lifecycle.

### Features

| Feature                  | Description                        |
| ------------------------ | ---------------------------------- |
| Create Order             | Creates delivery requests          |
| Address Management       | Stores pickup & delivery addresses |
| Courier Assignment       | Assigns available courier          |
| Delivery Fee Calculation | Calculates delivery cost           |
| Status Tracking          | Tracks delivery progress           |
| Event Publishing         | Publishes business events          |

### Order Status Lifecycle

```text
CREATED
   ↓
ASSIGNED
   ↓
PICKED_UP
   ↓
DELIVERED
```

### Published Events

| Event           | Trigger           |
| --------------- | ----------------- |
| ORDER_CREATED   | New order created |
| ORDER_ASSIGNED  | Courier assigned  |
| ORDER_DELIVERED | Order delivered   |

### Important Boundaries

✅ Knows assigned courier
✅ Controls order flow

❌ Does NOT manage courier balance
❌ Does NOT manage courier availability

---

# 🛵 Courier Service

### Responsibility

Manages courier states and availability.

### Features

| Feature               | Description                       |
| --------------------- | --------------------------------- |
| Courier Management    | Create/update couriers            |
| Availability Tracking | FREE / BUSY state                 |
| Event Reactions       | Updates courier state from events |
| Courier Search        | Provides available courier        |

### Courier Status

```text
FREE
BUSY
```

### Event Reactions

| Event           | Action         |
| --------------- | -------------- |
| ORDER_ASSIGNED  | Courier → BUSY |
| ORDER_DELIVERED | Courier → FREE |

### Important Boundaries

✅ Knows courier availability

❌ Does NOT calculate earnings
❌ Does NOT manage payments

---

# 💰 Payment Service

### Responsibility

Owns all financial operations.

### Features

| Feature                 | Description                       |
| ----------------------- | --------------------------------- |
| Payment Record Creation | Creates payment on order creation |
| Courier Earnings        | Calculates earnings               |
| Balance Management      | Updates courier balances          |
| Turnover Tracking       | Tracks total earnings             |
| Payment History         | Stores all transactions           |

### Payment Flow

#### On ORDER_CREATED

```text
Payment Record Created
Status → PENDING
```

#### On ORDER_DELIVERED

```text
Calculate Earnings
Update Balance
Update Turnover
Status → COMPLETED
```

### Important Boundaries

✅ Owns all financial data

❌ Does NOT manage orders
❌ Does NOT manage courier availability

---

# 🔔 Notification Service

### Responsibility

Sends informational notifications.

### Example Notifications

| Event           | Notification       |
| --------------- | ------------------ |
| ORDER_CREATED   | "Order created"    |
| ORDER_ASSIGNED  | "Courier assigned" |
| ORDER_DELIVERED | "Order delivered"  |

### Important Note

* Pure event listener
* Contains NO business logic

---

# 🌐 Infrastructure Services

---

# ⚙️ Config Server

Centralized configuration management for all services.

### Benefits

* Externalized configuration
* Easier environment management
* Centralized property management

---

# 🧭 Eureka Server

Service discovery server.

### Responsibilities

* Service registration
* Dynamic service discovery
* Load balancing support

---

# 🚪 API Gateway

Single entry point for all client requests.

### Responsibilities

| Feature              | Description                  |
| -------------------- | ---------------------------- |
| Routing              | Routes requests to services  |
| Load Balancing       | Balances requests            |
| Authentication Ready | Security integration support |
| Centralized Access   | Unified API access           |

---

# 🔄 Communication Strategy

The system uses a hybrid communication model.

| Communication Type | Technology      | Usage                 |
| ------------------ | --------------- | --------------------- |
| Synchronous        | OpenFeign       | Immediate requests    |
| Asynchronous       | RabbitMQ Events | Background processing |

---

# 📡 Event-Driven Architecture

Services react to events instead of directly controlling each other.

## Example

```text
ORDER_DELIVERED
```

Instead of:

```text
UPDATE_COURIER_STATUS
```

### Why?

Because events represent facts that already happened.

This creates:

* Loose coupling
* Better scalability
* Easier maintenance
* Independent service evolution

---

# 🚀 Example Order Flow

---

# 1️⃣ Order Creation

### Order Service

* Validates request
* Calculates delivery fee
* Calls Courier Service via Feign
* Finds available courier

### If courier unavailable

```text
Order → REJECTED
No events published
```

### If courier found

```text
Create Order
Assign Courier
Save Delivery Fee
```

---

# 2️⃣ Event Publishing

Order Service publishes:

```text
ORDER_CREATED
ORDER_ASSIGNED
```

---

# 3️⃣ Async Processing

| Service              | Action                 |
| -------------------- | ---------------------- |
| Payment Service      | Create PENDING payment |
| Courier Service      | Mark courier BUSY      |
| Notification Service | Send notifications     |

---

# 4️⃣ Delivery Completion

Order Service:

```text
Status → DELIVERED
```

### Validation Rules

* Cannot deliver before PICKED_UP
* Cannot update after DELIVERED

### Published Event

```text
ORDER_DELIVERED
```

---

# 5️⃣ Post-Delivery Async Processing

| Service              | Action                      |
| -------------------- | --------------------------- |
| Payment Service      | Calculate earnings          |
| Payment Service      | Update balance              |
| Payment Service      | Update turnover             |
| Payment Service      | Mark payment COMPLETED      |
| Courier Service      | Mark courier FREE           |
| Notification Service | Send delivered notification |

---

# 🧩 Shared Common Module

The `common` module contains reusable components shared across services.

### Includes

| Component  | Purpose                        |
| ---------- | ------------------------------ |
| DTOs       | Shared request/response models |
| Exceptions | Common exception handling      |
| Events     | Shared event contracts         |

---

# 🐳 Docker Support

The project supports containerized deployment.

### Includes

* RabbitMQ
* PostgreSQL
* All microservices
* Infrastructure services

### Run Project

```bash
docker-compose up --build
```

---

# 📂 Possible Future Improvements

| Feature               | Description                    |
| --------------------- | ------------------------------ |
| JWT Security          | Authentication & Authorization |
| Distributed Tracing   | Zipkin / Sleuth                |
| Circuit Breaker       | Resilience4j                   |
| Kafka Integration     | Alternative event broker       |
| Monitoring            | Prometheus + Grafana           |
| CI/CD Pipeline        | GitHub Actions                 |
| Kubernetes Deployment | Container orchestration        |
| Redis Cache           | Performance optimization       |

---

# 🧪 Example APIs

## Create Order

```http
POST /api/orders
```

## Update Order Status

```http
PUT /api/orders/{id}/status
```

## Get Available Courier

```http
GET /api/couriers/available
```

## Get Courier Balance

```http
GET /api/payments/courier/{courierId}/balance
```

---

# 📖 Architectural Mental Model

| Service              | Responsibility                |
| -------------------- | ----------------------------- |
| Order Service        | Controls order lifecycle      |
| Courier Service      | Controls courier availability |
| Payment Service      | Controls money & earnings     |
| Notification Service | Informs users                 |

---

# ✅ Key Takeaways

This project demonstrates:

* Real-world microservice architecture
* Proper service boundaries
* Event-driven communication
* Distributed system thinking
* Scalable backend design
* Clean architecture principles

---

# 👩‍💻 Author

**Aydan Bayramova**

Backend Developer | Java & Spring Boot Enthusiast

---
