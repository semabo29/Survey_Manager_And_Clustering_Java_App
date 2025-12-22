package main.presentation;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Vector;

/**
 * Vista principal de la presentación.
 * Esta clase contiene los menús principales de la aplicación:
 * {@link main.presentation.PanelPerfil}, {@link main.presentation.PanelGestionPerfil},
 * {@link main.presentation.PanelEncuesta}, {@link main.presentation.PanelRespuesta}
 * y {@link main.presentation.PanelSeleccionarAnalisis}.
 * La vista principal tiene una {@link JMenuBar} con dos apartados: Files y Help. Files permite importar ficheros
 * externos con las extensiones de nuestra aplicacion, y Help muestra diálogos de ayuda para cada menú principal.
 * Para navegar por los menús, mostramos un panel lateral escondible con botones para cambiar a cada menú.
 * @author Yimin Jin
 */
public class VistaPrincipal {
    // Strings
    private static final String textFrame = "Enquestador22.4";
    private static final String textFiles = "File";
    private static final String textCargarEncuesta = "Importar Encuesta";
    private static final String textCargarRespuesta = "Importar Respuesta";
    private static final String textCargarPerfil = "Importar Perfil";
    private static final String textHelpMenu = "Help";
    private static final String textHelpEncuesta = "Encuesta";
    private static final String textHelpRespuesta = "Respuesta";
    private static final String textHelpPerfil = "Perfil";
    private static final String textHelpAnalisis = "Analisis";
    private static final String textSidePanel = "MenuLateral";
    private static final String textGestionPerfil = "Gestión Perfil";
    private static final String textCambiarPerfil = "Cambiar Perfil";
    private static final String textEncuesta = "Encuesta";
    private static final String textRespuesta = "Respuesta";
    private static final String textAnalizar = "Analizar";

    private static final String textEncuestaFilter = "Archivos encuesta";
    private static final String textRespuestaFilter = "Archivos respuesta";
    private static final String textPerfilFilter = "Archivos perfil";

    private static final String textError = "Error";
    private static final String textExcepcionPerfil = "Excepción inesperada, no se ha podido importar el perfil. ";
    private static final String textExcepcionEncuesta = "Excepción inesperada, no se ha podido importar la encuesta. ";
    private static final String textExcepcionRespuesta = "Excepción inesperada, no se ha podido importar la respuesta. ";

    private static final String textExito = "Éxito";
    private static final String textPerfilImportado = "El perfil se ha importado exitosamente";
    private static final String textEncuestaImportada = "La encuesta se ha importado exitosamente";
    private static final String textRespuestaImportada = "La respuesta se ha importado exitosamente";

    private static final String helpApartado1 = "Ayuda Encuesta";
    private static final String helpApartado2 = "Ayuda Respuesta";
    private static final String helpApartado3 = "Ayuda Analisis";
    private static final String helpApartado4 = "Ayuda Perfil";

    private static final Vector<String> helpApartados = new Vector<>(List.of(helpApartado1, helpApartado2, helpApartado3, helpApartado4));

    private static final String textoAyudaApartado1 = "<html><body>" +
            "<h2 align='center'><b>Guía Menú Encuesta</b></h2>" +
            "<ul>" + "<li><b>Crear:</b><br>Puedes crear una encuesta nueva, introduciendo el título de la encuesta. " +
            "Automáticamente pasarás al menú de modificar la encuesta.</li><br>" +
            "<li><b>Cargar:</b><br>Puedes seleccionar una encuesta de la lista de encuestas. Después de seleccionar, puedes escoger " +
            "modificar, ver o borrar la encuesta.</li><br>" +
            "<li><b>Modificar:</b><br><p>Puedes modificar la encuesta seleccionada. Dentro del menú de modificación, puedes navegar " +
            "por las preguntas. Puedes modificar o borrar la pregunta que estás viendo, o añadir una pregunta nueva.</p><p>" +
            "Al modificar o añadir una pregunta nueva, puedes editar los atributos de la pregunta. La descripción es lo único opcional." +
            "</p></li><br>" + "<li><b>Ver:</b><br>Muestra las preguntas de la encuesta seleccionada.</li><br>" +
            "<li><b>Borrar:</b><br>Borra la encuesta seleccionada y las respuestas que tiene asociadas.</li><br>" +
            "</ul>" +
            "</body></html>";
    private static final String textoAyudaApartado2 = "<html><body>" +
            "<h2 align='center'><b>Guía Menú Respuesta</b></h2>" +
            "<ul>" + "<li><b>Ver respuestas:</b><br>Muestra una lista de las respuestas del usuario que ha iniciado " +
            "sesión. Puedes seleccionar una respuesta para verla, modificarla o borrarla.</li><br>" +
            "<li><b>Ver encuestas:</b><br>Muestra una lista de las encuestas que hay disponibles en el sistema. " +
            "Puedes seleccionar una para responderla.</li><br>" +
            "<li><b>Ver:</b><br>Muestra la respuesta seleccionada. Puedes navegar por las preguntas respondidas.</li><br>" +
            "<li><b>Modificar / Responder:</b><br>Muestra las preguntas de una encuesta, de manera que puedes responder. " +
            "Si estás modificando una respuesta, por defecto se muestra la respuesta a cada pregunta. Al llegar a la última " +
            "pregunta, se puede guardar la respuesta.</li><br>" +
            "<li><b>Borrar:</b><br>Borra la respuesta seleccionada.</li><br>" +
            "</ul>" +
            "</body></html>";
    private static final String textoAyudaApartado3 = "<html><body>" +
            "<h2 align='center'><b>Guía Menú Análisis</b></h2>" +
            "<p>Al comenzar, se muestra la lista de encuestas disponibles en el sistema. " +
            "Cuando se selecciona una encuesta, se muestra la lista de respuestas a esta encuesta.</p>" +
            "<p>Por defecto, todas las respuestas están seleccionadas. Para deseleccionar una respuesta, hay que " +
            "usar <b>Ctrl+Click izquierdo</b>, y para seleccionar un conjunto de respuestas seguidas hay seleccionar una respuesta " +
            "y usar <b>Shift+Click izquierdo</b> en la última respuesta que quieras incluir.</p>" +
            "<p><b>Recuerda darle al botón de cargar respuestas cuando finalices la selección.</b></p>" +
            "<p>A la derecha, hay listas desplegables para escoger los algoritmos a utilizar en el análisis. " +
            "Las combinaciones válidas de algoritmos son: <b>KMeans/KMeansOptimizado + Aleatorio/KMeans++</b> o " +
            "<b>KMedoids + Greedy</b>. No hay limitaciones respecto a los evaluadores que se pueden escoger. " +
            "También puedes seleccionar la K, que es el número de grupos que quieres utilizar para el análisis.</p>" +
            "<p>El botón de cálculo automático del número de agrupaciones no está activado hasta que se hayan cargado respuestas. " +
            "Esta opción intenta calcular un valor que sea óptimo para el análisis.</p>" +
            "<p>El botón de analizar comienza el análisis para el conjunto de respuestas seleccionado y cargado. " +
            "Este proceso puede tardar un rato si hay muchas respuestas y grupos. Al finalizar, se mostrarán las agrupaciones " +
            "en formato gráfico en dos dimensiones o en una tabla indicando a qué grupo pertenece cada respuesta.</p>" +
            "</body></html>";
    private static final String textoAyudaApartado4 = "<html><body>" +
            "<h2 align='center'><b>Guía Menús Perfil</b></h2>" +
            "<p>El menú Cambiar Perfil te devuelve a la pantalla inicial de la aplicación, donde puedes iniciar sesión " +
            "con un perfil ya creado o puedes registrarte creando un perfil nuevo.</p>" +
            "<p>El menú Gestión Perfil permite ver el email y nombre del perfil cargado, y también permite borrar el perfil.</p>" +
            "</body></html>";

    private static final Vector<String> textoAyudaApartados = new Vector<>(List.of(textoAyudaApartado1, textoAyudaApartado2, textoAyudaApartado3, textoAyudaApartado4));

    private static final String textEntendido = "Entendido";

    // Presentadores
    private PresenterPerfil prePerfil;
    private PresenterEncuesta preEncuesta;
    private PresenterAnalisis preAnalisis;
    private PresenterRespuesta preRespuesta;

    // Menus principales
    private PanelPerfil panelPerfil;
    private PanelGestionPerfil panelGestionPerfil;
    private PanelEncuesta panelEncuesta;
    private PanelRespuesta panelRespuesta;
    private PanelSeleccionarAnalisis panelAnalisis;

    private JFrame frameVista = new JFrame(textFrame);

    // Panel principal
    private JPanel panelPrincipal = new JPanel();
    private JPanel panelMenuSeleccionado = new JPanel();

    // Barra Menu
    private JMenuBar menuBar = new JMenuBar();
    private JMenu filesMenu = new JMenu(textFiles);
    private JMenuItem cargarEncuesta = new JMenuItem(textCargarEncuesta);
    private JMenuItem cargarRespuesta = new JMenuItem(textCargarRespuesta);
    private JMenuItem cargarPerfil = new JMenuItem(textCargarPerfil);
    private JMenu helpMenu = new JMenu(textHelpMenu);
    private JMenuItem helpEncuesta = new JMenuItem(textHelpEncuesta);
    private JMenuItem helpRespuesta = new JMenuItem(textHelpRespuesta);
    private JMenuItem helpAnalisis = new JMenuItem(textHelpAnalisis);
    private JMenuItem helpPerfil = new JMenuItem(textHelpPerfil);
    private JButton sidePanelButton = new JButton(textSidePanel);

    // Panel lateral
    private JPanel sidePanel = new JPanel();
    private JButton gestionPerfilButton = new JButton(textGestionPerfil);
    private JButton perfilButton = new JButton(textCambiarPerfil);
    private JButton encuestaButton = new JButton(textEncuesta);
    private JButton respuestaButton = new JButton(textRespuesta);
    private JButton analizarButton = new JButton(textAnalizar);

    /**
     * Constructor de la clase.
     * @param prePerfil Presentador para las operaciones de perfil.
     * @param preRespuesta Presentador para las operaciones de respuesta.
     * @param preAnalisis Presentador para las operaciones de análisis.
     * @param preEncuesta Presentador para las operaciones de encuesta.
     */
    public VistaPrincipal(PresenterPerfil prePerfil, PresenterRespuesta preRespuesta, PresenterAnalisis preAnalisis, PresenterEncuesta preEncuesta) {
        this.prePerfil = prePerfil;
        this.preEncuesta = preEncuesta;
        this.preRespuesta = preRespuesta;
        this.preAnalisis = preAnalisis;

        panelPerfil = new PanelPerfil(prePerfil);

        inicializarFrameVista();

        setPanelPerfil();
    }

    /**
     * Hace visible la vista principal. Es necesario llamarlo para mostrar la aplicación.
     */
    public void hacerVisible() {
        System.out.println
                ("isEventDispatchThread: " + SwingUtilities.isEventDispatchThread());
        frameVista.pack();
        frameVista.setVisible(true);
    }

    // ================================================================================================================
    // Inicializaciones

    private void inicializarComponentes() {
        panelGestionPerfil = new PanelGestionPerfil(prePerfil);
        panelEncuesta = new PanelEncuesta(preEncuesta, prePerfil);
        panelRespuesta = new PanelRespuesta(preRespuesta, preEncuesta);
        panelAnalisis = new PanelSeleccionarAnalisis(preAnalisis, preEncuesta, preRespuesta);
        inicializarBarraMenu();
        inicializarSidePanel();
        inicializarPanelPrincipal();
        asignarListeners();
    }

    private void inicializarFrameVista() {
        // Tamaño
        frameVista.setMinimumSize(new Dimension(1400,850));
        frameVista.setPreferredSize(frameVista.getMinimumSize());
        frameVista.setResizable(true);

        // Posicion y operaciones por defecto
        frameVista.setLocationRelativeTo(null);
        frameVista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel de contenidos
        JPanel contentPane = (JPanel) frameVista.getContentPane();
        contentPane.add(new JPanel());
    }

    private void inicializarBarraMenu() {
        menuBar.add(sidePanelButton);
        menuBar.add(filesMenu);
        menuBar.add(helpMenu);

        filesMenu.add(cargarEncuesta);
        filesMenu.add(cargarRespuesta);
        filesMenu.add(cargarPerfil);

        helpMenu.add(helpEncuesta);
        helpMenu.add(helpRespuesta);
        helpMenu.add(helpAnalisis);
        helpMenu.add(helpPerfil);

    }

    private void inicializarSidePanel() {
        sidePanel.setLayout(new GridLayout(0,1));
        sidePanel.add(perfilButton);
        sidePanel.add(gestionPerfilButton);
        sidePanel.add(encuestaButton);
        sidePanel.add(respuestaButton);
        sidePanel.add(analizarButton);
    }

    private void inicializarPanelPrincipal() {
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.add(sidePanel, BorderLayout.WEST);
        panelPrincipal.add(panelMenuSeleccionado, BorderLayout.CENTER);
    }

    // ================================================================================================================
    // Setters de panel y menus

    // Permite mostrar un panel en toda la ventana

    /**
     * Muestra el panel de inicio de sesión.
     */
    public void setPanelPerfil() {
        panelPrincipal.remove(panelMenuSeleccionado);
        panelMenuSeleccionado = new JPanel();
        frameVista.setContentPane(panelPerfil);
        frameVista.setJMenuBar(null);
        frameVista.revalidate();
    }

    // Muestra el panel principal

    /**
     * Muestra el menú principal. Es necesario llamarlo después de iniciar sesión, teniendo un perfil cargado.
     */
    public void mostrarMenuPrincipal() {
        inicializarComponentes();
        frameVista.setContentPane(panelPrincipal);
        inicializarBarraMenu();
        frameVista.setJMenuBar(menuBar);
        frameVista.revalidate();
    }

    // Muestra un panel seleccionado en el centro del panel principal
    private void setPanelMenuSeleccionado(JPanel panelMenuSeleccionado) {
        panelPrincipal.remove(this.panelMenuSeleccionado);
        this.panelMenuSeleccionado = panelMenuSeleccionado;
        panelPrincipal.add(this.panelMenuSeleccionado, BorderLayout.CENTER);
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    // ================================================================================================================
    // Listeners

    private void asignarListeners() {
        sidePanelButton.addActionListener(
                e -> actionToggleSidePanel()
        );
        perfilButton.addActionListener(
                e -> actionMostrarMenuPerfil()
        );
        gestionPerfilButton.addActionListener(
                e -> actionMostrarMenuGestionPerfil()
        );
        encuestaButton.addActionListener(
                e -> actionMostrarMenuEncuesta()
        );
        respuestaButton.addActionListener(
                e -> actionMostrarMenuRespuesta()
        );
        analizarButton.addActionListener(
                e -> actionMostrarMenuAnalisis()
        );
        cargarEncuesta.addActionListener(
                e -> actionCargarEncuesta()
        );
        cargarRespuesta.addActionListener(
                e -> actionCargarRespuesta()
        );
        cargarPerfil.addActionListener(
                e -> actionCargarPerfil()
        );

        helpEncuesta.addActionListener(
                e -> actionHelp(0)
        );
        helpRespuesta.addActionListener(
                e -> actionHelp(1)
        );
        helpAnalisis.addActionListener(
                e -> actionHelp(2)
        );
        helpPerfil.addActionListener(
                e -> actionHelp(3)
        );
    }

    // ================================================================================================================
    // Actions

    private void actionToggleSidePanel() {
        sidePanel.setVisible(!sidePanel.isVisible());
    }

    private void actionMostrarMenuPerfil() {
        setPanelPerfil();
    }

    private void actionMostrarMenuGestionPerfil() {
        panelGestionPerfil.refrescar();
        setPanelMenuSeleccionado(panelGestionPerfil);
    }

    private void actionMostrarMenuEncuesta() {
        panelEncuesta.refrescar();
        setPanelMenuSeleccionado(panelEncuesta);
    }

    private void actionMostrarMenuRespuesta() {
        panelRespuesta.refrescar();
        setPanelMenuSeleccionado(panelRespuesta);
    }

    private void actionMostrarMenuAnalisis() {
        panelAnalisis.refrescar();
        setPanelMenuSeleccionado(panelAnalisis);
    }

    private void actionCargarEncuesta() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(textEncuestaFilter, "enc");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String archivoSeleccionado = chooser.getSelectedFile().getAbsolutePath();
            try {
                preEncuesta.importarEncuestaPorPath(archivoSeleccionado);
                JOptionPane.showMessageDialog(this.frameVista, textEncuestaImportada, textExito, JOptionPane.INFORMATION_MESSAGE);
                panelEncuesta.refrescar();
                panelRespuesta.refrescar();
                panelAnalisis.refrescar();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this.frameVista, textExcepcionEncuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actionCargarRespuesta() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(textRespuestaFilter, "res");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String archivoSeleccionado = chooser.getSelectedFile().getAbsolutePath();
            try {
                preRespuesta.importarRespuestaPorPath(archivoSeleccionado);
                JOptionPane.showMessageDialog(this.frameVista, textRespuestaImportada, textExito, JOptionPane.INFORMATION_MESSAGE);
                panelRespuesta.refrescar();
                panelAnalisis.refrescar();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this.frameVista, textExcepcionRespuesta + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actionCargarPerfil() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(textPerfilFilter, "perf");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String archivoSeleccionado = chooser.getSelectedFile().getAbsolutePath();
            try {
                prePerfil.importarPerfilPorPath(archivoSeleccionado);
                JOptionPane.showMessageDialog(this.frameVista, textPerfilImportado, textExito, JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this.frameVista, textExcepcionPerfil + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actionHelp(int apartado) {
        JDialog dialogo = new JDialog(this.frameVista, helpApartados.get(apartado), false);
        dialogo.setLayout(new BorderLayout(10, 10));
        JEditorPane textoAyuda = new JEditorPane("text/html", textoAyudaApartados.get(apartado));
        textoAyuda.setEditable(false);
        textoAyuda.setMargin(new Insets(10, 10, 10, 10));
        textoAyuda.getCaret().setVisible(false);
        textoAyuda.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(textoAyuda);
        dialogo.add(scroll, BorderLayout.CENTER);

        // Boton cerrar
        JButton btnCerrar = new JButton(textEntendido);
        btnCerrar.addActionListener(ae -> dialogo.dispose());

        // Panel para centrar el botón y darle margen
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        dialogo.add(panelBoton, BorderLayout.SOUTH);

        dialogo.setMinimumSize(new Dimension(300, 300));
        dialogo.setSize(500, 500);
        dialogo.setLocationRelativeTo(this.frameVista); // Centrar respecto a la ventana principal
        dialogo.setVisible(true);
    }

    // ================================================================================================================

}