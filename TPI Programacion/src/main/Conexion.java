package main;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexion {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Conexion establecida con exito");

                String sql = "SELECT * FROM producto";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                System.out.println("Listado de productos:");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");
                    System.out.println("ID: " + id + ", Nombre: " + nombre + ", Precio: " + precio);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar o consultar la base de datos:");
            e.printStackTrace();
        }
    }
}
