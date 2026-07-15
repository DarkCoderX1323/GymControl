package com.utp.gymcontrol.dao;

import com.utp.gymcontrol.database.ConexionDB;
import com.utp.gymcontrol.model.Pago;

import java.sql.*;
import java.time.LocalDate;
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
    // FILTROS COMBINADOS
    // =========================

    /**
     * Filtra pagos combinando socio, método de pago, tipo de membresía y
     * rango de fechas. Cualquier parámetro puede venir null para omitir
     * ese criterio.
     */
    public List<Pago> filtrarPagos(
            Integer socioId,
            String metodoPago,
            String tipoMembresia,
            LocalDate desde,
            LocalDate hasta
    ) {

        List<Pago> listaPagos = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
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
                        "    ON m.tipo_membresia_id = tm.id\n" +
                        "WHERE 1=1\n"
        );

        List<Object> parametros = new ArrayList<>();

        if (socioId != null) {
            sql.append("  AND p.socio_id = ?\n");
            parametros.add(socioId);
        }

        if (metodoPago != null && !metodoPago.isBlank()
                && !metodoPago.equalsIgnoreCase("todos")) {
            sql.append("  AND p.metodo_pago = ?\n");
            parametros.add(metodoPago.trim().toLowerCase());
        }

        if (tipoMembresia != null && !tipoMembresia.isBlank()
                && !tipoMembresia.equalsIgnoreCase("todos")) {
            sql.append("  AND m.tipo = ?\n");
            parametros.add(tipoMembresia.trim());
        }

        if (desde != null) {
            sql.append("  AND DATE(p.fecha_pago) >= ?\n");
            parametros.add(Date.valueOf(desde));
        }

        if (hasta != null) {
            sql.append("  AND DATE(p.fecha_pago) <= ?\n");
            parametros.add(Date.valueOf(hasta));
        }

        sql.append("ORDER BY p.fecha_pago DESC");

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Pago pago = new Pago();

                    pago.setId(rs.getInt("id"));
                    pago.setSocioId(rs.getInt("socio_id"));
                    pago.setMonto(rs.getDouble("monto"));
                    pago.setMetodoPago(rs.getString("metodo_pago"));

                    Timestamp fecha = rs.getTimestamp("fecha_pago");

                    if (fecha != null) {
                        pago.setFechaPago(fecha.toLocalDateTime());
                    }

                    pago.setDescripcion(rs.getString("descripcion"));
                    pago.setMembresiaId(rs.getInt("membresia_id"));

                    listaPagos.add(pago);
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error al filtrar pagos.",
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

    // =========================
    // SINCRONIZACION CON MEMBRESIA
    // =========================

    /**
     * Actualiza el monto de todos los pagos que están vinculados a una
     * membresía (por membresia_id). Se usa cuando MembresiaView cambia el
     * tipo de una membresía existente: el pago original debe reflejar el
     * precio del nuevo tipo, no quedarse con el monto del tipo anterior.
     */
    public boolean actualizarMontoPorMembresia(
            int membresiaId,
            double nuevoMonto
    ) {

        String sql =
                "UPDATE pago SET monto=? WHERE membresia_id=?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, nuevoMonto);
            stmt.setInt(2, membresiaId);

            int filas = stmt.executeUpdate();

            logger.info(
                    "Monto sincronizado para membresia_id {} -> {} ({} pago(s))",
                    membresiaId, nuevoMonto, filas
            );

            return true;

        } catch (SQLException e) {

            logger.error(
                    "Error al sincronizar monto de pago con la membresía.",
                    e
            );

            return false;

        }
    }

}
