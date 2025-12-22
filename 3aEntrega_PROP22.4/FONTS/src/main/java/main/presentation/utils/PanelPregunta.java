package main.presentation.utils;

import main.presentation.PresenterRespuesta;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Panel que muestra una pregunta de una encuesta.
 * Este panel puede usarse para consultar una pregunta, mostrar la respuesta a una pregunta, responder a una pregunta
 * o modificar una respuesta.
 * @author Yimin Jin
 */
public class PanelPregunta extends JPanel {
    private int id;
    private boolean responder;
    private boolean mostrarRespuesta;
    private PresenterRespuesta preRes;
    private HashMap<String, String> info;
    private TreeSet<Integer> opcionesSelected;
    private String formatoLibre;
    private int respuestaNumerica;
    private String respuesta;

    private static final String textEsObligatorio = "Esta pregunta es obligatoria.";
    private static final String textCheck = "<html><font color='green'>✔ </font>";
    private static final String textSeleccionarMax = "Puedes seleccionar como máximo ";
    private static final String textNumOpciones = " opciones.";
    private static final String textError = "Error";
    private static final String textNoMostrar = "No se puede mostrar la respuesta";
    private static final String textRespuesta = "Respuesta: ";
    private static final String textErrorInesperado = "Error inesperado, no se ha podido mostrar la respuesta";
    private static final String textMin = "Min: ";
    private static final String textMax = " Max: ";

    /**
     * Constructor para consultar una pregunta.
     * @param info Información de una pregunta.
     */
    public PanelPregunta(HashMap<String, String> info) {
        responder = false;
        mostrarRespuesta = false;
        this.info = info;
        inicializar(info);
    }

    /**
     * Constructor que permite responder a la pregunta.
     * @param info Información de una pregunta.
     * @param preRes Presentador para operaciones sobre respuesta.
     */
    public PanelPregunta(HashMap<String, String> info, PresenterRespuesta preRes) {
        responder = true;
        mostrarRespuesta = false;
        this.preRes = preRes;
        this.info = info;
        inicializar(info);
    }

    /**
     * Constructor para mostrar una respuesta a una pregunta.
     * @param info Información de una pregunta.
     * @param respuesta Respuesta a la pregunta.
     */
    public PanelPregunta(HashMap<String, String> info, String respuesta) {
        responder = false;
        mostrarRespuesta = true;
        this.info = info;
        this.respuesta = respuesta;
        inicializar(info);
    }

    private void inicializar(HashMap<String, String> info) {
        setLayout(new GridLayout(0,1));
        id = Integer.parseInt(info.get("ID"));
        boolean obligatorio = Boolean.parseBoolean(info.get("OBLIGATORIO"));
        String texto = info.get("TEXTO");
        String descripcion = info.get("DESCRIPCION");
        JPanel infoTipo = crearTipo(info);
        JLabel titulo = new JLabel(id + ". " + texto);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD));

        JPanel pregunta = new JPanel();
        pregunta.setLayout(new GridLayout(0,1));
        pregunta.add(titulo);
        if (descripcion != null && !descripcion.isEmpty()) {
            JTextArea desc = new JTextArea(descripcion);
            desc.setEditable(false);
            desc.setLineWrap(true);
            desc.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(desc);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            pregunta.add(scrollPane);
        }
        if (obligatorio) {
            JLabel textObligatorio = new JLabel(textEsObligatorio);
            textObligatorio.setForeground(Color.RED);
            pregunta.add(textObligatorio);
        }

        add(pregunta);

        JScrollPane scroll = new JScrollPane(infoTipo);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scroll);

        setBorder(new CompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new CompoundBorder(
                        new EtchedBorder(EtchedBorder.LOWERED),
                        new EmptyBorder(10, 10, 10, 10))));
    }

    private JPanel crearTipo(HashMap<String, String> info) {
        JPanel panelTipo = new JPanel();
        panelTipo.setLayout(new GridLayout(0,1));

        String tipo = info.get("TIPO");
        if (tipo.equals("ConOpciones")) {
            opcionesSelected = new TreeSet<>();
            int numOpciones = Integer.parseInt(info.get("NUMOPCIONES"));
            int maxSelect = Integer.parseInt(info.get("MAXSELECT"));
            crearOpciones(info, panelTipo, numOpciones, maxSelect);

        } else if (tipo.equals("Numerica")) {
            int min = Integer.parseInt(info.get("MIN"));
            int max = Integer.parseInt(info.get("MAX"));
            respuestaNumerica = min;

            crearNumerica(panelTipo, min, max);
        } else if (tipo.equals("FormatoLibre")) {
            formatoLibre = "";

            crearFormatoLibre(panelTipo);
        }

        return panelTipo;
    }

    // Añadir componentes correspondientes al panel

    private void crearOpciones(HashMap<String, String> info, JPanel panelTipo, int numOpciones, int maxSelect) {
            ButtonGroup group = new ButtonGroup();
            // Caso mostrar respuesta
            if (mostrarRespuesta) {
                HashSet<Integer> opcionesSeleccionadas = getOpcionesString();

                for (int i = 1; i <= numOpciones; i++) {
                    String texto = i + ". " + info.get("OPCION" + i);
                    JLabel lb = new JLabel(texto);
                    if (opcionesSeleccionadas.contains(i)) {
                        lb.setText(textCheck + lb.getText());
                        lb.setFont(lb.getFont().deriveFont(Font.ITALIC));
                    }
                    panelTipo.add(lb);
                }
                // Caso maximo seleccionable 1
            } else if (maxSelect == 1) {
                for (int i = 1; i <= numOpciones; i++) {
                    JRadioButton rb = new JRadioButton(i + ". " + info.get("OPCION" + i));
                    final int select = i;

                    // Listener
                    rb.addItemListener(
                            e -> actionOpciones(e, select)
                    );

                    group.add(rb);
                    panelTipo.add(rb);
                }
                // Caso multiples seleccionables
            } else {
                for (int i = 1; i <= numOpciones; i++) {
                    JCheckBox cb = new JCheckBox(i + ". " + info.get("OPCION" + i));
                    final int select = i;

                    // Listener
                    cb.addItemListener(
                            e -> actionOpciones(e, select)
                    );

                    panelTipo.add(cb);
                }
                panelTipo.add(new JLabel(textSeleccionarMax + maxSelect + textNumOpciones));
            }
    }

    // Metodo auxiliar para devolver las opciones de una respuesta con multiples opciones
    private HashSet<Integer> getOpcionesString() {
        HashSet<Integer> opciones = new HashSet<>();
        if (!respuesta.isEmpty()) {
            for (String r : respuesta.split(" ")) {
                try {
                    opciones.add(Integer.parseInt(r));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, textNoMostrar, textError, JOptionPane.ERROR_MESSAGE);
                    return new HashSet<>();
                }
            }
        }
        return opciones;
    }

    private void crearNumerica(JPanel panelTipo, int min, int max) {
        if (mostrarRespuesta) {
            JLabel lb = new JLabel(textRespuesta + respuesta, JLabel.CENTER);
            panelTipo.add(lb);
        } else {
            JSpinner sp = new JSpinner(new SpinnerNumberModel(min, min, max, 1));

            // Listener
            sp.addChangeListener(
                    e -> actionNumerica((Integer) sp.getValue())
            );

            panelTipo.add(sp);
            JLabel lb = new JLabel(textMin + min + textMax + max);
            panelTipo.add(lb);
        }
    }

    private void crearFormatoLibre(JPanel panelTipo) {
        if (mostrarRespuesta) {
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setText(respuesta);
            JScrollPane sp = new JScrollPane(textArea);
            panelTipo.add(sp);
        } else {
            JTextArea f = new JTextArea();

            // Listener
            f.getDocument().addDocumentListener(
                    new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            actionFormatoLibre(f.getText());
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            actionFormatoLibre(f.getText());
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            // ignorar
                        }
                    }
            );
            panelTipo.add(f);
        }
    }


    // Actions

    private void actionOpciones(ItemEvent e, int selected) {
        int state = e.getStateChange();
        if (state == ItemEvent.SELECTED) {
            opcionesSelected.add(selected);
        } else if (state == ItemEvent.DESELECTED) {
            opcionesSelected.remove(selected);
        }
    }

    private void actionNumerica(int value) {
        respuestaNumerica = value;
    }

    private void actionFormatoLibre(String texto) {
        // Añadir comprobacion de longitud si queremos limitarlo

        formatoLibre = texto;
    }

    // Responder

    /**
     * Método que guarda la respuesta de una pregunta. Este método no guarda la respuesta a toda la encuesta.
     * Solo funciona si tiene un presentador asociado.
     * @throws Exception Vuelve a lanzar las excepciones que recibe.
     */
    public void responderPregunta() throws Exception {
        if (responder) {
            String tipo = info.get("TIPO");
            try {
                if (tipo.equals("ConOpciones")) {
                    preRes.responderPreguntaOpciones(id, new ArrayList<>(opcionesSelected));
                } else if (tipo.equals("Numerica")) {
                    preRes.responderPreguntaNumerica(id, respuestaNumerica);
                } else if (tipo.equals("FormatoLibre")) {
                    preRes.responderPreguntaFormatoLibre(id, formatoLibre);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
                throw e;
            }
        }
    }

    // Añadir respuesta

    /**
     * Muestra una respuesta de la pregunta.
     * Es útil para ver la respuesta antigua de la pregunta para poder modificarla.
     * Esta operación solo funciona si tiene un presentador asociado.
      * @param respuesta Respuesta a la pregunta.
     */
    public void setRespuesta(String respuesta) {
        if (responder) {
            try {
                this.respuesta = respuesta;
                JScrollPane scroll = (JScrollPane) getComponent(1);
                JPanel infoTipo = (JPanel) scroll.getViewport().getView();
                String tipo = info.get("TIPO");
                if (tipo.equals("ConOpciones")) {
                    if (!respuesta.isEmpty()) {
                        HashSet<Integer> opcionesSeleccionadas = getOpcionesString();
                        int maxSelect = Integer.parseInt(info.get("MAXSELECT"));

                        for (Integer i : opcionesSeleccionadas) {
                            if (maxSelect == 1) {
                                JRadioButton rb = (JRadioButton) infoTipo.getComponent(i - 1);
                                rb.setSelected(true);
                            } else {
                                JCheckBox cb = (JCheckBox) infoTipo.getComponent(i - 1);
                                cb.setSelected(true);
                            }
                            opcionesSelected.add(i);
                        }
                    }
                } else if (tipo.equals("Numerica")) {
                    int value = Integer.parseInt(respuesta);
                    JSpinner sp = (JSpinner) infoTipo.getComponent(0);
                    sp.setValue(value);
                    respuestaNumerica = value;
                } else if (tipo.equals("FormatoLibre")) {
                    JTextArea textArea = (JTextArea) infoTipo.getComponent(0);
                    textArea.setText(respuesta);
                    formatoLibre = respuesta;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, textErrorInesperado, textError, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
}
