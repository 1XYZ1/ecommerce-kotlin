package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions

/**
 * Componentes para estados de carga
 *
 * Indicadores visuales consistentes mientras se cargan datos.
 */

/**
 * Indicador de carga centrado - Para pantallas completas
 *
 * Uso: Cuando toda la pantalla está cargando
 */
@Composable
fun LoadingScreen(
    message: String = "Cargando...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppDimensions.spaceNormal)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Indicador de carga inline - Para secciones específicas
 *
 * Uso: Cuando solo una parte de la pantalla está cargando
 */
@Composable
fun LoadingIndicator(
    message: String? = null,
    modifier: Modifier = Modifier,
    size: LoadingSize = LoadingSize.Medium
) {
    val indicatorSize = when (size) {
        LoadingSize.Small -> 24.dp
        LoadingSize.Medium -> 32.dp
        LoadingSize.Large -> 48.dp
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppDimensions.spaceM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(indicatorSize),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
        if (message != null) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Indicador de carga mínimo - Solo el spinner
 *
 * Uso: Espacios muy reducidos
 */
@Composable
fun LoadingSpinner(
    modifier: Modifier = Modifier,
    size: LoadingSize = LoadingSize.Small
) {
    val indicatorSize = when (size) {
        LoadingSize.Small -> 20.dp
        LoadingSize.Medium -> 32.dp
        LoadingSize.Large -> 48.dp
    }

    CircularProgressIndicator(
        modifier = modifier.size(indicatorSize),
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 2.dp
    )
}

/**
 * Tamaños de indicadores de carga
 */
enum class LoadingSize {
    Small,
    Medium,
    Large
}
