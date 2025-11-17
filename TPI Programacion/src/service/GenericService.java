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

import java.util.List;

/**
 * Interfaz genérica que define las operaciones CRUD básicas para los servicios del sistema.
 * 
 * <p>Esta interfaz establece el contrato que deben cumplir todos los servicios
 * de entidades, proporcionando una API consistente para las operaciones básicas
 * de persistencia y recuperación de datos.</p>
 * 
 * <p><b>Patrón de diseño:</b> Generic Service Pattern - Define una interfaz
 * común para todos los servicios, promoviendo la consistencia y reduciendo
 * la duplicación de código en las operaciones CRUD.</p>
 * 
 * <p><b>Ventajas de esta aproximación:</b>
 * <ul>
 *   <li>Unificación de operaciones básicas en toda la aplicación</li>
 *   <li>Facilita la implementación de servicios nuevos</li>
 *   <li>Permite el uso de polimorfismo en el manejo de servicios</li>
 *   <li>Establece un estándar para la capa de servicio</li>
 * </ul>
 * </p>
 * 
 * <p><b>Uso esperado:</b>
 * <pre>
 * {@code
 * public class ProductoService implements GenericService<Producto> {
 *     // Implementación específica para Producto
 * }
 * }
 * </pre>
 * </p>
 * 
 * @param <T> El tipo de entidad que manejará el servicio
 * 
 * @author Gaston Alberto Cejas
 * @author Hernan Cóceres
 * @author Claudio Rodriguez  
 * @author Hernan E. Bula
 * @version 1.0
 * @since 2025
 */
public interface GenericService<T> {
    
    /**
     * Inserta una nueva entidad en el sistema.
     * 
     * <p><b>Responsabilidades de la implementación:</b>
     * <ul>
     *   <li>Validar las reglas de negocio de la entidad</li>
     *   <li>Manejar transacciones si es necesario</li>
     *   <li>Persistir la entidad mediante el DAO correspondiente</li>
     *   <li>Garantizar la integridad de los datos</li>
     * </ul>
     * </p>
     *
     * @param entidad La entidad a insertar. No debe ser {@code null}
     * @throws IllegalArgumentException Si la entidad no pasa las validaciones de negocio
     * @throws Exception Si ocurre un error durante la operación de inserción
     */
    void insertar(T entidad) throws Exception;
    
    /**
     * Actualiza una entidad existente en el sistema.
     * 
     * <p><b>Nota:</b> Las implementaciones deben validar que la entidad
     * exista antes de intentar la actualización y aplicar todas las
     * validaciones de negocio pertinentes.</p>
     *
     * @param entidad La entidad con los datos actualizados. No debe ser {@code null}
     * @throws IllegalArgumentException Si la entidad no existe o no pasa las validaciones
     * @throws Exception Si ocurre un error durante la operación de actualización
     */
    void actualizar(T entidad) throws Exception;
    
    /**
     * Elimina una entidad del sistema basado en su ID.
     * 
     * <p><b>Nota sobre eliminación:</b> La implementación puede ser física o lógica.
     * En este sistema se recomienda implementar eliminación lógica (soft delete)
     * mediante un campo {@code eliminado} para preservar la integridad referencial
     * y el historial de datos.</p>
     *
     * @param id El ID de la entidad a eliminar
     * @throws IllegalArgumentException Si no existe una entidad con el ID proporcionado
     * @throws Exception Si ocurre un error durante la operación de eliminación
     */
    void eliminar(long id) throws Exception;
    
    /**
     * Obtiene una entidad por su ID.
     * 
     * <p><b>Comportamiento esperado:</b> Este método debe retornar {@code null}
     * si no se encuentra ninguna entidad con el ID proporcionado o si la entidad
     * está marcada como eliminada (en caso de usar soft delete).</p>
     *
     * @param id El ID de la entidad a buscar
     * @return La entidad encontrada o {@code null} si no existe
     * @throws Exception Si ocurre un error durante la operación de consulta
     */
    T getById(long id) throws Exception;
    
    /**
     * Obtiene todas las entidades del sistema.
     * 
     * <p><b>Filtrado:</b> En caso de usar eliminación lógica, este método
     * debe retornar solo las entidades que no estén marcadas como eliminadas.</p>
     * 
     * <p><b>Rendimiento:</b> Para conjuntos de datos grandes, considere
     * implementar métodos adicionales con paginación.</p>
     *
     * @return Una lista con todas las entidades activas. Lista vacía si no hay entidades
     * @throws Exception Si ocurre un error durante la operación de consulta
     */
    List<T> getAll() throws Exception;
}