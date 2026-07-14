package com.utp.gymcontrol;

import com.utp.gymcontrol.dao.RegistroRapidoDAO;
import com.utp.gymcontrol.dao.TipoMembresiaDAO;
import com.utp.gymcontrol.model.Socio;
import com.utp.gymcontrol.model.TipoMembresia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistroRapidoDAOTest {

    private final RegistroRapidoDAO dao = new RegistroRapidoDAO();
    private final TipoMembresiaDAO tipoDAO = new TipoMembresiaDAO();

    @Test
    public void testRegistroRapidoCompleto() throws Exception {

        Socio socio = new Socio();

        // DNI único para evitar duplicados
        String dni = String.valueOf(System.currentTimeMillis());
        dni = dni.substring(dni.length() - 8);

        socio.setNombre("JUnit Registro Rapido");
        socio.setDni(dni);
        socio.setTelefono("999888777");
        socio.setEmail("registro@test.com");

        TipoMembresia tipo = tipoDAO.buscarPorId(1);

        assertNotNull(tipo);

        RegistroRapidoDAO.Resultado resultado =
                dao.registrarTodo(
                        socio,
                        null,
                        tipo,
                        "Efectivo"
                );

        assertNotNull(resultado);

        assertTrue(resultado.socioId > 0);
        assertTrue(resultado.membresiaId > 0);
        assertTrue(resultado.pagoId > 0);

    }

}