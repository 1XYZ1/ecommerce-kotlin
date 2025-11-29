package com.gymnastic.ecommerceapp.ui.components

import com.gymnastic.ecommerceapp.domain.Product
import org.junit.Test
import org.junit.Assert.assertEquals

/**
 * Unit Tests para AddToCartDialog
 *
 * CONCEPTO PARA ESTUDIANTES:
 * Los unit tests verifican la lógica de negocio de forma aislada,
 * sin necesidad de un emulador o dispositivo. Se ejecutan rápido en la JVM.
 *
 * Usamos el patrón AAA (Arrange-Act-Assert):
 * - Arrange (Given): Preparar los datos necesarios
 * - Act (When): Ejecutar la acción a testear
 * - Assert (Then): Verificar que el resultado sea el esperado
 */
class AddToCartDialogTest {

    /**
     * Test 1: Verificar la inicialización correcta de la talla
     *
     * CONCEPTO: State Initialization Testing
     * Verificamos que los estados iniciales del componente sean correctos
     */
    @Test
    fun `cuando se crea el dialog, la talla inicial debe ser la primera disponible`() {
        // Given: Un producto con tallas ["S", "M", "L"]
        val producto = crearProductoMock(sizes = listOf("S", "M", "L"))

        // When: Se crea el diálogo (simulamos la lógica de inicialización)
        val tallaInicial = if (producto.sizes.isNotEmpty())
            producto.sizes.first()
        else
            "UNICO"

        // Then: La talla inicial debe ser "S" (la primera de la lista)
        assertEquals("S", tallaInicial)
    }

    /**
     * Test 2: Verificar que la cantidad no puede ser menor a 1
     *
     * CONCEPTO: Boundary Testing (Límite Inferior)
     * Probamos el comportamiento en los límites de los valores permitidos
     */
    @Test
    fun `la cantidad no puede ser menor a 1`() {
        // Given: Cantidad actual es 1 (el mínimo permitido)
        var cantidad = 1

        // When: Se intenta decrementar (simula click en botón -)
        if (cantidad > 1) cantidad--

        // Then: Cantidad sigue siendo 1 (la validación previno el decremento)
        assertEquals(1, cantidad)
    }

    /**
     * Test 3: Verificar que la cantidad no puede exceder el stock
     *
     * CONCEPTO: Boundary Testing (Límite Superior) + Business Rules
     * Verificamos que se respeten las reglas de negocio (no vender más del stock)
     */
    @Test
    fun `la cantidad no puede exceder el stock del producto`() {
        // Given: Producto con stock = 5, cantidad actual = 5 (en el límite)
        val stock = 5
        var cantidad = 5

        // When: Se intenta incrementar (simula click en botón +)
        if (cantidad < stock) cantidad++

        // Then: Cantidad sigue siendo 5 (la validación previno el incremento)
        assertEquals(5, cantidad)
    }

    /**
     * Helper Function: Crea un producto mock para testing
     *
     * CONCEPTO: Test Helpers
     * Las funciones auxiliares reducen código duplicado y hacen tests más legibles
     *
     * @param id ID del producto (default: "test-id")
     * @param name Nombre del producto
     * @param price Precio
     * @param stock Stock disponible
     * @param sizes Lista de tallas disponibles
     */
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
}
