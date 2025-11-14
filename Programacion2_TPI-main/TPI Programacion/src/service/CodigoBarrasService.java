/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */

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
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar transacción
            
            // Validar UNIQUE: verificar si ya existe un código con el mismo valor (en la misma transacción)
            CodigoBarras existente = codigoBarrasDAO.getByValor(entidad.getValor(), conn);
            if (existente != null && !existente.isEliminado()) {
                throw new IllegalArgumentException("Ya existe un código de barras con el valor: " + entidad.getValor());
            }
            
            codigoBarrasDAO.insertar(entidad, conn);  // Pasar conexión al DAO
            
            conn.commit();  // Commit si todo sale bien
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback en caso de error
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error al hacer rollback: " + rollbackEx.getMessage(), e);
                }
            }
            throw e;  // Re-lanzar excepción original
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Restaurar auto-commit
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
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar transacción
            
            // Validar UNIQUE solo si cambió el valor (en la misma transacción)
            CodigoBarras existente = codigoBarrasDAO.getById(entidad.getId());
            if (existente != null && !existente.getValor().equals(entidad.getValor())) {
                // El valor cambió, verificar que no exista otro con el nuevo valor
                CodigoBarras otroConMismoValor = codigoBarrasDAO.getByValor(entidad.getValor(), conn);
                if (otroConMismoValor != null && !otroConMismoValor.isEliminado() && 
                    otroConMismoValor.getId() != entidad.getId()) {
                    throw new IllegalArgumentException("Ya existe otro código de barras con el valor: " + entidad.getValor());
                }
            }
            
            codigoBarrasDAO.actualizar(entidad, conn);  // Pasar conexión al DAO
            
            conn.commit();  // Commit si todo sale bien
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback en caso de error
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error al hacer rollback: " + rollbackEx.getMessage(), e);
                }
            }
            throw e;  // Re-lanzar excepción original
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Restaurar auto-commit
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
        // Validar tipo
        if (codigo.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de código de barras no puede ser null.");
        }
        
        // Validar valor
        if (codigo.getValor() == null || codigo.getValor().trim().isEmpty()) {
            throw new IllegalArgumentException("El valor del código de barras no puede estar vacío.");
        }
        if (codigo.getValor().length() > 20) {
            throw new IllegalArgumentException("El valor del código de barras no puede tener más de 20 caracteres.");
        }
        
        // Validar observaciones
        if (codigo.getObservaciones() != null && codigo.getObservaciones().length() > 255) {
            throw new IllegalArgumentException("Las observaciones no pueden tener más de 255 caracteres.");
        }
        
        // Validar fecha de asignación
        if (codigo.getFechaAsignacion() == null) {
            throw new IllegalArgumentException("La fecha de asignación no puede ser null.");
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar transacción
            
            codigoBarrasDAO.eliminar(id, conn);  // Pasar conexión al DAO
            
            conn.commit();  // Commit si todo sale bien
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback en caso de error
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error al hacer rollback: " + rollbackEx.getMessage(), e);
                }
            }
            throw e;  // Re-lanzar excepción original
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Restaurar auto-commit
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }

    @Override
    public CodigoBarras getById(int id) throws Exception {
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