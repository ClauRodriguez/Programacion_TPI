package config;
/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Configuración para MySQL 
    private static final String DB_NAME = "depositotpi";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String USER = "root";
    private static final String PASSWORD = "Laia1002";
    
    private static final String JDBC_PROTOCOL;
    private static final String URL_WITH_DB;
    private static final String URL_WITHOUT_DB;

    static {
        String protocol = "mysql"; // Por defecto MySQL
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            protocol = "mysql";
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                protocol = "mariadb";
            } catch (ClassNotFoundException e2) {
                throw new RuntimeException(
                    "Error: No se encontró el driver JDBC. " +
                    "Asegúrate de tener el driver MySQL Connector/J o MariaDB Connector/J en el classpath."
                );
            }
        }
        
        JDBC_PROTOCOL = protocol;
        URL_WITH_DB = "jdbc:" + JDBC_PROTOCOL + "://" + HOST + ":" + PORT + "/" + DB_NAME;
        URL_WITHOUT_DB = "jdbc:" + JDBC_PROTOCOL + "://" + HOST + ":" + PORT;
    }

    /**
     * Inicializa la base de datos si no existe.
     * Debe llamarse al inicio de la aplicación antes de usar getConnection().
     */
    public static void inicializarBaseDatos() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL_WITH_DB, USER, PASSWORD)) {
            return;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1049 || e.getMessage().contains("Unknown database")) {
                System.out.println("⚠ Base de datos '" + DB_NAME + "' no encontrada. Intentando crearla...");
                crearBaseDatosSiNoExiste();
            } else {
                throw e;
            }
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (URL_WITH_DB == null || URL_WITH_DB.isEmpty() || USER == null || USER.isEmpty()) {
            throw new SQLException("Configuración de la base de datos incompleta o inválida.");
        }
        
        return DriverManager.getConnection(URL_WITH_DB, USER, PASSWORD);
    }
    
    /**
     * Crea la base de datos y las tablas si no existen.
     * Se ejecuta automáticamente si la base de datos no existe.
     */
    private static void crearBaseDatosSiNoExiste() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL_WITHOUT_DB, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("✓ Base de datos '" + DB_NAME + "' creada exitosamente.");
            
            stmt.executeUpdate("USE " + DB_NAME);
            
            String sqlCodigoBarras = 
                "CREATE TABLE IF NOT EXISTS codigo_barras (" +
                "  id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "  tipo VARCHAR(10) NOT NULL COMMENT 'EAN13, EAN8, UPC', " +
                "  valor VARCHAR(20) NOT NULL UNIQUE, " +
                "  fecha_asignacion DATE, " +
                "  observaciones VARCHAR(255), " +
                "  eliminado BOOLEAN DEFAULT FALSE, " +
                "  INDEX idx_eliminado (eliminado), " +
                "  INDEX idx_valor (valor), " +
                "  INDEX idx_tipo (tipo)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
            stmt.executeUpdate(sqlCodigoBarras);
            
            String sqlProducto =
                "CREATE TABLE IF NOT EXISTS producto (" +
                "  id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "  nombre VARCHAR(120) NOT NULL, " +
                "  marca VARCHAR(80), " +
                "  categoria VARCHAR(80), " +
                "  precio DECIMAL(10,2) NOT NULL, " +
                "  peso DECIMAL(10,3), " +
                "  stock INT DEFAULT 0, " +
                "  eliminado BOOLEAN DEFAULT FALSE, " +
                "  codigo_barras_id BIGINT UNIQUE, " +
                "  INDEX idx_eliminado (eliminado), " +
                "  INDEX idx_categoria (categoria), " +
                "  INDEX idx_nombre (nombre), " +
                "  INDEX idx_marca (marca), " +
                "  CONSTRAINT fk_producto_codigo " +
                "    FOREIGN KEY (codigo_barras_id) " +
                "    REFERENCES codigo_barras(id) " +
                "    ON DELETE SET NULL " +
                "    ON UPDATE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
            stmt.executeUpdate(sqlProducto);
            
            System.out.println("✓ Base de datos y tablas inicializadas correctamente.\n");
            
            insertarDatosPrueba(conn);
            
        } catch (SQLException e) {
            throw new SQLException("Error al crear la base de datos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Inserta algunos productos de prueba con códigos de barras en la base de datos.
     */
    private static void insertarDatosPrueba(Connection conn) throws SQLException {
        System.out.println("✓ Insertando productos de prueba...");

        try (Statement stmt = conn.createStatement()) {

            String insertCodigos
                    = "INSERT IGNORE INTO codigo_barras (id, tipo, valor, fecha_asignacion, observaciones) VALUES "
                    + "(1, 'EAN13', '7791234567890', '2025-10-01', 'Lote L123 - Leche entera premium'), "
                    + "(2, 'EAN8', '7791234567891', '2025-10-02', 'Pan integral sin conservantes'), "
                    + "(3, 'UPC', '7791234567892', '2025-10-03', 'Arroz largo fino calidad exportación') ";


            stmt.executeUpdate(insertCodigos);

            String insertProductos
                    = "INSERT IGNORE INTO producto (id, nombre, marca, categoria, precio, peso, stock, codigo_barras_id) VALUES "
                    + "(1, 'Leche Entera', 'La Serenísima', 'ALIMENTOS', 1937.50, 1.000, 45, 1), "
                    + "(2, 'Pan de Molde', 'Bimbo', 'ALIMENTOS', 1519.75, 0.500, 32, 2), "
                    + "(3, 'Arroz Largo Fino', 'Gallo', 'ALIMENTOS', 2867.50, 1.000, 67, 3) ";


            int productosInsertados = stmt.executeUpdate(insertProductos);
            System.out.println("✓ " + productosInsertados + " productos de prueba insertados correctamente.");

        } catch (SQLException e) {
            System.err.println("⚠ Advertencia: No se pudieron insertar los datos de prueba: " + e.getMessage());
        }
    }
}
