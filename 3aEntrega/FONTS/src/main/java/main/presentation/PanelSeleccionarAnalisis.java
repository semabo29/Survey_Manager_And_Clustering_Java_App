package main.presentation;

import main.domain.exceptions.AlgoritmoNoReconocido;
import main.domain.exceptions.EvaluadorNoReconocido;
import main.domain.exceptions.InicializadorNoReconocido;
import main.domain.exceptions.InicializadorYAlgoritmoIncompatibles;
import main.presentation.utils.PanelOpcion;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/**
 * Menú dedicado a la gestión de análisis de encuestas y sus respuestas.
 * Esta clase permite seleccionar las encuestas y respuestas a analizar y configurar el análisis
 * en base a una serie de parámetros.
 * @author Javier Zhangpan
 */
public class PanelSeleccionarAnalisis extends JPanel {
    private static PresenterAnalisis presenterAnalisis;
    private static PresenterEncuesta presenterEncuesta;
    private static PresenterRespuesta presenterRespuesta;

    private JComboBox<String> comboBoxOpcionesAlgoritmo;
    private JComboBox<String> comboBoxOpcionesInicializador;
    private JComboBox<String> comboBoxOpcionesEvaluador;
    private JSpinner spinnerSeleccionarK;
    private JButton botonAutoSeleccionarK;
    private JButton botonIniciarAnalisis;
    private JList<AbstractMap.SimpleEntry<String, String>> listaEncuestas;
    private JList<String> listaRespuestas;
    private JPanel panelListas;
    private CardLayout cardLayout;
    private JPanel seleccionar;
    private PanelResultadoAnalisis panelResultadoAnalisis;
    private JButton botonCargarSeleccion;

    private static final Vector<String> ALGORITMOS_POSIBLES = new Vector<>(List.of("KMeansOptimizado", "KMeans", "KMedoids"));
    private static final Vector<String> INICIALIZADORES_POSIBLES = new Vector<>(List.of("KMeans++", "Aleatorio", "Greedy"));
    private static final Vector<String> EVALUADORES_POSIBLES = new Vector<>(List.of("Silhouette", "CalinskiHarabasz", "DaviesBouldinen"));

    private static final String TEXTO_SELECCIONAR_ENCUESTA = "Por favor, seleccione la encuesta que desea analizar.";
    private static final String TEXTO_SELECCIONAR_RESPUESTA = "Por favor, seleccione las respuestas que desea analizar.";
    private static final String TEXTO_SELECCIONAR_K = "Seleccione el número de clusters deseados";
    private static final String TEXTO_AUTO_SELECCIONAR_K = "Autoseleccionar K";
    private static final String TEXTO_INICIAR_ANALISIS = "Iniciar Análisis";
    private static final String TEXTO_SELECCIONAR_ALGORITMO = "Seleccione algoritmo de clustering";
    private static final String TEXTO_SELECCIONAR_INICIALIZADOR = "Seleccione inicializador de algoritmo";
    private static final String TEXTO_SELECCIONAR_EVALUADOR = "Seleccione evaluador de clustering";
    private static final String TEXTO_ESPERE = "Por favor, espere a que acabe el proceso...";
    private static final String TEXTO_ANALIZANDO = "Analizando...";
    private static final String TEXTO_CARGAR_RESPUESTAS = "Cargar selección de respuestas";

    private static final String MSG_ERROR_NUM_CLUSTERS = "Error: El número de clusters no debería exceder el número de respuestas a la encuesta";
    private static final String MSG_ERROR_NOMBRE = "Error de nombre ";
    private static final String MSG_ERROR_SELECCION = "Error de selección ";
    private static final String MSG_ERROR_INESPERADO = "Error inesperado: ";
    private static final String MSG_ERROR_ANALISIS_INTERRUPCION = "El análisis fue interrumpido";
    private static final String MSG_ERROR_ANALISIS = "Error en el análisis: ";
    private static final String TITULO_ERROR = "Error";
    private static final String TITULO_ADVERTENCIA = "Advertencia";

    private static final String CARD_ENCUESTAS = "encuestas";
    private static final String CARD_RESPUESTAS = "respuestas";

    private static final int NUM_OPCIONES_EN_PANEL = 5;
    private static final int BORDER_THICKNESS = 2;
    private static final int BORDER_PADDING = 20;
    private static final int STRUT = 10;

    private record PanelComboBox<T>(JPanel panel, JComboBox<T> combo) {}

    /**
     * Constructora con parámetros que crea el panel y asigna los presenters
     *
     * @param preAna presenter con operaciones relacionadas con el análisis
     * @param preEn presenter con operaciones relacionadas con las encuestas
     * @param preRes presenter con operaciones relacionadas con las respuestas
     */
    public PanelSeleccionarAnalisis(PresenterAnalisis preAna, PresenterEncuesta preEn, PresenterRespuesta preRes) {
        //inicializar presenters
        presenterAnalisis = preAna;
        presenterEncuesta = preEn;
        presenterRespuesta = preRes;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(STRUT, STRUT, STRUT, STRUT));

        //añadir paneles al principal
        seleccionar = new JPanel(new BorderLayout(STRUT,STRUT));
        seleccionar.add(crearEncuestasPanel(), BorderLayout.CENTER);
        seleccionar.add(crearPanelOpciones(), BorderLayout.EAST);
        add(seleccionar, BorderLayout.CENTER);

        //asignar listeners a paneles
        asignarListeners();
    }

    /**
     * Refresca el panel y reinicia su contenido.
     */
    public void refrescar() {
        //quita todas las componentes
        removeAll();

        //reinicializar panel principal
        seleccionar = new JPanel(new BorderLayout(STRUT,STRUT));
        seleccionar.add(crearEncuestasPanel(), BorderLayout.CENTER);
        seleccionar.add(crearPanelOpciones(), BorderLayout.EAST);

        add(seleccionar, BorderLayout.CENTER);

        asignarListeners();

        revalidate();
        repaint();
    }

    //crea el panel que contiene las encuestas/respuestas a elegir
    private JPanel crearEncuestasPanel() {
        JPanel encuestasPanel = new JPanel();
        //panel con label arriba y el listado de encuestas abajo
        encuestasPanel.setLayout(new BoxLayout(encuestasPanel, BoxLayout.Y_AXIS));

        Color borderColor = SystemColor.windowBorder;

        //crea un borde con color highlight y otro transparente para espaciado
        encuestasPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, BORDER_THICKNESS)
        , BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING))
        );

        incorporarListado(encuestasPanel);

        return encuestasPanel;
    }

    //crea el panel que contiene las diferentes configuraciones para el analizador
    private JPanel crearPanelOpciones() {
        JPanel opcionesPanel = new JPanel();

        // GridLayout:
        opcionesPanel.setLayout(new GridLayout(NUM_OPCIONES_EN_PANEL, 1, 0, STRUT));

        Color borderColor = SystemColor.windowBorder;

        opcionesPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, BORDER_THICKNESS),
                BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING))
        );

        incorporarComboBoxOpciones(opcionesPanel);
        incorporarPanelNumK(opcionesPanel);
        incorporarBotonAnalisis(opcionesPanel);

        return opcionesPanel;
    }

    private void incorporarListado(JPanel encuestasPanel) {
        //uso un cardLayout para cambiar entre el JList de encuestas y el de respuestas
        cardLayout = new CardLayout();
        panelListas = new JPanel(cardLayout);

        panelListas.add(crearListaEncuestasPanel(), CARD_ENCUESTAS);
        panelListas.add(crearListaRespuestasPanel(), CARD_RESPUESTAS);

        encuestasPanel.add(panelListas);
    }

    private void incorporarComboBoxOpciones(JPanel opcionesPanel) {
        //combo box parece lo más natural para elegir entre las opciones
        //PanelComboBox contiene tanto el panel como el comboBox individual
        PanelComboBox<String> panelComboBoxAlgoritmos = crearPanelConComboBox(TEXTO_SELECCIONAR_ALGORITMO, ALGORITMOS_POSIBLES);
        opcionesPanel.add(panelComboBoxAlgoritmos.panel);
        comboBoxOpcionesAlgoritmo = panelComboBoxAlgoritmos.combo;

        PanelComboBox<String> panelComboBoxInicializadores = crearPanelConComboBox(TEXTO_SELECCIONAR_INICIALIZADOR, INICIALIZADORES_POSIBLES);
        opcionesPanel.add(panelComboBoxInicializadores.panel);
        comboBoxOpcionesInicializador = panelComboBoxInicializadores.combo;

        PanelComboBox<String> panelComboBoxEvaluadores = crearPanelConComboBox(TEXTO_SELECCIONAR_EVALUADOR, EVALUADORES_POSIBLES);
        opcionesPanel.add(panelComboBoxEvaluadores.panel);
        comboBoxOpcionesEvaluador = panelComboBoxEvaluadores.combo;
    }

    private void incorporarBotonAnalisis(JPanel opcionesPanel) {
        botonIniciarAnalisis = new JButton(TEXTO_INICIAR_ANALISIS);
        //por default no se debería de poder cliquear
        botonIniciarAnalisis.setEnabled(false);
        opcionesPanel.add(botonIniciarAnalisis);
    }

    //crea un panel con label y spinner para seleccionar K
    private void incorporarPanelNumK(JPanel opcionesPanel) {
        JPanel panelNumK = new JPanel();
        panelNumK.setLayout(new BoxLayout(panelNumK, BoxLayout.Y_AXIS));
        panelNumK.setBorder(BorderFactory.createEmptyBorder(STRUT, 0, STRUT, 0));

        //etiqueta texto
        JLabel labelK = new JLabel(TEXTO_SELECCIONAR_K);
        labelK.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelNumK.add(labelK);
        panelNumK.add(Box.createVerticalStrut(STRUT));

        //spinner para elegir k
        spinnerSeleccionarK = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));
        spinnerSeleccionarK.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComponent editor = spinnerSeleccionarK.getEditor();
        if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
            defaultEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        }

        //botón de autoseleccionar
        botonAutoSeleccionarK = new JButton(TEXTO_AUTO_SELECCIONAR_K);
        botonAutoSeleccionarK.setEnabled(false);
        botonAutoSeleccionarK.setAlignmentX(Component.CENTER_ALIGNMENT);

        //panel horizontal para spinner + botón
        JPanel panelSpinnerBoton = new JPanel(new GridLayout(1, 2, STRUT, 0));
        panelSpinnerBoton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelSpinnerBoton.add(spinnerSeleccionarK);
        panelSpinnerBoton.add(botonAutoSeleccionarK);

        panelNumK.add(panelSpinnerBoton);

        opcionesPanel.add(panelNumK);
    }

    //crea un scroll pane con las todas las encuestas en el sistema
    private JPanel crearListaEncuestasPanel() {
        DefaultListModel<AbstractMap.SimpleEntry<String, String>> model = new DefaultListModel<>();
        try {
            for (AbstractMap.SimpleEntry<String, String> encuesta : presenterEncuesta.getTituloAutorAllEncuestas()) {
                model.addElement(encuesta);
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_INESPERADO + e.getMessage(),
                     TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
        }

        //hacer set modelo y cell renderer
        listaEncuestas = new JList<>(model);
        listaEncuestas.setCellRenderer(new PanelOpcion());
        listaEncuestas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listaEncuestas);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel seleccionarEncuesta = new JLabel(TEXTO_SELECCIONAR_ENCUESTA);
        seleccionarEncuesta.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(seleccionarEncuesta);
        panel.add(Box.createVerticalStrut(STRUT));
        panel.add(scrollPane);

        return panel;
    }

    //crea un scroll pane vacío donde se guardaran las respuestas
    private JScrollPane crearListaRespuestas() {
        DefaultListModel<String> model = new DefaultListModel<>();

        listaRespuestas = new JList<>(model);
        listaRespuestas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        return new JScrollPane(listaRespuestas);
    }

    private JPanel crearListaRespuestasPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JScrollPane lista = crearListaRespuestas();

        JLabel seleccionarRespuesta = new JLabel(TEXTO_SELECCIONAR_RESPUESTA);
        seleccionarRespuesta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(seleccionarRespuesta);

        panel.add(lista);

        botonCargarSeleccion = new JButton(TEXTO_CARGAR_RESPUESTAS);
        botonCargarSeleccion.setEnabled(false);
        botonCargarSeleccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCargarSeleccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, lista.getPreferredSize().height/6));

        panel.add(Box.createVerticalStrut(STRUT));
        panel.add(botonCargarSeleccion);

        return panel;
    }

    //crea un panel con un label "texto" y un combo box debajo con "opciones"
     private <T> PanelComboBox<T> crearPanelConComboBox(String texto, Vector<T> opciones) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(STRUT,0,STRUT,0));

        JLabel label = new JLabel(texto);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(STRUT));

        JComboBox<T> comboBox = new JComboBox<>(opciones);
        panel.add(comboBox);

        return new PanelComboBox<>(panel, comboBox);
    }

    //carga los emails de las respuestas de la encuesta actualmente cargada
    private void cargarRespuestas() {
        DefaultListModel<String> model =
                (DefaultListModel<String>) listaRespuestas.getModel();

        model.clear();

        try {
            presenterRespuesta.importarRespuestasEnMemoria();

            for (String r : presenterRespuesta.getRespuestasEncuestaCargada()) {
                model.addElement(r);
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_INESPERADO + e.getMessage(),
                    TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
        }

        if (!model.isEmpty()) {
            listaRespuestas.setSelectionInterval(0, model.getSize() - 1);
        }
    }

    //leer nombre de función
    private void asignarListeners() {
        asignarListenerListaEncuestas();
        asignarListenerBotonCargarRespuestas();
        asignarListenerBotonAnalisis();
        asignarListenerAutoSeleccionarK();
    }

    //al seleccionar una encuesta, se cargan los emails de las respuestas asociadas y se muestra en el panel
    private void asignarListenerListaEncuestas() {
        listaEncuestas.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            AbstractMap.SimpleEntry<String, String> seleccion = listaEncuestas.getSelectedValue();
            if (seleccion == null) return;

            try {
                presenterEncuesta.importarEncuesta(seleccion.getKey(), seleccion.getValue());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_INESPERADO + ex.getMessage(),
                        TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
                return;
            }

            botonCargarSeleccion.setEnabled(true);
            cargarRespuestas();
            cardLayout.show(panelListas, CARD_RESPUESTAS);
        });
    }

    private void asignarListenerBotonCargarRespuestas() {
        botonCargarSeleccion.addActionListener(e -> {
            List<String> respuestasSeleccionadas = listaRespuestas.getSelectedValuesList();
            if(respuestasSeleccionadas.isEmpty()) return;

            try {
                presenterRespuesta.cargarConjuntoRespuestas(new ArrayList<>(respuestasSeleccionadas));
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_INESPERADO + ex.getMessage(),
                        TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
            }

            botonIniciarAnalisis.setEnabled(!respuestasSeleccionadas.isEmpty());
            botonAutoSeleccionarK.setEnabled(!respuestasSeleccionadas.isEmpty());
        });
    }

    private void asignarListenerBotonAnalisis() {
        botonIniciarAnalisis.addActionListener(e -> {
            int k = (int) spinnerSeleccionarK.getValue();
            int respuestasSeleccionadas = listaRespuestas.getSelectedValuesList().size();

            //validar k
            if (k > respuestasSeleccionadas) {
                JOptionPane.showMessageDialog(seleccionar,
                        MSG_ERROR_NUM_CLUSTERS,
                        TITULO_ADVERTENCIA,JOptionPane.WARNING_MESSAGE);
                return;
            }

            //poner parámetros
            if(!setupAnalisis(k)) {
                return;
            }

            //barra progreso
            JDialog dialogoProgreso = crearDialogoProgreso();

            //análisis en otro thread
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    presenterAnalisis.analizar();
                    return null;
                }

                @Override
                protected void done() {
                    dialogoProgreso.dispose();
                    try {
                        get();
                        mostrarResultados(k);
                    }
                    catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_ANALISIS_INTERRUPCION,
                                TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
                    }
                    catch (ExecutionException ex) {
                        Throwable cause = ex.getCause();
                        JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_ANALISIS + cause.getMessage(),
                                TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();
            dialogoProgreso.setVisible(true);
        });
    }

    private void setupAnalisis() {
        presenterAnalisis.setEvaluador((String) comboBoxOpcionesEvaluador.getSelectedItem());
        presenterAnalisis.setAlgoritmoInicializador(
                (String) comboBoxOpcionesAlgoritmo.getSelectedItem(),
                (String) comboBoxOpcionesInicializador.getSelectedItem());
    }

    private boolean setupAnalisis(int k) {
        boolean correcto = true;
        try {
            presenterAnalisis.setK(k);
            setupAnalisis();
        }
        catch (AlgoritmoNoReconocido | EvaluadorNoReconocido | InicializadorNoReconocido e) {
            JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_NOMBRE + e.getMessage(),
                    TITULO_ADVERTENCIA,JOptionPane.WARNING_MESSAGE);
            correcto = false;
        }
        catch (InicializadorYAlgoritmoIncompatibles e) {
            JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_SELECCION + e.getMessage(),
                    TITULO_ADVERTENCIA,JOptionPane.WARNING_MESSAGE);
            correcto = false;
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_INESPERADO + e.getMessage(),
                    TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
            correcto = false;
        }
        return correcto;
    }

    private void mostrarResultados(int k) {
        remove(seleccionar);

        try {
            panelResultadoAnalisis = new PanelResultadoAnalisis(
                    presenterAnalisis.getDistanciasClusters(),
                    presenterAnalisis.getDistancias(),
                    presenterAnalisis.getAutoresClusters(),
                    k,
                    presenterAnalisis.getEval()
            );
        }
        //no debería haber excepciones en estas operaciones porque son consultas
        catch (Exception e) {
            JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_INESPERADO + e.getMessage(),
                    TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
            return;
        }

        add(panelResultadoAnalisis);
        revalidate();
        repaint();
    }

    private void asignarListenerAutoSeleccionarK() {
        botonAutoSeleccionarK.addActionListener(l -> {
            JDialog dialogoProgreso = crearDialogoProgreso();

            try {
                setupAnalisis();
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_INESPERADO + e.getMessage(),
                        TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
                return;
            }

            SwingWorker<Integer, Void> worker = new SwingWorker<>() {
                @Override
                protected Integer doInBackground() {
                    return presenterAnalisis.calcularK();
                }

                @Override
                protected void done() {
                    dialogoProgreso.dispose();
                    try {
                        int k = get();
                        spinnerSeleccionarK.setValue(k);
                    }
                    catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_ANALISIS_INTERRUPCION,
                                TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
                    }
                    catch (ExecutionException ex) {
                        Throwable cause = ex.getCause();
                        JOptionPane.showMessageDialog(seleccionar, MSG_ERROR_ANALISIS + cause.getMessage(),
                                TITULO_ERROR,JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
            dialogoProgreso.setVisible(true);
        });
    }

    private JDialog crearDialogoProgreso() {
        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                TEXTO_ANALIZANDO,
                Dialog.ModalityType.APPLICATION_MODAL
        );

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);

        dialog.add(new JLabel(TEXTO_ESPERE), BorderLayout.NORTH);
        dialog.add(bar, BorderLayout.CENTER);

        dialog.setSize(seleccionar.getPreferredSize().width/2, seleccionar.getPreferredSize().height/4);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        return dialog;
    }
}