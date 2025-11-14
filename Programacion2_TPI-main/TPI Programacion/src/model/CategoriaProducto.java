package model;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */
public enum CategoriaProducto {
    ALIMENTOS("Productos comestibles"),
    BEBIDAS("Bebidas y líquidos"),
    ELECTRODOMESTICOS("Dispositivos electrónicos"),
    FERRETERIA("Materiales de ferretería y construcción"),
    LIMPIEZA("Productos de limpieza y hogar");

    private final String descripcion;

    CategoriaProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    public void imprimirCategorias() { 
        System.out.println(CategoriaProducto.values());
    }
   

}
