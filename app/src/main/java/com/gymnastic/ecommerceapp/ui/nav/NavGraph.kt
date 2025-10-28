package com.gymnastic.ecommerceapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gymnastic.ecommerceapp.ui.screens.*
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel

/**
 * Objeto que define todas las rutas de navegación de la aplicación
 *
 * Centralizar las rutas aquí hace el código más mantenible y evita
 * errores de tipeo en las rutas de navegación.
 */
object Routes {
    // ========== RUTAS DE AUTENTICACIÓN ==========
    const val LOGIN = "login"
    const val REGISTER = "register"

    // ========== RUTAS PRINCIPALES ==========
    const val HOME = "home"
    const val CART = "cart"
    const val PROFILE = "profile"

    // ========== RUTAS DE PRODUCTOS Y CHECKOUT ==========
    const val DETAIL = "detail/{productId}"
    const val CHECKOUT = "checkout"
    const val SUCCESS = "success"

    // ========== RUTAS DE DIRECCIONES ==========
    const val SAVED_ADDRESSES = "saved_addresses"
}

/**
 * Grafo de navegación simplificado para estudiantes
 *
 * Este componente define todas las rutas de la aplicación y cómo
 * navegar entre ellas. Se simplificó eliminando animaciones complejas
 * y la pantalla de búsqueda separada.
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - NavHost: Contenedor principal de navegación
 * - composable: Define una pantalla navegable
 * - LaunchedEffect: Ejecuta efectos cuando cambian los valores observados
 * - popUpTo: Limpia el stack de navegación
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    authViewModel: com.gymnastic.ecommerceapp.ui.viewmodels.AuthViewModel
) {
    // ========== OBSERVAR ESTADO DE AUTENTICACIÓN ==========

    /**
     * Observar si el usuario está logueado
     *
     * Cuando cambia este estado, navegamos automáticamente a la pantalla
     * correspondiente (HOME si está logueado, LOGIN si no).
     */
    val estaLogueado by authViewModel.estaLogueado.collectAsState()

    /**
     * Efecto para navegar automáticamente según el estado de login
     *
     * LaunchedEffect se ejecuta cuando cambia el valor de estaLogueado.
     * Si el usuario está logueado, navega a HOME y limpia el stack.
     */
    LaunchedEffect(estaLogueado) {
        if (estaLogueado) {
            navController.navigate(Routes.HOME) {
                // Limpiar todo el stack hasta LOGIN (incluyendo LOGIN)
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }

    // ========== CONFIGURACIÓN DE NAVEGACIÓN ==========

    /**
     * NavHost simplificado sin animaciones complejas
     *
     * Las animaciones por defecto de Compose Navigation son suficientes
     * para una app de estudiantes y son más fáciles de entender.
     */
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN // Siempre empezar en LOGIN
    ) {
        // ========== PANTALLAS DE AUTENTICACIÓN ==========

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        // ========== PANTALLAS PRINCIPALES ==========

        composable(Routes.HOME) {
            HomeScreen(
                cartViewModel = cartViewModel,
                onProductClick = { producto ->
                    navController.navigate("detail/${producto.id}")
                }
            )
        }

        composable(Routes.CART) {
            CartScreen(
                cartViewModel = cartViewModel,
                onCheckout = {
                    navController.navigate(Routes.CHECKOUT)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onNavigateToDirecciones = {
                    navController.navigate(Routes.SAVED_ADDRESSES)
                }
            )
        }

        // ========== PANTALLAS DE PRODUCTOS Y CHECKOUT ==========

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Extraer el productId de los argumentos de navegación
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            DetailScreen(
                productoId = productId,
                cartViewModel = cartViewModel,
                onGoCart = {
                    navController.navigate(Routes.CART)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.CHECKOUT) {
            CheckoutScreen(
                cartViewModel = cartViewModel,
                onSuccess = {
                    navController.navigate(Routes.SUCCESS) {
                        // Limpiar el stack hasta HOME (excluyendo HOME)
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SUCCESS) {
            SuccessScreen(
                cartViewModel = cartViewModel,
                onBackHome = {
                    navController.navigate(Routes.HOME) {
                        // Limpiar todo el stack y volver a HOME
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // ========== PANTALLAS DE DIRECCIONES ==========

        composable(Routes.SAVED_ADDRESSES) {
            DireccionesGuardadasScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}