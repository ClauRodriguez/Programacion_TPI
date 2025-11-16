### 📍 **Universidad Tecnológica Nacional**  
### *TECNICATURA UNIVERSITARIA EN PROGRAMACIÓN A DISTANCIA*

## 💻 Programación II 
#### **Año:** 2025
.
## ✨ Docentes  
#### 👨‍🏫 Coordinador: Carlos Martinez
#### 👩‍🏫 Profesores: Ariel Enferrel | Cinthia Rigoni | Alberto Cortez
.
## 👥 Estudiantes  
#### Hernan Cóceres | Claudio Rodriguez | Hernan E. Bula | Gaston A. Cejas 
---
## Trabajo Final Integrador
---
# 📦 Sistema de Gestión de Productos con Códigos de Barras

## 🏪 Descripción

El sistema implementa un **sistema de gestión de inventario** para depósitos o supermercados, modelando la relación **1→1 unidireccional** entre **Producto (A)** y **CódigoBarras (B)**. 

**Características principales:**
- **Relación 1→1 unidireccional**: Solo Producto referencia a CódigoBarras
- **Gestión completa CRUD** para ambas entidades
- **Transacciones atómicas** con commit/rollback
- **Eliminación lógica** (soft delete)
- **Arquitectura en capas** (DAO + Service)
- **Validaciones de negocio** robustas

---

## 🗂️ Diagrama UML

![Diagrama UML del Sistema de Gestión de Productos](https://raw.githubusercontent.com/hernanbula/Programacion_TPI/main/UML/TPI_P2.png)

**Elementos principales del diagrama:**
- **Relación 1→1 unidireccional**: Producto → CodigoBarras
- **Arquitectura en 5 capas**: config, model, dao, service, main

---

## 🎥 Video Explicativo

📹 **Enlace al video de demostración:**  
[INSERTAR_ENLACE_AL_VIDEO_AQUÍ]

**Contenido del video (15 minutos):**
- ✅ Presentación de los 4 integrantes
- ✅ Demostración del flujo CRUD completo
- ✅ Explicación de la relación 1→1 funcionando
- ✅ Análisis de código por capas (models, dao, service, main)
- ✅ Demostración de transacción con rollback ante error
- ✅ Evidencia de la integridad referencial y validaciones

---

## 🎯 Flujo de Uso - Menú Principal
#### El menú debe visualizarse de la siguiente manera (con UTF-8):
```
┌───────────────────────────────────┐
│  ☰  MENÚ PRINCIPAL                │
├───────────────────────────────────┤
│  ✅ GESTIÓN DE PRODUCTOS 📦       │
│   1. ↪ Crear producto             │
│   2. ↪ Listar productos           │
│   3. ↪ Actualizar producto        │
│   4. ↪ Eliminar producto          │
│   5. ↪ Asignar código barras      │
│                                   │
│  ✅ GESTIÓN DE CÓDIGOS 𝄃𝄃𝄂𝄂𝄀𝄁𝄃𝄂𝄂𝄃        │
│   6. ↪ Crear código de barras     │
│   7. ↪ Listar códigos de barras   │
│   8. ↪ Actualizar código          │
│   9. ↪ Eliminar código            │
│                                   │
│   0. ↩ Salir                      │
└───────────────────────────────────┘
```

### Funcionalidades CRUD Completas:

#### Gestión de Productos (Opciones 1-5)
| Operación | Descripción | Validaciones |
|-----------|-------------|--------------|
| **1. Crear** | Producto con/sin código de barras | Nombre ≠ vacío, Precio ≥ 0, Stock ≥ 0 |
| **2. Listar** | Todos, por ID, nombre o categoría | Filtros con manejo de errores |
| **3. Actualizar** | Campos individuales | Validaciones por campo |
| **4. Eliminar** | Soft delete | Confirmación requerida |
| **5. Asignar código** | Asignar código de barras a producto existente | Producto y código deben existir, relación 1→1 preservada |

#### Gestión de Códigos de Barras (Opciones 6-9)
| Operación | Descripción | Validaciones |
|-----------|-------------|--------------|
| **6. Crear** | Código independiente | Valor único, Tipo válido |
| **7. Listar** | Todos los códigos activos | - |
| **8. Actualizar** | Valor, tipo, observaciones | Mantener unicidad del valor |
| **9. Eliminar** | Soft delete | Confirmación requerida |

#### Funcionalidad de Relación 1→1
**Opción 5: Asignar código de barras a producto existente**
- Permite vincular un código de barras existente a un producto
- Valida que ambos existan y no estén eliminados
- Preserva la relación 1→1 (un producto solo puede tener un código)
- Operación transaccional con commit/rollback

---

## ⚙️ Requisitos del Sistema

### Software Utilizado
- **Java JDK 21** o superior
- **MySQL 8.0+** o **MariaDB 10.4+**
- **MySQL Connector/J** 8.0+
- **IDE**: NetBeans

### Configuración de Base de Datos

#### 1. Configuración Automática
El sistema **crea automáticamente** la base de datos y tablas al ejecutarse por primera vez mediante `DatabaseConnection.inicializarBaseDatos()`.

#### 2. Script de Creación Manual (`database-schema.sql`)

```sql
CREATE DATABASE IF NOT EXISTS depositotpi;
USE depositotpi;

-- Tabla de códigos de barras (Entidad B)
CREATE TABLE codigo_barras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(10) NOT NULL COMMENT 'EAN13, EAN8, UPC',
    valor VARCHAR(20) NOT NULL UNIQUE,
    fecha_asignacion DATE,
    observaciones VARCHAR(255),
    eliminado BOOLEAN DEFAULT FALSE
);

-- Tabla de productos (Entidad A) con FK única para relación 1→1
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    marca VARCHAR(80),
    categoria VARCHAR(80),
    precio DECIMAL(10,2) NOT NULL,
    peso DECIMAL(10,3),
    stock INT DEFAULT 0,
    eliminado BOOLEAN DEFAULT FALSE,
    codigo_barras_id INT UNIQUE,
    FOREIGN KEY (codigo_barras_id) REFERENCES codigo_barras(id) ON DELETE SET NULL
);
```

#### 3. Configuración de Conexión

En `config/DatabaseConnection.java`:

```java
public class DatabaseConnection {
    private static final String DB_NAME = "depositotpi";
    private static final String HOST = "localhost";
    private static final String PORT = "3307";  // Puerto 3307
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Conexión automática con inicialización de BD
    public static void inicializarBaseDatos() throws SQLException {
        // Crea BD y tablas si no existen
    }
}
```

---

## 🚀 Compilación y Ejecución

### Pasos para Ejecutar:

1. **Clonar el repositorio** y abrir en el IDE
2. **Configurar MySQL**
3. **Agregar dependencias**:
   - MySQL Connector/J al classpath
4. **Compilar el proyecto**
5. **Ejecutar la clase `Main`**

### Credenciales de Prueba:
- **Host:** localhost
- **Puerto:** 3306
- **Usuario:** root
- **Contraseña:** [vacía]
- **Base de datos:** depositotpi (se crea automáticamente)

---

## 🏗️ Arquitectura del Sistema

### Estructura de Paquetes Actualizada:

```
src/
├── config/           # DatabaseConnection
├── model/            # Base, Producto, CodigoBarras, CategoriaProducto, EnumTipo
├── dao/              # GenericDAO, ProductoDAO, CodigoBarrasDAO
├── service/          # GenericService, ProductoService, CodigoBarrasService
└── main/             # Main, AppMenu, MenuDisplay, MenuHandler, MenuStyle 
```

### Características Técnicas Implementadas:

#### **Gestión de Transacciones**
```java
// En ProductoService y CodigoBarrasService
conn.setAutoCommit(false);
try {
    // Operaciones transaccionales
    productoDAO.insertar(entidad, conn);
    conn.commit();
} catch (Exception e) {
    conn.rollback();
    throw e;
}
```

#### **Validaciones de Negocio**
- **Producto**: nombre obligatorio, marca, categoria, precio ≥ 0, stock ≥ 0
- **CódigoBarras**: valor único, tipo válido, fecha obligatoria
- **Validaciones en capa Service** con mensajes descriptivos

#### **DAOs con Conexión Externa**
```java
// Los DAOs aceptan conexión externa para transacciones
public void insertar(Producto entidad, Connection conn) throws Exception
```

---

## 🔗 Relación 1→1 Unidireccional

### Implementación en Java:
```java
// En la clase Producto (Entidad A)
public class Producto extends Base {
    private CodigoBarras codigoBarras; // Referencia unidireccional 1→1
    
    // Producto conoce a CodigoBarras, pero no al revés
    public CodigoBarras getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(CodigoBarras codigoBarras) { 
        this.codigoBarras = codigoBarras; 
    }
}
```

### Implementación en Base de Datos:
```sql
-- Clave foránea única que garantiza la relación 1→1
ALTER TABLE producto ADD CONSTRAINT uk_producto_codigo 
UNIQUE (codigo_barras_id);

-- Integridad referencial con SET NULL
ALTER TABLE producto ADD CONSTRAINT fk_producto_codigo
FOREIGN KEY (codigo_barras_id) REFERENCES codigo_barras(id)
ON DELETE SET NULL;
```

### Transacción para Crear Producto con Código:
```java
public void insertarConCodigoBarras(Producto producto, CodigoBarras codigo) throws Exception {
    conn.setAutoCommit(false);
    try {
        // 1. Insertar código de barras
        codigoBarrasDAO.insertar(codigo, conn);
        
        // 2. Asociar y insertar producto
        producto.setCodigoBarras(codigo);
        productoDAO.insertar(producto, conn);
        
        conn.commit(); // Confirmar ambas operaciones
    } catch (Exception e) {
        conn.rollback(); // Revertir ambas operaciones
        throw e;
    }
}
```

---

## ✅ Checklist de Cumplimiento TFI

| Requisito | Estado | Observaciones |
|-----------|--------|---------------|
| **Java 21** | ✅ | Proyecto configurado con JDK 21 |
| **Estructura de paquetes** | ✅ | config, model, dao, service, main |
| **Relación 1→1 unidireccional** | ✅ | Producto → CodigoBarras implementada |
| **Patrón DAO** | ✅ | GenericDAO + implementaciones concretas |
| **DAOs con conexión externa** | ✅ | Métodos aceptan Connection para transacciones |
| **Capa Service con transacciones** | ✅ | Commit/rollback en todos los servicios |
| **CRUD completo** | ✅ | 9 operaciones implementadas |
| **Eliminación lógica** | ✅ | Campo `eliminado` en clase Base (soft delete) |
| **Validaciones de negocio** | ✅ | En capa Service con mensajes descriptivos |
| **Manejo de excepciones** | ✅ | Try-catch en todas las capas |
| **PreparedStatement** | ✅ | En todos los DAOs |
| **Inicialización automática BD** | ✅ | DatabaseConnection.inicializarBaseDatos() |
| **Scripts SQL** | ✅ | Incluidos en el proyecto |
| **Diagrama UML** | ✅ | Incluido en documentación |
| **Video explicativo** | ✅ | Los 4 integrantes explican el trabajo |


---

## 🛡️ Características de Seguridad y Validación

### Validaciones Implementadas:

#### **ProductoService**
- Nombre: obligatorio, máximo 120 caracteres
- Marca: máximo 80 caracteres (opcional)
- Precio: ≥ 0, formato decimal (10,2)
- Peso: ≥ 0, formato decimal (10,3) (opcional)
- Stock: ≥ 0
- Categoría: Elije una de Enum CategoriaProducto

#### **CodigoBarrasService**
- Tipo: EAN13, EAN8, UPC (obligatorio)
- Valor: único, máximo 20 caracteres (obligatorio)
- Fecha asignación: obligatoria (al asociarse a producto)
- Observaciones: máximo 255 caracteres (opcional)

### Manejo de Transacciones:
- **Transacciones atómicas** para operaciones múltiples
- **Rollback automático** en caso de error
- **Conexiones manejadas** con try-with-resources
- **Auto-commit controlado** manualmente

---

## 📊 Entregables Completados

| Entregable | Estado | Detalles |
|------------|--------|----------|
| **Código fuente completo** | ✅ | Repositorio GitHub público |
| **Estructura de paquetes** | ✅ | config, model, dao, service, main |
| **Relación 1→1 unidireccional** | ✅ | Producto → CodigoBarras |
| **Transacciones con commit/rollback** | ✅ | En ProductoService y CodigoBarrasService |
| **DAOs con conexión externa** | ✅ | Para participación en transacciones |
| **Validaciones de negocio** | ✅ | En capa Service |
| **CRUD completo** | ✅ | 9 operaciones implementadas |
| **Scripts SQL** | ✅ | Incluidos y probados |
| **Documentación README** | ✅ | Completa y detallada |
| **Diagrama UML** | ✅ | Incluido en documentación |
| **Video Explicativo** | ✅ | Integrantes del grupo explicando el trabajo |

---

## 🔧 Troubleshooting

### Problemas Comunes:

1. **Error de conexión a BD:**
   ```bash
   # Verificar que MySQL esté en puerto indicado (suele ser 3306)
   netstat -an | grep 3306
   ```

2. **Driver no encontrado:**
   - Descargar MySQL Connector/J desde dev.mysql.com
   - Agregar JAR al classpath del proyecto

3. **Base de datos no se crea:**
   - Verificar credenciales en DatabaseConnection.java
   - Verificar permisos de usuario root

4. **Error de valor único en código de barras:**
   - El sistema valida automáticamente duplicados
   - Usar valores diferentes para cada código

---

### *Proyecto desarrollado para el Trabajo Final Integrador de Programación II - TUPaD - UTN*
