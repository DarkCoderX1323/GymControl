package com.utp.gymcontrol;

import com.utp.gymcontrol.dao.TipoMembresiaDAO;
import com.utp.gymcontrol.model.TipoMembresia;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TipoMembresiaDAOTest {

    private final TipoMembresiaDAO dao =
            new TipoMembresiaDAO();

    /**
     * Verifica que existan tipos de membresía activos.
     */
    @Test
    public void testObtenerTiposMembresia() {

        List<TipoMembresia> lista =
                dao.obtenerTiposMembresia();

        assertNotNull(lista);

        assertFalse(lista.isEmpty());

    }

    /**
     * Verifica que exista la membresía Mensual.
     */
    @Test
    public void testBuscarTipoMensual() {

        TipoMembresia tipo =
                dao.buscarPorId(1);

        assertNotNull(tipo);

        assertEquals(
                "Mensual",
                tipo.getNombre()
        );

    }

    /**
     * Verifica que el precio sea mayor que cero.
     */
    @Test
    public void testPrecioMayorCero() {

        TipoMembresia tipo =
                dao.buscarPorId(1);

        assertNotNull(tipo);

        assertTrue(
                tipo.getPrecio() > 0
        );

    }

    /**
     * Verifica que la duración sea positiva.
     */
    @Test
    public void testDuracionMayorCero() {

        TipoMembresia tipo =
                dao.buscarPorId(1);

        assertNotNull(tipo);

        assertTrue(
                tipo.getDuracionDias() > 0
        );

    }

    /**
     * Verifica que buscar un ID inexistente devuelva null.
     */
    @Test
    public void testBuscarTipoInexistente() {

        TipoMembresia tipo =
                dao.buscarPorId(9999);

        assertNull(tipo);

    }

}
