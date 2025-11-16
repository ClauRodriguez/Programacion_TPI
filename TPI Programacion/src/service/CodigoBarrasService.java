/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.CodigoBarrasDAO;
import config.DatabaseConnection;
import model.CodigoBarras;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CodigoBarrasService implements GenericService<CodigoBarras> {

    private final CodigoBarrasDAO codigoBarrasDAO = new CodigoBarrasDAO();

    @Override
    public void insertar(CodigoBarras entidad) throws Exception {
        validarCodigoBarras(entidad);
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            CodigoBarras existente = codigoBarrasDAO.getByValor(entidad.getValor(), conn);
            if (existente != null && !existente.isEliminado()) {
                throw new IllegalArgumentException("Ya existe un código de barras con el valor: " + entidad.getValor());
            }
            
            codigoBarrasDAO.insertar(entidad, conn);
            
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
    public void actualizar(CodigoBarras entidad) throws Exception {
        validarCodigoBarras(entidad);
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            CodigoBarras existente = codigoBarrasDAO.getById(entidad.getId());
            if (existente != null && !existente.getValor().equals(entidad.getValor())) {
                CodigoBarras otroConMismoValor = codigoBarrasDAO.getByValor(entidad.getValor(), conn);
                if (otroConMismoValor != null && !otroConMismoValor.isEliminado() && 
                    otroConMismoValor.getId() != entidad.getId()) {
                    throw new IllegalArgumentException("Ya existe otro código de barras con el valor: " + entidad.getValor());
                }
            }
            
            codigoBarrasDAO.actualizar(entidad, conn);
            
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
     * Valida todas las reglas de negocio para un CodigoBarras.
     * @param codigo CodigoBarras a validar
     * @throws IllegalArgumentException Si alguna validación falla
     */
    private void validarCodigoBarras(CodigoBarras codigo) throws IllegalArgumentException {
        if (codigo.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de código de barras no puede ser null.");
        }
        
        if (codigo.getValor() == null || codigo.getValor().trim().isEmpty()) {
            throw new IllegalArgumentException("El valor del código de barras no puede estar vacío.");
        }
        if (codigo.getValor().length() > 20) {
            throw new IllegalArgumentException("El valor del código de barras no puede tener más de 20 caracteres.");
        }
        
        if (codigo.getObservaciones() != null && codigo.getObservaciones().length() > 255) {
            throw new IllegalArgumentException("Las observaciones no pueden tener más de 255 caracteres.");
        }
        
        if (codigo.getFechaAsignacion() == null) {
            throw new IllegalArgumentException("La fecha de asignación no puede ser null.");
        }
    }

    @Override
    public void eliminar(long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            codigoBarrasDAO.eliminar(id, conn);
            
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
    CodigoBarras codigoActivo = codigoBarrasDAO.getById(id);
    if (codigoActivo != null) {
        throw new IllegalArgumentException("El código de barras con ID " + id + " no está borrado.");
    }

    Connection conn = null;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);

        codigoBarrasDAO.recuperar(id, conn);

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
    public CodigoBarras getById(long id) throws Exception {
        return codigoBarrasDAO.getById(id);
    }

    @Override
    public List<CodigoBarras> getAll() throws Exception {
        return codigoBarrasDAO.getAll();
    }

    public CodigoBarras getByValor(String valor) throws Exception {
        return codigoBarrasDAO.getByValor(valor);
    }
}