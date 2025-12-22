package main.domain;

import main.domain.types.TDatos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Clase que representa una respuesta a una encuesta.
 * Esta clase guarda los datos de las respuestas a cada pregunta de la encuesta a la que responde, el título y creador
 * de la encuesta y el email del perfil que ha respondido.
 * @author Andres Lafuente Patau
 */
public class RespuestaEncuesta implements Serializable {

    private  String tituloencuesta; // Encuesta a la que responde
    private  String creador; // Perfil del creador de la encuesta
    private String emailrespuesta; // email del respondedor

    // idPregunta y dato respondido
    private  TreeMap<Integer, TDatos> datosRespuesta = new TreeMap<>();

    /**
     * Constructor de la clase.
     * @param encuesta Título de la encuesta.
     * @param autor Autor de la encuesta.
     * @param email Email del respondedor.
     */
    public RespuestaEncuesta(String encuesta, String autor, String email) {
        this.tituloencuesta = encuesta;
        this.creador = autor;
        this.emailrespuesta = email;
    }

 //Getters

    /**
     * Obtiene el título de la encuesta.
     * @return Título de la encuesta.
     */
    public String getEncuesta() {
        return tituloencuesta;
    }

    /**
     * Obtiene el creador de la encuesta.
     * @return Creador de la encuesta.
     */
    public String getCreador() {
        return creador;
    }

    /**
     * Obtiene el email del respondedor.
     * @return Email del respondedor.
     */
    public String getEmailrespuesta() {
        return emailrespuesta;
    }

    /**
     * Obtiene los datos de las respuesta.
     * @return Datos de la respuesta.
     */
    public TreeMap<Integer,TDatos> getDatosRespuesta(){
        return datosRespuesta;
    }

//Setters

    /**
     * Añade un título de encuesta nuevo.
     * @param encuesta Título nuevo.
     */
    public void SetEncuesta(String encuesta){
        if (encuesta == null) throw new IllegalArgumentException("encuesta null");
        this.tituloencuesta = encuesta;
    }

    /**
     * Añade un creador de encuesta nuevo.
     * @param perfil Creador nuevo.
     */
    public void setPerfil(String perfil){
        if(perfil == null) throw new IllegalArgumentException("perfil null");
        this.creador = perfil;
    }

    /**
     * Añade un email de respondedor nuevo.
     * @param emailResp Email nuevo.
     */
    public void setEmailrespuesta(String emailResp){
        this.emailrespuesta = emailResp;
    }

    /**
     * Añade una nueva respuesta a una pregunta.
     * @param id ID de la pregunta.
     * @param datos Datos de la respuesta a la pregunta.
     */
    public void addRespuesta(int id, TDatos datos) {
        this.datosRespuesta.put(id, datos);
    }

    /**
     * Obtiene los datos de la respuesta en una lista.
     * @return Datos de la respuesta en una lista.
     */
    public ArrayList<TDatos> getDatos() {
        return new ArrayList<>(datosRespuesta.values());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RespuestaEncuesta other = (RespuestaEncuesta) obj;

        return  datosRespuesta.equals(other.datosRespuesta);
    }
}
