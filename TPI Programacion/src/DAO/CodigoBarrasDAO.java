package DAO;

import config.DatabaseConnection;
import model.CodigoBarras;
import model.EnumTipo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CodigoBarrasDAO implements GenericDAO<CodigoBarras> {

    @Override
    public void insertar(CodigoBarras entidad) throws Exception {
        String sql = "INSERT INTO codigo_barras (tipo, valor, fecha_asignacion) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, entidad.getTipo().name()); // <-- Enum a String
            stmt.setString(2, entidad.getValor());
            stmt.setDate(3, Date.valueOf(entidad.getFechaAsignacion())); // <-- LocalDate a sql.Date

            stmt.executeUpdate();

            // opcional: recuperar id generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) entidad.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public void actualizar(CodigoBarras entidad) throws Exception {
        String sql = "UPDATE codigo_barras SET tipo = ?, valor = ?, fecha_asignacion = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entidad.getTipo().name());
            stmt.setString(2, entidad.getValor());
            stmt.setDate(3, Date.valueOf(entidad.getFechaAsignacion()));
            stmt.setInt(4, entidad.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM codigo_barras WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public CodigoBarras getById(int id) throws Exception {
        String sql = "SELECT * FROM codigo_barras WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<CodigoBarras> getAll() throws Exception {
        List<CodigoBarras> lista = new ArrayList<>();
        String sql = "SELECT * FROM codigo_barras";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public CodigoBarras getByValor(String valor) throws Exception {
        String sql = "SELECT * FROM codigo_barras WHERE valor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

private CodigoBarras mapRow(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");

    String tipoStr = rs.getString("tipo");
    EnumTipo tipo = (tipoStr != null) 
            ? EnumTipo.valueOf(tipoStr.trim().toUpperCase())
            : null; // o lanzá una excepción si es obligatorio

    String valor = rs.getString("valor");

    java.sql.Date sqlDate = rs.getDate("fecha_asignacion");
    LocalDate fecha = (sqlDate != null) ? sqlDate.toLocalDate() : null;

    String observaciones = rs.getString("observaciones");

    // Constructor: (int id, boolean eliminado, EnumTipo tipo, String valor, LocalDate fechaAsignacion, String observaciones)
    return new CodigoBarras(id, false, tipo, valor, fecha, observaciones);
}

    }

//int id, boolean eliminado, EnumTipo tipo, String valor, LocalDate fechaAsignacion, String observaciones)
