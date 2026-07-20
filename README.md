# Product Management API

A **Spring Boot REST API** for managing products, built with JWT-based authentication and role-based access control (RBAC). Users can register/log in, and — depending on their role — view or manage product records.

---

## 📌 Overview

This service exposes two main modules:

| Module | Responsibility |
|---|---|
| **Auth** | User registration, login, and JWT (access + refresh token) issuance |
| **Products** | Create and fetch product records, scoped to authenticated users |

Access to product endpoints is controlled by **role-based authorization**:
- `ROLE_CUSTOMER` → can **view** products
- `ROLE_ADMIN` → can **view and create** products

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.1.0 (Spring Web MVC) |
| Security | Spring Security + JWT (`java-jwt` by Auth0) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL |
| Object Mapping | ModelMapper |
| Boilerplate Reduction | Lombok |
| Build Tool | Maven (Maven Wrapper included) |

---

## 🏗️ Architecture & Request Flow

```
                 ┌─────────────────────┐
                 │  Client / Frontend   │
                 └──────────┬───────────┘
                            │
                            ▼
        ┌───────────────────────────────────────┐
        │        JwtAuthenticationFilter          │
        │  (skips /Auth/**, validates JWT for      │
        │   all other requests)                    │
        └───────────────────┬─────────────────────┘
                            │
                            ▼
        ┌───────────────────────────────────────┐
        │         Spring Security Filter Chain     │
        │   (role-based authorization rules in     │
        │    AppConfig)                            │
        └───────────────────┬─────────────────────┘
                            │
             ┌──────────────┴───────────────┐
             ▼                               ▼
   ┌───────────────────┐          ┌───────────────────────┐
   │   AuthController    │          │   ProductController    │
   │  /Auth/register      │          │   /products             │
   │  /Auth/login          │          │   /products/{id}         │
   └─────────┬──────────┘          └───────────┬─────────────┘
             ▼                                   ▼
   ┌───────────────────┐          ┌───────────────────────┐
   │    AuthService       │          │    ProductService       │
   └─────────┬──────────┘          └───────────┬─────────────┘
             ▼                                   ▼
   ┌───────────────────┐          ┌───────────────────────┐
   │  UserRepository (JPA)│          │ ProductRepository (JPA)  │
   └─────────┬──────────┘          └───────────┬─────────────┘
             └──────────────┬───────────────────┘
                            ▼
                    ┌───────────────┐
                    │  MySQL Database │
                    └───────────────┘
```

### Authentication flow
1. A new user registers via `POST /Auth/register`, providing a name, email, password, and role.
2. The user logs in via `POST /Auth/login` with email + password.
3. On success, the server issues an **access token** and a **refresh token** (both JWTs signed with HMAC256), returned in the response body along with basic user info.
4. For every subsequent request (except `/Auth/**`), the client sends the token in the `Authorization: Bearer <token>` header.
5. `JwtAuthenticationFilter` validates the token, extracts the user's email and roles from its claims, and populates Spring Security's context so downstream authorization rules can evaluate the request.

---

## 📂 Project Structure

```
src/main/java/com/st/productmanagement
├── config/            # Security & bean configuration (AppConfig)
├── controller/         # REST controllers (AuthController, ProductController)
├── dtos/                # Request/response DTOs
├── entity/              # JPA entities (User, Product)
├── enums/               # Role, ProductEnums (product status)
├── exception/            # Custom exceptions + centralized GlobalExceptionHandler
├── repository/            # Spring Data JPA repositories
├── security/              # JWT utilities & authentication filter
└── service/               # Business logic (AuthService, ProductService)
```

---

## 🔐 Roles & Permissions

| Endpoint | Method | Required Role | Notes |
|---|---|---|---|
| `/Auth/register` | POST | Public | Create a new account |
| `/Auth/login` | POST | Public | Get access + refresh tokens |
| `/products` | POST | `ROLE_ADMIN` | Create a product |
| `/products/{id}` | GET | `ROLE_ADMIN` or `ROLE_CUSTOMER` | Fetch a product by ID |

> Authorization rules are defined centrally in `AppConfig`. `PUT` and `DELETE` on `/products/**` are currently restricted to `ROLE_ADMIN` in the security configuration, though their controller endpoints are not yet implemented (see [Roadmap](#-roadmap--known-limitations)).

---

## 📡 API Reference

### Auth

<details>
<summary><code>POST /Auth/register</code> — Register a new user</summary>

**Request body**
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "yourPassword",
  "role": "ROLE_CUSTOMER"
}
```

**Response `200 OK`**
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "role": "ROLE_CUSTOMER"
}
```
</details>

<details>
<summary><code>POST /Auth/login</code> — Authenticate and receive tokens</summary>

**Request body**
```json
{
  "email": "jane@example.com",
  "password": "yourPassword"
}
```

**Response `200 OK`**
```json
{
  "accessToken": "<jwt>",
  "refreshToken": "<jwt>",
  "user": {
    "name": "Jane Doe",
    "email": "jane@example.com",
    "role": "ROLE_CUSTOMER"
  }
}
```
</details>

### Products
*(All product endpoints require `Authorization: Bearer <accessToken>`)*

<details>
<summary><code>POST /products</code> — Create a product (ROLE_ADMIN)</summary>

**Request body**
```json
{
  "name": "Wireless Mouse",
  "brand": "Logitech",
  "price": 1999,
  "quantity": 50,
  "category": "Electronics",
  "user_id": 1
}
```

**Response `200 OK`**
```json
{
  "name": "Wireless Mouse",
  "price": 1999,
  "quantity": 50,
  "brand": "Logitech",
  "category": "Electronics",
  "status": "AVAILABLE",
  "user_id": 1
}
```
</details>

<details>
<summary><code>GET /products/{id}</code> — Fetch a product by ID</summary>

**Response `200 OK`**
```json
{
  "name": "Wireless Mouse",
  "price": 1999,
  "quantity": 50,
  "brand": "Logitech",
  "category": "Electronics",
  "status": "AVAILABLE",
  "user_id": 1
}
```

**Response `404 Not Found`** — if the product does not exist
</details>

### Error Responses
Validation errors return `400 Bad Request` with a field → message map:
```json
{
  "email": "must not be blank",
  "password": "must not be blank"
}
```
Other handled exceptions:
| Exception | HTTP Status |
|---|---|
| `ProductNotFoundException` | 404 |
| `ProductAlreadyExistException` | 409 |
| `UserNotFoundException` | 404 * |
| `InvalidCredentialsException` | 401 * |

\* `handleUserNotFoundException` and `handleInvalidCredentials` in `GlobalExceptionHandler` are currently missing the `@ExceptionHandler` annotation, so as written these two exceptions will **not** be intercepted and will surface as a generic `500 Internal Server Error` instead of the intended 404/401. See [Known Issues](#-roadmap--known-limitations).

---

## ⚙️ Getting Started

### Prerequisites
- **Java 25** (JDK)
- **Maven** (or use the included `mvnw` / `mvnw.cmd` wrapper — no local Maven install needed)
- **MySQL** server running locally (or reachable) with a database named `product_db`

### 1. Clone the repository
```bash
git clone <your-repository-url>
cd product-management
```

### 2. Create the database
```sql
CREATE DATABASE product_db;
```

### 3. Configure `src/main/resources/application.yml`
Update the datasource credentials and JWT secret for your environment:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/product_db
    username: <your-mysql-username>
    password: <your-mysql-password>
jwt:
  secret: <a-long-random-secret>
```
> ⚠️ **Do not commit real credentials or secrets.** Use environment variables or a `.env`/`application-local.yml` file (git-ignored) for anything beyond local experimentation. See [Security Notes](#-security-notes) below.

### 4. Run the application

Using the Maven wrapper:
```bash
./mvnw spring-boot:run       # macOS/Linux
mvnw.cmd spring-boot:run     # Windows
```

Or build and run the jar:
```bash
./mvnw clean package
java -jar target/product-management-0.0.1-SNAPSHOT.jar
```

The API will be available at:
```
http://localhost:8080
```
(Port is configurable via `server.port` in `application.yml`.)

### 5. Run tests
```bash
./mvnw test
```

---

## 🔒 Security Notes

This is a learning/portfolio-stage project. Before using it beyond local development, be aware of the following, and address them as needed:

- **Plaintext passwords**: passwords are currently compared and stored as plain text rather than hashed with `PasswordEncoder` (a `BCryptPasswordEncoder` bean is already defined in `AppConfig` and ready to be wired in).
- **Hardcoded secrets in `application.yml`**: the JWT secret and DB credentials in the committed config are placeholders — replace them and keep real values out of version control.
- **`ddl-auto: update`**: convenient for development, but not recommended for production schemas.

---

## 🗺️ Roadmap / Known Limitations

**Bugs to fix:**
- [ ] In `AuthService.login`, the access and refresh tokens are assigned swapped — `AuthResponseDto.accessToken` is set to the *refresh* token value and vice versa. Swap the two `response.setXxxToken(...)` calls so each field returns the correct token.
- [ ] `GlobalExceptionHandler.handleUserNotFoundException` and `handleInvalidCredentials` are missing the `@ExceptionHandler` annotation, so `UserNotFoundException` and `InvalidCredentialsException` currently fall through to a default `500` response instead of `404`/`401`.

**Not yet implemented:**
- [ ] Wire up `PasswordEncoder` for password hashing on registration and login
- [ ] Implement `PUT /products/{id}` (update) and `DELETE /products/{id}` (delete) — currently only enforced at the security-rule level, not yet implemented in the controller/service
- [ ] Add pagination/listing endpoint (`GET /products`)
- [ ] Externalize secrets via environment variables
- [ ] Add refresh-token endpoint to reissue access tokens

---

## 📄 License

No license has been specified for this project yet. Add a `LICENSE` file to define how others may use, modify, or distribute this code.
