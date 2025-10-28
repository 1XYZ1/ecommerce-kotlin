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
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions

/**
 * Componente de navegación inferior simplificado para estudiantes
 *
 * Este componente proporciona navegación entre las pantallas principales de la app:
 * - Inicio: Pantalla principal con productos y búsqueda integrada
 * - Carrito: Pantalla del carrito de compras
 * - Perfil: Pantalla de perfil del usuario con gestión de direcciones
 *
 * Se simplificó eliminando el botón de búsqueda ya que ahora la búsqueda
 * está integrada en la pantalla de inicio.
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - NavigationBar: Componente de Material Design 3 para navegación inferior
 * - Badge: Indicador visual que muestra la cantidad de items en el carrito
 * - MaterialTheme: Sistema de colores y estilos de Material Design
 */
@Composable
fun BottomNavBar(
    rutaActual: String,
    onNavigate: (String) -> Unit,
    cantidadItemsCarrito: Int
) {
    // ========== CONFIGURACIÓN DE ITEMS ==========

    /**
     * Lista de items de navegación simplificada
     *
     * Solo incluye las pantallas principales: Inicio, Carrito y Perfil.
     * La búsqueda ahora está integrada en la pantalla de inicio.
     */
    val itemsNavegacion = listOf(
        BottomNavItem(
            route = "home",
            label = "Inicio",
            icon = Icons.Default.Home,
            selectedIcon = Icons.Default.Home
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

    // ========== UI ==========

    /**
     * Barra de navegación inferior
     *
     * Utiliza Material Design 3 con colores del tema actual.
     */
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        itemsNavegacion.forEach { item ->
            NavigationBarItem(
                icon = {
                    Box {
                        // Icono principal del item
                        Icon(
                            imageVector = if (rutaActual == item.route) item.selectedIcon else item.icon,
                            contentDescription = item.label,
                            tint = if (rutaActual == item.route) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )

                        // Badge para mostrar cantidad de items en el carrito
                        if (item.route == "cart" && cantidadItemsCarrito > 0) {
                            CountBadge(
                                count = cantidadItemsCarrito,
                                modifier = Modifier.align(Alignment.TopEnd)
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        fontWeight = if (rutaActual == item.route) FontWeight.Medium else FontWeight.Normal,
                        color = if (rutaActual == item.route) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                },
                selected = rutaActual == item.route,
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
 * Clase de datos simple para representar un item de navegación
 *
 * @param route ruta de navegación (debe coincidir con Routes)
 * @param label texto que se muestra al usuario
 * @param icon icono cuando el item no está seleccionado
 * @param selectedIcon icono cuando el item está seleccionado
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)
