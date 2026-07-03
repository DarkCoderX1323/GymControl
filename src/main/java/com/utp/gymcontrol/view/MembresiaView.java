package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.MembresiaDAO;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.dao.TipoMembresiaDAO;
import com.utp.gymcontrol.model.Membresia;
import com.utp.gymcontrol.model.Socio;
import com.utp.gymcontrol.model.TipoMembresia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        JPanel principal =
                new JPanel(new BorderLayout(15, 15));

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        JPanel formulario =
                new JPanel(new GridLayout(5, 2, 10, 10));

        cbSocios = new JComboBox<>();
        cbTipos = new JComboBox<>();

        txtInicio = new JTextField();
        txtFin = new JTextField();

        txtInicio.setEditable(false);
        txtFin.setEditable(false);

        btnRegistrar =
                new JButton("Registrar Membresía");
        
        btnVolver.setBackground(
        new Color(255,140,0)
);

btnVolver.setForeground(Color.WHITE);

        formulario.add(new JLabel("Socio"));
        formulario.add(cbSocios);

        formulario.add(new JLabel("Tipo de Membresía"));
        formulario.add(cbTipos);

        formulario.add(new JLabel("Fecha Inicio"));
        formulario.add(txtInicio);

        formulario.add(new JLabel("Fecha Fin"));
        formulario.add(txtFin);

        formulario.add(new JLabel(""));
        formulario.add(btnRegistrar);
        formulario.add(new JLabel());

formulario.add(btnVolver);

        modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Socio");
        modelo.addColumn("Tipo");
        modelo.addColumn("Inicio");
        modelo.addColumn("Fin");
        modelo.addColumn("Estado");

        tabla = new JTable(modelo);

        JScrollPane scroll =
                new JScrollPane(tabla);

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

    new DashboardView();

    dispose();

});
    }

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
