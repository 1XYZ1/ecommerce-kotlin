package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gymnastic.ecommerceapp.MainActivity
import com.gymnastic.ecommerceapp.domain.Product
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

/**
 * UI Tests para AddToCartDialog usando Compose Testing API
 *
 * CONCEPTO PARA ESTUDIANTES:
 * Los UI tests verifican que la interfaz se renderice correctamente y
 * responda a las interacciones del usuario. Requieren un emulador o dispositivo.
 *
 * Ventajas:
 * - Testan la UI real como la vería el usuario
 * - Detectan problemas de layout, clicks, estados visuales
 * - Más lentos que unit tests pero más confiables
 */
@RunWith(AndroidJUnit4::class)
class AddToCartDialogUITest {

    /**
     * ComposeTestRule: Herramienta principal para testing de Compose
     *
     * CONCEPTO: Test Rules
     * @get:Rule indica que JUnit debe inicializar esta regla antes de cada test
     * Usamos createAndroidComposeRule para evitar problemas de compatibilidad
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Test 1: Verificar que el diálogo muestra la información del producto
     *
     * CONCEPTO: UI Rendering Testing
     * Verificamos que todos los elementos visuales se muestren correctamente
     */
    @Test
    fun dialogMuestraInformacionDelProducto() {
        // Given: Un producto de prueba
        val producto = Product(
            id = "test-1",
            name = "Collar Premium para Perro",
            price = 29.99,
            description = "Collar ajustable de nylon",
            imageUrl = "",
            stock = 10,
            sizes = listOf("S", "M", "L")
        )

        // When: Se renderiza el diálogo
        composeTestRule.setContent {
            AddToCartDialog(
                product = producto,
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }

        // Then: Se muestra el título del diálogo
        composeTestRule.onNodeWithText("Agregar al Carrito").assertExists()

        // Then: Se muestra el nombre del producto
        composeTestRule.onNodeWithText("Collar Premium para Perro").assertExists()

        // Then: Se muestra el precio formateado correctamente
        composeTestRule.onNodeWithText("$29.99").assertExists()

        // Then: Se muestran los botones de acción
        composeTestRule.onNodeWithText("Cancelar").assertExists()
        composeTestRule.onNodeWithText("Agregar").assertExists()
    }

    /**
     * Test 2: Verificar que se puede seleccionar una talla
     *
     * CONCEPTO: User Interaction Testing
     * Simulamos clicks del usuario y verificamos que la UI responda
     */
    @Test
    fun seleccionarTallaFunciona() {
        // Given: Producto con tallas S, M, L
        val producto = crearProductoPrueba(sizes = listOf("S", "M", "L"))

        // When: Se renderiza el diálogo
        composeTestRule.setContent {
            AddToCartDialog(
                product = producto,
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }

        // When: Se hace clic en la talla "M"
        composeTestRule.onNodeWithText("M").performClick()

        // Then: La talla "M" debe seguir visible (el componente no crasheó)
        composeTestRule.onNodeWithText("M").assertExists()

        // Then: También deben estar visibles las otras tallas
        composeTestRule.onNodeWithText("S").assertExists()
        composeTestRule.onNodeWithText("L").assertExists()
    }

    /**
     * Test 3: Verificar que el callback se ejecuta con los parámetros correctos
     *
     * CONCEPTO: Callback Testing
     * Verificamos que al confirmar, el diálogo pase los valores correctos
     */
    @Test
    fun confirmarEjecutaCallbackConParametrosCorrectos() {
        // Given: Variables para capturar los parámetros del callback
        var tallaRecibida: String? = null
        var cantidadRecibida: Int? = null

        val producto = crearProductoPrueba(
            sizes = listOf("M"),
            stock = 10
        )

        // When: Se renderiza el diálogo
        composeTestRule.setContent {
            AddToCartDialog(
                product = producto,
                onDismiss = {},
                onConfirm = { size, quantity ->
                    // Capturamos los parámetros cuando se llama el callback
                    tallaRecibida = size
                    cantidadRecibida = quantity
                }
            )
        }

        // When: Se hace clic en el botón "Agregar"
        composeTestRule.onNodeWithText("Agregar").performClick()

        // Then: El callback se ejecutó con la talla correcta
        assertEquals("M", tallaRecibida)

        // Then: El callback se ejecutó con la cantidad inicial (1)
        assertEquals(1, cantidadRecibida)
    }

    /**
     * Helper Function: Crea un producto de prueba
     *
     * CONCEPTO: Test Data Builders
     * Facilita crear objetos de prueba con valores por defecto razonables
     */
    private fun crearProductoPrueba(
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
            description = "Descripción de prueba",
            imageUrl = "",
            stock = stock,
            sizes = sizes
        )
    }
}
