# Rubik’s Cube Backend API

A RESTful backend API that simulates a **3×3 Rubik’s Cube**, supporting layer rotations across all axes with **undo/redo functionality**.  
Built using **Java**, **Spring Boot**, and **object-oriented design principles**.

This project focuses on **cube state modeling**, **rotation logic**, and **clean API design** rather than UI.

---

## Features

- **Accurate Rubik’s Cube simulation**
  - Supports rotations along **X, Y, and Z axes**
  - Layer-based operations (index 0–2)
  - Clockwise / counter-clockwise directions
- **Undo / Redo support**
  - Uses stacks to track operation history
- **RESTful API**
  - Programmatic interaction with the cube
- **Modular OOP design**
  - Clear separation between controller, service, and domain logic
- **Validation & error handling**
  - Invalid operations return appropriate HTTP responses

---

## Tech Stack

- **Java**
- **Spring Boot**
- **JUnit**
- **REST APIs**
- **Object-Oriented Design**

---

## Cube Model Overview

- The cube consists of **6 faces**, each with **9 cells**
- Each `Cell` has a fixed `Color`
- Cube rotations are handled by:
  - Mapping face indices
  - Rotating face grids
  - Updating adjacent layers correctly

Core domain classes:
- `Cube` – manages cube state and rotation logic
- `Operation` – represents a single move (axis, direction, layer)
- `Cell` – represents one colored cube unit

---

## API Endpoints

### Get Current Cube State
GET /cube

### Get Current Cube State
POST /cube/operate
**Request Body**
```json
{
  "axis": "X",
  "direction": 0,
  "index": 1
}
```
**Parameters**
| Field     | Description              |
|-----------|--------------------------|
| axis      | X, Y, or Z               |
| direction | 0 or 1                   |
| index     | Layer index (0–2)        |

### Undo Last Operation
PUT /cube/undo

### Redo Last Undone Operation
PUT /cube/redo

## Testing
- Core logic is designed to be testable independently of the controller

- JUnit tests validate:

  - Cube state transitions

  - Stack behavior for undo/redo

  - Input validation
