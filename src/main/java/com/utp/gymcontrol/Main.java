/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.utp.gymcontrol;

import com.formdev.flatlaf.FlatDarkLaf;
import com.utp.gymcontrol.utils.Tema;
import com.utp.gymcontrol.view.LoginView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        try {

            // Bordes redondeados globales (botones, campos de texto, combos, etc.)
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("CheckBox.arc", 6);

            // Color de acento aplicado a foco y botones por defecto
            UIManager.put("Component.focusColor", Tema.ACENTO);
            UIManager.put("Component.focusedBorderColor", Tema.ACENTO);
            UIManager.put("Button.default.background", Tema.ACENTO);
            UIManager.put("Button.default.foreground", Tema.TEXTO_PRIMARIO);
            UIManager.put("Button.default.focusedBackground", Tema.ACENTO_OSCURO);

            // Colores base de fondo/superficie
            UIManager.put("Panel.background", Tema.FONDO);
            UIManager.put("TextField.background", Tema.SUPERFICIE_CLARA);
            UIManager.put("PasswordField.background", Tema.SUPERFICIE_CLARA);

            UIManager.setLookAndFeel(
                    new FlatDarkLaf()
            );

        } catch (Exception e) {

            System.out.println("Error al cargar tema visual");
        }

        SwingUtilities.invokeLater(() -> {

            new LoginView();

        });
    }
}
