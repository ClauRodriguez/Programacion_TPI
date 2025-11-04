
package model;

import java.time.LocalDate;
import java.util.Scanner; // REVISAR COMO IMPORTAR UNA SOLA VEZ SCANNER EN LA CLASE PRINCIPAL



/**
 * @author Hernán E. Bula
 */

public class CodigoBarras extends Base {
    
    Scanner scanner = new Scanner(System.in); // REVISAR COMO IMPORTAR UNA SOLA VEZ SCANNER EN LA CLASE PRINCIPAL

    
// tipo             Enum {EAN13, EAN8, UPC} 	NOT NULL 
// valor            String 			NOT NULL, UNIQUE, máx. 20 
// fechaAsignacion  java.time.LocalDate
// observaciones     String 			máx. 255

    private EnumTipo tipo;
    private String valor;
    private LocalDate fechaAsignacion;
    private String observaciones;

    public CodigoBarras(int id, boolean eliminado, EnumTipo tipo, String valor, LocalDate fechaAsignacion, String observaciones) {
        super(id, eliminado);
        this.tipo = tipo;
        this.valor = valor;
        this.fechaAsignacion = fechaAsignacion;
        this.observaciones = observaciones;
    }
    public CodigoBarras() {
    }
    
    
// GETTERS
    
    public EnumTipo getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public String getObservaciones() {
        return observaciones;
    }
    
// SETTERS
    
    /**
     * Establece el tipo de Codigo de Barras.
     * Validación: CodigoServiceImpl verifica que no esté vacío. 
     */
public void setTipo(EnumTipo tipo) {
    this.tipo = tipo;
}
    
    /**
     * Establece el valor del Codigo de Barras.
     * Validación: CodigoServiceImpl verifica que NOT NULL, UNIQUE, máx. 20 
     * (FALTA DESARROLAR ESTO)
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
    
    /**
     * Establece la fecha y hora del producto.
     */
    public void setFechaAsignacion(int aaaa, int mm, int dd) {
        this.fechaAsignacion = fechaAsignacion.of(aaaa, mm, dd);
    }

     /**
     * Establece las observaciones del código de barras.
     * Validación: máx. 255 caracteres.
     * (FALTA DESARROLAR ¿Esto es máximo de caracteres o de marcas?)
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
// METODOS

    @Override
    public String toString() {
        return "\n---\nCódigo de barras:\n - ID: " + getId() + "\n - Tipo: " + tipo + "\n - Valor: " + valor + "\n - Fecha de asignacion: " + fechaAsignacion + "\n - Observaciones: " + observaciones;
    }
    
}
