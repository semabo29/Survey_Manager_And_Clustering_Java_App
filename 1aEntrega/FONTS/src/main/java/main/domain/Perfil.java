package main.domain;

import main.domain.exceptions.EmailInvalido;
import java.io.Serializable;
import java.util.HashSet;
import java.util.AbstractMap.SimpleEntry; // Implementacion alternativa a Pair: Key corresponde a titulo, y Value a creador

public class Perfil implements Serializable {

    private String email;
    private String nombre;
    private String contrasena;
    private HashSet<String> encuestasCreadas; // Por la fuerza que el emailCreador de la encuesta sea este
    private HashSet<SimpleEntry<String,String>> respuestasHechas; // Por la fuerza que el emailRespuesta sea este
    // Si en algun caso de uso hace falta acceder a asociaciones con estos identificadores, se han de juntar con getEmail de este perfil

    public Perfil(String email, String nombre, String contrasena) throws EmailInvalido {
        if (!validarEmail(email)) {
            throw new EmailInvalido("El email proporcionado no es válido: " + email);
        }
        this.email = email;
        this.nombre = nombre;
        this.contrasena = contrasena;
        encuestasCreadas = new HashSet<String>();
        respuestasHechas = new HashSet<SimpleEntry<String,String>>();
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public HashSet<String> getEncuestasCreadas() {
        return encuestasCreadas;
    }

    public HashSet<SimpleEntry<String,String>> getRespuestasHechas() {
        return respuestasHechas;
    }

    // Setters

    // Una vez creado el perfil, el email no se puede cambiar
    //public void setEmail(String email) {this.email = email;}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Metodo innecesario
    //public void setEncuestasCreadas(Set<String> encCre) {this.encuestasCreadas = encCre;}

    // Metodo innecesario
    //public void setRespuestasEncuesta(Set<SimpleEntry<String,String>> respHec) {this.respuestasHechas = respHec;}

    // Add, remove methods, importante recordar que los parametros son instancias pero lo que guarda son identificadores

    public void addEncuestaCreada(Encuesta enc) {
        this.encuestasCreadas.add(enc.getTitulo());
    }

    public void addRespuestaHecha(RespuestaEncuesta respEnc) {
        this.respuestasHechas.add(new SimpleEntry<>(respEnc.getEncuesta(), respEnc.getCreador()));
    }

    public void removeEncuestaCreada(Encuesta enc) {
        this.encuestasCreadas.remove(enc.getTitulo());
    }

    public void removeRespuestaHecha(RespuestaEncuesta respEnc) {
        this.respuestasHechas.remove(new SimpleEntry<>(respEnc.getEncuesta(), respEnc.getCreador()));
    }

    // Metodos privados

    // Expresion regex para validar un email
    private boolean validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

}