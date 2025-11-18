package main.domain;

import main.domain.types.TDatos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;


public class RespuestaEncuesta implements Serializable {

    private  String tituloencuesta; // Encuesta a la que responde
    private  String creador; // Perfil del que responde
    private String emailrespuesta; //email del creador

    // idPregunta y dato respondido
    private  TreeMap<Integer, TDatos> idRespuesta = new TreeMap<>();

    public RespuestaEncuesta(String encuesta, String autor, String email) {
        //if (encuesta == null) throw new IllegalArgumentException("encuesta null");
        //if (autor == null) throw new IllegalArgumentException("autor null");
        this.tituloencuesta = encuesta;
        this.creador = autor;
        this.emailrespuesta = email;
    }

 //Getters
    public String getEncuesta() {
        return tituloencuesta;
    }
    public String getCreador() {
        return creador;
    }

    public String getEmailrespuesta() {
        return emailrespuesta;
    }

    public TreeMap<Integer,TDatos> getIdRespuesta(){
        return idRespuesta;
    }

//Setters
    public void SetEncuesta(String encuesta){
        if (encuesta == null) throw new IllegalArgumentException("encuesta null");
        this.tituloencuesta = encuesta;
    }

    public void setPerfil(String perfil){
        if(perfil == null) throw new IllegalArgumentException("perfil null");
        this.creador = perfil;
    }

    public void setEmailrespuesta(String emailResp){
        this.emailrespuesta = emailResp;
    }
/*
    public float getdistancia(RespuestaEncuesta respuesta1){
        float suma = 0f;
        for(int i = 0; i < idRespuesta.size();i++){
            suma += (idRespuesta.get(i).calcularDistancia(respuesta1.getIdRespuesta().get(i)));
        }
        float media = suma /idRespuesta.size();
        return media;
    }

 */


/*
    public void responder(Pregunta pregunta, TDatos datos) throws Exception {
        if (pregunta == null) throw new IllegalArgumentException("pregunta null");
        if (datos == null) throw new IllegalArgumentException("datos null");
        if (pregunta.getEncuesta() != this.encuesta) {
            throw new IllegalArgumentException("La pregunta no pertenece a esta encuesta");
        }
        pregunta.validar(datos);
        idRespuesta.put(pregunta.getId(), datos);
    }

 */

    public void addRespuesta(int id, TDatos datos) {
        this.idRespuesta.put(id, datos);
    }


    public ArrayList<TDatos> getDatos() {
        return new ArrayList<>(idRespuesta.values());
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

        return  idRespuesta.equals(other.idRespuesta);
    }
}
