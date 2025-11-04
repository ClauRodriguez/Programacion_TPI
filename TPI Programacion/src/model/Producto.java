
package model;

import java.util.Scanner; // REVISAR COMO IMPORTAR UNA SOLA VEZ SCANNER EN LA CLASE PRINCIPAL

/**
 * @author Hernán E. Bula
 */

public class Producto extends Base {
    
    Scanner scanner = new Scanner(System.in); // REVISAR COMO IMPORTAR UNA SOLA VEZ SCANNER EN LA CLASE PRINCIPAL
    

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
     * Validación: ProductoServiceImpl máx. 80 (FALTA DESARROLAR Si lo hacemos con NUM hace falta validarlo?)
     */
    public void setCategoria() {
        CategoriaProducto[] categorias = CategoriaProducto.values();
        int i = 0;
        System.out.println("Seleccione una opción para la categoría: ");
        for (CategoriaProducto categoria : categorias) {
            System.out.println((i+1) + "). " + categoria);
            i++;
        }
        int opcion = Integer.parseInt(scanner.nextLine());

        if (opcion >= 0 && opcion < categorias.length) {
            this.categoria = categorias[opcion];
        } else {
            throw new IllegalArgumentException(
                    "La opción elegida (" + opcion + ") está fuera de rango. Debe ingresar una entre 1 y " + (categorias.length)
            );
        }
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
        return "\n" + ".".repeat(40) + "\nPRODUCTO: \n - ID: " + getId() + "\n - Nombre: " + nombre + "\n - Marca: " + marca + "\n - Categoria: " + categoria + "\n - Precio: " + precio + "\n - Peso: " + peso + "\n - Stock: " + stock + codigoBarras;
    }
    
}
