package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/deposito";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: No se encontró el driver JDBC de MariaDB.");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (URL == null || URL.isEmpty() || USER == null || USER.isEmpty()) {
    throw new SQLException("Configuración de la base de datos incompleta o inválida.");
}
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
