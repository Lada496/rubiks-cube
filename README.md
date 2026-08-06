# Rubik's Cube Backend API

A REST API that models a **3×3 Rubik's Cube**, including layer rotations and undo/redo history. The project focuses on cube-state modeling, object-oriented design, validation, and testable backend logic.

## Features

- Rotate any layer along the X, Y, or Z axis
- Apply clockwise and counter-clockwise operations
- Undo and redo cube operations using history stacks
- Retrieve the current cube state through an API
- Validate operations and return appropriate HTTP errors
- Test cube transitions, controller behavior, history, and input validation

## Technology

- Java 17
- Spring Boot 3
- JUnit
- Maven

## Design

The backend separates HTTP handling from the cube domain model:

- `Cube` manages cube state and rotation logic
- `Operation` represents an axis, direction, and layer
- `Cell` represents one colored cube unit
- Controllers expose the model through REST endpoints
- A global exception handler translates invalid operations into HTTP responses

The implementation lives in [`rubiks-cube-backend/`](rubiks-cube-backend/).

## API

| Method | Endpoint | Purpose |
|---|---|---|
| `GET` | `/cube` | Return the current cube state |
| `POST` | `/cube/operate` | Apply a rotation |
| `PUT` | `/cube/undo` | Undo the previous operation |
| `PUT` | `/cube/redo` | Reapply the most recently undone operation |

Example operation:

```json
{
  "axis": "X",
  "direction": 0,
  "index": 1
}
```

- `axis`: `X`, `Y`, or `Z`
- `direction`: `0` or `1`
- `index`: layer index from `0` to `2`

## Run locally

Requirements: Java 17 and Maven.

```bash
cd rubiks-cube-backend
mvn spring-boot:run
```

Run the tests:

```bash
cd rubiks-cube-backend
mvn test
```
