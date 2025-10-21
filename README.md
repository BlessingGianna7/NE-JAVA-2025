# Enterprise Payroll System (ERP)

Full-stack payroll management system with JWT authentication and automated salary processing. Built as a **High School Final Capstone Project**.

## Features
- JWT-based authentication with role-based access (Admin, Manager, Employee)
- Automated monthly payroll generation
- PDF payslip generation and email notifications
- Configurable deductions (tax, pension, medical, etc.)
- 40+ RESTful API endpoints

## Tech Stack
**Java 17** • **Spring Boot 3** • **PostgreSQL** • **Spring Security** • **JWT** • **OpenPDF** • **JavaMailSender**

## Quick Start

```bash
# Setup database
createdb ne

# Configure application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ne
spring.datasource.username=postgres
spring.datasource.password=your_password

# Run application
mvn spring-boot:run
```

Access API docs at: `http://localhost:8080/swagger-ui.html`

## Key Endpoints

```http
POST /api/auth/register          # Register user
POST /api/auth/login             # Get JWT token
GET  /api/employees              # List employees (Manager)
POST /api/payslips/generate      # Generate payroll (Manager)
PUT  /api/payslips/{id}/approve  # Approve payslip (Admin)
GET  /api/payslips/{id}/download # Download PDF
```

## Authentication Example

```json
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "roles": ["ROLE_EMPLOYEE"]
}
```

Use token in headers: `Authorization: Bearer <token>`


## Project Structure
```
src/main/java/com/example/erp/
├── controller/    # REST endpoints
├── service/       # Business logic
├── model/         # JPA entities
├── repository/    # Data access
├── security/      # JWT & auth
└── util/          # PDF generation
```

## Author
**Gianna Blessing Ishema**  
GitHub: [@BlessingGianna7](https://github.com/BlessingGianna7) • Email: gishema@brynmawr.edu

---

**License:** MIT
