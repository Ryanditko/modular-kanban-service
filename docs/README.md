# Clojure Kanban API

<p align="center">
  <img src="blob:https://imgur.com/80c8d72c-4c08-45c0-be8d-48dd97c05896" alt="kanban" width="800">
</p>

## Overview
A RESTful API backend for managing Kanban cards built with Clojure. This project provides endpoints for creating, reading, updating, and deleting cards across three status columns: todo, doing, and done.

**Note:** This is a backend-only API with no frontend interface. Use tools like Postman, Insomnia, or curl to interact with the endpoints.

## Tech Stack
- **Language:** Clojure 1.11.1
- **Web Server:** Jetty (Ring adapter)
- **Routing:** Reitit
- **Validation:** Malli (schema validation)
- **JSON:** Jsonista
- **Database:** PostgreSQL with next.jdbc and HikariCP
- **Migrations:** Migratus

## Setup Instructions

### 1. Prerequisites
- Java 11+ installed
- Clojure CLI tools installed

### 2. Clone the repository
```bash
git clone https://github.com/Ryanditko/clojure-kanban.git
cd clojure-kanban
```

### 3. Configure environment variables
Create a `.env` file in the project root:

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/kanban_dev
DATABASE_USER=your_username
DATABASE_PASSWORD=your_password
```

### 4. Run database migrations
```bash
clj -M:migratus migrate
```

### 5. Run the application
```bash
clj -M -m kanban.core
```

The server will start on `http://localhost:3000`

## API Endpoints

### Health Check
- `GET /health` - Check if the API is running

### Cards Management

#### List all cards
```bash
GET /cards
GET /cards?status=todo  # Filter by status
```

#### Create a new card
```bash
POST /cards
Content-Type: application/json

{
  "title": "Implement login feature",
  "description": "Create authentication screen and API",
  "status": "todo"
}
```

#### Get a specific card
```bash
GET /cards/:id
```

#### Update a card
```bash
PUT /cards/:id
Content-Type: application/json

{
  "title": "Updated title",
  "description": "Updated description",
  "status": "doing"
}
```

#### Delete a card
```bash
DELETE /cards/:id
```

## Card Structure

Each card has the following fields:

```json
{
  "id": "uuid-string",
  "title": "Card title",
  "description": "Card description",
  "status": "todo|doing|done",
  "created-at": "2026-01-16T10:30:00Z",
  "updated-at": "2026-01-16T10:30:00Z"
}
```

## Testing with curl

### Create a card
```bash
curl -X POST http://localhost:3000/cards \
  -H "Content-Type: application/json" \
  -d '{"title":"My first card","status":"todo"}'
```

### List all cards
```bash
curl http://localhost:3000/cards
```

### Filter cards by status
```bash
curl "http://localhost:3000/cards?status=doing"
```

For more detailed testing examples, see [CRUD.md](./CRUD.md)

## Development

### Project Structure
```
src/
├── kanban/
│   ├── core.clj              # Application entry point
│   ├── migrations.clj        # Database migration runner
│   ├── db/
│   │   ├── core.clj          # Database connection
│   │   └── card.clj          # Card repository (SQL queries)
│   ├── domain/
│   │   └── cards.clj         # Business logic
│   ├── http/
│   │   ├── handlers/
│   │   │   ├── cards.clj     # Card HTTP handlers
│   │   │   ├── health.clj    # Health check handler
│   │   │   └── errors.clj    # Error handlers
│   │   ├── routes/
│   │   │   ├── api.clj       # Route composition
│   │   │   ├── cards.clj     # Card routes
│   │   │   └── health.clj    # Health routes
│   │   ├── routes.clj        # Router configuration
│   │   ├── responses.clj     # HTTP response helpers
│   │   └── schemas/
│   │       └── cards.clj     # Malli validation schemas
│   └── infra/
│       └── middleware.clj    # JSON and DB middleware
test/
├── kanban/
│   ├── core_test.clj
│   ├── db/
│   │   ├── core_test.clj     # Database tests
│   │   └── config_test.clj   # Config validation tests
│   ├── domain/
│   │   └── cards_test.clj    # Business logic tests
│   └── http/
│       ├── handlers/
│       │   └── cards_test.clj # Handler tests
│       ├── routes/
│       │   └── routes_test.clj # Routing tests
│       └── schemas/
│           └── cards_test.clj # Schema validation tests
```

### Running Tests

```bash
clj -M:test
```

For detailed testing documentation, see [TESTS.md](./TESTS.md)

## Error Handling

The API returns appropriate HTTP status codes:

- `200 OK` - Successful GET/PUT requests
- `201 Created` - Successful POST requests
- `204 No Content` - Successful DELETE requests
- `400 Bad Request` - Invalid input data or validation errors
- `404 Not Found` - Card not found
- `500 Internal Server Error` - Unexpected server errors

### Validation

The API uses Malli schemas for request validation:
- **Title:** 1-200 characters (required)
- **Description:** 0-1000 characters (optional)
- **Status:** Must be one of `todo`, `doing`, or `done` (required)
- **Closed maps:** Extra fields are rejected

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Card assignment to users
- [ ] Tags/labels
- [ ] Due dates
- [ ] Card comments
- [ ] Search and advanced filtering
- [ ] API documentation (Swagger/OpenAPI)
- [ ] Pagination for large datasets

## License
This project is licensed under the MIT License.
