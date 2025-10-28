# 📚 Documentación Técnica - Ecommerce App Simplificada

## 📋 Índice

1. [Arquitectura General](#arquitectura-general)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Modelos de Datos](#modelos-de-datos)
4. [ViewModels y Lógica de Negocio](#viewmodels-y-lógica-de-negocio)
5. [Pantallas y Navegación](#pantallas-y-navegación)
6. [Base de Datos](#base-de-datos)
7. [Dependencias y Tecnologías](#dependencias-y-tecnologías)
8. [Configuración del Proyecto](#configuración-del-proyecto)
9. [Flujos Principales](#flujos-principales)
10. [Consideraciones de Desarrollo](#consideraciones-de-desarrollo)

---

## 🏗️ Arquitectura General

### Patrón Arquitectónico Simplificado

- **MVVM (Model-View-ViewModel)**: Separación clara de responsabilidades
- **Repository Pattern Simplificado**: Acceso directo a datos sin capas innecesarias
- **Dependency Injection**: Hilt para gestión de dependencias
- **Reactive Programming**: StateFlow para gestión de estado
- **Variables en Español**: Código más fácil de entender para estudiantes

### Capas de la Aplicación Simplificadas

```
┌─────────────────────────────────────┐
│           UI Layer                  │
│  (Screens, Components, Navigation)  │
├─────────────────────────────────────┤
│         ViewModel Layer             │
│    (Business Logic, State Mgmt)     │
├─────────────────────────────────────┤
│        Repository Layer             │
│     (Acceso Directo a DAOs)         │
├─────────────────────────────────────┤
│         Data Layer                  │
│   (Room DB, Local Storage)         │
└─────────────────────────────────────┘
```

### Principios de Simplificación

- **Menos Capas**: Eliminación de abstracciones innecesarias
- **Código Educativo**: Variables y comentarios en español
- **Funcionalidad Completa**: Mantiene todas las características principales
- **Fácil de Entender**: Estructura clara para estudiantes

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/gymnastic/ecommerceapp/
├── data/
│   ├── local/                    # Entidades y DAOs de Room
│   │   ├── AppDb.kt             # Configuración de base de datos
│   │   ├── CartItem.kt          # Entidad del carrito
│   │   ├── CartDao.kt           # DAO del carrito simplificado
│   │   ├── Usuario.kt           # Entidad de usuario
│   │   ├── UsuarioDao.kt        # DAO de usuario simplificado
│   │   ├── Direccion.kt         # Entidad de dirección
│   │   ├── DireccionDao.kt      # DAO de dirección simplificado
│   │   └── UserInfo.kt          # Info del usuario
│   ├── Repository.kt            # Repository simplificado
│   └── ProductCatalog.kt        # Catálogo de productos mock
├── domain/
│   └── Product.kt               # Modelo de dominio Product
├── ui/
│   ├── components/              # Componentes reutilizables
│   │   ├── BottomNavBar.kt      # Barra de navegación simplificada
│   │   └── ProductCard.kt       # Card de producto
│   ├── nav/
│   │   └── NavGraph.kt          # Navegación simplificada
│   ├── screens/                 # Pantallas de la aplicación (8 pantallas)
│   │   ├── LoginScreen.kt       # Pantalla de login
│   │   ├── RegisterScreen.kt    # Pantalla de registro
│   │   ├── HomeScreen.kt        # Pantalla principal CON búsqueda integrada
│   │   ├── DetailScreen.kt      # Detalles del producto
│   │   ├── CartScreen.kt        # Carrito de compras
│   │   ├── CheckoutScreen.kt    # Proceso de checkout
│   │   ├── SuccessScreen.kt     # Confirmación de compra
│   │   ├── ProfileScreen.kt     # Perfil de usuario
│   │   └── DireccionesGuardadasScreen.kt # Gestión de direcciones
│   ├── theme/
│   │   └── Theme.kt             # Configuración de temas
│   └── viewmodels/              # ViewModels simplificados
│       ├── AuthViewModel.kt     # Lógica de autenticación simplificada
│       ├── CartViewModel.kt     # Lógica del carrito simplificada
│       └── DireccionViewModel.kt # Lógica de direcciones simplificada
├── utils/
│   └── NativeUtils.kt           # Utilidades nativas
├── MainActivity.kt              # Actividad principal simplificada
└── EcomApp.kt                   # Clase Application
```

### Cambios Principales de Simplificación

- **Eliminado**: `SearchScreen.kt` - Búsqueda integrada en `HomeScreen`
- **Eliminado**: `UserPreferences.kt` - Lógica migrada a `AuthViewModel`
- **Simplificado**: Repository con acceso directo a DAOs
- **Simplificado**: ViewModels con variables en español
- **Simplificado**: DAOs con comentarios educativos
- **Simplificado**: Navegación sin animaciones complejas

---

## 🗄️ Modelos de Datos

### Entidades Principales

#### 1. **Usuario** (`Usuario.kt`)

```kotlin
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val id: String = "usuario_principal",
    val nombre: String,
    val email: String,
    val password: String,
    val estaLogueado: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaUltimaActualizacion: Long = System.currentTimeMillis()
)
```

- **Propósito**: Almacena información del usuario y estado de autenticación
- **Características**: Solo un usuario por aplicación (id fijo)

#### 2. **CartItem** (`CartItem.kt`)

```kotlin
@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val productId: String,
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String,
    val quantity: Int
)
```

- **Propósito**: Representa un item en el carrito de compras
- **Características**: Persistente entre sesiones

#### 3. **Direccion** (`Direccion.kt`)

```kotlin
@Entity(tableName = "direcciones")
data class Direccion(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val usuarioId: String,
    val nombreCompleto: String,
    val telefono: String,
    val direccionCompleta: String,
    val esPredeterminada: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaUltimaActualizacion: Long = System.currentTimeMillis()
)
```

- **Propósito**: Direcciones guardadas del usuario
- **Características**: Relación con Usuario, soporte para dirección predeterminada

#### 4. **Product** (`Product.kt`)

```kotlin
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String
)
```

- **Propósito**: Modelo de dominio para productos
- **Características**: Datos mock en ProductCatalog

---

## 🧠 ViewModels y Lógica de Negocio

### 1. **AuthViewModel** (`AuthViewModel.kt`)

**Responsabilidades:**

- Gestión de autenticación (login/registro/logout)
- Validación de campos de entrada
- Manejo de estados de carga y errores
- Persistencia del estado de login

**Estados Principales:**

```kotlin
val isLoggedIn: StateFlow<Boolean>
val userInfo: StateFlow<UserInfo>
val errorMessage: StateFlow<String?>
val isLoading: StateFlow<Boolean>
```

**Funciones Clave:**

- `login(email, password)`: Autenticación de usuario
- `register(name, email, password, confirmPassword)`: Registro de nuevo usuario
- `logout()`: Cierre de sesión

### 2. **CartViewModel** (`CartViewModel.kt`)

**Responsabilidades:**

- Gestión del carrito de compras
- Operaciones CRUD en items del carrito
- Cálculo de totales
- Acceso al catálogo de productos

**Estados Principales:**

```kotlin
val cartItems: Flow<List<CartItem>>
```

**Funciones Clave:**

- `addToCart(product)`: Agregar producto al carrito
- `removeFromCart(productId)`: Eliminar producto del carrito
- `updateQuantity(productId, quantity)`: Actualizar cantidad
- `clearCart()`: Limpiar carrito completo
- `getAllProducts()`: Obtener catálogo de productos

### 3. **DireccionViewModel** (`DireccionViewModel.kt`)

**Responsabilidades:**

- Gestión de direcciones guardadas
- Operaciones CRUD en direcciones
- Gestión de dirección predeterminada
- Validación de datos de dirección

**Estados Principales:**

```kotlin
val direcciones: StateFlow<List<Direccion>>
val direccionPredeterminada: StateFlow<Direccion?>
val estaCargando: StateFlow<Boolean>
val mensajeError: StateFlow<String?>
```

**Funciones Clave:**

- `agregarDireccion()`: Agregar nueva dirección
- `actualizarDireccion()`: Modificar dirección existente
- `eliminarDireccion()`: Eliminar dirección
- `establecerPredeterminada()`: Marcar como predeterminada

---

## 📱 Pantallas y Navegación

### Rutas de Navegación (`Routes`)

```kotlin
object Routes {
    // Autenticación
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Principales
    const val HOME = "home"
    const val SEARCH = "search"
    const val CART = "cart"
    const val PROFILE = "profile"

    // Productos y Checkout
    const val DETAIL = "detail/{productId}"
    const val CHECKOUT = "checkout"
    const val SUCCESS = "success"

    // Direcciones
    const val SAVED_ADDRESSES = "saved_addresses"
}
```

### Pantallas Principales

#### 1. **LoginScreen** (`LoginScreen.kt`)

- **Propósito**: Autenticación de usuarios existentes
- **Características**: Validación en tiempo real, manejo de errores
- **Navegación**: → RegisterScreen, → HomeScreen

#### 2. **RegisterScreen** (`RegisterScreen.kt`)

- **Propósito**: Registro de nuevos usuarios
- **Características**: Validación completa, confirmación de contraseña
- **Navegación**: → LoginScreen, → HomeScreen

#### 3. **HomeScreen** (`HomeScreen.kt`)

- **Propósito**: Pantalla principal con catálogo de productos
- **Características**: Grid responsivo, productos destacados
- **Navegación**: → DetailScreen

#### 4. **SearchScreen** (`SearchScreen.kt`)

- **Propósito**: Búsqueda de productos
- **Características**: Filtrado en tiempo real, barra de búsqueda
- **Navegación**: → DetailScreen

#### 5. **DetailScreen** (`DetailScreen.kt`)

- **Propósito**: Vista detallada del producto
- **Características**: Imágenes con Coil, selector de cantidad
- **Navegación**: → CartScreen, ← Back

#### 6. **CartScreen** (`CartScreen.kt`)

- **Propósito**: Gestión del carrito de compras
- **Características**: Modificación de cantidades, cálculo de totales
- **Navegación**: → CheckoutScreen, ← Back

#### 7. **CheckoutScreen** (`CheckoutScreen.kt`)

- **Propósito**: Proceso de checkout
- **Características**: Formulario completo, gestión de direcciones
- **Navegación**: → SuccessScreen, ← Back

#### 8. **SuccessScreen** (`SuccessScreen.kt`)

- **Propósito**: Confirmación de compra exitosa
- **Características**: Mensaje de éxito, navegación automática
- **Navegación**: → HomeScreen

#### 9. **ProfileScreen** (`ProfileScreen.kt`)

- **Propósito**: Perfil del usuario
- **Características**: Información del usuario, gestión de direcciones
- **Navegación**: → DireccionesGuardadasScreen, → LoginScreen (logout)

#### 10. **DireccionesGuardadasScreen** (`DireccionesGuardadasScreen.kt`)

- **Propósito**: Gestión de direcciones guardadas
- **Características**: CRUD completo, dirección predeterminada
- **Navegación**: ← Back

### Animaciones de Navegación

- **Duración**: 300ms
- **Tipo**: Slide horizontal + Fade
- **Direccionalidad**:
  - Adelante: Desliza desde derecha
  - Atrás: Desliza desde izquierda

---

## 🗃️ Base de Datos

### Configuración (`AppDb.kt`)

```kotlin
@Database(
    entities = [CartItem::class, Usuario::class, Direccion::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase()
```

### Características

- **Nombre**: `ecommerce_database_v2`
- **Migración**: `fallbackToDestructiveMigration()`
- **Thread**: `allowMainThreadQueries()` (temporal)
- **Patrón**: Singleton con `@Volatile`

### DAOs Principales

#### 1. **CartDao** (`CartDao.kt`)

```kotlin
@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)

    @Delete
    suspend fun deleteCartItem(item: CartItem)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
```

#### 2. **UsuarioDao** (`UsuarioDao.kt`)

```kotlin
@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE id = 'usuario_principal'")
    suspend fun getUsuarioPrincipal(): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario)

    @Update
    suspend fun updateUsuario(usuario: Usuario)

    @Query("UPDATE usuarios SET estaLogueado = :estaLogueado WHERE id = 'usuario_principal'")
    suspend fun updateLoginStatus(estaLogueado: Boolean)
}
```

#### 3. **DireccionDao** (`DireccionDao.kt`)

```kotlin
@Dao
interface DireccionDao {
    @Query("SELECT * FROM direcciones WHERE usuarioId = :usuarioId")
    fun getDireccionesByUsuario(usuarioId: String): Flow<List<Direccion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDireccion(direccion: Direccion)

    @Update
    suspend fun updateDireccion(direccion: Direccion)

    @Delete
    suspend fun deleteDireccion(direccion: Direccion)

    @Query("UPDATE direcciones SET esPredeterminada = 0 WHERE usuarioId = :usuarioId")
    suspend fun clearPredeterminada(usuarioId: String)
}
```

---

## 🔧 Dependencias y Tecnologías

### Core Android

```kotlin
// Kotlin y Android Core
implementation("androidx.core:core-ktx:1.10.1")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
implementation("androidx.activity:activity-compose:1.8.0")

// Jetpack Compose
implementation(platform("androidx.compose:compose-bom:2024.09.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material3:material3-adaptive-navigation-suite")
```

### Navegación y Estado

```kotlin
// Navegación
implementation("androidx.navigation:navigation-compose:2.8.3")

// Animaciones
implementation("androidx.compose.animation:animation:1.5.4")

// Dependency Injection
implementation("com.google.dagger:hilt-android:2.48")
kapt("com.google.dagger:hilt-compiler:2.48")
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
```

### Base de Datos

```kotlin
// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
```

### UI/Imágenes

```kotlin
// Carga de imágenes
implementation("io.coil-kt:coil-compose:2.6.0")
```

### Versiones

- **Android Gradle Plugin**: 8.13.0
- **Kotlin**: 2.0.21
- **Compile SDK**: 36
- **Min SDK**: 21
- **Target SDK**: 36

---

## ⚙️ Configuración del Proyecto

### Gradle Files

#### `build.gradle.kts` (Project)

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}
```

#### `build.gradle.kts` (App)

```kotlin
android {
    namespace = "com.gymnastic.ecommerceapp"
    compileSdk { version = release(36) }

    defaultConfig {
        applicationId = "com.gymnastic_app"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
}
```

### Manifest (`AndroidManifest.xml`)

```xml
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<application
    android:name=".EcomApp"
    android:theme="@style/Theme.Ecommerceapp">

    <activity
        android:name=".MainActivity"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>
```

---

## 🔄 Flujos Principales

### 1. Flujo de Autenticación

```
LoginScreen → AuthViewModel.login() → UserPreferences → HomeScreen
     ↓
RegisterScreen → AuthViewModel.register() → UserPreferences → HomeScreen
```

### 2. Flujo de Compra

```
HomeScreen → DetailScreen → CartScreen → CheckoutScreen → SuccessScreen
     ↓              ↓           ↓            ↓
ProductCard → AddToCart → UpdateQuantity → ProcessOrder → ClearCart
```

### 3. Flujo de Gestión de Direcciones

```
ProfileScreen → DireccionesGuardadasScreen → DireccionViewModel
     ↓                    ↓
ShowAddresses → CRUD Operations → Database Update
```

### 4. Flujo de Búsqueda

```
SearchScreen → SearchQuery → FilterProducts → DisplayResults → DetailScreen
```

---

## 🚨 Consideraciones de Desarrollo

### Seguridad

- **Contraseñas**: Almacenadas en texto plano (NO para producción)
- **Validación**: Campos de entrada validados en ViewModels
- **Autenticación**: Sistema básico con SharedPreferences

### Rendimiento

- **Imágenes**: Carga optimizada con Coil
- **Base de Datos**: Queries optimizadas con índices
- **UI**: Lazy loading en grids y listas
- **Estado**: StateFlow para reactividad eficiente

### Escalabilidad

- **Arquitectura**: MVVM permite fácil extensión
- **Repository**: Abstracción lista para APIs externas
- **Modularización**: Estructura preparada para módulos

### Mantenibilidad

- **Código**: Documentación extensa en funciones
- **Patrones**: Uso consistente de patrones establecidos
- **Testing**: Estructura preparada para unit tests
- **Logging**: Sistema de logs implementado

### Limitaciones Actuales

- **Datos Mock**: Productos hardcodeados en ProductCatalog
- **Usuario Único**: Solo soporta un usuario por aplicación
- **Sin API**: No hay integración con backend real
- **Sin Pagos**: Proceso de checkout simulado

---

## 📝 Notas de Desarrollo

### Próximas Mejoras

1. **Integración API**: Conectar con backend real
2. **Múltiples Usuarios**: Soporte para múltiples cuentas
3. **Pagos Reales**: Integración con pasarelas de pago
4. **Notificaciones**: Sistema de notificaciones push
5. **Modo Offline**: Funcionalidad sin conexión
6. **Testing**: Unit tests y UI tests
7. **CI/CD**: Pipeline de integración continua

### Decisiones de Diseño

- **Material Design 3**: Para consistencia visual
- **Jetpack Compose**: Para UI moderna y declarativa
- **Room Database**: Para persistencia local robusta
- **Hilt DI**: Para gestión de dependencias
- **StateFlow**: Para programación reactiva

---

## 🎯 Simplificaciones Implementadas

### Objetivo Principal

Esta aplicación fue simplificada específicamente para estudiantes, manteniendo toda la funcionalidad pero con código más fácil de entender y mantener.

### Cambios Realizados

#### 1. **Eliminación de Capas Innecesarias**

- ❌ **Eliminado**: `UserPreferences.kt` - Lógica migrada directamente a `AuthViewModel`
- ✅ **Resultado**: Menos archivos, acceso más directo a datos

#### 2. **Repository Simplificado**

- ❌ **Antes**: Múltiples funciones wrapper que solo pasaban datos
- ✅ **Después**: Acceso directo a DAOs con comentarios educativos
- ✅ **Variables en español**: `baseDeDatos`, `daoCarrito`, `daoUsuario`

#### 3. **ViewModels Educativos**

- ✅ **Variables en español**: `estaLogueado`, `mensajeError`, `estaCargando`
- ✅ **Comentarios explicativos**: Conceptos importantes para estudiantes
- ✅ **Funciones en español**: `iniciarSesion()`, `registrarUsuario()`, `cerrarSesion()`

#### 4. **Navegación Simplificada**

- ❌ **Eliminado**: `SearchScreen.kt` - Pantalla separada innecesaria
- ✅ **Integrado**: Búsqueda simple en `HomeScreen` con `TextField`
- ❌ **Eliminado**: Animaciones complejas de navegación
- ✅ **Resultado**: Navegación más simple y fácil de entender

#### 5. **DAOs con Comentarios Educativos**

- ✅ **Comentarios en español**: Explicación de cada query SQL
- ✅ **Conceptos explicados**: `@Transaction`, `Flow`, `suspend`
- ✅ **Queries simplificadas**: Solo funciones esenciales

#### 6. **Pantallas Actualizadas**

- ✅ **Variables en español**: `itemsCarrito`, `producto`, `estaCargando`
- ✅ **Funciones actualizadas**: Uso de nuevos nombres de funciones
- ✅ **Comentarios educativos**: Explicación de conceptos Compose

### Beneficios para Estudiantes

#### 📚 **Facilidad de Comprensión**

- Variables y funciones en español
- Comentarios explicativos en cada concepto importante
- Estructura más directa sin capas innecesarias

#### 🔧 **Mantenibilidad**

- Menos archivos que mantener
- Código más directo y fácil de seguir
- Menos abstracciones que confundan

#### 🎓 **Valor Educativo**

- Conceptos claramente explicados
- Ejemplos prácticos de MVVM
- Uso correcto de Room, Compose y Hilt

### Funcionalidades Mantenidas

✅ **Todas las pantallas principales** (8 pantallas)
✅ **Autenticación completa** (login/registro/logout)
✅ **Carrito de compras** con persistencia
✅ **Gestión de direcciones** múltiples
✅ **Proceso de checkout** completo
✅ **Búsqueda de productos** integrada
✅ **Navegación fluida** entre pantallas

### Tecnologías Mantenidas

✅ **Room Database** para persistencia
✅ **Jetpack Compose** para UI moderna
✅ **Hilt** para inyección de dependencias
✅ **StateFlow** para programación reactiva
✅ **Material Design 3** para UI consistente

---

_Documentación generada para Ecommerce App Simplificada v2.0_
_Última actualización: Diciembre 2024_
_Simplificada para estudiantes - Mantiene toda la funcionalidad_
