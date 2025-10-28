package com.gymnastic.ecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gymnastic.ecommerceapp.ui.components.BottomNavBar
import com.gymnastic.ecommerceapp.ui.nav.NavGraph
import com.gymnastic.ecommerceapp.ui.nav.Routes
import com.gymnastic.ecommerceapp.ui.theme.EcommerceappTheme
import com.gymnastic.ecommerceapp.ui.viewmodels.AuthViewModel
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal simplificada para estudiantes
 *
 * Esta es la actividad principal de la aplicación. Se simplificó eliminando
 * la referencia a la pantalla de búsqueda y actualizando los nombres de
 * parámetros para ser más claros en español.
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - @AndroidEntryPoint: Marca la actividad para inyección de dependencias con Hilt
 * - hiltViewModel(): Obtiene ViewModels con dependencias inyectadas
 * - rememberNavController(): Crea un controlador de navegación que persiste
 * - collectAsState(): Observa cambios en Flows y recomponer la UI
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcommerceappTheme {
                EcommerceApp()
            }
        }
    }
}

/**
 * Composable principal de la aplicación
 *
 * Configura la navegación, ViewModels y la estructura general de la app.
 * Se simplificó eliminando la referencia a SearchScreen.
 */
@Composable
fun EcommerceApp() {
    // ========== CONTROLADORES Y VIEWMODELS ==========

    val controladorNavegacion = rememberNavController()
    val viewModelCarrito: CartViewModel = hiltViewModel()
    val viewModelAuth: AuthViewModel = hiltViewModel()

    // ========== ESTADO DE NAVEGACIÓN ==========

    /**
     * Obtener la ruta actual para mostrar/ocultar la navegación inferior
     */
    val entradaActual by controladorNavegacion.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    /**
     * Determinar si mostrar la navegación inferior
     *
     * Solo se muestra en las pantallas principales: HOME, CART, PROFILE
     * Se eliminó SEARCH ya que ahora la búsqueda está integrada en HOME
     */
    val mostrarNavegacionInferior = rutaActual in listOf(Routes.HOME, Routes.CART, Routes.PROFILE)

    // ========== ESTADO DEL CARRITO ==========

    /**
     * Observar los items del carrito para mostrar el badge
     */
    val itemsCarrito by viewModelCarrito.itemsDelCarrito.collectAsState(initial = emptyList())
    val cantidadItemsCarrito = itemsCarrito.sumOf { it.quantity }

    // ========== UI PRINCIPAL ==========

    Scaffold(
        bottomBar = {
            if (mostrarNavegacionInferior) {
                BottomNavBar(
                    rutaActual = rutaActual ?: Routes.HOME,
                    onNavigate = { ruta ->
                        controladorNavegacion.navigate(ruta) {
                            // Evitar múltiples copias de la misma pantalla en el stack
                            popUpTo(controladorNavegacion.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    cantidadItemsCarrito = cantidadItemsCarrito
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraph(
                navController = controladorNavegacion,
                cartViewModel = viewModelCarrito,
                authViewModel = viewModelAuth
            )
        }
    }
}
