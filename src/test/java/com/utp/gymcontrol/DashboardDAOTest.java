package com.utp.gymcontrol;

import com.utp.gymcontrol.dao.DashboardDAO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DashboardDAOTest {

    private final DashboardDAO dao = new DashboardDAO();

    /**
     * Verifica el conteo de socios activos.
     */
    @Test
    public void testContarSociosActivos() {

        int total = dao.contarSociosActivos();

        assertTrue(total >= 0);

    }

    /**
     * Verifica el conteo de membresías activas.
     */
    @Test
    public void testContarMembresiasActivas() {

        int total = dao.contarMembresiasActivas();

        assertTrue(total >= 0);

    }

    /**
     * Verifica el cálculo de ingresos del mes.
     */
    @Test
    public void testTotalPagosMes() {

        double total = dao.totalPagosMes();

        assertTrue(total >= 0);

    }

    /**
     * Verifica que exista al menos un socio registrado.
     */
    @Test
    public void testExisteAlgunSocio() {

        assertTrue(
                dao.contarSociosActivos() > 0
        );

    }

    /**
     * Verifica que exista al menos una membresía registrada.
     */
    @Test
    public void testExisteAlgunaMembresia() {

        assertTrue(
                dao.contarMembresiasActivas() > 0
        );

    }

}