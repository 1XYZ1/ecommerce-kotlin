package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gymnastic.ecommerceapp.domain.Product
import com.gymnastic.ecommerceapp.ui.components.ProductCard
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel
import com.gymnastic.ecommerceapp.utils.NativeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cartViewModel: CartViewModel,
    onProductClick: (Product) -> Unit
) {
    val context = LocalContext.current
    val products = cartViewModel.getAllProducts()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mi Tienda", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Productos Destacados",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = onProductClick,
                        onAddToCart = {
                            cartViewModel.addToCart(product)
                            NativeUtils.vibrateOnAddToCart(context)
                        }
                    )
                }
            }
        }
    }
}
