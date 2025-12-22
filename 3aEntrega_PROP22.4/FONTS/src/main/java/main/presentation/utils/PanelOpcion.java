package main.presentation.utils;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.AbstractMap;

/**
 * Panel que dibuja las opciones seleccionables de una {@link JList}.
 * Este panel implementa {@link ListCellRenderer} para {@link java.util.AbstractMap.SimpleEntry<String, String>},
 * de manera que podamos mostrar los elementos de una lista y poder seleccionarlos.
 * Esta clase representa estos elementos de una manera más personalizada.
 * @author Yimin Jin
 */
public class PanelOpcion extends JPanel implements ListCellRenderer<AbstractMap.SimpleEntry<String, String>> {
    private final JLabel labelTitulo;
    private final JLabel labelAutor;

    private static final String textCreador = "Creador: ";

    /**
     * Constructor de esta clase.
     */
    public PanelOpcion() {
        // Configurar la plantilla del JPanel
        setLayout(new GridLayout(2, 1)); // Un pequeño espaciado horizontal
        setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(10, 10, 10, 10)));
        //setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED), new EmptyBorder(10, 10, 10, 10)));


        labelTitulo = new JLabel();
        labelAutor = new JLabel();

        // Estilos básicos
        labelTitulo.setFont(labelTitulo.getFont().deriveFont(Font.BOLD));
        labelAutor.setFont(labelAutor.getFont().deriveFont(Font.ITALIC));

        // Añadir a la plantilla
        add(labelTitulo);
        add(labelAutor);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends AbstractMap.SimpleEntry<String, String>> list,
                                                  AbstractMap.SimpleEntry<String, String> value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        // Configurar la plantilla con los datos
        labelTitulo.setText(value.getKey());
        labelAutor.setText(textCreador + value.getValue());

        // Gestionar el estado de selección
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}