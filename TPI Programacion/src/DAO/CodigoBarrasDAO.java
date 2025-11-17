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

        /**
     * Inserta un nuevo código de barras usando una conexión propia.
     *
     * <p>Este método crea y gestiona su propia conexión. Para usarlo dentro
     * de una transacción externa, ver la sobrecarga con {@link Connection}.</p>
     *
     * @param entidad código de barras a insertar. No debe ser {@code null}.
     * @throws Exception si ocurre un error al acceder a la base de datos.
     * @see #insertar(CodigoBarras, Connection)
     */
    @Override
    public void insertar(CodigoBarras entidad) throws Exception {
        insertar(entidad, null);
    }
    
   /**
     * Inserta un nuevo código de barras usando una conexión existente.
     *
     * <p>Si {@code conn} es {@code null}, el método crea una nueva conexión
     * y hace <em>commit</em> al finalizar. Si se pasa una conexión externa,
     * no se modifica su estado de transacción.</p>
     *
     * @param entidad código de barras a insertar. No debe ser {@code null}.
     * @param conn    conexión a reutilizar, o {@code null} para crear una nueva.
     * @throws Exception si ocurre un error al ejecutar la sentencia SQL.
     */
    public void insertar(CodigoBarras entidad, Connection conn) throws Exception {
        String sql = "INSERT INTO codigo_barras (tipo, valor, fecha_asignacion, observaciones) VALUES (?, ?, ?, ?)";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, entidad.getTipo().name());
            stmt.setString(2, entidad.getValor());
            stmt.setDate(3, Date.valueOf(entidad.getFechaAsignacion()));
            
            String obsValue = entidad.getObservaciones();
            
            if (obsValue != null && !obsValue.trim().isEmpty()) {
                stmt.setString(4, obsValue.trim());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getLong(1));
                }
            }
            
            if (!usarConexionExterna) {
                conn.commit();
            }
        } finally {
            if (!usarConexionExterna && conn != null) {
                conn.close();
            }
        }
    }

        /**
     * Actualiza un código de barras usando una conexión propia.
     *
     * <p>Este método crea y gestiona su propia conexión. Para usarlo dentro
     * de una transacción externa, ver la sobrecarga con {@link Connection}.</p>
     *
     * @param entidad código de barras con los datos actualizados.
     * @throws Exception si ocurre un error al acceder a la base de datos.
     * @see #actualizar(CodigoBarras, Connection)
     */
    @Override
    public void actualizar(CodigoBarras entidad) throws Exception {
        actualizar(entidad, null);
    }
    
   /**
     * Actualiza un código de barras usando una conexión existente.
     *
     * <p>Si {@code conn} es {@code null}, el método crea una nueva conexión
     * y hace <em>commit</em> al finalizar. Si se pasa una conexión externa,
     * no se modifica su estado de transacción.</p>
     *
     * @param entidad código de barras a actualizar. Debe tener un {@code id} válido.
     * @param conn    conexión a reutilizar, o {@code null} para crear una nueva.
     * @throws Exception si ocurre un error al ejecutar la sentencia SQL.
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
            
            if (entidad.getObservaciones() != null && !entidad.getObservaciones().trim().isEmpty()) {
                stmt.setString(4, entidad.getObservaciones());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            stmt.setLong(5, entidad.getId());

            stmt.executeUpdate();
            
            if (!usarConexionExterna) {
                conn.commit();
            }
        } finally {
            if (!usarConexionExterna && conn != null) {
                conn.close();
            }
        }
    }

     /**
     * Marca como eliminado (soft delete) un código de barras usando una conexión propia.
     *
     * <p>Este método actualiza la bandera <code>eliminado</code> a {@code true}
     * para el registro indicado.</p>
     *
     * @param id identificador del código de barras a eliminar.
     * @throws Exception si ocurre un error al acceder a la base de datos.
     * @see #eliminar(long, Connection)
     */
    @Override
    public void eliminar(long id) throws Exception {
        eliminar(id, null);
    }
    
     /**
     * Marca como eliminado (soft delete) un código de barras usando una conexión existente.
     *
     * <p>Si {@code conn} es {@code null}, el método crea una nueva conexión
     * y hace <em>commit</em> al finalizar. Si se pasa una conexión externa,
     * no se modifica su estado de transacción.</p>
     *
     * @param id   identificador del código de barras a eliminar.
     * @param conn conexión a reutilizar, o {@code null} para crear una nueva.
     * @throws Exception si ocurre un error al ejecutar la sentencia SQL.
     */
    public void eliminar(long id, Connection conn) throws Exception {
        String sql = "UPDATE codigo_barras SET eliminado = true WHERE id = ? AND eliminado = false";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            
            if (!usarConexionExterna) {
                conn.commit();
            }
        } finally {
            if (!usarConexionExterna && conn != null) {
                conn.close();
            }
        }
    }
    
        /**
     * Recupera (revierte el soft delete) de un código de barras usando una conexión propia.
     *
     * @param id identificador del código de barras a recuperar.
     * @throws Exception si ocurre un error al acceder a la base de datos.
     * @see #recuperar(long, Connection)
     */
    public void recuperar(long id) throws Exception {
    recuperar(id, null);
}

        /**
     * Recupera (revierte el soft delete) de un código de barras usando una conexión existente.
     *
     * <p>Cambia la bandera <code>eliminado</code> a {@code false} si el registro
     * estaba previamente marcado como eliminado.</p>
     *
     * @param id   identificador del código de barras a recuperar.
     * @param conn conexión a reutilizar, o {@code null} para crear una nueva.
     * @throws Exception si ocurre un error al ejecutar la sentencia SQL.
     */
public void recuperar(long id, Connection conn) throws Exception {
    String sql = "UPDATE codigo_barras SET eliminado = false WHERE id = ? AND eliminado = true";
    boolean usarConexionExterna = (conn != null);

    if (!usarConexionExterna) {
        conn = DatabaseConnection.getConnection();
    }

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setLong(1, id);
        stmt.executeUpdate();

        if (!usarConexionExterna) {
            conn.commit();
        }
    } finally {
        if (!usarConexionExterna && conn != null) {
            conn.close();
        }
    }
}

    /**
     * Obtiene un código de barras por su identificador.
     *
     * <p>Solo devuelve registros no eliminados.</p>
     *
     * @param id identificador del código de barras.
     * @return instancia de {@link CodigoBarras} o {@code null} si no se encuentra.
     * @throws Exception si ocurre un error al acceder a la base de datos.
     */
    @Override
    public CodigoBarras getById(long id) throws Exception {
        String sql = "SELECT * FROM codigo_barras WHERE id = ? AND eliminado = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

       /**
     * Obtiene todos los códigos de barras activos.
     *
     * <p>Solo se listan registros donde <code>eliminado = false</code>.</p>
     *
     * @return lista de códigos de barras activos. Nunca es {@code null}.
     * @throws Exception si ocurre un error al acceder a la base de datos.
     */
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

        /**
     * Obtiene un código de barras por su valor, usando una conexión propia.
     *
     * @param valor valor exacto del código de barras.
     * @return instancia de {@link CodigoBarras} o {@code null} si no se encuentra.
     * @throws Exception si ocurre un error al acceder a la base de datos.
     * @see #getByValor(String, Connection)
     */
    public CodigoBarras getByValor(String valor) throws Exception {
        return getByValor(valor, null);
    }
    
    /**
     * Obtiene un código de barras por su valor usando una conexión existente.
     *
     * <p>Solo se tienen en cuenta registros no eliminados.</p>
     *
     * @param valor valor exacto del código de barras.
     * @param conn  conexión a reutilizar, o {@code null} para crear una nueva.
     * @return instancia de {@link CodigoBarras} o {@code null} si no se encuentra.
     * @throws Exception si ocurre un error al ejecutar la consulta.
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
            if (!usarConexionExterna && conn != null) {
                conn.close();
            }
        }
        return null;
    }

    /**
     * Mapea la fila actual de un {@link ResultSet} a un objeto {@link CodigoBarras}.
     *
     * <p>Lee las columnas básicas de la tabla y construye la instancia
     * de dominio correspondiente.</p>
     *
     * @param rs resultado de una consulta posicionado en una fila válida.
     * @return instancia de {@link CodigoBarras} creada a partir de la fila.
     * @throws SQLException si ocurre un error al leer los datos del {@link ResultSet}.
     */
    private CodigoBarras mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        boolean eliminado = rs.getBoolean("eliminado");

        String tipoStr = rs.getString("tipo");
        EnumTipo tipo = (tipoStr != null && !tipoStr.trim().isEmpty()) 
                ? EnumTipo.valueOf(tipoStr.trim().toUpperCase())
                : null;

        String valor = rs.getString("valor");

        java.sql.Date sqlDate = rs.getDate("fecha_asignacion");
        LocalDate fecha = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        String observaciones = rs.getString("observaciones");

        return new CodigoBarras(id, eliminado, tipo, valor, fecha, observaciones);
    }
}
