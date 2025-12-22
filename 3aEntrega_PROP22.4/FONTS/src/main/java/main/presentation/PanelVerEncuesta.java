package main.presentation;

import main.presentation.utils.PanelPregunta;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Panel para ver una encuesta cargada.
 * @author Sergi Malaguilla Bombin
 */
public class PanelVerEncuesta extends JPanel {


    private final PresenterEncuesta presenter;

    //Ver
    private int preguntaActualIndex;
    private final ArrayList<HashMap<String, String>> listaPreguntasInfo;
    private JPanel panelPreguntaContenedor;
    private final JButton btnSiguientePregunta;
    private final JButton btnAnteriorPregunta;

    private static final String textAnterior = "<< Anterior";
    private static final String textSiguiente = "Siguiente >>";
    private static final String textVisualizando = "Visualizando Encuesta: ";
    private static final String textNoPreguntas = "No hay preguntas cargadas.";
    private static final String textContador = "Pregunta %d de %d";

    /**
     * Constructor del panel para ver una encuesta cargada.
     * @param presenter Presenter para la lógica de encuestas.
     */
    public PanelVerEncuesta(PresenterEncuesta presenter) {
        this.presenter = presenter;

        this.listaPreguntasInfo = presenter.getPreguntasEncuestaCargada();
        this.preguntaActualIndex = 0;

        //navegacion preguntas
        this.btnAnteriorPregunta = new JButton(textAnterior);
        this.btnSiguientePregunta = new JButton(textSiguiente);

        setLayout(new BorderLayout());
        construirPanelVerEncuesta();
        actualizarVistaPregunta();
    }


    public boolean tienePreguntas() {
        return listaPreguntasInfo != null && !listaPreguntasInfo.isEmpty();
    }

    private void construirPanelVerEncuesta() {

        JLabel titulo = new JLabel(textVisualizando + presenter.getTituloEncuestaCargada());
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        panelPreguntaContenedor = new JPanel(new BorderLayout());
        add(panelPreguntaContenedor, BorderLayout.CENTER);

        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNavegacion.setBorder(new EmptyBorder(10, 0, 10, 0));
        panelNavegacion.add(btnAnteriorPregunta);
        panelNavegacion.add(btnSiguientePregunta);

        add(panelNavegacion, BorderLayout.SOUTH);

        asignarListenersNavegacion();
    }

    private void actualizarVistaPregunta() {
        panelPreguntaContenedor.removeAll();

        //por si acaso, pero no seria necesario pq ya se comprueba antes
        if (listaPreguntasInfo == null || listaPreguntasInfo.isEmpty()) {
            btnAnteriorPregunta.setEnabled(false);
            btnSiguientePregunta.setEnabled(false);
            panelPreguntaContenedor.add(new JLabel(textNoPreguntas), BorderLayout.CENTER);
            return;
        }

        HashMap<String, String> infoPregunta = listaPreguntasInfo.get(preguntaActualIndex);

        PanelPregunta panelPregunta = new PanelPregunta(infoPregunta);

        panelPreguntaContenedor.add(panelPregunta, BorderLayout.CENTER);

        //desactivar si estas en un extremo u otro
        btnAnteriorPregunta.setEnabled(preguntaActualIndex > 0);
        btnSiguientePregunta.setEnabled(preguntaActualIndex < listaPreguntasInfo.size() - 1);

        //contador de pregunta
        int total = listaPreguntasInfo.size();
        String contador = String.format(textContador, preguntaActualIndex + 1, total);

        panelPreguntaContenedor.add(new JLabel(contador, SwingConstants.CENTER), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void asignarListenersNavegacion() {
        btnSiguientePregunta.addActionListener(e -> {
            if (preguntaActualIndex < listaPreguntasInfo.size() - 1) {
                preguntaActualIndex++;
                actualizarVistaPregunta();
            }
        });
        btnAnteriorPregunta.addActionListener(e -> {
            if (preguntaActualIndex > 0) {
                preguntaActualIndex--;
                actualizarVistaPregunta();
            }
        });
    }
}