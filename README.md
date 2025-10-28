# 🛒 Ecommerce App

Una aplicación móvil de comercio electrónico moderna desarrollada con **Jetpack Compose** y **Material Design 3**. Esta app ofrece una experiencia de compra completa con autenticación de usuarios, catálogo de productos, carrito de compras y proceso de checkout.

## ✨ Características Principales

### 🔐 Autenticación de Usuarios
- **Login/Registro**: Sistema completo de autenticación con validación de campos
- **Gestión de sesiones**: Mantiene el estado de login del usuario
- **Validación en tiempo real**: Campos con validación instantánea

### 🏠 Pantalla Principal
- **Catálogo de productos**: Grid responsivo con productos destacados
- **Navegación intuitiva**: Bottom navigation con Material Design 3
- **Diseño moderno**: Interfaz limpia y atractiva

### 🔍 Búsqueda de Productos
- **Búsqueda en tiempo real**: Filtra productos por nombre y descripción
- **Interfaz de búsqueda**: Barra de búsqueda integrada
- **Resultados instantáneos**: Filtrado dinámico sin necesidad de botones

### 🛍️ Carrito de Compras
- **Gestión de productos**: Agregar, eliminar y modificar cantidades
- **Cálculo automático**: Total actualizado en tiempo real
- **Persistencia local**: Los items se mantienen entre sesiones
- **Badge de notificación**: Contador visual en la navegación

### 📱 Detalles del Producto
- **Vista detallada**: Información completa del producto
- **Imágenes de alta calidad**: Carga optimizada con Coil
- **Selector de cantidad**: Control intuitivo para agregar al carrito
- **Navegación fluida**: Transiciones suaves entre pantallas

### 💳 Proceso de Checkout
- **Formulario completo**: Datos de envío y facturación
- **Gestión de direcciones**: Direcciones guardadas y predeterminadas
- **Métodos de pago**: Selección de forma de pago
- **Validación de datos**: Campos obligatorios y formato correcto

### 👤 Perfil de Usuario
- **Información personal**: Datos del usuario logueado
- **Gestión de direcciones**: CRUD completo de direcciones guardadas
- **Cerrar sesión**: Logout seguro con limpieza de datos

### 🎉 Pantalla de Éxito
- **Confirmación de compra**: Mensaje de éxito tras completar el pedido
- **Navegación inteligente**: Retorno automático al inicio
- **Experiencia satisfactoria**: Feedback visual positivo

## 🏗️ Arquitectura Técnica

### 📱 Frontend
- **Jetpack Compose**: UI moderna y declarativa
- **Material Design 3**: Diseño consistente y atractivo
- **Navigation Compose**: Navegación fluida entre pantallas
- **Animaciones direccionales**: Transiciones lógicas según el flujo

### 🗄️ Backend Local
- **Room Database**: Persistencia local de datos
- **SQLite**: Base de datos robusta y eficiente
- **Entidades**: Usuario, Producto, Carrito, Dirección

### 🔧 Patrones de Arquitectura
- **MVVM**: Separación clara de responsabilidades
- **Repository Pattern**: Abstracción de acceso a datos
- **Dependency Injection**: Hilt para gestión de dependencias
- **State Management**: StateFlow para reactividad

### 🎨 UI/UX
- **Responsive Design**: Adaptable a diferentes tamaños de pantalla
- **Dark/Light Theme**: Soporte para temas (preparado)
- **Animaciones suaves**: Transiciones de 400ms con easing natural
- **Feedback visual**: Estados de carga, éxito y error

## 🛠️ Tecnologías Utilizadas

### Core Android
- **Kotlin**: Lenguaje principal de desarrollo
- **Android SDK**: API 21+ (Android 5.0+)
- **Jetpack Compose**: Framework de UI moderno
- **Material Design 3**: Sistema de diseño de Google

### Base de Datos
- **Room**: ORM para SQLite
- **SQLite**: Base de datos local
- **Coroutines**: Programación asíncrona

### Navegación y Estado
- **Navigation Compose**: Sistema de navegación
- **Hilt**: Inyección de dependencias
- **StateFlow**: Gestión de estado reactivo

### UI/Imágenes
- **Coil**: Carga y cache de imágenes
- **Material 3**: Componentes de diseño
- **Compose Animation**: Animaciones fluidas

## 📦 Instalación y Configuración

### Prerrequisitos
- Android Studio Hedgehog o superior
- Android SDK API 21+
- Kotlin 2.0.21+

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/ecommerceapp.git
   cd ecommerceapp
   ```

2. **Abrir en Android Studio**
   - Abrir Android Studio
   - Seleccionar "Open an existing project"
   - Navegar al directorio del proyecto

3. **Sincronizar dependencias**
   - Android Studio sincronizará automáticamente
   - O ejecutar: `./gradlew build`

4. **Ejecutar la aplicación**
   - Conectar dispositivo Android o iniciar emulador
   - Presionar el botón "Run" o `Shift + F10`

## 🎯 Funcionalidades Destacadas

### 🔄 Animaciones Inteligentes
- **Navegación direccional**: Las transiciones siguen la lógica del movimiento
- **Duración optimizada**: 400ms para transiciones suaves
- **Easing natural**: FastOutSlowInEasing para movimiento orgánico

### 💾 Persistencia Inteligente
- **Carrito persistente**: Los items se mantienen entre sesiones
- **Direcciones guardadas**: Sistema completo de gestión de direcciones
- **Estado de autenticación**: Login mantenido automáticamente

### 🎨 Diseño Responsivo
- **Grid adaptativo**: Se ajusta al tamaño de pantalla
- **Componentes reutilizables**: Cards y botones consistentes
- **Espaciado consistente**: Sistema de spacing uniforme

## 📱 Capturas de Pantalla

*[Aquí puedes agregar capturas de pantalla de tu aplicación]*

## 🚀 Próximas Mejoras

- [ ] **Integración con API**: Conectar con backend real
- [ ] **Notificaciones push**: Alertas de ofertas y pedidos
- [ ] **Modo offline**: Funcionalidad sin conexión
- [ ] **Pagos reales**: Integración con pasarelas de pago
- [ ] **Reviews de productos**: Sistema de calificaciones
- [ ] **Wishlist**: Lista de deseos personalizada

## 🤝 Contribuciones

Las contribuciones son bienvenidas! Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

**Tu Nombre**
- GitHub: [@tu-usuario](https://github.com/tu-usuario)
- LinkedIn: [Tu Perfil](https://linkedin.com/in/tu-perfil)

## 🙏 Agradecimientos

- Google por Jetpack Compose
- Material Design Team
- Comunidad de Android Developers
- Todos los contribuidores del proyecto

---

⭐ **¡Si te gusta este proyecto, no olvides darle una estrella!** ⭐
