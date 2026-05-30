/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.gymcontrol.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
   // URL de conexión
    private static final String URL =
            "jdbc:mysql://localhost:3306/gymcontrol";

    // Usuario de MySQL
    private static final String USER = "root";

    // Contraseña MySQL
    private static final String PASSWORD = "2548";

    // Método para obtener conexión
    public static Connection conectar() {

    try {

        Connection conexion = DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );

        System.out.println("Conexión exitosa a MySQL.");

        return conexion;

    } catch (SQLException e) {

        System.out.println("Error de conexión:");
        e.printStackTrace();

        return null;
    }

}
}