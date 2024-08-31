## Features

- **Core Operations**: Perform basic arithmetic operations like addition, subtraction, multiplication, and division using predefined operations.
- **Dynamic Operations**: Add new operations and use them in calculations without modifying the core code.
- **Chaining Operations**: Chain multiple operations together to perform complex calculations in a single request.
- **Customizable**: Can include new mathematical operations through an API call.

## API Endpoints

### 1. Calculate Core Operation

**`GET` `/api/calculator/calculate/core`**   
**Description**: Perform a core arithmetic operation using predefined operations.

**Request Parameters**:
- `operation` (required): The operation to perform. Must be one of the predefined operations (`ADD`, `SUBTRACT`, `MULTIPLY`, `DIVIDE`).
- `num1` (required): The first operand.
- `num2` (required): The second operand.

**Example**:
```bash
GET /api/calculator/calculate/core?operation=ADD&num1=10&num2=5
```

### 2. Chain Calculate Operations

**`POST` `/api/calculator/chainCalculate`**   
**Description**: Chain multiple operations together to perform calculations.

**Request Parameters**:
- `initial` (required): The initial value for the chain calculation.
- `opsAndValues` (required, in the body): An array of operations and corresponding values.

**Example**:
```bash
POST /api/calculator/chainCalculate?initial=10
[
    "ADD", 5, 
    "MULTIPLY", 2, 
    "SUBTRACT", 3, 
    "DIVIDE", 2
]
```

### 3. Add Custom Operation

**`POST` `/api/calculator/addOperation`**   
**Description**: Add a new custom operation to the calculator.

**Request Parameters**:
- `operation` (required): The name of the new operation.
- `func` (required, in the body text): The function logic to implement the operation
- `(e.g., `pow(a, b)` or `a % b` or `(a + b + c) / 3`)`.

**Example**:
```bash
POST /api/calculator/addOperation?operation=POWER
body text pow(a, b)
```

### 4. Calculate Dynamic Operation

**`GET` `/api/calculator/calculate/dynamic`**  
**Description**: Perform a calculation using a custom-defined operation.

**Request Parameters**:
- `operation` (required): The name of the dynamic operation.
- `args` (required): The operands to use in the calculation.

**Example**:
```bash
GET /api/calculator/calculate/dynamic?operation=POWER&args=2&args=3
GET /api/calculator/calculate/dynamic?operation=MODULUS&args=8&args=3
```



