package main.domain;

import java.io.Serializable;

/**
 * Opción de una pregunta con múltiples opciones.
 * Esta clase representa una opción, guardando un ID y el texto de la opción.
 * @author Yimin Jin
 */
public class Opcion implements Serializable {
    private int id;
    private String texto;

    /**
     * Constructor de la clase.
     * @param id ID de la opción.
     * @param texto Texto de la opción.
     */
    public Opcion(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    /**
     * Obtiene el ID de la opción.
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el texto de la opción.
     * @return Texto de la opción.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Añade un nuevo texto a la opción.
     * @param texto Texto nuevo.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Añade un ID nuevo a la opción.
     * @param id ID nuevo.
     */
    public void setId(int id) {
        this.id = id;
    }
}
