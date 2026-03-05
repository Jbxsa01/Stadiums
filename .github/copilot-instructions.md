# Copilot Instructions for Stadiums Repository

## Project Overview

This is a **Stadium Reservation System** built with a Spring Boot microservices backend and an Angular 17 frontend. It allows users to browse stadiums, make reservations (with CIN document upload), and receive PDF tickets. Admins can manage stadiums and reservations.

---

## Repository Structure

```
Stadiums/
├── .github/                   # GitHub configuration (this file)
├── auth-service/              # Authentication microservice (port 8082)
├── config-service/            # Spring Cloud Config Server (port 8888)
├── eureka-service/            # Eureka Service Registry (port 8761)
├── gateway-service/           # Spring Cloud API Gateway (port 8081)
├── reservation-service/       # Reservation microservice (port 8084)
├── stadium-service/           # Stadium management microservice (port 8083)
├── frontend-angular/          # Angular 17 frontend (port 4200)
├── src/                       # Root Spring Boot project (DoctoratApplication)
├── uploads/                   # Uploaded files (CIN documents, images)
├── pom.xml                    # Root POM (parent for gateway-service only)
├── mvnw / mvnw.cmd            # Maven wrapper scripts
└── package-lock.json          # npm lockfile at root level
```

---

## Architecture

```
Angular Frontend (4200)
        │
        ▼
API Gateway (8081)  ← single entry point
  ├─ /auth/**          → auth-service (8082)     [service name: users-service]
  ├─ /stadium/**       → stadium-service (8083)
  └─ /reservation/**   → reservation-service (8084)

All services register with Eureka (8761).
All services (except eureka-service) fetch config from Config Server (8888),
which pulls from a remote GitHub repository: https://github.com/Jbxsa01/Config-server
```

---

## Technology Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 17 |
| Framework | Spring Boot | 3.2.x – 3.5.x |
| Orchestration | Spring Cloud | 2023.0.x – 2025.0.0 |
| Service Discovery | Netflix Eureka | via Spring Cloud |
| API Gateway | Spring Cloud Gateway (WebFlux) | — |
| Databases (dev) | H2 (in-memory) | — |
| Databases (prod) | PostgreSQL via Supabase | — |
| Frontend | Angular | 17.3.0 |
| Frontend Language | TypeScript | 5.4.2 |
| Build tool (backend) | Maven 3.x | mvnw wrapper included |
| Package manager (frontend) | npm | Latest |
| Testing (backend) | Spring Boot Test | — |
| Testing (frontend) | Karma + Jasmine | — |

---

## How to Build and Run

### Prerequisites
- **Java 17+** (set `JAVA_HOME`)
- **Node 18+ / npm 9+**
- Maven is provided via the `./mvnw` wrapper – no separate install needed.

### Backend – Start Order (important)

Services must be started in the following order because they depend on each other:

```bash
# 1. Eureka Service Registry
cd eureka-service && ../mvnw spring-boot:run

# 2. Config Server
cd config-service && ../mvnw spring-boot:run

# 3. Auth Service
cd auth-service && ../mvnw spring-boot:run

# 4. Stadium Service
cd stadium-service && ../mvnw spring-boot:run

# 5. Reservation Service
cd reservation-service && ../mvnw spring-boot:run

# 6. API Gateway
cd gateway-service && ../mvnw spring-boot:run
```

Build a single service from its directory:
```bash
cd <service-directory>
../mvnw clean package
```

Build from root (builds gateway-service only, since root pom.xml is gateway-service's pom):
```bash
./mvnw clean package
```

### Frontend

```bash
cd frontend-angular
npm install
npm start            # dev server → http://localhost:4200
npm run build        # production build → dist/frontend-angular/
npm run test         # run Karma/Jasmine unit tests
```

### Running with Production Database (Supabase/PostgreSQL)

Add the profile flag when running any backend service:
```bash
../mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=supabase"
```

---

## Service-by-Service Guide

### eureka-service
- **Purpose:** Central service registry; all microservices register here.
- **Port:** 8761
- **Dashboard:** http://localhost:8761
- **Key file:** `eureka-service/src/main/resources/application.properties`
- **Spring Cloud version:** 2025.0.0 (differs from other services – uses newer BOM)

### config-service
- **Purpose:** Centralized configuration pulled from https://github.com/Jbxsa01/Config-server
- **Port:** 8888
- **Key file:** `config-service/src/main/resources/application.properties`
- **Note:** Requires internet access at startup to clone remote config repository.

### gateway-service
- **Purpose:** Single entry point; routes requests, handles CORS.
- **Port:** 8081
- **Key files:** `gateway-service/src/main/resources/application.yml` (route definitions)
- **Routes:**
  - `/auth/**` → `USERS-SERVICE` (lb://USERS-SERVICE)
  - `/stadium/**` → `STADIUM-SERVICE`
  - `/reservation/**` → `RESERVATION-SERVICE`
- **CORS:** All origins (`*`) are allowed – restrict in production.

### auth-service
- **Purpose:** User registration, login, logout.
- **Port:** 8082
- **Eureka name:** `users-service`
- **Endpoints:**
  - `POST /auth/register` – register new user
  - `POST /auth/login` – login, returns token
  - `POST /auth/logout/{userId}` – logout
  - `GET /auth/health` – health check
- **Entities:** `User` (username, email, password, role: USER/ADMIN)
- **Security:** BCrypt password encoding; Spring Security configured.
- **Dev DB:** H2 in-memory (`/h2-console` available)
- **Prod DB:** PostgreSQL (Supabase) via `application-supabase.properties`

### stadium-service
- **Purpose:** CRUD for stadiums, including image uploads.
- **Port:** 8083
- **Endpoints:**
  - `GET /api/stadiums` – list all
  - `GET /api/stadiums/{id}` – get by ID
  - `POST /api/stadiums` – create
  - `PUT /api/stadiums/{id}` – update
  - `DELETE /api/stadiums/{id}` – delete
- **Entity:** `Stadium` (name, pricePerHour, description, imageUrl, location, available)
- **Key files:** `stadium-service/src/main/resources/application.yml`

### reservation-service
- **Purpose:** Manage stadium reservations; CIN file upload; PDF ticket generation.
- **Port:** 8084
- **Endpoints:**
  - `POST /api/reservations` – create (multipart: CIN file + reservation data)
  - `GET /api/reservations` – list all
  - `GET /api/reservations/{id}` – get by ID
  - `PUT /api/reservations/{id}` – update
  - `DELETE /api/reservations/{id}` – delete
  - `PATCH /api/reservations/{id}/statut` – change status
- **Entity:** `Reservation` (cin, date, heureDebut, heureFin, prix, statut, ticketPdfPath, cinUploadPath, stadeId)
- **PDF:** Uses OpenPDF library for ticket generation.
- **Uploads:** Files saved to `uploads/` directory at project root.

### frontend-angular
- **Purpose:** Angular 17 SPA; user auth, stadium browsing, reservation booking, admin panel.
- **Port:** 4200
- **Mock mode:** Services default to mock data (`useMockAuth = true`, `useMockData = true` in service files). Toggle these flags to switch to real backend calls.
- **Key services:** `auth.service.ts`, `stadium.service.ts`, `reservation.service.ts`, `mock-data.service.ts`
- **Routes:**
  - `/login`, `/register`
  - `/dashboard`
  - `/stadiums`
  - `/reservation/:id`
  - `/my-bookings`
  - `/admin/dashboard`, `/admin/stadium/create`, `/admin/stadium/edit/:id`
- **API base URL:** `http://localhost:8081` (gateway)
- **PDF export:** jsPDF + jspdf-autotable (client-side ticket generation)
- **SSR:** Angular SSR configured (`main.server.ts`) but optional for development.

---

## Database Configuration

### Development (default) – H2 In-Memory
- Auto-configured per service; no setup needed.
- H2 console accessible at `http://localhost:{port}/h2-console`
- Credentials: username `sa`, password *(empty)*

### Production – PostgreSQL (Supabase)
- Connection properties are in each service's `application-supabase.properties`.
- Activate with `--spring.profiles.active=supabase`.
- **⚠️ Warning:** Credentials are stored in plain text in the repository – use environment variables or secrets management in a real production deployment.

---

## Known Issues and Workarounds

1. **Config Server requires internet access at startup.**  
   If the remote config repo (https://github.com/Jbxsa01/Config-server) is unreachable, `config-service` will fail to start. Workaround: ensure internet access or configure a local fallback using `spring.cloud.config.server.git.clone-on-start=false` and a local file backend.

2. **Services fail to start if Eureka or Config Server are not already running.**  
   Always start `eureka-service` first, then `config-service`, then the business microservices. If a service can't reach Config Server during startup, it may crash. Set `spring.cloud.config.fail-fast=false` per service to make config optional.

3. **Spring Cloud version mismatch.**  
   `eureka-service` uses Spring Cloud `2025.0.0` while other services use `2023.0.x`. Avoid mixing version-specific features across services.

4. **Frontend mock mode is on by default.**  
   The Angular services (`auth.service.ts`, `stadium.service.ts`, `reservation.service.ts`) have mock flags set to `true`. To test against a real backend, set `useMockAuth = false` and `useMockData = false` in each service file.

5. **No Docker Compose file.**  
   Services must be started manually in the correct order (see above). There is no orchestration file to start all services at once.

6. **No CI/CD pipeline.**  
   The `.github/workflows` directory does not exist. There are no automated build, test, or deployment pipelines.

7. **CORS is fully open (`*`) in the gateway.**  
   This is acceptable for development but should be restricted to specific origins in production.

8. **Uploads directory is committed to the repository.**  
   The `uploads/` directory at the root stores CIN documents and images. In production, use external object storage (e.g., S3, Supabase Storage).

9. **Root `pom.xml` is the gateway-service POM.**  
   The root-level `pom.xml` belongs specifically to `gateway-service`, not a true multi-module parent. To build individual services, navigate into each service subdirectory and run `../mvnw`.

---

## Code Conventions

- **Java packages:** `com.doctorat.<service>` (e.g., `com.doctorat.auth`, `com.doctorat.stadium`)
- **REST controllers:** Annotated with `@RestController`, paths follow `/api/<resource>` inside services (gateway prefixes are separate)
- **DTOs:** Named `<Action>Request` / `<Action>Response` (e.g., `LoginRequest`, `AuthResponse`)
- **Spring profiles:** `default` → H2; `supabase` → PostgreSQL
- **Angular components:** Each component has its own folder under `src/app/components/`
- **Angular services:** Located in `src/app/services/`
- **Angular models:** Located in `src/app/models/`

---

## Security Notes

- 🚨 **Critical:** Supabase database credentials are committed in plain text in `application-supabase.properties` files across multiple services. These credentials should be treated as compromised. Immediate actions required:
  1. Rotate the Supabase credentials immediately in the Supabase dashboard.
  2. Replace the hard-coded values with environment variables (e.g., `${DB_PASSWORD}`) or a secrets manager.
  3. Remove the credentials from Git history using a tool like `git filter-repo` or BFG Repo Cleaner to prevent exposure even after updating the files.
  4. Never commit credentials to source control.
- ⚠️ CORS policy uses `*` (all origins). Restrict to the known frontend origin in production.
- ⚠️ No JWT validation is currently enforced at the gateway level; auth is handled per-service.
