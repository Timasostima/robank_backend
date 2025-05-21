# Robank Backend

API REST que gestiona la persistencia de datos y la autenticación para el sistema Robank de gestión de finanzas personales.

## 🛠️ Tecnologías

- **Spring Boot**: Framework Java para el desarrollo de aplicaciones
- **Spring Security**: Autenticación y autorización
- **Firebase Auth**: Verificación de tokens y gestión de usuarios
- **Spring Data JPA**: Persistencia de datos
- **PostgreSQL**: Base de datos relacional
- **Gradle**: Gestión de dependencias y construcción
- **Docker/Docker Compose**: Contenerización para despliegue

## 📊 Modelo de datos

- **Usuarios**: Información de perfil y preferencias
- **Categorías**: Clasificación personalizable para gastos
- **Gastos**: Registro de transacciones financieras
- **Metas**: Objetivos financieros con seguimiento de progreso

## 🚀 Instalación

### Prerrequisitos

- JDK 21+
- Docker & Docker Compose
- Variables de entorno configuradas para Firebase

### Desarrollo local

```bash
# Clonar el repositorio
git clone https://github.com/Timasostima/robank_backend.git
cd robank_backend

# Ejecutar con Docker Compose (recomendado)
docker-compose up

# Alternativamente, ejecutar con Gradle
./gradlew bootRun
```

## ⚙️ Configuración

### Variables de entorno requeridas

Necesitarás configurar las siguientes variables de entorno para la autenticación con Firebase:

```properties
FIREBASE_PROJECT_ID=tu-project-id
FIREBASE_PRIVATE_KEY_ID=tu-private-key-id
FIREBASE_PRIVATE_KEY=tu-private-key
FIREBASE_CLIENT_EMAIL=tu-client-email
FIREBASE_CLIENT_ID=tu-client-id
FIREBASE_x509=tu-certificado-url
```

## 🔒 Autenticación

El sistema utiliza Firebase Authentication:

1. El cliente obtiene un token ID de Firebase
2. El token se envía en el encabezado Authorization como `Bearer {token}`
3. El backend verifica el token con Firebase
4. Si es válido, se crea un contexto de seguridad para el usuario

## 📡 API Endpoints

### Usuarios
- `GET /user/check-new-user`: Verificar si un usuario existe
- `POST /user/register`: Registrar nuevo usuario
- `GET /user/preferences`: Obtener preferencias del usuario
- `PATCH /user/preferences`: Actualizar preferencias
- `GET /user/profile`: Obtener perfil del usuario
- `POST /user/upload-pfp`: Subir imagen de perfil

### Categorías
- `GET /categories`: Listar categorías del usuario
- `POST /categories`: Crear nueva categoría

### Gastos
- `GET /bills`: Obtener gastos del usuario
- `POST /bills`: Registrar nuevo gasto

### Metas financieras
- `GET /goals`: Listar metas del usuario
- `POST /goals`: Crear nueva meta
- `PUT /goals/{id}`: Actualizar meta existente
- `DELETE /goals/{id}`: Eliminar meta
- `POST /goals/{id}/image`: Subir imagen para meta
- `GET /goals/{id}/image`: Obtener imagen de meta

## 🧪 Desarrollo

### Estructura del proyecto

```
src/
├── main/
│   ├── java/dev/tymurkulivar/robank_backend/
│   │   ├── config/         # Configuración de seguridad y Firebase
│   │   ├── controllers/    # Controladores REST
│   │   ├── dto/            # Objetos de transferencia de datos
│   │   ├── entities/       # Entidades JPA
│   │   ├── repositories/   # Repositorios de datos
│   │   └── services/       # Lógica de negocio
│   └── resources/          # Archivos de configuración
└── test/                   # Pruebas unitarias e integración
```

## 👨‍💻 Autor

Tymur Kulivar Shymanskyi

---

*Desarrollado como Trabajo de Fin de Grado para el ciclo de Desarrollo de Aplicaciones Multiplataforma.*