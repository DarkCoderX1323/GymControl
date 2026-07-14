package com.utp.gymcontrol;

import com.utp.gymcontrol.dao.MembresiaDAO;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.dao.TipoMembresiaDAO;
import com.utp.gymcontrol.model.Membresia;
import com.utp.gymcontrol.model.Socio;
import com.utp.gymcontrol.model.TipoMembresia;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MembresiaDAOTest {

    private final MembresiaDAO membresiaDAO = new MembresiaDAO();
    private final SocioDAO socioDAO = new SocioDAO();
    private final TipoMembresiaDAO tipoDAO = new TipoMembresiaDAO();

    private static int socioId;

    @BeforeAll
    static void init() {
        // reservado por si luego queremos preparar datos
    }

    @Order(1)
    @Test
    public void testObtenerSocio() {

        Socio socio = socioDAO.buscarPorDni("99999998");

        assertNotNull(socio);

        socioId = socio.getId();

    }

    @Order(2)
    @Test
    public void testRegistrarMembresia() {

        TipoMembresia tipo =
                tipoDAO.buscarPorId(1);

        assertNotNull(tipo);

        Membresia m = new Membresia();

        m.setSocioId(socioId);
        m.setTipo(tipo.getNombre());
        m.setTipoMembresiaId(tipo.getId());

        m.setFechaInicio(LocalDate.now());

        m.setFechaFin(
                LocalDate.now()
                        .plusDays(tipo.getDuracionDias())
        );

        m.setEstado("activa");

        assertTrue(
                membresiaDAO.registrarMembresia(m)
        );

    }

    @Order(3)
    @Test
    public void testObtenerMembresias() {

        List<Membresia> lista =
                membresiaDAO.obtenerMembresias();

        assertNotNull(lista);

        assertFalse(lista.isEmpty());

    }

    @Order(4)
    @Test
    public void testExisteMembresiaDelSocio() {

        List<Membresia> lista =
                membresiaDAO.obtenerMembresias();

        boolean encontrada = lista.stream()
                .anyMatch(m ->
                        m.getSocioId() == socioId
                );

        assertTrue(encontrada);

    }

    @Order(5)
    @Test
    public void testFiltrarPorSocio() {

        List<Membresia> lista =
                membresiaDAO.filtrarMembresias(
                        socioId,
                        null,
                        null
                );

        assertNotNull(lista);

        assertFalse(lista.isEmpty());

    }

    @Order(6)
    @Test
    public void testFiltrarPorEstado() {

        List<Membresia> lista =
                membresiaDAO.filtrarMembresias(
                        null,
                        "activa",
                        null
                );

        assertNotNull(lista);

    }

    @Order(7)
    @Test
    public void testFiltrarPorTipo() {

        List<Membresia> lista =
                membresiaDAO.filtrarMembresias(
                        null,
                        null,
                        "Mensual"
                );

        assertNotNull(lista);

    }

}