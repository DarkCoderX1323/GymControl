package com.utp.gymcontrol.dao;

import com.utp.gymcontrol.database.ConexionDB;
import com.utp.gymcontrol.model.Pago;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagoDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(PagoDAO.class);

    // =========================
    // REGISTRAR
    // =========================

    public boolean registrarPago(Pago pago) {

        String sql = """
                INSERT INTO pago
                (
                    socio_id,
                    monto,
                    metodo_pago,
                    descripcion,
                    membresia_id
                )
                VALUES
                (?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pago.getSocioId());
            stmt.setDouble(2, pago.getMonto());
            stmt.setString(3, pago.getMetodoPago());
            stmt.setString(4, pago.getDescripcion());
            stmt.setInt(5, pago.getMembresiaId());

            stmt.executeUpdate();

            logger.info(
                    "Pago registrado correctamente. Socio ID: {}",
                    pago.getSocioId()
            );

            return true;

        } catch (SQLException e) {

            logger.error(
                    "Error al registrar pago.",
                    e
            );

            return false;
        }
    }

    // =========================
    // OBTENER TODOS
    // =========================

    public List<Pago> obtenerPagos() {

        List<Pago> listaPagos =
                new ArrayList<>();

        String sql =
                "SELECT\n" +
                        "    p.id,\n" +
                        "    s.id AS socio_id,\n" +
                        "    s.nombre AS socio,\n" +
                        "    p.monto,\n" +
                        "    p.metodo_pago,\n" +
                        "    p.fecha_pago,\n" +
                        "    p.descripcion,\n" +
                        "    tm.id AS membresia_id,\n" +
                        "    tm.nombre AS membresia\n" +
                        "FROM pago p\n" +
                        "INNER JOIN socio s\n" +
                        "    ON p.socio_id = s.id\n" +
                        "INNER JOIN membresia m\n" +
                        "    ON p.membresia_id = m.id\n" +
                        "INNER JOIN tipo_membresia tm\n" +
                        "    ON m.tipo_membresia_id = tm.id;";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Pago pago = new Pago();

                pago.setId(
                        rs.getInt("id")
                );

                pago.setSocioId(
                        rs.getInt("socio_id")
                );

                pago.setMonto(
                        rs.getDouble("monto")
                );

                pago.setMetodoPago(
                        rs.getString("metodo_pago")
                );

                Timestamp fecha =
                        rs.getTimestamp(
                                "fecha_pago"
                        );

                if (fecha != null) {

                    pago.setFechaPago(
                            fecha.toLocalDateTime()
                    );
                }

                pago.setDescripcion(
                        rs.getString("descripcion")
                );

                pago.setMembresiaId(
                        rs.getInt("membresia_id")
                );

                listaPagos.add(pago);

            }

        } catch (SQLException e) {

            logger.error(
                    "Error al obtener pagos.",
                    e
            );

        }

        return listaPagos;

    }

    // =========================
    // BUSCAR POR ID
    // =========================

    public Pago buscarPago(int id) {

        String sql =
                "SELECT * FROM pago WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Pago pago =
                            new Pago();

                    pago.setId(
                            rs.getInt("id")
                    );

                    pago.setSocioId(
                            rs.getInt("socio_id")
                    );

                    pago.setMonto(
                            rs.getDouble("monto")
                    );

                    pago.setMetodoPago(
                            rs.getString("metodo_pago")
                    );

                    Timestamp fecha =
                            rs.getTimestamp(
                                    "fecha_pago"
                            );

                    if (fecha != null) {

                        pago.setFechaPago(
                                fecha.toLocalDateTime()
                        );
                    }

                    pago.setDescripcion(
                            rs.getString("descripcion")
                    );

                    pago.setMembresiaId(
                            rs.getInt("membresia_id")
                    );

                    return pago;

                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error al buscar pago.",
                    e
            );

        }

        return null;

    }

    // =========================
    // ACTUALIZAR
    // =========================

    public boolean actualizarPago(Pago pago) {

        String sql = """
                UPDATE pago
                SET
                    socio_id=?,
                    monto=?,
                    metodo_pago=?,
                    descripcion=?,
                    membresia_id=?
                WHERE id=?
                """;

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(
                    1,
                    pago.getSocioId()
            );

            stmt.setDouble(
                    2,
                    pago.getMonto()
            );

            stmt.setString(
                    3,
                    pago.getMetodoPago()
            );

            stmt.setString(
                    4,
                    pago.getDescripcion()
            );

            stmt.setInt(
                    5,
                    pago.getMembresiaId()
            );

            stmt.setInt(
                    6,
                    pago.getId()
            );

            stmt.executeUpdate();

            logger.info(
                    "Pago actualizado. ID {}",
                    pago.getId()
            );

            return true;

        } catch (SQLException e) {

            logger.error(
                    "Error al actualizar pago.",
                    e
            );

            return false;

        }

    }

    // =========================
    // ELIMINAR
    // =========================

    public boolean eliminarPago(int id) {

        String sql =
                "DELETE FROM pago WHERE id=?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(
                    1,
                    id
            );

            stmt.executeUpdate();

            logger.info(
                    "Pago eliminado. ID {}",
                    id
            );

            return true;

        } catch (SQLException e) {

            logger.error(
                    "Error al eliminar pago.",
                    e
            );

            return false;

        }

    }

}
