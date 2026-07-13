package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.DashboardDAO;
import com.utp.gymcontrol.dao.MembresiaDAO;

import javax.swing.*;
import java.awt.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.utp.gymcontrol.utils.ExcelReportGenerator;
import com.utp.gymcontrol.utils.DashboardUtils;
import com.utp.gymcontrol.utils.Tema;
import com.utp.gymcontrol.utils.PanelRedondeado;

public class DashboardView extends JFrame {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    DashboardView.class

            );


    private DashboardDAO dashboardDAO;

    private JButton btnSocios;
    private JButton btnRegistroRapido;
    private JButton btnMembresias;
    private JButton btnPagos;
    private JButton btnAsistencia;
    private JButton btnAlertas;
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

        MembresiaDAO membresiaDAO = new MembresiaDAO();

        membresiaDAO.actualizarMembresiasVencidas();

        int socios =
                dashboardDAO.contarSociosActivos();

        int membresias =
                dashboardDAO.contarMembresiasActivas();

        double ingresos =
                dashboardDAO.totalPagosMes();

        int porVencer =
                membresiaDAO.contarMembresiasPorVencer(3);

        // =========================
        // PANEL PRINCIPAL
        // =========================

        JPanel panelPrincipal =
                new JPanel(new BorderLayout());

        panelPrincipal.setBackground(Tema.FONDO);

        // =========================
        // MENU LATERAL
        // =========================

        JPanel panelMenu = new JPanel();

        panelMenu.setBackground(Tema.FONDO);

        panelMenu.setPreferredSize(
                new Dimension(240, 700)
        );

        panelMenu.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 0, 1, Tema.SUPERFICIE_CLARA
                        ),
                        BorderFactory.createEmptyBorder(20, 14, 20, 14)
                )
        );

        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));

        // =========================
        // LOGO
        // =========================

        JPanel panelLogo = crearLogo();
        panelLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLogo.setMaximumSize(new Dimension(240, 40));

        panelMenu.add(panelLogo);
        panelMenu.add(Box.createVerticalStrut(24));

        // =========================
        // ITEM ACTIVO: DASHBOARD
        // =========================

        JPanel itemActivo = crearItemActivo("Dashboard");
        itemActivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemActivo.setMaximumSize(new Dimension(240, 40));

        panelMenu.add(itemActivo);
        panelMenu.add(Box.createVerticalStrut(4));

        // =========================
        // ITEMS DE NAVEGACION
        // =========================

        btnSocios = crearItemMenu("Socios");
        btnRegistroRapido = crearItemMenu("Registro rápido");
        btnMembresias = crearItemMenu("Membresías");
        btnPagos = crearItemMenu("Pagos");
        btnAsistencia = crearItemMenu("Asistencia");
        btnAlertas = crearItemMenu("Alertas");
        btnReportes = crearItemMenu("Reportes");
        btnCerrarSesion = crearItemMenu("Cerrar sesión");

        btnRegistroRapido.addActionListener(e -> {

            logger.info(
                    "Acceso al modulo Registro Rapido"
            );

            new RegistroRapidoView();

        });

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
        btnAsistencia.addActionListener(e -> {

            logger.info(
                    "Acceso al modulo Asistencia"
            );

            new AsistenciaView();

        });
        btnAlertas.addActionListener(e -> {

            logger.info(
                    "Acceso al modulo Alertas de Membresias"
            );

            new AlertaMembresiaView();

        });
        btnCerrarSesion.addActionListener(e -> {

            logger.info(
                    "Cierre de sesion realizado"
            );

            dispose();

            new LoginView();

        });

        for (JButton item : new JButton[]{btnRegistroRapido, btnSocios, btnMembresias, btnPagos, btnAsistencia, btnAlertas, btnReportes}) {
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            item.setMaximumSize(new Dimension(240, 40));
            panelMenu.add(item);
            panelMenu.add(Box.createVerticalStrut(4));
        }

        panelMenu.add(Box.createVerticalGlue());

        JSeparator separador = new JSeparator();
        separador.setForeground(Tema.SUPERFICIE_CLARA);
        separador.setBackground(Tema.SUPERFICIE_CLARA);
        separador.setAlignmentX(Component.LEFT_ALIGNMENT);
        separador.setMaximumSize(new Dimension(240, 1));

        panelMenu.add(separador);
        panelMenu.add(Box.createVerticalStrut(4));

        btnCerrarSesion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(240, 40));
        panelMenu.add(btnCerrarSesion);

        // =========================
        // PANEL CENTRAL
        // =========================

        JPanel panelCentro =
                new JPanel(new BorderLayout());

        panelCentro.setBackground(Tema.FONDO);

        panelCentro.setBorder(
                BorderFactory.createEmptyBorder(
                        24, 24, 24, 24
                )
        );

        JLabel tituloDashboard =
                new JLabel("Dashboard");

        tituloDashboard.setForeground(Tema.TEXTO_PRIMARIO);

        tituloDashboard.setFont(
                Tema.fuenteTitulo().deriveFont(26f)
        );

        tituloDashboard.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 20, 0)
        );

        // =========================
        // TARJETAS
        // =========================

        JPanel panelCards =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                15
                        )
                );

        panelCards.setBackground(Tema.FONDO);

        panelCards.add(
                crearTarjeta(
                        "Socios activos",
                        String.valueOf(socios),
                        Tema.ACENTO
                )
        );

        panelCards.add(
                crearTarjeta(
                        "Membresías activas",
                        String.valueOf(membresias),
                        Tema.EXITO
                )
        );

        panelCards.add(
                crearTarjeta(
                        "Ingresos del mes",
                        String.format("S/ %.2f", ingresos),
                        Tema.ADVERTENCIA
                )
        );

        JPanel tarjetaPorVencer =
                crearTarjeta(
                        "Membresías por vencer (3 días)",
                        String.valueOf(porVencer),
                        porVencer > 0 ? Tema.PELIGRO : Tema.EXITO
                );

        tarjetaPorVencer.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        tarjetaPorVencer.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {

                        logger.info(
                                "Acceso al modulo Alertas de Membresias"
                        );

                        new AlertaMembresiaView();
                    }
                }
        );

        panelCards.add(tarjetaPorVencer);

        // =========================
        // ACCIONES RAPIDAS
        // =========================

        JLabel lblAcciones = new JLabel("Acciones rápidas");
        lblAcciones.setForeground(Tema.TEXTO_SECUNDARIO);
        lblAcciones.setFont(Tema.fuenteEtiqueta());
        lblAcciones.setBorder(
                BorderFactory.createEmptyBorder(20, 0, 10, 0)
        );

        JPanel panelAcciones =
                new JPanel(
                        new GridLayout(
                                0,
                                2,
                                15,
                                15
                        )
                );

        panelAcciones.setBackground(Tema.FONDO);

        JButton btnAccionRegistroRapido =
                crearBotonAccion("Registro rápido");

        btnAccionRegistroRapido.setBackground(Tema.ACENTO);

        btnAccionRegistroRapido.addActionListener(e -> {

            logger.info(
                    "Acceso rapido: Registro Rapido"
            );

            new RegistroRapidoView();

        });

        panelAcciones.add(btnAccionRegistroRapido);

        JButton btnNuevoSocio =
                crearBotonAccion("Nuevo socio");

        btnNuevoSocio.addActionListener(e -> {

            logger.info(
                    "Acceso rapido: Nuevo Socio"
            );

            new SocioView();

        });

        JButton btnNuevaMembresia =
                crearBotonAccion("Nueva membresía");

        btnNuevaMembresia.addActionListener(e -> {

            logger.info(
                    "Acceso rapido: Nueva Membresia"
            );

            new MembresiaView();

        });

        JButton btnRegistrarPago =
                crearBotonAccion("Registrar pago");

        btnRegistrarPago.addActionListener(e -> {

            logger.info(
                    "Acceso rapido: Registrar Pago"
            );

            new PagoView();

        });

        panelAcciones.add(btnNuevoSocio);
        panelAcciones.add(btnNuevaMembresia);
        panelAcciones.add(btnRegistrarPago);

        JButton btnGenerarReporte =
                crearBotonAccion("Generar reporte");


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

        contenido.setBackground(Tema.FONDO);

        JPanel bloqueAcciones = new JPanel(new BorderLayout());
        bloqueAcciones.setBackground(Tema.FONDO);
        bloqueAcciones.add(lblAcciones, BorderLayout.NORTH);
        bloqueAcciones.add(panelAcciones, BorderLayout.CENTER);

        contenido.add(
                panelCards,
                BorderLayout.NORTH
        );

        contenido.add(
                bloqueAcciones,
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

    /**
     * Logo compacto para el sidebar: cuadrado con acento + iniciales "GC" +
     * texto "GymControl".
     */
    private JPanel crearLogo() {

        JPanel contenedor = new JPanel();
        contenedor.setOpaque(false);
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.X_AXIS));

        PanelRedondeado cuadroLogo = new PanelRedondeado(8, Tema.ACENTO);
        cuadroLogo.setPreferredSize(new Dimension(32, 32));
        cuadroLogo.setMaximumSize(new Dimension(32, 32));
        cuadroLogo.setLayout(new GridBagLayout());

        JLabel lblIniciales = new JLabel("GC");
        lblIniciales.setForeground(Tema.TEXTO_PRIMARIO);
        lblIniciales.setFont(new Font("Segoe UI", Font.BOLD, 13));

        cuadroLogo.add(lblIniciales);

        JLabel lblNombre = new JLabel("GymControl");
        lblNombre.setForeground(Tema.TEXTO_PRIMARIO);
        lblNombre.setFont(Tema.fuenteBoton());
        lblNombre.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        contenedor.add(cuadroLogo);
        contenedor.add(lblNombre);

        return contenedor;
    }

    /**
     * Item de sidebar resaltado como la sección actualmente activa
     * (no es clickeable, ya que representa la vista en la que ya estamos).
     */
    private JPanel crearItemActivo(String texto) {

        PanelRedondeado item = new PanelRedondeado(10, Tema.SUPERFICIE);
        item.setLayout(new BorderLayout());
        item.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Tema.TEXTO_PRIMARIO);
        lbl.setFont(Tema.fuenteBoton().deriveFont(14f));

        item.add(lbl, BorderLayout.WEST);

        return item;
    }

    /**
     * Item de navegación del sidebar con estilo plano (sin fondo, sin
     * borde), consistente con el resto del menú.
     */
    private JButton crearItemMenu(String texto) {

        JButton boton = new JButton(texto);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setForeground(Tema.TEXTO_SECUNDARIO);
        boton.setFont(Tema.fuenteRegular().deriveFont(14f));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }

    /**
     * Tarjeta de métrica con franja de acento a la izquierda, en vez del
     * relleno sólido tipo "banner" que tenía el diseño anterior.
     */
    private JPanel crearTarjeta(
            String titulo,
            String valor,
            Color colorAcento
    ) {

        JPanel tarjeta = new JPanel(new BorderLayout());

        tarjeta.setBackground(Tema.SUPERFICIE);

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 4, 0, 0, colorAcento
                        ),
                        BorderFactory.createEmptyBorder(16, 14, 16, 16)
                )
        );

        JLabel lblTitulo =
                new JLabel(titulo);

        lblTitulo.setForeground(Tema.TEXTO_SECUNDARIO);
        lblTitulo.setFont(Tema.fuenteEtiqueta());

        JLabel lblValor =
                new JLabel(valor);

        lblValor.setForeground(Tema.TEXTO_PRIMARIO);

        lblValor.setFont(
                Tema.fuenteTitulo().deriveFont(24f)
        );

        lblValor.setBorder(
                BorderFactory.createEmptyBorder(6, 0, 0, 0)
        );

        tarjeta.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        tarjeta.add(
                lblValor,
                BorderLayout.CENTER
        );

        return tarjeta;
    }

    /**
     * Botón para el bloque de "Acciones rápidas": superficie clara, sin
     * relleno gris genérico.
     */
    private JButton crearBotonAccion(String texto) {

        JButton boton = new JButton(texto);
        boton.setBackground(Tema.SUPERFICIE_CLARA);
        boton.setForeground(Tema.TEXTO_PRIMARIO);
        boton.setFont(Tema.fuenteRegular());
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }
}
