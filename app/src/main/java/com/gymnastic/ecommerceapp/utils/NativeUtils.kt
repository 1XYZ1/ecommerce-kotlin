package com.gymnastic.ecommerceapp.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Utilidades para funciones nativas de Android
 *
 * Esta clase encapsula las llamadas a APIs nativas de Android para mantener
 * el código limpio y facilitar el mantenimiento.
 */
object NativeUtils {

    /**
     * Comparte un producto usando el Intent nativo de Android Share
     *
     * @param context Contexto de la aplicación
     * @param productName Nombre del producto a compartir
     * @param productDescription Descripción del producto
     * @param productPrice Precio del producto
     */
    fun shareProduct(
        context: Context,
        productName: String,
        productDescription: String,
        productPrice: Double
    ) {
        val shareText = """
            ¡Mira este producto increíble!

            📱 $productName
            💰 Precio: $${String.format("%.2f", productPrice)}
            📝 $productDescription

            Descárgate nuestra app para ver más productos como este.
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        // Crear un chooser para que el usuario elija cómo compartir
        val chooserIntent = Intent.createChooser(shareIntent, "Compartir producto")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    /**
     * Hace vibrar el dispositivo al agregar un producto al carrito
     *
     * @param context Contexto de la aplicación
     */
    fun vibrateOnAddToCart(context: Context) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Para Android 12+ usar VibratorManager
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            // Para versiones anteriores usar Vibrator directamente
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Crear un efecto de vibración corto y suave
            val vibrationEffect = VibrationEffect.createOneShot(
                100, // Duración en milisegundos
                VibrationEffect.DEFAULT_AMPLITUDE
            )
            vibrator.vibrate(vibrationEffect)
        } else {
            // Para versiones anteriores usar el método deprecated
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }

    /**
     * Muestra una notificación local cuando se completa una compra
     *
     * @param context Contexto de la aplicación
     * @param totalAmount Monto total de la compra
     */
    fun showPurchaseNotification(context: Context, totalAmount: Double) {
        val notificationManager = NotificationManagerCompat.from(context)

        // Verificar si el permiso está disponible y las notificaciones están habilitadas
        if (notificationManager.areNotificationsEnabled()) {
            val notification = NotificationCompat.Builder(context, "purchase_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Icono simple del sistema
                .setContentTitle("¡Compra exitosa!")
                .setContentText("Tu compra por $${String.format("%.2f", totalAmount)} se ha completado.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true) // Se cancela al tocarla
                .build()

            // Mostrar la notificación con un ID único
            notificationManager.notify(1001, notification)
        }
        // Si las notificaciones no están habilitadas, simplemente no se muestra
    }
}
