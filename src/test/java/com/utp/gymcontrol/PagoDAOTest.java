package com.utp.gymcontrol;

import com.utp.gymcontrol.dao.PagoDAO;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.dao.MembresiaDAO;
import com.utp.gymcontrol.model.Pago;
import com.utp.gymcontrol.model.Socio;
import com.utp.gymcontrol.model.Membresia;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PagoDAOTest {

    private final PagoDAO pagoDAO = new PagoDAO();
    private final SocioDAO socioDAO = new SocioDAO();
    private final MembresiaDAO membresiaDAO = new MembresiaDAO();

    private static int socioId;
    private static int membresiaId;

    @Order(1)
    @Test
    public void testPrepararDatos() {

        Socio socio = socioDAO.buscarPorDni("99999998");

        assertNotNull(socio);

        socioId = socio.getId();

        List<Membresia> membresias =
                membresiaDAO.obtenerMembresias();

        Membresia m = membresias.stream()
                .filter(x -> x.getSocioId() == socioId)
                .findFirst()
                .orElse(null);

        assertNotNull(m);

        membresiaId = m.getId();

    }

    @Order(2)
    @Test
    public void testRegistrarPago() {

        Pago pago = new Pago();

        pago.setSocioId(socioId);
        pago.setMembresiaId(membresiaId);
        pago.setMonto(80.0);
        pago.setMetodoPago("Efectivo");
        pago.setDescripcion("JUnit Pago");

        assertTrue(
                pagoDAO.registrarPago(pago)
        );

    }

    @Order(3)
    @Test
    public void testObtenerPagos() {

        List<Pago> pagos =
                pagoDAO.obtenerPagos();

        assertNotNull(pagos);

        assertFalse(
                pagos.isEmpty()
        );

    }

    @Order(4)
    @Test
    public void testExistePagoRegistrado() {

        List<Pago> pagos =
                pagoDAO.obtenerPagos();

        boolean existe = pagos.stream()
                .anyMatch(p ->
                        p.getSocioId() == socioId &&
                        p.getMonto() == 80.0
                );

        assertTrue(existe);

    }

    @Order(5)
    @Test
    public void testMontoMayorCero() {

        List<Pago> pagos =
                pagoDAO.obtenerPagos();

        Pago pago = pagos.stream()
                .filter(p ->
                        p.getSocioId() == socioId
                )
                .findFirst()
                .orElse(null);

        assertNotNull(pago);

        assertTrue(
                pago.getMonto() > 0
        );

    }

    @Order(6)
    @Test
    public void testMetodoPagoNoVacio() {

        List<Pago> pagos =
                pagoDAO.obtenerPagos();

        Pago pago = pagos.stream()
                .filter(p ->
                        p.getSocioId() == socioId
                )
                .findFirst()
                .orElse(null);

        assertNotNull(pago);

        assertNotNull(
                pago.getMetodoPago()
        );

        assertFalse(
                pago.getMetodoPago().isBlank()
        );

    }

}