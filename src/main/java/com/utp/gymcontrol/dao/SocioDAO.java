package com.utp.gymcontrol.dao;

import com.utp.gymcontrol.database.ConexionDB;
import com.utp.gymcontrol.model.Socio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO {
    public boolean registrarSocio(Socio socio) {

        String sql = "INSERT INTO socio(nombre, dni, telefono, email, estado) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, socio.getNombre());
            stmt.setString(2, socio.getDni());
            stmt.setString(3, socio.getTelefono());
            stmt.setString(4, socio.getEmail());
            stmt.setString(5, socio.getEstado());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al registrar socio:");
            e.printStackTrace();

            return false;
        }

    }
    public List<Socio> obtenerSocios() {

        List<Socio> listaSocios = new ArrayList<>();

        String sql = "SELECT * FROM socio";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Socio socio = new Socio();

                socio.setId(rs.getInt("id"));
                socio.setNombre(rs.getString("nombre"));
                socio.setDni(rs.getString("dni"));
                socio.setTelefono(rs.getString("telefono"));
                socio.setEmail(rs.getString("email"));

                Timestamp timestamp = rs.getTimestamp("fecha_registro");

                if (timestamp != null) {
                    socio.setFechaRegistro(
                            timestamp.toLocalDateTime()
                    );
                }

                socio.setEstado(rs.getString("estado"));

                listaSocios.add(socio);
            }

        } catch (SQLException e) {

            System.out.println("Error al obtener socios:");
            e.printStackTrace();
        }

        return listaSocios;
    }
    public Socio buscarPorDni(String dni) {

        String sql = "SELECT * FROM socio WHERE dni = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Socio socio = new Socio();

                    socio.setId(rs.getInt("id"));
                    socio.setNombre(rs.getString("nombre"));
                    socio.setDni(rs.getString("dni"));
                    socio.setTelefono(rs.getString("telefono"));
                    socio.setEmail(rs.getString("email"));
                    socio.setEstado(rs.getString("estado"));

                    return socio;
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al buscar socio:");
            e.printStackTrace();
        }

        return null;
    }
    public Socio buscarPorId(int id) {

        String sql = "SELECT * FROM socio WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Socio socio = new Socio();

                    socio.setId(rs.getInt("id"));
                    socio.setNombre(rs.getString("nombre"));
                    socio.setDni(rs.getString("dni"));
                    socio.setTelefono(rs.getString("telefono"));
                    socio.setEmail(rs.getString("email"));
                    socio.setEstado(rs.getString("estado"));

                    return socio;
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al buscar socio por ID:");
            e.printStackTrace();
        }

        return null;
    }
    // =========================
    // FILTROS COMBINADOS
    // =========================

    /**
     * Filtra socios combinando nombre (parcial), DNI (parcial) y estado.
     * Cualquier parámetro puede venir vacío/null para omitir ese criterio;
     * "estado" también acepta null/"" o "todos" para no filtrar por estado.
     */
    public List<Socio> filtrarSocios(String nombre, String dni, String estado) {

        List<Socio> listaSocios = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM socio WHERE 1=1"
        );

        List<Object> parametros = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            sql.append(" AND nombre LIKE ?");
            parametros.add("%" + nombre.trim() + "%");
        }

        if (dni != null && !dni.isBlank()) {
            sql.append(" AND dni LIKE ?");
            parametros.add("%" + dni.trim() + "%");
        }

        if (estado != null && !estado.isBlank()
                && !estado.equalsIgnoreCase("todos")) {
            sql.append(" AND estado = ?");
            parametros.add(estado.trim().toLowerCase());
        }

        sql.append(" ORDER BY nombre");

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Socio socio = new Socio();

                    socio.setId(rs.getInt("id"));
                    socio.setNombre(rs.getString("nombre"));
                    socio.setDni(rs.getString("dni"));
                    socio.setTelefono(rs.getString("telefono"));
                    socio.setEmail(rs.getString("email"));

                    Timestamp timestamp = rs.getTimestamp("fecha_registro");

                    if (timestamp != null) {
                        socio.setFechaRegistro(timestamp.toLocalDateTime());
                    }

                    socio.setEstado(rs.getString("estado"));

                    listaSocios.add(socio);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al filtrar socios:");
            e.printStackTrace();
        }

        return listaSocios;
    }

    public boolean actualizarSocio(Socio socio) {

        String sql = "UPDATE socio SET nombre=?, telefono=?, email=?, estado=? " +
                "WHERE dni=?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, socio.getNombre());
            stmt.setString(2, socio.getTelefono());
            stmt.setString(3, socio.getEmail());
            stmt.setString(4, socio.getEstado());
            stmt.setString(5, socio.getDni());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al actualizar socio:");
            e.printStackTrace();

            return false;
        }

    }
    public boolean eliminarSocio(int id) {

        String sql = "DELETE FROM socio WHERE id=?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al eliminar socio:");
            e.printStackTrace();

            return false;
        }

    }
}
