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

@Composable
fun EcommerceApp() {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    // Obtener la ruta actual para mostrar/ocultar la navegación inferior
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Determinar si mostrar la navegación inferior
    val showBottomNav = currentRoute in listOf(Routes.HOME, Routes.SEARCH, Routes.CART, Routes.PROFILE)

    // Obtener cantidad de items en el carrito para el badge
    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())
    val cartItemCount = cartItems.sumOf { it.quantity }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(
                    currentRoute = currentRoute ?: Routes.HOME,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Evitar múltiples copias de la misma pantalla en el stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    cartItemCount = cartItemCount
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraph(
                navController = navController,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel
            )
        }
    }
}
