package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gymnastic.ecommerceapp.ui.components.AddToCartDialog
import com.gymnastic.ecommerceapp.ui.components.OutlineButton
import com.gymnastic.ecommerceapp.ui.components.PrimaryButton
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel
import com.gymnastic.ecommerceapp.utils.NativeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    productoId: String,
    cartViewModel: CartViewModel,
    onGoCart: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var producto by remember { mutableStateOf<com.gymnastic.ecommerceapp.domain.Product?>(null) }
    var estaCargando by remember { mutableStateOf(true) }

    // Estado para el diálogo de agregar al carrito
    var showAddToCartDialog by remember { mutableStateOf(false) }

    // Cargar producto de forma asíncrona
    LaunchedEffect(productoId) {
        estaCargando = true
        producto = cartViewModel.obtenerProductoPorId(productoId)
        estaCargando = false
    }

    if (estaCargando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (producto == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Producto no encontrado", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
        return
    }

    val productoActual = producto ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(productoActual.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            NativeUtils.shareProduct(
                                context = context,
                                productName = productoActual.name,
                                productDescription = productoActual.description,
                                productPrice = productoActual.price
                            )
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen del producto
            AsyncImage(
                model = ImageRequest.Builder(context).data(productoActual.imageUrl).build(),
                contentDescription = productoActual.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(productoActual.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "$${String.format("%.2f", productoActual.price)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Descripción", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(8.dp))

                Text(productoActual.description, fontSize = 16.sp, lineHeight = 24.sp)

                Spacer(modifier = Modifier.height(24.dp))

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para abrir el diálogo
                PrimaryButton(
                    onClick = {
                        showAddToCartDialog = true
                    },
                    text = "Agregar al carrito",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppDimensions.spaceM))

                OutlineButton(
                    onClick = onGoCart,
                    text = "Ver carrito",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Diálogo para agregar al carrito
    if (showAddToCartDialog && producto != null) {
        AddToCartDialog(
            product = producto!!,
            onDismiss = {
                showAddToCartDialog = false
            },
            onConfirm = { size, quantity ->
                cartViewModel.agregarAlCarrito(producto!!, size, quantity)
                NativeUtils.vibrateOnAddToCart(context)
            }
        )
    }
}
