# Boxy Service

A Java Spring Boot application for calculating optimal box sizes for item packaging. Boxy helps determine the smallest
box that can efficiently contain a given set of items with different dimensions.

## Overview

Boxy Service uses bin packing algorithms to solve the problem of efficiently packing items into standard-sized boxes.
The service provides a RESTful API that accepts a list of items with quantities and returns the optimal box size
required to contain them.

## Running with Maven

You can run the application directly using Maven:

```bash
# Build and run the application
mvn clean package
mvn spring-boot:run
```

Access swagger: http://localhost:8080/swagger-ui/index.html

## Features

- **Bin Packing Algorithm**: Multi-pass algorithm with first-fit decreasing for larger items, gap filling, and random
  fill attempts
- **Standard Box Sizes**: Pre-defined box dimensions (small, medium, large)
- **Item Size Definitions**: Support for 8 standard item sizes with different widths
- **Configurable Fill Factor**: Adjustable fill factor parameter to control packing density
- **RESTful API**: Simple HTTP interface with JSON request/response format
- **OpenAPI Documentation**: Interactive API documentation with Swagger UI
- **Comprehensive Validation**: Input validation with detailed error messages

## Box Sizes

The service supports three standard box sizes:

| Box Name | Label    | Dimensions |
|----------|----------|------------|
| Box 1    | Box nr 1 | 4x5        |
| Box 2    | Box nr 2 | 8x12       |
| Box 3    | Box nr 3 | 12x20      |

If items cannot fit in any standard box, the service will return "Pickup required".

## Item Types

The following item types are available:

| Item ID | Dimensions |
|---------|------------|
| ITEM_1  | 1x1        |
| ITEM_2  | 2x1        |
| ITEM_3  | 4x1        |
| ITEM_4  | 5x1        |
| ITEM_5  | 6x1        |
| ITEM_6  | 8x1        |
| ITEM_7  | 9x1        |
| ITEM_8  | 12x1       |

## API Usage

### Calculate Box Size

**Endpoint:** `POST /calculate-size`

**Request:**

```json
{
  "items": [
    {
      "item": "ITEM_1",
      "quantity": 5
    },
    {
      "item": "ITEM_3",
      "quantity": 2
    }
  ],
  "fillFactor": 0.75
}
