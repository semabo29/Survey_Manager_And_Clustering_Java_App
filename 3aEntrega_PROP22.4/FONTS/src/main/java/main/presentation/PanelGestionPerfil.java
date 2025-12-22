package main.presentation;

import main.domain.exceptions.NoHayPerfilCargado;

import javax.swing.*;
import java.awt.*;

// Panel para consultar y borrar el perfil cargado
// NOTA: El caso de uso de ConsultarPerfil, se muestra en el panel directamente en vez de que el usuario tenga que
// directamente pedirle que lo quiere consultar.

/**
 * Panel para la gestión del perfil de usuario.
 * Permite consultar la información del perfil cargado y eliminarlo si se desea.
 * @author Hadeer Abbas Khalil Wysocka
 */
public class PanelGestionPerfil extends JPanel {
    /**
     * Presentador asociado al panel de gestión de perfil.
     */
    private PresenterPerfil presenterPerfil;
    /**
     * Panel que muestra la información del perfil.
     */
    private JPanel panelCentral;
    /**
     * Panel que contiene el botón de borrado del perfil.
     */
    private JPanel panelBorrado;

    /**
     * Constructor del panel de gestión de perfil.
     * @param presenterPerfil Presentador asociado al panel de gestión de perfil.
     */
    public PanelGestionPerfil (PresenterPerfil presenterPerfil) {
        this.presenterPerfil = presenterPerfil;

        setLayout(new BorderLayout(10, 10));

        panelCentral = crearPanelCentral();
        add(panelCentral, BorderLayout.CENTER);

        panelBorrado = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton eliminarButton = new JButton("Eliminar perfil");
        panelBorrado.add(eliminarButton);
        add(panelBorrado, BorderLayout.SOUTH);

        eliminarButton.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this, "¿Estás seguro/a de que quieres borrar el perfil?", "Confirmar borrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                try {
                    presenterPerfil.borrarPerfilCargado();
                } catch (NoHayPerfilCargado ex) {
                    JOptionPane.showMessageDialog(this, "No hay ningun perfil cargado.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Excepción inesperada, por favor intente de nuevo. " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                JOptionPane.showMessageDialog(this, "El perfil fue eliminado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // ANTES DE MOSTRAR EL PANELGESTIONPERFIL, HAY QUE OBLIGATORIAMENTE LLAMAR A ESTA FUNCION ANTES

    /**
     * Refresca el panel de gestión de perfil para mostrar la información actualizada del perfil cargado.
     * Se ha de llamar antes de mostrar el panel para actualizar el contenido del panel central.
     */
    public void refrescar () {
        remove(panelCentral);
        // Crea un nuevo panel central, que es el componente dinamico que cambia
        panelCentral = crearPanelCentral();
        add(panelCentral, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel crearPanelCentral () {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Campos
        JLabel perfilCargado = new JLabel("Perfil cargado");
        Font fuente = new Font("Arial", Font.BOLD, 18);
        perfilCargado.setFont(fuente);

        g.gridy = 0; g.gridx = 0;
        panel.add(perfilCargado, g);

        try {
            // Mostrar el nombre y correo

            String nombrePerfil = presenterPerfil.getNombrePerfilCargado();
            String correoPerfil = presenterPerfil.getEmailPerfilCargado();
            JLabel nombreLabel = new JLabel("Nombre: " + nombrePerfil);
            JLabel correoLabel = new JLabel("Correo: " + correoPerfil);

            g.gridy = 1; g.gridx = 0;
            panel.add(nombreLabel, g);
            g.gridy = 2; g.gridx = 0;
            panel.add(correoLabel, g);

        } catch (NoHayPerfilCargado e) {
            // Mostrar unicamente: No hay ningun perfil cargado.

            JLabel noHay = new JLabel("No hay ningun perfil cargado.");
            g.gridy = 1; g.gridx = 0;
            panel.add(noHay, g);
        }

        return panel;
    }
}
