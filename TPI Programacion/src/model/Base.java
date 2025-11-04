
package tpi_p2.Models;

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

// ATRIBUTOS

    private int id; // Identificador único de la entidad. Generado automáticamente por la base de datos (AUTO_INCREMENT).
    
    private boolean eliminado; // Bandera de eliminación lógica. Mantiene integridad referencial y datos históricos
    // -> true: eliminada (no se mostrará en consultas)
    // -> false: activa
    
    
// CONSTRUCTORES
    
    protected Base() { // Constructor por defecto.
        this.eliminado = false; // Por defecto, las entidades nuevas NO están eliminadas.
        // Inicializa sin ID: debe ser asignada por DB.
    }
    
    protected Base(int id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    /**
     * Obtiene el ID de la entidad.
     * @return ID de la entidad, 0 si aún no ha sido persistida
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la entidad.
     * Típicamente llamado por el DAO después de insertar en la BD.
     *
     * @param id Nuevo ID de la entidad
     */
    public void setId(int id) {
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

