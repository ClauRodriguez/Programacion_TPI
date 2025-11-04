/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 
package model;

/**
 *
 * @author hcoceres

public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private String marca;
    private String categoria;
    private double peso;
    private long codigoBarrasId;

    public Producto() {}

    public Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public long getCodigoBarrasId() { return codigoBarrasId; }
    public void setCodigoBarrasId(long codigoBarrasId) { this.codigoBarrasId = codigoBarrasId; }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + "]";
    }
}
*/