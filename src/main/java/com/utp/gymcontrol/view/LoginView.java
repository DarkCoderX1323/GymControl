package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.UsuarioDAO;
import com.utp.gymcontrol.model.Usuario;
import com.utp.gymcontrol.utils.PanelRedondeado;
import com.utp.gymcontrol.utils.Tema;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    private UsuarioDAO usuarioDAO;

    public LoginView() {

        usuarioDAO = new UsuarioDAO();

        iniciarComponentes();

        setTitle("GymControl - Login");
        setSize(440, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void iniciarComponentes() {

        // =========================
        // PANEL PRINCIPAL (fondo)
        // =========================

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(Tema.FONDO);

        // =========================
        // TARJETA CENTRAL (redondeada)
        // =========================

        PanelRedondeado panelCentral = new PanelRedondeado(20, Tema.SUPERFICIE);
        panelCentral.setPreferredSize(new Dimension(350, 400));
        panelCentral.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(6, 30, 6, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // =========================
        // LOGO
        // =========================

        JPanel logo = crearLogo();
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 30, 10, 30);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panelCentral.add(logo, gbc);

        // =========================
        // TITULO Y SUBTITULO
        // =========================

        JLabel lblTitulo = new JLabel("GYMCONTROL", SwingConstants.CENTER);
        lblTitulo.setForeground(Tema.TEXTO_PRIMARIO);
        lblTitulo.setFont(Tema.fuenteTitulo().deriveFont(24f));

        gbc.gridy = 1;
        gbc.insets = new Insets(4, 30, 0, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelCentral.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Sistema Administrativo", SwingConstants.CENTER);
        lblSubtitulo.setForeground(Tema.TEXTO_SECUNDARIO);
        lblSubtitulo.setFont(Tema.fuenteSubtitulo());

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 30, 20, 30);
        panelCentral.add(lblSubtitulo, gbc);

        // =========================
        // USUARIO
        // =========================

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setForeground(Tema.TEXTO_SECUNDARIO);
        lblUsuario.setFont(Tema.fuenteEtiqueta());

        gbc.gridy = 3;
        gbc.insets = new Insets(6, 30, 2, 30);
        panelCentral.add(lblUsuario, gbc);

        txtUsuario = new JTextField();
        txtUsuario.setFont(Tema.fuenteRegular());
        txtUsuario.setBackground(Tema.SUPERFICIE_CLARA);
        txtUsuario.setForeground(Tema.TEXTO_PRIMARIO);
        txtUsuario.setCaretColor(Tema.TEXTO_PRIMARIO);
        txtUsuario.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 30, 10, 30);
        panelCentral.add(txtUsuario, gbc);

        // =========================
        // PASSWORD
        // =========================

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setForeground(Tema.TEXTO_SECUNDARIO);
        lblPassword.setFont(Tema.fuenteEtiqueta());

        gbc.gridy = 5;
        gbc.insets = new Insets(6, 30, 2, 30);
        panelCentral.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(Tema.fuenteRegular());
        txtPassword.setBackground(Tema.SUPERFICIE_CLARA);
        txtPassword.setForeground(Tema.TEXTO_PRIMARIO);
        txtPassword.setCaretColor(Tema.TEXTO_PRIMARIO);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 30, 20, 30);
        panelCentral.add(txtPassword, gbc);

        // =========================
        // BOTON LOGIN
        // =========================

        btnLogin = new JButton("INGRESAR");
        btnLogin.setFont(Tema.fuenteBoton());
        btnLogin.setBackground(Tema.ACENTO);
        btnLogin.setForeground(Tema.TEXTO_PRIMARIO);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(260, 45));
        btnLogin.addActionListener(e -> iniciarSesion());

        gbc.gridy = 7;
        gbc.insets = new Insets(6, 30, 30, 30);
        panelCentral.add(btnLogin, gbc);

        // =========================
        // ENSAMBLAR
        // =========================

        GridBagConstraints gbcWrapper = new GridBagConstraints();
        panelPrincipal.add(panelCentral, gbcWrapper);

        add(panelPrincipal);
    }

    /**
     * Crea un "logo" simple: un cuadrado con el color de acento y las
     * iniciales "GC", ya que el proyecto no cuenta con un logo gráfico.
     */
    private JPanel crearLogo() {

        PanelRedondeado cuadroLogo = new PanelRedondeado(14, Tema.ACENTO);
        cuadroLogo.setPreferredSize(new Dimension(56, 56));
        cuadroLogo.setLayout(new GridBagLayout());

        JLabel lblIniciales = new JLabel("GC");
        lblIniciales.setForeground(Tema.TEXTO_PRIMARIO);
        lblIniciales.setFont(new Font("Segoe UI", Font.BOLD, 20));

        cuadroLogo.add(lblIniciales);

        return cuadroLogo;
    }

    private void iniciarSesion() {

        String username = txtUsuario.getText().trim();

        String password =
                new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos."
            );

            return;
        }

        Usuario usuario =
                usuarioDAO.login(username, password);

        if (usuario != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido " + usuario.getUsername()
            );

            new DashboardView();

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario o contraseña incorrectos."
            );
        }
    }
}
