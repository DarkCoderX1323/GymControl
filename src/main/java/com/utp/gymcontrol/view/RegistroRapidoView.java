package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.RegistroRapidoDAO;
import com.utp.gymcontrol.utils.DashboardManager;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.dao.TipoMembresiaDAO;
import com.utp.gymcontrol.model.Socio;
import com.utp.gymcontrol.model.TipoMembresia;
import com.utp.gymcontrol.utils.Tema;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Registro rápido: crea un socio nuevo, su membresía y el pago
 * correspondiente en una sola pantalla, en vez de tener que pasar por
 * SocioView, MembresiaView y PagoView por separado copiando IDs a mano.
 *
 * La opción de "socio existente" se sacó de acá: insertaba una membresía
 * y un pago nuevos para un socio que ya tenía una, en vez de renovarla,
 * dejando dos membresías para el mismo socio. Para renovar o cambiar de
 * plan a un socio existente, usar MembresiaView (seleccionar la fila →
 * cambiar tipo → Actualizar membresía).
 */
public class RegistroRapidoView extends JFrame {

    private static final int ANCHO_CAMPO = 320;

    private final RegistroRapidoDAO registroRapidoDAO;
    private final SocioDAO socioDAO;
    private final TipoMembresiaDAO tipoMembresiaDAO;

    // =========================
    // SOCIO
    // =========================

    private JTextField txtNombre;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    // =========================
    // MEMBRESIA
    // =========================

    private JComboBox<TipoMembresia> cbTipo;
    private JLabel lblFechas;

    // =========================
    // PAGO
    // =========================

    private JLabel lblMonto;
    private JComboBox<String> cbMetodoPago;

    // =========================
    // BOTONES
    // =========================

    private JButton btnRegistrarTodo;
    private JButton btnLimpiar;
    private JButton btnVolver;

    public RegistroRapidoView() {

        registroRapidoDAO = new RegistroRapidoDAO();
        socioDAO = new SocioDAO();
        tipoMembresiaDAO = new TipoMembresiaDAO();

        iniciarComponentes();

        setTitle("GymControl - Registro Rápido");

        setSize(650, 720);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }

    private void iniciarComponentes() {

        JPanel principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBackground(Tema.FONDO);
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        // =========================
        // TITULO
        // =========================

        JLabel titulo = new JLabel(
                "GYMCONTROL - Registro rápido",
                SwingConstants.CENTER
        );

        titulo.setForeground(Tema.TEXTO_PRIMARIO);
        titulo.setFont(Tema.fuenteTitulo().deriveFont(22f));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Alta de socio nuevo: socio + membresía + pago, todo en un solo paso",
                SwingConstants.CENTER
        );

        subtitulo.setForeground(Tema.TEXTO_SECUNDARIO);
        subtitulo.setFont(Tema.fuenteEtiqueta());
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setBorder(
                BorderFactory.createEmptyBorder(4, 0, 20, 0)
        );

        principal.add(titulo);
        principal.add(subtitulo);

        // =========================
        // SECCION 1: SOCIO
        // =========================

        principal.add(crearSeccionSocio());
        principal.add(Box.createVerticalStrut(20));

        // =========================
        // SECCION 2: MEMBRESIA
        // =========================

        principal.add(crearSeccionMembresia());
        principal.add(Box.createVerticalStrut(20));

        // =========================
        // SECCION 3: PAGO
        // =========================

        principal.add(crearSeccionPago());
        principal.add(Box.createVerticalStrut(20));

        // =========================
        // BOTONES
        // =========================

        btnRegistrarTodo = crearBoton("Registrar todo", Tema.ACENTO);
        btnLimpiar = crearBoton("Limpiar", Tema.SUPERFICIE_CLARA);
        btnVolver = crearBoton("Volver", Tema.SUPERFICIE_CLARA);

        btnRegistrarTodo.addActionListener(e -> registrarTodo());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnVolver.addActionListener(e -> dispose());

        btnRegistrarTodo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLimpiar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);

        principal.add(btnRegistrarTodo);
        principal.add(Box.createVerticalStrut(10));
        principal.add(btnLimpiar);
        principal.add(Box.createVerticalStrut(10));
        principal.add(btnVolver);

        JScrollPane scroll = new JScrollPane(principal);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Tema.FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);

        cargarTiposMembresia();
    }

    // =========================
    // SECCION SOCIO
    // =========================

    private JPanel crearSeccionSocio() {

        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(Tema.SUPERFICIE);
        seccion.setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
        seccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = crearTituloSeccion("1. Socio");

        // Se sacó la opción "Socio existente": estaba insertando una
        // membresía y un pago nuevos para un socio que ya tenía una
        // membresía, en vez de renovarla -- terminaba con dos membresías
        // para el mismo socio. Ese caso (renovar o cambiar de plan a un
        // socio existente) ya lo cubre MembresiaView (seleccionar la fila
        // → cambiar tipo → Actualizar membresía), que sí actualiza en vez
        // de duplicar. Este formulario queda solo para el alta de un
        // socio nuevo.
        JPanel panelSocioNuevo = crearPanelSocioNuevo();

        seccion.add(titulo);
        seccion.add(Box.createVerticalStrut(10));
        seccion.add(panelSocioNuevo);

        return seccion;
    }

    private JPanel crearPanelSocioNuevo() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Tema.SUPERFICIE);

        txtNombre = crearCampoConEtiqueta(panel, "Nombre completo");
        txtDni = crearCampoConEtiqueta(panel, "DNI");
        txtTelefono = crearCampoConEtiqueta(panel, "Teléfono");
        txtEmail = crearCampoConEtiqueta(panel, "Email");

        return panel;
    }

    // =========================
    // SECCION MEMBRESIA
    // =========================

    private JPanel crearSeccionMembresia() {

        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(Tema.SUPERFICIE);
        seccion.setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
        seccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = crearTituloSeccion("2. Membresía");

        JLabel labelTipo = new JLabel("Tipo de membresía");
        labelTipo.setForeground(Tema.TEXTO_SECUNDARIO);
        labelTipo.setFont(Tema.fuenteEtiqueta());
        labelTipo.setAlignmentX(Component.CENTER_ALIGNMENT);

        cbTipo = new JComboBox<>();
        cbTipo.setMaximumSize(new Dimension(ANCHO_CAMPO, 40));
        cbTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        estilizarCombo(cbTipo);

        cbTipo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus
            ) {

                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                if (value instanceof TipoMembresia tipo) {

                    setText(String.format(
                            "%s — %d días — S/ %.2f",
                            tipo.getNombre(),
                            tipo.getDuracionDias(),
                            tipo.getPrecio()
                    ));
                }

                return this;
            }
        });

        cbTipo.addActionListener(e -> actualizarResumen());

        lblFechas = new JLabel(" ");
        lblFechas.setForeground(Tema.TEXTO_SECUNDARIO);
        lblFechas.setFont(Tema.fuenteEtiqueta());
        lblFechas.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFechas.setBorder(
                BorderFactory.createEmptyBorder(8, 0, 0, 0)
        );

        seccion.add(titulo);
        seccion.add(Box.createVerticalStrut(10));
        seccion.add(labelTipo);
        seccion.add(Box.createVerticalStrut(5));
        seccion.add(cbTipo);
        seccion.add(lblFechas);

        return seccion;
    }

    // =========================
    // SECCION PAGO
    // =========================

    private JPanel crearSeccionPago() {

        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(Tema.SUPERFICIE);
        seccion.setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
        seccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = crearTituloSeccion("3. Pago");

        lblMonto = new JLabel("Monto a pagar: S/ 0.00");
        lblMonto.setForeground(Tema.TEXTO_PRIMARIO);
        lblMonto.setFont(Tema.fuenteTitulo().deriveFont(16f));
        lblMonto.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMonto.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 12, 0)
        );

        JLabel labelMetodo = new JLabel("Método de pago");
        labelMetodo.setForeground(Tema.TEXTO_SECUNDARIO);
        labelMetodo.setFont(Tema.fuenteEtiqueta());
        labelMetodo.setAlignmentX(Component.CENTER_ALIGNMENT);

        cbMetodoPago = new JComboBox<>(
                new String[]{"efectivo", "tarjeta", "yape", "plin"}
        );
        cbMetodoPago.setMaximumSize(new Dimension(ANCHO_CAMPO, 40));
        cbMetodoPago.setAlignmentX(Component.CENTER_ALIGNMENT);
        estilizarCombo(cbMetodoPago);

        seccion.add(titulo);
        seccion.add(Box.createVerticalStrut(10));
        seccion.add(lblMonto);
        seccion.add(labelMetodo);
        seccion.add(Box.createVerticalStrut(5));
        seccion.add(cbMetodoPago);

        return seccion;
    }

    // =========================
    // COMPONENTES AUXILIARES
    // =========================

    private JLabel crearTituloSeccion(String texto) {

        JLabel label = new JLabel(texto);
        label.setForeground(Tema.TEXTO_PRIMARIO);
        label.setFont(Tema.fuenteBoton().deriveFont(15f));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    private JTextField crearCampoConEtiqueta(JPanel panel, String texto) {

        JLabel label = new JLabel(texto);
        label.setForeground(Tema.TEXTO_SECUNDARIO);
        label.setFont(Tema.fuenteEtiqueta());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campo = new JTextField();
        campo.setMaximumSize(new Dimension(ANCHO_CAMPO, 40));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        campo.setBackground(Tema.SUPERFICIE_CLARA);
        campo.setForeground(Tema.TEXTO_PRIMARIO);
        campo.setCaretColor(Tema.TEXTO_PRIMARIO);
        campo.setBorder(
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        );

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(12));

        return campo;
    }

    private void estilizarCombo(JComboBox<?> combo) {

        combo.setBackground(Tema.SUPERFICIE_CLARA);
        combo.setForeground(Tema.TEXTO_PRIMARIO);
        combo.setFont(Tema.fuenteRegular().deriveFont(13f));
    }

    private JButton crearBoton(String texto, Color color) {

        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Tema.TEXTO_PRIMARIO);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMaximumSize(new Dimension(ANCHO_CAMPO, 42));
        boton.setPreferredSize(new Dimension(ANCHO_CAMPO, 42));
        boton.setFont(Tema.fuenteBoton().deriveFont(14f));

        return boton;
    }

    // =========================
    // CARGA DE DATOS
    // =========================

    private void cargarTiposMembresia() {

        cbTipo.removeAllItems();

        List<TipoMembresia> tipos =
                tipoMembresiaDAO.obtenerTiposMembresia();

        for (TipoMembresia tipo : tipos) {
            cbTipo.addItem(tipo);
        }

        actualizarResumen();
    }

    private void actualizarResumen() {

        TipoMembresia tipo = (TipoMembresia) cbTipo.getSelectedItem();

        if (tipo == null) {

            lblMonto.setText("Monto a pagar: S/ 0.00");
            lblFechas.setText(" ");

            return;
        }

        lblMonto.setText(
                String.format("Monto a pagar: S/ %.2f", tipo.getPrecio())
        );

        LocalDate inicio = LocalDate.now();
        LocalDate fin = inicio.plusDays(tipo.getDuracionDias());

        lblFechas.setText(
                "Del " + inicio + " al " + fin
        );
    }

    // =========================
    // REGISTRAR TODO
    // =========================

    private void registrarTodo() {

        TipoMembresia tipo = (TipoMembresia) cbTipo.getSelectedItem();

        if (tipo == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay tipos de membresía disponibles."
            );

            return;
        }

        String metodoPago = (String) cbMetodoPago.getSelectedItem();

        String nombre = txtNombre.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();

        if (StringUtils.isBlank(nombre) || StringUtils.isBlank(dni)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nombre y DNI son obligatorios para un socio nuevo."
            );

            return;
        }

        Socio socioNuevo = new Socio();
        socioNuevo.setNombre(nombre);
        socioNuevo.setDni(dni);
        socioNuevo.setTelefono(telefono);
        socioNuevo.setEmail(email);

        try {

            RegistroRapidoDAO.Resultado resultado =
                    registroRapidoDAO.registrarTodo(
                            socioNuevo, null, tipo, metodoPago
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "Registro completo.\n\n"
                            + "Socio ID: " + resultado.socioId + "\n"
                            + "Membresía ID: " + resultado.membresiaId + "\n"
                            + "Pago ID: " + resultado.pagoId
            );
            DashboardManager.actualizar();
            limpiarFormulario();

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo completar el registro. No se guardó "
                            + "ningún dato (socio, membresía o pago).\n"
                            + "Detalle: " + e.getMessage()
            );
        }
    }

    private void limpiarFormulario() {

        txtNombre.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");

        if (cbTipo.getItemCount() > 0) {
            cbTipo.setSelectedIndex(0);
        }

        cbMetodoPago.setSelectedIndex(0);

        actualizarResumen();
    }
}
