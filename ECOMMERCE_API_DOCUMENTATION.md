# Documentación API E-commerce - Pet Shop

## Información General

**Base URL**: `http://localhost:3000/api`

**Formato de Respuesta**: JSON

**Autenticación**: JWT Bearer Token (donde se requiera)

---

## Tabla de Contenidos

1. [Autenticación](#autenticación)
2. [Productos](#productos)
3. [Carrito de Compras](#carrito-de-compras)

---

## Autenticación

### 1. Registro de Usuario

Crea una nueva cuenta de usuario.

**Endpoint**: `POST /auth/register`

**Autenticación**: No requerida

**Rate Limit**: 5 peticiones cada 5 minutos

**Body (JSON)**:
```json
{
  "email": "usuario@ejemplo.com",
  "password": "Password123",
  "fullName": "Juan Pérez"
}
```

**Validaciones**:
- `email`: Debe ser un email válido
- `password`:
  - Mínimo 6 caracteres, máximo 50
  - Al menos UNA letra mayúscula
  - Al menos UNA letra minúscula
  - Al menos UN número O un carácter especial
- `fullName`: Mínimo 1 carácter

**Respuesta Exitosa (201)**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "usuario@ejemplo.com",
  "fullName": "Juan Pérez",
  "isActive": true,
  "roles": ["user"],
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Errores Comunes**:
- `400 Bad Request`: Email ya registrado o validaciones fallidas
- `429 Too Many Requests`: Superó el límite de intentos

---

### 2. Iniciar Sesión

Autentica a un usuario existente y retorna un token JWT.

**Endpoint**: `POST /auth/login`

**Autenticación**: No requerida

**Rate Limit**: 5 peticiones cada 5 minutos

**Body (JSON)**:
```json
{
  "email": "usuario@ejemplo.com",
  "password": "Password123"
}
```

**Respuesta Exitosa (200)**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "usuario@ejemplo.com",
  "fullName": "Juan Pérez",
  "isActive": true,
  "roles": ["user"],
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Nota sobre el Token**:
- El token JWT expira en 2 horas
- Debe enviarse en el header `Authorization: Bearer {token}` en peticiones protegidas

**Errores Comunes**:
- `401 Unauthorized`: Credenciales incorrectas
- `429 Too Many Requests`: Superó el límite de intentos

---

### 3. Verificar Estado de Autenticación

Verifica si el token es válido y retorna información del usuario actualizada con un nuevo token.

**Endpoint**: `GET /auth/check-status`

**Autenticación**: Requerida

**Headers**:
```
Authorization: Bearer {token}
```

**Respuesta Exitosa (200)**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "usuario@ejemplo.com",
  "fullName": "Juan Pérez",
  "isActive": true,
  "roles": ["user"],
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Errores Comunes**:
- `401 Unauthorized`: Token inválido o expirado
- `403 Forbidden`: Token malformado

---

## Productos

### 1. Listar Productos (con Filtros)

Obtiene una lista paginada de productos con múltiples opciones de filtrado.

**Endpoint**: `GET /products`

**Autenticación**: No requerida

**Query Parameters (todos opcionales)**:

| Parámetro | Tipo | Descripción | Ejemplo | Por Defecto |
|-----------|------|-------------|---------|-------------|
| `limit` | Integer | Productos por página (1-100) | `limit=20` | `10` |
| `offset` | Integer | Productos a omitir | `offset=10` | `0` |
| `q` | String | Búsqueda en título/descripción | `q=collar` | - |
| `type` | Enum | Tipo de producto | `type=accesorios` | - |
| `species` | Enum | Especie de mascota | `species=dogs` | - |
| `sizes` | String | Tallas (separadas por comas) | `sizes=M,L,XL` | - |
| `minPrice` | Number | Precio mínimo | `minPrice=10.50` | - |
| `maxPrice` | Number | Precio máximo | `maxPrice=100.00` | - |

**Valores de Enum `type`**:
- `alimento-seco`: Croquetas, balanceado
- `alimento-humedo`: Latas, paté, sobres
- `snacks`: Premios, golosinas
- `accesorios`: Collares, camas, comederos, transportadoras
- `juguetes`: Entretenimiento y ejercicio
- `higiene`: Shampoo, arena para gatos

**Valores de Enum `species`**:
- `cats`: Productos para gatos
- `dogs`: Productos para perros

**Ejemplo de Request**:
```
GET /api/products?type=accesorios&species=dogs&minPrice=15&maxPrice=50&limit=20&offset=0
```

**Respuesta Exitosa (200)**:
```json
{
  "products": [
    {
      "id": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
      "title": "Collar Premium para Perro",
      "price": 29.99,
      "description": "Collar ajustable de nylon resistente con hebilla de seguridad",
      "slug": "collar_premium_para_perro",
      "stock": 45,
      "sizes": ["S", "M", "L", "XL"],
      "type": "accesorios",
      "species": "dogs",
      "tags": ["collar", "nylon", "ajustable", "perros"],
      "images": [
        {
          "id": "img-uuid-1",
          "url": "http://localhost:3000/api/files/product/collar-premium-1.jpg"
        },
        {
          "id": "img-uuid-2",
          "url": "http://localhost:3000/api/files/product/collar-premium-2.jpg"
        }
      ]
    },
    {
      "id": "a1b2c3d4-e5f6-4a3b-9c8d-7e6f5a4b3c2d",
      "title": "Cama Ortopédica para Perro",
      "price": 45.50,
      "description": "Cama con espuma de memoria para máximo confort",
      "slug": "cama_ortopedica_para_perro",
      "stock": 20,
      "sizes": ["M", "L", "XL"],
      "type": "accesorios",
      "species": "dogs",
      "tags": ["cama", "ortopédica", "confort"],
      "images": [
        {
          "id": "img-uuid-3",
          "url": "http://localhost:3000/api/files/product/cama-ortopedica.jpg"
        }
      ]
    }
  ],
  "total": 2
}
```

**Estructura de Producto**:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | UUID | Identificador único del producto |
| `title` | String | Nombre del producto (único) |
| `price` | Float | Precio del producto |
| `description` | String | Descripción detallada |
| `slug` | String | URL-friendly identifier (único) |
| `stock` | Integer | Cantidad disponible |
| `sizes` | Array[String] | Tallas disponibles |
| `type` | Enum | Categoría del producto |
| `species` | Enum | Especie (puede ser null para productos universales) |
| `tags` | Array[String] | Etiquetas para búsqueda |
| `images` | Array[Object] | Array de imágenes con `id` y `url` |

**Errores Comunes**:
- `400 Bad Request`: Parámetros de query inválidos (ej: `limit=150` excede máximo)

---

### 2. Obtener Producto Individual

Obtiene los detalles de un producto específico por ID, título o slug.

**Endpoint**: `GET /products/:term`

**Autenticación**: No requerida

**Parámetros de Ruta**:
- `:term`: Puede ser UUID (id), título exacto, o slug del producto

**Ejemplos de Request**:
```
GET /api/products/cd533345-f1f3-48c9-a62e-7dc2da50c8f8
GET /api/products/collar_premium_para_perro
GET /api/products/Collar Premium para Perro
```

**Respuesta Exitosa (200)**:
```json
{
  "id": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
  "title": "Collar Premium para Perro",
  "price": 29.99,
  "description": "Collar ajustable de nylon resistente con hebilla de seguridad",
  "slug": "collar_premium_para_perro",
  "stock": 45,
  "sizes": ["S", "M", "L", "XL"],
  "type": "accesorios",
  "species": "dogs",
  "tags": ["collar", "nylon", "ajustable", "perros"],
  "images": [
    {
      "id": "img-uuid-1",
      "url": "http://localhost:3000/api/files/product/collar-premium-1.jpg"
    },
    {
      "id": "img-uuid-2",
      "url": "http://localhost:3000/api/files/product/collar-premium-2.jpg"
    }
  ]
}
```

**Errores Comunes**:
- `404 Not Found`: Producto no encontrado

---

### 3. Crear Producto

Crea un nuevo producto en el catálogo.

**Endpoint**: `POST /products`

**Autenticación**: Requerida (solo usuarios autenticados)

**Headers**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "title": "Juguete Interactivo para Gato",
  "price": 15.99,
  "description": "Juguete con plumas y cascabel para estimular el instinto cazador",
  "stock": 100,
  "sizes": ["UNICO"],
  "type": "juguetes",
  "species": "cats",
  "tags": ["juguete", "interactivo", "plumas", "gatos"],
  "images": ["toy-cat-1.jpg", "toy-cat-2.jpg"]
}
```

**Campos Requeridos**:
- `title`: String único
- `price`: Number positivo
- `stock`: Integer positivo
- `sizes`: Array de strings
- `type`: Enum ProductType

**Campos Opcionales**:
- `description`: String
- `slug`: String (se genera automáticamente desde title si no se provee)
- `species`: Enum ProductSpecies
- `tags`: Array de strings
- `images`: Array de strings (nombres de archivo)

**Respuesta Exitosa (201)**:
```json
{
  "id": "nuevo-uuid-generado",
  "title": "Juguete Interactivo para Gato",
  "price": 15.99,
  "description": "Juguete con plumas y cascabel para estimular el instinto cazador",
  "slug": "juguete_interactivo_para_gato",
  "stock": 100,
  "sizes": ["UNICO"],
  "type": "juguetes",
  "species": "cats",
  "tags": ["juguete", "interactivo", "plumas", "gatos"],
  "images": [
    {
      "id": "img-uuid-nuevo-1",
      "url": "http://localhost:3000/api/files/product/toy-cat-1.jpg"
    },
    {
      "id": "img-uuid-nuevo-2",
      "url": "http://localhost:3000/api/files/product/toy-cat-2.jpg"
    }
  ]
}
```

**Errores Comunes**:
- `400 Bad Request`: Título duplicado o validaciones fallidas
- `401 Unauthorized`: Token no provisto o inválido
- `403 Forbidden`: Token relacionado

---

### 4. Actualizar Producto

Actualiza parcial o totalmente un producto existente.

**Endpoint**: `PATCH /products/:id`

**Autenticación**: Requerida (solo administradores)

**Headers**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Parámetros de Ruta**:
- `:id`: UUID del producto

**Body (JSON)** - Todos los campos son opcionales:
```json
{
  "price": 18.99,
  "stock": 150,
  "description": "Descripción actualizada del producto"
}
```

**Respuesta Exitosa (200)**:
```json
{
  "id": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
  "title": "Juguete Interactivo para Gato",
  "price": 18.99,
  "description": "Descripción actualizada del producto",
  "slug": "juguete_interactivo_para_gato",
  "stock": 150,
  "sizes": ["UNICO"],
  "type": "juguetes",
  "species": "cats",
  "tags": ["juguete", "interactivo", "plumas", "gatos"],
  "images": [...]
}
```

**Errores Comunes**:
- `400 Bad Request`: UUID inválido o validaciones fallidas
- `401 Unauthorized`: Token no provisto o inválido
- `403 Forbidden`: Usuario no es administrador
- `404 Not Found`: Producto no encontrado

---

### 5. Eliminar Producto

Elimina permanentemente un producto del catálogo.

**Endpoint**: `DELETE /products/:id`

**Autenticación**: Requerida (solo administradores)

**Headers**:
```
Authorization: Bearer {token}
```

**Parámetros de Ruta**:
- `:id`: UUID del producto

**Respuesta Exitosa (200)**:
```json
{
  "message": "Product deleted successfully"
}
```

**Errores Comunes**:
- `400 Bad Request`: UUID inválido
- `401 Unauthorized`: Token no provisto o inválido
- `403 Forbidden`: Usuario no es administrador
- `404 Not Found`: Producto no encontrado

---

## Carrito de Compras

### 1. Obtener Carrito

Obtiene el carrito del usuario autenticado. Si no existe, lo crea automáticamente.

**Endpoint**: `GET /cart`

**Autenticación**: Requerida

**Headers**:
```
Authorization: Bearer {token}
```

**Respuesta Exitosa (200)**:
```json
{
  "id": "cart-uuid",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "items": [
    {
      "id": "item-uuid-1",
      "cartId": "cart-uuid",
      "productId": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
      "quantity": 2,
      "size": "M",
      "priceAtTime": 29.99,
      "product": {
        "id": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
        "title": "Collar Premium para Perro",
        "price": 29.99,
        "description": "Collar ajustable de nylon resistente",
        "slug": "collar_premium_para_perro",
        "stock": 45,
        "sizes": ["S", "M", "L", "XL"],
        "type": "accesorios",
        "species": "dogs",
        "tags": ["collar", "nylon"],
        "images": [
          {
            "id": "img-uuid-1",
            "url": "http://localhost:3000/api/files/product/collar-premium-1.jpg"
          }
        ]
      }
    },
    {
      "id": "item-uuid-2",
      "cartId": "cart-uuid",
      "productId": "a1b2c3d4-e5f6-4a3b-9c8d-7e6f5a4b3c2d",
      "quantity": 1,
      "size": "L",
      "priceAtTime": 45.50,
      "product": {
        "id": "a1b2c3d4-e5f6-4a3b-9c8d-7e6f5a4b3c2d",
        "title": "Cama Ortopédica para Perro",
        "price": 45.50,
        "description": "Cama con espuma de memoria",
        "slug": "cama_ortopedica_para_perro",
        "stock": 20,
        "sizes": ["M", "L", "XL"],
        "type": "accesorios",
        "species": "dogs",
        "tags": ["cama", "ortopédica"],
        "images": [...]
      }
    }
  ],
  "subtotal": 105.48,
  "tax": 16.88,
  "total": 122.36,
  "updatedAt": "2025-11-29T10:30:00.000Z",
  "createdAt": "2025-11-28T15:20:00.000Z"
}
```

**Estructura de Carrito**:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | UUID | ID único del carrito |
| `userId` | UUID | ID del usuario propietario |
| `items` | Array[CartItem] | Items en el carrito |
| `subtotal` | Float | Suma de (precio × cantidad) de todos los items |
| `tax` | Float | IVA 16% calculado sobre el subtotal |
| `total` | Float | Subtotal + Tax |
| `updatedAt` | DateTime | Última modificación |
| `createdAt` | DateTime | Fecha de creación |

**Estructura de CartItem**:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | UUID | ID único del item |
| `cartId` | UUID | ID del carrito |
| `productId` | UUID | ID del producto |
| `quantity` | Integer | Cantidad del producto |
| `size` | String | Talla seleccionada |
| `priceAtTime` | Float | Precio al momento de agregar al carrito |
| `product` | Object | Datos completos del producto |

**Cálculos Automáticos**:
- **Subtotal**: Suma de `(priceAtTime × quantity)` de todos los items
- **Tax (IVA)**: `subtotal × 0.16` (16%)
- **Total**: `subtotal + tax`

**Errores Comunes**:
- `401 Unauthorized`: Token no provisto o inválido

---

### 2. Agregar Item al Carrito

Agrega un producto al carrito o incrementa su cantidad si ya existe con la misma talla.

**Endpoint**: `POST /cart/items`

**Autenticación**: Requerida

**Headers**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "productId": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
  "quantity": 2,
  "size": "M"
}
```

**Validaciones**:
- `productId`: Debe ser un UUID válido
- `quantity`: Entero mayor o igual a 1
- `size`: Debe ser una talla válida del producto

**Lógica de Negocio**:
1. Verifica que el producto exista
2. Verifica que la talla esté disponible en el producto
3. Verifica que haya stock suficiente
4. Si ya existe un item con el mismo `productId` + `size`: **incrementa la cantidad**
5. Si no existe: crea un nuevo CartItem
6. Recalcula `subtotal`, `tax` y `total` del carrito

**Respuesta Exitosa (201)**:
```json
{
  "id": "cart-uuid",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "items": [
    {
      "id": "item-uuid-1",
      "cartId": "cart-uuid",
      "productId": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
      "quantity": 2,
      "size": "M",
      "priceAtTime": 29.99,
      "product": {
        "id": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
        "title": "Collar Premium para Perro",
        "price": 29.99,
        ...
      }
    }
  ],
  "subtotal": 59.98,
  "tax": 9.60,
  "total": 69.58,
  "updatedAt": "2025-11-29T10:35:00.000Z",
  "createdAt": "2025-11-28T15:20:00.000Z"
}
```

**Errores Comunes**:
- `400 Bad Request`:
  - UUID de producto inválido
  - Talla no disponible para el producto
  - Stock insuficiente
  - Validaciones fallidas
- `401 Unauthorized`: Token no provisto o inválido
- `404 Not Found`: Producto no encontrado

---

### 3. Actualizar Cantidad de Item

Actualiza la cantidad de un item específico en el carrito.

**Endpoint**: `PATCH /cart/items/:itemId`

**Autenticación**: Requerida

**Headers**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Parámetros de Ruta**:
- `:itemId`: UUID del item en el carrito

**Body (JSON)**:
```json
{
  "quantity": 5
}
```

**Validaciones**:
- `quantity`: Entero mayor o igual a 1
- Verifica que haya stock suficiente para la nueva cantidad

**Respuesta Exitosa (200)**:
```json
{
  "id": "cart-uuid",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "items": [
    {
      "id": "item-uuid-1",
      "cartId": "cart-uuid",
      "productId": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
      "quantity": 5,
      "size": "M",
      "priceAtTime": 29.99,
      "product": {...}
    }
  ],
  "subtotal": 149.95,
  "tax": 23.99,
  "total": 173.94,
  "updatedAt": "2025-11-29T10:40:00.000Z",
  "createdAt": "2025-11-28T15:20:00.000Z"
}
```

**Errores Comunes**:
- `400 Bad Request`:
  - UUID inválido
  - Stock insuficiente
  - Cantidad menor a 1
- `401 Unauthorized`: Token no provisto o inválido
- `404 Not Found`: Item no encontrado en el carrito del usuario

---

### 4. Eliminar Item del Carrito

Elimina un item específico del carrito.

**Endpoint**: `DELETE /cart/items/:itemId`

**Autenticación**: Requerida

**Headers**:
```
Authorization: Bearer {token}
```

**Parámetros de Ruta**:
- `:itemId`: UUID del item a eliminar

**Respuesta Exitosa (200)**:
```json
{
  "id": "cart-uuid",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "items": [],
  "subtotal": 0,
  "tax": 0,
  "total": 0,
  "updatedAt": "2025-11-29T10:45:00.000Z",
  "createdAt": "2025-11-28T15:20:00.000Z"
}
```

**Errores Comunes**:
- `401 Unauthorized`: Token no provisto o inválido
- `404 Not Found`: Item no encontrado en el carrito del usuario

---

### 5. Vaciar Carrito

Elimina todos los items del carrito del usuario.

**Endpoint**: `DELETE /cart`

**Autenticación**: Requerida

**Headers**:
```
Authorization: Bearer {token}
```

**Respuesta Exitosa (200)**:
```json
{
  "id": "cart-uuid",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "items": [],
  "subtotal": 0,
  "tax": 0,
  "total": 0,
  "updatedAt": "2025-11-29T10:50:00.000Z",
  "createdAt": "2025-11-28T15:20:00.000Z"
}
```

**Errores Comunes**:
- `401 Unauthorized`: Token no provisto o inválido

---

### 6. Sincronizar Carrito de Invitado

Sincroniza items de un carrito de invitado (almacenado en localStorage del frontend) con el carrito del usuario autenticado. Útil cuando un usuario agrega productos sin autenticarse y luego inicia sesión.

**Endpoint**: `POST /cart/sync`

**Autenticación**: Requerida

**Headers**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "items": [
    {
      "productId": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
      "quantity": 2,
      "size": "M"
    },
    {
      "productId": "a1b2c3d4-e5f6-4a3b-9c8d-7e6f5a4b3c2d",
      "quantity": 1,
      "size": "L"
    },
    {
      "productId": "invalid-product-id",
      "quantity": 3,
      "size": "S"
    }
  ]
}
```

**Validaciones**:
- `items`: Debe ser un array con al menos 1 elemento
- Máximo 50 items por sincronización
- Cada item debe tener `productId`, `quantity` y `size` válidos

**Lógica de Negocio**:
1. Procesa cada item **individualmente**
2. Para cada item exitoso: lo agrega al carrito (o incrementa si ya existe)
3. Para items fallidos: los registra con la razón del fallo
4. Retorna resumen de sincronización con items exitosos y fallidos

**Respuesta Exitosa (200)**:
```json
{
  "synced": 2,
  "failed": [
    {
      "item": {
        "productId": "invalid-product-id",
        "quantity": 3,
        "size": "S"
      },
      "reason": "Producto no encontrado"
    }
  ],
  "cart": {
    "id": "cart-uuid",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "items": [
      {
        "id": "item-uuid-1",
        "cartId": "cart-uuid",
        "productId": "cd533345-f1f3-48c9-a62e-7dc2da50c8f8",
        "quantity": 2,
        "size": "M",
        "priceAtTime": 29.99,
        "product": {...}
      },
      {
        "id": "item-uuid-2",
        "cartId": "cart-uuid",
        "productId": "a1b2c3d4-e5f6-4a3b-9c8d-7e6f5a4b3c2d",
        "quantity": 1,
        "size": "L",
        "priceAtTime": 45.50,
        "product": {...}
      }
    ],
    "subtotal": 105.48,
    "tax": 16.88,
    "total": 122.36,
    "updatedAt": "2025-11-29T11:00:00.000Z",
    "createdAt": "2025-11-28T15:20:00.000Z"
  }
}
```

**Estructura de Respuesta**:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `synced` | Integer | Número de items sincronizados exitosamente |
| `failed` | Array[Object] | Items que fallaron con razón del error |
| `cart` | Object | Carrito actualizado después de la sincronización |

**Razones Comunes de Fallo**:
- "Producto no encontrado"
- "Talla no disponible"
- "Stock insuficiente"
- "Datos inválidos"

**Errores Comunes**:
- `400 Bad Request`:
  - Array vacío
  - Más de 50 items
  - Formato de items inválido
- `401 Unauthorized`: Token no provisto o inválido

---

## Códigos de Estado HTTP

| Código | Significado | Cuándo Ocurre |
|--------|-------------|---------------|
| `200` | OK | Operación exitosa (GET, PATCH, DELETE) |
| `201` | Created | Recurso creado exitosamente (POST) |
| `400` | Bad Request | Datos inválidos o validaciones fallidas |
| `401` | Unauthorized | Token no provisto, inválido o expirado |
| `403` | Forbidden | Token válido pero sin permisos suficientes |
| `404` | Not Found | Recurso no encontrado |
| `429` | Too Many Requests | Límite de rate limiting excedido |
| `500` | Internal Server Error | Error inesperado del servidor |

---

## Notas Importantes para Integración Móvil

### 1. Autenticación
- Guarda el token JWT de forma segura (ej: Secure Storage, Keychain)
- Incluye el token en el header `Authorization: Bearer {token}` en todas las peticiones protegidas
- El token expira en 2 horas - implementa lógica para refrescar usando `/auth/check-status`
- Maneja errores 401 redirigiendo al login

### 2. Carrito de Invitado
- Almacena items del carrito localmente mientras el usuario no esté autenticado
- Al iniciar sesión, usa el endpoint `/cart/sync` para sincronizar
- Maneja los items que fallen en la sincronización (ej: mostrar notificaciones)

### 3. Imágenes de Productos
- Las URLs de imágenes son completas y listas para usar
- Formato: `http://localhost:3000/api/files/product/{filename}`
- Implementa caché de imágenes para mejor rendimiento

### 4. Paginación de Productos
- Usa `limit` y `offset` para cargar productos en chunks
- Ejemplo: Primera página `offset=0&limit=20`, segunda página `offset=20&limit=20`
- El campo `total` en la respuesta indica el total de productos disponibles

### 5. Filtros
- Todos los parámetros de filtro son opcionales
- Puedes combinar múltiples filtros en una sola petición
- Para tallas múltiples, separa con comas: `sizes=S,M,L`

### 6. Manejo de Errores
- Parsea los mensajes de error del campo `message` en la respuesta
- Los mensajes están en español y son user-friendly
- Implementa reintentos automáticos para errores 500

### 7. Rate Limiting
- Login y registro limitados a 5 intentos cada 5 minutos
- Muestra mensaje claro al usuario si recibe error 429
- Implementa cooldown timer en la UI

### 8. Stock y Disponibilidad
- Siempre verifica el campo `stock` antes de permitir agregar al carrito
- El backend valida stock al agregar/actualizar items
- Maneja errores de "stock insuficiente" mostrando stock disponible

### 9. Cálculos de Precio
- El backend calcula automáticamente `subtotal`, `tax` (16%) y `total`
- NO calcules estos valores en el frontend - usa siempre los del servidor
- `priceAtTime` captura el precio al agregar al carrito (puede diferir del precio actual)

### 10. Performance
- Implementa debouncing en búsquedas por texto (`q` parameter)
- Usa lazy loading/infinite scroll para listas de productos
- Caché respuestas de productos que no cambian frecuentemente

---

## Ejemplos de Flujos Completos

### Flujo 1: Usuario Nuevo Compra Producto

```
1. POST /auth/register
   -> Guarda token recibido

2. GET /products?type=accesorios&species=dogs
   -> Muestra lista de productos

3. GET /products/{productId}
   -> Usuario ve detalles del producto

4. POST /cart/items
   Body: { productId, quantity: 1, size: "M" }
   -> Producto agregado al carrito

5. GET /cart
   -> Muestra resumen del carrito con totales

6. POST /cart/items
   Body: { productId: otro-producto, quantity: 2, size: "L" }
   -> Segundo producto agregado

7. PATCH /cart/items/{itemId}
   Body: { quantity: 3 }
   -> Actualiza cantidad del primer producto

8. GET /cart
   -> Muestra carrito actualizado antes de checkout
```

### Flujo 2: Usuario Invitado Luego Se Autentica

```
1. [Usuario sin autenticar]
   GET /products
   -> Agrega productos a carrito local (localStorage)

2. POST /auth/login
   -> Obtiene token

3. POST /cart/sync
   Body: { items: [...items del localStorage] }
   -> Sincroniza carrito local con servidor

4. [Limpiar localStorage]

5. GET /cart
   -> Muestra carrito sincronizado
```

### Flujo 3: Búsqueda y Filtrado Avanzado

```
1. GET /products?q=collar
   -> Búsqueda inicial por texto

2. GET /products?q=collar&species=dogs
   -> Refina búsqueda agregando filtro de especie

3. GET /products?q=collar&species=dogs&minPrice=20&maxPrice=50
   -> Refina con rango de precio

4. GET /products?q=collar&species=dogs&minPrice=20&maxPrice=50&sizes=M,L
   -> Refina con tallas específicas

5. GET /products/{productId}
   -> Usuario selecciona un producto de los resultados
```

---

## Recursos Adicionales

- **Swagger Documentation**: Accede a `http://localhost:3000/api` con el servidor corriendo para ver la documentación interactiva
- **Código Fuente**: Ver los controladores y DTOs para validaciones exactas
- **Seed Data**: Ejecuta `GET /api/seed` (solo desarrollo) para poblar la BD con datos de prueba

---

## Contacto y Soporte

Para reportar problemas o dudas sobre la API, contacta al equipo de desarrollo.

**Última actualización**: 2025-11-29
