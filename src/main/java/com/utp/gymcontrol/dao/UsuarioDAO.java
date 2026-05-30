package com.utp.gymcontrol.dao;

import com.utp.gymcontrol.database.ConexionDB;
import com.utp.gymcontrol.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario login(String username, String password) {

        String sql = "SELECT * FROM usuario " +
                     "WHERE username=? AND password=? " +
                     "AND estado='activo'";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEstado(rs.getString("estado"));

                return usuario;
            }

        } catch (SQLException e) {

            System.out.println("Error en login:");
            e.printStackTrace();
        }

        return null;
    }
}