/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.DatabaseConnection;
import model.Producto;
import model.CategoriaProducto;
import model.CodigoBarras;
import model.EnumTipo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Interfaz + implementación en un solo archivo
public class ProductoDAO implements GenericDAO<Producto> {

    @Override
    public void insertar(Producto entidad) throws Exception {
        insertar(entidad, null);
    }
    
    /**
     * Inserta un producto usando una conexión existente (para transacciones).
     * Si conn es null, crea una nueva conexión.
     */
    public void insertar(Producto entidad, Connection conn) throws Exception {
        String sql = "INSERT INTO producto (nombre, marca, categoria, precio, peso, stock, codigo_barras_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, entidad.getNombre());
            stmt.setString(2, entidad.getMarca());
            
            // Convertir enum CategoriaProducto a String
            String categoriaStr = (entidad.getCategoria() != null) ? entidad.getCategoria().name() : null;
            stmt.setString(3, categoriaStr);
            
            stmt.setDouble(4, entidad.getPrecio());
            stmt.setDouble(5, entidad.getPeso());
            stmt.setInt(6, entidad.getStock());
            
            // Guardar ID del código de barras si existe
            if (entidad.getCodigoBarras() != null && entidad.getCodigoBarras().getId() > 0) {
                stmt.setLong(7, entidad.getCodigoBarras().getId());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }
            
            stmt.executeUpdate();
            
            // Recuperar ID generado automáticamente por la BD
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getLong(1));
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
    public void actualizar(Producto entidad) throws Exception {
        actualizar(entidad, null);
    }
    
    /**
     * Actualiza un producto usando una conexión existente (para transacciones).
     * Si conn es null, crea una nueva conexión.
     */
    public void actualizar(Producto entidad, Connection conn) throws Exception {
        String sql = "UPDATE producto SET nombre = ?, marca = ?, categoria = ?, precio = ?, peso = ?, stock = ?, codigo_barras_id = ? WHERE id = ?";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, entidad.getNombre());
            stmt.setString(2, entidad.getMarca());
            
            // Convertir enum CategoriaProducto a String
            String categoriaStr = (entidad.getCategoria() != null) ? entidad.getCategoria().name() : null;
            stmt.setString(3, categoriaStr);
            
            stmt.setDouble(4, entidad.getPrecio());
            stmt.setDouble(5, entidad.getPeso());
            stmt.setInt(6, entidad.getStock());
            
            // Actualizar ID del código de barras si existe
            if (entidad.getCodigoBarras() != null && entidad.getCodigoBarras().getId() > 0) {
                stmt.setLong(7, entidad.getCodigoBarras().getId());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }
            
            stmt.setLong(8, entidad.getId());
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
    
     public void asignarCodigoDeBarras(Producto entidad, Connection conn) throws Exception {
        String sql = "UPDATE producto SET  codigo_barras_id = ? WHERE id = ?";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Actualizar ID del código de barras si existe
            if (entidad.getCodigoBarras() != null && entidad.getCodigoBarras().getId() > 0) {
                stmt.setLong(1, entidad.getCodigoBarras().getId());
            } else {
                stmt.setNull(1, Types.BIGINT);
            }
            
            stmt.setLong(2, entidad.getId());
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
    public void eliminar(long id) throws Exception {
        eliminar(id, null);
    }
    
    /**
     * Elimina (soft delete) un producto usando una conexión existente (para transacciones).
     * Si conn es null, crea una nueva conexión.
     */
    public void eliminar(long id, Connection conn) throws Exception {
        String sql = "UPDATE producto SET eliminado = true WHERE id = ? AND eliminado = false";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
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
    
    public void recuperar(long id) throws Exception {
        recuperar(id, null);
    }

    public void recuperar(long id, Connection conn) throws Exception {
        String sql = "UPDATE producto SET eliminado = false WHERE id = ? AND eliminado = true";
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

    @Override
    public Producto getById(long id) throws Exception {
        // Usar LEFT JOIN para cargar código de barras en una sola query
        String sql = "SELECT p.id, p.nombre, p.marca, p.categoria, p.precio, p.peso, p.stock, p.eliminado, " +
                     "p.codigo_barras_id, " +
                     "c.id AS codigo_id, c.tipo AS codigo_tipo, c.valor AS codigo_valor, " +
                     "c.fecha_asignacion AS codigo_fecha, c.observaciones AS codigo_obs, " +
                     "c.eliminado AS codigo_eliminado " +
                     "FROM producto p " +
                     "LEFT JOIN codigo_barras c ON p.codigo_barras_id = c.id AND c.eliminado = false " +
                     "WHERE p.id = ? AND p.eliminado = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowWithJoin(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Producto> getAll() throws Exception {
        List<Producto> lista = new ArrayList<>();
        // Usar LEFT JOIN para cargar código de barras en una sola query
        String sql = "SELECT p.id, p.nombre, p.marca, p.categoria, p.precio, p.peso, p.stock, p.eliminado, " +
                     "p.codigo_barras_id, " +
                     "c.id AS codigo_id, c.tipo AS codigo_tipo, c.valor AS codigo_valor, " +
                     "c.fecha_asignacion AS codigo_fecha, c.observaciones AS codigo_obs, " +
                     "c.eliminado AS codigo_eliminado " +
                     "FROM producto p " +
                     "LEFT JOIN codigo_barras c ON p.codigo_barras_id = c.id AND c.eliminado = false " +
                     "WHERE p.eliminado = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRowWithJoin(rs));
            }
        }
        return lista;
    }

    public Producto getByNombre(String nombre) throws Exception {
        return getByNombre(nombre, null);
    }
    
    /**
     * Obtiene un producto por nombre usando una conexión existente (para transacciones).
     * Si conn es null, crea una nueva conexión.
     */
    public Producto getByNombre(String nombre, Connection conn) throws Exception {
        // Usar LEFT JOIN para cargar código de barras en una sola query
        String sql = "SELECT p.id, p.nombre, p.marca, p.categoria, p.precio, p.peso, p.stock, p.eliminado, " +
                     "p.codigo_barras_id, " +
                     "c.id AS codigo_id, c.tipo AS codigo_tipo, c.valor AS codigo_valor, " +
                     "c.fecha_asignacion AS codigo_fecha, c.observaciones AS codigo_obs, " +
                     "c.eliminado AS codigo_eliminado " +
                     "FROM producto p " +
                     "LEFT JOIN codigo_barras c ON p.codigo_barras_id = c.id AND c.eliminado = false " +
                     "WHERE p.nombre = ? AND p.eliminado = false";
        boolean usarConexionExterna = (conn != null);
        
        if (!usarConexionExterna) {
            conn = DatabaseConnection.getConnection();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowWithJoin(rs);
                }
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
     * Método auxiliar para mapear un ResultSet con JOIN a un objeto Producto.
     * Carga el código de barras directamente desde el JOIN, evitando query separada.
     * 
     * @param rs ResultSet con datos del producto y código de barras (si existe)
     * @return Producto con código de barras cargado (si existe)
     * @throws SQLException Si hay error al leer el ResultSet
     */
    private Producto mapRowWithJoin(ResultSet rs) throws SQLException {
        // Mapear campos del producto
        long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        String marca = rs.getString("marca");
        double precio = rs.getDouble("precio");
        double peso = rs.getDouble("peso");
        int stock = rs.getInt("stock");
        boolean eliminado = rs.getBoolean("eliminado");
        
        // Convertir String a CategoriaProducto enum
        CategoriaProducto categoria = null;
        String categoriaStr = rs.getString("categoria");
        if (categoriaStr != null && !categoriaStr.trim().isEmpty()) {
            try {
                categoria = CategoriaProducto.valueOf(categoriaStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Advertencia: Categoría inválida en BD: " + categoriaStr);
            }
        }
        
        // Crear producto
        Producto producto = new Producto(nombre, marca, precio, peso, stock, id);
        producto.setCategoria(categoria);
        producto.setEliminado(eliminado);
        
        // Cargar código de barras desde el JOIN (si existe)
        // Verificar si el código de barras existe usando wasNull() o verificando el ID
        long codigoId = rs.getLong("codigo_id");
        if (!rs.wasNull() && codigoId > 0) {
            // El JOIN trajo datos del código de barras
            String tipoStr = rs.getString("codigo_tipo");
            EnumTipo tipo = null;
            if (tipoStr != null && !tipoStr.trim().isEmpty()) {
                try {
                    tipo = EnumTipo.valueOf(tipoStr.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println("Advertencia: Tipo de código inválido en BD: " + tipoStr);
                }
            }
            
            String valor = rs.getString("codigo_valor");
            java.sql.Date sqlDate = rs.getDate("codigo_fecha");
            LocalDate fecha = (sqlDate != null) ? sqlDate.toLocalDate() : null;
            String observaciones = rs.getString("codigo_obs");
            boolean codigoEliminado = rs.getBoolean("codigo_eliminado");
            
            // Crear objeto CodigoBarras desde el JOIN
            CodigoBarras codigo = new CodigoBarras(codigoId, codigoEliminado, tipo, valor, fecha, observaciones);
            producto.setCodigoBarras(codigo);
        }
        // Si wasNull() es true, significa que no hay código de barras asociado (LEFT JOIN devuelve NULL)
        
        return producto;
    }
    
    /**
     * Método auxiliar para mapear un ResultSet a un objeto Producto (método legacy).
     * Mantenido para compatibilidad, pero se recomienda usar mapRowWithJoin().
     * Maneja la conversión de categoría (String a Enum) y carga el código de barras asociado.
     * 
     * @deprecated Usar mapRowWithJoin() en su lugar para mejor rendimiento
     */
    @Deprecated
    private Producto mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        String marca = rs.getString("marca");
        double precio = rs.getDouble("precio");
        double peso = rs.getDouble("peso");
        int stock = rs.getInt("stock");
        boolean eliminado = rs.getBoolean("eliminado");
        
        // Convertir String a CategoriaProducto enum
        CategoriaProducto categoria = null;
        String categoriaStr = rs.getString("categoria");
        if (categoriaStr != null && !categoriaStr.trim().isEmpty()) {
            try {
                categoria = CategoriaProducto.valueOf(categoriaStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Si el valor no es un enum válido, se deja como null
                System.err.println("Advertencia: Categoría inválida en BD: " + categoriaStr);
            }
        }
        
        // Crear producto
        Producto producto = new Producto(nombre, marca, precio, peso, stock, id);
        producto.setCategoria(categoria);
        producto.setEliminado(eliminado);
        
        // Cargar código de barras si existe la relación
        long codigoBarrasId = rs.getLong("codigo_barras_id");
        if (!rs.wasNull() && codigoBarrasId > 0) {
            try {
                CodigoBarrasDAO codigoDAO = new CodigoBarrasDAO();
                CodigoBarras codigo = codigoDAO.getById(codigoBarrasId);
                if (codigo != null) {
                    producto.setCodigoBarras(codigo);
                }
            } catch (Exception e) {
                // Si no se puede cargar el código de barras, continuar sin él
                System.err.println("Advertencia: No se pudo cargar código de barras con ID: " + codigoBarrasId);
            }
        }
        
        return producto;
    }
}