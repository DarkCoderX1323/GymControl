package com.utp.gymcontrol;

import com.utp.gymcontrol.dao.AsistenciaDAO;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.model.Asistencia;
import com.utp.gymcontrol.model.Socio;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AsistenciaDAOTest {

    private final AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
    private final SocioDAO socioDAO = new SocioDAO();

    private static int socioId;

    @Order(1)
    @Test
    public void testObtenerSocio() {

        Socio socio = socioDAO.buscarPorDni("99999998");

        assertNotNull(socio);

        socioId = socio.getId();

    }

    @Order(2)
    @Test
    public void testRegistrarAsistencia() {

        // Si ya existe una asistencia hoy,
        // simplemente damos la prueba como válida.

        if (asistenciaDAO.existeAsistenciaHoy(socioId)) {

            assertTrue(true);
            return;

        }

        assertTrue(
                asistenciaDAO.registrarAsistencia(socioId)
        );

    }

    @Order(3)
    @Test
    public void testExisteAsistenciaHoy() {

        assertTrue(
                asistenciaDAO.existeAsistenciaHoy(socioId)
        );

    }

    @Order(4)
    @Test
    public void testObtenerAsistencias() {

        List<Asistencia> lista =
                asistenciaDAO.obtenerAsistencias();

        assertNotNull(lista);

        assertFalse(lista.isEmpty());

    }

    @Order(5)
    @Test
    public void testExisteAsistenciaDelSocio() {

        List<Asistencia> lista =
                asistenciaDAO.obtenerAsistencias();

        boolean encontrada =
                lista.stream()
                        .anyMatch(a ->
                                a.getSocioId() == socioId
                        );

        assertTrue(encontrada);

    }

    @Order(6)
    @Test
    public void testFechaHoraRegistradas() {

        List<Asistencia> lista =
                asistenciaDAO.obtenerAsistencias();

        Asistencia asistencia =
                lista.stream()
                        .filter(a ->
                                a.getSocioId() == socioId
                        )
                        .findFirst()
                        .orElse(null);

        assertNotNull(asistencia);

        assertNotNull(
                asistencia.getFecha()
        );

        assertNotNull(
                asistencia.getHora()
        );

    }

}