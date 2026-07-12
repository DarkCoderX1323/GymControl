package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.MembresiaDAO;
import com.utp.gymcontrol.model.Membresia;
import com.utp.gymcontrol.utils.Tema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AlertaMembresiaView extends JFrame {

    // Días de anticipación para considerar una membresía "por vencer".
    private static final int DIAS_ALERTA = 3;

    private final MembresiaDAO membresiaDAO;

    private JTable tablaAlertas;
    private DefaultTableModel modeloTabla;
    private JLabel lblResumen;

    private JButton btnActualizar;
    private JButton btnVolver;

    public AlertaMembresiaView() {

        membresiaDAO = new MembresiaDAO();

        iniciarComponentes();

        setTitle("GymControl - Alertas de Membresías por Vencer");

        setSize(1000, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }

    private void iniciarComponentes() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(15, 15));

        panelPrincipal.setBackground(Tema.FONDO);

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // =========================
        // TITULO
        // =========================

        JLabel titulo =
                new JLabel(
                        "GYMCONTROL - Membresías por vencer",
                        SwingConstants.CENTER
                );

        titulo.setForeground(Tema.TEXTO_PRIMARIO);
        titulo.setFont(Tema.fuenteTitulo().deriveFont(24f));

        lblResumen = new JLabel("", SwingConstants.CENTER);
        lblResumen.setForeground(Tema.TEXTO_SECUNDARIO);
        lblResumen.setFont(Tema.fuenteEtiqueta());
        lblResumen.setBorder(
                BorderFactory.createEmptyBorder(8, 0, 0, 0)
        );

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(Tema.FONDO);
        panelTitulo.add(titulo, BorderLayout.NORTH);
        panelTitulo.add(lblResumen, BorderLayout.SOUTH);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        // =========================
        // TABLA
        // =========================

        modeloTabla =
                new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Socio");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Vence el");
        modeloTabla.addColumn("Días restantes");

        tablaAlertas = new JTable(modeloTabla);

        estilizarTabla(tablaAlertas);

        JScrollPane scroll = new JScrollPane(tablaAlertas);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Tema.SUPERFICIE);

        panelPrincipal.add(scroll, BorderLayout.CENTER);

        // =========================
        // BOTONES
        // =========================

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Tema.FONDO);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));

        btnActualizar = crearBoton("Actualizar", Tema.ACENTO);
        btnVolver = crearBoton("Volver", Tema.SUPERFICIE_CLARA);

        btnActualizar.addActionListener(e -> cargarAlertas());
        btnVolver.addActionListener(e -> dispose());

        panelBotones.add(btnActualizar);
        panelBotones.add(Box.createHorizontalStrut(15));
        panelBotones.add(btnVolver);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        cargarAlertas();
    }

    // =========================
    // COMPONENTES
    // =========================

    private JButton crearBoton(String texto, Color color) {

        JButton boton = new JButton(texto);

        boton.setBackground(color);
        boton.setForeground(Tema.TEXTO_PRIMARIO);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(160, 42));
        boton.setFont(Tema.fuenteBoton().deriveFont(14f));

        return boton;
    }

    private void estilizarTabla(JTable tabla) {

        tabla.setRowHeight(32);
        tabla.setFont(Tema.fuenteRegular().deriveFont(13f));
        tabla.setBackground(Tema.SUPERFICIE);
        tabla.setForeground(Tema.TEXTO_PRIMARIO);
        tabla.setGridColor(Tema.SUPERFICIE_CLARA);
        tabla.setSelectionBackground(Tema.ACENTO);
        tabla.setSelectionForeground(Tema.TEXTO_PRIMARIO);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(Tema.fuenteBoton().deriveFont(13f));
        header.setBackground(Tema.SUPERFICIE_CLARA);
        header.setForeground(Tema.TEXTO_PRIMARIO);
    }

    // =========================
    // CARGAR ALERTAS
    // =========================

    private void cargarAlertas() {

        modeloTabla.setRowCount(0);

        List<Membresia> lista =
                membresiaDAO.obtenerMembresiasPorVencer(DIAS_ALERTA);

        for (Membresia m : lista) {

            String diasTexto = m.getDiasRestantes() == 0
                    ? "Vence hoy"
                    : m.getDiasRestantes() + " día(s)";

            Object[] fila = {

                    m.getDniSocio(),
                    m.getNombreSocio(),
                    m.getTipo(),
                    m.getFechaFin(),
                    diasTexto

            };

            modeloTabla.addRow(fila);
        }

        lblResumen.setText(
                lista.isEmpty()
                        ? "No hay membresías por vencer en los próximos "
                        + DIAS_ALERTA + " días."
                        : lista.size()
                        + " membresía(s) vencen en los próximos "
                        + DIAS_ALERTA + " días."
        );
    }
}
