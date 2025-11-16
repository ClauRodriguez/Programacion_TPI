
package model;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */

/**
 * Clase base abstracta para extender al resto de entidades del sistema (id, eliminado).
 * Implementa eliminación lógica mediante un booleano.
 */

public abstract class Base {

    private long id;
    
    private boolean eliminado;
    
    protected Base() {
        this.eliminado = false;
    }
    
    protected Base(long id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    /**
     * Obtiene el ID de la entidad.
     * @return ID de la entidad, 0 si aún no ha sido persistida
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el ID de la entidad.
     * Típicamente llamado por el DAO después de insertar en la BD.
     *
     * @param id Nuevo ID de la entidad
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Verifica si la entidad está marcada como eliminada.
     * @return true si está eliminada, false si está activa
     */
    public boolean isEliminado() {
        return eliminado;
    }

    /**
     * Marca o desmarca la entidad como eliminada.
     * En el contexto del soft delete, esto se usa para "eliminar" sin borrar físicamente.
     *
     * @param eliminado true para marcar como eliminada, false para reactivar
     */
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}

