package com.utp.gymcontrol;


import com.utp.gymcontrol.dao.UsuarioDAO;
import com.utp.gymcontrol.model.Usuario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDAOTest {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Verifica que un usuario administrador pueda iniciar sesión.
     */
    @Test
public void testLoginCorrecto() {

    Usuario usuario = usuarioDAO.login(
            "Admin",
            "Admin123"
    );

    assertNotNull(usuario);

    assertEquals(
            "admin",
            usuario.getUsername().toLowerCase()
    );

    assertEquals(
            "admin",
            usuario.getRol().toLowerCase()
    );

}

    /**
     * Verifica que una contraseña incorrecta no permita el acceso.
     */
    @Test
    public void testPasswordIncorrecto() {

        Usuario usuario = usuarioDAO.login(
                "Admin",
                "123456"
        );

        assertNull(usuario);

    }

    /**
     * Verifica que un usuario inexistente no pueda autenticarse.
     */
    @Test
    public void testUsuarioInexistente() {

        Usuario usuario = usuarioDAO.login(
                "AdministradorFalso",
                "Admin123"
        );

        assertNull(usuario);

    }

    /**
     * Verifica que credenciales vacías no produzcan un login válido.
     */
    @Test
    public void testCamposVacios() {

        Usuario usuario = usuarioDAO.login(
                "",
                ""
        );

        assertNull(usuario);

    }

    /**
     * Verifica que valores nulos no autentiquen usuarios.
     */
    @Test
    public void testValoresNull() {

        Usuario usuario = usuarioDAO.login(
                null,
                null
        );

        assertNull(usuario);

    }

}