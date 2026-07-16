package com.utp.gymcontrol.dao;

import com.utp.gymcontrol.database.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardDAO {

    public int contarSociosActivos() {

        String sql =
                "SELECT COUNT(*) total " +
                        "FROM socio " +
                        "WHERE estado='activo'";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt =
                    conn.prepareStatement(sql);
            ResultSet rs =
                    stmt.executeQuery()) {

            if(rs.next()) {
                return rs.getInt("total");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int contarMembresiasActivas() {

        String sql =
                "SELECT COUNT(*) total " +
                        "FROM membresia " +
                        "WHERE estado='activa'";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt =
                    conn.prepareStatement(sql);
            ResultSet rs =
                    stmt.executeQuery()) {

            if(rs.next()) {
                return rs.getInt("total");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double totalPagosMes() {

        // Compara el mes y año de la fecha de pago con el mes y año actual

        String sql =
                "SELECT IFNULL(SUM(monto),0) total " +
                        "FROM pago " +
                        "WHERE MONTH(fecha_pago)=MONTH(CURDATE()) " +
                        "AND YEAR(fecha_pago)=YEAR(CURDATE())";

        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt =
                    conn.prepareStatement(sql);
            ResultSet rs =
                    stmt.executeQuery()) {

            if(rs.next()) {
                return rs.getDouble("total");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
