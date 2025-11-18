
package model;

import java.time.LocalDate;

/**
 * @authors 
 * Gaston Alberto Cejas, 
 * Hernan Cóceres, 
 * Claudio Rodriguez, 
 * Hernan E.Bula
 */

/**
 * Clase que representa un código de barras asociado a un producto.
 * Extiende de Base para heredar funcionalidad de eliminación lógica e ID.
 */
public class CodigoBarras extends Base {
    
    private EnumTipo tipo;
    private String valor;
    private LocalDate fechaAsignacion;
    private String observaciones;

    /**
     * Constructor completo de CodigoBarras.
     * 
     * @param id ID único del código de barras
     * @param eliminado Indica si el código está marcado como eliminado
     * @param tipo Tipo de código de barras (EAN13, EAN8, UPC)
     * @param valor Valor del código de barras
     * @param fechaAsignacion Fecha en que se asignó el código
     * @param observaciones Observaciones adicionales sobre el código
     */
    public CodigoBarras(long id, boolean eliminado, EnumTipo tipo, String valor, LocalDate fechaAsignacion, String observaciones) {
        super(id, eliminado);
        this.tipo = tipo;
        this.valor = valor;
        this.fechaAsignacion = fechaAsignacion;
        this.observaciones = observaciones;
    }
    public CodigoBarras() {
    }
    
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

    /**
     * Devuelve una representación en cadena del código de barras.
     * 
     * @return String con la información del código de barras
     */
    @Override
    public String toString() {
        String obsTexto = (observaciones != null && !observaciones.trim().isEmpty()) 
            ? observaciones 
            : "(sin observaciones)";
        return "\n---\nCódigo de barras:\n - ID: " + getId() + "\n - Tipo: " + tipo + "\n - Valor: " + valor + "\n - Fecha de asignacion: " + fechaAsignacion + "\n - Observaciones: " + obsTexto;
    }
    
    /**
     * Compara este código de barras con otro objeto para determinar igualdad.
     * Dos códigos son iguales si tienen el mismo ID (si ambos tienen ID > 0),
     * o si tienen el mismo valor y tipo.
     * 
     * @param obj Objeto a comparar
     * @return true si los objetos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        CodigoBarras codigo = (CodigoBarras) obj;
        
        if (this.getId() > 0 && codigo.getId() > 0) {
            return this.getId() == codigo.getId();
        }
        
        if (valor == null) {
            if (codigo.valor != null) {
                return false;
            }
        } else if (!valor.equals(codigo.valor)) {
            return false;
        }
        
        if (tipo != codigo.tipo) {
            return false;
        }
        
        return true;
    }

    /**
     * Calcula el código hash del código de barras.
     * @return Código hash del objeto
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        if (this.getId() > 0) {
            return Long.hashCode(this.getId());
        }
        
        result = prime * result + ((valor == null) ? 0 : valor.hashCode());
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
        
        return result;
    }
    
}
