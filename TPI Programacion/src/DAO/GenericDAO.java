
package DAO;
/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */
import java.util.List;

public interface GenericDAO<T> {
        /**
     * Inserta una nueva entidad en la fuente de datos.
     *
     * @param entidad entidad a insertar. No debe ser {@code null}.
     * @throws Exception si ocurre un error al acceder a la fuente de datos.
     */
    void insertar(T entidad) throws Exception;
        /**
     * Actualiza una entidad existente en la fuente de datos.
     *
     * @param entidad entidad a actualizar. Debe tener un identificador válido.
     * @throws Exception si ocurre un error al acceder a la fuente de datos.
     */
    void actualizar(T entidad) throws Exception;
       /**
     * Marca como eliminada o elimina una entidad según su identificador.
     *
     * @param id identificador de la entidad a eliminar.
     * @throws Exception si ocurre un error al acceder a la fuente de datos.
     */
    void eliminar(long id) throws Exception;
        /**
     * Obtiene una entidad por su identificador.
     *
     * @param id identificador de la entidad.
     * @return la entidad encontrada o {@code null} si no existe.
     * @throws Exception si ocurre un error al acceder a la fuente de datos.
     */
    T getById(long id) throws Exception;
        /**
     * Obtiene todas las entidades de la fuente de datos.
     *
     * @return lista de entidades. Nunca debe ser {@code null}.
     * @throws Exception si ocurre un error al acceder a la fuente de datos.
     */
    List<T> getAll() throws Exception;
}