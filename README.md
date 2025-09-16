# 🛒 OrderUp - Reliable Product Ordering API

**OrderUp** is a robust, production-grade **Spring Boot REST API** designed to handle product orders with guaranteed **thread-safety**, **centralized exception management**, and **comprehensive test coverage**. Built with a focus on clean architecture and consistent API behavior, it ensures that stock levels are updated safely, even under concurrent access.

---

## 🚀 Core Capabilities

- 🧾 Submit product orders via a REST endpoint.
- 🔐 Prevent overselling through thread-safe stock deductions.
- 🧼 Clean JSON error responses powered by centralized exception handling.
- 🧱 Clear separation of concerns using DTOs.
- ✅ Comprehensive unit and integration testing with >80% coverage using **JaCoCo**.

---

## 🧠 Architecture Breakdown

### 🎯 Controller: `OrderController`

- Exposes `/orders` POST endpoint.
- Accepts `OrderRequest` payload and returns `OrderResponse`.
- Delegates all business logic to the service layer.

### ⚙️ Service: `OrderService`

Handles all business rules:

- Verifies product existence.
- Checks available stock.
- Deducts quantity using thread-safe logic.

> 🧵 **Concurrency Safe**:  
> Critical stock updates are wrapped in a `synchronized` block (or handled via DB-level transactions) to prevent race conditions.

### 🧩 Model Layer

- `Product` entity represents persisted data.
- `OrderRequest` and `OrderResponse` act as clean transport models for the API.

### 🚨 Error Handling: `GlobalExceptionHandler`

Centralized using `@RestControllerAdvice`, it captures and formats exceptions such as:

- `ProductNotFoundException` → `404 Not Found`
- `InsufficientStockException` → `400 Bad Request`
- Generic exceptions → `500 Internal Server Error`

All errors follow a consistent structure for client-side clarity.

---

## 🔁 Order Processing Flow


### 📥 Example Request
Client → OrderController → OrderService → Product Repository → Response

POST /orders
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
📤 Example Success Response
{
  "orderId": 101,
  "productId": 1,
  "quantity": 2,
  "status": "CONFIRMED"
}

❌ Example Error Response
{
  "timestamp": "2025-09-14T10:30:45Z",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found"
}
🧪 Testing Strategy
🔹 Unit Tests

Validate business rules in OrderService.

Simulate error scenarios like missing products or insufficient stock.

🔹 Integration Tests

Use MockMvc to test full HTTP lifecycle.

Validate both success and failure cases through simulated POST requests.

📊 Code Coverage with JaCoCo
mvn clean test
mvn jacoco:report
Open report in browser:

target/site/jacoco/index.html


🎯 Goal: >80% coverage for core logic and exception handling.

🧪 Manual Testing with curl
✅ Place an Order

curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'

❌ Product Not Found

curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": 999, "quantity": 1}'

❌ Not Enough Stock

   curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 1000}'

▶️ Running the Application
Prerequisites:

Java 17+

Maven 3.8+

Start the server:
mvn spring-boot:run


API will be available at:

http://localhost:8080/orders

📌 Project Highlights

Clean and modular architecture with proper layering.

High reliability in concurrent environments.

Consistent and informative error handling.

80%+ tested business logic with automatic coverage reporting.
