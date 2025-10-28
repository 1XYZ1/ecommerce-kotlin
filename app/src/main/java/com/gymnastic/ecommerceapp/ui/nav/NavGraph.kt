package com.gymnastic.ecommerceapp.ui.nav

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gymnastic.ecommerceapp.ui.screens.*
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel

object Routes {
    // Rutas de autenticación
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Rutas principales de la app
    const val HOME = "home"
    const val SEARCH = "search"
    const val CART = "cart"
    const val PROFILE = "profile"

    // Rutas de productos y checkout
    const val DETAIL = "detail/{productId}"
    const val CHECKOUT = "checkout"
    const val SUCCESS = "success"

    // Rutas de direcciones
    const val SAVED_ADDRESSES = "saved_addresses"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    authViewModel: com.gymnastic.ecommerceapp.ui.viewmodels.AuthViewModel
) {
    // Verificar estado de autenticación para determinar la pantalla inicial
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Navegar automáticamente a HOME si el usuario ya está logueado
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN, // Siempre empezar en LOGIN
        // Configurar animaciones de transición entre pantallas
        enterTransition = {
            // Animación de entrada: deslizar desde la derecha
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            // Animación de salida: deslizar hacia la izquierda
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            // Animación al volver: deslizar desde la izquierda
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            // Animación al salir hacia atrás: deslizar hacia la derecha
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        // Pantallas de autenticación
        composable(Routes.LOGIN) {
            com.gymnastic.ecommerceapp.ui.screens.LoginScreen(
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
            com.gymnastic.ecommerceapp.ui.screens.RegisterScreen(
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

        // Pantallas principales de la app
        composable(Routes.HOME) {
            HomeScreen(
                cartViewModel = cartViewModel,
                onProductClick = { product ->
                    navController.navigate("detail/${product.id}")
                }
            )
        }

        composable(Routes.SEARCH) {
            com.gymnastic.ecommerceapp.ui.screens.SearchScreen(
                cartViewModel = cartViewModel,
                onProductClick = { product ->
                    navController.navigate("detail/${product.id}")
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
            com.gymnastic.ecommerceapp.ui.screens.ProfileScreen(
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

        composable(Routes.DETAIL) { backStackEntry ->
            // Extraer el productId de los argumentos de navegación
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            DetailScreen(
                productId = productId,
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
                        // Limpiar el stack de navegación hasta HOME (excluyendo HOME)
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

        composable(Routes.SAVED_ADDRESSES) {
            com.gymnastic.ecommerceapp.ui.screens.DireccionesGuardadasScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}