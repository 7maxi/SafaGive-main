# 🚀 SafaGive
<img width="200" height="500" alt="imagen" src="https://github.com/user-attachments/assets/8e18672d-cb05-4d2f-b40f-18fdb5a56c05" />

## 📝 Descripción del Proyecto
**SafaGive** es una aplicación móvil desarrollada como **Proyecto de Fin de Grado**, diseñada para operar como una plataforma de **donaciones peer-to-peer (P2P)**.      
El objetivo es digitalizar y optimizar la gestión de **material escolar** entre centros educativos e individuos, promoviendo la **economía circular** y la **solidaridad**.
Este proyecto destaca por su implementación de arquitecturas asíncronas y la integración de servicios *cloud* para funcionalidad en tiempo real. 

---

## 🛠️ Tecnología y Arquitectura

### Core Development
* **Lenguaje:** Kotlin (Garantizando interoperabilidad con Java y concisión en el código).
* **Plataforma:** Android Nativo (Alto rendimiento y acceso completo a APIs del sistema).
* **Arquitectura:** Implementación de la librería **Android Navigation** para una gestión robusta de la navegación multi-fragmento (`VistaMainFragment.kt`).

### Back-end y Servicios Cloud
* **Base de Datos:** **Firebase Realtime Database** (Seleccionado específicamente para facilitar la sincronización de datos de baja latencia, muy importante para el módulo de chat en tiempo real).
* **Autenticación:** Sistema híbrido que incluye:
    * **Login Tradicional:** Gestión de credenciales mediante validación directa contra Firebase.
    * **Google Sign-In (OAuth 2.0):** Implementación de la API moderna `CredentialManager` y **Google Identity Services** para un proceso de autenticación *one-tap* seguro y eficiente.
    * **Manejo de Tokens:** Uso de la librería `com.auth0` para la decodificación y gestión del **JSON Web Token (JWT)** de Google, extrayendo *claims* como el email.

---

## ✨ Módulos Clave Implementados

### 1. Sistema de Chat en Tiempo Real

* **Modelo de Datos:** Estructuración de las entidades `Chat` y `Message` serializables (`Serializable`).
* **Asincronía:** Uso de listeners de Firebase para la observación continua de cambios en la base de datos, lo que garantiza una entrega de mensajes instantánea.

### 2. Gestión de Usuarios y Seguridad

* **Persistencia:** La lógica de registro (`Register.kt`) y login (`Login.kt`) maneja la persistencia de credenciales y la validación de acceso (`email` como `key`).
* **Permissions:** Declaración de permisos críticos en `AndroidManifest.xml` para la funcionalidad completa: `INTERNET`, `CAMERA`, y `READ_MEDIA_IMAGES`.

### 3. Modelado de Donaciones

* **Entidad `Product`:** Definición estricta de la entidad de dominio (`Product.kt`) con *Data Classes* de Kotlin y el uso de la anotación `@PropertyName` de Firebase para mapear campos entre el código y la estructura NoSQL (`categoria`, `descripcion`, `fotos`, `titulo`, `usuario`).

---

## 💻 Configuración de Entorno de Desarrollo

Para la correcta ejecución y prueba del proyecto, se requiere la siguiente configuración:

1.  **Clonación y Setup:**
    ```bash
    git clone https://github.com/7maxi/SafaGive-main
    ```
2.  **Configuración de Firebase:**
    * El proyecto se conecta a una instancia de Firebase en la región `europe-west1`.
    * Se requiere configurar la aplicación en la Consola de Firebase y ubicar el archivo `google-services.json` en el directorio `app/`.
3.  **Client ID (Google Sign-In):**
    * El código está preconfigurado con un `ServerClientId` específico. Este debe coincidir con el ID del servidor web configurado en Firebase para la autenticación de Google.

---

## 👤 Desarrolladores

* **[Máximo Barcos Julián, Pedro Real Ramos, Manuel García Márquez, Alfonso Bervel Benavente]**
* **Proyecto de Fin de Grado:** Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)
