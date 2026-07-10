package com.utp.gymcontrol.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.model.Socio;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class SocioView extends JFrame {

    private SocioDAO socioDAO;

    // Campos de texto
    private JTextField txtNombre;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtBuscarDni;

    // Botones
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnVolver;
    private JButton btnBuscar;

    // Tabla
    private JTable tablaSocios;
    private DefaultTableModel modeloTabla;

    public SocioView() {

        socioDAO = new SocioDAO();

        iniciarComponentes();

        setTitle("GymControl - Gestión de Socios");

        setSize(1200, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }

    private void iniciarComponentes() {

        Font fuente = new Font("Segoe UI", Font.PLAIN, 15);

        // =========================
        // PANEL PRINCIPAL
        // =========================

        JPanel panelPrincipal = new JPanel(
                new BorderLayout(15,15)
        );

        panelPrincipal.setBackground(
                new Color(30,30,30)
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        15,15,15,15
                )
        );

        // =========================
        // TITULO
        // =========================

        JLabel titulo = new JLabel(
                "GYMCONTROL - Gestión de Socios",
                SwingConstants.CENTER
        );

        titulo.setForeground(Color.WHITE);

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // =========================
        // PANEL FORMULARIO
        // =========================

        JPanel panelFormulario = new JPanel();

        panelFormulario.setBackground(
                new Color(43,43,43)
        );

        panelFormulario.setLayout(
                new BoxLayout(
                        panelFormulario,
                        BoxLayout.Y_AXIS
                )
        );

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );

        // El alto ya no se fija aquí: el panel crece según su
        // contenido y el JScrollPane que lo envuelve se encarga
        // de mostrar scroll si no entra en la ventana.

        // =========================
        // BUSCAR POR DNI
        // =========================

        txtBuscarDni = crearCampo(
                panelFormulario,
                "Buscar por DNI",
                fuente
        );

        btnBuscar = crearBoton(
                "Buscar",
                new Color(120,0,215)
        );

        btnBuscar.addActionListener(
                e -> buscarSocioPorDni()
        );

        panelFormulario.add(btnBuscar);

        panelFormulario.add(
                Box.createVerticalStrut(20)
        );

        panelFormulario.add(
                new JSeparator()
        );

        panelFormulario.add(
                Box.createVerticalStrut(20)
        );

        // =========================
        // CAMPOS
        // =========================

        txtNombre = crearCampo(
                panelFormulario,
                "Nombre",
                fuente
        );

        txtDni = crearCampo(
                panelFormulario,
                "DNI",
                fuente
        );

        txtTelefono = crearCampo(
                panelFormulario,
                "Teléfono",
                fuente
        );

        txtEmail = crearCampo(
                panelFormulario,
                "Email",
                fuente
        );

        // =========================
        // BOTONES
        // =========================

        panelFormulario.add(
                Box.createVerticalStrut(20)
        );

        btnRegistrar = crearBoton(
                "Registrar",
                new Color(0,120,215)
        );

        btnActualizar = crearBoton(
                "Actualizar",
                new Color(0,153,51)
        );

        btnEliminar = crearBoton(
                "Eliminar",
                new Color(204,51,51)
        );

        btnLimpiar = crearBoton(
                "Limpiar",
                new Color(100,100,100)
        );

        btnVolver = crearBoton(
                "Volver",
                new Color(255,140,0)
        );

        // =========================
        // EVENTOS
        // =========================

        btnRegistrar.addActionListener(
                e -> registrarSocio()
        );

        btnActualizar.addActionListener(
                e -> actualizarSocio()
        );

        btnEliminar.addActionListener(
                e -> eliminarSocio()
        );

        btnLimpiar.addActionListener(
                e -> limpiarCampos()
        );

        btnVolver.addActionListener(e -> {

            dispose();

        });

        // =========================
        // AGREGAR BOTONES
        // =========================

        panelFormulario.add(btnRegistrar);

        panelFormulario.add(
                Box.createVerticalStrut(10)
        );

        panelFormulario.add(btnActualizar);

        panelFormulario.add(
                Box.createVerticalStrut(10)
        );

        panelFormulario.add(btnEliminar);

        panelFormulario.add(
                Box.createVerticalStrut(10)
        );

        panelFormulario.add(btnLimpiar);

        panelFormulario.add(
                Box.createVerticalStrut(10)
        );

        panelFormulario.add(btnVolver);

        // =========================
        // TABLA
        // =========================

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Email");
        modeloTabla.addColumn("Estado");

        tablaSocios = new JTable(modeloTabla);

        tablaSocios.setRowHeight(35);

        tablaSocios.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        tablaSocios.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        tablaSocios.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        seleccionarSocio();
                    }
                });

        JScrollPane scrollPane =
                new JScrollPane(tablaSocios);

        // =========================
        // AGREGAR COMPONENTES
        // =========================

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
                scrollPane,
                BorderLayout.CENTER
        );

        add(panelPrincipal);

        // =========================
        // CARGAR DATOS
        // =========================

        cargarSocios();
    }

    private JTextField crearCampo(
            JPanel panel,
            String texto,
            Font fuente
    ) {

        JLabel label = new JLabel(texto);

        label.setForeground(Color.WHITE);

        label.setFont(fuente);

        JTextField campo = new JTextField();

        campo.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        campo.setFont(fuente);

        panel.add(label);

        panel.add(
                Box.createVerticalStrut(5)
        );

        panel.add(campo);

        panel.add(
                Box.createVerticalStrut(15)
        );

        return campo;
    }

    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton boton = new JButton(texto);

        boton.setBackground(color);

        boton.setForeground(Color.WHITE);

        boton.setFocusPainted(false);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

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

        return boton;
    }

    private void cargarSocios() {

        modeloTabla.setRowCount(0);

        List<Socio> listaSocios = socioDAO.obtenerSocios();

        for (Socio socio : listaSocios) {

            Object[] fila = {

                    socio.getId(),
                    socio.getNombre(),
                    socio.getDni(),
                    socio.getTelefono(),
                    socio.getEmail(),
                    socio.getEstado()
            };

            modeloTabla.addRow(fila);
        }
    }

    private void seleccionarSocio() {

        int filaSeleccionada = tablaSocios.getSelectedRow();

        if (filaSeleccionada != -1) {

            txtNombre.setText(
                    modeloTabla.getValueAt(filaSeleccionada, 1).toString()
            );

            txtDni.setText(
                    modeloTabla.getValueAt(filaSeleccionada, 2).toString()
            );

            txtTelefono.setText(
                    modeloTabla.getValueAt(filaSeleccionada, 3).toString()
            );

            txtEmail.setText(
                    modeloTabla.getValueAt(filaSeleccionada, 4).toString()
            );
        }
    }

    private void buscarSocioPorDni() {

        String dni = txtBuscarDni.getText().trim();

        if (StringUtils.isBlank(dni)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un DNI para buscar."
            );

            return;
        }

        Socio socio = socioDAO.buscarPorDni(dni);

        if (socio != null) {

            txtNombre.setText(socio.getNombre());
            txtDni.setText(socio.getDni());
            txtTelefono.setText(socio.getTelefono());
            txtEmail.setText(socio.getEmail());

            JOptionPane.showMessageDialog(
                    this,
                    "Socio encontrado: " + socio.getNombre()
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró ningún socio con ese DNI."
            );
        }
    }

    private void registrarSocio() {

        String nombre = txtNombre.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();

        if (StringUtils.isBlank(nombre) ||
                StringUtils.isBlank(dni) ||
                StringUtils.isBlank(telefono)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos obligatorios."
            );

            return;
        }

        if (!validarCampos(nombre, dni, telefono)) {
            return;
        }

        Socio socio = new Socio();

        socio.setNombre(nombre);
        socio.setDni(dni);
        socio.setTelefono(telefono);
        socio.setEmail(email);
        socio.setEstado("activo");

        boolean registrado =
                socioDAO.registrarSocio(socio);

        if (registrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Socio registrado correctamente."
            );

            cargarSocios();

            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar socio."
            );
        }
    }

    private void actualizarSocio() {

        int filaSeleccionada =
                tablaSocios.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un socio para actualizar."
            );

            return;
        }

        String nombre = txtNombre.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();

        if (StringUtils.isBlank(nombre) ||
                StringUtils.isBlank(dni) ||
                StringUtils.isBlank(telefono)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos obligatorios."
            );

            return;
        }

        if (!validarCampos(nombre, dni, telefono)) {
            return;
        }

        Socio socio = new Socio();

        socio.setNombre(nombre);
        socio.setDni(dni);
        socio.setTelefono(telefono);
        socio.setEmail(email);
        socio.setEstado("activo");

        boolean actualizado =
                socioDAO.actualizarSocio(socio);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Socio actualizado correctamente."
            );

            cargarSocios();

            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al actualizar socio."
            );
        }
    }

    private void eliminarSocio() {

        int filaSeleccionada =
                tablaSocios.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un socio para eliminar."
            );

            return;
        }

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar este socio?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        int id = Integer.parseInt(
                modeloTabla.getValueAt(
                        filaSeleccionada,
                        0
                ).toString()
        );

        boolean eliminado =
                socioDAO.eliminarSocio(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Socio eliminado correctamente."
            );

            cargarSocios();

            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar socio."
            );
        }
    }

    private void limpiarCampos() {

        txtNombre.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");

        txtNombre.requestFocus();
    }

    private boolean validarCampos(
            String nombre,
            String dni,
            String telefono
    ) {

        if (StringUtils.isBlank(nombre) ||
                StringUtils.isBlank(dni) ||
                StringUtils.isBlank(telefono)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Existen campos vacíos."
            );

            return false;
        }

        if (!nombre.matches(
                "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre solo debe contener letras."
            );

            return false;
        }

        if (!dni.matches("\\d{8}")) {

            JOptionPane.showMessageDialog(
                    this,
                    "El DNI debe contener exactamente 8 números."
            );

            return false;
        }

        if (!telefono.matches("\\d{9}")) {

            JOptionPane.showMessageDialog(
                    this,
                    "El teléfono debe contener exactamente 9 números."
            );

            return false;
        }

        return true;
    }
}