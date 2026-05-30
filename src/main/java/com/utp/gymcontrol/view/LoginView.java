package com.utp.gymcontrol.view;

import com.utp.gymcontrol.dao.UsuarioDAO;
import com.utp.gymcontrol.model.Usuario;

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
        setSize(440, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void iniciarComponentes() {

        Font fuente = new Font("Segoe UI", Font.PLAIN, 16);
        Font tituloFuente = new Font("Segoe UI", Font.BOLD, 32);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(30,30,30));
        panelPrincipal.setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(new Color(43,43,43));
        panelCentral.setLayout(null);

        panelCentral.setPreferredSize(new Dimension(350,380));

        // =========================
        // TITULO
        // =========================

        JLabel lblTitulo = new JLabel("GYMCONTROL");
        lblTitulo.setBounds(70,20,250,40);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(tituloFuente);

        JLabel lblSubtitulo = new JLabel("Sistema Administrativo");
        lblSubtitulo.setBounds(90,60,220,25);
        lblSubtitulo.setForeground(Color.LIGHT_GRAY);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // =========================
        // USUARIO
        // =========================

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setBounds(40,110,100,25);
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(fuente);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(40,140,260,40);
        txtUsuario.setFont(fuente);

        // =========================
        // PASSWORD
        // =========================

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setBounds(40,190,120,25);
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(fuente);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(40,220,260,40);
        txtPassword.setFont(fuente);

        // =========================
        // BOTON LOGIN
        // =========================

        btnLogin = new JButton("INGRESAR");
        btnLogin.setBounds(40,280,260,45);

        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));

        btnLogin.setBackground(new Color(0,120,215));
        btnLogin.setForeground(Color.WHITE);

        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);

        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogin.addActionListener(e -> iniciarSesion());

        // =========================
        // AGREGAR COMPONENTES
        // =========================

        panelCentral.add(lblTitulo);
        panelCentral.add(lblSubtitulo);

        panelCentral.add(lblUsuario);
        panelCentral.add(txtUsuario);

        panelCentral.add(lblPassword);
        panelCentral.add(txtPassword);

        panelCentral.add(btnLogin);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(new Color(30,30,30));

        wrapper.add(panelCentral);

        panelPrincipal.add(wrapper, BorderLayout.CENTER);

        add(panelPrincipal);
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

            new SocioView();

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario o contraseña incorrectos."
            );
        }
    }
}