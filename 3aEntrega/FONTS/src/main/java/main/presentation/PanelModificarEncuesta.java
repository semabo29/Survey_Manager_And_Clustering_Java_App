package main.presentation;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import main.presentation.utils.PanelPregunta;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Panel para modificar una encuesta existente.
 * @author Sergi Malaguilla Bombin
 */
public class PanelModificarEncuesta extends JPanel {

    private final PanelEncuesta contenedor;
    private final PresenterEncuesta presenter;

    // Modificar
    private int preguntaActualIndex;
    private ArrayList<HashMap<String, String>> listaPreguntasInfo;
    private JPanel panelPreguntaContenedor;
    private final JButton btnSiguientePregunta;
    private final JButton btnAnteriorPregunta;

    private final JButton btnAddPregunta;
    private final JButton btnModificarPregunta;
    private final JButton btnEliminarPregunta;
    private final JButton btnGuardarEncuesta;
    private final JButton btnCancelarModificar;

    private static final String textAnterior = "<< Anterior";
    private static final String textSiguiente = "Siguiente >>";
    private static final String textAddPregunta = "Añadir Pregunta";
    private static final String textModificarPregunta = "Modificar Pregunta";
    private static final String textEliminarPregunta = "Eliminar Pregunta";
    private static final String textGuardarCambios = "Guardar Cambios";
    private static final String textVolverLista = "Volver a la Lista";

    private static final String textModificandoEncuesta = "Modificando Encuesta: %s";
    private static final String textNoPreguntas = "Aún no hay preguntas en esta encuesta.";
    private static final String textContadorPreguntas = "Pregunta %d de %d";
    private static final String textTituloAdd = "Añadir Nueva Pregunta";
    private static final String textTituloMod = "Modificar Pregunta %d";
    private static final String textConfirmarDescarte = "¿Desea descartar los cambios no guardados?";
    private static final String textTituloDescarte = "Confirmar Descarte";
    private static final String textConfirmarEliminar = "¿Está seguro que desea eliminar la pregunta %d:\n\"%s\"";
    private static final String textTituloEliminar = "Confirmar Eliminación";

    private static final String textExitoGuardar = "Cambios guardados correctamente.";
    private static final String textExitoAdd = "Pregunta añadida (recuerda Guardar Cambios).";
    private static final String textExitoMod = "Pregunta modificada (recuerda Guardar Cambios).";
    private static final String textExitoEliminar = "Pregunta eliminada correctamente (recuerda Guardar Cambios).";
    private static final String textErrorValidacion = "Texto no puede estar vacío.";
    private static final String textErrorNum = "El Mínimo, Máximo y Máximo seleccionable deben ser números válidos.";
    private static final String textErrorLogica = "Error de lógica: %s";
    private static final String textErrorGuardar = "Error al guardar la encuesta: %s";
    private static final String textErrorRecargar = "Error al recargar la encuesta: %s";
    private static final String textErrorEliminar = "Error al eliminar la pregunta: %s";

    private static final String textLabelId = "ID:";
    private static final String textLabelTexto = "Texto:";
    private static final String textLabelObligatorio = "Obligatoria:";
    private static final String textLabelTipo = "Tipo:";
    private static final String textLabelDescripcion = "Descripción:";
    private static final String textLabelMaxSelect = "Máximo seleccionable:";
    private static final String textLabelOpciones = "Opciones (una por línea):";
    private static final String textLabelMin = "Mínimo:";
    private static final String textLabelMax = "Máximo:";

    private static final String textExito = "Éxito";
    private static final String textError = "Error";
    private static final String textErrorValidacionTitle = "Error de Validación";
    private static final String textErrorFormatoTitle = "Error de Formato";

    /**
     * Constructor del panel para modificar una encuesta existente.
     * @param contenedor Panel contenedor de encuestas.
     * @param presenter Presenter para la lógica de encuestas.
     */
    public PanelModificarEncuesta(PanelEncuesta contenedor, PresenterEncuesta presenter) {
        this.contenedor = contenedor;
        this.presenter = presenter;

        this.listaPreguntasInfo = presenter.getPreguntasEncuestaCargada();
        this.preguntaActualIndex = 0;

        this.btnAnteriorPregunta = new JButton(textAnterior);
        this.btnSiguientePregunta = new JButton(textSiguiente);
        this.btnAddPregunta = new JButton(textAddPregunta);
        this.btnModificarPregunta = new JButton(textModificarPregunta);
        this.btnEliminarPregunta = new JButton(textEliminarPregunta);
        this.btnGuardarEncuesta = new JButton(textGuardarCambios);
        this.btnCancelarModificar = new JButton(textVolverLista);

        setLayout(new BorderLayout());
        construirPanelModificarEncuesta();
        actualizarVistaPreguntaModificar();
    }

    private void construirPanelModificarEncuesta() {
        JLabel titulo = new JLabel(String.format(textModificandoEncuesta, presenter.getTituloEncuestaCargada()));
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        panelPreguntaContenedor = new JPanel(new BorderLayout());
        add(panelPreguntaContenedor, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());

        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNavegacion.add(btnAnteriorPregunta);
        panelNavegacion.add(btnSiguientePregunta);
        asignarListenersNavegacion();

        JPanel panelAccionesPregunta = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelAccionesPregunta.add(btnAddPregunta);
        panelAccionesPregunta.add(btnModificarPregunta);
        panelAccionesPregunta.add(btnEliminarPregunta);

        JPanel panelAccionesEncuesta = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelAccionesEncuesta.add(btnCancelarModificar);
        panelAccionesEncuesta.add(btnGuardarEncuesta);

        panelSur.add(panelNavegacion, BorderLayout.WEST);
        panelSur.add(panelAccionesPregunta, BorderLayout.CENTER);
        panelSur.add(panelAccionesEncuesta, BorderLayout.EAST);
        panelSur.setBorder(new EmptyBorder(10, 0, 10, 0));

        add(panelSur, BorderLayout.SOUTH);
        asignarListenersModificacion();
    }

    private void actualizarVistaPreguntaModificar() {
        panelPreguntaContenedor.removeAll();
        boolean hayPreguntas = listaPreguntasInfo != null && !listaPreguntasInfo.isEmpty();

        btnModificarPregunta.setEnabled(hayPreguntas);
        btnEliminarPregunta.setEnabled(hayPreguntas);

        if (!hayPreguntas) {
            btnAnteriorPregunta.setEnabled(false);
            btnSiguientePregunta.setEnabled(false);
            panelPreguntaContenedor.add(new JLabel(textNoPreguntas, SwingConstants.CENTER), BorderLayout.CENTER);
        }
        else {
            HashMap<String, String> infoPregunta = listaPreguntasInfo.get(preguntaActualIndex);
            PanelPregunta panelPregunta = new PanelPregunta(infoPregunta);

            panelPreguntaContenedor.add(panelPregunta, BorderLayout.CENTER);

            btnAnteriorPregunta.setEnabled(preguntaActualIndex > 0);
            btnSiguientePregunta.setEnabled(preguntaActualIndex < listaPreguntasInfo.size() - 1);

            String contador = String.format(textContadorPreguntas, preguntaActualIndex + 1, listaPreguntasInfo.size());
            panelPreguntaContenedor.add(new JLabel(contador, SwingConstants.CENTER), BorderLayout.SOUTH);
        }
        revalidate();
        repaint();
    }

    private boolean mostrarDialogoCrearPregunta(boolean esModificacion, int idOriginal) {
        JTextField txtID = new JTextField(5);
        JTextField txtTexto = new JTextField(30);
        JCheckBox checkObligatorio = new JCheckBox("Sí", true);
        JTextArea txtDescripcion = new JTextArea(3, 30);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        String[] tipos = {"ConOpciones", "Numerica", "FormatoLibre"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);

        JTextField txtMin = new JTextField(5);
        JTextField txtMax = new JTextField(5);
        JTextField txtMaxSelect = new JTextField(5);
        JTextArea areaOpciones = new JTextArea(5, 30);
        JScrollPane scrollOpciones = new JScrollPane(areaOpciones);
        scrollOpciones.setMinimumSize(new Dimension(300, 100));
        scrollOpciones.setPreferredSize(new Dimension(300, 120));

        JPanel panelDinamico = new JPanel(new CardLayout());
        panelDinamico.setPreferredSize(new Dimension(500, 200));

        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBorder(new EmptyBorder(5, 0, 0, 0));

        JPanel panelMaxSelectRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMaxSelectRow.add(new JLabel(textLabelMaxSelect));
        panelMaxSelectRow.add(txtMaxSelect);
        panelMaxSelectRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelOpciones.add(panelMaxSelectRow);
        panelOpciones.add(new JLabel(textLabelOpciones));
        panelOpciones.add(scrollOpciones);

        JPanel panelNumerica = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNumerica.add(new JLabel(textLabelMin));
        panelNumerica.add(txtMin);
        panelNumerica.add(new JLabel(textLabelMax));
        panelNumerica.add(txtMax);

        panelDinamico.add(panelOpciones, "ConOpciones");
        panelDinamico.add(panelNumerica, "Numerica");
        panelDinamico.add(new JPanel(), "FormatoLibre");

        comboTipo.addActionListener(e -> ((CardLayout) panelDinamico.getLayout()).show(panelDinamico, (String) comboTipo.getSelectedItem()));
        txtID.setEditable(false);
        if (esModificacion) {
            HashMap<String, String> info = listaPreguntasInfo.get(preguntaActualIndex);
            txtID.setText(info.get("ID"));
            txtTexto.setText(info.get("TEXTO"));
            checkObligatorio.setSelected(Boolean.parseBoolean(info.get("OBLIGATORIO")));
            comboTipo.setSelectedItem(info.get("TIPO"));
            if (info.get("DESCRIPCION") != null) txtDescripcion.setText(info.get("DESCRIPCION"));

            if ("Numerica".equals(info.get("TIPO"))) {
                txtMin.setText(info.get("MIN"));
                txtMax.setText(info.get("MAX"));
            } else if ("ConOpciones".equals(info.get("TIPO"))) {
                txtMaxSelect.setText(info.get("MAXSELECT"));
                StringBuilder sb = new StringBuilder();
                int num = Integer.parseInt(info.get("NUMOPCIONES"));
                for (int i = 1; i <= num; i++) sb.append(info.get("OPCION" + i)).append("\n");
                areaOpciones.setText(sb.toString().trim());
            }
        } else {
            txtMaxSelect.setText("1");
            int maxId = 0;
            if (listaPreguntasInfo != null) {
                for (HashMap<String, String> info : listaPreguntasInfo) {
                    maxId = Math.max(maxId, Integer.parseInt(info.get("ID")));
                }
            }
            txtID.setText(String.valueOf(maxId + 1));
        }

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        addFormRow(panelPrincipal, textLabelId, txtID);
        addFormRow(panelPrincipal, textLabelTexto, txtTexto);
        addFormRow(panelPrincipal, textLabelObligatorio, checkObligatorio);
        addFormRow(panelPrincipal, textLabelTipo, comboTipo);
        addFormRow(panelPrincipal, textLabelDescripcion, scrollDescripcion);
        panelPrincipal.add(panelDinamico);

        ((CardLayout) panelDinamico.getLayout()).show(panelDinamico, (String) comboTipo.getSelectedItem());

        String titulo = esModificacion ? String.format(textTituloMod, idOriginal) : textTituloAdd;
        JOptionPane pane = new JOptionPane(panelPrincipal, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = pane.createDialog(this, titulo);
        dialog.setResizable(true);
        dialog.setMinimumSize(new Dimension(550, 600));
        dialog.setVisible(true);

        Object val = pane.getValue();
        if (val != null && (int) val == JOptionPane.OK_OPTION) {
            String sID = txtID.getText().trim();
            String sTexto = txtTexto.getText().trim();
            String sTipo = (String) comboTipo.getSelectedItem();

            if (sID.isEmpty() || sTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, textErrorValidacion, textErrorValidacionTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }

            HashMap<String, String> datos = new HashMap<>();
            datos.put("ID", sID);
            datos.put("TEXTO", sTexto);
            datos.put("OBLIGATORIO", String.valueOf(checkObligatorio.isSelected()));
            datos.put("TIPO", sTipo);
            datos.put("DESCRIPCION", txtDescripcion.getText().trim());

            try {
                if ("Numerica".equals(sTipo)) {
                    int min = Integer.parseInt(txtMin.getText().trim());
                    int max = Integer.parseInt(txtMax.getText().trim());
                    if (min >= max) throw new IllegalArgumentException("Mínimo debe ser menor que Máximo.");
                    datos.put("MIN", String.valueOf(min));
                    datos.put("MAX", String.valueOf(max));
                } else if ("ConOpciones".equals(sTipo)) {
                    int maxSel = Integer.parseInt(txtMaxSelect.getText().trim());
                    String[] ops = areaOpciones.getText().trim().split("\n");
                    ArrayList<String> validas = new ArrayList<>();
                    for (String op : ops) if (!op.trim().isEmpty()) validas.add(op.trim());

                    if (validas.isEmpty()) throw new IllegalArgumentException("Debe haber al menos una opción.");
                    if (maxSel <= 0 || maxSel > validas.size()) throw new IllegalArgumentException("Máximo seleccionable inválido.");

                    datos.put("NUMOPCIONES", String.valueOf(validas.size()));
                    datos.put("MAXSELECT", String.valueOf(maxSel));
                    for (int i = 0; i < validas.size(); i++) datos.put("OPCION" + (i + 1), validas.get(i));
                }
                presenter.crearPregunta(datos);
                return true;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, textErrorNum, textErrorFormatoTitle, JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, String.format(textErrorLogica, ex.getMessage()), textErrorValidacionTitle, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, String.format(textErrorGuardar, ex.getMessage()), textError, JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    private void addFormRow(JPanel container, String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));
        row.add(label, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, component.getPreferredSize().height + 5));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(row);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void asignarListenersNavegacion() {
        btnSiguientePregunta.addActionListener(e -> {
            if (preguntaActualIndex < listaPreguntasInfo.size() - 1) {
                preguntaActualIndex++;
                actualizarVistaPreguntaModificar();
            }
        });
        btnAnteriorPregunta.addActionListener(e -> {
            if (preguntaActualIndex > 0) {
                preguntaActualIndex--;
                actualizarVistaPreguntaModificar();
            }
        });
    }

    private void asignarListenersModificacion() {
        btnGuardarEncuesta.addActionListener(e -> {
            try {
                presenter.guardarEncuesta();
                contenedor.cargarListaEncuestas();
                JOptionPane.showMessageDialog(this, textExitoGuardar, textExito, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, String.format(textErrorGuardar, ex.getMessage()), textError, JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelarModificar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, textConfirmarDescarte, textTituloDescarte, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                presenter.deseleccionarEncuestaActual();
                contenedor.mostrarPanelCargar();
            }
        });

        btnAddPregunta.addActionListener(e -> {
            if (mostrarDialogoCrearPregunta(false, 0)) {
                try {
                    listaPreguntasInfo = presenter.getPreguntasEncuestaCargada();
                    preguntaActualIndex = listaPreguntasInfo.size() - 1;
                    actualizarVistaPreguntaModificar();
                    JOptionPane.showMessageDialog(this, textExitoAdd, textExito, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, String.format(textErrorRecargar, ex.getMessage()), textError, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnModificarPregunta.addActionListener(e -> {
            if (listaPreguntasInfo == null || listaPreguntasInfo.isEmpty()) return;
            int id = Integer.parseInt(listaPreguntasInfo.get(preguntaActualIndex).get("ID"));
            if (mostrarDialogoCrearPregunta(true, id)) {
                try {
                    listaPreguntasInfo = presenter.getPreguntasEncuestaCargada();
                    actualizarVistaPreguntaModificar();
                    JOptionPane.showMessageDialog(this, textExitoMod, textExito, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, String.format(textErrorRecargar, ex.getMessage()), textError, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEliminarPregunta.addActionListener(e -> {
            if (listaPreguntasInfo == null || listaPreguntasInfo.isEmpty()) return;
            int id = Integer.parseInt(listaPreguntasInfo.get(preguntaActualIndex).get("ID"));
            String txt = listaPreguntasInfo.get(preguntaActualIndex).get("TEXTO");
            if (JOptionPane.showConfirmDialog(this, String.format(textConfirmarEliminar, id, txt), textTituloEliminar, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                try {
                    presenter.borrarPreguntaPorId(id);
                    listaPreguntasInfo = presenter.getPreguntasEncuestaCargada();
                    if (preguntaActualIndex >= listaPreguntasInfo.size() && preguntaActualIndex > 0) preguntaActualIndex--;
                    actualizarVistaPreguntaModificar();
                    JOptionPane.showMessageDialog(this, textExitoEliminar, textExito, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, String.format(textErrorEliminar, ex.getMessage()), textError, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}