# Guía Rápida: Firma de Aplicación

## 🔧 Cómo se Creó el Keystore (Android Studio)

### Paso 1: Generar Signed APK
1. **Build** → **Generate Signed Bundle / APK**
2. Seleccionar **APK** → Click **Next**

### Paso 2: Crear Nuevo Keystore
3. Click en **"Create new..."** (bajo Key store path)
4. Completar el formulario:

```
Key store path:     C:\Users\danie\...\ecommerceapp\ecommerce-keystore.jks
Password:           EcommerceApp2025!
Confirm:            EcommerceApp2025!

Key:
  Alias:            ecommerce-app-key
  Password:         EcommerceApp2025!
  Confirm:          EcommerceApp2025!
  Validity (years): 25

Certificate:
  First and Last Name:     Ecommerce App
  Organizational Unit:     Educational Project
  Organization:            Gymnastic
  City or Locality:        Argentina
  State or Province:       Buenos Aires
  Country Code (XX):       AR
```

5. Click **OK** → Se crea el archivo `ecommerce-keystore.jks`
6. Click **Next** → Seleccionar **release** → **Finish**

### Paso 3: Proteger Credenciales
7. Crear `keystore.properties` en la raíz del proyecto
8. Agregar `*.jks` y `keystore.properties` al `.gitignore`

---

## 📦 Archivos Generados

```
ecommerce-keystore.jks     → Tu clave privada (NUNCA compartir)
keystore.properties        → Credenciales (protegido en .gitignore)
app-release.apk           → APK firmada lista para publicar
```

## 🔑 Credenciales

```properties
Keystore Password:  EcommerceApp2025!
Key Alias:          ecommerce-app-key
Key Password:       EcommerceApp2025!
```

⚠️ **IMPORTANTE:** Guarda estas contraseñas en un lugar seguro (gestor de contraseñas, USB cifrado, etc.)

## 🚀 Generar APK Release

### Opción 1: Android Studio (Recomendado)
1. **Build** → **Generate Signed Bundle / APK**
2. Seleccionar **APK** → **Next**
3. Elegir el keystore existente (`ecommerce-keystore.jks`)
4. Ingresar contraseñas
5. Seleccionar **release** → **Finish**
6. APK en: `app/release/app-release.apk`

### Opción 2: Línea de Comandos
```bash
# Configurar JAVA_HOME
export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr"

# Generar APK firmada
./gradlew assembleRelease

# APK generada en:
# app/build/outputs/apk/release/app-release.apk
```

## 📱 Tipos de Build

| Tipo | Comando | Uso | Firmado |
|------|---------|-----|---------|
| **Debug** | `./gradlew assembleDebug` | Testing local | Automático |
| **Release** | `./gradlew assembleRelease` | Producción | Con tu keystore |

## ✅ Verificar Firma

```bash
keytool -printcert -jarfile app/build/outputs/apk/release/app-release.apk
```

## 📋 Info del Certificado

- **Owner:** CN=Ecommerce App, O=Gymnastic
- **SHA1:** `8E:47:25:3F:0F:42:91:26:33:84:65:D0:E3:5E:32:AD:48:04:B5:A7`
- **SHA256:** `79:15:EE:53:E2:E0:C6:6C:D0:9C:1A:B2:15:4B:0D:0A:14:02:75:5D:9C:31:D3:D7:D3:28:A0:F8:14:08:78:2D`
- **Validez:** Hasta 2053 (~27 años)
- **Algoritmo:** RSA 2048 bits

## 🔒 Seguridad

### ✅ Protegido en .gitignore:
- `*.jks`
- `*.keystore`
- `keystore.properties`

### ❌ NUNCA:
- Subir el keystore a Git
- Compartir las contraseñas públicamente
- Perder el keystore (no podrás actualizar la app)

## 📝 Incrementar Versión

Edita `app/build.gradle.kts`:

```kotlin
versionCode = 2        // Incrementa cada release
versionName = "1.1"    // Versión visible para usuarios
```

---

**Fecha de creación:** 2025-11-29
**Estado:** ✅ Configuración completa y verificada
