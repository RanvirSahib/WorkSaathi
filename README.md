# WorkSaathi — On-Demand Local Services & Blue-Collar Hiring Platform 🛠️✨

[![Backend Tests](https://img.shields.io/badge/Backend%20Tests-18%2F18%20Passed-brightgreen)](backend)
[![Frontend Tests](https://img.shields.io/badge/Frontend%20Tests-13%2F13%20Passed-brightgreen)](frontend)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-blue)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-cyan)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)](https://www.postgresql.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**WorkSaathi** is a modern, full-stack on-demand local services platform designed for India's service ecosystem. It connects customers with background-verified, skilled tradespeople (Electricians, Plumbers, Carpenters, AC Technicians, Home Cleaners, and more) through transparent pricing, real-time job state progression, and verified reviews.

---

## 🏗️ Architecture & Project Structure

This monorepo contains both the **Spring Boot REST Backend** and the **React + Vite Frontend Web Application**:

```
worksaathi/
├── backend/                  # Spring Boot 3 + PostgreSQL REST API & WebSocket Server
│   ├── src/
│   │   ├── main/java/com/worksaathi/
│   │   │   ├── config/       # Security (JWT), CORS, WebSocket, DataInitializer
│   │   │   ├── controller/   # REST API Controllers (Auth, Workers, Jobs, Admin, etc.)
│   │   │   ├── dto/          # Data Transfer Objects
│   │   │   ├── entity/       # Hibernate / JPA Entities (User, Worker, Job, Review...)
│   │   │   ├── repository/   # Spring Data JPA Repositories
│   │   │   ├── security/     # JwtFilter, JwtService, CustomUserDetailsService
│   │   │   └── service/      # Business logic & state machines
│   │   └── test/             # JUnit 5 & Mockito test suites (100% Pass)
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── pom.xml
│
├── frontend/                 # React 18 + Vite Web Application & Vitest Suite
│   ├── src/
│   │   ├── __tests__/        # Vitest & Testing Library suites (100% Pass)
│   │   ├── services/         # Centralized Fetch API Client with Bearer tokens
│   │   ├── main.jsx          # React Component Architecture & Portal Views
│   │   ├── setupTests.js     # Jest-DOM matchers setup
│   │   └── styles.css        # Premium custom design system
│   ├── index.html
│   ├── vite.config.js        # Vite + Vitest config
│   └── package.json
│
├── .gitignore
└── README.md
```

---

## 🚀 Key Features

- 👥 **Multi-Role Experience**: Unified interface with dedicated views for **Customers**, **Workers**, and **Administrators**.
- 🔍 **Worker Discovery & Filtering**: Real-time category filtering, price sliders, rating sorting, and proximity metrics.
- 📅 **Interactive Job Booking**: Multi-step booking modal with instant schedule selection and price estimation.
- ⚡ **Full Job State Machine**: Real-time progression (`REQUESTED` ➔ `ACCEPTED` ➔ `ON_THE_WAY` ➔ `ARRIVED` ➔ `IN_PROGRESS` ➔ `COMPLETED`).
- 🛡️ **Admin Verification Portal**: Real-time dashboard statistics and 1-click verification queue for newly registered professionals.
- 🔐 **Stateless JWT Security**: Secure access tokens, password hashing with BCrypt, role-based route guards.
- 📖 **Interactive API Documentation**: OpenAPI 3 & Swagger UI explorer pre-configured.

---

## ⚡ Quick Start Guide

### Prerequisites
- **Java 17 or 21**
- **Node.js 18+** & **npm**
- **PostgreSQL** (running on port `5432` or `1104`)

---

### 1. Start the Backend API

```bash
cd backend

# Build and run the Spring Boot application
mvn spring-boot:run
```
* **Backend API Base**: `http://localhost:8080/api/v1`
* **Swagger UI Docs**: `http://localhost:8080/swagger-ui/index.html`

---

### 2. Start the Frontend Web App

```bash
cd frontend

# Install dependencies
npm install

# Start Vite dev server
npm run dev
```
* **Frontend Application**: `http://localhost:5173`

---

## 🧪 Testing Suites & Verification

### Run Backend Tests (JUnit 5 / Mockito)
```bash
cd backend
mvn test
```
* **Result**: `Tests run: 18, Failures: 0, Errors: 0, Skipped: 0` (100% Success)

### Run Frontend Tests (Vitest / Testing Library)
```bash
cd frontend
npm test
```
* **Result**: `2 Test Files Passed, 13 Tests Passed` (100% Success)

---

## 🔑 Demo Login Accounts

| Role | Email | Password |
| :--- | :--- | :--- |
| 👨 **Customer** | `customer@worksaathi.com` | `password123` |
| ⚡ **Worker** | `raj@worksaathi.com` | `password123` |
| 🛡️ **Admin** | `admin@worksaathi.com` | `password123` |

*(You can also use the 1-click demo buttons on the login screen).*

---

## 📄 License
Distributed under the **MIT License**.
