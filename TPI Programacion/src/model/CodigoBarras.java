
package model;

import java.time.LocalDate;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
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

    public CodigoBarras(long id, boolean eliminado, EnumTipo tipo, String valor, LocalDate fechaAsignacion, String observaciones) {
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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Misma referencia en memoria
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Objeto null o de clase diferente
        }
        
        CodigoBarras codigo = (CodigoBarras) obj;
        
        // Comparar por ID si ambos están persistidos
        if (this.getId() > 0 && codigo.getId() > 0) {
            return this.getId() == codigo.getId();
        }
        
        // Si alguno no tiene ID, comparar por valor (campo UNIQUE)
        if (valor == null) {
            if (codigo.valor != null) {
                return false;
            }
        } else if (!valor.equals(codigo.valor)) {
            return false;
        }
        
        // También comparar tipo para mayor seguridad
        if (tipo != codigo.tipo) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        // Si tiene ID, usar ID para hash
        if (this.getId() > 0) {
            return Long.hashCode(this.getId());
        }
        
        // Si no tiene ID, usar valor (campo UNIQUE) para hash
        result = prime * result + ((valor == null) ? 0 : valor.hashCode());
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
        
        return result;
    }
    
}
