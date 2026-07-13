package com.utp.gymcontrol.dao;

import com.utp.gymcontrol.database.ConexionDB;
import com.utp.gymcontrol.model.Socio;
import com.utp.gymcontrol.model.TipoMembresia;

import java.sql.*;
import java.time.LocalDate;

/**
 * Coordina el registro de "socio nuevo (o existente) + membresía + pago"
 * en una sola operación, para no obligar al usuario a pasar por 3
 * ventanas distintas copiando IDs a mano.
 *
 * Las 3 inserciones ocurren dentro de una misma transacción: si algo
 * falla a mitad de camino (por ejemplo, el pago), se revierte todo y no
 * queda ningún socio o membresía huérfana en la base de datos.
 */
public class RegistroRapidoDAO {

    /**
     * Resultado de un registro exitoso, con los 3 IDs generados.
     */
    public static class Resultado {

        public final int socioId;
        public final int membresiaId;
        public final int pagoId;

        public Resultado(int socioId, int membresiaId, int pagoId) {
            this.socioId = socioId;
            this.membresiaId = membresiaId;
            this.pagoId = pagoId;
        }
    }

    /**
     * @param socioNuevo       datos del socio a crear, o null si se usa
     *                         uno existente
     * @param socioIdExistente ID de un socio ya registrado, o null si es
     *                         nuevo
     * @param tipo             tipo de membresía elegido (define duración
     *                         y precio del pago)
     * @param metodoPago       método de pago elegido
     */
    public Resultado registrarTodo(
            Socio socioNuevo,
            Integer socioIdExistente,
            TipoMembresia tipo,
            String metodoPago
    ) throws SQLException {

        Connection conn = null;

        try {

            conn = ConexionDB.conectar();
            conn.setAutoCommit(false);

            int socioId;

            if (socioIdExistente != null) {

                socioId = socioIdExistente;

            } else {

                socioId = insertarSocio(conn, socioNuevo);
            }

            LocalDate fechaInicio = LocalDate.now();

            LocalDate fechaFin =
                    fechaInicio.plusDays(tipo.getDuracionDias());

            int membresiaId = insertarMembresia(
                    conn, socioId, tipo, fechaInicio, fechaFin
            );

            int pagoId = insertarPago(
                    conn, socioId, membresiaId, tipo.getPrecio(), metodoPago
            );

            conn.commit();

            return new Resultado(socioId, membresiaId, pagoId);

        } catch (SQLException e) {

            if (conn != null) {

                try {

                    conn.rollback();

                } catch (SQLException exRollback) {

                    exRollback.printStackTrace();
                }
            }

            throw e;

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

    private int insertarSocio(
            Connection conn,
            Socio socio
    ) throws SQLException {

        String sql =
                "INSERT INTO socio (nombre, dni, telefono, email, estado) " +
                        "VALUES (?, ?, ?, ?, 'activo')";

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
        )) {

            stmt.setString(1, socio.getNombre());
            stmt.setString(2, socio.getDni());
            stmt.setString(3, socio.getTelefono());
            stmt.setString(4, socio.getEmail());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException(
                "No se pudo obtener el ID del socio nuevo."
        );
    }

    private int insertarMembresia(
            Connection conn,
            int socioId,
            TipoMembresia tipo,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) throws SQLException {

        String sql =
                "INSERT INTO membresia " +
                        "(socio_id, tipo, fecha_inicio, fecha_fin, estado, tipo_membresia_id) " +
                        "VALUES (?, ?, ?, ?, 'activa', ?)";

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
        )) {

            stmt.setInt(1, socioId);
            stmt.setString(2, tipo.getNombre());
            stmt.setDate(3, Date.valueOf(fechaInicio));
            stmt.setDate(4, Date.valueOf(fechaFin));
            stmt.setInt(5, tipo.getId());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException(
                "No se pudo obtener el ID de la membresía nueva."
        );
    }

    private int insertarPago(
            Connection conn,
            int socioId,
            int membresiaId,
            double monto,
            String metodoPago
    ) throws SQLException {

        String sql =
                "INSERT INTO pago " +
                        "(socio_id, monto, metodo_pago, descripcion, membresia_id) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
        )) {

            stmt.setInt(1, socioId);
            stmt.setDouble(2, monto);
            stmt.setString(3, metodoPago);
            stmt.setString(4, "Registro rapido");
            stmt.setInt(5, membresiaId);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException(
                "No se pudo obtener el ID del pago nuevo."
        );
    }
}
