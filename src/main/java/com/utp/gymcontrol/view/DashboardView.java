package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.DashboardDAO;

import javax.swing.*;
import java.awt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.utp.gymcontrol.utils.ExcelReportGenerator;
import com.utp.gymcontrol.utils.DashboardUtils;

public class DashboardView extends JFrame {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    DashboardView.class
                    
            );
    
    

    private DashboardDAO dashboardDAO;

    private JButton btnSocios;
    private JButton btnMembresias;
    private JButton btnPagos;
    private JButton btnReportes;
    private JButton btnCerrarSesion;

    public DashboardView() {

        dashboardDAO = new DashboardDAO();
        logger.info(
        "Dashboard iniciado - GymControl v0.3"
                
);
        logger.info(
            "Modulos cargados: "
            + DashboardUtils.obtenerModulos()
    );

        iniciarComponentes();

        setTitle("GymControl - Dashboard");

        setSize(1200, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }
       

    private void iniciarComponentes() {

        // =========================
        // DATOS REALES
        // =========================

        int socios =
                dashboardDAO.contarSociosActivos();

        int membresias =
                dashboardDAO.contarMembresiasActivas();

        double ingresos =
                dashboardDAO.totalPagosMes();

        // =========================
        // PANEL PRINCIPAL
        // =========================

        JPanel panelPrincipal =
                new JPanel(new BorderLayout());

        panelPrincipal.setBackground(
                new Color(30,30,30)
        );

        // =========================
        // MENU LATERAL
        // =========================

        JPanel panelMenu = new JPanel();

        panelMenu.setBackground(
                new Color(25,25,25)
        );

        panelMenu.setPreferredSize(
                new Dimension(250,700)
        );

        panelMenu.setLayout(
                new GridLayout(7,1,10,10)
        );

        JLabel lblTitulo =
                new JLabel("GYMCONTROL");

        lblTitulo.setForeground(Color.WHITE);

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        lblTitulo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        btnSocios =
                new JButton("Socios");

        btnMembresias =
                new JButton("Membresías");

        btnPagos =
                new JButton("Pagos");

        btnReportes =
                new JButton("Reportes");

        btnCerrarSesion =
                new JButton("Cerrar Sesión");

        btnSocios.addActionListener(e -> {

    logger.info(
            "Acceso al modulo Gestion de Socios"
    );

    new SocioView();

});
        btnMembresias.addActionListener(e -> {

    logger.info(
            "Acceso al modulo Membresias"
    );

    new MembresiaView();

});
    btnPagos.addActionListener(e -> {

    logger.info(
            "Acceso al modulo Pagos"
    );

    new PagoView();

});
    btnReportes.addActionListener(e -> {

    logger.info(
            "Acceso al modulo Reportes"
    );

    new ReporteView();

});
    btnCerrarSesion.addActionListener(e -> {

    logger.info(
            "Cierre de sesion realizado"
    );

    dispose();

    new LoginView();

});

        panelMenu.add(lblTitulo);
        panelMenu.add(btnSocios);
        panelMenu.add(btnMembresias);
        panelMenu.add(btnPagos);
        panelMenu.add(btnReportes);
        panelMenu.add(new JLabel());
        panelMenu.add(btnCerrarSesion);

        // =========================
        // PANEL CENTRAL
        // =========================

        JPanel panelCentro =
                new JPanel(new BorderLayout());

        panelCentro.setBackground(
                new Color(40,40,40)
        );

        panelCentro.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );

        JLabel tituloDashboard =
                new JLabel("Dashboard");

        tituloDashboard.setForeground(
                Color.WHITE
        );

        tituloDashboard.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        // =========================
        // TARJETAS
        // =========================

        JPanel panelCards =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                15,
                                15
                        )
                );

        panelCards.setBackground(
                new Color(40,40,40)
        );

        panelCards.add(
                crearTarjeta(
                        "Socios Activos",
                        String.valueOf(socios),
                        new Color(0,120,215)
                )
        );

        panelCards.add(
                crearTarjeta(
                        "Membresías Activas",
                        String.valueOf(membresias),
                        new Color(0,153,51)
                )
        );

        panelCards.add(
                crearTarjeta(
                        "Ingresos del Mes",
                        "S/ " + ingresos,
                        new Color(255,140,0)
                )
        );

        // =========================
        // ACCIONES RAPIDAS
        // =========================

        JPanel panelAcciones =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                15,
                                15
                        )
                );

        panelAcciones.setBackground(
                new Color(40,40,40)
        );

        panelAcciones.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        "Acciones Rápidas"
                )
        );

        panelAcciones.add(
                new JButton("Nuevo Socio")
        );

        panelAcciones.add(
                new JButton("Nueva Membresía")
        );

        panelAcciones.add(
                new JButton("Registrar Pago")
        );

        JButton btnGenerarReporte =
        new JButton("Generar Reporte");

btnGenerarReporte.addActionListener(e -> {

    boolean generado =
            ExcelReportGenerator
                    .generarReporteSocios();

    if (generado) {

        JOptionPane.showMessageDialog(
                this,
                "Reporte generado correctamente."
        );

    } else {

        JOptionPane.showMessageDialog(
                this,
                "Error al generar reporte."
        );
    }
});

panelAcciones.add(
        btnGenerarReporte
);

        JPanel contenido =
                new JPanel(new BorderLayout());

        contenido.setBackground(
                new Color(40,40,40)
        );

        contenido.add(
                panelCards,
                BorderLayout.NORTH
        );

        contenido.add(
                panelAcciones,
                BorderLayout.CENTER
        );

        panelCentro.add(
                tituloDashboard,
                BorderLayout.NORTH
        );

        panelCentro.add(
                contenido,
                BorderLayout.CENTER
        );

        // =========================
        // ENSAMBLAR
        // =========================

        panelPrincipal.add(
                panelMenu,
                BorderLayout.WEST
        );

        panelPrincipal.add(
                panelCentro,
                BorderLayout.CENTER
        );

        add(panelPrincipal);
    }

    private JPanel crearTarjeta(
            String titulo,
            String valor,
            Color color
    ) {

        JPanel card = new JPanel();

        card.setBackground(color);

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        15,15,15,15
                )
        );

        JLabel lblTitulo =
                new JLabel(titulo);

        lblTitulo.setForeground(
                Color.WHITE
        );

        JLabel lblValor =
                new JLabel(valor);

        lblValor.setForeground(
                Color.WHITE
        );

        lblValor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        card.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        card.add(
                lblValor,
                BorderLayout.CENTER
        );

        return card;
    }
}
