package com.utp.gymcontrol;

import com.utp.gymcontrol.model.Socio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SocioTest {

    @Test
    public void testNombreSocio() {

        Socio socio = new Socio();

        socio.setNombre("Juan Perez");

        assertEquals(
                "Juan Perez",
                socio.getNombre()
        );
    }

    @Test
    public void testDniSocio() {

        Socio socio = new Socio();

        socio.setDni("12345678");

        assertEquals(
                "12345678",
                socio.getDni()
        );
    }
}