
package model;

import java.time.LocalDate;

/**
 * @author Hernán E. Bula
 */

public class CodigoBarras extends Base {
    
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
     * Establece la fecha de asignación del código de barras.
     * 
     * @param fechaAsignacion La fecha de asignación
     */
    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
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
        // Mostrar observaciones correctamente, incluso si es null
        String obsTexto = (observaciones != null && !observaciones.trim().isEmpty()) 
            ? observaciones 
            : "(sin observaciones)";
        return "\n---\nCódigo de barras:\n - ID: " + getId() + "\n - Tipo: " + tipo + "\n - Valor: " + valor + "\n - Fecha de asignacion: " + fechaAsignacion + "\n - Observaciones: " + obsTexto;
    }
    
}
