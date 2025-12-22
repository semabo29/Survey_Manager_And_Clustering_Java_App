package main;

import main.presentation.CtrlPresentacion;

import javax.swing.UIManager;

/**
 * Main de la aplicación.
 */
public class Main {
    /**
     * Inicia la aplicación. Crea el {@link CtrlPresentacion} y lo inicializa.
     * En nuestra aplicación, usamos el look and feel del sistema.
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        javax.swing.SwingUtilities.invokeLater (
                () -> {
                    CtrlPresentacion ctrlPresentacion =
                            new CtrlPresentacion();
                    ctrlPresentacion.inicializarPresentacion();
                }
        );
    }
}
