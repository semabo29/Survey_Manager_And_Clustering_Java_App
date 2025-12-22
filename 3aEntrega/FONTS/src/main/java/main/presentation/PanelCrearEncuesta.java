package main.presentation;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.AbstractMap.SimpleEntry;

/**
 * Panel para crear una nueva encuesta.
 * @author Sergi Malaguilla Bombin
 */
public class PanelCrearEncuesta extends JPanel {

    private final PanelEncuesta contenedor;
    private final PresenterEncuesta presenter;
    private final PresenterPerfil presenterp;

    private JTextField txtTituloEncuesta;
    private final JButton btnGuardarCrear;
    private final JButton btnCancelarCrear;

    private static final String textTituloFormulario = "Crear Nueva Encuesta";
    private static final String textLabelTitulo = "Título de la Encuesta:";
    private static final String textBotonCrear = "Crear";
    private static final String textBotonCancelar = "Cancelar";

    private static final String textErrorVacio = "El título de la encuesta no puede estar vacío.";
    private static final String textErrorCrear = "Error al crear la encuesta: ";
    private static final String textExitoCrear = "Encuesta '%s' creada. Añada sus preguntas.";

    private static final String textException = "Exception";
    private static final String textError = "Error";
    private static final String textExito = "Éxito";

    /**
     * Constructor del panel para crear una nueva encuesta.
     * @param contenedor Panel contenedor de encuestas.
     * @param presenter Presenter para la lógica de encuestas.
     * @param presenterp Presenter para la lógica de perfiles.
     */
    public PanelCrearEncuesta(PanelEncuesta contenedor, PresenterEncuesta presenter, PresenterPerfil presenterp) {
        this.contenedor = contenedor;
        this.presenter = presenter;
        this.presenterp = presenterp;

        btnGuardarCrear = new JButton(textBotonCrear);
        btnCancelarCrear = new JButton(textBotonCancelar);

        construirPanelCrearEncuesta();
        asignarListenersCreacion();
    }

    private void construirPanelCrearEncuesta() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel tituloFormulario = new JLabel(textTituloFormulario);
        tituloFormulario.setFont(new Font("Arial", Font.BOLD, 20));
        tituloFormulario.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtTituloEncuesta = new JTextField(30);
        txtTituloEncuesta.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtTituloEncuesta.getPreferredSize().height));

        JPanel panelTituloInput = new JPanel(new BorderLayout());
        panelTituloInput.setMaximumSize(new Dimension(400, 50));
        panelTituloInput.add(new JLabel(textLabelTitulo), BorderLayout.NORTH);
        panelTituloInput.add(txtTituloEncuesta, BorderLayout.CENTER);
        panelTituloInput.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelBotonesCrear = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotonesCrear.add(btnCancelarCrear);
        panelBotonesCrear.add(btnGuardarCrear);
        panelBotonesCrear.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(tituloFormulario);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(panelTituloInput);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(panelBotonesCrear);
    }

    private void asignarListenersCreacion() {
        btnCancelarCrear.addActionListener(e -> contenedor.mostrarPanelCargar());
        btnGuardarCrear.addActionListener(e -> {
            String titulo = txtTituloEncuesta.getText().trim();
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, textErrorVacio, textException, JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                presenter.crearEncuesta(titulo);
                contenedor.cargarListaEncuestas();

                SimpleEntry<String, String> nuevaEncuesta = new SimpleEntry<>(titulo, presenterp.getEmailPerfilCargado());

                contenedor.mostrarPanelModificar(nuevaEncuesta); // pasar a modificar la nueva encuesta

                JOptionPane.showMessageDialog(this, String.format(textExitoCrear, titulo), textExito, JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, textErrorCrear + ex.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}