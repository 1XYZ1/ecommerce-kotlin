package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel
import com.gymnastic.ecommerceapp.utils.NativeUtils

@Composable
fun SuccessScreen(
    cartViewModel: CartViewModel,
    onBackHome: () -> Unit
) {
    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())
    val total = cartItems.sumOf { it.productPrice * it.quantity }

    var isVisible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(500),
        label = "success_icon_scale"
    )

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        isVisible = true

        kotlinx.coroutines.delay(200)
        NativeUtils.showPurchaseNotification(context, total)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // Ícono de éxito con animación
            Box(
                modifier = Modifier.size(120.dp).scale(scale),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Compra exitosa",
                            tint = Color.White,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "¡Compra Exitosa!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tu pedido ha sido procesado correctamente",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recibirás un email de confirmación pronto",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onBackHome,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Volver al Inicio", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Gracias por tu compra!",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
