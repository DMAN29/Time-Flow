# TimeFlow - Industry Product Development Time Study

## Overview

TimeFlow is a comprehensive system for analyzing and tracking the time study of industry product development. It allows users to create and manage orders, conduct time studies on operations, and manage user authentication and roles efficiently. It provides robust API management and security features to ensure seamless integration and secure access control.

## Features

- User Authentication & Authorization (JWT-based security)
- Role-based Access Control
- Create, retrieve, and update time study records
- Calculate average time, capacity per hour (PH), and capacity per day (PD)
- Store and retrieve laps in both milliseconds and formatted time
- Assign remarks to individual operators
- RESTful APIs for seamless integration
- MongoDB as the database for storing time study records
- Swagger documentation for easy API testing and reference

## Models

### User

Stores user information, roles, and authentication details.

```java
public class User {
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> role;
    private String password;
    private LocalDateTime createdAt;
}
```

### Order

Stores order details, production targets, and efficiency metrics.

```java
public class Order {
    private String id;
    private String styleNo;
    private String itemNo;
    private String description;
    private String fabric;
    private String division;
    private String buyer;
    private int orderQuantity;
    private int target;
    private Double efficiency;
    private int designOutput;
    private Double totalSam;
    private Double totalAllocation;
    private Double totalRequired;
    private List<Operation> operations;
    private LocalDateTime createdAt;
    private LocalDateTime deadLine;
    private int allowance;
    private int lane;
}
```

### TimeStudy

Stores time study details for different operations in the production process.

```java
public class TimeStudy {
	private String id;
	private String styleNo;
	private String operatorName;
	private String operatorId;
	private String operationName;
	private String section;
	private String machineType;
	private List<String> laps;
	private List<Long> lapsMS;
	private String avgTime;
	private Integer capacityPH;
	private Integer capacityPD;
	private String allowanceTime;
	private String remarks;
	private Double assigned;
}
```

## API Endpoints

### User Authentication & Management

#### Register a New User

- **POST** `http://localhost:8080/register`

**Request Body:**

```json
{
  "userId": "ARVIND002",
  "firstName": "Anurag",
  "lastName": "Kumar",
  "email": "anurag@gmail.com",
  "password": "password123"
}
```

**Response Body:**

```json
{
  "id": "67e4b0134139b7678b84769c",
  "userId": "ARVIND002",
  "firstName": "Anurag",
  "lastName": "Kumar",
  "email": "anurag@gmail.com",
  "role": ["ROLE_USER"],
  "createdAt": "2025-03-27T07:25:31.292"
}
```

#### Authenticate and Generate a JWT Token

- **POST** `http://localhost:8080/login`

**Request Body:**

```json
{
  "email": "anurag@gmail.com",
  "password": "password123"
}
```

**Response Body:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Retrieve All Users

- **GET** `http://localhost:8080/api/user`

**Response Body:**

```json
[
  {
    "id": "67dfab7eb538314a0b4d3cf2",
    "userId": "ARVIND000",
    "firstName": "Admin",
    "lastName": "Admin",
    "email": "admin@gmail.com",
    "role": ["ROLE_USER", "ROLE_HEAD", "ROLE_ADMIN"],
    "createdAt": "2025-03-23T12:04:38.001"
  }
]
```

#### Retrieve User Profile

- **GET** `http://localhost:8080/api/user/profile`

**Response Body:**

```json
{
  "id": "67e4b0134139b7678b84769c",
  "userId": "ARVIND002",
  "firstName": "Anurag",
  "lastName": "Kumar",
  "email": "anurag@gmail.com",
  "role": ["ROLE_USER"],
  "createdAt": "2025-03-27T07:25:31.292"
}
```

#### Change User Role

- **PUT** `http://localhost:8080/api/user/change-role?email=vaibhav@gmail.com`

**Response Body:**

```json
{
  "message": "Role Changed For Email: vaibhav@gmail.com"
}
```

#### Delete a User

- **DELETE** `http://localhost:8080/api/user/delete?email=anurag@gmail.com`

**Response Body:**

```json
{
  "message": "User deleted with email: anurag@gmail.com"
}
```

## API Endpoints

### Order Management

#### Create an Order

- **POST** `http://localhost:8080/api/order`

**Request Body:**

```json
{
  "styleNo": "12346",
  "itemNo": "12346",
  "description": "Plain Shirt",
  "fabric": "Cotton",
  "division": "South",
  "buyer": "US POLO",
  "orderQuantity": 1000000,
  "target": 1000,
  "efficiency": 80
}
```

**Response Body:**

```json
{
  "id": "67e4b4954139b7678b84769d",
  "styleNo": "12346",
  "itemNo": "12346",
  "description": "Plain Shirt",
  "fabric": "Cotton",
  "division": "South",
  "buyer": "US POLO",
  "orderQuantity": 1000000,
  "target": 1000,
  "lineDesign": null,
  "efficiency": 80.0,
  "designOutput": 800,
  "totalSam": null,
  "totalAllocation": null,
  "totalRequired": null,
  "operations": null,
  "createdAt": "2025-03-27T07:44:45.291",
  "deadLine": null,
  "allowance": 20,
  "lane": 2
}
```

#### Retrieve Order by Style Number

- **GET** `http://localhost:8080/api/order/style/{styleNo}`

**Response Body:**

```json
{
  "id": "67e4b4954139b7678b84769d",
  "styleNo": "12346",
  "itemNo": "12346",
  "description": "Plain Shirt",
  "fabric": "Cotton",
  "division": "South",
  "buyer": "US POLO",
  "orderQuantity": 1000000,
  "target": 1000,
  "lineDesign": null,
  "efficiency": 80.0,
  "designOutput": 800,
  "totalSam": null,
  "totalAllocation": null,
  "totalRequired": null,
  "operations": null,
  "createdAt": "2025-03-27T07:44:45.291",
  "deadLine": null,
  "allowance": 20,
  "lane": 2
}
```

#### Retrieve Order by Item Number

- **GET** `http://localhost:8080/api/order/item/{itemNo}`

**Response Body:**

```json
{
  "id": "67e4b4954139b7678b84769d",
  "styleNo": "12346",
  "itemNo": "12346",
  "description": "Plain Shirt",
  "fabric": "Cotton",
  "division": "South",
  "buyer": "US POLO",
  "orderQuantity": 1000000,
  "target": 1000,
  "lineDesign": null,
  "efficiency": 80.0,
  "designOutput": 800,
  "totalSam": null,
  "totalAllocation": null,
  "totalRequired": null,
  "operations": null,
  "createdAt": "2025-03-27T07:44:45.291",
  "deadLine": null,
  "allowance": 20,
  "lane": 2
}
```

#### Upload OB

- **PUT** `http://localhost:8080/api/order/upload`

### Request Body (form-data):

| Key  | Type | Description          | Required |
| ---- | ---- | -------------------- | -------- |
| file | File | Excel file to upload | Yes      |

**Response Body:**

```json
{
  "id": "67e4b4954139b7678b84769d",
  "styleNo": "12346",
  "itemNo": "12346",
  "description": "Plain Shirt",
  "fabric": "Cotton",
  "division": "South",
  "buyer": "US POLO",
  "orderQuantity": 1000000,
  "target": 1000,
  "lineDesign": 62,
  "efficiency": 80.0,
  "designOutput": 800,
  "totalSam": 57.35,
  "totalAllocation": 155.0,
  "totalRequired": 119.45,
  "operations": [
    {
      "id": 1,
      "operationName": "Front BH Placket",
      "section": "FRONT SECTION",
      "sam": 0.5,
      "machineType": "MNCS",
      "required": 1.04,
      "allocated": 2.0,
      "target": 1152
    },
    {
      "id": 2,
      "operationName": "Wash-care label attachment",
      "section": "FRONT SECTION",
      "sam": 0.4,
      "machineType": "SNLS",
      "required": 0.83,
      "allocated": 1.0,
      "target": 960
    },
    {
      "id": 3,
      "operationName": "Front pocet crease",
      "section": "FRONT SECTION",
      "sam": 0.35,
      "machineType": "Iron table",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 4,
      "operationName": "Front pocket hem stitch",
      "section": "FRONT SECTION",
      "sam": 0.55,
      "machineType": "SNLS",
      "required": 1.15,
      "allocated": 1.5,
      "target": 1047
    },
    {
      "id": 5,
      "operationName": "Front pocket + label attachment",
      "section": "FRONT SECTION",
      "sam": 0.8,
      "machineType": "SNLS",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 6,
      "operationName": "Front button Placket",
      "section": "FRONT SECTION",
      "sam": 0.95,
      "machineType": "SNLS",
      "required": 1.98,
      "allocated": 2.0,
      "target": 808
    },
    {
      "id": 7,
      "operationName": "Button hole placket lining stitching",
      "section": "FRONT SECTION",
      "sam": 0.28,
      "machineType": "SNLS",
      "required": 0.58,
      "allocated": 1.0,
      "target": 1371
    },
    {
      "id": 8,
      "operationName": "In-line checking",
      "section": "FRONT SECTION",
      "sam": 0.65,
      "machineType": "Table",
      "required": 1.35,
      "allocated": 2.0,
      "target": 886
    },
    {
      "id": 9,
      "operationName": "Front Button hole",
      "section": "FRONT SECTION",
      "sam": 0.6,
      "machineType": "Button Hole",
      "required": 1.25,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 10,
      "operationName": "Gusset preparation",
      "section": "FRONT SECTION",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 11,
      "operationName": "Sleeve upper placket crease (Top part )",
      "section": "SLEEVE SECTION",
      "sam": 0.5,
      "machineType": "Iron table",
      "required": 1.04,
      "allocated": 1.5,
      "target": 1152
    },
    {
      "id": 12,
      "operationName": "Sleeve upper placket crease",
      "section": "SLEEVE SECTION",
      "sam": 0.4,
      "machineType": "Iron table",
      "required": 0.83,
      "allocated": 1.0,
      "target": 960
    },
    {
      "id": 13,
      "operationName": "Sleeve opening stitch",
      "section": "SLEEVE SECTION",
      "sam": 0.35,
      "machineType": "SNLS",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 14,
      "operationName": "Sleeve lower placket attach",
      "section": "SLEEVE SECTION",
      "sam": 0.55,
      "machineType": "SNLS",
      "required": 1.15,
      "allocated": 1.5,
      "target": 1047
    },
    {
      "id": 15,
      "operationName": "Sleeve lower placket tacking and trimming",
      "section": "SLEEVE SECTION",
      "sam": 0.8,
      "machineType": "SNLS",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 16,
      "operationName": "Sleeve upper placket finish",
      "section": "SLEEVE SECTION",
      "sam": 0.95,
      "machineType": "SNLS",
      "required": 1.98,
      "allocated": 2.0,
      "target": 808
    },
    {
      "id": 17,
      "operationName": "Sleeve under placket attachment",
      "section": "SLEEVE SECTION",
      "sam": 0.28,
      "machineType": "SNLS",
      "required": 0.58,
      "allocated": 1.0,
      "target": 1371
    },
    {
      "id": 18,
      "operationName": "Sleeve in-line checking",
      "section": "SLEEVE SECTION",
      "sam": 0.65,
      "machineType": "Table",
      "required": 1.35,
      "allocated": 2.0,
      "target": 886
    },
    {
      "id": 19,
      "operationName": "Sleeve placket opening stitch",
      "section": "SLEEVE SECTION",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 20,
      "operationName": "Sleeve lower placket tuck and trim",
      "section": "SLEEVE SECTION",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 21,
      "operationName": "Sleeve upper placket creasing",
      "section": "SLEEVE SECTION",
      "sam": 0.5,
      "machineType": "Iron table",
      "required": 1.04,
      "allocated": 1.5,
      "target": 1152
    },
    {
      "id": 22,
      "operationName": "Back center pleat",
      "section": "BACK \nSECTION",
      "sam": 0.4,
      "machineType": "SNLS",
      "required": 0.83,
      "allocated": 1.0,
      "target": 960
    },
    {
      "id": 23,
      "operationName": "Coo label attaching",
      "section": "BACK \nSECTION",
      "sam": 0.35,
      "machineType": "SNLS",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 24,
      "operationName": "Brand +size label",
      "section": "BACK \nSECTION",
      "sam": 0.55,
      "machineType": "Pattern taker",
      "required": 1.15,
      "allocated": 2.0,
      "target": 1047
    },
    {
      "id": 25,
      "operationName": "Back yoke Attachment",
      "section": "BACK \nSECTION",
      "sam": 0.8,
      "machineType": "SNLS",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 26,
      "operationName": "In-line checking",
      "section": "BACK \nSECTION",
      "sam": 0.95,
      "machineType": "Table",
      "required": 1.98,
      "allocated": 2.0,
      "target": 808
    },
    {
      "id": 27,
      "operationName": "Collar stand lining",
      "section": "COLLAR SECTION",
      "sam": 0.28,
      "machineType": "SNLS",
      "required": 0.58,
      "allocated": 1.0,
      "target": 1371
    },
    {
      "id": 28,
      "operationName": "Collar edge cutting",
      "section": "COLLAR SECTION",
      "sam": 0.65,
      "machineType": "SNEC",
      "required": 1.35,
      "allocated": 1.5,
      "target": 886
    },
    {
      "id": 29,
      "operationName": "Button hole",
      "section": "COLLAR SECTION",
      "sam": 0.6,
      "machineType": "BH M/c",
      "required": 1.25,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 30,
      "operationName": "Collar turn",
      "section": "COLLAR SECTION",
      "sam": 0.6,
      "machineType": "Collar turn M/c",
      "required": 1.25,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 31,
      "operationName": "Collar top stitch",
      "section": "COLLAR SECTION",
      "sam": 0.5,
      "machineType": "SNLS",
      "required": 1.04,
      "allocated": 1.5,
      "target": 1152
    },
    {
      "id": 32,
      "operationName": "Collar pick shape ready",
      "section": "COLLAR SECTION",
      "sam": 0.4,
      "machineType": "SNEC",
      "required": 0.83,
      "allocated": 1.0,
      "target": 960
    },
    {
      "id": 33,
      "operationName": "Collar center stitch",
      "section": "COLLAR SECTION",
      "sam": 0.35,
      "machineType": "SNLS",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 34,
      "operationName": "Collar lable",
      "section": "COLLAR SECTION",
      "sam": 0.55,
      "machineType": "SNLS",
      "required": 1.15,
      "allocated": 1.5,
      "target": 1047
    },
    {
      "id": 35,
      "operationName": "Collar In-line checking",
      "section": "COLLAR SECTION",
      "sam": 0.8,
      "machineType": "Table",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 36,
      "operationName": "Collar edge finishing",
      "section": "COLLAR SECTION",
      "sam": 0.95,
      "machineType": "SNEC",
      "required": 1.98,
      "allocated": 2.5,
      "target": 808
    },
    {
      "id": 37,
      "operationName": "Cuff hem",
      "section": "CUFF SECTION",
      "sam": 0.28,
      "machineType": "SNLS",
      "required": 0.58,
      "allocated": 1.0,
      "target": 1371
    },
    {
      "id": 38,
      "operationName": "Cuff run",
      "section": "CUFF SECTION",
      "sam": 0.65,
      "machineType": "SNLS",
      "required": 1.35,
      "allocated": 1.5,
      "target": 886
    },
    {
      "id": 39,
      "operationName": "Cuff edge cut",
      "section": "CUFF SECTION",
      "sam": 0.6,
      "machineType": "SNEC",
      "required": 1.25,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 40,
      "operationName": "Cuff turning",
      "section": "CUFF SECTION",
      "sam": 0.6,
      "machineType": "-",
      "required": 1.25,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 41,
      "operationName": "Cuff setting",
      "section": "CUFF SECTION",
      "sam": 0.5,
      "machineType": "Cuff setting M/c",
      "required": 1.04,
      "allocated": 2.0,
      "target": 1152
    },
    {
      "id": 42,
      "operationName": "Cuff top stitch",
      "section": "CUFF SECTION",
      "sam": 0.4,
      "machineType": "SNLS",
      "required": 0.83,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 43,
      "operationName": "Cuff ironing",
      "section": "CUFF SECTION",
      "sam": 0.35,
      "machineType": "Iron table",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 44,
      "operationName": "Front and back attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.55,
      "machineType": "SNLS",
      "required": 1.15,
      "allocated": 1.5,
      "target": 1047
    },
    {
      "id": 45,
      "operationName": "Shoulder top stitch",
      "section": "ASSEMBLY LINE",
      "sam": 0.8,
      "machineType": "SNLS",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 46,
      "operationName": "Collar attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.95,
      "machineType": "SNLS",
      "required": 1.98,
      "allocated": 2.0,
      "target": 808
    },
    {
      "id": 47,
      "operationName": "Hanging loop attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.28,
      "machineType": "SNLS",
      "required": 0.58,
      "allocated": 1.0,
      "target": 1371
    },
    {
      "id": 48,
      "operationName": "Collar finishing",
      "section": "ASSEMBLY LINE",
      "sam": 0.65,
      "machineType": "SNLS",
      "required": 1.35,
      "allocated": 1.5,
      "target": 886
    },
    {
      "id": 49,
      "operationName": "Sleeve attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 50,
      "operationName": "Sleeve attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 51,
      "operationName": "Sleeve attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.5,
      "machineType": "SNLS",
      "required": 1.04,
      "allocated": 1.5,
      "target": 1152
    },
    {
      "id": 52,
      "operationName": "Armhole top stitch",
      "section": "ASSEMBLY LINE",
      "sam": 0.4,
      "machineType": "DNCS",
      "required": 0.83,
      "allocated": 1.0,
      "target": 960
    },
    {
      "id": 53,
      "operationName": "Armhole top stitch",
      "section": "ASSEMBLY LINE",
      "sam": 0.35,
      "machineType": "DNCS",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 54,
      "operationName": "Bottom hemming",
      "section": "ASSEMBLY LINE",
      "sam": 0.55,
      "machineType": "SNLS",
      "required": 1.15,
      "allocated": 1.5,
      "target": 1047
    },
    {
      "id": 55,
      "operationName": "Bottom hemming",
      "section": "ASSEMBLY LINE",
      "sam": 0.8,
      "machineType": "SNLS",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 56,
      "operationName": "Side seam",
      "section": "ASSEMBLY LINE",
      "sam": 0.95,
      "machineType": "FOA",
      "required": 1.98,
      "allocated": 2.0,
      "target": 808
    },
    {
      "id": 57,
      "operationName": "Side seam",
      "section": "ASSEMBLY LINE",
      "sam": 0.28,
      "machineType": "FOA",
      "required": 0.58,
      "allocated": 1.0,
      "target": 1371
    },
    {
      "id": 58,
      "operationName": "Sleeve mouth edge",
      "section": "ASSEMBLY LINE",
      "sam": 0.65,
      "machineType": "SNEC",
      "required": 1.35,
      "allocated": 1.5,
      "target": 886
    },
    {
      "id": 59,
      "operationName": "In-line inspection",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "table",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 60,
      "operationName": "Sleeve marking",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "table",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 61,
      "operationName": "Cuff finishing",
      "section": "ASSEMBLY LINE",
      "sam": 0.5,
      "machineType": "SNLS",
      "required": 1.04,
      "allocated": 1.5,
      "target": 1152
    },
    {
      "id": 62,
      "operationName": "Cuff finishing",
      "section": "ASSEMBLY LINE",
      "sam": 0.4,
      "machineType": "SNLS",
      "required": 0.83,
      "allocated": 1.0,
      "target": 960
    },
    {
      "id": 63,
      "operationName": "Button hole (cuff & collar)",
      "section": "ASSEMBLY LINE",
      "sam": 0.35,
      "machineType": "BH",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 64,
      "operationName": "Button marking",
      "section": "ASSEMBLY LINE",
      "sam": 0.55,
      "machineType": "table",
      "required": 1.15,
      "allocated": 1.5,
      "target": 1047
    },
    {
      "id": 65,
      "operationName": "Cuff button marking",
      "section": "ASSEMBLY LINE",
      "sam": 0.8,
      "machineType": "table",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 66,
      "operationName": "Button attachment (Auto feed )",
      "section": "ASSEMBLY LINE",
      "sam": 0.95,
      "machineType": "BA",
      "required": 1.98,
      "allocated": 2.0,
      "target": 808
    },
    {
      "id": 67,
      "operationName": "Button attachment (Manual feed )",
      "section": "ASSEMBLY LINE",
      "sam": 0.28,
      "machineType": "BA",
      "required": 0.58,
      "allocated": 1.0,
      "target": 1371
    },
    {
      "id": 68,
      "operationName": "Button attachment (sleeve opening)",
      "section": "ASSEMBLY LINE",
      "sam": 0.65,
      "machineType": "BA",
      "required": 1.35,
      "allocated": 1.5,
      "target": 886
    },
    {
      "id": 69,
      "operationName": "Gusset Attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 70,
      "operationName": "Gusset Attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 71,
      "operationName": "Bar tack",
      "section": "ASSEMBLY LINE",
      "sam": 0.5,
      "machineType": "Bar tack",
      "required": 1.04,
      "allocated": 1.5,
      "target": 1152
    },
    {
      "id": 72,
      "operationName": "End-line checking",
      "section": "ASSEMBLY LINE",
      "sam": 0.4,
      "machineType": "Table",
      "required": 0.83,
      "allocated": 1.0,
      "target": 960
    },
    {
      "id": 73,
      "operationName": "Front and back attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.35,
      "machineType": "SNLS",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 74,
      "operationName": "Shoulder top stitch",
      "section": "ASSEMBLY LINE",
      "sam": 0.55,
      "machineType": "SNLS",
      "required": 1.15,
      "allocated": 1.5,
      "target": 1047
    },
    {
      "id": 75,
      "operationName": "Collar attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.8,
      "machineType": "SNLS",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 76,
      "operationName": "Hanging loop attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.95,
      "machineType": "SNLS",
      "required": 1.98,
      "allocated": 2.0,
      "target": 808
    },
    {
      "id": 77,
      "operationName": "Collar finishing",
      "section": "ASSEMBLY LINE",
      "sam": 0.28,
      "machineType": "SNLS",
      "required": 0.58,
      "allocated": 1.0,
      "target": 1371
    },
    {
      "id": 78,
      "operationName": "Sleeve attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.65,
      "machineType": "SNLS",
      "required": 1.35,
      "allocated": 1.5,
      "target": 886
    },
    {
      "id": 79,
      "operationName": "Sleeve attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 80,
      "operationName": "Sleeve attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 81,
      "operationName": "Armhole top stitch",
      "section": "ASSEMBLY LINE",
      "sam": 0.5,
      "machineType": "DNCS",
      "required": 1.04,
      "allocated": 1.5,
      "target": 1152
    },
    {
      "id": 82,
      "operationName": "Armhole top stitch",
      "section": "ASSEMBLY LINE",
      "sam": 0.4,
      "machineType": "DNCS",
      "required": 0.83,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 83,
      "operationName": "Bottom hemming",
      "section": "ASSEMBLY LINE",
      "sam": 0.35,
      "machineType": "SNLS",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 84,
      "operationName": "Bottom hemming",
      "section": "ASSEMBLY LINE",
      "sam": 0.55,
      "machineType": "SNLS",
      "required": 1.15,
      "allocated": 1.5,
      "target": 1047
    },
    {
      "id": 85,
      "operationName": "Side seam",
      "section": "ASSEMBLY LINE",
      "sam": 0.8,
      "machineType": "FOA",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 86,
      "operationName": "Side seam",
      "section": "ASSEMBLY LINE",
      "sam": 0.95,
      "machineType": "FOA",
      "required": 1.98,
      "allocated": 2.0,
      "target": 808
    },
    {
      "id": 87,
      "operationName": "Sleeve mouth edge",
      "section": "ASSEMBLY LINE",
      "sam": 0.28,
      "machineType": "SNEC",
      "required": 0.58,
      "allocated": 1.5,
      "target": 1371
    },
    {
      "id": 88,
      "operationName": "In-line inspection",
      "section": "ASSEMBLY LINE",
      "sam": 0.65,
      "machineType": "table",
      "required": 1.35,
      "allocated": 1.5,
      "target": 886
    },
    {
      "id": 89,
      "operationName": "Sleeve marking",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "table",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 90,
      "operationName": "Cuff finishing",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 91,
      "operationName": "Cuff finishing",
      "section": "ASSEMBLY LINE",
      "sam": 0.5,
      "machineType": "SNLS",
      "required": 1.04,
      "allocated": 1.5,
      "target": 1152
    },
    {
      "id": 92,
      "operationName": "Button hole (cuff & collar)",
      "section": "ASSEMBLY LINE",
      "sam": 0.4,
      "machineType": "BH",
      "required": 0.83,
      "allocated": 1.0,
      "target": 960
    },
    {
      "id": 93,
      "operationName": "Button marking",
      "section": "ASSEMBLY LINE",
      "sam": 0.35,
      "machineType": "table",
      "required": 0.73,
      "allocated": 1.0,
      "target": 1097
    },
    {
      "id": 94,
      "operationName": "Cuff button marking",
      "section": "ASSEMBLY LINE",
      "sam": 0.55,
      "machineType": "table",
      "required": 1.15,
      "allocated": 1.5,
      "target": 1047
    },
    {
      "id": 95,
      "operationName": "Button attachment (Auto feed )",
      "section": "ASSEMBLY LINE",
      "sam": 0.8,
      "machineType": "BA",
      "required": 1.67,
      "allocated": 2.0,
      "target": 960
    },
    {
      "id": 96,
      "operationName": "Button attachment (Manual feed )",
      "section": "ASSEMBLY LINE",
      "sam": 0.95,
      "machineType": "BA",
      "required": 1.98,
      "allocated": 2.0,
      "target": 808
    },
    {
      "id": 97,
      "operationName": "Button attachment (sleeve opening)",
      "section": "ASSEMBLY LINE",
      "sam": 0.28,
      "machineType": "BA",
      "required": 0.58,
      "allocated": 1.5,
      "target": 1371
    },
    {
      "id": 98,
      "operationName": "Gusset Attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.65,
      "machineType": "SNLS",
      "required": 1.35,
      "allocated": 1.5,
      "target": 886
    },
    {
      "id": 99,
      "operationName": "Gusset Attachment",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "SNLS",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 100,
      "operationName": "Bar tack",
      "section": "ASSEMBLY LINE",
      "sam": 0.6,
      "machineType": "Bar tack",
      "required": 1.25,
      "allocated": 1.5,
      "target": 960
    },
    {
      "id": 101,
      "operationName": "End-line checking",
      "section": "ASSEMBLY LINE",
      "sam": 0.55,
      "machineType": "Table",
      "required": 1.15,
      "allocated": 2.0,
      "target": 1047
    }
  ],
  "createdAt": "2025-03-27T07:44:45.291",
  "deadLine": null,
  "allowance": 20,
  "lane": 2
}
```

#### Update Allowance

- **PUT** `http://localhost:8080/api/order/allowance`

**Request Body:**

```json
{
  "styleNo": "12346",
  "allowance": 20
}
```

**Response Body:**

```json
{
    "Allowance set to 20%"
}
```

#### Update Lane

- **PUT** `http://localhost:8080/api/order/lane`

**Request Body:**

```json
{
  "styleNo": "12346",
  "lane": 2
}
```

**Response Body:**

```json
{
   "Lane set to Lane no, :2"
}
```

### Time Study Management

#### Create a Time Study Record

- **POST** `http://localhost:8080/api/study`

**Request Body:**

```json
{
  "styleNo": "12346",
  "operatorName": "Front BH Placket",
  "operatorId": "V142",
  "operationName": "Sahil",
  "section": "FRONT SECTION",
  "machineType": "MNCS",
  "lapsMS": [280, 330, 280, 310, 260, 260, 290, 320, 290, 370]
}
```

**Response Body:**

```json
{
  "id": "67e4be754139b7678b8476a0",
  "styleNo": "12346",
  "operatorName": "Front BH Placket",
  "operatorId": "V142",
  "operationName": "Sahil",
  "section": "FRONT SECTION",
  "machineType": "MNCS",
  "laps": [
    "00:00:28",
    "00:00:33",
    "00:00:28",
    "00:00:31",
    "00:00:26",
    "00:00:26",
    "00:00:29",
    "00:00:32",
    "00:00:29",
    "00:00:37"
  ],
  "lapsMS": [280, 330, 280, 310, 260, 260, 290, 320, 290, 370],
  "avgTime": "00:00:29",
  "capacityPH": 10055,
  "capacityPD": 80446,
  "allowanceTime": "00:00:35",
  "remarks": null,
  "assigned": null
}
```

#### Retrieve Time Study Records

- **GET** `http://localhost:8080/api/study`

#### Retrieve Time Study by Operator ID and Style No

- **GET** `http://localhost:8080/api/study/operator`

**Request Body:**

```json
{
  "operatorId": "V142",
  "styleNo": "12346"
}
```

**Response Body:**

```json
{
  "id": "67e4be754139b7678b8476a0",
  "styleNo": "12346",
  "operatorName": "Front BH Placket",
  "operatorId": "V142",
  "operationName": "Sahil",
  "section": "FRONT SECTION",
  "machineType": "MNCS",
  "laps": [
    "00:05:68",
    "00:04:22",
    "00:03:27",
    "00:02:16",
    "00:02:73",
    "00:02:53",
    "00:04:68",
    "00:03:20",
    "00:03:17",
    "00:02:46"
  ],
  "lapsMS": [5680, 4220, 3270, 2160, 2730, 2530, 4680, 3200, 3170, 2460],
  "avgTime": "00:03:41",
  "capacityPH": 879,
  "capacityPD": 7038,
  "allowanceTime": "00:04:09",
  "remarks": "A little accurate",
  "assigned": null
}
```

#### Update Time Study Record

- **PUT** `http://localhost:8080/api/study/update`

**Request Body:**

```json
{
  "operator": {
    "operatorId": "V142",
    "styleNo": "12346"
  },
  "lapsMS": [5680, 4220, 3270, 2160, 2730, 2530, 4680, 3200, 3170, 2460]
}
```

**Response Body:**

```json
{
  "id": "67e4be754139b7678b8476a0",
  "styleNo": "12346",
  "operatorName": "Front BH Placket",
  "operatorId": "V142",
  "operationName": "Sahil",
  "section": "FRONT SECTION",
  "machineType": "MNCS",
  "laps": [
    "00:05:68",
    "00:04:22",
    "00:03:27",
    "00:02:16",
    "00:02:73",
    "00:02:53",
    "00:04:68",
    "00:03:20",
    "00:03:17",
    "00:02:46"
  ],
  "lapsMS": [5680, 4220, 3270, 2160, 2730, 2530, 4680, 3200, 3170, 2460],
  "avgTime": "00:03:41",
  "capacityPH": 879,
  "capacityPD": 7038,
  "allowanceTime": "00:04:09",
  "remarks": null,
  "assigned": null
}
```

#### Add Remarks to a Time Study Record

- **PUT** `http://localhost:8080/api/study/remarks`

**Request Body:**

```json
{
  "operator": {
    "operatorId": "V142",
    "styleNo": "12346"
  },
  "remarks": "A little inconsistent"
}
```

**Response Body:**

```json
Remarks successfully added for Operator Id: V142 in Order Style no 12346
```

## Installation & Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/timeflow.git
   cd timeflow
   ```
2. Install dependencies and configure MongoDB.
3. Run the Spring Boot application:
   ```sh
   mvn spring-boot:run
   ```

## Future Enhancements

- Role-based access improvements.
- Integration with external authentication providers.

## License

This project is licensed under the MIT License.
