/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.ProductoDAO;
import model.Producto;
import java.util.List;

// Interfaz + implementación unificadas
public class ProductoService implements GenericService<Producto> {

    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    public void insertar(Producto entidad) throws Exception {
        validarProducto(entidad);
        productoDAO.insertar(entidad);
    }

    @Override
    public void actualizar(Producto entidad) throws Exception {
        validarProducto(entidad);
        productoDAO.actualizar(entidad);
    }
    
    /**
     * Valida todas las reglas de negocio para un Producto.
     * @param producto Producto a validar
     * @throws IllegalArgumentException Si alguna validación falla
     */
    private void validarProducto(Producto producto) throws IllegalArgumentException {
        // Validar nombre
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (producto.getNombre().length() > 120) {
            throw new IllegalArgumentException("El nombre no puede tener más de 120 caracteres.");
        }
        
        // Validar marca
        if (producto.getMarca() != null && producto.getMarca().length() > 80) {
            throw new IllegalArgumentException("La marca no puede tener más de 80 caracteres.");
        }
        
        // Validar precio
        if (producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0.");
        }
        if (producto.getPrecio() > 99999999.99) {
            throw new IllegalArgumentException("El precio no puede ser mayor a 99,999,999.99");
        }
        
        // Validar peso (opcional, pero si existe debe ser >= 0)
        if (producto.getPeso() < 0) {
            throw new IllegalArgumentException("El peso no puede ser negativo.");
        }
        if (producto.getPeso() > 9999999.999) {
            throw new IllegalArgumentException("El peso no puede ser mayor a 9,999,999.999");
        }
        
        // Validar stock
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        productoDAO.eliminar(id);
    }

    @Override
    public Producto getById(int id) throws Exception {
        return productoDAO.getById(id);
    }

    @Override
    public List<Producto> getAll() throws Exception {
        return productoDAO.getAll();
    }

    public Producto getByNombre(String nombre) throws Exception {
        return productoDAO.getByNombre(nombre);
    }
    
}