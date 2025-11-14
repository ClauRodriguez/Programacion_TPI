package DAO;
/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */
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
        insertar(entidad, null);
    }
    
    /**
     * Inserta un código de barras usando una conexión existente (para transacciones).
     * Si conn es null, crea una nueva conexión.
     */
    public void insertar(CodigoBarras entidad, Connection conn) throws Exception {
        String sql = "INSERT INTO codigo_barras (tipo, valor, fecha_asignacion, observaciones) VALUES (?, ?, ?, ?)";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, entidad.getTipo().name()); // Enum a String
            stmt.setString(2, entidad.getValor());
            stmt.setDate(3, Date.valueOf(entidad.getFechaAsignacion())); // LocalDate a sql.Date
            
            // IMPORTANTE: Guardar observaciones SIEMPRE, incluso si es cadena vacía
            String obsValue = entidad.getObservaciones();
            
            // Si tiene valor, guardarlo (trim si es necesario pero mantener el valor)
            if (obsValue != null && !obsValue.trim().isEmpty()) {
                stmt.setString(4, obsValue.trim());
            } else {
                // Si es null o cadena vacía, guardar como NULL
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.executeUpdate();

            // Recuperar id generado automáticamente por la BD
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            
            // Solo hacer commit si manejamos la conexión
            if (!usarConexionExterna) {
                conn.commit();
            }
        } finally {
            // Solo cerrar si manejamos la conexión
            if (!usarConexionExterna && conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void actualizar(CodigoBarras entidad) throws Exception {
        actualizar(entidad, null);
    }
    
    /**
     * Actualiza un código de barras usando una conexión existente (para transacciones).
     * Si conn es null, crea una nueva conexión.
     */
    public void actualizar(CodigoBarras entidad, Connection conn) throws Exception {
        String sql = "UPDATE codigo_barras SET tipo = ?, valor = ?, fecha_asignacion = ?, observaciones = ? WHERE id = ?";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entidad.getTipo().name());
            stmt.setString(2, entidad.getValor());
            stmt.setDate(3, Date.valueOf(entidad.getFechaAsignacion()));
            
            // Actualizar observaciones (puede ser null)
            if (entidad.getObservaciones() != null && !entidad.getObservaciones().trim().isEmpty()) {
                stmt.setString(4, entidad.getObservaciones());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            stmt.setInt(5, entidad.getId());

            stmt.executeUpdate();
            
            // Solo hacer commit si manejamos la conexión
            if (!usarConexionExterna) {
                conn.commit();
            }
        } finally {
            // Solo cerrar si manejamos la conexión
            if (!usarConexionExterna && conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        eliminar(id, null);
    }
    
    /**
     * Elimina (soft delete) un código de barras usando una conexión existente (para transacciones).
     * Si conn es null, crea una nueva conexión.
     */
    public void eliminar(int id, Connection conn) throws Exception {
        String sql = "UPDATE codigo_barras SET eliminado = true WHERE id = ? AND eliminado = false";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            
            // Solo hacer commit si manejamos la conexión
            if (!usarConexionExterna) {
                conn.commit();
            }
        } finally {
            // Solo cerrar si manejamos la conexión
            if (!usarConexionExterna && conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public CodigoBarras getById(int id) throws Exception {
        String sql = "SELECT * FROM codigo_barras WHERE id = ? AND eliminado = false";
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
        String sql = "SELECT * FROM codigo_barras WHERE eliminado = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public CodigoBarras getByValor(String valor) throws Exception {
        return getByValor(valor, null);
    }
    
    /**
     * Obtiene un código de barras por valor usando una conexión existente (para transacciones).
     * Si conn es null, crea una nueva conexión.
     */
    public CodigoBarras getByValor(String valor, Connection conn) throws Exception {
        String sql = "SELECT * FROM codigo_barras WHERE valor = ? AND eliminado = false";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } finally {
            // Solo cerrar si manejamos la conexión
            if (!usarConexionExterna && conn != null) {
                conn.close();
            }
        }
        return null;
    }

    /**
     * Método auxiliar para mapear un ResultSet a un objeto CodigoBarras.
     */
    private CodigoBarras mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        boolean eliminado = rs.getBoolean("eliminado");

        String tipoStr = rs.getString("tipo");
        EnumTipo tipo = (tipoStr != null && !tipoStr.trim().isEmpty()) 
                ? EnumTipo.valueOf(tipoStr.trim().toUpperCase())
                : null;

        String valor = rs.getString("valor");

        java.sql.Date sqlDate = rs.getDate("fecha_asignacion");
        LocalDate fecha = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        // Leer observaciones - puede ser null en la BD
        String observaciones = rs.getString("observaciones");
        // Si rs.getString devuelve null, el objeto tendrá null
        // Si devuelve cadena vacía, también se mantiene

        // Constructor: (int id, boolean eliminado, EnumTipo tipo, String valor, LocalDate fechaAsignacion, String observaciones)
        return new CodigoBarras(id, eliminado, tipo, valor, fecha, observaciones);
    }
}
