package model;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */

/**
 * Enum que representa las categorías de productos disponibles en el sistema.
 * Cada categoría tiene una descripción asociada.
 */
public enum CategoriaProducto {
    ALIMENTOS("Productos comestibles"),
    BEBIDAS("Bebidas y líquidos"),
    ELECTRODOMESTICOS("Dispositivos electrónicos"),
    FERRETERIA("Materiales de ferretería y construcción"),
    LIMPIEZA("Productos de limpieza y hogar");

    private final String descripcion;

    /**
     * Constructor del enum CategoriaProducto.
     * 
     * @param descripcion Descripción textual de la categoría
     */
    CategoriaProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción de la categoría.
     * 
     * @return Descripción textual de la categoría
     */
    public String getDescripcion() {
        return descripcion;
    }
}
