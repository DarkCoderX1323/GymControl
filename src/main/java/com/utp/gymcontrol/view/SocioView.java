package com.utp.gymcontrol.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.model.Socio;
import com.utp.gymcontrol.utils.Tema;
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

    // Filtros combinados de listado
    private JTextField txtFiltroNombre;
    private JTextField txtFiltroDni;
    private JComboBox<String> cbFiltroEstado;
    private JButton btnFiltrar;
    private JButton btnQuitarFiltro;

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

        Font fuente = Tema.fuenteRegular().deriveFont(14f);

        // =========================
        // PANEL PRINCIPAL
        // =========================

        JPanel panelPrincipal = new JPanel(
                new BorderLayout(15, 15)
        );

        panelPrincipal.setBackground(Tema.FONDO);

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        // =========================
        // TITULO
        // =========================

        JLabel titulo = new JLabel(
                "GYMCONTROL - Gestión de socios",
                SwingConstants.CENTER
        );

        titulo.setForeground(Tema.TEXTO_PRIMARIO);

        titulo.setFont(
                Tema.fuenteTitulo().deriveFont(24f)
        );

        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // =========================
        // PANEL FORMULARIO
        // =========================

        JPanel panelFormulario = new JPanel();

        panelFormulario.setBackground(Tema.SUPERFICIE);

        panelFormulario.setLayout(
                new BoxLayout(
                        panelFormulario,
                        BoxLayout.Y_AXIS
                )
        );

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
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
                Tema.ACENTO
        );

        btnBuscar.addActionListener(
                e -> buscarSocioPorDni()
        );

        panelFormulario.add(btnBuscar);

        panelFormulario.add(
                Box.createVerticalStrut(20)
        );

        JSeparator separador = new JSeparator();
        separador.setForeground(Tema.SUPERFICIE_CLARA);
        separador.setBackground(Tema.SUPERFICIE_CLARA);

        panelFormulario.add(separador);

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
                Tema.ACENTO
        );

        btnActualizar = crearBoton(
                "Actualizar",
                Tema.EXITO
        );

        btnEliminar = crearBoton(
                "Eliminar",
                Tema.PELIGRO
        );

        btnLimpiar = crearBoton(
                "Limpiar",
                Tema.SUPERFICIE_CLARA
        );

        btnVolver = crearBoton(
                "Volver",
                Tema.SUPERFICIE_CLARA
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

        estilizarTabla(tablaSocios);

        tablaSocios.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        seleccionarSocio();
                    }
                });

        JScrollPane scrollPane =
                new JScrollPane(tablaSocios);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Tema.SUPERFICIE);

        // =========================
        // AGREGAR COMPONENTES
        // =========================

        JScrollPane scrollFormulario =
                new JScrollPane(panelFormulario);

        scrollFormulario.setPreferredSize(
                new Dimension(340, 600)
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

        // =========================
        // BARRA DE FILTROS COMBINADOS
        // =========================

        JPanel panelFiltros = crearPanelFiltros();

        JPanel panelTabla = new JPanel(new BorderLayout(0, 10));
        panelTabla.setBackground(Tema.FONDO);
        panelTabla.add(panelFiltros, BorderLayout.NORTH);
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        panelPrincipal.add(
                panelTabla,
                BorderLayout.CENTER
        );

        add(panelPrincipal);

        // =========================
        // CARGAR DATOS
        // =========================

        cargarSocios();
    }

    /**
     * Barra de filtros combinados (nombre + DNI + estado) sobre el listado
     * de socios. Es independiente del formulario de registro/edición.
     */
    private JPanel crearPanelFiltros() {

        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );

        panel.setBackground(Tema.SUPERFICIE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        );

        txtFiltroNombre = new JTextField(12);
        estilizarCampoFiltro(txtFiltroNombre);
        txtFiltroNombre.setToolTipText("Filtrar por nombre (parcial)");

        txtFiltroDni = new JTextField(10);
        estilizarCampoFiltro(txtFiltroDni);
        txtFiltroDni.setToolTipText("Filtrar por DNI (parcial)");

        cbFiltroEstado = new JComboBox<>(
                new String[]{"Todos", "activo", "inactivo"}
        );

        cbFiltroEstado.setBackground(Tema.SUPERFICIE_CLARA);
        cbFiltroEstado.setForeground(Tema.TEXTO_PRIMARIO);
        cbFiltroEstado.setFont(Tema.fuenteRegular().deriveFont(13f));

        btnFiltrar = new JButton("Filtrar");
        estilizarBotonFiltro(btnFiltrar, Tema.ACENTO);

        btnQuitarFiltro = new JButton("Quitar filtros");
        estilizarBotonFiltro(btnQuitarFiltro, Tema.SUPERFICIE_CLARA);

        btnFiltrar.addActionListener(e -> filtrarListado());

        btnQuitarFiltro.addActionListener(e -> quitarFiltros());

        panel.add(crearEtiquetaFiltro("Nombre"));
        panel.add(txtFiltroNombre);

        panel.add(crearEtiquetaFiltro("DNI"));
        panel.add(txtFiltroDni);

        panel.add(crearEtiquetaFiltro("Estado"));
        panel.add(cbFiltroEstado);

        panel.add(btnFiltrar);
        panel.add(btnQuitarFiltro);

        return panel;
    }

    private JLabel crearEtiquetaFiltro(String texto) {

        JLabel label = new JLabel(texto);
        label.setForeground(Tema.TEXTO_SECUNDARIO);
        label.setFont(Tema.fuenteEtiqueta());

        return label;
    }

    private void estilizarCampoFiltro(JTextField campo) {

        campo.setBackground(Tema.SUPERFICIE_CLARA);
        campo.setForeground(Tema.TEXTO_PRIMARIO);
        campo.setCaretColor(Tema.TEXTO_PRIMARIO);
        campo.setFont(Tema.fuenteRegular().deriveFont(13f));
        campo.setBorder(
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        );
    }

    private void estilizarBotonFiltro(JButton boton, Color color) {

        boton.setBackground(color);
        boton.setForeground(Tema.TEXTO_PRIMARIO);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(Tema.fuenteBoton().deriveFont(13f));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JTextField crearCampo(
            JPanel panel,
            String texto,
            Font fuente
    ) {

        JLabel label = new JLabel(texto);

        label.setForeground(Tema.TEXTO_SECUNDARIO);

        label.setFont(Tema.fuenteEtiqueta());

        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campo = new JTextField();

        campo.setMaximumSize(
                new Dimension(
                        ANCHO_CAMPO,
                        40
                )
        );

        campo.setFont(fuente);
        campo.setBackground(Tema.SUPERFICIE_CLARA);
        campo.setForeground(Tema.TEXTO_PRIMARIO);
        campo.setCaretColor(Tema.TEXTO_PRIMARIO);
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        campo.setBorder(
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        );

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

    private static final int ANCHO_CAMPO = 280;

    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton boton = new JButton(texto);

        boton.setBackground(color);

        boton.setForeground(Tema.TEXTO_PRIMARIO);

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setFont(
                Tema.fuenteBoton().deriveFont(14f)
        );

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.setAlignmentX(Component.CENTER_ALIGNMENT);

        boton.setPreferredSize(
                new Dimension(ANCHO_CAMPO, 42)
        );

        boton.setMaximumSize(
                new Dimension(ANCHO_CAMPO, 42)
        );

        return boton;
    }

    private void estilizarTabla(JTable tabla) {

        tabla.setRowHeight(32);

        tabla.setFont(
                Tema.fuenteRegular().deriveFont(13f)
        );

        tabla.setBackground(Tema.SUPERFICIE);
        tabla.setForeground(Tema.TEXTO_PRIMARIO);
        tabla.setGridColor(Tema.SUPERFICIE_CLARA);
        tabla.setSelectionBackground(Tema.ACENTO);
        tabla.setSelectionForeground(Tema.TEXTO_PRIMARIO);

        JTableHeader header = tabla.getTableHeader();

        header.setFont(
                Tema.fuenteBoton().deriveFont(13f)
        );

        header.setBackground(Tema.SUPERFICIE_CLARA);
        header.setForeground(Tema.TEXTO_PRIMARIO);
    }

    private void cargarSocios() {

        List<Socio> listaSocios = socioDAO.obtenerSocios();

        poblarTabla(listaSocios);
    }

    /**
     * Vuelca una lista de socios en la tabla, sea el listado completo o el
     * resultado de aplicar los filtros combinados.
     */
    private void poblarTabla(List<Socio> listaSocios) {

        modeloTabla.setRowCount(0);

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

    // =========================
    // FILTROS COMBINADOS
    // =========================

    private void filtrarListado() {

        String nombre = txtFiltroNombre.getText().trim();
        String dni = txtFiltroDni.getText().trim();
        String estado = (String) cbFiltroEstado.getSelectedItem();

        List<Socio> listaFiltrada =
                socioDAO.filtrarSocios(nombre, dni, estado);

        poblarTabla(listaFiltrada);

        if (listaFiltrada.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron socios con esos filtros."
            );
        }
    }

    private void quitarFiltros() {

        txtFiltroNombre.setText("");
        txtFiltroDni.setText("");
        cbFiltroEstado.setSelectedIndex(0);

        cargarSocios();
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
