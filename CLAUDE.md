# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an educational Android e-commerce application built with Jetpack Compose and Material Design 3. The codebase is intentionally simplified for students, using Spanish variable names and extensive comments to make learning easier while maintaining complete functionality.

**Key Characteristics:**
- MVVM architecture with simplified Repository pattern
- Single-user application (fixed user ID: "usuario_principal")
- Mock product catalog (no external API)
- Room database for local persistence
- Hilt for dependency injection
- Material Design 3 with shadcn/ui inspired design system

## Build and Development Commands

### Building the Project
```bash
# Build the project
./gradlew build

# Clean build
./gradlew clean build

# Assemble debug APK
./gradlew assembleDebug

# Assemble release APK
./gradlew assembleRelease
```

### Running the Application
- Use Android Studio's "Run" button (Shift + F10)
- Or connect device/emulator and run: `./gradlew installDebug`
- Minimum SDK: API 21 (Android 5.0)
- Target SDK: API 36

### Database
- Database is automatically created on first app launch
- Database name: `ecommerce_database_v2`
- Location: `/data/data/com.gymnastic.ecommerce_app/databases/`
- To reset database: Uninstall and reinstall the app

## Architecture Overview

### Simplified MVVM Pattern

The app uses MVVM but with intentional simplifications for educational purposes:

```
UI Layer (Screens) → ViewModels → Repository → DAOs → Room Database
```

**Important architectural decisions:**
1. **Direct DAO Access**: Repository directly accesses DAOs without additional abstraction layers
2. **Spanish Variables**: All variables and functions use Spanish names (e.g., `estaLogueado`, `agregarAlCarrito`) to help Spanish-speaking students
3. **Single User Model**: Only one user per app with fixed ID "usuario_principal"
4. **Mock Data**: Products are hardcoded in `ProductCatalog.kt`
5. **No Search Screen**: Search functionality is integrated directly into `HomeScreen` (not a separate screen)

### Key Components

**ViewModels (ui/viewmodels/):**
- `AuthViewModel`: Authentication, user state, login/register/logout
- `CartViewModel`: Shopping cart operations, product catalog access
- `DireccionViewModel`: Saved addresses management

**Repository (data/):**
- Single `Repository.kt` class provides simplified data access
- Directly wraps DAO operations with Spanish method names
- Located at: `com.gymnastic.ecommerceapp.data.Repository`

**Database (data/local/):**
- `AppDb.kt`: Room database configuration
- Three DAOs: `CartDao`, `UsuarioDao`, `DireccionDao`
- Three entities: `CartItem`, `Usuario`, `Direccion`

### Navigation Structure

Navigation is defined in `ui/nav/NavGraph.kt` with these routes:

**Authentication Flow:**
- `login` → `register` → `home`

**Main Navigation (Bottom Nav):**
- `home`: Product catalog with integrated search
- `cart`: Shopping cart
- `profile`: User profile and settings

**Additional Screens:**
- `detail/{productId}`: Product details
- `checkout`: Checkout process
- `success`: Order confirmation
- `saved_addresses`: Address management

**Important:** There is NO separate search screen. Search is integrated into `HomeScreen`.

## Database Schema

### Usuario (Single User)
- **Table**: `usuarios`
- **Primary Key**: Fixed ID "usuario_principal"
- **Fields**: nombre, email, password (plaintext - educational only), estaLogueado
- **Relationship**: One user per app

### CartItem (Shopping Cart)
- **Table**: `cart_items`
- **Primary Key**: productId
- **Persistence**: Survives app restarts
- **Auto-update**: Changes emit Flow updates to UI

### Direccion (Saved Addresses)
- **Table**: `direcciones`
- **Primary Key**: UUID-generated ID
- **Relationship**: Foreign key to Usuario via `usuarioId`
- **Features**: Support for default address (`esPredeterminada`)

## Common Development Patterns

### Working with ViewModels

ViewModels use Spanish names. Common patterns:

```kotlin
// AuthViewModel
authViewModel.estaLogueado.collectAsState() // Check login status
authViewModel.iniciarSesion(email, password) // Login
authViewModel.cerrarSesion() // Logout

// CartViewModel
cartViewModel.itemsCarrito.collectAsState() // Observe cart items
cartViewModel.agregarAlCarrito(producto) // Add to cart
cartViewModel.actualizarCantidad(productId, cantidad) // Update quantity

// DireccionViewModel
direccionViewModel.direcciones.collectAsState() // Observe addresses
direccionViewModel.establecerPredeterminada(id) // Set default address
```

### Navigation Patterns

```kotlin
// Navigate to product detail
navController.navigate("detail/${producto.id}")

// Navigate with back stack cleanup
navController.navigate(Routes.HOME) {
    popUpTo(Routes.LOGIN) { inclusive = true }
}

// Simple back navigation
navController.popBackStack()
```

### State Management

All ViewModels use `StateFlow` for reactive state:

```kotlin
// In ViewModel
private val _estaCargando = MutableStateFlow(false)
val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

// In Composable
val cargando by viewModel.estaCargando.collectAsState()
```

## Design System

The app uses a professional design system inspired by shadcn/ui:

### Theme Files (ui/theme/)
- `Color.kt`: Zinc-based neutral palette + Blue accent colors
- `Type.kt`: Complete Material 3 typography scale
- `Dimensions.kt`: Spacing system (spaceXS to space3XL)

### Reusable Components (ui/components/)

**Buttons**: `PrimaryButton`, `SecondaryButton`, `OutlineButton`, `DestructiveButton`
**Text Fields**: `AppOutlinedTextField`, `SearchTextField`
**Cards**: `AppCard`, `ElevatedAppCard`, `ProductCard`, `CartItemCard`
**Dialogs**: `ConfirmDialog`, `InfoDialog`
**States**: `LoadingScreen`, `EmptyState`, `EmptyCart`
**Badges**: `CountBadge` (used for cart item count)

All buttons include press animations (scale 0.95) for better UX.

## UX Features

The following UX enhancements are implemented:

1. **Snackbar Feedback**: Shows confirmation when adding items to cart
2. **Confirmation Dialogs**: Required before deleting cart items
3. **Search Clear Button**: X button appears when search has text
4. **Button Press Animations**: All primary buttons have scale animation
5. **Checkout Progress Stepper**: Visual progress indicator (Carrito → Datos → Confirmación)
6. **Dark Mode**: Toggle in ProfileScreen, persisted via DataStore
7. **Haptic Feedback**: Vibration on cart additions

## Important Notes for Development

### Security (Educational Only)
- Passwords stored in **plaintext** - DO NOT use in production
- No encryption or hashing implemented
- Single user model is not production-ready

### Data Limitations
- Products are hardcoded in `ProductCatalog.kt`
- No backend API integration
- Checkout process is simulated (no real payments)
- Images are mock URLs

### Code Style
- **Spanish variables**: All variable and function names are in Spanish
- **Extensive comments**: Educational comments explain concepts
- **Simplified architecture**: Fewer layers than production apps
- **Direct patterns**: Avoid over-abstraction for clarity

### When Adding Features

1. **ViewModels**: Use Spanish names for state and functions
2. **Database**: Always use `suspend` for write operations
3. **Navigation**: Define routes in `Routes` object first
4. **Components**: Check `ui/components/` before creating new ones
5. **Spacing**: Use `AppDimensions` from theme, not hardcoded dp values
6. **Colors**: Use theme colors, not hardcoded Color values

### Dependency Injection

Hilt is configured with two modules:
- `DatabaseModule`: Provides AppDb instance
- `RepositoryModule`: Provides Repository instance

ViewModels are automatically injected via `@HiltViewModel` annotation.

## Testing Notes

Currently no unit tests are implemented. When adding tests:
- Room supports in-memory database for DAO testing
- ViewModels can be tested with fake Repository
- Use `TestDispatcher` for coroutine testing

## Gradle Configuration

- **Android Gradle Plugin**: 8.13.0
- **Kotlin**: 2.0.21
- **Compose Compiler**: Auto-configured via kotlin-compose plugin
- **Java Target**: 11

## Common Issues

1. **Database Migration**: App uses `fallbackToDestructiveMigration()` - schema changes will clear data
2. **Main Thread Queries**: Temporarily uses `allowMainThreadQueries()` for simplicity
3. **Single User**: Logout doesn't delete user, only updates `estaLogueado` flag
4. **Cart Persistence**: Cart is NOT cleared on logout (intentional for demo purposes)
