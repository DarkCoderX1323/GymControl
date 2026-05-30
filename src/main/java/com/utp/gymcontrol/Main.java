/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.utp.gymcontrol;

import com.formdev.flatlaf.FlatDarkLaf;
import com.utp.gymcontrol.view.LoginView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        try {

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
