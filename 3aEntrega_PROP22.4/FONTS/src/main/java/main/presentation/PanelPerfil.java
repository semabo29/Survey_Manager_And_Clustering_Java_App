package main.presentation;

import main.domain.exceptions.EmailInvalido;
import main.domain.exceptions.IncorrectPassword;
import main.domain.exceptions.NoHayPerfilCargado;
import main.domain.exceptions.YaExistePerfil;
import main.persistence.exceptions.LecturaNula;

import javax.swing.*;
import java.awt.*;

// Panel para inicio de sesion y registro de perfiles

/**
 * Panel para el inicio de sesión y registro de perfiles de usuario.
 * Permite a los usuarios crear y cargar perfiles mediante una interfaz gráfica.
 * @author Hadeer Abbas Khalil Wysocka
 */
public class PanelPerfil extends JPanel {
    /**
     * Botón para registrarse.
     */
    private JButton signUpButton;
    /**
     * Botón para iniciar sesión.
     */
    private JButton logInButton;
    /**
     * Panel que contiene las tarjetas de inicio de sesión y registro.
     */
    private JPanel cardPanel;
    /**
     * Layout de tarjetas para cambiar entre los paneles de inicio de sesión y registro.
     */
    private CardLayout cardLayout;
    /**
     * Presentador asociado al panel de perfil.
     */
    private PresenterPerfil presenterPerfil;
    /**
     * Panel de registro.
     */
    private JPanel signUpPanel;
    /**
     * Panel de inicio de sesión.
     */
    private JPanel logInPanel;

    /**
     * Constructor del panel de perfil.
     * @param presenterPerfil Presentador asociado al panel de perfil.
     */
    public PanelPerfil (PresenterPerfil presenterPerfil) {
        this.presenterPerfil = presenterPerfil;

        setLayout(new BorderLayout(10, 10));

        // Panel superior con botones
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        signUpButton = new JButton("Registrarse");
        logInButton = new JButton("Iniciar Sesión");
        topPanel.add(logInButton);
        topPanel.add(signUpButton);
        add(topPanel, BorderLayout.NORTH);

        // Panel central con CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        signUpPanel = crearSignUpPanel();
        logInPanel = crearLogInPanel();
        cardPanel.add(logInPanel, "LOG_IN");
        cardPanel.add(signUpPanel, "SIGN_UP");
        add(cardPanel, BorderLayout.CENTER);

        // Action Listeners
        signUpButton.addActionListener(e -> cardLayout.show(cardPanel, "SIGN_UP"));
        logInButton.addActionListener(e -> cardLayout.show(cardPanel, "LOG_IN"));
    }

    private JPanel crearLogInPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Campos
        JLabel correoLabel = new JLabel("Correo: ");
        JTextField correoText = new JTextField(15);
        JLabel contrasenaLabel = new JLabel("Contraseña: ");
        JPasswordField contrasenaText = new JPasswordField(15);
        JButton iniciarSesionButton = new JButton("Entra");

        g.gridy = 0; g.gridx = 0;
        panel.add(correoLabel, g);
        g.gridy = 0; g.gridx = 1;
        panel.add(correoText, g);
        g.gridy = 1; g.gridx = 0;
        panel.add(contrasenaLabel, g);
        g.gridy = 1; g.gridx = 1;
        panel.add(contrasenaText, g);
        g.gridy = 2; g.gridx = 0;
        panel.add(iniciarSesionButton, g);

        iniciarSesionButton.addActionListener(e -> {

            try {
                presenterPerfil.cargarPerfil(correoText.getText(), contrasenaText.getText());
            } catch (LecturaNula ex) {
                JOptionPane.showMessageDialog(this, "El perfil no está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IncorrectPassword ex) {
                JOptionPane.showMessageDialog(this, "La contraseña es incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception ex) {
                // Aqui no hace falta deseleccionar
                JOptionPane.showMessageDialog(this, "Excepción inesperada, por favor intente de nuevo. " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mostrar menu principal despues de registrarse
            presenterPerfil.mostrarMenuPrincipal();
        });

        return panel;
    }

    private JPanel crearSignUpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Campos
        JLabel correoLabel = new JLabel("Correo: ");
        JTextField correoText = new JTextField(15);
        JLabel nombreLabel = new JLabel("Nombre: ");
        JTextField nombreText = new JTextField(15);
        JLabel contrasenaLabel = new JLabel("Contraseña: ");
        JPasswordField contrasenaText = new JPasswordField(15);
        JLabel confirmarLabel = new JLabel("Confirmar contraseña: ");
        JPasswordField confirmarText = new JPasswordField(15);
        JButton registrarseButton = new JButton("Registrarse");

        g.gridy = 0; g.gridx = 0;
        panel.add(correoLabel, g);
        g.gridy = 0; g.gridx = 1;
        panel.add(correoText, g);
        g.gridy = 1; g.gridx = 0;
        panel.add(nombreLabel, g);
        g.gridy = 1; g.gridx = 1;
        panel.add(nombreText, g);
        g.gridy = 2; g.gridx = 0;
        panel.add(contrasenaLabel, g);
        g.gridy = 2; g.gridx = 1;
        panel.add(contrasenaText, g);
        g.gridy = 3; g.gridx = 0;
        panel.add(confirmarLabel, g);
        g.gridy = 3; g.gridx = 1;
        panel.add(confirmarText, g);
        g.gridy = 4; g.gridx = 0;
        panel.add(registrarseButton, g);

        registrarseButton.addActionListener(e -> {
            if (!contrasenaText.getText().equals(confirmarText.getText())) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                presenterPerfil.crearPerfil(correoText.getText(), nombreText.getText(), contrasenaText.getText());
            } catch (YaExistePerfil ex) {
                JOptionPane.showMessageDialog(this, "Ya existe un perfil con este correo.", "Error", JOptionPane.ERROR_MESSAGE);
                presenterPerfil.deseleccionarPerfil();
                return;
            } catch (EmailInvalido ex) {
                JOptionPane.showMessageDialog(this, "El correo introducido no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                presenterPerfil.deseleccionarPerfil();
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Excepción inesperada, por favor intente de nuevo." + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                presenterPerfil.deseleccionarPerfil();
                return;
            }

            // Mostrar menu principal despues de registrarse
            presenterPerfil.mostrarMenuPrincipal();
        });

        return panel;
    }
}
