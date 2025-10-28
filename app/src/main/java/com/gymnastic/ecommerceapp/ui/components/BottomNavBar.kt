package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Componente de navegación inferior (Bottom Navigation Bar)
 *
 * Este componente proporciona navegación entre las pantallas principales de la app:
 * - Inicio: Pantalla principal con productos destacados
 * - Búsqueda: Pantalla de búsqueda de productos
 * - Carrito: Pantalla del carrito de compras
 * - Perfil: Pantalla de perfil del usuario
 *
 * Utiliza Material Design 3 con el esquema de colores azul/naranja de la app.
 * Incluye un badge en el carrito para mostrar la cantidad de items.
 */
@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    cartItemCount: Int
) {
    // Definir las rutas y sus iconos
    val bottomNavItems = listOf(
        BottomNavItem(
            route = "home",
            label = "Inicio",
            icon = Icons.Default.Home,
            selectedIcon = Icons.Default.Home
        ),
        BottomNavItem(
            route = "search",
            label = "Buscar",
            icon = Icons.Default.Search,
            selectedIcon = Icons.Default.Search
        ),
        BottomNavItem(
            route = "cart",
            label = "Carrito",
            icon = Icons.Default.ShoppingCart,
            selectedIcon = Icons.Default.ShoppingCart
        ),
        BottomNavItem(
            route = "profile",
            label = "Perfil",
            icon = Icons.Default.Person,
            selectedIcon = Icons.Default.Person
        )
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Box {
                        Icon(
                            imageVector = if (currentRoute == item.route) item.selectedIcon else item.icon,
                            contentDescription = item.label,
                            tint = if (currentRoute == item.route) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )

                        // Mostrar badge en el carrito si hay items
                        if (item.route == "cart" && cartItemCount > 0) {
                            Badge(
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Text(
                                    text = cartItemCount.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        fontWeight = if (currentRoute == item.route) FontWeight.Medium else FontWeight.Normal,
                        color = if (currentRoute == item.route) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    onNavigate(item.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

/**
 * Clase de datos para representar un item de la navegación inferior
 * @param route ruta de navegación
 * @param label texto del item
 * @param icon icono cuando no está seleccionado
 * @param selectedIcon icono cuando está seleccionado
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)
