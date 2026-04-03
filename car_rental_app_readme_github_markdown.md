# 🚗 Car Rental App

> Automation Testing Project • Test Plan • UI + API + DB Testing

## ✨ Project Overview

Car Rental App is a web application for booking and renting cars. The goal of this project is to create a unified automation framework for testing the Car Rental web application and validating the complete business process for both **Client** and **Admin** roles.

The project includes:

- UI testing using Selenium WebDriver
- API testing using Rest Assured
- Database validation using JDBC + MySQL
- Unified automation framework based on Java + TestNG
- End-to-end testing scenarios for Client and Admin roles

---

## 🎯 Project Goals

- Validate all major user scenarios
- Ensure consistency between UI, API, and Database
- Reduce manual regression time
- Detect defects at an early stage
- Build scalable and maintainable automation

---

## 🧰 Technologies and Tools

| Tool | Purpose |
|------|----------|
| Java 21 | Main automation language |
| Gradle | Project build and execution |
| TestNG | Test suite management |
| Selenium WebDriver | UI automation |
| Rest Assured | API automation |
| JDBC | Database validation |
| MySQL | Test database |
| Lombok | Reduce boilerplate code |
| Logback | Logging |
| Allure | Test reporting |
| Jenkins | CI/CD integration |
| GitHub | Version control |
| Trello | Defect tracking |

---

## 💡 Why These Technologies Were Chosen

| Technology | Reason |
|-----------|---------|
| Java 21+ | Modern and stable language for automation |
| TestNG | Flexible test suites, annotations, parallel execution |
| Selenium WebDriver | Reliable UI testing across multiple browsers |
| Rest Assured | Easy API testing and schema validation |
| JDBC | Direct validation of data stored in the database |
| Gradle | Convenient dependency and build management |
| Allure + Jenkins | Reporting and CI/CD integration |

---

## 🚧 Challenges Found During the Project

- Some endpoints from the User Stories are missing in Swagger:
  - `PATCH /users/me`
  - `POST /logout`
- Unstable local environment and changing API contracts
- Tests depend on database state and authorization tokens
- Need to synchronize UI, API, and DB validation within a single framework

---

## 🔗 Test Environment

| Environment | URL |
|------------|-----|
| Frontend | `http://localhost:5173/` |
| API v3 | `https://dev.pshacks.org/api/v3/` |
| Database | MySQL |

---

## 🎯 Testing Objective

Testing should confirm that:

- [x] Client can register and log in
- [x] Client can browse available cars and categories
- [x] Client can create and cancel bookings
- [x] Client can view booking history
- [x] Client can leave and view reviews
- [x] Admin can manage bookings
- [x] Admin can change car status
- [x] API returns correct data
- [x] UI, API, and Database work consistently within one automation framework

---

## 🧭 Main User Flow

```text
Registration / Login
        ↓
      Profile
        ↓
 Cars and Categories
        ↓
      Booking
        ↓
  Booking History
        ↓
  Admin Processing
        ↓
      Reviews
```

---

## 🧪 Scope of Testing

### ✅ In Scope

- Registration
- Login / Logout
- Profile page
- Cars page
- Car categories
- Booking creation
- Booking history
- Booking cancellation
- Reviews
- API v3 endpoints
- Database consistency validation
- Authorization and token handling
- UI + API + DB integration

### ❌ Out of Scope

- Payment systems
- Mobile application
- Email / SMS notifications
- Third-party integrations
- Full accessibility testing

---

## ⚙️ Installation and Setup

### Prerequisites

Before running the project, install:

- Java 21+
- Gradle 9.3.0+
- Chrome / Firefox / Safari
- MySQL
- Git

### Clone Repository

```bash
git clone git@github.com:vitrom7777/CarBookingV1.git
cd CarBookingV1
```

### Configure Environment

Create a `.env` file or update your configuration:

```env
BASE_URL=http://localhost:5173/
API_URL=https://dev.pshacks.org/api/v3/
DB_URL=jdbc:mysql://localhost:3306/car_rental
DB_USER=root
DB_PASSWORD=your_password
```

### Build Project

```bash
./gradlew clean build
```

### Run All Tests

```bash
./gradlew clean test
```

### Run a Specific Test Suite

```bash
./gradlew test -DsuiteXmlFile=testng.xml
```

---

## 🖥️ Supported Browsers

- Chrome
- Firefox
- Safari

## 💻 Supported Operating Systems

- Windows 11
- macOS

---

## 📁 Project Structure

```text
src
├── main
├── test
│   ├── ui
│   ├── api
│   ├── db
│   └── integration
├── resources
│   ├── testng.xml
│   ├── config.properties
│   └── testdata
└── docs
    ├── swagger
    └── postman
```

---

## 📷 Documentation

### Swagger API

Base URL:

```text
https://dev.pshacks.org/api/v3/
```

Swagger and Postman collections can be stored in:

```text
docs/swagger
docs/postman
```

### Useful Links

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Rest Assured Documentation](https://rest-assured.io/)
- [TestNG Documentation](https://testng.org/)
- [Allure Report Documentation](https://docs.qameta.io/allure/)
- [HTTP Status Codes Reference](https://http.cat/status/100)

---

## 👨‍💻 QA Team

| Role | Team Member | Responsibility |
|------|-------------|----------------|
| QA Lead | Yana Yerusalymska | Test Plan, TestLink, Trello, Reporting |
| QA API | Vitalii Romanskyi | API testing, DB testing, Postman, Rest Assured |
| QA Manual | Alexandr Karpov | Manual testing, State Transition Diagrams |
| QA GUI | Oleh Hanziienko | UI automation, Jenkins, Allure |
| QA Manual | Tetiana Nosenko | Manual testing, Mind Maps |

---

## ⚠️ Risks

- Unstable QA / local environment
- API contract changes
- Frontend or backend development delays
- Lack of test data
- No access to admin functionality
- Data inconsistency between UI, API, and DB
- Unstable tests caused by database state
- Authorization token issues

---

## 🚀 Test Execution

Run all tests:

```bash
./gradlew clean test
```

Run a specific test suite:

```bash
./gradlew test -DsuiteXmlFile=testng.xml
```

---

# 🌟 Quality in Motion. Test Everything. 🌟

