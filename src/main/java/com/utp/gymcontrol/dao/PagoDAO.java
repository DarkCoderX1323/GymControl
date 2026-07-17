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

        // OJO: la consulta anterior traía "tm.id AS membresia_id" (el id
        // del TIPO de membresía, 1/2/3) en vez del id real de la
        // membresía (p.membresia_id) -- eso hacía que la tabla de Pagos y
        // el reporte Excel mostraran/guardaran un membresia_id
        // incorrecto. Corregido acá.
        //
        // También se cambió el JOIN con membresia de INNER a LEFT: un
        // pago puede quedar con membresia_id = NULL (por ejemplo, al
        // eliminar una membresía desde MembresiaView, que desvincula en
        // vez de borrar el pago) y con INNER JOIN esos pagos
        // desaparecían por completo de la lista en vez de simplemente no
        // tener tipo de membresía asociado.
        String sql =
                "SELECT\n" +
                        "    p.id,\n" +
                        "    p.socio_id,\n" +
                        "    s.nombre AS socio_nombre,\n" +
                        "    s.dni AS socio_dni,\n" +
                        "    p.monto,\n" +
                        "    p.metodo_pago,\n" +
                        "    p.fecha_pago,\n" +
                        "    p.descripcion,\n" +
                        "    p.membresia_id,\n" +
                        "    m.tipo AS membresia_tipo\n" +
                        "FROM pago p\n" +
                        "INNER JOIN socio s\n" +
                        "    ON p.socio_id = s.id\n" +
                        "LEFT JOIN membresia m\n" +
                        "    ON p.membresia_id = m.id\n" +
                        "ORDER BY p.fecha_pago DESC";

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

                pago.setNombreSocio(
                        rs.getString("socio_nombre")
                );

                pago.setDniSocio(
                        rs.getString("socio_dni")
                );

                pago.setTipoMembresia(
                        rs.getString("membresia_tipo")
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
     * Filtra pagos combinando socio (por id exacto), método de pago, tipo
     * de membresía y rango de fechas. Cualquier parámetro puede venir
     * null para omitir ese criterio.
     *
     * Firma original, sin tocar el primer parámetro (Integer socioId):
     * por el mismo motivo que en MembresiaDAO, la búsqueda por
     * nombre/DNI vive aparte en filtrarPagosPorNombre, para no arriesgar
     * una ambigüedad de overload con los null literales que puedan venir
     * de otros llamadores o tests.
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
                        "    p.socio_id,\n" +
                        "    s.nombre AS socio_nombre,\n" +
                        "    s.dni AS socio_dni,\n" +
                        "    p.monto,\n" +
                        "    p.metodo_pago,\n" +
                        "    p.fecha_pago,\n" +
                        "    p.descripcion,\n" +
                        "    p.membresia_id,\n" +
                        "    m.tipo AS membresia_tipo\n" +
                        "FROM pago p\n" +
                        "INNER JOIN socio s\n" +
                        "    ON p.socio_id = s.id\n" +
                        "LEFT JOIN membresia m\n" +
                        "    ON p.membresia_id = m.id\n" +
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
                    pago.setNombreSocio(rs.getString("socio_nombre"));
                    pago.setDniSocio(rs.getString("socio_dni"));
                    pago.setTipoMembresia(rs.getString("membresia_tipo"));

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

    /**
     * Filtra pagos combinando nombre/DNI del socio (coincidencia
     * parcial), método de pago, tipo de membresía y rango de fechas.
     * Usada por la barra de filtros y el buscador rápido de PagoView
     * (búsqueda por nombre, en vez de por ID de socio).
     */
    public List<Pago> filtrarPagosPorNombre(
            String nombreODni,
            String metodoPago,
            String tipoMembresia,
            LocalDate desde,
            LocalDate hasta
    ) {

        List<Pago> listaPagos = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT\n" +
                        "    p.id,\n" +
                        "    p.socio_id,\n" +
                        "    s.nombre AS socio_nombre,\n" +
                        "    s.dni AS socio_dni,\n" +
                        "    p.monto,\n" +
                        "    p.metodo_pago,\n" +
                        "    p.fecha_pago,\n" +
                        "    p.descripcion,\n" +
                        "    p.membresia_id,\n" +
                        "    m.tipo AS membresia_tipo\n" +
                        "FROM pago p\n" +
                        "INNER JOIN socio s\n" +
                        "    ON p.socio_id = s.id\n" +
                        "LEFT JOIN membresia m\n" +
                        "    ON p.membresia_id = m.id\n" +
                        "WHERE 1=1\n"
        );

        List<Object> parametros = new ArrayList<>();

        if (nombreODni != null && !nombreODni.isBlank()) {
            sql.append("  AND (s.nombre LIKE ? OR s.dni LIKE ?)\n");
            String comodin = "%" + nombreODni.trim() + "%";
            parametros.add(comodin);
            parametros.add(comodin);
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
                    pago.setNombreSocio(rs.getString("socio_nombre"));
                    pago.setDniSocio(rs.getString("socio_dni"));
                    pago.setTipoMembresia(rs.getString("membresia_tipo"));

                    listaPagos.add(pago);
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error al filtrar pagos por nombre.",
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
    /**
     * Actualiza el monto de todos los pagos que están vinculados a una
     * membresía (por membresia_id). Se usa cuando MembresiaView cambia el
     * tipo de una membresía existente: el pago original debe reflejar el
     * precio del nuevo tipo, no quedarse con el monto del tipo anterior.
     *
     * @param esRenovacion si es true (la membresía estaba vencida y se
     *                     está renovando desde hoy), además actualiza
     *                     fecha_pago a ahora -- si no, ese pago sigue
     *                     fechado en el mes en que se cobró originalmente
     *                     y "Ingresos del mes" del Dashboard nunca lo
     *                     cuenta, aunque el monto ya haya cambiado. Si es
     *                     false (solo se corrigió el tipo de una
     *                     membresía activa, sin que eso implique un cobro
     *                     nuevo), la fecha del pago no se toca.
     * @param metodoPago   método de pago elegido en el formulario de
     *                     edición (efectivo/tarjeta/yape/plin). Se
     *                     actualiza siempre, sin importar si es
     *                     renovación o no: el staff puede estar
     *                     corrigiendo el método aunque no sea una
     *                     renovación completa.
     */
    public boolean actualizarMontoPorMembresia(
            int membresiaId,
            double nuevoMonto,
            boolean esRenovacion,
            String metodoPago
    ) {

        String sql =
                esRenovacion
                        ? "UPDATE pago SET monto=?, metodo_pago=?, fecha_pago=NOW() WHERE membresia_id=?"
                        : "UPDATE pago SET monto=?, metodo_pago=? WHERE membresia_id=?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, nuevoMonto);
            stmt.setString(2, metodoPago);
            stmt.setInt(3, membresiaId);

            int filas = stmt.executeUpdate();

            logger.info(
                    "Monto/metodo sincronizados para membresia_id {} -> {} / {} ({} pago(s)), fecha_pago actualizada: {}",
                    membresiaId, nuevoMonto, metodoPago, filas, esRenovacion
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

    /**
     * Devuelve el método de pago del pago vinculado a una membresía (el
     * más reciente, si hubiera más de uno), para precargarlo en el
     * formulario de edición de MembresiaView. Devuelve null si no hay
     * ningún pago vinculado.
     */
    public String obtenerMetodoPagoPorMembresia(int membresiaId) {

        String sql =
                "SELECT metodo_pago FROM pago " +
                        "WHERE membresia_id=? " +
                        "ORDER BY fecha_pago DESC LIMIT 1";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, membresiaId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getString("metodo_pago");
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error al obtener método de pago de la membresía.",
                    e
            );
        }

        return null;
    }

}
