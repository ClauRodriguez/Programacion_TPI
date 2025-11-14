📦 Proyecto TPI – Programación II & Base de Datos
Relación UML 1→1 Unidireccional: Producto → Código de Barras










Este proyecto es una aplicación de consola desarrollada en Java, con arquitectura multicapa, patrón DAO, transacciones atómicas y una relación 1→1 unidireccional obligatoria entre Producto y Código de Barras.
Incluye creación automática de la base de datos, validaciones de negocio y eliminación lógica.

📚 Tabla de Contenidos

📦 Proyecto TPI

👥 Autores

🧰 Tecnologías Utilizadas

🧱 Arquitectura del Proyecto

⚙️ Configuración del Entorno

🗄️ Base de Datos

🏗️ Estructura de Carpetas

▶️ Ejecución del Proyecto

🚀 Funcionalidades

📘 Características Técnicas

✔️ Validaciones

📋 Checklist de Cumplimiento TPI

🔐 Seguridad

🔗 Relación UML 1→1

🪵 Logs y Debugging

👥 Autores

Grupo 117 – Proyecto Producto → Código de Barras

| Nombre               | Rol        |
| -------------------- | ---------- |
| Hernan Cóceres       | Desarrollo |
| Gaston Alberto Cejas | Desarrollo |
| Hernan Bula          | Desarrollo |
| Claudio Rodriguez    | Desarrollo |


🧰 Tecnologías Utilizadas

Java 21

JDBC

MySQL/MariaDB

XAMPP

NetBeans IDE

DBeaver (opcional)

🧱 Arquitectura del Proyecto

Este proyecto está estructurado bajo una arquitectura profesional de 5 capas, siguiendo el patrón DAO + Services:

Config  
Models  
DAO  
Service  
Main (UI)


⚙️ Configuración del Entorno
1. Instalación de Software
🔸 Java JDK 21
java -version

🔸 XAMPP

Activar:

Apache

MySQL (puerto 3306)

🔸 NetBeans

Asegurarse de que detecte JDK 21.

🔸 DBeaver (opcional)

Para visualizar la BD.

2. Configuración de la Base de Datos
✔️ Conexión en DBeaver

Host: localhost

Puerto: 3306

Usuario: root

Contraseña: (vacía)

✔️ Creación Automática

La app crea:

depositotpi

producto

codigo_barras

al ejecutarse por primera vez.

3. Dependencias en NetBeans

Agregar MySQL Connector/J:

Properties → Libraries → Add JAR/Folder

🗄️ Base de Datos
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    marca VARCHAR(80),
    categoria VARCHAR(80),
    precio DECIMAL(10,2) NOT NULL,
    peso DECIMAL(10,3),
    stock INT DEFAULT 0,
    eliminado BOOLEAN DEFAULT FALSE,
    codigo_barras_id INT
);


Tabla: codigo_barras
CREATE TABLE codigo_barras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(10) NOT NULL COMMENT 'EAN13, EAN8, UPC',
    valor VARCHAR(20) NOT NULL UNIQUE,
    fecha_asignacion DATE,
    observaciones VARCHAR(255),
    eliminado BOOLEAN DEFAULT FALSE
);


🏗️ Estructura de Carpetas
src/

├── config/

│   └── DatabaseConnection.java

│
├── models/

│   ├── Base.java

│   ├── Producto.java

│   ├── CodigoBarras.java

│   ├── CategoriaProducto.java

│   └── EnumTipo.java

│
├── dao/

│   ├── GenericDAO.java

│   ├── ProductoDAO.java

│   └── CodigoBarrasDAO.java

│
├── service/

│   ├── GenericService.java

│   ├── ProductoService.java

│   └── CodigoBarrasService.java

│
└── main/

    ├── AppMenu.java
    
    ├── MenuHandler.java
    
    ├── MenuDisplay.java
    
    └── main.java

▶️ Ejecución del Proyecto
Primera ejecución

✔ Encender XAMPP
✔ Ejecutar en NetBeans
✔ Creación automática de BD y tablas
✔ Inicio del menú principal

🚀 Funcionalidades
🟢 CRUD Completo

Crear producto (con código obligatorio)

Editar producto y su código de barras

Listar por:

ID

Nombre

Categoría

Eliminación lógica

🟢 Transacciones

Commit/Rollback asegurado

🟢 Validaciones de negocio

Longitudes

Formatos

Unicidad

Integridad

📘 Características Técnicas

Relación 1→1 unidireccional obligatoria

Arquitectura 5 capas

Patrón DAO

Transacciones atómicas

Borrado lógico

Manejo de excepciones

PreparedStatement en todas las consultas

Base de datos autogenerada

✔️ Validaciones

Campos obligatorios

Precio y peso positivos

Código de barras único

Categorías y tipos validados por enums

Integridad referencial mantenida

📋 Checklist de Cumplimiento TPI
| Requisito                   | Estado |
| --------------------------- | ------ |
| 5 capas                     | ✔️     |
| Relación 1→1 unidireccional | ✔️     |
| DAO genérico                | ✔️     |
| CRUD completo               | ✔️     |
| Búsquedas por campos        | ✔️     |
| Eliminación lógica          | ✔️     |
| Validaciones                | ✔️     |
| Transacciones               | ✔️     |
| Manejo excepciones          | ✔️     |
| PreparedStatement           | ✔️     |

🔐 Seguridad

Prevención de SQL Injection

Validaciones en frontend y backend

try-with-resources para conexiones

Transacciones para integridad

🔗 Relación UML 1→1
En Java
private CodigoBarras codigoBarras; // Relación unidireccional

En la BD
producto.codigo_barras_id → codigo_barras.id


📌 El producto conoce su código, pero no al revés.

🪵 Logs y Debugging

Errores descriptivos

Mensajes ✓ para operaciones exitosas

Validaciones claras

Seguimiento por consola
