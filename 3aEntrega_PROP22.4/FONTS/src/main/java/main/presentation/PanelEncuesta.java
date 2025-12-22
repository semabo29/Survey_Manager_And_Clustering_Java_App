package main.presentation;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import main.presentation.utils.PanelOpcion;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

/**
 * Panel principal para la gestión de encuestas.
 * Permite crear, cargar, modificar, ver y borrar encuestas.
 * @author Sergi Malaguilla Bombin
 */
public class PanelEncuesta extends JPanel {

    private final PresenterEncuesta presenter;
    private final PresenterPerfil presenterp;

    //titulo
    private JLabel labelTitulo;

    //panel con el contenido central que ira variando segun la opcion
    private JPanel panelContenidoCentral;

    //botones
    private JButton btnCrear;
    private JButton btnCargar;
    private JButton btnModificar;
    private JButton btnVer;
    private JButton btnBorrar;

    //campo de texto
    private JTextField txtNombreArchivo;

    //lista de encuestas (se mantiene aquí porque los botones de menú necesitan saber la selección actual)
    private JList<SimpleEntry<String, String>> listaEncuestas;
    private JScrollPane scrollListaEncuestas;

    private static final String textBotonCrear = "Crear";
    private static final String textBotonCargar = "Cargar";
    private static final String textBotonModificar = "Modificar";
    private static final String textBotonVer = "Ver";
    private static final String textBotonBorrar = "Borrar";

    private static final String textTituloPrincipal = "Gestion de encuestas";
    private static final String textSeleccioneEncuesta = "Seleccione una encuesta";
    private static final String textNoEncuestas = "No hay encuestas disponibles.";
    private static final String textAdvertenciaSeleccionModificar = "Selecciona una encuesta para modificar.";
    private static final String textAdvertenciaSeleccionVer = "Selecciona una encuesta para ver.";
    private static final String textAdvertenciaSeleccionBorrar = "Selecciona una encuesta para borrar.";
    private static final String textAdvertenciaSinPreguntas = "La encuesta no contiene preguntas.";
    private static final String textConfirmarBorrado = "¿Está seguro que desea borrar la encuesta '%s' de %s?";
    private static final String textExitoBorrado = "Encuesta borrada correctamente.";

    private static final String textTituloConfirmarBorrado = "Confirmar borrado";
    private static final String textErrorCargarModificar = "Error al cargar la encuesta para modificar: ";
    private static final String textErrorCargarVer = "Error al cargar la encuesta: ";
    private static final String textErrorBorrar = "Error al borrar la encuesta: ";
    private static final String textAdvertencia = "Advertencia";
    private static final String textError = "Error";
    private static final String textExito = "Éxito";

    /**
     * Constructor del panel de gestión de encuestas.
     * inicializa los componentes y muestra el menú principal.
     * Por defecto muestra el panel de carga de encuestas.
     * @param presenter Presenter para la lógica de encuestas.
     * @param presenterp Presenter para la lógica de perfil.
     */
    public PanelEncuesta(PresenterEncuesta presenter, PresenterPerfil presenterp) {
        this.presenterp = presenterp;
        this.presenter = presenter;

        //config del panel
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        inicializarComponentes();
        asignarListeners();

        construirPanelMenuEncuesta();

        mostrarPanelCargar();
    }


    //INICIALIZACION
    private void inicializarComponentes() {
        labelTitulo = new JLabel(textTituloPrincipal);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        btnCrear = new JButton(textBotonCrear);
        btnCargar = new JButton(textBotonCargar);
        //por defecto deshabilitados
        btnModificar = new JButton(textBotonModificar);
        btnModificar.setEnabled(false);
        btnVer = new JButton(textBotonVer);
        btnVer.setEnabled(false);
        btnBorrar = new JButton(textBotonBorrar);
        btnBorrar.setEnabled(false);

        //para el path del archivo
        txtNombreArchivo = new JTextField(20);
        txtNombreArchivo.setText(textSeleccioneEncuesta);

        listaEncuestas = new JList<>();

        //seleccion de un solo elemento
        listaEncuestas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEncuestas.setCellRenderer(new PanelOpcion());
        //desplazable
        scrollListaEncuestas = new JScrollPane(listaEncuestas);
        //panel central
        panelContenidoCentral = new JPanel(new BorderLayout());

        cargarListaEncuestas();
    }

    private void construirPanelMenuEncuesta() {
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));

        //panel del titulo
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.add(labelTitulo);
        panelNorte.add(panelTitulo);

        //espaciador
        panelNorte.add(Box.createRigidArea(new Dimension(0, 10)));

        //panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.add(btnCrear);
        panelBotones.add(btnCargar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnVer);
        panelBotones.add(btnBorrar);

        panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelNorte.add(panelBotones);

        //add del panel norte al panel principal
        add(panelNorte, BorderLayout.NORTH);

        //add del panel central al panel principal (inicialmente vacio)
        add(panelContenidoCentral, BorderLayout.CENTER);
    }



    /**
     * Muestra el panel para cargar encuestas.
     * */
    public void mostrarPanelCargar() {
        panelContenidoCentral.removeAll();

        PanelCargarEncuesta panelCargar = new PanelCargarEncuesta(txtNombreArchivo, listaEncuestas, scrollListaEncuestas, btnModificar, btnVer, btnBorrar );

        panelContenidoCentral.add(panelCargar, BorderLayout.CENTER);

        panelContenidoCentral.revalidate();
        panelContenidoCentral.repaint();
    }


    /**
     * Muestra el panel para modificar una encuesta.
     * */
    public void mostrarPanelModificar(SimpleEntry<String,String> nombreEncuesta) {
        panelContenidoCentral.removeAll();

        PanelModificarEncuesta panelModificar;
        try {
            presenter.importarEncuesta(nombreEncuesta.getKey(), nombreEncuesta.getValue());
            panelModificar = new PanelModificarEncuesta(this, presenter);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textErrorCargarModificar + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
            mostrarPanelCargar();
            return;
        }

        panelContenidoCentral.add(panelModificar, BorderLayout.CENTER);
        panelContenidoCentral.revalidate();
        panelContenidoCentral.repaint();
    }


    /**
     * Muestra el panel para ver una encuesta.
     * */
    public void mostrarPanelVer(SimpleEntry<String,String> nombreEncuesta) {
        panelContenidoCentral.removeAll();

        PanelVerEncuesta panelVer;

        try {
            presenter.importarEncuesta(nombreEncuesta.getKey(), nombreEncuesta.getValue());
            panelVer = new PanelVerEncuesta(presenter);

            if (!panelVer.tienePreguntas()) {
                JOptionPane.showMessageDialog(this, textAdvertenciaSinPreguntas, textAdvertencia, JOptionPane.WARNING_MESSAGE);
                mostrarPanelCargar();
                return;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, textErrorCargarVer + e.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
            mostrarPanelCargar();
            return;
        }

        panelContenidoCentral.add(panelVer, BorderLayout.CENTER);
        panelContenidoCentral.revalidate();
        panelContenidoCentral.repaint();
    }



    /**
     * Muestra el panel para crear una nueva encuesta.
     * */
    public void mostrarPanelCrear() {
        panelContenidoCentral.removeAll();

        PanelCrearEncuesta panelCreacion = new PanelCrearEncuesta(this, presenter, presenterp);
        JPanel p = new JPanel(new GridBagLayout()); //GridBagLayout para centrar el contenido
        p.add(panelCreacion);

        panelContenidoCentral.add(p, BorderLayout.CENTER);

        panelContenidoCentral.revalidate();
        panelContenidoCentral.repaint();
    }

    /**
     * Carga la lista de encuestas disponibles en la JList.
     * */
    public void cargarListaEncuestas() {

        ArrayList<SimpleEntry<String, String>> listaTitulosAutores = presenter.getTituloAutorAllEncuestas();
        SimpleEntry<String, String>[] encuestas;

        if (listaTitulosAutores == null || listaTitulosAutores.isEmpty()) {
            SimpleEntry<String, String> mensaje = new SimpleEntry<>(textNoEncuestas, " ");
            encuestas = new SimpleEntry[]{mensaje};
            btnModificar.setEnabled(false);
            btnVer.setEnabled(false);
            btnBorrar.setEnabled(false);
            listaEncuestas.setEnabled(false);
            listaEncuestas.clearSelection();
        } else {
            encuestas = listaTitulosAutores.toArray(new SimpleEntry[0]);
            btnModificar.setEnabled(false);
            btnVer.setEnabled(false);
            btnBorrar.setEnabled(false);
            listaEncuestas.setEnabled(true);
        }

        listaEncuestas.setListData(encuestas);
    }

    /**
     * Refresca la lista de encuestas y muestra el panel de carga.
     * */
    public void refrescar() {
        cargarListaEncuestas();
        mostrarPanelCargar();
        revalidate();
        repaint();
    }


    //LISTENERS
    private void asignarListeners() {
        btnCrear.addActionListener(e -> {
            mostrarPanelCrear();
        });
        btnCargar.addActionListener(e -> {
            mostrarPanelCargar();
        });
        btnModificar.addActionListener(e -> {
            SimpleEntry<String,String> seleccion = listaEncuestas.getSelectedValue();
            if (seleccion != null) {
                mostrarPanelModificar(seleccion);
            }
            else {
                JOptionPane.showMessageDialog(this, textAdvertenciaSeleccionModificar, textAdvertencia, JOptionPane.WARNING_MESSAGE);
            }
        });
        btnVer.addActionListener(e -> {
            SimpleEntry<String,String> seleccion = listaEncuestas.getSelectedValue();
            if (seleccion != null) {
                mostrarPanelVer(seleccion);
            } else {
                JOptionPane.showMessageDialog(this, textAdvertenciaSeleccionVer, textAdvertencia, JOptionPane.WARNING_MESSAGE);
            }
        });
        btnBorrar.addActionListener(e -> {
            SimpleEntry<String,String> seleccion = listaEncuestas.getSelectedValue();
            if (seleccion != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        String.format(textConfirmarBorrado, seleccion.getKey(), seleccion.getValue()),
                        textTituloConfirmarBorrado,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        presenter.importarEncuesta(seleccion.getKey(), seleccion.getValue());
                        presenter.borrarEncuestaActual();
                        txtNombreArchivo.setText(textSeleccioneEncuesta);
                        refrescar();
                        JOptionPane.showMessageDialog(this, textExitoBorrado, textExito, JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, textErrorBorrar + ex.getMessage(), textError, JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, textAdvertenciaSeleccionBorrar, textAdvertencia, JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}