package com.utp.gymcontrol.dao;

import com.utp.gymcontrol.database.ConexionDB;
import com.utp.gymcontrol.model.Asistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsistenciaDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(AsistenciaDAO.class);

    // =========================
    // REGISTRAR
    // =========================

    /**
     * Registra la asistencia de un socio en el momento actual. La fecha y
     * hora las pone la base de datos (CURDATE()/CURTIME()) para evitar
     * desfaces con el reloj de la máquina cliente.
     */
    public boolean registrarAsistencia(int socioId) {

        String sql =
                "INSERT INTO asistencia (socio_id, fecha, hora) " +
                        "VALUES (?, CURDATE(), CURTIME())";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, socioId);

            stmt.executeUpdate();

            logger.info(
                    "Asistencia registrada. Socio ID: {}",
                    socioId
            );

            return true;

        } catch (SQLException e) {

            logger.error(
                    "Error al registrar asistencia.",
                    e
            );

            return false;
        }
    }

    // =========================
    // YA REGISTRO ASISTENCIA HOY
    // =========================

    /**
     * Indica si el socio ya tiene una asistencia registrada en la fecha
     * actual, para evitar registros duplicados el mismo día.
     */
    public boolean existeAsistenciaHoy(int socioId) {

        String sql =
                "SELECT COUNT(*) FROM asistencia " +
                        "WHERE socio_id = ? AND fecha = CURDATE()";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, socioId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error al verificar asistencia del día.",
                    e
            );
        }

        return false;
    }

    // =========================
    // OBTENER TODAS (con datos del socio)
    // =========================

    public List<Asistencia> obtenerAsistencias() {

        List<Asistencia> lista = new ArrayList<>();

        String sql =
                "SELECT\n" +
                        "    a.id,\n" +
                        "    a.socio_id,\n" +
                        "    a.fecha,\n" +
                        "    a.hora,\n" +
                        "    a.registrado_en,\n" +
                        "    s.nombre AS socio_nombre,\n" +
                        "    s.dni AS socio_dni\n" +
                        "FROM asistencia a\n" +
                        "INNER JOIN socio s\n" +
                        "    ON a.socio_id = s.id\n" +
                        "ORDER BY a.fecha DESC, a.hora DESC, a.id DESC";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Asistencia asistencia = new Asistencia();

                asistencia.setId(rs.getInt("id"));
                asistencia.setSocioId(rs.getInt("socio_id"));

                Date fecha = rs.getDate("fecha");

                if (fecha != null) {
                    asistencia.setFecha(fecha.toLocalDate());
                }

                Time hora = rs.getTime("hora");

                if (hora != null) {
                    asistencia.setHora(hora.toLocalTime());
                }

                Timestamp registradoEn = rs.getTimestamp("registrado_en");

                if (registradoEn != null) {
                    asistencia.setRegistradoEn(registradoEn.toLocalDateTime());
                }

                asistencia.setNombreSocio(rs.getString("socio_nombre"));
                asistencia.setDniSocio(rs.getString("socio_dni"));

                lista.add(asistencia);
            }

        } catch (SQLException e) {

            logger.error(
                    "Error al obtener asistencias.",
                    e
            );
        }

        return lista;
    }

    // =========================
    // ELIMINAR
    // =========================

    public boolean eliminarAsistencia(int id) {

        String sql = "DELETE FROM asistencia WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

            logger.info(
                    "Asistencia eliminada. ID {}",
                    id
            );

            return true;

        } catch (SQLException e) {

            logger.error(
                    "Error al eliminar asistencia.",
                    e
            );

            return false;
        }
    }
}
