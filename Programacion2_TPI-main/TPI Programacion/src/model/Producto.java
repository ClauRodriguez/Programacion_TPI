
package model;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */

public class Producto extends Base {
    
// nombre 		String 			NOT NULL, máx. 120 
// marca 		String 			máx. 80 
// categoria 		String 			máx. 80
// precio 		double			NOT NULL, escala sugerida  (10,2) 
// peso 		Double                  opcional, (10,3) 
// codigoBarras  	CodigoBarras 		Referencia 1→1 a B 

    // IMPORTANTE LAS VALIDACIONES SE HACEN EN LA CAPA DE SERVICIO. NO EN EL FRONT.
    
// ATRIBUTOS
    
    private String nombre;
    private String marca;
    private double precio;
    private double peso;
    private int stock;
    private CategoriaProducto categoria;
    private CodigoBarras codigoBarras;

// CONSTRUCTORES 
    
    public Producto(String nombre, String marca, double precio, double peso, int stock, int id) {
        super(id, false);
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.peso = peso;
        this.stock = stock;
       // this.categoria = categoria;
       // this.codigoBarras = codigoBarras;
    }
    
    public Producto() { // Constructor por defecto para crear un producto nuevo sin ID.
        super();
    }
    
// GETTERS
    
    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public double getPeso() {
        return peso;
    }

    public int getStock() {
        return stock;
    }

    public CodigoBarras getCodigoBarras() {
        return codigoBarras;
    }
    
        
// SETTERS
    
    /**
     * Establece el nombre del producto.
     * Validación: ProductoServiceImpl verifica que no esté vacío. (FALTA DESARROLAR ESTO)
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

     /**
     * Establece la marca del producto.
     * Validación: ProductoServiceImpl máx. 80 (FALTA DESARROLAR)
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Establece la categoria del producto.
     * 
     * @param categoria La categoría a asignar al producto
     */
    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }

    /**
     * Establece el precio del producto. 
     * Validación: ProductoServiceImpl verifica que no esté vacío y que escala sugerida sea (10,2). (FALTA DESARROLAR ESTO)
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    /**
     * Establece el precio del producto. 
     * Validación: ProductoServiceImpl puede ser opcional, pero si se completa verifica que escala sugerida sea (10,3). (FALTA DESARROLAR ESTO)
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Establece el stock del producto. 
     * Validación: ProductoServiceImpl (FALTA DESARROLAR ESTO)
     */
   public void setStock(int stock) {
    this.stock = stock;
}

     /**
     * Setea la asociación unidireccional del codigo de barras.
     * (HACE FALTA DESARROLAR ESTO De OTRA MANERA MÁS COMPLEJA???)
     */
    public void setCodigoBarras(CodigoBarras codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
    
// METODOS

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(".".repeat(40));
        sb.append("\nPRODUCTO: ");
        sb.append("\n - ID: ").append(getId());
        sb.append("\n - Nombre: ").append(nombre);
        sb.append("\n - Marca: ").append(marca != null ? marca : "N/A");
        sb.append("\n - Categoria: ").append(categoria != null ? categoria : "N/A");
        sb.append("\n - Precio: ").append(precio);
        sb.append("\n - Peso: ").append(peso);
        sb.append("\n - Stock: ").append(stock);
        
        // Mostrar código de barras solo si existe
        if (codigoBarras != null) {
            sb.append(codigoBarras.toString());
        } else {
            sb.append("\n---\nCódigo de barras: No asignado");
        }
        
        return sb.toString();
    }
    
}
