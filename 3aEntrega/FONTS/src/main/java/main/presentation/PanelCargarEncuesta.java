package main.presentation;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.AbstractMap.SimpleEntry;

/**
 * Panel para cargar una encuesta existente.
 * @author Sergi Malaguilla Bombin
 */
public class PanelCargarEncuesta extends JPanel {

    private final JTextField txtNombreArchivo;
    private final JList<SimpleEntry<String, String>> listaEncuestas;
    private final JScrollPane scrollListaEncuestas;
    private final JButton btnModificar;
    private final JButton btnVer;
    private final JButton btnBorrar;

    private static final String textLabelArchivo = "Archivo Seleccionado:";
    private static final String textNoEncuestas = "No hay encuestas disponibles.";
    private static final String textSeleccioneEncuesta = "Seleccione una encuesta";
    private static final String textFormatoNombre = "%s, %s";

    /**
     * Constructor del panel para cargar una encuesta existente.
     * @param txtNombreArchivo Campo de texto para el nombre del archivo.
     * @param listaEncuestas Lista de encuestas disponibles.
     * @param scrollListaEncuestas ScrollPane que contiene la lista de encuestas.
     * @param btnModificar Botón para modificar la encuesta seleccionada.
     * @param btnVer Botón para ver la encuesta seleccionada.
     * @param btnBorrar Botón para borrar la encuesta seleccionada.
     */
    public PanelCargarEncuesta(JTextField txtNombreArchivo, JList<SimpleEntry<String, String>> listaEncuestas, JScrollPane scrollListaEncuestas, JButton btnModificar, JButton btnVer, JButton btnBorrar) {
        this.txtNombreArchivo = txtNombreArchivo;
        this.listaEncuestas = listaEncuestas;
        this.scrollListaEncuestas = scrollListaEncuestas;
        this.btnModificar = btnModificar;
        this.btnVer = btnVer;
        this.btnBorrar = btnBorrar;

        construirPanelCargarEncuesta();
        asignarListeners();
    }

    private void construirPanelCargarEncuesta() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel panelArchivo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelArchivo.add(new JLabel(textLabelArchivo));
        panelArchivo.add(txtNombreArchivo);
        panelArchivo.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(panelArchivo);

        add(Box.createRigidArea(new Dimension(0, 10)));

        scrollListaEncuestas.setPreferredSize(new Dimension(500, 300));
        scrollListaEncuestas.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel lista = new JPanel(new BorderLayout());
        lista.add(scrollListaEncuestas, BorderLayout.CENTER);
        lista.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(lista);
    }

    private void asignarListeners() {
        listaEncuestas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SimpleEntry<String,String> seleccion = listaEncuestas.getSelectedValue();
                boolean seleccionado = seleccion != null;
                boolean esMensaje = seleccionado && textNoEncuestas.equals(seleccion.getKey());
                btnModificar.setEnabled(seleccionado && !esMensaje);
                btnVer.setEnabled(seleccionado && !esMensaje);
                btnBorrar.setEnabled(seleccionado && !esMensaje);
                if (seleccion != null && !esMensaje) {
                    txtNombreArchivo.setText(String.format(textFormatoNombre, seleccion.getKey(), seleccion.getValue()));
                } else {
                    txtNombreArchivo.setText(textSeleccioneEncuesta);
                }
            }
        });
    }
}