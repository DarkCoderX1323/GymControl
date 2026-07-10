package com.utp.gymcontrol.dao;

import com.utp.gymcontrol.database.ConexionDB;
import com.utp.gymcontrol.model.Membresia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembresiaDAO {

    public boolean registrarMembresia(Membresia membresia) {

        String sql =
                "INSERT INTO membresia " +
                        "(socio_id,tipo,fecha_inicio,fecha_fin,estado,tipo_membresia_id) " +
                        "VALUES (?,?,?,?,?,?)";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt =
                    conn.prepareStatement(sql)) {

            stmt.setInt(1,
                    membresia.getSocioId());

            stmt.setString(2,
                    membresia.getTipo());

            stmt.setDate(3,
                    Date.valueOf(
                            membresia.getFechaInicio()
                    ));

            stmt.setDate(4,
                    Date.valueOf(
                            membresia.getFechaFin()
                    ));

            stmt.setString(5,
                    membresia.getEstado());

            stmt.setInt(6,
                    membresia.getTipoMembresiaId());

            return stmt.executeUpdate() > 0;

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public List<Membresia> obtenerMembresias() {

        List<Membresia> lista =
                new ArrayList<>();

        String sql =
                "SELECT * FROM membresia";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt =
                    conn.prepareStatement(sql);
            ResultSet rs =
                    stmt.executeQuery()) {

            while(rs.next()) {

                Membresia m =
                        new Membresia();

                m.setId(
                        rs.getInt("id")
                );

                m.setSocioId(
                        rs.getInt("socio_id")
                );

                m.setTipo(
                        rs.getString("tipo")
                );

                m.setFechaInicio(
                        rs.getDate("fecha_inicio")
                                .toLocalDate()
                );

                m.setFechaFin(
                        rs.getDate("fecha_fin")
                                .toLocalDate()
                );

                m.setEstado(
                        rs.getString("estado")
                );

                m.setTipoMembresiaId(
                        rs.getInt("tipo_membresia_id")
                );

                lista.add(m);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public int actualizarMembresiasVencidas() {

        String sql =
                "UPDATE membresia " +
                        "SET estado='vencida' " +
                        "WHERE fecha_fin < CURDATE() " +
                        "AND estado='activa'";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt =
                    conn.prepareStatement(sql)) {

            return stmt.executeUpdate();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    public boolean eliminarMembresia(int id) {

        String sql =
                "DELETE FROM membresia WHERE id=?";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt =
                    conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}
