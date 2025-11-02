/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.DatabaseConnection;
import model.CodigoBarras;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CodigoBarrasDAO implements GenericDAO<CodigoBarras> {

    @Override
    public void insertar(CodigoBarras entidad) throws Exception {
        String sql = "INSERT INTO codigo_barras (tipo, valor, fecha_asignacion) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entidad.getTipo());
            stmt.setString(2, entidad.getValor());
            stmt.setDate(3, new java.sql.Date(entidad.getFechaAsignacion().getTime()));
            stmt.executeUpdate();
        }
    }

    @Override
    public void actualizar(CodigoBarras entidad) throws Exception {
        String sql = "UPDATE codigo_barras SET tipo = ?, valor = ?, fecha_asignacion = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entidad.getTipo());
            stmt.setString(2, entidad.getValor());
            stmt.setDate(3, new java.sql.Date(entidad.getFechaAsignacion().getTime()));
            stmt.setInt(4, entidad.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM codigo_barras WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public CodigoBarras getById(int id) throws Exception {
        String sql = "SELECT * FROM codigo_barras WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CodigoBarras(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("valor"),
                    rs.getDate("fecha_asignacion")
                );
            }
            return null;
        }
    }

    @Override
    public List<CodigoBarras> getAll() throws Exception {
        List<CodigoBarras> lista = new ArrayList<>();
        String sql = "SELECT * FROM codigo_barras";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new CodigoBarras(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("valor"),
                    rs.getDate("fecha_asignacion")
                ));
            }
        }
        return lista;
    }

    public CodigoBarras getByValor(String valor) throws Exception {
        String sql = "SELECT * FROM codigo_barras WHERE valor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valor);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CodigoBarras(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("valor"),
                    rs.getDate("fecha_asignacion")
                );
            }
            return null;
        }
    }
}