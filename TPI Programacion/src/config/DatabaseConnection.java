package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Configuración para MySQL (compatible con MariaDB también)
    // Para MySQL: "jdbc:mysql://localhost:3306/depositotpi"
    // Para MariaDB: "jdbc:mariadb://localhost:3306/depositotpi"
    private static final String DB_NAME = "depositotpi";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String URL_WITH_DB = "jdbc:mariadb://" + HOST + ":" + PORT + "/" + DB_NAME;
    private static final String URL_WITHOUT_DB = "jdbc:mysql://" + HOST + ":" + PORT;
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            // Intentar cargar driver de MySQL primero (más común)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                // Si no está MySQL, intentar con MariaDB
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e2) {
                throw new RuntimeException(
                    "Error: No se encontró el driver JDBC. " +
                    "Asegúrate de tener el driver MySQL Connector/J o MariaDB Connector/J en el classpath."
                );
            }
        }
    }

    /**
     * Inicializa la base de datos si no existe.
     * Debe llamarse al inicio de la aplicación antes de usar getConnection().
     */
    public static void inicializarBaseDatos() throws SQLException {
        try {
            // Intentar conectar para verificar si existe
            try (Connection conn = DriverManager.getConnection(URL_WITH_DB, USER, PASSWORD)) {
                // Si se conecta, la BD existe
                return;
            }
        } catch (SQLException e) {
            // Si la BD no existe (error 1049), crearla
            if (e.getErrorCode() == 1049 || e.getMessage().contains("Unknown database")) {
                System.out.println("⚠ Base de datos '" + DB_NAME + "' no encontrada. Intentando crearla...");
                crearBaseDatosSiNoExiste();
            } else {
                // Si es otro error, lanzarlo
                throw e;
            }
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (URL_WITH_DB == null || URL_WITH_DB.isEmpty() || USER == null || USER.isEmpty()) {
            throw new SQLException("Configuración de la base de datos incompleta o inválida.");
        }
        
        // Intentar conectar a la base de datos
        // Nota: La BD debe estar inicializada previamente con inicializarBaseDatos()
        return DriverManager.getConnection(URL_WITH_DB, USER, PASSWORD);
    }
    
    /**
     * Crea la base de datos y las tablas si no existen.
     * Se ejecuta automáticamente si la base de datos no existe.
     */
    private static void crearBaseDatosSiNoExiste() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL_WITHOUT_DB, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Crear base de datos si no existe
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("✓ Base de datos '" + DB_NAME + "' creada exitosamente.");
            
            // Usar la base de datos
            stmt.executeUpdate("USE " + DB_NAME);
            
            // Tabla codigo_barras
            String sqlCodigoBarras = "CREATE TABLE IF NOT EXISTS codigo_barras (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "tipo VARCHAR(10) NOT NULL COMMENT 'EAN13, EAN8, UPC', " +
                "valor VARCHAR(20) NOT NULL UNIQUE, " +
                "fecha_asignacion DATE, " +
                "observaciones VARCHAR(255), " +
                "eliminado BOOLEAN DEFAULT FALSE, " +
                "INDEX idx_eliminado (eliminado), " +
                "INDEX idx_valor (valor), " +
                "INDEX idx_tipo (tipo)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
            stmt.executeUpdate(sqlCodigoBarras);
            
            // Tabla producto
            String sqlProducto = "CREATE TABLE IF NOT EXISTS producto (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nombre VARCHAR(120) NOT NULL, " +
                "marca VARCHAR(80), " +
                "categoria VARCHAR(80), " +
                "precio DECIMAL(10,2) NOT NULL, " +
                "peso DECIMAL(10,3), " +
                "stock INT DEFAULT 0, " +
                "eliminado BOOLEAN DEFAULT FALSE, " +
                "codigo_barras_id INT, " +
                "INDEX idx_eliminado (eliminado), " +
                "INDEX idx_categoria (categoria), " +
                "INDEX idx_nombre (nombre), " +
                "INDEX idx_marca (marca), " +
                "CONSTRAINT fk_producto_codigo " +
                "FOREIGN KEY (codigo_barras_id) " +
                "REFERENCES codigo_barras(id) " +
                "ON DELETE SET NULL " +
                "ON UPDATE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
            stmt.executeUpdate(sqlProducto);
            
            System.out.println("✓ Base de datos y tablas inicializadas correctamente.\n");
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear la base de datos: " + e.getMessage(), e);
        }
    }
}
