
package model;

/**
 * @author Hernán E. Bula
 */

public class Producto extends Base {
    
    private String nombre;
    private String marca;
    private double precio;
    private double peso;
    private int stock;
    private CategoriaProducto categoria;
    private CodigoBarras codigoBarras;
    
    public Producto(String nombre, String marca, double precio, double peso, int stock, long id) {
        super(id, false);
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.peso = peso;
        this.stock = stock;
    }
    
    public Producto() {
        super();
    }
    
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
        
        if (codigoBarras != null) {
            sb.append(codigoBarras.toString());
        } else {
            sb.append("\n---\nCódigo de barras: No asignado");
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        Producto producto = (Producto) obj;
        
        if (this.getId() > 0 && producto.getId() > 0) {
            return this.getId() == producto.getId();
        }
        
        if (Double.compare(producto.precio, precio) != 0) {
            return false;
        }
        if (stock != producto.stock) {
            return false;
        }
        if (nombre == null) {
            if (producto.nombre != null) {
                return false;
            }
        } else if (!nombre.equals(producto.nombre)) {
            return false;
        }
        if (marca == null) {
            if (producto.marca != null) {
                return false;
            }
        } else if (!marca.equals(producto.marca)) {
            return false;
        }
        if (categoria != producto.categoria) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        if (this.getId() > 0) {
            return Long.hashCode(this.getId());
        }
        
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((marca == null) ? 0 : marca.hashCode());
        result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
        long temp = Double.doubleToLongBits(precio);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + stock;
        
        return result;
    }
    
}
