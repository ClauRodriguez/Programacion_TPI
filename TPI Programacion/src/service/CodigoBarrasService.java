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
        validarCodigoBarras(entidad);
        
        // Validar UNIQUE: verificar si ya existe un código con el mismo valor
        CodigoBarras existente = codigoBarrasDAO.getByValor(entidad.getValor());
        if (existente != null && !existente.isEliminado()) {
            throw new IllegalArgumentException("Ya existe un código de barras con el valor: " + entidad.getValor());
        }
        
        codigoBarrasDAO.insertar(entidad);
    }

    @Override
    public void actualizar(CodigoBarras entidad) throws Exception {
        validarCodigoBarras(entidad);
        
        // Validar UNIQUE solo si cambió el valor
        CodigoBarras existente = codigoBarrasDAO.getById(entidad.getId());
        if (existente != null && !existente.getValor().equals(entidad.getValor())) {
            // El valor cambió, verificar que no exista otro con el nuevo valor
            CodigoBarras otroConMismoValor = codigoBarrasDAO.getByValor(entidad.getValor());
            if (otroConMismoValor != null && !otroConMismoValor.isEliminado() && 
                otroConMismoValor.getId() != entidad.getId()) {
                throw new IllegalArgumentException("Ya existe otro código de barras con el valor: " + entidad.getValor());
            }
        }
        
        codigoBarrasDAO.actualizar(entidad);
    }
    
    /**
     * Valida todas las reglas de negocio para un CodigoBarras.
     * @param codigo CodigoBarras a validar
     * @throws IllegalArgumentException Si alguna validación falla
     */
    private void validarCodigoBarras(CodigoBarras codigo) throws IllegalArgumentException {
        // Validar tipo
        if (codigo.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de código de barras no puede ser null.");
        }
        
        // Validar valor
        if (codigo.getValor() == null || codigo.getValor().trim().isEmpty()) {
            throw new IllegalArgumentException("El valor del código de barras no puede estar vacío.");
        }
        if (codigo.getValor().length() > 20) {
            throw new IllegalArgumentException("El valor del código de barras no puede tener más de 20 caracteres.");
        }
        
        // Validar observaciones
        if (codigo.getObservaciones() != null && codigo.getObservaciones().length() > 255) {
            throw new IllegalArgumentException("Las observaciones no pueden tener más de 255 caracteres.");
        }
        
        // Validar fecha de asignación
        if (codigo.getFechaAsignacion() == null) {
            throw new IllegalArgumentException("La fecha de asignación no puede ser null.");
        }
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