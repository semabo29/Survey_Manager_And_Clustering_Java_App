package main.domain;

import main.domain.exceptions.EmailInvalido;
import java.io.Serializable;
import java.util.HashSet;
import java.util.AbstractMap.SimpleEntry; // Implementacion alternativa a Pair: Key corresponde a titulo, y Value a creador

/**
 * Clase que representa un perfil de usuario en el sistema de encuestas.
 * @author Hadeer Abbas Khalil Wysocka
 */
public class Perfil implements Serializable {

    /**
     * Email del perfil, que actua como identificador unico.
     */
    private String email;
    /**
     * Nombre del perfil.
     */
    private String nombre;
    /**
     * Contrasena del perfil.
     */
    private String contrasena;
    /**
     * Conjunto de identificadores de encuestas creadas por este perfil.
     */
    private HashSet<String> encuestasCreadas; // Por la fuerza que el emailCreador de la encuesta sea este
    /**
     * Conjunto de pares (identificador de encuesta, email del creador de la encuesta) de respuestas hechas por este perfil.
     */
    private HashSet<SimpleEntry<String,String>> respuestasHechas; // Por la fuerza que el emailRespuesta sea este
    // Si en algun caso de uso hace falta acceder a asociaciones con estos identificadores, se han de juntar con getEmail de este perfil

    /**
     * Constructor de la clase Perfil.
     * @param email Email del perfil.
     * @param nombre Nombre del perfil.
     * @param contrasena Contraseña del perfil.
     * @throws EmailInvalido Si el email proporcionado no es válido.
     */
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

    /**
     * Getter del email del perfil.
     * @return Email del perfil.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter del nombre del perfil.
     * @return Nombre del perfil.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Getter de la contraseña del perfil.
     * @return Contraseña del perfil.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Getter de los identificadores de las encuestas creadas por el perfil.
     * @return Conjunto de identificadores de encuestas creadas por el perfil.
     */
    public HashSet<String> getEncuestasCreadas() {
        return encuestasCreadas;
    }

    /**
     * Getter de los pares (identificador de encuesta, email del creador de la encuesta) de respuestas hechas por el perfil.
     * @return Conjunto de pares (identificador de encuesta, email del creador de la encuesta) de respuestas hechas por el perfil.
     */
    public HashSet<SimpleEntry<String,String>> getRespuestasHechas() {
        return respuestasHechas;
    }

    // Setters

    /**
     * Setter del nombre del perfil.
     * @param nombre Nuevo nombre del perfil.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Setter de la contraseña del perfil.
     * @param contrasena Nueva contraseña del perfil.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Add, remove methods, importante recordar que los parametros son instancias pero lo que guarda son identificadores

    /**
     * Añade un identificador de encuesta creada por el perfil.
     * @param enc Encuesta creada a añadir.
     */
    public void addEncuestaCreada(Encuesta enc) {
        this.encuestasCreadas.add(enc.getTitulo());
    }

    /**
     * Añade un par (identificador de encuesta, email del creador de la encuesta) de respuesta hecha por el perfil.
     * @param respEnc Respuesta hecha a añadir.
     */
    public void addRespuestaHecha(RespuestaEncuesta respEnc) {
        this.respuestasHechas.add(new SimpleEntry<>(respEnc.getEncuesta(), respEnc.getCreador()));
    }

    /**
     * Elimina un identificador de encuesta creada por el perfil.
     * @param enc Encuesta creada a eliminar.
     */
    public void removeEncuestaCreada(Encuesta enc) {
        this.encuestasCreadas.remove(enc.getTitulo());
    }

    /**
     * Elimina un par (identificador de encuesta, email del creador de la encuesta) de respuesta hecha por el perfil.
     * @param respEnc Respuesta hecha a eliminar.
     */
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