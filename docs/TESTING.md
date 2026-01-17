# API Testing Guide

This document provides practical examples for testing the Kanban API during development.

## Prerequisites

Ensure the server is running:

```bash
clj -M -m kanban.core
```

The server will be available at `http://localhost:3000`

## Health Check

### Verify API is running

```bash
curl http://localhost:3000/api/health
```

Expected response:

```json
{
  "message": "ok"
}
```

## Cards - CRUD Operations

### Create a new card

#### Successful creation

```bash
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Implement authentication",
    "description": "Add login and JWT",
    "status": "todo"
  }'
```

Expected response (201):

```json
{
  "message": "Card created successfully",
  "card": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "title": "Implement authentication",
    "description": "Add login and JWT",
    "status": "todo",
    "created-at": "2026-01-17T10:30:00Z",
    "updated-at": "2026-01-17T10:30:00Z"
  }
}
```

#### Create card without description (optional field)

```bash
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Code review",
    "status": "doing"
  }'
```

#### Validation errors

**Missing required status field:**

```bash
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Invalid card"
  }'
```

Expected response (400):

```json
{
  "error": "Validation failed",
  "details": {
    "status": ["missing required key"]
  }
}
```

**Invalid status value:**

```bash
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Invalid card",
    "status": "invalid-status"
  }'
```

Expected response (400):

```json
{
  "error": "Validation failed",
  "details": {
    "status": ["should be one of: todo, doing, done"]
  }
}
```

**Extra fields not allowed (closed map validation):**

```bash
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Card with extra field",
    "status": "todo",
    "hacker_field": "malicious"
  }'
```

Expected response (400):

```json
{
  "error": "Validation failed",
  "details": {
    "hacker_field": ["disallowed key"]
  }
}
```

### List all cards

```bash
curl http://localhost:3000/api/cards
```

Expected response:

```json
{
  "cards": [
    {
      "id": "123...",
      "title": "Card 1",
      "description": "...",
      "status": "todo",
      "created-at": "...",
      "updated-at": "..."
    },
    {
      "id": "456...",
      "title": "Card 2",
      "description": "...",
      "status": "doing",
      "created-at": "...",
      "updated-at": "..."
    }
  ]
}
```

### Filter cards by status

List only "todo" cards:

```bash
curl "http://localhost:3000/api/cards?status=todo"
```

List only "doing" cards:

```bash
curl "http://localhost:3000/api/cards?status=doing"
```

List only "done" cards:

```bash
curl "http://localhost:3000/api/cards?status=done"
```

### Get board view (grouped by status)

```bash
curl http://localhost:3000/api/cards/board
```

Expected response:

```json
{
  "todo": [
    { "id": "...", "title": "Card in TODO", ... }
  ],
  "doing": [
    { "id": "...", "title": "Card in DOING", ... }
  ],
  "done": [
    { "id": "...", "title": "COMPLETED card", ... }
  ]
}
```

### Get specific card by ID

```bash
curl http://localhost:3000/api/cards/{CARD_ID}
```

Example:

```bash
curl http://localhost:3000/api/cards/123e4567-e89b-12d3-a456-426614174000
```

Expected response (200):

```json
{
  "card": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "title": "Implement authentication",
    "description": "Add login and JWT",
    "status": "todo",
    "created-at": "2026-01-17T10:30:00Z",
    "updated-at": "2026-01-17T10:30:00Z"
  }
}
```

#### Not found error

```bash
curl http://localhost:3000/api/cards/non-existent-id
```

Expected response (404):

```json
{
  "error": "Card not found",
  "id": "non-existent-id"
}
```

### Update a card

#### Update title and description

```bash
curl -X PUT http://localhost:3000/api/cards/{CARD_ID} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated title",
    "description": "New description"
  }'
```

#### Update only status

```bash
curl -X PUT http://localhost:3000/api/cards/{CARD_ID} \
  -H "Content-Type: application/json" \
  -d '{
    "status": "doing"
  }'
```

#### Update all fields

```bash
curl -X PUT http://localhost:3000/api/cards/{CARD_ID} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Completely updated card",
    "description": "New description",
    "status": "done"
  }'
```

Expected response (200):

```json
{
  "message": "Card updated successfully",
  "card": {
    "id": "...",
    "title": "Completely updated card",
    "description": "New description",
    "status": "done",
    "created-at": "2026-01-17T10:30:00Z",
    "updated-at": "2026-01-17T11:45:00Z"
  }
}
```

#### Validation error with extra field

```bash
curl -X PUT http://localhost:3000/api/cards/{CARD_ID} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test",
    "invalid_field": "value"
  }'
```

Expected response (400):

```json
{
  "error": "Validation failed",
  "details": {
    "invalid_field": ["disallowed key"]
  }
}
```

### Move card to another column

```bash
curl -X PATCH http://localhost:3000/api/cards/{CARD_ID}/move \
  -H "Content-Type: application/json" \
  -d '{
    "status": "done"
  }'
```

Expected response (200):

```json
{
  "message": "Card moved successfully",
  "card": {
    "id": "...",
    "title": "...",
    "description": "...",
    "status": "done",
    "created-at": "...",
    "updated-at": "2026-01-17T12:00:00Z"
  }
}
```

### Delete a card

```bash
curl -X DELETE http://localhost:3000/api/cards/{CARD_ID}
```

Expected response (204):

```
(no response body)
```

#### Not found error

```bash
curl -X DELETE http://localhost:3000/api/cards/non-existent-id
```

Expected response (404):

```json
{
  "error": "Card not found",
  "id": "non-existent-id"
}
```

## Complete Test Flow

Execute this flow to test all operations:

```bash
# 1. Check API health
curl http://localhost:3000/api/health

# 2. Create a card
CARD_RESPONSE=$(curl -s -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{"title":"Complete test","status":"todo"}')

echo $CARD_RESPONSE

# 3. Extract card ID (PowerShell or Bash)
# PowerShell:
$CARD_ID = ($CARD_RESPONSE | ConvertFrom-Json).card.id

# Bash/Linux:
# CARD_ID=$(echo $CARD_RESPONSE | jq -r '.card.id')

# 4. List all cards
curl http://localhost:3000/api/cards

# 5. Get specific card
curl http://localhost:3000/api/cards/$CARD_ID

# 6. Update the card
curl -X PUT http://localhost:3000/api/cards/$CARD_ID \
  -H "Content-Type: application/json" \
  -d '{"status":"doing"}'

# 7. Move the card
curl -X PATCH http://localhost:3000/api/cards/$CARD_ID/move \
  -H "Content-Type: application/json" \
  -d '{"status":"done"}'

# 8. View board
curl http://localhost:3000/api/cards/board

# 9. Delete the card
curl -X DELETE http://localhost:3000/api/cards/$CARD_ID
```

## Validation Tests (Error Cases)

### Required status field

```bash
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{"title":"No status"}'
# Expected: 400 - Validation failed
```

### Invalid status value

```bash
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{"title":"Wrong status","status":"blocked"}'
# Expected: 400 - Validation failed
```

### Title too long (> 200 characters)

```bash
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{
    "title":"'$(printf 'A%.0s' {1..250})'",
    "status":"todo"
  }'
# Expected: 400 - Validation failed
```

### Extra field not allowed (:closed true)

```bash
curl -X POST http://localhost:3000/api/cards \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Test",
    "status":"todo",
    "extra_field":"not_allowed"
  }'
# Expected: 400 - Validation failed (disallowed key)
```

## Active Validations

The API now validates:

- Status is **required** when creating cards
- Status must be one of: `todo`, `doing`, or `done`
- Extra fields are **rejected** (`:closed true`)
- Title: minimum 1, maximum 200 characters
- Description: maximum 1000 characters
- Error handling uses `cond` (more idiomatic)

## Development Tips

### Using PowerShell (Windows)

```powershell
# Create card and save response
$response = Invoke-RestMethod -Method Post -Uri http://localhost:3000/api/cards `
  -ContentType "application/json" `
  -Body '{"title":"Test","status":"todo"}'

# Get card ID
$response.card.id

# Update card
Invoke-RestMethod -Method Put -Uri "http://localhost:3000/api/cards/$($response.card.id)" `
  -ContentType "application/json" `
  -Body '{"status":"done"}'
```

### Using Postman/Insomnia

1. Import the examples above
2. Create an environment variable `baseUrl = http://localhost:3000`
3. Create a `cardId` variable to reuse between requests

## Notes

- All data is currently stored **in-memory** using Clojure atoms
- This is a temporary solution for development and testing
- Data is lost when the server restarts
- PostgreSQL integration will be implemented in future releases for persistent storage
- Use `seed-data!` in the REPL to quickly populate test data
