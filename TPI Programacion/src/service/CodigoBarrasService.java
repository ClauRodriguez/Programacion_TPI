/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.CodigoBarrasDAO;
import model.CodigoBarras;

import java.util.List;

public class CodigoBarrasService implements GenericService<CodigoBarras> {

    private final CodigoBarrasDAO codigoBarrasDAO = new CodigoBarrasDAO();

    @Override
    public void insertar(CodigoBarras entidad) throws Exception {
        codigoBarrasDAO.insertar(entidad);
    }

    @Override
    public void actualizar(CodigoBarras entidad) throws Exception {
        codigoBarrasDAO.actualizar(entidad);
    }

    @Override
    public void eliminar(int id) throws Exception {
        codigoBarrasDAO.eliminar(id);
    }

    @Override
    public CodigoBarras getById(int id) throws Exception {
        return codigoBarrasDAO.getById(id);
    }

    @Override
    public List<CodigoBarras> getAll() throws Exception {
        return codigoBarrasDAO.getAll();
    }

    public CodigoBarras getByValor(String valor) throws Exception {
        return codigoBarrasDAO.getByValor(valor);
    }
}