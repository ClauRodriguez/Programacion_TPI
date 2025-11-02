package model;

import java.util.Date;

public class CodigoBarras {
    private int id;
    private String tipo;
    private String valor;
    private String observaciones;
    private Date fechaAsignacion;

    public CodigoBarras() {}

    // ctor sin id (para INSERT auto-incremental)
    public CodigoBarras(String tipo, String valor, Date fechaAsignacion) {
        this.tipo = tipo;
        this.valor = valor;
        this.fechaAsignacion = fechaAsignacion;
    }

    // ctor con id (para lecturas desde DB)
    public CodigoBarras(int id, String tipo, String valor, Date fechaAsignacion) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.fechaAsignacion = fechaAsignacion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Date getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(Date fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    @Override
    public String toString() {
        return "CodigoBarras [id=" + id + ", tipo=" + tipo + ", valor=" + valor +
               ", fechaAsignacion=" + fechaAsignacion + "]";
    }
}