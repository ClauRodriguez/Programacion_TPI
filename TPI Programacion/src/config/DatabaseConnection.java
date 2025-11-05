package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Configuración para MySQL (compatible con MariaDB también)
    // Para MySQL: "jdbc:mysql://localhost:3306/depositotpi"
    // Para MariaDB: "jdbc:mariadb://localhost:3306/depositotpi"
    private static final String URL = "jdbc:mysql://localhost:3307/depositotpi";
    private static final String USER = "root";
    private static final String PASSWORD = "Claudev1!";

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

    public static Connection getConnection() throws SQLException {
        if (URL == null || URL.isEmpty() || USER == null || USER.isEmpty()) {
    throw new SQLException("Configuración de la base de datos incompleta o inválida.");
}
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
