package com.utp.gymcontrol;

import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.model.Socio;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SocioDAOTest {

    private final SocioDAO dao = new SocioDAO();

    /**
     * Verifica que se pueda registrar un socio.
     */
    @Order(1)
    @Test
    public void testRegistrarSocio() {

        Socio socio = new Socio();

        socio.setNombre("JUnit Test");
        socio.setDni("99999998");
        socio.setTelefono("999999998");
        socio.setEmail("junit@test.com");
        socio.setEstado("activo");

        assertTrue(
                dao.registrarSocio(socio)
        );

    }

    /**
     * Verifica la búsqueda por DNI.
     */
    @Order(2)
    @Test
    public void testBuscarPorDni() {

        Socio socio =
                dao.buscarPorDni("99999998");

        assertNotNull(socio);

        assertEquals(
                "99999998",
                socio.getDni()
        );

    }

    /**
     * Verifica la búsqueda por ID.
     */
    @Order(3)
    @Test
    public void testBuscarPorId() {

        Socio socio =
                dao.buscarPorDni("99999998");

        assertNotNull(socio);

        Socio encontrado =
                dao.buscarPorId(socio.getId());

        assertNotNull(encontrado);

        assertEquals(
                socio.getId(),
                encontrado.getId()
        );

    }

    /**
     * Verifica que existan socios registrados.
     */
    @Order(4)
    @Test
    public void testObtenerSocios() {

        List<Socio> lista =
                dao.obtenerSocios();

        assertNotNull(lista);

        assertFalse(lista.isEmpty());

    }

    /**
     * Verifica el filtro por nombre.
     */
    @Order(5)
    @Test
    public void testFiltrarPorNombre() {

        List<Socio> lista =
                dao.filtrarSocios(
                        "JUnit",
                        "",
                        "activo"
                );

        assertNotNull(lista);

        assertFalse(lista.isEmpty());

    }

    /**
     * Verifica la actualización.
     */
    @Order(6)
    @Test
    public void testActualizarSocio() {

        Socio socio =
                dao.buscarPorDni("99999998");

        assertNotNull(socio);

        socio.setNombre("JUnit Actualizado");
        socio.setTelefono("999111222");

        assertTrue(
                dao.actualizarSocio(socio)
        );

    }

    /**
     * Verifica que la actualización realmente ocurrió.
     */
    @Order(7)
    @Test
    public void testVerificarActualizacion() {

        Socio socio =
                dao.buscarPorDni("99999998");

        assertNotNull(socio);

        assertEquals(
                "JUnit Actualizado",
                socio.getNombre()
        );

        assertEquals(
                "999111222",
                socio.getTelefono()
        );

    }

    /**
     * Verifica el cambio de estado del socio (baja lógica).
     */
    @Order(8)
    @Test
    public void testCambiarEstadoSocio() {

        Socio socio =
                dao.buscarPorDni("99999998");

        assertNotNull(socio);

        socio.setEstado("inactivo");

        assertTrue(
                dao.actualizarSocio(socio)
        );

    }

    /**
     * Verifica que el cambio de estado se haya realizado correctamente.
     */
    @Order(9)
    @Test
    public void testVerificarCambioEstado() {

        Socio socio =
                dao.buscarPorDni("99999998");

        assertNotNull(socio);

        assertEquals(
                "inactivo",
                socio.getEstado().toLowerCase()
        );

    }

}