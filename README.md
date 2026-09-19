# 🎬 BookMyShow Production Application

A production-oriented movie ticket booking platform built with **Spring Boot, React, PostgreSQL, Redis, Kafka, and Razorpay**.

The project goes beyond a basic CRUD application by implementing concepts such as **JWT authentication, role-based authorization, concurrency-safe seat booking, temporary seat locking, asynchronous event processing, payment verification, Razorpay webhooks, idempotent payment handling, transactional notifications, Flyway migrations, and Docker-based infrastructure**.

---

## 🚀 Overview

This application simulates a modern movie ticket booking platform where users can:

* Browse movies and theatres
* View available shows
* Select seats
* Temporarily lock seats during booking
* Create bookings
* Make payments through Razorpay
* Receive booking/payment notifications
* View their bookings and tickets

Administrators can manage the application's movie and theatre-related data through protected endpoints.

The system is designed with reliability and failure handling in mind, particularly around **seat concurrency and payment processing**.

---

## 🏗️ Architecture

```text
                         ┌──────────────────────┐
                         │   React + TypeScript │
                         │      Frontend        │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │    Spring Boot       │
                         │    Main Application  │
                         │       :8080          │
                         └───────┬───────┬──────┘
                                 │       │
                    ┌────────────┘       └─────────────┐
                    ▼                                  ▼
             ┌─────────────┐                    ┌─────────────┐
             │ PostgreSQL  │                    │    Redis    │
             │   Database  │                    │ Seat / OTP  │
             └─────────────┘                    └─────────────┘
                    │
                    │
                    ▼
             ┌─────────────┐
             │    Kafka    │
             │ Event Bus   │
             └──────┬──────┘
                    │
             ┌──────┴──────────┐
             ▼                 ▼
     ┌────────────────┐  ┌──────────────────┐
     │ Payment Service│  │ Notification     │
     │     :8081      │  │ Service :8082    │
     └───────┬────────┘  └────────┬─────────┘
             │                    │
             ▼                    ▼
        ┌──────────┐          ┌──────────┐
        │ Razorpay │          │  Email   │
        └──────────┘          └──────────┘
```

The current implementation keeps the main booking/catalog functionality together while separating payment and notification responsibilities into dedicated services.

---

## 🧰 Tech Stack

### Backend

* Java 25
* Spring Boot 4
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Security
* JWT
* MapStruct
* Maven
* Flyway

### Database & Infrastructure

* PostgreSQL
* Redis
* Apache Kafka
* Docker
* Docker Compose

### Payment & Communication

* Razorpay
* SMTP / Gmail
* JavaMailSender

### Frontend

* React
* TypeScript
* Vite
* CSS

### Testing

* JUnit
* Spring Boot Test
* Mockito

---

## ✨ Key Features

### 🔐 Authentication & Authorization

* User registration
* User login
* JWT-based authentication
* Stateless Spring Security configuration
* BCrypt password hashing
* Role-based authorization
* `USER` and `ADMIN` roles
* Protected administrative operations
* Internal service authentication using service tokens
* Centralized CORS configuration

---

### 🎬 Movie & Theatre Management

The application models the movie booking domain using:

```text
City
 └── Theatre
      └── Screen
           └── Seat
                └── ShowSeat
```

Movies are associated with:

* Genres
* Languages
* People
* Cast/director roles

Movie lifecycle states include:

```text
DRAFT
UPCOMING
RELEASED
ARCHIVED
```

---

### 🎟️ Show & Seat Management

A physical seat belongs to a screen, while a `ShowSeat` represents that seat for a particular show.

This separation allows the system to maintain independent availability for each show.

Example:

```text
Screen A
 ├── A1
 ├── A2
 └── A3

Movie Show #101
 ├── A1 → AVAILABLE
 ├── A2 → BOOKED
 └── A3 → AVAILABLE

Movie Show #102
 ├── A1 → AVAILABLE
 ├── A2 → AVAILABLE
 └── A3 → BOOKED
```

---

## 🔒 Concurrency-Safe Seat Booking

Seat booking is one of the critical parts of the system.

The application uses database locking when selecting seats for a booking operation.

Conceptually:

```text
User A ─────┐
            │
            ▼
       Request Seat A1
            │
            ▼
     Database Lock
            │
            ▼
       Check Status
            │
            ▼
        Book Seat
```

If another request attempts to book the same seat concurrently, the database-level locking prevents both transactions from successfully claiming the same seat.

A database constraint also prevents the same `ShowSeat` from being associated with multiple bookings.

---

## ⏱️ Temporary Seat Locking

Seats can be temporarily held during the payment process.

The booking contains a unique `lockToken`, while Redis is used for fast temporary state.

The general flow is:

```text
AVAILABLE
    │
    ▼
TEMPORARILY LOCKED
    │
    ├── Payment succeeds ──► BOOKED
    │
    └── Lock expires ──────► AVAILABLE
```

Expired bookings are handled by a scheduled expiration process.

---

# 💳 Payment Architecture

Payments are handled by a dedicated Payment Service.

The application does **not** trust the frontend alone to determine whether a payment succeeded.

### Payment flow

```text
Frontend
   │
   ▼
Main Application
   │
   ▼
Payment Service
   │
   ▼
Create Razorpay Order
   │
   ▼
Frontend Razorpay Checkout
   │
   ▼
Razorpay
   │
   ├───────────────┐
   │               │
   ▼               ▼
Verification     Webhook
   │               │
   └───────┬───────┘
           ▼
     Payment Service
           │
           ▼
      Kafka Event
           │
           ▼
   Main Application
           │
           ▼
 Booking Confirmation
           │
           ├──► Ticket
           │
           └──► Notification
```

---

## 🛡️ Payment Security

The payment flow includes:

* Razorpay order creation
* Razorpay signature verification
* Razorpay webhook signature validation
* Payment status validation
* Database-level payment constraints
* Idempotent payment processing
* Late-payment refund handling
* Internal service authentication

The frontend payment result is therefore not treated as the final source of truth.

---

## 🔁 Idempotent Payment Processing

Payment events can potentially be delivered more than once.

The application uses event identifiers and processed-event tracking to avoid processing the same event repeatedly.

Example:

```text
Payment Event
     │
     ▼
Check event ID
     │
     ├── Already processed ──► Ignore
     │
     └── New event
             │
             ▼
       Process payment
             │
             ▼
      Save event ID
```

Payment records also use unique Razorpay identifiers to prevent duplicate processing.

---

# 📨 Kafka Event Processing

Kafka is used for asynchronous communication between the application components.

Examples of payment events include:

```text
PAYMENT_SUCCESS
PAYMENT_FAILED
PAYMENT_LATE_REFUNDED
```

The payment service uses an **outbox event** mechanism.

Conceptually:

```text
Payment Transaction
       │
       ├── Update Payment
       │
       └── Create Outbox Event
                │
                ▼
          Outbox Publisher
                │
                ▼
              Kafka
                │
                ▼
       Main Application /
       Notification Service
```

This reduces the risk of updating the database successfully while failing to publish the corresponding event.

---

# 📧 Notification Service

The Notification Service is responsible for email-based notifications.

Current functionality includes:

* OTP generation
* OTP verification
* Email delivery
* Payment success notifications
* Late payment refund notifications
* Notification event logging
* Duplicate-event protection

Redis is used for OTP storage with an expiration time.

Example:

```text
Generate OTP
    │
    ▼
Redis
otp:<email>
TTL: 5 minutes
    │
    ▼
Send Email
```

---

# 🎫 Ticket Generation

Tickets are generated after successful booking confirmation.

Each ticket receives a unique ticket number.

Example:

```text
Booking
   │
   ▼
Payment Confirmed
   │
   ▼
Booking CONFIRMED
   │
   ▼
Generate Ticket
   │
   ▼
Unique Ticket Number
```

Ticket generation is designed to be idempotent so that duplicate payment events do not generate multiple tickets for the same booking.

---

# 🗄️ Database Design

The main application uses PostgreSQL.

Important entities include:

```text
User
Movie
Genre
Language
Person
MovieGenre
MovieLanguage
MoviePerson

City
Theatre
Screen
Seat
Show
ShowSeat

Booking
BookingSeat
Payment
Ticket
ProcessedEvent
```

Database schema evolution is handled using **Flyway migrations**.

Current migrations include:

```text
V1__initial_schema.sql
V2__add_role_to_user.sql
V3__add_booking_seat_unique_constraint.sql
V4__add_lock_token_to_booking.sql
V5__prevent_duplicate_active_payments.sql
V6__create_processed_events.sql
V7__create_ticket.sql
V8__fix_movie_poster_urls.sql
V9__fix_kantara_poster_url.sql
```

---

# 🧩 Project Structure

```text
bookmyshow-production-app/
│
├── BookMyShow/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── config/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── entity/
│   │   │   │   ├── enums/
│   │   │   │   ├── event/
│   │   │   │   ├── exception/
│   │   │   │   ├── mapper/
│   │   │   │   ├── paymentclient/
│   │   │   │   ├── repository/
│   │   │   │   ├── scheduler/
│   │   │   │   ├── security/
│   │   │   │   └── service/
│   │   │   └── resources/
│   │   │       └── db/migration/
│   │   └── test/
│   │
│   └── pom.xml
│
├── payment-service/
│   ├── src/
│   └── pom.xml
│
├── NotificationService/
│   ├── src/
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── context/
│   │   ├── layouts/
│   │   ├── pages/
│   │   ├── routes/
│   │   └── styles/
│   └── package.json
│
├── docker-compose.yml
├── bookmyshow_data_postgres.sql
├── .gitignore
└── README.md
```

---

# 🔄 Request Architecture

The backend follows a layered architecture:

```text
Controller
    │
    ▼
Request DTO
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
Entity
```

Responses follow:

```text
Entity
   │
   ▼
MapStruct Mapper
   │
   ▼
Response DTO
   │
   ▼
Controller
```

This keeps API contracts separate from persistence entities.

---

# 🐳 Running the Infrastructure

Docker Compose is used for local infrastructure.

Current infrastructure includes:

```text
PostgreSQL
Redis
Kafka
Zookeeper
```

Start the infrastructure with:

```bash
docker compose up -d
```

Check running containers:

```bash
docker ps
```

---

# ⚙️ Environment Variables

Secrets are intentionally **not committed** to this repository.

The application uses environment variables for sensitive configuration.

Important variables include:

```text
DB_USERNAME
DB_PASSWORD
JWT_SECRET

PAYMENT_SERVICE_TOKEN
PAYMENT_SERVICE_URL
NOTIFICATION_SERVICE_URL

RAZORPAY_KEY_ID
RAZORPAY_KEY_SECRET

MAIL_USERNAME
MAIL_PASSWORD
```

Refer to the example environment/configuration files for the required variable names.

> Never commit real API keys, passwords, JWT secrets, Razorpay secrets, or email credentials.

---

# ▶️ Running the Backend

Start the infrastructure first:

```bash
docker compose up -d
```

Then start the main application:

```bash
cd BookMyShow
./mvnw spring-boot:run
```

On Windows:

```cmd
cd BookMyShow
mvnw.cmd spring-boot:run
```

The main application runs on:

```text
http://localhost:8080
```

---

## 💳 Start Payment Service

```cmd
cd payment-service
mvnw.cmd spring-boot:run
```

Payment Service:

```text
http://localhost:8081
```

---

## 📧 Start Notification Service

```cmd
cd NotificationService
mvnw.cmd spring-boot:run
```

Notification Service:

```text
http://localhost:8082
```

---

# 🌐 Frontend

Install dependencies:

```bash
cd frontend
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend is served by Vite, normally at:

```text
http://localhost:5173
```

---

# 🧪 Testing

Backend tests can be executed using Maven:

```cmd
mvnw.cmd test
```

The project includes Spring Boot application tests for the services.

Security behavior has also been tested for role-based access, including scenarios where administrative operations are accessible to administrators but rejected for ordinary users.

---

# 🔐 Security Principles

The project follows several security practices:

* Passwords hashed with BCrypt
* JWT authentication
* Stateless sessions
* Role-based authorization
* Protected internal service endpoints
* Service-to-service authentication
* Centralized CORS configuration
* Razorpay signature verification
* Razorpay webhook verification
* Secrets supplied through environment variables
* No secrets committed to Git

---

# 📈 Reliability & Failure Handling

The system considers several real-world failure scenarios:

### Duplicate payment events

Handled through event identifiers and processed-event tracking.

### Concurrent seat booking

Database locking and unique constraints protect seat ownership.

### Payment succeeds after booking expiration

The system can identify the expired booking and initiate the refund flow.

### Duplicate active payments

A database constraint prevents multiple active payment records for the same booking.

### Notification duplication

Notification events are logged with event identifiers to avoid sending the same transactional notification repeatedly.

### Service communication

Internal services use authenticated service-to-service requests rather than exposing internal operations as public APIs.

---

# 🛠️ Current Status

### Implemented

* [x] User registration
* [x] JWT authentication
* [x] Role-based authorization
* [x] Movie management
* [x] Genre/language management
* [x] Theatre management
* [x] Screen management
* [x] Seat management
* [x] Show management
* [x] Show-seat management
* [x] Booking management
* [x] Seat locking
* [x] Booking expiration
* [x] PostgreSQL persistence
* [x] Flyway migrations
* [x] Redis integration
* [x] Kafka integration
* [x] Payment Service
* [x] Razorpay integration
* [x] Razorpay webhook processing
* [x] Payment event publishing
* [x] Outbox events
* [x] Idempotent payment processing
* [x] Late payment refund handling
* [x] Ticket generation
* [x] Notification Service
* [x] Email notifications
* [x] OTP functionality
* [x] React + TypeScript frontend
* [x] Dark/light theme
* [x] Docker infrastructure

---

# 🔮 Future Improvements

Planned improvements include:

* API Gateway
* Stronger service discovery/configuration
* WebSocket-based real-time seat updates
* Distributed tracing
* Centralized observability
* Prometheus/Grafana metrics
* OpenTelemetry
* More comprehensive integration testing
* Testcontainers-based infrastructure tests
* CI/CD pipeline
* Containerized deployment
* Production cloud deployment
* Rate limiting
* Advanced caching strategies
* Improved fault tolerance and retry policies
* More comprehensive API documentation

---

# 🎯 Engineering Focus

The primary goal of this project is not to reproduce a movie-ticket UI.

The project is being developed to explore the backend engineering problems that appear in real transactional systems:

```text
Concurrency
     ↓
Consistency
     ↓
Transactions
     ↓
Distributed Events
     ↓
Payment Reliability
     ↓
Idempotency
     ↓
Failure Recovery
     ↓
Security
     ↓
Scalability
```

The project therefore focuses on understanding **why** these mechanisms are required rather than simply connecting CRUD endpoints.

---

# 📚 Learning Objectives

This project provides practical experience with:

* Spring Boot backend development
* REST API design
* Spring Security
* JWT authentication
* JPA/Hibernate
* PostgreSQL
* Database transactions
* Pessimistic locking
* Redis
* Apache Kafka
* Event-driven architecture
* Outbox pattern
* Idempotency
* Payment gateway integration
* Webhook processing
* Distributed service communication
* Docker
* Database migrations
* React + TypeScript
* Production-oriented system design

---

# 👨‍💻 Author

**Preetham B**

AI & ML Engineering Student | Backend Developer

Focused on Java, Spring Boot, distributed systems, and backend engineering.

---

## ⭐ Project

If you find the project useful for learning backend engineering, feel free to explore the source code and follow the development of the system.


