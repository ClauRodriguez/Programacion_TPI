/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */
package service;

import DAO.ProductoDAO;
import config.DatabaseConnection;
import model.Producto;
import model.CodigoBarras;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para gestionar las operaciones de negocio de la entidad Producto.
 * 
 * <p>Esta clase implementa la lógica de negocio, validaciones y transacciones
 * para las operaciones CRUD de productos, incluyendo la gestión de la relación
 * 1→1 unidireccional con CódigoBarras.</p>
 * 
 * <p><b>Patrón de diseño:</b> Service Layer - Separa la lógica de negocio del
 * acceso a datos (DAO) y presenta una interfaz cohesiva para las operaciones
 * transaccionales.</p>
 * 
 * <p><b>Responsabilidades principales:</b>
 * <ul>
 *   <li>Validaciones de negocio para productos</li>
 *   <li>Gestión de transacciones atómicas</li>
 *   <li>Coordinación entre DAOs para operaciones compuestas</li>
 *   <li>Manejo de la relación 1→1 con CódigoBarras</li>
 * </ul>
 * </p>
 * 
 * @author Gaston Alberto Cejas
 * @author Hernan Cóceres
 * @author Claudio Rodriguez  
 * @author Hernan E. Bula
 * @version 1.0
 * @since 2025
 * @see Producto
 * @see CodigoBarras
 * @see ProductoDAO
 * @see GenericService
 */
public class ProductoService implements GenericService<Producto> {

    private final ProductoDAO productoDAO = new ProductoDAO();

    /**
     * Inserta un nuevo producto en el sistema con transacción atómica.
     * 
     * <p><b>Flujo transaccional:</b>
     * <ol>
     *   <li>Valida las reglas de negocio del producto</li>
     *   <li>Inicia transacción (autoCommit = false)</li>
     *   <li>Inserta el producto mediante el DAO</li>
     *   <li>Confirma la transacción (commit)</li>
     * </ol>
     * </p>
     * 
     * <p><b>Validaciones aplicadas:</b>
     * <ul>
     *   <li>Nombre no nulo y no vacío</li>
     *   <li>Longitud máxima del nombre (120 caracteres)</li>
     *   <li>Longitud máxima de la marca (80 caracteres)</li>
     *   <li>Precio no negativo y dentro de límites</li>
     *   <li>Peso no negativo y dentro de límites</li>
     *   <li>Stock no negativo</li>
     * </ul>
     * </p>
     *
     * @param entidad El producto a insertar. No puede ser {@code null}
     * @throws IllegalArgumentException Si las validaciones de negocio fallan
     * @throws Exception Si ocurre un error durante la transacción
     * @see #validarProducto(Producto)
     */
    @Override
    public void insertar(Producto entidad) throws Exception {
        validarProducto(entidad);
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            productoDAO.insertar(entidad, conn);
            
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error al hacer rollback: " + rollbackEx.getMessage(), e);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }

    /**
     * Actualiza un producto existente en el sistema con transacción atómica.
     * 
     * <p><b>Nota:</b> Este método actualiza todos los campos del producto.
     * Para actualizaciones parciales, considere implementar métodos específicos.</p>
     *
     * @param entidad El producto con los datos actualizados. No puede ser {@code null}
     * @throws IllegalArgumentException Si las validaciones de negocio fallan
     * @throws Exception Si ocurre un error durante la transacción
     * @see #validarProducto(Producto)
     */
    @Override
    public void actualizar(Producto entidad) throws Exception {
        validarProducto(entidad);
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            productoDAO.actualizar(entidad, conn);
            
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error al hacer rollback: " + rollbackEx.getMessage(), e);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }
    
    /**
     * Asigna un código de barras a un producto existente.
     * 
     * <p><b>Relación 1→1:</b> Este método preserva la relación unidireccional
     * 1→1 permitiendo que un producto tenga un único código de barras.</p>
     *
     * @param entidad El producto al que se le asignará el código de barras
     * @throws IllegalArgumentException Si las validaciones de negocio fallan
     * @throws Exception Si ocurre un error durante la transacción
     */
    public void asignarCodigoDeBarras(Producto entidad) throws Exception {
        validarProducto(entidad);
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            productoDAO.asignarCodigoDeBarras(entidad, conn);
            
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error al hacer rollback: " + rollbackEx.getMessage(), e);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }
    
    /**
     * Valida todas las reglas de negocio para un Producto antes de su persistencia.
     * 
     * <p><b>Por qué se valida aquí:</b> Las validaciones de negocio se realizan
     * en la capa Service para garantizar la consistencia de los datos antes de
     * iniciar cualquier transacción, evitando operaciones innecesarias en la BD.</p>
     *
     * @param producto Producto a validar. No puede ser {@code null}
     * @throws IllegalArgumentException Si alguna validación falla, con mensaje descriptivo
     */
    private void validarProducto(Producto producto) throws IllegalArgumentException {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (producto.getNombre().length() > 120) {
            throw new IllegalArgumentException("El nombre no puede tener más de 120 caracteres.");
        }
        
        if (producto.getMarca() != null && producto.getMarca().length() > 80) {
            throw new IllegalArgumentException("La marca no puede tener más de 80 caracteres.");
        }
        
        if (producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0.");
        }
        if (producto.getPrecio() > 99999999.99) {
            throw new IllegalArgumentException("El precio no puede ser mayor a 99,999,999.99");
        }
        
        if (producto.getPeso() < 0) {
            throw new IllegalArgumentException("El peso no puede ser negativo.");
        }
        if (producto.getPeso() > 9999999.999) {
            throw new IllegalArgumentException("El peso no puede ser mayor a 9,999,999.999");
        }
        
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
    }

    /**
     * Realiza una eliminación lógica (soft delete) de un producto.
     * 
     * <p><b>Ventaja de eliminación lógica:</b> Permite preservar la integridad
     * referencial y la posibilidad de recuperar el producto en el futuro
     * mediante el método {@link #recuperar(long)}.</p>
     *
     * @param id ID del producto a eliminar
     * @throws Exception Si ocurre un error durante la transacción
     * @see #recuperar(long)
     */
    @Override
    public void eliminar(long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            productoDAO.eliminar(id, conn);
            
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error al hacer rollback: " + rollbackEx.getMessage(), e);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }
    
    /**
     * Recupera un producto previamente eliminado (soft delete).
     * 
     * <p><b>Validación previa:</b> Verifica que el producto no esté activo
     * antes de intentar la recuperación para evitar inconsistencias.</p>
     *
     * @param id ID del producto a recuperar
     * @throws IllegalArgumentException Si el producto no está eliminado
     * @throws Exception Si ocurre un error durante la transacción
     * @see #eliminar(long)
     */
    public void recuperar(long id) throws Exception {
        Producto productoActivo = productoDAO.getById(id);
        if (productoActivo != null) {
            throw new IllegalArgumentException("El producto con ID " + id + " no está borrado.");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            productoDAO.recuperar(id, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error al hacer rollback: " + rollbackEx.getMessage(), e);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }
    
    /**
     * Obtiene un producto por su ID.
     * 
     * <p><b>Nota:</b> Este método solo retorna productos activos (no eliminados).</p>
     *
     * @param id ID del producto a buscar
     * @return El producto encontrado o {@code null} si no existe o está eliminado
     * @throws Exception Si ocurre un error en el acceso a datos
     */
    @Override
    public Producto getById(long id) throws Exception {
        return productoDAO.getById(id);
    }

    /**
     * Obtiene todos los productos activos del sistema.
     * 
     * @return Lista de productos activos. Lista vacía si no hay productos
     * @throws Exception Si ocurre un error en el acceso a datos
     */
    @Override
    public List<Producto> getAll() throws Exception {
        return productoDAO.getAll();
    }

    /**
     * Busca un producto por su nombre exacto.
     * 
     * <p><b>Búsqueda exacta:</b> Este método realiza una búsqueda por coincidencia
     * exacta del nombre. Para búsquedas parciales, considere implementar
     * un método adicional.</p>
     *
     * @param nombre Nombre exacto del producto a buscar
     * @return El producto encontrado o {@code null} si no existe
     * @throws Exception Si ocurre un error en el acceso a datos
     */
    public Producto getByNombre(String nombre) throws Exception {
        return productoDAO.getByNombre(nombre);
    }
    
    /**
     * Inserta un producto junto con su código de barras en una sola transacción atómica.
     * 
     * <p><b>Transacción compuesta:</b> Este método representa una operación
     * transaccional compleja que involucra múltiples entidades. Si falla
     * cualquier paso, se realiza rollback completo.</p>
     * 
     * <p><b>Flujo de la operación:</b>
     * <ol>
     *   <li>Valida el producto y el código de barras</li>
     *   <li>Verifica unicidad del valor del código de barras</li>
     *   <li>Inserta el código de barras</li>
     *   <li>Asocia el código al producto</li>
     *   <li>Inserta el producto</li>
     *   <li>Confirma la transacción (commit)</li>
     * </ol>
     * </p>
     * 
     * <p><b>Por qué es transaccional:</b> Garantiza que ambas entidades se
     * persistan consistentemente, manteniendo la integridad de la relación 1→1.</p>
     *
     * @param producto Producto a insertar. No puede ser {@code null}
     * @param codigo Código de barras a insertar y asociar al producto. No puede ser {@code null}
     * @throws IllegalArgumentException Si las validaciones fallan o el código de barras ya existe
     * @throws Exception Si ocurre un error durante la transacción
     */
    public void insertarConCodigoBarras(Producto producto, CodigoBarras codigo) throws Exception {
        validarProducto(producto);
        
        if (codigo == null) {
            throw new IllegalArgumentException("El código de barras no puede ser null.");
        }
        if (codigo.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de código de barras no puede ser null.");
        }
        if (codigo.getValor() == null || codigo.getValor().trim().isEmpty()) {
            throw new IllegalArgumentException("El valor del código de barras no puede estar vacío.");
        }
        if (codigo.getFechaAsignacion() == null) {
            throw new IllegalArgumentException("La fecha de asignación no puede ser null.");
        }
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            DAO.CodigoBarrasDAO codigoBarrasDAO = new DAO.CodigoBarrasDAO();
            
            CodigoBarras existente = codigoBarrasDAO.getByValor(codigo.getValor(), conn);
            if (existente != null && !existente.isEliminado()) {
                throw new IllegalArgumentException("Ya existe un código de barras con el valor: " + codigo.getValor());
            }
            
            codigoBarrasDAO.insertar(codigo, conn);
            
            producto.setCodigoBarras(codigo);
            
            productoDAO.insertar(producto, conn);
            
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error al hacer rollback: " + rollbackEx.getMessage(), e);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }
}