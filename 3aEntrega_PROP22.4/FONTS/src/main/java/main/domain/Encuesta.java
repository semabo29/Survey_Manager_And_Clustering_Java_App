package main.domain;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Clase que representa una encuesta, compuesta por un título, un creador y un conjunto de preguntas.
 * Cada encuesta puede contener múltiples {@link Pregunta}, y cada pregunta puede tener múltiples respuestas.
 * La clase implementa {@link Serializable} para permitir la serialización de objetos Encuesta.
 * @author Sergi Malaguilla Bombin
 */
public class Encuesta implements Serializable {
    private final String titulo; //cambia siempre que se quiera
    private final String creador; //final si se quiere que no cambie
    private HashSet<Pregunta> preguntas; //final si se quiere que no cambie

    //CREADORA
    public Encuesta(String titulo, String emailCreador) {
        this.creador= emailCreador;
        this.titulo = titulo;
        //se puede crear sin preguntas y respuestas
        this.preguntas = new HashSet<>();
    }

    /**
     * Obtiene el título de la encuesta.
     * @return El título de la encuesta.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el identificador (email) del creador de la encuesta.
     * @return El email del creador.
     */
    public String getCreador() {
        return creador;
    }

    /**
     * Obtiene el conjunto de preguntas que forman parte de la encuesta.
     * @return Un HashSet conteniendo los objetos Pregunta.
     */
    public HashSet<Pregunta> getPreguntas() {
        return preguntas;
    }

    /**
     * Establece o reemplaza el conjunto completo de preguntas de la encuesta.
     * @param preguntas El nuevo conjunto de preguntas a asignar.
     */
    public void setPreguntas(HashSet<Pregunta> preguntas) {this.preguntas = preguntas;}

    /**
     * Añade una nueva pregunta al conjunto de preguntas de la encuesta, si ya existe una pregunta con el mismo ID
     * la reemplaza.
     * @param p La instancia de pregunta que se desea añadir.
     */
    public void addPregunta(Pregunta p) {
        for (Pregunta pr : this.preguntas) {
            if (pr.getId() == p.getId()){
                preguntas.remove(pr);
                break;
            }
        }
        this.preguntas.add(p);
    }

    /**
     * Elimina una pregunta específica de la encuesta.
     * Al eliminar una pregunta, la funcion reasigna automáticamente los identificadores (ID)
     * de todas las preguntas restantes cuyo ID sea superior al de la pregunta eliminada
     * para mantener una secuencia numérica continua.
     * @param p La pregunta que se desea eliminar de la encuesta.
     */
    public void removePregunta(Pregunta p) {
        //reasignar los id de las preguntas restantes
        for (Pregunta otraPreg : this.preguntas) {
            if (otraPreg.getId() > p.getId()) {
                        otraPreg.setId(otraPreg.getId() - 1);
            }
        }
        //eliminar la pregunta
        this.preguntas.remove(p);
    }
}
