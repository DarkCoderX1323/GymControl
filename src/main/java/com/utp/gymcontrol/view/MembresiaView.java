package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.MembresiaDAO;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.dao.TipoMembresiaDAO;
import com.utp.gymcontrol.model.Membresia;
import com.utp.gymcontrol.model.Socio;
import com.utp.gymcontrol.model.TipoMembresia;
import com.utp.gymcontrol.utils.Tema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MembresiaView extends JFrame {

    private JComboBox<Socio> cbSocios;
    private JComboBox<TipoMembresia> cbTipos;

    private JTextField txtInicio;
    private JTextField txtFin;

    private JButton btnRegistrar;
    private JButton btnVolver;

    private JTable tabla;
    private DefaultTableModel modelo;

    private MembresiaDAO membresiaDAO;
    private SocioDAO socioDAO;
    private TipoMembresiaDAO tipoDAO;

    public MembresiaView() {

        membresiaDAO = new MembresiaDAO();
        socioDAO = new SocioDAO();
        tipoDAO = new TipoMembresiaDAO();

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
        // FORMULARIO
        // =========================

        JPanel formulario =
                new JPanel(new GridBagLayout());

        formulario.setBackground(Tema.SUPERFICIE);

        formulario.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 18, 18, 18
                )
        );

        cbSocios = new JComboBox<>();
        cbTipos = new JComboBox<>();

        estilizarCombo(cbSocios);
        estilizarCombo(cbTipos);

        txtInicio = new JTextField();
        txtFin = new JTextField();

        txtInicio.setEditable(false);
        txtFin.setEditable(false);

        estilizarCampo(txtInicio);
        estilizarCampo(txtFin);

        btnRegistrar =
                new JButton("Registrar membresía");

        estilizarBoton(btnRegistrar, Tema.ACENTO);

        btnVolver = new JButton("Volver");

        estilizarBoton(btnVolver, Tema.SUPERFICIE_CLARA);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Columna izquierda: etiquetas (ancho fijo, no se estiran)
        // Columna derecha: campos/combos (ocupan el espacio restante)
        gbc.gridx = 0;
        gbc.weightx = 0;

        gbc.gridy = 0;
        formulario.add(crearEtiqueta("Socio"), gbc);

        gbc.gridy = 1;
        formulario.add(crearEtiqueta("Tipo de membresía"), gbc);

        gbc.gridy = 2;
        formulario.add(crearEtiqueta("Fecha inicio"), gbc);

        gbc.gridy = 3;
        formulario.add(crearEtiqueta("Fecha fin"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        gbc.gridy = 0;
        formulario.add(cbSocios, gbc);

        gbc.gridy = 1;
        formulario.add(cbTipos, gbc);

        gbc.gridy = 2;
        formulario.add(txtInicio, gbc);

        gbc.gridy = 3;
        formulario.add(txtFin, gbc);

        // Botones: compactos y centrados, en un sub-panel aparte para que
        // no hereden el ancho de las columnas del formulario
        btnRegistrar.setPreferredSize(new Dimension(180, 36));
        btnVolver.setPreferredSize(new Dimension(110, 36));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 4;
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

        JScrollPane scroll =
                new JScrollPane(tabla);

        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Tema.SUPERFICIE);

        principal.add(formulario,
                BorderLayout.NORTH);

        principal.add(scroll,
                BorderLayout.CENTER);

        add(principal);

        txtInicio.setText(
                LocalDate.now().toString()
        );

        cargarSocios();
        cargarTipos();
        cargarMembresias();

        cbTipos.addActionListener(
                e -> calcularFechaFin()
        );

        btnRegistrar.addActionListener(
                e -> registrarMembresia()
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

    // =========================
    // LOGICA (sin cambios)
    // =========================

    private void cargarSocios() {

        cbSocios.removeAllItems();

        try {

            List<Socio> socios =
                    socioDAO.obtenerSocios();

            for (Socio s : socios) {

                cbSocios.addItem(s);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error cargando socios"
            );

            e.printStackTrace();
        }
    }

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

        try {

            TipoMembresia tipo =
                    (TipoMembresia)
                            cbTipos.getSelectedItem();

            if (tipo == null) {
                return;
            }

            LocalDate inicio =
                    LocalDate.now();

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

    private void registrarMembresia() {

        try {

            Socio socio =
                    (Socio)
                            cbSocios.getSelectedItem();

            TipoMembresia tipo =
                    (TipoMembresia)
                            cbTipos.getSelectedItem();

            if (socio == null || tipo == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un socio y un tipo."
                );

                return;
            }

            Membresia membresia =
                    new Membresia();

            membresia.setSocioId(
                    socio.getId()
            );

            membresia.setTipo(
                    tipo.getNombre()
            );

            membresia.setTipoMembresiaId(
                    tipo.getId()
            );

            membresia.setFechaInicio(
                    LocalDate.parse(
                            txtInicio.getText()
                    )
            );

            membresia.setFechaFin(
                    LocalDate.parse(
                            txtFin.getText()
                    )
            );

            membresia.setEstado(
                    "activa"
            );

            boolean registrado =
                    membresiaDAO
                            .registrarMembresia(
                                    membresia
                            );

            if (registrado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Membresía registrada correctamente."
                );

                cargarMembresias();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar."
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

        modelo.setRowCount(0);

        try {

            List<Membresia> lista =
                    membresiaDAO.obtenerMembresias();

            for (Membresia m : lista) {

                modelo.addRow(new Object[]{

                        m.getId(),
                        m.getSocioId(),
                        m.getTipo(),
                        m.getFechaInicio(),
                        m.getFechaFin(),
                        m.getEstado()
                });
            }

        } catch (Exception e) {

            System.out.println(
                    "Error cargando membresías"
            );

            e.printStackTrace();
        }
    }
}
