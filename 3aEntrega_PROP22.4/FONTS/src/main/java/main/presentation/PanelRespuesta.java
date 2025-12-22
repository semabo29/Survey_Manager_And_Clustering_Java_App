package main.presentation;

import main.presentation.utils.PanelOpcion;
import main.presentation.utils.PanelPregunta;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Menú principal para la gestión de respuestas del usuario.
 * Esta clase permite consultar, modificar y borrar las respuestas del usuario que ha iniciado sesión.
 * También permite al usuario responder a cualquier encuesta del sistema.
 * @author Yimin Jin
 */
public class PanelRespuesta extends JPanel {
    private PresenterRespuesta presenterRespuesta;
    private PresenterEncuesta presenterEncuesta;

    // Componentes principales
    private JPanel panelSuperior;
    private JButton botonModificar;
    private JButton botonVer;
    private JButton botonDeseleccionar;
    private JLabel tituloLabel;

    // Lista de respuestas con JList
    private JList<SimpleEntry<String, String>> listaRespuestas;
    private JScrollPane scrollPane;

    // Lista de encuestas
    private JList<SimpleEntry<String, String>> listaEncuestas;
    private JScrollPane scrollPaneEncuestas;

    private JButton botonResponder;
    private JPanel panelMostrarRespuestas;
    private ArrayList<PanelPregunta> panelPreguntas;
    private JPanel panelInferior;
    private JButton botonNext;
    private JButton botonPrev;
    private SimpleEntry<String, String> respuestaSeleccionada;
    private SimpleEntry<String, String> encuestaSeleccionada;
    private JButton botonEnviarRespuesta;
    private int index;
    private boolean respondiendo;
    private JButton botonVerListaRespuestas;
    private JButton botonVerListaEncuestas;

    private static final String textTitulo1 = "Estás viendo tus respuestas";
    private static final String textTitulo2 = "Selecciona una encuesta para responder";
    private static final String textExcepcionInesperada = "Excepción inesperada: ";
    private static final String textError = "Error";
    private static final String textErrorMostrarEncuesta = "Error al mostrar la encuesta: ";
    private static final String textVerRespuestas = "Ver respuestas";
    private static final String textVerEncuestas = "Ver encuestas";
    private static final String textResponder = "Responder Encuesta";
    private static final String textSiguiente = "Siguiente >>";
    private static final String textAnterior = "<< Anterior";
    private static final String textGuardarRespuesta = "Guardar Respuesta";
    private static final String textVer = "Ver";
    private static final String textModificar = "Modificar";
    private static final String textDeseleccionar = "Deseleccionar";
    private static final String textBorrar = "Borrar";
    private static final String textRespuestaFormatoIncorrecto = "La respuesta no sigue el formato de la encuesta";
    private static final String textRespondiendoEncuesta = "Respondiendo encuesta";
    private static final String textModificandoRespuesta = "Modificando respuesta";
    private static final String textConfirmarModificacion = "Confirmar modificación";
    private static final String textConfirmarBorrado = "Confirmar borrado";
    private static final String textConfirmarModificarPregunta = "¿Deseas modificar la respuesta de la encuesta \"%s\"?";
    private static final String textConfirmarBorrarPregunta = "¿Está seguro que quiere borrar la respuesta?";
    private static final String textRespuestasNoAdecuadas = "Algunas respuestas no son adecuadas, vuelva a intentarlo";
    private static final String textFalloGuardarRespuesta = "Fallo inesperado al intentar guardar la respuesta";
    private static final String textRespuestaGuardadaExito = "Respuesta guardada con éxito";
    private static final String textTituloRespuestaGuardada = "Respuesta guardada";
    private static final String textErrorBorrarEncuesta = "Error al intentar borrar la encuesta: ";
    private static final String textRespuestaBorradaExito = "Respuesta borrada con éxito";
    private static final String textTituloRespuestaBorrada = "Respuesta borrada";
    private static final String textErrorGetListaRespuesta = "Error al obtener la lista de respuestas: ";
    private static final String textErrorGetListaEncuesta = "Error al obtener la lista de encuestas: ";


    /**
     * Constructor de la clase.
     * @param presenterRespuesta Presentador para las operaciones de respuesta.
     * @param presenterEncuesta Presentador para las operaciones de encuesta.
     */
    public PanelRespuesta(PresenterRespuesta presenterRespuesta, PresenterEncuesta presenterEncuesta) {
        this.presenterRespuesta = presenterRespuesta;
        this.presenterEncuesta = presenterEncuesta;
        inicializarComponentes();
    }

    // =================================================================================================================
    // Inicializacion

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        listaRespuestas = new JList<>();
        listaEncuestas = new JList<>();

        inicializarPanelSuperior();

        add(panelSuperior, BorderLayout.NORTH);

        inicializarListaRespuestas();
        inicializarListaEncuestas();

        add(scrollPane, BorderLayout.CENTER);

        inicializarBotonesMostrarPreguntas();

        inicializarPanelInferior();

        add(panelInferior, BorderLayout.SOUTH);

        asignarListeners();
    }

    private void inicializarPanelSuperior() {
        // Panel superior con título y botones
        panelSuperior = new JPanel(new BorderLayout());

        tituloLabel = new JLabel(textTitulo1);
        tituloLabel.setHorizontalAlignment(SwingConstants.LEFT);

        botonVerListaRespuestas = new JButton(textVerRespuestas);
        botonVerListaEncuestas = new JButton(textVerEncuestas);

        // Panel de botones a la derecha
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBotones.add(botonVerListaRespuestas);
        panelBotones.add(botonVerListaEncuestas);

        panelSuperior.add(tituloLabel, BorderLayout.WEST);
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    private void inicializarListaRespuestas() {
        // JList de respuestas con PanelOpcion como renderizador
        try {
            listaRespuestas = new JList<>(presenterRespuesta.getRespuestasPerfilCargado().toArray(new SimpleEntry[0]));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textErrorGetListaRespuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
        }
        listaRespuestas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaRespuestas.setCellRenderer(new PanelOpcion());

        scrollPane = new JScrollPane(listaRespuestas);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        respuestaSeleccionada = null;
    }

    private void inicializarListaEncuestas() {
        try {
            listaEncuestas = new JList<>(presenterEncuesta.getTituloAutorAllEncuestas().toArray(new SimpleEntry[0]));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textErrorGetListaEncuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
        }
        listaEncuestas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEncuestas.setCellRenderer(new PanelOpcion());

        scrollPaneEncuestas = new JScrollPane(listaEncuestas);
        scrollPaneEncuestas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneEncuestas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        encuestaSeleccionada = null;
    }

    private void inicializarBotonesMostrarPreguntas() {
        panelPreguntas = new ArrayList<>();
        botonResponder = new JButton(textResponder);
        panelMostrarRespuestas = new JPanel(new BorderLayout());
        botonNext = new JButton(textSiguiente);
        botonPrev = new JButton(textAnterior);
        botonEnviarRespuesta = new JButton(textGuardarRespuesta);

        index = 0;
        respondiendo = false;
    }

    private void inicializarPanelInferior() {
        panelInferior = new JPanel(new GridLayout(1, 0));

        botonVer = new JButton(textVer);
        botonVer.setEnabled(false);

        botonModificar = new JButton(textModificar);
        botonModificar.setEnabled(false);

        botonDeseleccionar = new JButton(textDeseleccionar);
        botonDeseleccionar.setEnabled(false);

        botonVer.setEnabled(false);
        botonModificar.setEnabled(false);
        botonDeseleccionar.setEnabled(false);
        panelInferior.add(botonVer);
        panelInferior.add(botonModificar);
        panelInferior.add(botonDeseleccionar);
        JButton borrar = new JButton(textBorrar);
        borrar.setEnabled(false);
        borrar.addActionListener(e -> actionBorrarRespuesta());
        panelInferior.add(borrar);

        panelMostrarRespuestas.add(panelInferior, BorderLayout.SOUTH);

    }

    // =================================================================================================================
    // Asignar listeners

    private void asignarListeners() {
        listaRespuestas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                respuestaSeleccionada = listaRespuestas.getSelectedValue();
                botonVer.setEnabled(true);
                botonModificar.setEnabled(true);
                botonDeseleccionar.setEnabled(true);
                if (panelInferior.getComponentCount() >= 4) {
                    Component c = panelInferior.getComponent(3);
                    if (c instanceof JButton) {
                        JButton borrar = (JButton) c;
                        borrar.setEnabled(true);
                    }
                }
            }
        });
        listaEncuestas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                encuestaSeleccionada = listaEncuestas.getSelectedValue();
                botonDeseleccionar.setEnabled(true);
                botonResponder.setEnabled(true);
            }
        });

        botonVer.addActionListener(e -> actionVerRespuesta());
        botonModificar.addActionListener(e -> actionModificarRespuesta());
        botonDeseleccionar.addActionListener(e -> deseleccionarRespuesta());

        botonVerListaEncuestas.addActionListener(e -> actionVerListaEncuestas());
        botonVerListaRespuestas.addActionListener(e -> actionVerListaRespuestas());

        botonNext.addActionListener(e -> actionNext());
        botonPrev.addActionListener(e -> actionPrev());

        botonResponder.addActionListener(e -> actionResponderEncuesta());
        botonEnviarRespuesta.addActionListener(e -> actionGuardarRespuesta());
    }

    // Deseleccionar la respuesta
    private void deseleccionarRespuesta() {
        listaRespuestas.clearSelection();
        respuestaSeleccionada = null;
        botonVer.setEnabled(false);
        botonModificar.setEnabled(false);
        botonDeseleccionar.setEnabled(false);
        if (panelInferior.getComponentCount() >= 4) {
            Component c = panelInferior.getComponent(3);
            if (c instanceof JButton) {
                JButton borrar = (JButton) c;
                borrar.setEnabled(false);
            }
        }
    }

    // Deseleccionar la encuesta
    private void deseleccionarEncuesta() {
        listaEncuestas.clearSelection();
        encuestaSeleccionada = null;
        botonDeseleccionar.setEnabled(false);
        botonResponder.setEnabled(false);
    }

    private void actionVerRespuesta() {
        try {
            rellenarPanelPreguntasConsulta();
        } catch (Exception e) {
            return;
        }

        index = 0;
        respondiendo = false;
        botonPrev.setEnabled(false);
        botonNext.setEnabled(index < panelPreguntas.size() - 1);
        botonEnviarRespuesta.setVisible(false);

        if (!panelPreguntas.isEmpty()) {
            removePanelCentral();
            add(panelPreguntas.get(index), BorderLayout.CENTER);
            ponerPanelInferiorDePreguntas();
        }
        revalidate();
        repaint();
    }

    private void actionResponderEncuesta() {
        try {
            rellenarPanelPreguntasResponder(encuestaSeleccionada.getKey(), encuestaSeleccionada.getValue());
        } catch (Exception e) {
            return;
        }
        presenterRespuesta.responderEncuesta();

        tituloLabel.setText(textRespondiendoEncuesta);
        prepararacionResponder();
    }

    private void actionModificarRespuesta() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                String.format(textConfirmarModificarPregunta, respuestaSeleccionada.getKey()),
                textConfirmarModificacion,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                presenterRespuesta.modificarRespuesta(respuestaSeleccionada.getKey(), respuestaSeleccionada.getValue());
                rellenarPanelPreguntasModificar();
            } catch (Exception e) {
                return;
            }
            tituloLabel.setText(textModificandoRespuesta);
            prepararacionResponder();
        }
    }

    private void actionVerListaRespuestas() {
        deseleccionarEncuesta();
        removePanelCentral();
        actualizarRespuestas();
        tituloLabel.setText(textTitulo1);
        add(scrollPane);
        panelInferior.removeAll();
        panelInferior.add(botonVer);
        panelInferior.add(botonModificar);
        panelInferior.add(botonDeseleccionar);
        botonDeseleccionar.addActionListener(e -> deseleccionarRespuesta());
        JButton borrar = new JButton(textBorrar);
        borrar.setEnabled(false);
        borrar.addActionListener(e -> actionBorrarRespuesta());
        panelInferior.add(borrar);
        revalidate();
        repaint();
    }

    private void actionVerListaEncuestas() {
        deseleccionarRespuesta();
        removePanelCentral();
        actualizarEncuestas();
        tituloLabel.setText(textTitulo2);
        add(scrollPaneEncuestas);
        panelInferior.removeAll();
        panelInferior.add(botonResponder);
        panelInferior.add(botonDeseleccionar);
        // Reutilizar botonDeseleccionar
        botonDeseleccionar.addActionListener(ae -> deseleccionarEncuesta());
        revalidate();
        repaint();
    }

    private void actionGuardarRespuesta() {
        for (PanelPregunta pregunta : panelPreguntas) {
            try{
                pregunta.responderPregunta();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, textRespuestasNoAdecuadas, textError, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            presenterRespuesta.guardarRespuesta();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textFalloGuardarRespuesta, textError, JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(this, textRespuestaGuardadaExito, textTituloRespuestaGuardada, JOptionPane.INFORMATION_MESSAGE);
        actionVerListaRespuestas();
    }

    private void actionBorrarRespuesta() {
        if (respuestaSeleccionada != null) {
            int confirm = JOptionPane.showConfirmDialog(this, textConfirmarBorrarPregunta,
                    textConfirmarBorrado, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    presenterEncuesta.importarEncuesta(respuestaSeleccionada.getKey(), respuestaSeleccionada.getValue());
                    presenterRespuesta.getRespuestaAEncuestaCargadaPerfilActual();
                    presenterRespuesta.borrarRespuestaActual();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, textErrorBorrarEncuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(this, textRespuestaBorradaExito, textTituloRespuestaBorrada, JOptionPane.INFORMATION_MESSAGE);
                actualizarRespuestas();
            }
        }
    }

    private void actionPrev() {
        if (index > 0) {
            index--;
            removePanelCentral();
            add(panelPreguntas.get(index), BorderLayout.CENTER);
            if (index == 0) {
                botonPrev.setEnabled(false);
            }
            botonEnviarRespuesta.setVisible(false);
            botonNext.setEnabled(true);
            revalidate();
            repaint();
        }
    }

    private void actionNext() {
        if (index < panelPreguntas.size() - 1) {
            index++;
            removePanelCentral();
            add(panelPreguntas.get(index), BorderLayout.CENTER);
            if (index == panelPreguntas.size() - 1) {
                botonNext.setEnabled(false);
                if (respondiendo) botonEnviarRespuesta.setVisible(true);
            }
            botonPrev.setEnabled(true);
            revalidate();
            repaint();
        }
    }

    // =================================================================================================================
    // Utils

    /**
     * Actualiza las listas de respuestas y encuestas, y vuelve al menú inicial.
     */
    public void refrescar() {
        actualizarRespuestas();
        actualizarEncuestas();
        actionVerListaRespuestas();
    }

    // Actualiza la lista de respuestas
    private void actualizarRespuestas() {
        try {
            HashSet<SimpleEntry<String, String>> respuestas = presenterRespuesta.getRespuestasPerfilCargado();

            SimpleEntry<String, String>[] arrayRespuestas;
            arrayRespuestas = respuestas.toArray(new SimpleEntry[0]);

            listaRespuestas.setListData(arrayRespuestas);
            deseleccionarRespuesta();
            revalidate();
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textErrorGetListaRespuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
        }
    }

    // Actualiza la lista de encuestas
    private void actualizarEncuestas() {
        try {
            listaEncuestas.setListData(presenterEncuesta.getTituloAutorAllEncuestas().toArray(new SimpleEntry[0]));
            deseleccionarEncuesta();
            revalidate();
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textErrorGetListaEncuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cambiar panel inferior al mostrar una respuesta o encuesta
    private void ponerPanelInferiorDePreguntas() {
        panelInferior.removeAll();
        panelInferior.add(botonPrev);
        panelInferior.add(botonNext);
        panelInferior.add(botonEnviarRespuesta);
    }

    // Quitar lo que haya en el centro para poder reeemplazarlo
    private void removePanelCentral() {
        try {
            BorderLayout layout = (BorderLayout) getLayout();
            remove(layout.getLayoutComponent(BorderLayout.CENTER));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textExcepcionInesperada + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
        }
    }

    // Prepara el menu para responder una encuesta o modificar una respuesta
    private void prepararacionResponder() {
        index = 0;
        respondiendo = true;
        botonPrev.setEnabled(false);
        botonNext.setEnabled(index < panelPreguntas.size() - 1);
        botonEnviarRespuesta.setVisible(index == panelPreguntas.size() - 1);

        if (!panelPreguntas.isEmpty()) {
            removePanelCentral();
            add(panelPreguntas.get(index), BorderLayout.CENTER);
            ponerPanelInferiorDePreguntas();
        }
        revalidate();
        repaint();
    }

    // Crea e inserta paneles pregunta en modo consultar respuesta
    private void rellenarPanelPreguntasConsulta() throws Exception {
        try {
            presenterEncuesta.importarEncuesta(respuestaSeleccionada.getKey(), respuestaSeleccionada.getValue());
            ArrayList<String> respuestas = presenterRespuesta.getRespuestaAEncuestaCargadaPerfilActual();

            ArrayList<HashMap<String, String>> preguntas = presenterEncuesta.getPreguntasEncuestaCargada();

            if (respuestas.size() != preguntas.size()) {
                JOptionPane.showMessageDialog(this, textRespuestaFormatoIncorrecto, textError, JOptionPane.ERROR_MESSAGE);
                return;
            }

            panelPreguntas = new ArrayList<>();
            for (int i = 0; i < respuestas.size(); i++) {
                panelPreguntas.add(new PanelPregunta(preguntas.get(i), respuestas.get(i)));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textErrorMostrarEncuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    // Crea e inserta paneles pregunta en modo responder
    private void rellenarPanelPreguntasResponder(String titulo, String autor) throws Exception {
        try {
            presenterEncuesta.importarEncuesta(titulo, autor);

            ArrayList<HashMap<String, String>> preguntas = presenterEncuesta.getPreguntasEncuestaCargada();

            panelPreguntas = new ArrayList<>();
            for (HashMap<String, String> pregunta : preguntas) {
                panelPreguntas.add(new PanelPregunta(pregunta, presenterRespuesta));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textErrorMostrarEncuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    // Crea e inserta paneles pregunta en modo responder, poniendo respuestas por defecto
    private void rellenarPanelPreguntasModificar() throws Exception {
        try {
            rellenarPanelPreguntasResponder(respuestaSeleccionada.getKey(), respuestaSeleccionada.getValue());
            ArrayList<String> respuestas = presenterRespuesta.getRespuestaAEncuestaCargadaPerfilActual();

            if (respuestas.size() != panelPreguntas.size()) {
                JOptionPane.showMessageDialog(this, textRespuestaFormatoIncorrecto, textError, JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < respuestas.size(); i++) {
                panelPreguntas.get(i).setRespuesta(respuestas.get(i));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textErrorMostrarEncuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
}