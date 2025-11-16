/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.ProductoDAO;
import config.DatabaseConnection;
import model.Producto;
import model.CodigoBarras;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductoService implements GenericService<Producto> {

    private final ProductoDAO productoDAO = new ProductoDAO();

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
     * Valida todas las reglas de negocio para un Producto.
     * @param producto Producto a validar
     * @throws IllegalArgumentException Si alguna validación falla
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
    @Override
    public Producto getById(long id) throws Exception {
        return productoDAO.getById(id);
    }

    @Override
    public List<Producto> getAll() throws Exception {
        return productoDAO.getAll();
    }

    public Producto getByNombre(String nombre) throws Exception {
        return productoDAO.getByNombre(nombre);
    }
    
    /**
     * Inserta un producto junto con su código de barras en una sola transacción.
     * Si falla alguna operación, se hace rollback de ambas.
     * 
     * @param producto Producto a insertar
     * @param codigo Código de barras a insertar y asociar al producto
     * @throws Exception Si falla la validación o la inserción
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