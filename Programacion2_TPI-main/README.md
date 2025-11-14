Trabajo Practico Integrador de Base de Datos y Programación 2: Relación unidireccional UML Producto → Código de Barras
Este proyecto es una aplicación de consola desarrollada en Java que implementa un sistema de gestión de productos y códigos de barras utilizando una arquitectura multicapa, patrón DAO y relación 1→1 unidireccional obligatoria.

A) Grupo 117: Relación Producto - Código de Barras

B) Autores
1) Hernan Cóceres

2) Gaston Alberto Cejas

3) Hernan Bula

4) Claudio Rodriguez

C) Tecnologías Utilizadas
1) Java 21 (versión recomendada)

2) JDBC para conexión a base de datos

3) MySQL/MariaDB como sistema gestor de base de datos

4) XAMPP para servidor web y base de datos

5) Apache NetBeans como IDE de desarrollo

6) DBeaver para gestión y consultas de base de datos (opcional)

D) Arquitectura de 5 capas (Config, Models, DAO, Service, Main)

E) Configuración del Entorno - Paso a Paso
1. Instalación de Software Requerido
a) Java Development Kit (JDK) 21

Descargar e instalar JDK 21 desde Oracle o OpenJDK

Configurar variable de entorno JAVA_HOME

Verificar instalación: java -version

b) XAMPP
Descargar e instalar XAMPP desde Apache Friends

Iniciar el Panel de Control de XAMPP

Activar los servicios:

Apache (servidor web)

MySQL/MariaDB (base de datos)

Verificar que MySQL esté corriendo en puerto 3306 (preferentemente)

c) Apache NetBeans
Descargar e instalar Apache NetBeans desde netbeans.apache.org

Asegurarse de que NetBeans detecte el JDK 21

d) DBeaver (Opcional pero Recomendado)
Descargar e instalar DBeaver desde dbeaver.io

Útil para visualizar y gestionar la base de datos

2. Configuración de la Base de Datos
Conectar MySQL con DBeaver:
Abrir DBeaver

Click en "Nueva Conexión"

Seleccionar "MySQL"

Configurar parámetros:

Host: localhost

Puerto: 3306

Base de datos: depositotpi (se creará automáticamente)

Usuario: root

Contraseña: "" (vacía por defecto en XAMPP)

Testear conexión

Creación Automática de la Base de Datos:
La aplicación crea automáticamente la base de datos y tablas al ejecutarse por primera vez mediante la clase DatabaseConnection.

3. Configuración del Proyecto en NetBeans
Importar el Proyecto:
Abrir NetBeans

File → Open Project

Seleccionar la carpeta del proyecto

Asegurarse de que el JDK 21 esté configurado

Configurar Dependencias:
El proyecto requiere el conector MySQL/MariaDB

Descargar: MySQL Connector/J

Agregar el JAR al classpath del proyecto:

Click derecho en el proyecto → Properties

Libraries → Add JAR/Folder

Seleccionar mysql-connector-java-8.0.x.jar

Notas Importantes
Para la Ejecución Correcta:
XAMPP debe estar activo antes de ejecutar la aplicación

No modificar manualmente la base de datos mientras la aplicación corre

Primera ejecución puede ser más lenta (creación de BD)

Los IDs son autoincrementales, asignados por la base de datos

Soporte y Troubleshooting
Problemas Comunes:
Error de conexión a BD:

Verificar que XAMPP esté ejecutándose

Confirmar que MySQL esté en puerto 3306

Driver no encontrado:

Verificar que el connector JAR esté en el classpath

Permisos de base de datos:

Usuario root sin contraseña por defecto

4. Estructura de la Base de Datos
La aplicación crea automáticamente las siguientes tablas:

Tabla producto
sql
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
Tabla codigo_barras
sql
CREATE TABLE codigo_barras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(10) NOT NULL COMMENT 'EAN13, EAN8, UPC',
    valor VARCHAR(20) NOT NULL UNIQUE,
    fecha_asignacion DATE,
    observaciones VARCHAR(255),
    eliminado BOOLEAN DEFAULT FALSE
);
E) Arquitectura del Proyecto
Estructura de Capas (5 capas requeridas):
text
src/

├── config/

│   └── DatabaseConnection.java      # Gestión de conexión BD

├── models/

│   ├── Base.java                    # Clase abstracta con id y eliminado

│   ├── Producto.java               # Entidad A (Producto)

│   ├── CodigoBarras.java           # Entidad B (Código de Barras)

│   ├── CategoriaProducto.java      # Enum de categorías

│   └── EnumTipo.java               # Enum tipos de código

├── dao/

│   ├── GenericDAO.java             # Interfaz genérica CRUD

│   ├── ProductoDAO.java            # DAO específico Producto

│   └── CodigoBarrasDAO.java        # DAO específico Código Barras

├── service/

│   ├── GenericService.java          # Interfaz genérica servicios

│   ├── ProductoService.java         # Lógica negocio Producto

│   └── CodigoBarrasService.java     # Lógica negocio Código Barras

└── main/

    ├── AppMenu.java                 # Orquestador principal
    
    ├── MenuHandler.java            # Controlador de operaciones
    
    ├── MenuDisplay.java            # Vista del menú
    
    └── main.java                   # Punto de entrada

F) Ejecución del Proyecto
Primera Ejecución:
Asegurarse de que XAMPP esté ejecutándose

En NetBeans: Click derecho en el proyecto → Run

La aplicación:

Verificará la conexión a la base de datos

Creará automáticamente la BD depositotpi si no existe

Creará las tablas producto y codigo_barras

Iniciará el menú principal

Flujo Normal:
El sistema mostrará el menú principal con opciones CRUD

Todas las operaciones son transaccionales

La relación Producto → CódigoBarras es obligatoria

G) Funcionalidades Implementadas
a) CRUD Completo (Create, Read, Update, Delete)
b) Crear Producto: Con código de barras obligatorio
c) Listar Productos: Todos, por ID, por nombre, por categoría
d) Actualizar Producto: Datos del producto y su código de barras
e) Eliminar Producto: Borrado lógico (soft delete)

H) Características Técnicas Cumplidas
a) Relación 1→1 unidireccional obligatoria entre Producto y CódigoBarras
b) Arquitectura de 5 capas bien definidas
c) Patrón DAO con interfaz genérica
d) Transacciones atómicas para operaciones compuestas
e) Borrado lógico en todas las entidades
f) Validaciones exhaustivas en capa de servicio
g) Manejo robusto de excepciones
h) Uso de PreparedStatement para prevenir SQL injection

I) Validaciones Implementadas
a) Campos obligatorios y longitudes máximas
b) Formatos de precio y peso (números positivos)
c) Unicidad de códigos de barras
d) Integridad referencial
e) Validación de categorías y tipos enumerados

J) Cumplimiento de Requisitos del TPI
Requisito	Cumplimiento
Arquitectura 5 capas	-----> Config, Models, DAO, Service, Main
Relación 1→1 unidireccional	-----> Producto → CódigoBarras (obligatoria)
Patrón DAO con GenericDAO	-----> Interfaces e implementaciones
Transacciones commit/rollback	-----> Operaciones atómicas
Borrado lógico	-----> Campo eliminado en todas las entidades
Validaciones exhaustivas	-----> Capa Service con reglas de negocio
CRUD completo	-----> Todas las operaciones para A y B
Búsquedas por campo relevante	-----> 4 tipos de búsqueda
Manejo de excepciones	-----> Try-catch y mensajes claros
PreparedStatement	-----> En todas las operaciones DAO

Características de Seguridad:
a) Uso de PreparedStatement previene inyección SQL
b) Validaciones en frontend y backend
c) Manejo seguro de conexiones con try-with-resources
d) Transacciones aseguran consistencia de datos

K) Estructura de la Relación 1→1
En Código Java:
En la clase Producto -----> private CodigoBarras codigoBarras;  // Relación unidireccional

En la base de datos
producto.codigo_barras_id → codigo_barras.id -----> Características de la Relación: Unidireccional: Solo Producto conoce a CódigoBarras

a) Obligatoria: Todo producto debe tener un código de barras

b) Transaccional: Inserción atómica de ambas entidades

c) Integridad: Clave foránea con restricciones

L) Logs y Debugging:
Los mensajes de error se muestran en consola

Operaciones exitosas se confirman con ✓

Validaciones fallidas muestran mensajes descriptivos
