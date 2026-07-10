package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.PagoDAO;
import com.utp.gymcontrol.model.Pago;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class PagoView extends JFrame {

    private PagoDAO pagoDAO;

    // =========================
    // CAMPOS
    // =========================

    private JTextField txtSocioId;
    private JTextField txtMonto;
    private JTextField txtMetodoPago;
    private JTextField txtDescripcion;
    private JTextField txtMembresiaId;
    private JTextField txtBuscarId;

    // =========================
    // BOTONES
    // =========================

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnVolver;
    private JButton btnBuscarPago;

    // =========================
    // TABLA
    // =========================

    private JTable tablaPagos;
    private DefaultTableModel modeloTabla;

    public PagoView() {

        pagoDAO = new PagoDAO();

        iniciarComponentes();

        setTitle("GymControl - Gestión de Pagos");

        setSize(1200,700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(false);

        setVisible(true);

    }

    private void iniciarComponentes() {

        Font fuente =
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                );

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        panelPrincipal.setBackground(
                new Color(
                        30,
                        30,
                        30
                )
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        // =========================
        // TITULO
        // =========================

        JLabel titulo =
                new JLabel(
                        "GYMCONTROL - Gestión de Pagos",
                        SwingConstants.CENTER
                );

        titulo.setForeground(
                Color.WHITE
        );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        // =========================
        // FORMULARIO
        // =========================

        JPanel panelFormulario =
                new JPanel();

        panelFormulario.setBackground(
                new Color(
                        43,
                        43,
                        43
                )
        );

        panelFormulario.setLayout(
                new BoxLayout(
                        panelFormulario,
                        BoxLayout.Y_AXIS
                )
        );

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        // El alto ya no se fija aquí: el panel crece según su
        // contenido y el JScrollPane que lo envuelve se encarga
        // de mostrar scroll si no entra en la ventana.

        // =========================
        // BUSCAR POR ID
        // =========================

        txtBuscarId =
                crearCampo(
                        panelFormulario,
                        "Buscar por ID Pago",
                        fuente
                );

        btnBuscarPago =
                crearBoton(
                        "Buscar",
                        new Color(120,0,215)
                );

        btnBuscarPago.addActionListener(
                e -> buscarPagoPorId()
        );

        panelFormulario.add(btnBuscarPago);

        panelFormulario.add(
                Box.createVerticalStrut(20)
        );

        panelFormulario.add(
                new JSeparator()
        );

        panelFormulario.add(
                Box.createVerticalStrut(20)
        );

        txtSocioId =
                crearCampo(
                        panelFormulario,
                        "ID Socio",
                        fuente
                );

        txtMonto =
                crearCampo(
                        panelFormulario,
                        "Monto",
                        fuente
                );

        txtMetodoPago =
                crearCampo(
                        panelFormulario,
                        "Método de Pago",
                        fuente
                );

        txtDescripcion =
                crearCampo(
                        panelFormulario,
                        "Descripción",
                        fuente
                );

        txtMembresiaId =
                crearCampo(
                        panelFormulario,
                        "ID Membresía",
                        fuente
                );

        panelFormulario.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =========================
        // BOTONES
        // =========================

        btnRegistrar =
                crearBoton(
                        "Registrar",
                        new Color(
                                0,
                                120,
                                215
                        )
                );

        btnActualizar =
                crearBoton(
                        "Actualizar",
                        new Color(
                                0,
                                153,
                                51
                        )
                );

        btnEliminar =
                crearBoton(
                        "Eliminar",
                        new Color(
                                204,
                                51,
                                51
                        )
                );

        btnLimpiar =
                crearBoton(
                        "Limpiar",
                        new Color(
                                100,
                                100,
                                100
                        )
                );

        btnVolver = crearBoton(
                "Volver",
                new Color(255,140,0)
        );

        // =========================
        // EVENTOS
        // =========================

        btnRegistrar.addActionListener(
                e -> registrarPago()
        );

        btnActualizar.addActionListener(
                e -> actualizarPago()
        );

        btnEliminar.addActionListener(
                e -> eliminarPago()
        );

        btnLimpiar.addActionListener(
                e -> limpiarCampos()
        );

        btnVolver.addActionListener(e -> {

            dispose();

        });

        panelFormulario.add(btnRegistrar);

        panelFormulario.add(
                Box.createVerticalStrut(
                        10
                )
        );

        panelFormulario.add(btnActualizar);

        panelFormulario.add(
                Box.createVerticalStrut(
                        10
                )
        );

        panelFormulario.add(btnEliminar);

        panelFormulario.add(
                Box.createVerticalStrut(
                        10
                )
        );

        panelFormulario.add(btnLimpiar);

        panelFormulario.add(
                Box.createVerticalStrut(10)
        );

        panelFormulario.add(btnVolver);

        // =========================
        // TABLA
        // =========================

        modeloTabla =
                new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Socio");
        modeloTabla.addColumn("Monto");
        modeloTabla.addColumn("Método");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Membresía");

        tablaPagos =
                new JTable(
                        modeloTabla
                );

        tablaPagos.setRowHeight(
                35
        );

        tablaPagos.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        tablaPagos.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        tablaPagos.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        seleccionarPago();

                    }

                });

        JScrollPane scroll =
                new JScrollPane(
                        tablaPagos
                );

        JScrollPane scrollFormulario =
                new JScrollPane(panelFormulario);

        scrollFormulario.setPreferredSize(
                new Dimension(340,600)
        );

        scrollFormulario.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollFormulario.getVerticalScrollBar()
                .setUnitIncrement(16);

        scrollFormulario.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        panelPrincipal.add(
                scrollFormulario,
                BorderLayout.WEST
        );

        panelPrincipal.add(
                scroll,
                BorderLayout.CENTER
        );

        add(panelPrincipal);

        cargarPagos();

    }

    // =========================
    // COMPONENTES
    // =========================

    private JTextField crearCampo(
            JPanel panel,
            String texto,
            Font fuente
    ) {

        JLabel label =
                new JLabel(texto);

        label.setForeground(
                Color.WHITE
        );

        label.setFont(fuente);

        JTextField campo =
                new JTextField();

        campo.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        campo.setFont(fuente);

        panel.add(label);

        panel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        panel.add(campo);

        panel.add(
                Box.createVerticalStrut(
                        15
                )
        );

        return campo;

    }

    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton boton =
                new JButton(texto);

        boton.setBackground(color);

        boton.setForeground(
                Color.WHITE
        );

        boton.setFocusPainted(false);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        return boton;

    }
    // =========================
    // CARGAR PAGOS
    // =========================

    private void cargarPagos() {

        modeloTabla.setRowCount(0);

        List<Pago> listaPagos = pagoDAO.obtenerPagos();

        for (Pago pago : listaPagos) {

            Object[] fila = {

                    pago.getId(),
                    pago.getSocioId(),
                    pago.getMonto(),
                    pago.getMetodoPago(),
                    pago.getFechaPago(),
                    pago.getDescripcion(),
                    pago.getMembresiaId()

            };

            modeloTabla.addRow(fila);

        }

    }

    // =========================
    // SELECCIONAR
    // =========================

    private void seleccionarPago() {

        int fila = tablaPagos.getSelectedRow();

        if (fila != -1) {

            txtSocioId.setText(
                    modeloTabla.getValueAt(fila,1).toString()
            );

            txtMonto.setText(
                    modeloTabla.getValueAt(fila,2).toString()
            );

            txtMetodoPago.setText(
                    modeloTabla.getValueAt(fila,3).toString()
            );

            txtDescripcion.setText(
                    modeloTabla.getValueAt(fila,5).toString()
            );

            txtMembresiaId.setText(
                    modeloTabla.getValueAt(fila,6).toString()
            );

        }

    }

    // =========================
    // BUSCAR POR ID
    // =========================

    private void buscarPagoPorId() {

        String texto = txtBuscarId.getText().trim();

        if (StringUtils.isBlank(texto)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un ID de pago para buscar."
            );

            return;
        }

        int id;

        try {

            id = Integer.parseInt(texto);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El ID debe ser un número."
            );

            return;
        }

        Pago pago = pagoDAO.buscarPago(id);

        if (pago != null) {

            txtSocioId.setText(
                    String.valueOf(pago.getSocioId())
            );

            txtMonto.setText(
                    String.valueOf(pago.getMonto())
            );

            txtMetodoPago.setText(
                    pago.getMetodoPago()
            );

            txtDescripcion.setText(
                    pago.getDescripcion()
            );

            txtMembresiaId.setText(
                    String.valueOf(pago.getMembresiaId())
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Pago encontrado (ID " + pago.getId() + ")."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró ningún pago con ese ID."
            );
        }
    }

    // =========================
    // REGISTRAR
    // =========================

    private void registrarPago() {

        if (!validarCampos()) {

            return;

        }

        Pago pago = new Pago();

        pago.setSocioId(
                Integer.parseInt(
                        txtSocioId.getText()
                )
        );

        pago.setMonto(
                Double.parseDouble(
                        txtMonto.getText()
                )
        );

        pago.setMetodoPago(
                txtMetodoPago.getText()
        );

        pago.setDescripcion(
                txtDescripcion.getText()
        );

        pago.setMembresiaId(
                Integer.parseInt(
                        txtMembresiaId.getText()
                )
        );

        boolean registrado =
                pagoDAO.registrarPago(pago);

        if (registrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pago registrado correctamente."
            );

            cargarPagos();

            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el pago."
            );

        }

    }

    // =========================
    // ACTUALIZAR
    // =========================

    private void actualizarPago() {

        int fila = tablaPagos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pago."
            );

            return;

        }

        if (!validarCampos()) {

            return;

        }

        Pago pago = new Pago();

        pago.setId(
                Integer.parseInt(
                        modeloTabla.getValueAt(fila,0).toString()
                )
        );

        pago.setSocioId(
                Integer.parseInt(
                        txtSocioId.getText()
                )
        );

        pago.setMonto(
                Double.parseDouble(
                        txtMonto.getText()
                )
        );

        pago.setMetodoPago(
                txtMetodoPago.getText()
        );

        pago.setDescripcion(
                txtDescripcion.getText()
        );

        pago.setMembresiaId(
                Integer.parseInt(
                        txtMembresiaId.getText()
                )
        );

        boolean actualizado =
                pagoDAO.actualizarPago(pago);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pago actualizado correctamente."
            );

            cargarPagos();

            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al actualizar."
            );

        }

    }

    // =========================
    // ELIMINAR
    // =========================

    private void eliminarPago() {

        int fila = tablaPagos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pago."
            );

            return;

        }

        int confirmar = JOptionPane.showConfirmDialog(

                this,

                "¿Eliminar el pago seleccionado?",

                "Confirmar",

                JOptionPane.YES_NO_OPTION

        );

        if (confirmar != JOptionPane.YES_OPTION) {

            return;

        }

        int id = Integer.parseInt(

                modeloTabla.getValueAt(
                        fila,
                        0
                ).toString()

        );

        boolean eliminado =
                pagoDAO.eliminarPago(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(

                    this,

                    "Pago eliminado."

            );

            cargarPagos();

            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(

                    this,

                    "No fue posible eliminar."

            );

        }

    }

    // =========================
    // LIMPIAR
    // =========================

    private void limpiarCampos() {

        txtSocioId.setText("");

        txtMonto.setText("");

        txtMetodoPago.setText("");

        txtDescripcion.setText("");

        txtMembresiaId.setText("");

        txtSocioId.requestFocus();

    }

    // =========================
    // VALIDACIONES
    // =========================

    private boolean validarCampos() {

        if (

                StringUtils.isBlank(
                        txtSocioId.getText()
                ) ||

                        StringUtils.isBlank(
                                txtMonto.getText()
                        ) ||

                        StringUtils.isBlank(
                                txtMetodoPago.getText()
                        ) ||

                        StringUtils.isBlank(
                                txtMembresiaId.getText()
                        )

        ) {

            JOptionPane.showMessageDialog(

                    this,

                    "Complete todos los campos."

            );

            return false;

        }

        try {

            Integer.parseInt(
                    txtSocioId.getText()
            );

            Integer.parseInt(
                    txtMembresiaId.getText()
            );

            double monto =
                    Double.parseDouble(
                            txtMonto.getText()
                    );

            if (monto <= 0) {

                JOptionPane.showMessageDialog(

                        this,

                        "El monto debe ser mayor que cero."

                );

                return false;

            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(

                    this,

                    "Datos numéricos inválidos."

            );

            return false;

        }

        return true;

    }

}
