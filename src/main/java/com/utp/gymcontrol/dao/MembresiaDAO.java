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
                "SELECT m.id, m.socio_id, m.tipo, m.fecha_inicio, m.fecha_fin, " +
                        "m.estado, m.tipo_membresia_id, " +
                        "s.nombre AS socio_nombre, s.dni AS socio_dni " +
                        "FROM membresia m " +
                        "INNER JOIN socio s ON m.socio_id = s.id " +
                        "ORDER BY m.id";

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

                m.setNombreSocio(
                        rs.getString("socio_nombre")
                );

                m.setDniSocio(
                        rs.getString("socio_dni")
                );

                lista.add(m);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    // =========================
    // FILTROS COMBINADOS
    // =========================

    /**
     * Filtra membresías por socio (id exacto), estado (activa/vencida) y
     * tipo (Mensual/Trimestral/Anual). Cualquier parámetro puede venir
     * null para omitir ese criterio.
     *
     * Firma original, sin tocar: la usa MembresiaDAOTest (de mi
     * compañero) con socioId de tipo int/Integer. No renombrar ni
     * sobrecargar con un primer parámetro String en el mismo nombre de
     * método — con argumentos null literales (como
     * filtrarMembresias(null, "activa", null)) el compilador no podría
     * decidir entre un overload (Integer,...) y uno (String,...) y el
     * proyecto de mi compañero dejaría de compilar. Por eso la búsqueda
     * por nombre/DNI vive en un método aparte: filtrarMembresiasPorNombre.
     */
    public List<Membresia> filtrarMembresias(
            Integer socioId,
            String estado,
            String tipo
    ) {

        List<Membresia> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT m.id, m.socio_id, m.tipo, m.fecha_inicio, m.fecha_fin, " +
                        "m.estado, m.tipo_membresia_id, " +
                        "s.nombre AS socio_nombre, s.dni AS socio_dni " +
                        "FROM membresia m " +
                        "INNER JOIN socio s ON m.socio_id = s.id " +
                        "WHERE 1=1"
        );

        List<Object> parametros = new ArrayList<>();

        if (socioId != null) {
            sql.append(" AND m.socio_id = ?");
            parametros.add(socioId);
        }

        if (estado != null && !estado.isBlank()
                && !estado.equalsIgnoreCase("todos")) {
            sql.append(" AND m.estado = ?");
            parametros.add(estado.trim().toLowerCase());
        }

        if (tipo != null && !tipo.isBlank()
                && !tipo.equalsIgnoreCase("todos")) {
            sql.append(" AND m.tipo = ?");
            parametros.add(tipo.trim());
        }

        sql.append(" ORDER BY m.fecha_fin DESC");

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Membresia m = new Membresia();

                    m.setId(rs.getInt("id"));
                    m.setSocioId(rs.getInt("socio_id"));
                    m.setTipo(rs.getString("tipo"));
                    m.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                    m.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                    m.setEstado(rs.getString("estado"));
                    m.setTipoMembresiaId(rs.getInt("tipo_membresia_id"));
                    m.setNombreSocio(rs.getString("socio_nombre"));
                    m.setDniSocio(rs.getString("socio_dni"));

                    lista.add(m);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Filtra membresías combinando nombre/DNI del socio (coincidencia
     * parcial), estado (activa/vencida) y tipo (Mensual/Trimestral/Anual).
     * Cualquier parámetro puede venir null o vacío para omitir ese criterio.
     * Usada por la barra de filtros de MembresiaView (búsqueda por
     * nombre, en vez de por ID de socio).
     */
    public List<Membresia> filtrarMembresiasPorNombre(
            String nombreODni,
            String estado,
            String tipo
    ) {

        List<Membresia> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT m.id, m.socio_id, m.tipo, m.fecha_inicio, m.fecha_fin, " +
                        "m.estado, m.tipo_membresia_id, " +
                        "s.nombre AS socio_nombre, s.dni AS socio_dni " +
                        "FROM membresia m " +
                        "INNER JOIN socio s ON m.socio_id = s.id " +
                        "WHERE 1=1"
        );

        List<Object> parametros = new ArrayList<>();

        if (nombreODni != null && !nombreODni.isBlank()) {
            sql.append(" AND (s.nombre LIKE ? OR s.dni LIKE ?)");
            String comodin = "%" + nombreODni.trim() + "%";
            parametros.add(comodin);
            parametros.add(comodin);
        }

        if (estado != null && !estado.isBlank()
                && !estado.equalsIgnoreCase("todos")) {
            sql.append(" AND m.estado = ?");
            parametros.add(estado.trim().toLowerCase());
        }

        if (tipo != null && !tipo.isBlank()
                && !tipo.equalsIgnoreCase("todos")) {
            sql.append(" AND m.tipo = ?");
            parametros.add(tipo.trim());
        }

        sql.append(" ORDER BY m.fecha_fin DESC");

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Membresia m = new Membresia();

                    m.setId(rs.getInt("id"));
                    m.setSocioId(rs.getInt("socio_id"));
                    m.setTipo(rs.getString("tipo"));
                    m.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                    m.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                    m.setEstado(rs.getString("estado"));
                    m.setTipoMembresiaId(rs.getInt("tipo_membresia_id"));
                    m.setNombreSocio(rs.getString("socio_nombre"));
                    m.setDniSocio(rs.getString("socio_dni"));

                    lista.add(m);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    // =========================
    // ALERTAS DE VENCIMIENTO
    // =========================

    /**
     * Devuelve las membresías activas cuya fecha de fin cae dentro de los
     * próximos `dias` (inclusive hoy), junto con los datos del socio, para
     * las alertas de "por vencer".
     */
    public List<Membresia> obtenerMembresiasPorVencer(int dias) {

        List<Membresia> lista = new ArrayList<>();

        String sql =
                "SELECT\n" +
                        "    m.id,\n" +
                        "    m.socio_id,\n" +
                        "    m.tipo,\n" +
                        "    m.fecha_inicio,\n" +
                        "    m.fecha_fin,\n" +
                        "    m.estado,\n" +
                        "    m.tipo_membresia_id,\n" +
                        "    s.nombre AS socio_nombre,\n" +
                        "    s.dni AS socio_dni,\n" +
                        "    DATEDIFF(m.fecha_fin, CURDATE()) AS dias_restantes\n" +
                        "FROM membresia m\n" +
                        "INNER JOIN socio s\n" +
                        "    ON m.socio_id = s.id\n" +
                        "WHERE m.estado = 'activa'\n" +
                        "  AND m.fecha_fin BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY)\n" +
                        "ORDER BY m.fecha_fin ASC";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dias);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Membresia m = new Membresia();

                    m.setId(rs.getInt("id"));
                    m.setSocioId(rs.getInt("socio_id"));
                    m.setTipo(rs.getString("tipo"));
                    m.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                    m.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                    m.setEstado(rs.getString("estado"));
                    m.setTipoMembresiaId(rs.getInt("tipo_membresia_id"));
                    m.setNombreSocio(rs.getString("socio_nombre"));
                    m.setDniSocio(rs.getString("socio_dni"));
                    m.setDiasRestantes(rs.getLong("dias_restantes"));

                    lista.add(m);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Cuenta cuántas membresías activas están por vencer dentro de los
     * próximos `dias`. Usado para la tarjeta de alerta en el Dashboard.
     */
    public int contarMembresiasPorVencer(int dias) {

        String sql =
                "SELECT COUNT(*) total " +
                        "FROM membresia " +
                        "WHERE estado = 'activa' " +
                        "  AND fecha_fin BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dias);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
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

    /**
     * Actualiza el tipo, fechas y estado de una membresía ya existente
     * (identificada por su id). No modifica el socio_id: reasignar una
     * membresía a otro socio no está contemplado en este flujo de edición.
     */
    public boolean actualizarMembresia(Membresia membresia) {

        String sql =
                "UPDATE membresia SET " +
                        "tipo=?, tipo_membresia_id=?, fecha_inicio=?, " +
                        "fecha_fin=?, estado=? " +
                        "WHERE id=?";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt =
                    conn.prepareStatement(sql)) {

            stmt.setString(1,
                    membresia.getTipo());

            stmt.setInt(2,
                    membresia.getTipoMembresiaId());

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
                    membresia.getId());

            return stmt.executeUpdate() > 0;

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina una membresía. La tabla pago tiene una FK hacia
     * membresia_id SIN ON DELETE CASCADE (a propósito: el dinero ya se
     * cobró y no queremos perder el registro del pago), así que antes de
     * borrar la membresía se desvinculan sus pagos (membresia_id -> NULL)
     * en vez de dejar que la FK rechace el DELETE. Ambos pasos van en una
     * misma transacción: si falla el DELETE, la desvinculación también se
     * revierte.
     */
    public boolean eliminarMembresia(int id) {

        Connection conn = null;

        try {

            conn = ConexionDB.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPago = conn.prepareStatement(
                    "UPDATE pago SET membresia_id = NULL WHERE membresia_id = ?"
            )) {

                stmtPago.setInt(1, id);
                stmtPago.executeUpdate();
            }

            boolean eliminada;

            try (PreparedStatement stmtMembresia = conn.prepareStatement(
                    "DELETE FROM membresia WHERE id = ?"
            )) {

                stmtMembresia.setInt(1, id);
                eliminada = stmtMembresia.executeUpdate() > 0;
            }

            conn.commit();

            return eliminada;

        } catch (Exception e) {

            if (conn != null) {

                try {
                    conn.rollback();
                } catch (SQLException exRollback) {
                    exRollback.printStackTrace();
                }
            }

            e.printStackTrace();

            return false;

        } finally {

            if (conn != null) {

                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException exClose) {
                    exClose.printStackTrace();
                }
            }
        }
    }
}
