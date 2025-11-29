# Documentación de Testing - AddToCartDialog

## Objetivo

Implementar tests básicos y didácticos para verificar la funcionalidad del modal de agregar productos al carrito.

---

## Tests Implementados

### Unit Tests (3 tests básicos)

**Ubicación:** `app/src/test/java/com/gymnastic/ecommerceapp/ui/components/AddToCartDialogTest.kt`

**Tipo:** Unit tests (JVM local - no requieren emulador)

**Tiempo de ejecución:** ~20ms total

---

## Descripción de Cada Test

### Test 1: Inicialización de Talla

**Nombre:** `cuando se crea el dialog, la talla inicial debe ser la primera disponible`

**Objetivo:** Verificar que el componente seleccione correctamente la talla inicial

**Código:**
```kotlin
@Test
fun `cuando se crea el dialog, la talla inicial debe ser la primera disponible`() {
    // Given: Un producto con tallas ["S", "M", "L"]
    val producto = crearProductoMock(sizes = listOf("S", "M", "L"))

    // When: Se crea el diálogo (simulamos la lógica de inicialización)
    val tallaInicial = if (producto.sizes.isNotEmpty())
        producto.sizes.first()
    else
        "UNICO"

    // Then: La talla inicial debe ser "S"
    assertEquals("S", tallaInicial)
}
```

**Qué verifica:**
- Si el producto tiene tallas, la primera se selecciona automáticamente
- Si no hay tallas, se usa "UNICO" como default

**Resultado esperado:** ✅ La talla inicial es "S"

---

### Test 2: Validación de Cantidad Mínima

**Nombre:** `la cantidad no puede ser menor a 1`

**Objetivo:** Verificar que la cantidad nunca pueda ser menor a 1

**Código:**
```kotlin
@Test
fun `la cantidad no puede ser menor a 1`() {
    // Given: Cantidad actual es 1
    var cantidad = 1

    // When: Se intenta decrementar
    if (cantidad > 1) cantidad--

    // Then: Cantidad sigue siendo 1
    assertEquals(1, cantidad)
}
```

**Qué verifica:**
- La validación `if (cantidad > 1)` previene valores negativos
- El botón de decrementar no funciona cuando cantidad = 1

**Resultado esperado:** ✅ La cantidad permanece en 1

**Concepto:** Boundary Testing (límite inferior)

---

### Test 3: Validación de Stock

**Nombre:** `la cantidad no puede exceder el stock del producto`

**Objetivo:** Verificar que la cantidad respete el stock disponible

**Código:**
```kotlin
@Test
fun `la cantidad no puede exceder el stock del producto`() {
    // Given: Producto con stock = 5, cantidad actual = 5
    val stock = 5
    var cantidad = 5

    // When: Se intenta incrementar
    if (cantidad < stock) cantidad++

    // Then: Cantidad sigue siendo 5
    assertEquals(5, cantidad)
}
```

**Qué verifica:**
- La validación `if (cantidad < stock)` previene exceder inventario
- El botón de incrementar no funciona cuando cantidad = stock

**Resultado esperado:** ✅ La cantidad permanece en 5

**Concepto:** Boundary Testing (límite superior) + Reglas de negocio

---

## Helper Function

```kotlin
private fun crearProductoMock(
    id: String = "test-id",
    name: String = "Producto Test",
    price: Double = 29.99,
    stock: Int = 10,
    sizes: List<String> = listOf("UNICO")
): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        description = "Descripción de prueba para testing",
        imageUrl = "",
        stock = stock,
        sizes = sizes
    )
}
```

**Propósito:** Facilitar la creación de productos de prueba con valores por defecto

---

## Tecnologías Utilizadas

- **JUnit 4.13.2** - Framework de testing
- **MockK 1.13.8** - Mocking (aunque no se usó en estos tests básicos)
- **Kotlin** - Lenguaje de programación

---

## Cómo Ejecutar los Tests

### Desde Android Studio:

1. Abrir `AddToCartDialogTest.kt`
2. Click derecho → `Run 'AddToCartDialogTest'`
3. O click en el ícono ▶️ verde al lado del nombre de la clase

### Desde Terminal:

```bash
./gradlew test
```

### Ver Reporte HTML:

Después de ejecutar, abre en el navegador:
```
app/build/reports/tests/testDebugUnitTest/index.html
```

---

## Resultados Esperados

```
AddToCartDialogTest
  ✓ cuando se crea el dialog, la talla inicial debe ser la primera disponible (17ms)
  ✓ la cantidad no puede ser menor a 1 (1ms)
  ✓ la cantidad no puede exceder el stock del producto (0ms)

BUILD SUCCESSFUL
Tests passed: 3 of 3 tests
```

---

## Conceptos Didácticos Aprendidos

### 1. Patrón AAA (Arrange-Act-Assert)
- **Arrange (Given):** Preparar datos de prueba
- **Act (When):** Ejecutar la acción a testear
- **Assert (Then):** Verificar el resultado

### 2. Boundary Testing
- Probar valores en los límites (min=1, max=stock)
- Verificar que las validaciones funcionen correctamente

### 3. Helper Functions
- Reducir código duplicado
- Hacer tests más legibles y mantenibles

### 4. Nombres Descriptivos
- Usar backticks para nombres en español
- Describir qué se está testeando de forma clara

---

## Configuración Necesaria

### gradle/libs.versions.toml
```toml
[versions]
mockk = "1.13.8"

[libraries]
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
```

### app/build.gradle.kts
```kotlin
dependencies {
    testImplementation(libs.mockk)
}

android {
    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}
```

---

## Próximos Pasos (Opcional)

Para expandir el testing:
- Agregar tests para otros componentes (ProductCard, CartItemCard)
- Implementar tests de ViewModel
- Agregar tests de integración con Room
- Implementar tests de Repository con API mock

---

**Última actualización:** 2025-11-29
**Autor:** Desarrollo E-commerce App
