package com.utp.gymcontrol.dao;

import com.utp.gymcontrol.database.ConexionDB;
import com.utp.gymcontrol.model.TipoMembresia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoMembresiaDAO {

    public List<TipoMembresia> obtenerTiposMembresia() {

        List<TipoMembresia> lista = new ArrayList<>();

        String sql =
                "SELECT * FROM tipo_membresia " +
                "WHERE estado='activo'";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                TipoMembresia tipo =
                        new TipoMembresia();

                tipo.setId(
                        rs.getInt("id")
                );

                tipo.setNombre(
                        rs.getString("nombre")
                );

                tipo.setDuracionDias(
                        rs.getInt("duracion_dias")
                );

                tipo.setPrecio(
                        rs.getDouble("precio")
                );

                tipo.setEstado(
                        rs.getString("estado")
                );

                lista.add(tipo);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al obtener tipos de membresía"
            );

            e.printStackTrace();
        }

        return lista;
    }

    public TipoMembresia buscarPorId(int id) {

        String sql =
                "SELECT * FROM tipo_membresia WHERE id=?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    TipoMembresia tipo =
                            new TipoMembresia();

                    tipo.setId(rs.getInt("id"));

                    tipo.setNombre(
                            rs.getString("nombre")
                    );

                    tipo.setDuracionDias(
                            rs.getInt("duracion_dias")
                    );

                    tipo.setPrecio(
                            rs.getDouble("precio")
                    );

                    tipo.setEstado(
                            rs.getString("estado")
                    );

                    return tipo;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }
}