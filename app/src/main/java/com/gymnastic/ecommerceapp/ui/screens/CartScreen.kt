package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gymnastic.ecommerceapp.data.local.CartItem
import com.gymnastic.ecommerceapp.ui.components.CartItemCard
import com.gymnastic.ecommerceapp.ui.components.ConfirmDialog
import com.gymnastic.ecommerceapp.ui.components.EmptyCart
import com.gymnastic.ecommerceapp.ui.components.PrimaryButton
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    val itemsCarrito by cartViewModel.itemsDelCarrito.collectAsState(initial = emptyList())

    // Cálculos según la API
    val subtotal = itemsCarrito.sumOf { it.productPrice * it.quantity }
    val tax = subtotal * 0.16  // IVA 16%
    val total = subtotal + tax

    // Estado para el diálogo de confirmación
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<CartItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (itemsCarrito.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Subtotal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal:", fontSize = 16.sp)
                            Text(
                                "$${String.format("%.2f", subtotal)}",
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Tax (IVA)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("IVA (16%):", fontSize = 16.sp)
                            Text(
                                "$${String.format("%.2f", tax)}",
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))

                        // Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "$${String.format("%.2f", total)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(AppDimensions.spaceM))

                        PrimaryButton(
                            onClick = onCheckout,
                            text = "Proceder al Checkout",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (itemsCarrito.isEmpty()) {
            EmptyCart(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                onStartShopping = onBack
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(itemsCarrito) { cartItem ->
                    CartItemCard(
                        cartItem = cartItem,
                        onUpdateQuantity = { newQuantity ->
                            cartViewModel.actualizarCantidad(cartItem.productId, newQuantity)
                        },
                        onRemoveItem = {
                            // Mostrar diálogo de confirmación
                            itemToDelete = cartItem
                            showDeleteDialog = true
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog && itemToDelete != null) {
        ConfirmDialog(
            onDismiss = {
                showDeleteDialog = false
                itemToDelete = null
            },
            onConfirm = {
                itemToDelete?.let { item ->
                    cartViewModel.eliminarDelCarrito(item.productId)
                }
                itemToDelete = null
            },
            title = "Eliminar producto",
            message = "¿Estás seguro de que deseas eliminar ${itemToDelete?.productName} del carrito?",
            confirmText = "Eliminar",
            dismissText = "Cancelar",
            isDestructive = true
        )
    }
}
