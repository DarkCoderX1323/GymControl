package com.utp.gymcontrol.view;

import com.utp.gymcontrol.utils.ExcelReportGenerator;
import com.utp.gymcontrol.utils.Tema;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReporteView extends JFrame {

    private JTextField txtDesde;
    private JTextField txtHasta;

    public ReporteView() {

        iniciarComponentes();

        setTitle("GymControl - Reportes");

        setSize(700, 660);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }

    private void iniciarComponentes() {

        // =========================
        // PANEL PRINCIPAL
        // =========================

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(0, 20));

        panelPrincipal.setBackground(Tema.FONDO);

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        24, 28, 24, 28
                )
        );

        // =========================
        // TITULO
        // =========================

        JLabel titulo = new JLabel("GYMCONTROL - Reportes");
        titulo.setForeground(Tema.TEXTO_PRIMARIO);
        titulo.setFont(Tema.fuenteTitulo().deriveFont(24f));

        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // =========================
        // CONTENIDO (tarjetas apiladas)
        // =========================

        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setOpaque(false);

        GridBagConstraints gbcContenido = new GridBagConstraints();
        gbcContenido.gridx = 0;
        gbcContenido.fill = GridBagConstraints.HORIZONTAL;
        gbcContenido.weightx = 1;
        gbcContenido.anchor = GridBagConstraints.NORTH;

        gbcContenido.gridy = 0;
        gbcContenido.insets = new Insets(0, 0, 16, 0);
        contenido.add(
                crearTarjetaSimple(
                        "Reporte de socios",
                        "Exporta el listado completo de socios registrados, con su estado actual.",
                        "Generar reporte",
                        Tema.ACENTO,
                        e -> generarReporte(
                                ExcelReportGenerator::generarReporteSocios,
                                "ReporteSocios.xlsx"
                        )
                ),
                gbcContenido
        );

        gbcContenido.gridy = 1;
        contenido.add(
                crearTarjetaSimple(
                        "Reporte de membresías",
                        "Exporta todas las membresías registradas, incluyendo su vigencia y estado.",
                        "Generar reporte",
                        Tema.EXITO,
                        e -> generarReporte(
                                ExcelReportGenerator::generarReporteMembresias,
                                "ReporteMembresias.xlsx"
                        )
                ),
                gbcContenido
        );

        gbcContenido.gridy = 2;
        gbcContenido.insets = new Insets(0, 0, 0, 0);
        contenido.add(crearTarjetaPagos(), gbcContenido);

        // Fila de relleno: absorbe cualquier espacio vertical sobrante
        // para que las tarjetas no se estiren ni queden centradas.
        gbcContenido.gridy = 3;
        gbcContenido.weighty = 1;
        contenido.add(new JLabel(), gbcContenido);

        panelPrincipal.add(contenido, BorderLayout.CENTER);

        // =========================
        // VOLVER
        // =========================

        JButton btnVolver = new JButton("Volver");
        estilizarBotonSecundario(btnVolver);
        btnVolver.addActionListener(e -> dispose());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelInferior.setOpaque(false);
        panelInferior.add(btnVolver);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    /**
     * Tarjeta genérica para un reporte de un solo botón (sin filtros
     * adicionales): Socios y Membresías.
     */
    private JPanel crearTarjetaSimple(
            String titulo,
            String descripcion,
            String textoBoton,
            Color colorAcento,
            java.awt.event.ActionListener alGenerar
    ) {

        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Tema.SUPERFICIE);
        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, colorAcento),
                        BorderFactory.createEmptyBorder(18, 18, 18, 18)
                )
        );

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Tema.TEXTO_PRIMARIO);
        lblTitulo.setFont(Tema.fuenteBoton().deriveFont(16f));

        JLabel lblDescripcion = new JLabel(
                "<html><div style='width:320px'>" + descripcion + "</div></html>"
        );
        lblDescripcion.setForeground(Tema.TEXTO_SECUNDARIO);
        lblDescripcion.setFont(Tema.fuenteEtiqueta());
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        textos.add(lblTitulo);
        textos.add(lblDescripcion);

        JButton boton = new JButton(textoBoton);
        boton.setBackground(colorAcento);
        boton.setForeground(Tema.TEXTO_PRIMARIO);
        boton.setFont(Tema.fuenteBoton().deriveFont(13f));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 38));
        boton.addActionListener(alGenerar);

        JPanel panelBoton = new JPanel(new BorderLayout());
        panelBoton.setOpaque(false);
        panelBoton.add(boton, BorderLayout.CENTER);

        tarjeta.add(textos, BorderLayout.CENTER);
        tarjeta.add(panelBoton, BorderLayout.EAST);

        return tarjeta;
    }

    /**
     * Tarjeta de Pagos: incluye los dos campos de fecha (Desde/Hasta)
     * además del botón de generación.
     */
    private JPanel crearTarjetaPagos() {

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Tema.SUPERFICIE);
        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, Tema.ADVERTENCIA),
                        BorderFactory.createEmptyBorder(18, 18, 18, 18)
                )
        );

        JLabel lblTitulo = new JLabel("Reporte de pagos");
        lblTitulo.setForeground(Tema.TEXTO_PRIMARIO);
        lblTitulo.setFont(Tema.fuenteBoton().deriveFont(16f));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel(
                "Exporta los pagos registrados dentro del rango de fechas indicado."
        );
        lblDescripcion.setForeground(Tema.TEXTO_SECUNDARIO);
        lblDescripcion.setFont(Tema.fuenteEtiqueta());
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(6, 0, 14, 0));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(lblDescripcion);

        // Fila de fechas
        JPanel filaFechas = new JPanel(new GridBagLayout());
        filaFechas.setOpaque(false);
        filaFechas.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 16);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblDesde = new JLabel("Desde");
        lblDesde.setForeground(Tema.TEXTO_SECUNDARIO);
        lblDesde.setFont(Tema.fuenteEtiqueta());

        txtDesde = new JTextField(
                LocalDate.now().minusDays(30).toString()
        );
        estilizarCampoFecha(txtDesde);

        JLabel lblHasta = new JLabel("Hasta");
        lblHasta.setForeground(Tema.TEXTO_SECUNDARIO);
        lblHasta.setFont(Tema.fuenteEtiqueta());

        txtHasta = new JTextField(
                LocalDate.now().toString()
        );
        estilizarCampoFecha(txtHasta);

        gbc.gridx = 0;
        gbc.gridy = 0;
        filaFechas.add(lblDesde, gbc);

        gbc.gridx = 1;
        filaFechas.add(txtDesde, gbc);

        gbc.gridx = 2;
        filaFechas.add(lblHasta, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        filaFechas.add(txtHasta, gbc);

        tarjeta.add(filaFechas);

        tarjeta.add(Box.createVerticalStrut(14));

        JButton boton = new JButton("Generar reporte");
        boton.setBackground(Tema.ADVERTENCIA);
        boton.setForeground(Tema.TEXTO_PRIMARIO);
        boton.setFont(Tema.fuenteBoton().deriveFont(13f));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 38));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.addActionListener(e -> generarReportePagos());

        tarjeta.add(boton);

        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        tarjeta.getPreferredSize().height
                )
        );

        return tarjeta;
    }

    private void estilizarCampoFecha(JTextField campo) {

        campo.setPreferredSize(new Dimension(170, 34));
        campo.setBackground(Tema.SUPERFICIE_CLARA);
        campo.setForeground(Tema.TEXTO_PRIMARIO);
        campo.setCaretColor(Tema.TEXTO_PRIMARIO);
        campo.setFont(Tema.fuenteRegular().deriveFont(13f));
        campo.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    }

    private void estilizarBotonSecundario(JButton boton) {

        boton.setBackground(Tema.SUPERFICIE_CLARA);
        boton.setForeground(Tema.TEXTO_PRIMARIO);
        boton.setFont(Tema.fuenteBoton().deriveFont(14f));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(120, 38));
    }

    private interface GeneradorReporte {
        boolean generar();
    }

    private void generarReporte(GeneradorReporte generador, String nombreArchivo) {

        boolean generado = generador.generar();

        if (generado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Reporte generado correctamente: " + nombreArchivo
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo generar el reporte."
            );
        }
    }

    private void generarReportePagos() {

        LocalDate desde;
        LocalDate hasta;

        try {

            desde = LocalDate.parse(txtDesde.getText().trim());
            hasta = LocalDate.parse(txtHasta.getText().trim());

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Las fechas deben tener el formato AAAA-MM-DD."
            );

            return;
        }

        if (hasta.isBefore(desde)) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha 'Hasta' no puede ser anterior a 'Desde'."
            );

            return;
        }

        boolean generado =
                ExcelReportGenerator.generarReportePagos(desde, hasta);

        if (generado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Reporte generado correctamente: ReportePagos.xlsx"
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo generar el reporte."
            );
        }
    }
}
