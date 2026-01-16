# Clojure Kanban API

## Overview
A RESTful API backend for managing Kanban cards built with Clojure. This project provides endpoints for creating, reading, updating, and deleting cards across three status columns: todo, doing, and done.

**Note:** This is a backend-only API with no frontend interface. Use tools like Postman, Insomnia, or curl to interact with the endpoints.

## Tech Stack
- **Language:** Clojure 1.11.1
- **Web Server:** Jetty (Ring adapter)
- **Routing:** Reitit
- **JSON:** Jsonista
- **Storage:** In-memory (Atom)

## Setup Instructions

### 1. Prerequisites
- Java 11+ installed
- Clojure CLI tools installed

### 2. Clone the repository
```bash
git clone https://github.com/Ryanditko/clojure-kanban.git
cd clojure-kanban
```

### 3. Install dependencies
Dependencies are managed via `deps.edn` and will be downloaded automatically.

### 4. Run the application
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
GET /api/cards
GET /api/cards?status=todo  # Filter by status
```

#### Get board view (grouped by status)
```bash
GET /api/cards/board
```

Response:
```json
{
  "todo": [...],
  "doing": [...],
  "done": [...]
}
```

#### Create a new card
```bash
POST /api/cards
Content-Type: application/json

{
  "title": "Implement login feature",
  "description": "Create authentication screen and API",
  "status": "todo"
}
```

#### Get a specific card
```bash
GET /api/cards/:id
```

#### Update a card
```bash
PUT /api/cards/:id
Content-Type: application/json

{
  "title": "Updated title",
  "description": "Updated description",
  "status": "doing"
}
```

#### Move a card to another status
```bash
PATCH /api/cards/:id/move
Content-Type: application/json

{
  "status": "done"
}
```

#### Delete a card
```bash
DELETE /api/cards/:id
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
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{"title":"My first card","status":"todo"}'
```

### List all cards
```bash
curl http://localhost:3000/api/cards
```

### Get board view
```bash
curl http://localhost:3000/api/cards/board
```

## Development

### Project Structure
```
src/
├── kanban/
│   ├── core.clj              # Application entry point
│   ├── domain/
│   │   └── cards.clj         # Business logic
│   ├── http/
│   │   ├── handlers/
│   │   │   ├── cards.clj     # Card HTTP handlers
│   │   │   ├── health.clj    # Health check handler
│   │   │   └── errors.clj    # Error handlers
│   │   ├── routes/
│   │   │   └── routes.clj    # Route definitions
│   │   ├── routes.clj        # Router configuration
│   │   └── responses.clj     # HTTP response helpers
│   └── infra/
│       └── middleware.clj    # JSON middleware
```

### REPL Development

Start a REPL and load the namespace:
```clojure
(require '[kanban.domain.cards :as cards])

;; Create sample data
(cards/seed-data!)

;; List all cards
(cards/list-cards)

;; Get board view
(cards/get-board)

;; Clear all cards
(cards/clear-all!)
```

## Error Handling

The API returns appropriate HTTP status codes:

- `200 OK` - Successful GET/PUT requests
- `201 Created` - Successful POST requests
- `204 No Content` - Successful DELETE requests
- `400 Bad Request` - Invalid input data
- `404 Not Found` - Card not found
- `405 Method Not Allowed` - Invalid HTTP method

## Future Enhancements

- [ ] PostgreSQL database integration
- [ ] User authentication
- [ ] Card assignment to users
- [ ] Tags/labels
- [ ] Due dates
- [ ] Card comments
- [ ] Search and filtering
- [ ] API documentation (Swagger)

## License
This project is licensed under the MIT License.
