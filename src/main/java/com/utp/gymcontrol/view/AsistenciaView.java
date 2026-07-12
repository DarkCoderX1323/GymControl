package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.AsistenciaDAO;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.model.Asistencia;
import com.utp.gymcontrol.model.Socio;
import com.utp.gymcontrol.utils.Tema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class AsistenciaView extends JFrame {

    private static final int ANCHO_CAMPO = 280;

    private final AsistenciaDAO asistenciaDAO;
    private final SocioDAO socioDAO;

    // Socio localizado por la búsqueda dual, pendiente de registrar
    // asistencia.
    private Socio socioEncontrado;

    // =========================
    // CAMPOS
    // =========================

    private JTextField txtBuscarDni;
    private JTextField txtBuscarId;

    private JLabel lblSocioEncontrado;

    // =========================
    // BOTONES
    // =========================

    private JButton btnBuscarSocio;
    private JButton btnRegistrar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnVolver;

    // =========================
    // TABLA
    // =========================

    private JTable tablaAsistencias;
    private DefaultTableModel modeloTabla;

    public AsistenciaView() {

        asistenciaDAO = new AsistenciaDAO();
        socioDAO = new SocioDAO();

        iniciarComponentes();

        setTitle("GymControl - Registro de Asistencia");

        setSize(1200, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }

    private void iniciarComponentes() {

        Font fuente = Tema.fuenteRegular().deriveFont(14f);

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(15, 15));

        panelPrincipal.setBackground(Tema.FONDO);

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        // =========================
        // TITULO
        // =========================

        JLabel titulo =
                new JLabel(
                        "GYMCONTROL - Registro de asistencia",
                        SwingConstants.CENTER
                );

        titulo.setForeground(Tema.TEXTO_PRIMARIO);

        titulo.setFont(
                Tema.fuenteTitulo().deriveFont(24f)
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

        // =========================
        // BUSQUEDA DUAL: DNI + ID
        // =========================

        JLabel lblBuscar = new JLabel("UBICAR SOCIO");
        lblBuscar.setForeground(Tema.TEXTO_SECUNDARIO);
        lblBuscar.setFont(Tema.fuenteEtiqueta());
        lblBuscar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelFormulario.add(lblBuscar);
        panelFormulario.add(Box.createVerticalStrut(10));

        txtBuscarDni =
                crearCampo(
                        panelFormulario,
                        "DNI del socio",
                        fuente
                );

        txtBuscarId =
                crearCampo(
                        panelFormulario,
                        "ID del socio",
                        fuente
                );

        btnBuscarSocio =
                crearBoton(
                        "Buscar socio",
                        Tema.ACENTO
                );

        btnBuscarSocio.addActionListener(
                e -> buscarSocio()
        );

        panelFormulario.add(btnBuscarSocio);

        panelFormulario.add(
                Box.createVerticalStrut(15)
        );

        lblSocioEncontrado =
                new JLabel("Ningún socio localizado todavía.");

        lblSocioEncontrado.setForeground(Tema.TEXTO_SECUNDARIO);
        lblSocioEncontrado.setFont(Tema.fuenteEtiqueta());
        lblSocioEncontrado.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSocioEncontrado.setHorizontalAlignment(SwingConstants.CENTER);

        panelFormulario.add(lblSocioEncontrado);

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
        // BOTONES
        // =========================

        btnRegistrar =
                crearBoton(
                        "Registrar asistencia",
                        Tema.ACENTO
                );

        btnEliminar =
                crearBoton(
                        "Eliminar",
                        Tema.PELIGRO
                );

        btnLimpiar =
                crearBoton(
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
                e -> registrarAsistencia()
        );

        btnEliminar.addActionListener(
                e -> eliminarAsistencia()
        );

        btnLimpiar.addActionListener(
                e -> limpiarCampos()
        );

        btnVolver.addActionListener(e -> {

            dispose();

        });

        panelFormulario.add(btnRegistrar);

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

        modeloTabla =
                new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Socio");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Hora");

        tablaAsistencias =
                new JTable(modeloTabla);

        estilizarTabla(tablaAsistencias);

        JScrollPane scroll =
                new JScrollPane(tablaAsistencias);

        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Tema.SUPERFICIE);

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

        panelPrincipal.add(
                scroll,
                BorderLayout.CENTER
        );

        add(panelPrincipal);

        cargarAsistencias();
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

        label.setForeground(Tema.TEXTO_SECUNDARIO);

        label.setFont(Tema.fuenteEtiqueta());

        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campo =
                new JTextField();

        campo.setMaximumSize(
                new Dimension(ANCHO_CAMPO, 40)
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

    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton boton =
                new JButton(texto);

        boton.setBackground(color);

        boton.setForeground(Tema.TEXTO_PRIMARIO);

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

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

        boton.setFont(
                Tema.fuenteBoton().deriveFont(14f)
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

    // =========================
    // CARGAR ASISTENCIAS
    // =========================

    private void cargarAsistencias() {

        modeloTabla.setRowCount(0);

        List<Asistencia> lista = asistenciaDAO.obtenerAsistencias();

        for (Asistencia asistencia : lista) {

            Object[] fila = {

                    asistencia.getId(),
                    asistencia.getDniSocio(),
                    asistencia.getNombreSocio(),
                    asistencia.getFecha(),
                    asistencia.getHora()

            };

            modeloTabla.addRow(fila);
        }
    }

    // =========================
    // BUSCAR SOCIO (DNI + ID)
    // =========================

    private void buscarSocio() {

        String dni = txtBuscarDni.getText().trim();
        String idTexto = txtBuscarId.getText().trim();

        if (StringUtils.isBlank(dni) && StringUtils.isBlank(idTexto)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el DNI o el ID del socio para buscar."
            );

            return;
        }

        Socio socio = null;

        // Se prioriza el DNI si ambos campos están completos; si no da
        // resultado o está vacío, se intenta por ID.
        if (StringUtils.isNotBlank(dni)) {

            socio = socioDAO.buscarPorDni(dni);
        }

        if (socio == null && StringUtils.isNotBlank(idTexto)) {

            try {

                int id = Integer.parseInt(idTexto);

                socio = socioDAO.buscarPorId(id);

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "El ID debe ser un número."
                );

                return;
            }
        }

        if (socio == null) {

            socioEncontrado = null;

            lblSocioEncontrado.setText(
                    "Ningún socio localizado todavía."
            );

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró ningún socio con esos datos."
            );

            return;
        }

        socioEncontrado = socio;

        lblSocioEncontrado.setText(
                "<html><div style='text-align:center;'>"
                        + "Socio: " + socio.getNombre() + "<br>"
                        + "DNI: " + socio.getDni()
                        + " · ID: " + socio.getId()
                        + "</div></html>"
        );
    }

    // =========================
    // REGISTRAR ASISTENCIA
    // =========================

    private void registrarAsistencia() {

        if (socioEncontrado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero busque un socio por DNI o ID."
            );

            return;
        }

        if (asistenciaDAO.existeAsistenciaHoy(socioEncontrado.getId())) {

            JOptionPane.showMessageDialog(
                    this,
                    "Este socio ya registró asistencia hoy."
            );

            return;
        }

        boolean registrado =
                asistenciaDAO.registrarAsistencia(socioEncontrado.getId());

        if (registrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Asistencia registrada correctamente."
            );

            cargarAsistencias();

            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar la asistencia."
            );
        }
    }

    // =========================
    // ELIMINAR
    // =========================

    private void eliminarAsistencia() {

        int fila = tablaAsistencias.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un registro de asistencia."
            );

            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(

                this,

                "¿Eliminar el registro de asistencia seleccionado?",

                "Confirmar",

                JOptionPane.YES_NO_OPTION

        );

        if (confirmar != JOptionPane.YES_OPTION) {

            return;
        }

        int id = Integer.parseInt(
                modeloTabla.getValueAt(fila, 0).toString()
        );

        boolean eliminado = asistenciaDAO.eliminarAsistencia(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registro eliminado."
            );

            cargarAsistencias();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el registro."
            );
        }
    }

    // =========================
    // LIMPIAR
    // =========================

    private void limpiarCampos() {

        txtBuscarDni.setText("");
        txtBuscarId.setText("");

        socioEncontrado = null;

        lblSocioEncontrado.setText(
                "Ningún socio localizado todavía."
        );

        txtBuscarDni.requestFocus();
    }
}
