package main.domain;

import java.io.Serializable;
import java.util.HashSet;

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

    //GETTERS
    public String getTitulo() {
        return titulo;
    }
    public String getCreador() {
        return creador;
    }
    public HashSet<Pregunta> getPreguntas() {
        return preguntas;
    }

    //SETTERS

    //eliminada por problemas de integridad
    //public void setTitulo(String t) {this.titulo = t;}

    public void setPreguntas(HashSet<Pregunta> preguntas) {this.preguntas = preguntas;}

    //ADDERS
    public void addPregunta(Pregunta p) {this.preguntas.add(p);}

    //REMOVERS
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
