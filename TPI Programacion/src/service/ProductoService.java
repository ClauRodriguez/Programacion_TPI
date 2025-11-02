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
        productoDAO.insertar(entidad);
    }

    @Override
    public void actualizar(Producto entidad) throws Exception {
        productoDAO.actualizar(entidad);
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