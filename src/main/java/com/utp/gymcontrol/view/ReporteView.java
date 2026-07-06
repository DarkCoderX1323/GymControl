package com.utp.gymcontrol.view;

import com.utp.gymcontrol.utils.ExcelReportGenerator;

import javax.swing.*;
import java.awt.*;

public class ReporteView extends JFrame {

    private JButton btnReporteSocios;
    private JButton btnVolver;
    private JLabel lblEstado;

    public ReporteView() {

        iniciarComponentes();

        setTitle("GymControl - Reportes");

        setSize(700, 420);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }

    private void iniciarComponentes() {

        Font fuente = new Font("Segoe UI", Font.PLAIN, 15);

        JPanel panelPrincipal = new JPanel(
                new BorderLayout(15, 15)
        );

        panelPrincipal.setBackground(
                new Color(30, 30, 30)
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        // =========================
        // TITULO
        // =========================

        JLabel titulo = new JLabel(
                "GYMCONTROL - Reportes",
                SwingConstants.CENTER
        );

        titulo.setForeground(Color.WHITE);

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // =========================
        // PANEL CENTRAL
        // =========================

        JPanel panelCentro = new JPanel();

        panelCentro.setBackground(
                new Color(40, 40, 40)
        );

        panelCentro.setLayout(
                new GridLayout(2, 1, 15, 15)
        );

        panelCentro.setBorder(
                BorderFactory.createEmptyBorder(
                        40, 60, 40, 60
                )
        );

        btnReporteSocios = new JButton(
                "Generar Reporte de Socios (Excel)"
        );

        btnReporteSocios.setFont(fuente);

        btnReporteSocios.setFocusPainted(false);

        btnReporteSocios.setBackground(
                new Color(0, 120, 215)
        );

        btnReporteSocios.setForeground(Color.WHITE);

        btnReporteSocios.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btnReporteSocios.addActionListener(
                e -> generarReporteSocios()
        );

        lblEstado = new JLabel(
                " ", SwingConstants.CENTER
        );

        lblEstado.setForeground(Color.LIGHT_GRAY);

        lblEstado.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        panelCentro.add(btnReporteSocios);
        panelCentro.add(lblEstado);

        panelPrincipal.add(
                panelCentro, BorderLayout.CENTER
        );

        // =========================
        // VOLVER
        // =========================

        btnVolver = new JButton("Volver");

        btnVolver.setFont(fuente);

        btnVolver.setFocusPainted(false);

        btnVolver.setBackground(
                new Color(90, 90, 90)
        );

        btnVolver.setForeground(Color.WHITE);

        btnVolver.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btnVolver.addActionListener(e -> dispose());

        JPanel panelInferior = new JPanel();

        panelInferior.setBackground(
                new Color(30, 30, 30)
        );

        panelInferior.add(btnVolver);

        panelPrincipal.add(
                panelInferior, BorderLayout.SOUTH
        );

        add(panelPrincipal);
    }

    private void generarReporteSocios() {

        boolean generado =
                ExcelReportGenerator
                        .generarReporteSocios();

        if (generado) {

            lblEstado.setText(
                    "Reporte generado: ReporteSocios.xlsx"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Reporte de socios generado correctamente."
            );

        } else {

            lblEstado.setText(
                    "Error al generar el reporte."
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Error al generar el reporte.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}