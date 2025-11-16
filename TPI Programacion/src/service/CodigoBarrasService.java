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

import DAO.CodigoBarrasDAO;
import config.DatabaseConnection;
import model.CodigoBarras;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para gestionar las operaciones de negocio de la entidad CodigoBarras.
 * 
 * <p>Esta clase implementa la lógica de negocio, validaciones y transacciones
 * para las operaciones CRUD de códigos de barras, garantizando la unicidad
 * de los valores y la integridad de los datos.</p>
 * 
 * <p><b>Relación 1→1 unidireccional:</b> Esta entidad representa el lado "B"
 * de la relación Producto→CodigoBarras, siendo referenciada por Producto
 * pero sin conocimiento de dicha relación (unidireccional).</p>
 * 
 * <p><b>Responsabilidades principales:</b>
 * <ul>
 *   <li>Validar la unicidad del valor del código de barras</li>
 *   <li>Gestionar transacciones para operaciones CRUD</li>
 *   <li>Aplicar validaciones de formato y reglas de negocio</li>
 *   <li>Garantizar la integridad referencial con Producto</li>
 * </ul>
 * </p>
 * 
 * @author Gaston Alberto Cejas
 * @author Hernan Cóceres
 * @author Claudio Rodriguez  
 * @author Hernan E. Bula
 * @version 1.0
 * @since 2025
 * @see CodigoBarras
 * @see ProductoService
 * @see GenericService
 */
public class CodigoBarrasService implements GenericService<CodigoBarras> {

    private final CodigoBarrasDAO codigoBarrasDAO = new CodigoBarrasDAO();

    /**
     * Inserta un nuevo código de barras en el sistema con validación de unicidad.
     * 
     * <p><b>Validación de unicidad:</b> Antes de insertar, verifica que no exista
     * otro código de barras activo con el mismo valor, garantizando la restricción
     * UNIQUE requerida por el negocio.</p>
     * 
     * <p><b>Flujo transaccional:</b>
     * <ol>
     *   <li>Valida las reglas de negocio del código de barras</li>
     *   <li>Verifica unicidad del valor</li>
     *   <li>Inicia transacción (autoCommit = false)</li>
     *   <li>Inserta el código de barras mediante el DAO</li>
     *   <li>Confirma la transacción (commit)</li>
     * </ol>
     * </p>
     *
     * @param entidad El código de barras a insertar. No puede ser {@code null}
     * @throws IllegalArgumentException Si el valor ya existe o las validaciones fallan
     * @throws Exception Si ocurre un error durante la transacción
     * @see #validarCodigoBarras(CodigoBarras)
     */
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

    /**
     * Actualiza un código de barras existente con validación de unicidad.
     * 
     * <p><b>Validación de unicidad en actualización:</b> Cuando se actualiza el valor
     * de un código de barras, verifica que el nuevo valor no esté siendo utilizado
     * por otro código de barras activo, excepto por sí mismo.</p>
     * 
     * <p><b>Escenarios manejados:</b>
     * <ul>
     *   <li>Si el valor no cambia: permite la actualización directamente</li>
     *   <li>Si el valor cambia: verifica que el nuevo valor no exista</li>
     *   <li>Si existe otro código con el nuevo valor: rechaza la actualización</li>
     * </ul>
     * </p>
     *
     * @param entidad El código de barras con los datos actualizados. No puede ser {@code null}
     * @throws IllegalArgumentException Si el nuevo valor ya existe o las validaciones fallan
     * @throws Exception Si ocurre un error durante la transacción
     */
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
     * 
     * <p><b>Por qué se valida aquí:</b> Las validaciones se realizan en la capa Service
     * para garantizar la consistencia de los datos antes de iniciar cualquier
     * transacción, evitando operaciones innecesarias en la BD y proporcionando
     * mensajes de error claros al usuario.</p>
     * 
     * <p><b>Validaciones aplicadas:</b>
     * <ul>
     *   <li>Tipo no nulo (EAN13, EAN8, UPC)</li>
     *   <li>Valor no nulo y no vacío</li>
     *   <li>Longitud máxima del valor (20 caracteres)</li>
     *   <li>Longitud máxima de observaciones (255 caracteres)</li>
     *   <li>Fecha de asignación no nula</li>
     * </ul>
     * </p>
     *
     * @param codigo CodigoBarras a validar. No puede ser {@code null}
     * @throws IllegalArgumentException Si alguna validación falla, con mensaje descriptivo
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

    /**
     * Realiza una eliminación lógica (soft delete) de un código de barras.
     * 
     * <p><b>Consideraciones de integridad referencial:</b> Al eliminar un código
     * de barras, la relación 1→1 con Producto se maneja mediante ON DELETE SET NULL,
     * permitiendo que el producto quede sin código de barras asociado pero
     * manteniendo su existencia en el sistema.</p>
     *
     * @param id ID del código de barras a eliminar
     * @throws Exception Si ocurre un error durante la transacción
     * @see #recuperar(long)
     */
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
    
    /**
     * Recupera un código de barras previamente eliminado (soft delete).
     * 
     * <p><b>Validación previa:</b> Verifica que el código de barras no esté activo
     * antes de intentar la recuperación para evitar inconsistencias en el estado.</p>
     * 
     * <p><b>Impacto en la relación 1→1:</b> Al recuperar un código de barras,
     * los productos que previamente lo referenciaban pueden volver a asociarse
     * mediante el método correspondiente en ProductoService.</p>
     *
     * @param id ID del código de barras a recuperar
     * @throws IllegalArgumentException Si el código de barras no está eliminado
     * @throws Exception Si ocurre un error durante la transacción
     * @see #eliminar(long)
     */
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
    
    /**
     * Obtiene un código de barras por su ID.
     * 
     * <p><b>Nota:</b> Este método solo retorna códigos de barras activos
     * (no eliminados). Para consultar incluyendo eliminados, use el DAO directamente.</p>
     *
     * @param id ID del código de barras a buscar
     * @return El código de barras encontrado o {@code null} si no existe o está eliminado
     * @throws Exception Si ocurre un error en el acceso a datos
     */
    @Override
    public CodigoBarras getById(long id) throws Exception {
        return codigoBarrasDAO.getById(id);
    }

    /**
     * Obtiene todos los códigos de barras activos del sistema.
     * 
     * @return Lista de códigos de barras activos. Lista vacía si no hay códigos
     * @throws Exception Si ocurre un error en el acceso a datos
     */
    @Override
    public List<CodigoBarras> getAll() throws Exception {
        return codigoBarrasDAO.getAll();
    }

    /**
     * Busca un código de barras por su valor exacto.
     * 
     * <p><b>Búsqueda exacta:</b> Este método realiza una búsqueda por coincidencia
     * exacta del valor. Es utilizado principalmente para validar la unicidad
     * durante inserciones y actualizaciones.</p>
     * 
     * <p><b>Uso típico:</b>
     * <pre>
     * {@code
     * // Validar si un valor ya existe
     * CodigoBarras existente = codigoBarrasService.getByValor("1234567890123");
     * if (existente != null) {
     *     throw new IllegalArgumentException("El código de barras ya existe");
     * }
     * }
     * </pre>
     * </p>
     *
     * @param valor Valor exacto del código de barras a buscar
     * @return El código de barras encontrado o {@code null} si no existe
     * @throws Exception Si ocurre un error en el acceso a datos
     */
    public CodigoBarras getByValor(String valor) throws Exception {
        return codigoBarrasDAO.getByValor(valor);
    }
}