## IDLReasoner web API

### Overview

The **IDLReasoner web API** is a web-based API designed to complement the **IDLReasoner** library. It offers a set of analysis operations specifically tailored for IDL (Inter-parameter Dependency Language) within the OpenAPI Specification (OAS). The API enables users to assess the consistency of IDL in OAS documents, examining user requests, and providing detailed error explanations when inconsistencies or issues are detected. With IDLReasoner web API, developers can leverage the power of IDLReasoner for comprehensive analysis and validation of their OAS documents enriched with IDL.

### Table of Contents

- [IDLReasoner web API](#idlreasoner-web-api)
  - [Overview](#overview)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
  - [Base URL](#base-url)
  - [API Operation Reference](#api-operation-reference)
    - [GET `/api/operationAnalysis`](#get-apioperationanalysis)
    - [POST `/api/operationAnalysis`](#post-apioperationanalysis)
    - [GET `/api/isConsistent`](#get-apiisconsistent)
    - [POST `/api/isConsistent`](#post-apiisconsistent)
    - [GET `/api/isDeadParameter`](#get-apiisdeadparameter)
    - [POST `/api/isDeadParameter`](#post-apiisdeadparameter)
    - [GET `/api/isFalseOptional`](#get-apiisfalseoptional)
    - [POST `/api/isFalseOptional`](#post-apiisfalseoptional)
    - [GET `/api/isValidSpecification`](#get-apiisvalidspecification)
    - [POST `/api/isValidSpecification`](#post-apiisvalidspecification)
    - [GET `/api/isValidRequest`](#get-apiisvalidrequest)
    - [POST `/api/isValidRequest`](#post-apiisvalidrequest)
    - [GET `/api/isValidPartialRequest`](#get-apiisvalidpartialrequest)
    - [POST `/api/isValidPartialRequest`](#post-apiisvalidpartialrequest)
    - [GET `/api/generateRandomValidRequest`](#get-apigeneraterandomvalidrequest)
    - [POST `/api/generateRandomValidRequest`](#post-apigeneraterandomvalidrequest)
    - [GET `/api/generateRandomInvalidRequest`](#get-apigeneraterandominvalidrequest)
    - [POST `/api/generateRandomInvalidRequest`](#post-apigeneraterandominvalidrequest)
  - [How to Use It](#how-to-use-it)
    - [GET `/api/isDeadParameter`](#get-apiisdeadparameter-1)
  - [Refer to Swagger Documentation](#refer-to-swagger-documentation)

### Features

- **IDL Consistency Check**: Ensure that your IDL is consistent and meets all specified dependencies.
- **Validation of Operations**: Verify if specific operations in your OpenAPI document comply with the IDL. If an operation is invalid, provide detailed explanations for the inconsistencies found.
- **Dead Parameter Detection**: Identify parameters that cannot be included in any valid service call.
- **False Optional Parameter Detection**: Detect parameters that are required despite being defined as optional.
- **Request Validation**: Validate requests based on specific parameter values and types to ensure they conform to the IDL specification.

- **Random Request Generation**: Generate random valid and invalid requests for testing purposes.

### Base URL

The base URL for the IDLReasoner API is: `idl.us.es`

### API Operation Reference

#### GET `/api/operationAnalysis`

**Description**:  
This operation returns `true` if the IDL specification in the operation of the OpenAPI document is valid and `false` with an explanation if it is invalid.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

#### POST `/api/operationAnalysis`

**Description**:  
This operation returns `true` if the IDL specification in the operation of the OpenAPI document is valid and `false` with an explanation if it is invalid.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

**Request Body**:

The request body should contain the entire OpenAPI Specification (OAS) document in JSON or YAML format.

---

#### GET `/api/isConsistent`

**Description**:  
This operation returns `true` if the IDL is consistent. An IDL is consistent if at least one request exists that satisfies all the dependencies of the specification.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

#### POST `/api/isConsistent`

**Description**:  
This operation returns `true` if the IDL is consistent. An IDL is consistent if at least one request exists that satisfies all the dependencies of the specification.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

**Request Body**:

The request body should contain the entire OpenAPI Specification (OAS) document in JSON or YAML format.

---

#### GET `/api/isDeadParameter`

**Description**:  
This operation returns `true` if the parameter is dead. A parameter is dead if it cannot be included in any valid call to the service.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |
| `parameter`    | Yes      | The name of the parameter to check.           |

#### POST `/api/isDeadParameter`

**Description**:  
This operation returns `true` if the parameter is dead. A parameter is dead if it cannot be included in any valid call to the service.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |
| `parameter`    | Yes      | The name of the parameter to check.           |

**Request Body**:

The request body should contain the entire OpenAPI Specification (OAS) document in JSON or YAML format.

---

#### GET `/api/isFalseOptional`

**Description**:  
This operation returns `true` if the parameter is a false optional. A parameter is false optional if it is required despite being defined as optional.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |
| `parameter`    | Yes      | The name of the parameter to check.           |

#### POST `/api/isFalseOptional`

**Description**:  
This operation returns `true` if the parameter is a false optional. A parameter is false optional if it is required despite being defined as optional.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |
| `parameter`    | Yes      | The name of the parameter to check.           |

**Request Body**:

The request body should contain the entire OpenAPI Specification (OAS) document in JSON or YAML format.

---

#### GET `/api/isValidSpecification`

**Description**:  
This operation returns `true` if the IDL is valid. An IDL specification is valid if it is consistent and does not contain any dead or false optional parameters.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

#### POST `/api/isValidSpecification`

**Description**:  
This operation returns `true` if the IDL is valid. An IDL specification is valid if it is consistent and does not contain any dead or false optional parameters.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

**Request Body**:

The request body should contain the entire OpenAPI Specification (OAS) document in JSON or YAML format.

---

#### GET `/api/isValidRequest`

**Description**:  
This operation returns `true` if the request is valid. A request is valid if it satisfies all the dependencies of the IDL specification.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |
| `requestParams`    | Yes      | A map of request parameters where each key is the parameter name and its value is the corresponding value. |

#### POST `/api/isValidRequest`

**Description**:  
This operation returns `true` if the request is valid. A request is valid if it satisfies all the dependencies of the IDL specification.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |
| `requestParams`    | Yes      | A map of request parameters where each key is the parameter name and its value is the corresponding value. |

**Request Body**:

The request body should contain the entire OpenAPI Specification (OAS) document in JSON or YAML format.

---

#### GET `/api/isValidPartialRequest`

**Description**:  
This operation returns `true` if the request is partially valid. A request that is partially valid means that some other parameters should still be included to make it a fully valid request.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |
| `requestParams`    | Yes      | A map of request parameters where each key is the parameter name and its value is the corresponding value. |

#### POST `/api/isValidPartialRequest`

**Description**:  
This operation returns `true` if the request is partially valid. A request that is partially valid means that some other parameters should still be included to make it a fully valid request.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |
| `requestParams`    | Yes      | A map of request parameters where each key is the parameter name and its value is the corresponding value. |

**Request Body**:

The request body should contain the entire OpenAPI Specification (OAS) document in JSON or YAML format.

---

#### GET `/api/generateRandomValidRequest`

**Description**:  
This operation generates a random valid request.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

#### POST `/api/generateRandomValidRequest`

**Description**:  
This operation generates a random valid request.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

**Request Body**:

The request body should contain the entire OpenAPI Specification (OAS) document in JSON or YAML format.

---

#### GET `/api/generateRandomInvalidRequest`

**Description**:  
This operation generates a random invalid request.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

#### POST `/api/generateRandomInvalidRequest`

**Description**:  
This operation generates a random invalid request.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |

**Request Body**:

The request body should contain the entire OpenAPI Specification (OAS) document in JSON or YAML format.

---

### How to Use It

**Example**: Checking for Dead Parameters

The `isDeadParameter` operation checks if a parameter is dead, meaning it cannot be included in any valid call to the service. Here’s how you can use this operation with a GET request.

#### GET `/api/isDeadParameter`

**Description**:  
This operation returns `true` if the parameter is dead. A parameter is dead if it cannot be included in any valid call to the service.

**Parameters**:

| Parameter          | Required | Description                                    |
|--------------------|----------|------------------------------------------------|
| `specificationUrl` | Yes      | The URL of the OpenAPI document.              |
| `operationPath`    | Yes      | The operation path to be analyzed.            |
| `operationType`    | Yes      | The type of operation (e.g., GET, POST).      |
| `parameter`    | Yes      | The name of the parameter to check.           |


**Example Request**:

[http://idl.us.es/api/isDeadParameter?specificationUrl=https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml&operationPath=/oneDependencyRequires&operationType=GET&parameter=p1
](http://idl.us.es/api/isDeadParameter?specificationUrl=https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml&operationPath=/oneDependencyRequires&operationType=GET&parameter=p1)


**Response**:
```
{
  "deadParameter": false
}
```

### Refer to Swagger Documentation

For detailed request and response examples, please refer to the [Swagger Documentation](http://idl.us.es/swagger-ui/index.html).
