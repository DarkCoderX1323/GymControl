package com.utp.gymcontrol.view;

import com.utp.gymcontrol.utils.DashboardManager;
import com.utp.gymcontrol.dao.MembresiaDAO;
import com.utp.gymcontrol.dao.PagoDAO;
import com.utp.gymcontrol.dao.TipoMembresiaDAO;
import com.utp.gymcontrol.model.Membresia;
import com.utp.gymcontrol.model.TipoMembresia;
import com.utp.gymcontrol.utils.Tema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista de administración de membresías: ver, filtrar, editar (cambiar
 * tipo/fechas) y eliminar membresías ya existentes.
 *
 * A propósito, esta vista YA NO crea membresías nuevas: ese flujo vive en
 * RegistroRapidoView, que registra socio + membresía + pago juntos en una
 * sola transacción. Tener un "Registrar membresía" suelto acá (sin pago
 * asociado) era redundante con Registro rápido y generaba confusión, así
 * que se sacó — ver el hilo de conversación donde se decidió esto.
 */
public class MembresiaView extends JFrame {

    private JLabel lblSocioValor;
    private JComboBox<TipoMembresia> cbTipos;

    private JTextField txtInicio;
    private JTextField txtFin;

    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnVolver;

    // Filtros combinados de listado
    private JTextField txtFiltroNombre;
    private JComboBox<String> cbFiltroEstado;
    private JComboBox<String> cbFiltroTipo;
    private JButton btnFiltrar;
    private JButton btnQuitarFiltro;

    private JTable tabla;
    private DefaultTableModel modelo;

    // Guarda la última lista cargada (completa o filtrada) en el mismo
    // orden que las filas de la tabla, para poder recuperar el objeto
    // Membresia real al hacer click en una fila (sin tener que reparsear
    // texto de celdas).
    private List<Membresia> membresiasCargadas;

    // null = no hay ninguna membresía seleccionada (formulario inactivo).
    // no-null = el formulario está editando la membresía con ese id.
    private Integer idEnEdicion;

    // true si la membresía cargada en el formulario estaba vencida al
    // seleccionarla (o sea, esto es una renovación real con cobro nuevo),
    // false si era una edición de una membresía todavía activa. Se usa
    // para decidir si el pago vinculado también debe actualizar su
    // fecha_pago a hoy (y así reflejarse en "Ingresos del mes").
    private boolean idEnEdicionEraVencida;

    private JLabel lblModoFormulario;

    private MembresiaDAO membresiaDAO;
    private TipoMembresiaDAO tipoDAO;
    private PagoDAO pagoDAO;

    public MembresiaView() {

        membresiaDAO = new MembresiaDAO();
        tipoDAO = new TipoMembresiaDAO();
        pagoDAO = new PagoDAO();

        iniciarComponentes();

        setTitle("GymControl - Membresías");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void iniciarComponentes() {

        // =========================
        // PANEL PRINCIPAL
        // =========================

        JPanel principal =
                new JPanel(new BorderLayout(15, 15));

        principal.setBackground(Tema.FONDO);

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        // =========================
        // TITULO
        // =========================

        JLabel titulo = new JLabel("GYMCONTROL - Membresías");
        titulo.setForeground(Tema.TEXTO_PRIMARIO);
        titulo.setFont(Tema.fuenteTitulo().deriveFont(24f));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        principal.add(titulo, BorderLayout.NORTH);

        // =========================
        // FORMULARIO (editar / eliminar la membresía seleccionada)
        // =========================

        JPanel formulario =
                new JPanel(new GridBagLayout());

        formulario.setBackground(Tema.SUPERFICIE);

        formulario.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 18, 18, 18
                )
        );

        lblModoFormulario = new JLabel(
                "Ningún registro seleccionado — hacé click en una fila de la tabla para editarla"
        );
        lblModoFormulario.setForeground(Tema.TEXTO_SECUNDARIO);
        lblModoFormulario.setFont(Tema.fuenteEtiqueta().deriveFont(Font.BOLD, 13f));
        lblModoFormulario.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        lblSocioValor = new JLabel("—");
        lblSocioValor.setForeground(Tema.TEXTO_PRIMARIO);
        lblSocioValor.setFont(Tema.fuenteRegular().deriveFont(14f));

        cbTipos = new JComboBox<>();
        estilizarCombo(cbTipos);
        cbTipos.setEnabled(false);

        txtInicio = new JTextField();
        txtFin = new JTextField();

        // Editable: por defecto se autocompleta (hoy para renovar una
        // vencida, la fecha original para editar una activa), pero el
        // staff puede ajustarla a mano si hace falta -- por ejemplo, para
        // dejar constancia de que el socio pagó unos días antes o después
        // de la fecha "ideal".
        txtFin.setEditable(false);

        txtInicio.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                calcularFechaFin();
            }
        });

        estilizarCampo(txtInicio);
        estilizarCampo(txtFin);

        btnActualizar =
                new JButton("Actualizar membresía");

        estilizarBoton(btnActualizar, Tema.EXITO);

        btnEliminar =
                new JButton("Eliminar membresía");

        estilizarBoton(btnEliminar, Tema.PELIGRO);

        btnVolver = new JButton("Volver");

        estilizarBoton(btnVolver, Tema.SUPERFICIE_CLARA);

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Fila 0: etiqueta de modo (sin selección / editando X), ocupa
        // las dos columnas.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        formulario.add(lblModoFormulario, gbc);
        gbc.gridwidth = 1;

        // Columna izquierda: etiquetas (ancho fijo, no se estiran)
        // Columna derecha: campos/combos (ocupan el espacio restante)
        gbc.gridx = 0;
        gbc.weightx = 0;

        gbc.gridy = 1;
        formulario.add(crearEtiqueta("Socio"), gbc);

        gbc.gridy = 2;
        formulario.add(crearEtiqueta("Tipo de membresía"), gbc);

        gbc.gridy = 3;
        formulario.add(crearEtiqueta("Fecha inicio"), gbc);

        gbc.gridy = 4;
        formulario.add(crearEtiqueta("Fecha fin"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        gbc.gridy = 1;
        formulario.add(lblSocioValor, gbc);

        gbc.gridy = 2;
        formulario.add(cbTipos, gbc);

        gbc.gridy = 3;
        formulario.add(txtInicio, gbc);

        gbc.gridy = 4;
        formulario.add(txtFin, gbc);

        // Botones: compactos y centrados, en un sub-panel aparte para que
        // no hereden el ancho de las columnas del formulario
        btnActualizar.setPreferredSize(new Dimension(190, 36));
        btnEliminar.setPreferredSize(new Dimension(180, 36));
        btnVolver.setPreferredSize(new Dimension(110, 36));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(16, 6, 6, 6);
        formulario.add(panelBotones, gbc);

        gbc.gridwidth = 1;

        // =========================
        // TABLA
        // =========================

        modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Socio");
        modelo.addColumn("Tipo");
        modelo.addColumn("Inicio");
        modelo.addColumn("Fin");
        modelo.addColumn("Estado");

        tabla = new JTable(modelo);

        estilizarTabla(tabla);

        tabla.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarSeleccionEnFormulario();
            }
        });

        JScrollPane scroll =
                new JScrollPane(tabla);

        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Tema.SUPERFICIE);

        principal.add(formulario,
                BorderLayout.NORTH);

        JPanel panelFiltros = crearPanelFiltros();

        JPanel panelTabla = new JPanel(new BorderLayout(0, 10));
        panelTabla.setBackground(Tema.FONDO);
        panelTabla.add(panelFiltros, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        principal.add(panelTabla,
                BorderLayout.CENTER);

        add(principal);

        cargarTipos();
        cargarMembresias();

        cbTipos.addActionListener(
                e -> calcularFechaFin()
        );

        btnActualizar.addActionListener(
                e -> actualizarMembresiaSeleccionada()
        );

        btnEliminar.addActionListener(
                e -> eliminarMembresiaSeleccionada()
        );

        btnVolver.addActionListener(e -> {

            dispose();

        });
    }

    // =========================
    // HELPERS DE ESTILO
    // =========================

    private JLabel crearEtiqueta(String texto) {

        JLabel label = new JLabel(texto);
        label.setForeground(Tema.TEXTO_SECUNDARIO);
        label.setFont(Tema.fuenteEtiqueta());

        return label;
    }

    private void estilizarCampo(JTextField campo) {

        campo.setBackground(Tema.SUPERFICIE_CLARA);
        campo.setForeground(Tema.TEXTO_PRIMARIO);
        campo.setCaretColor(Tema.TEXTO_PRIMARIO);
        campo.setFont(Tema.fuenteRegular().deriveFont(14f));
        campo.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
    }

    private void estilizarCombo(JComboBox<?> combo) {

        combo.setBackground(Tema.SUPERFICIE_CLARA);
        combo.setForeground(Tema.TEXTO_PRIMARIO);
        combo.setFont(Tema.fuenteRegular().deriveFont(14f));
    }

    private void estilizarBoton(JButton boton, Color colorFondo) {

        boton.setBackground(colorFondo);
        boton.setForeground(Tema.TEXTO_PRIMARIO);
        boton.setFont(Tema.fuenteBoton().deriveFont(14f));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void estilizarTabla(JTable tabla) {

        tabla.setBackground(Tema.SUPERFICIE);
        tabla.setForeground(Tema.TEXTO_PRIMARIO);
        tabla.setGridColor(Tema.SUPERFICIE_CLARA);
        tabla.setRowHeight(32);
        tabla.setFont(Tema.fuenteRegular().deriveFont(13f));
        tabla.setSelectionBackground(Tema.ACENTO);
        tabla.setSelectionForeground(Tema.TEXTO_PRIMARIO);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(Tema.SUPERFICIE_CLARA);
        header.setForeground(Tema.TEXTO_PRIMARIO);
        header.setFont(Tema.fuenteBoton().deriveFont(13f));
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

    /**
     * Barra de filtros combinados (nombre/DNI + estado + tipo) sobre el
     * listado de membresías. Es independiente del formulario de edición.
     */
    private JPanel crearPanelFiltros() {

        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );

        panel.setBackground(Tema.SUPERFICIE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        );

        txtFiltroNombre = new JTextField(14);
        estilizarCampoFiltro(txtFiltroNombre);
        txtFiltroNombre.setToolTipText("Filtrar por nombre o DNI del socio");

        cbFiltroEstado = new JComboBox<>(
                new String[]{"Todos", "activa", "vencida"}
        );

        estilizarCombo(cbFiltroEstado);

        cbFiltroTipo = new JComboBox<>(
                new String[]{"Todos", "Mensual", "Trimestral", "Anual"}
        );

        estilizarCombo(cbFiltroTipo);

        btnFiltrar = new JButton("Filtrar");
        estilizarBotonFiltro(btnFiltrar, Tema.ACENTO);

        btnQuitarFiltro = new JButton("Quitar filtros");
        estilizarBotonFiltro(btnQuitarFiltro, Tema.SUPERFICIE_CLARA);

        btnFiltrar.addActionListener(e -> filtrarListado());

        btnQuitarFiltro.addActionListener(e -> quitarFiltros());

        panel.add(crearEtiquetaFiltro("Nombre o DNI"));
        panel.add(txtFiltroNombre);

        panel.add(crearEtiquetaFiltro("Estado"));
        panel.add(cbFiltroEstado);

        panel.add(crearEtiquetaFiltro("Tipo"));
        panel.add(cbFiltroTipo);

        panel.add(btnFiltrar);
        panel.add(btnQuitarFiltro);

        return panel;
    }

    // =========================
    // LOGICA
    // =========================

    private void cargarTipos() {

        cbTipos.removeAllItems();

        try {

            List<TipoMembresia> tipos =
                    tipoDAO.obtenerTiposMembresia();

            for (TipoMembresia t : tipos) {

                cbTipos.addItem(t);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error cargando tipos"
            );

            e.printStackTrace();
        }
    }

    private void calcularFechaFin() {

        // Mientras no haya ninguna membresía seleccionada, el combo de
        // tipo está deshabilitado y no hay fecha de inicio real que usar
        // como base — no hay nada que recalcular todavía.
        if (idEnEdicion == null) {
            return;
        }

        try {

            TipoMembresia tipo =
                    (TipoMembresia)
                            cbTipos.getSelectedItem();

            if (tipo == null) {
                return;
            }

            // Usa la fecha de inicio real de la membresía seleccionada
            // (no "hoy") como base, para que cambiar el tipo mientras se
            // edita recalcule la fecha fin respetando cuándo empezó
            // realmente esa membresía.
            LocalDate inicio =
                    LocalDate.parse(
                            txtInicio.getText().trim()
                    );

            LocalDate fin =
                    inicio.plusDays(
                            tipo.getDuracionDias()
                    );

            txtFin.setText(
                    fin.toString()
            );

        } catch (Exception e) {

            txtFin.setText("");
        }
    }

    /**
     * Al hacer click en una fila de la tabla, carga esa membresía en el
     * formulario para poder editarla o eliminarla: muestra el socio y la
     * fecha de inicio real (de solo lectura), selecciona el tipo actual
     * en el combo, y habilita los botones de Actualizar/Eliminar.
     */
    private void cargarSeleccionEnFormulario() {

        int fila = tabla.getSelectedRow();

        if (fila < 0 || membresiasCargadas == null
                || fila >= membresiasCargadas.size()) {
            return;
        }

        Membresia m = membresiasCargadas.get(fila);

        String nombreMostrado =
                m.getNombreSocio() != null
                        ? m.getNombreSocio()
                        + (m.getDniSocio() != null
                        ? " - " + m.getDniSocio()
                        : "")
                        : ("socio #" + m.getSocioId());

        lblSocioValor.setText(nombreMostrado);

        cbTipos.setEnabled(true);

        // Ojo: seleccionarTipoPorId dispara el actionListener del combo
        // (calcularFechaFin), pero idEnEdicion todavía es null en este
        // punto, así que ese guard evita que se recalcule nada todavía
        // con datos a medio cargar.
        seleccionarTipoPorId(m.getTipoMembresiaId());

        boolean esVencida =
                "vencida".equalsIgnoreCase(m.getEstado());

        idEnEdicion = m.getId();
        idEnEdicionEraVencida = esVencida;

        if (esVencida) {

            // BUG CORREGIDO: antes se dejaba la fecha de inicio de la
            // membresía vencida (por ejemplo, la de hace 3 meses) como
            // base para calcular la nueva fecha fin al cambiar el tipo.
            // Eso podía dar una fecha fin que seguía en el pasado (según
            // cuánto tiempo llevaba vencida y qué tan corto era el nuevo
            // tipo), y entonces el "Actualizar" guardaba bien el tipo
            // nuevo pero el estado se quedaba en "vencida" -- parecía que
            // no había pasado nada. Una membresía vencida se renueva
            // desde HOY, no desde el inicio del período que ya terminó.
            txtInicio.setText(
                    LocalDate.now().toString()
            );

            calcularFechaFin();

        } else {

            txtInicio.setText(
                    m.getFechaInicio().toString()
            );

            txtFin.setText(
                    m.getFechaFin().toString()
            );
        }

        lblModoFormulario.setForeground(Tema.ACENTO);
        lblModoFormulario.setText(
                esVencida
                        ? "Renovando membresía vencida de " + nombreMostrado
                        + " (ID " + m.getId() + ") — inicio ajustado a hoy"
                        : "Editando membresía de " + nombreMostrado
                        + " (ID " + m.getId() + ")"
        );

        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    /**
     * Vuelve el formulario a su estado inicial: sin ninguna membresía
     * seleccionada, con los controles de edición deshabilitados.
     */
    private void limpiarFormulario() {

        idEnEdicion = null;
        idEnEdicionEraVencida = false;

        lblSocioValor.setText("—");

        cbTipos.setEnabled(false);
        cbTipos.setSelectedIndex(-1);

        txtInicio.setText("");
        txtFin.setText("");

        lblModoFormulario.setForeground(Tema.TEXTO_SECUNDARIO);
        lblModoFormulario.setText(
                "Ningún registro seleccionado — hacé click en una fila de la tabla para editarla"
        );

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        tabla.clearSelection();
    }

    private void seleccionarTipoPorId(int id) {

        for (int i = 0; i < cbTipos.getItemCount(); i++) {

            TipoMembresia t = cbTipos.getItemAt(i);

            if (t != null && t.getId() == id) {

                cbTipos.setSelectedIndex(i);

                return;
            }
        }
    }

    /**
     * Actualiza la membresía actualmente cargada en el formulario: nuevo
     * tipo, fecha fin recalculada, y estado recalculado según esa fecha
     * fin. Además, sincroniza el monto de los pagos ya asociados a esta
     * membresía con el precio del nuevo tipo (para que Pagos no se quede
     * mostrando el precio del tipo anterior).
     */
    private void actualizarMembresiaSeleccionada() {

        if (idEnEdicion == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná una membresía de la tabla primero."
            );

            return;
        }

        try {

            TipoMembresia tipo =
                    (TipoMembresia)
                            cbTipos.getSelectedItem();

            if (tipo == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un tipo de membresía."
                );

                return;
            }

            LocalDate fechaInicio =
                    LocalDate.parse(
                            txtInicio.getText()
                    );

            LocalDate fechaFin =
                    LocalDate.parse(
                            txtFin.getText()
                    );

            // Si la nueva fecha fin ya pasó, la membresía queda vencida;
            // si no, activa. Relevante al renovar una membresía vencida
            // con un tipo nuevo.
            String estado =
                    fechaFin.isBefore(LocalDate.now())
                            ? "vencida"
                            : "activa";

            Membresia membresia = new Membresia();

            membresia.setId(idEnEdicion);
            membresia.setTipo(tipo.getNombre());
            membresia.setTipoMembresiaId(tipo.getId());
            membresia.setFechaInicio(fechaInicio);
            membresia.setFechaFin(fechaFin);
            membresia.setEstado(estado);

            boolean actualizado =
                    membresiaDAO.actualizarMembresia(membresia);

            if (!actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar la membresía."
                );

                return;
            }

            // Sincroniza el monto del pago asociado con el precio del
            // tipo vigente. Si esto es una renovación real (la membresía
            // estaba vencida), también actualiza fecha_pago a hoy -- si
            // no, el pago sigue fechado en el mes viejo y "Ingresos del
            // mes" del Dashboard nunca refleja el nuevo monto. No es
            // crítico para la membresía en sí, así que si falla se avisa
            // aparte pero no se revierte la actualización ya confirmada.
            boolean montoSincronizado =
                    pagoDAO.actualizarMontoPorMembresia(
                            idEnEdicion,
                            tipo.getPrecio(),
                            idEnEdicionEraVencida
                    );

            cargarMembresias();
            limpiarFormulario();
            DashboardManager.actualizar();

            if (montoSincronizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Membresía actualizada y monto de pago sincronizado correctamente."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Membresía actualizada, pero no se pudo sincronizar el monto del pago asociado."
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    /**
     * Elimina la membresía actualmente cargada en el formulario, previa
     * confirmación. Los pagos ya cobrados para esta membresía no se
     * borran: quedan en el historial, solo se desvinculan (ver
     * MembresiaDAO.eliminarMembresia).
     */
    private void eliminarMembresiaSeleccionada() {

        if (idEnEdicion == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná una membresía de la tabla primero."
            );

            return;
        }

        String nombreSocio = lblSocioValor.getText();

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar la membresía de " + nombreSocio + "?\n"
                        + "Los pagos ya registrados para esta membresía "
                        + "no se eliminan, solo quedan sin membresía asociada.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean eliminado =
                    membresiaDAO.eliminarMembresia(idEnEdicion);

            if (eliminado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Membresía eliminada correctamente."
                );
                DashboardManager.actualizar();
                cargarMembresias();
                limpiarFormulario();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar la membresía."
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void cargarMembresias() {

        membresiaDAO.actualizarMembresiasVencidas();

        try {

            List<Membresia> lista =
                    membresiaDAO.obtenerMembresias();

            poblarTabla(lista);

        } catch (Exception e) {

            System.out.println(
                    "Error cargando membresías"
            );

            e.printStackTrace();
        }
    }

    /**
     * Vuelca una lista de membresías en la tabla, sea el listado completo
     * o el resultado de aplicar los filtros combinados. Guarda también la
     * lista en membresiasCargadas, en el mismo orden que las filas, para
     * poder recuperar el objeto real al seleccionar una fila (edición).
     */
    private void poblarTabla(List<Membresia> lista) {

        membresiasCargadas = lista;

        modelo.setRowCount(0);

        for (Membresia m : lista) {

            String socioMostrado;

            if (m.getNombreSocio() != null) {

                socioMostrado =
                        m.getDniSocio() != null
                                ? m.getNombreSocio() + " - " + m.getDniSocio()
                                : m.getNombreSocio();

            } else {

                // Respaldo defensivo por si alguna consulta no trajera el
                // JOIN con socio (no debería pasar con obtenerMembresias,
                // filtrarMembresias ni filtrarMembresiasPorNombre actuales).
                socioMostrado = "socio #" + m.getSocioId();
            }

            modelo.addRow(new Object[]{

                    m.getId(),
                    socioMostrado,
                    m.getTipo(),
                    m.getFechaInicio(),
                    m.getFechaFin(),
                    m.getEstado()
            });
        }
    }

    // =========================
    // FILTROS COMBINADOS
    // =========================

    private void filtrarListado() {

        String nombreODni = txtFiltroNombre.getText().trim();

        String estado = (String) cbFiltroEstado.getSelectedItem();
        String tipo = (String) cbFiltroTipo.getSelectedItem();

        List<Membresia> listaFiltrada =
                membresiaDAO.filtrarMembresiasPorNombre(nombreODni, estado, tipo);

        poblarTabla(listaFiltrada);

        limpiarFormulario();

        if (listaFiltrada.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron membresías con esos filtros."
            );
        }
    }

    private void quitarFiltros() {

        txtFiltroNombre.setText("");
        cbFiltroEstado.setSelectedIndex(0);
        cbFiltroTipo.setSelectedIndex(0);

        cargarMembresias();
        limpiarFormulario();
    }
}
