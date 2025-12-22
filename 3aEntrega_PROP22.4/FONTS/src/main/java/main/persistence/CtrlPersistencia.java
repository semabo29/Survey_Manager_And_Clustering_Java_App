package main.persistence;

import main.persistence.exceptions.FalloPersistencia;
import main.persistence.exceptions.LecturaNula;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Controlador de persistencia que gestiona la persistencia de perfiles, encuestas y respuestas.
 * Utiliza el patrón Singleton para asegurar una única instancia.
 * También proporciona métodos de consultar arbitrarias útiles para la presentación y otros controladores.
 * @author Hadeer Abbas Khalil Wysocka
 */
public class CtrlPersistencia {
    /**
     * Instancia única singleton del controlador de persistencia.
     */
    private static CtrlPersistencia INSTANCE = new CtrlPersistencia();
    /**
     * Gestor para la persistencia de perfiles.
     */
    private GestorPerfil gestorPerfil;
    /**
     * Gestor para la persistencia de encuestas.
     */
    private GestorEncuesta gestorEncuesta;
    /**
     * Gestor para la persistencia de respuestas.
     */
    private GestorRespuestaEncuesta gestorRespuestaEncuesta;

    private CtrlPersistencia() {
        gestorPerfil = new GestorPerfil();
        gestorEncuesta = new GestorEncuesta();
        gestorRespuestaEncuesta = new GestorRespuestaEncuesta();
    }

    /**
     * Obtiene la instancia única del controlador de persistencia.
     * @return Instancia única del controlador de persistencia.
     */
    public static CtrlPersistencia getInstance() {
        return INSTANCE;
    }

    /**
     * Guarda el perfil de un usuario.
     * @param email Email del usuario.
     * @param datos Datos del perfil en un flujo de bytes.
     */
    public void guardarPerfil(String email, ByteArrayOutputStream datos) {
        gestorPerfil.guardarPerfil(email, datos);
    }

    /**
     * Carga el perfil de un usuario.
     * @param email Email del usuario.
     * @return Datos del perfil en un flujo de bytes.
     */
    public ByteArrayInputStream cargarPerfil(String email) {
        return gestorPerfil.cargarPerfil(email);
    }

    /**
     * Borra el perfil de un usuario.
     * @param email Email del usuario.
     */
    public void borrarPerfil(String email) {
        gestorPerfil.borrarPerfil(email);
    }

    /**
     * Guarda una encuesta.
     * @param emailCreador Email del creador de la encuesta.
     * @param titulo Título de la encuesta.
     * @param datos Datos de la encuesta en un flujo de bytes.
     */
    public void guardarEncuesta(String emailCreador, String titulo, ByteArrayOutputStream datos) {
        gestorEncuesta.guardarEncuesta(emailCreador, titulo, datos);
    }

    /**
     * Carga una encuesta.
     * @param emailCreador Email del creador de la encuesta.
     * @param titulo Título de la encuesta.
     * @return Datos de la encuesta en un flujo de bytes.
     */
    public ByteArrayInputStream cargarEncuesta(String emailCreador, String titulo) {
        return gestorEncuesta.cargarEncuesta(emailCreador, titulo);
    }

    /**
     * Borra una encuesta.
     * @param emailCreador Email del creador de la encuesta.
     * @param titulo Título de la encuesta.
     */
    public void borrarEncuesta(String emailCreador, String titulo) {
        gestorEncuesta.borrarEncuesta(emailCreador, titulo);
    }

    /**
     * Guarda una respuesta a una encuesta.
     * @param emailCreador Email del creador de la encuesta.
     * @param titulo Título de la encuesta.
     * @param emailRespuesta Email del usuario que responde.
     * @param datos Datos de la respuesta en un flujo de bytes.
     */
    public void guardarRespuestaEncuesta(String emailCreador, String titulo, String emailRespuesta, ByteArrayOutputStream datos) {
        gestorRespuestaEncuesta.guardarRespuestaEncuesta(emailCreador, titulo, emailRespuesta, datos);
    }

    /**
     * Carga una respuesta a una encuesta.
     * @param emailCreador Email del creador de la encuesta.
     * @param titulo Título de la encuesta.
     * @param emailRespuesta Email del usuario que responde.
     * @return Datos de la respuesta en un flujo de bytes.
     */
    public ByteArrayInputStream cargarRespuestaEncuesta(String emailCreador, String titulo, String emailRespuesta) {
        return gestorRespuestaEncuesta.cargarRespuestaEncuesta(emailCreador, titulo, emailRespuesta);
    }

    /**
     * Borra una respuesta a una encuesta.
     * @param emailCreador Email del creador de la encuesta.
     * @param titulo Título de la encuesta.
     * @param emailRespuesta Email del usuario que responde.
     */
    public void borrarRespuestaEncuesta(String emailCreador, String titulo, String emailRespuesta) {
        gestorRespuestaEncuesta.borrarRespuestaEncuesta(emailCreador, titulo, emailRespuesta);
    }

    /**
     * Carga todas las respuestas de una encuesta.
     * @param emailCreador Email del creador de la encuesta.
     * @param titulo Título de la encuesta.
     * @return Conjunto de flujos de bytes con los datos de las respuestas.
     */
    public HashSet<ByteArrayInputStream> cargarRespuestasDeEncuesta(String emailCreador, String titulo) {
        return gestorRespuestaEncuesta.cargarRespuestasDeEncuesta(emailCreador, titulo);
    }

    // CONSULTORAS ARBITRARIAS

    // Devuelve strings para luego no tener que cargar TODAS las respuestas de una encuesta, simple consultora

    /**
     * Obtiene los emails de los usuarios que han respondido a una encuesta específica.
     * @param emailCreador Email del creador de la encuesta.
     * @param titulo Título de la encuesta.
     * @return Conjunto de emails de los usuarios que han respondido a la encuesta.
     */
    public HashSet<String> getEmailsRespuestasDeEncuesta(String emailCreador, String titulo) {
        return gestorRespuestaEncuesta.getEmailsRespuestasDeEncuesta(emailCreador, titulo);
    }

    /**
     * Obtiene una lista de pares (título, autor) de todas las encuestas.
     * @return Lista de pares (título, autor) de todas las encuestas.
     */
    public ArrayList<SimpleEntry<String,String>> getTituloAutorAllEncuestas() {
        return gestorEncuesta.getTituloAutorAllEncuestas();
    }

    // Key = titulo, Value = creador

    /**
     * Obtiene las respuestas realizadas por un usuario específico.
     * @param email Email del usuario.
     * @return Conjunto de pares (identificador de encuesta, email del creador de la encuesta) de respuestas hechas por el usuario.
     */
    public HashSet<SimpleEntry<String,String>> getRespuestasByEmail (String email) {
        return gestorRespuestaEncuesta.getRespuestasByEmail(email);
    }

    // pseudo-patron plantilla para la parte comun de escribir y leer, aunque esto no sea de la superclase. Para no repetir codigo.

    /**
     * Escribe una clase serializada en el sistema de archivos.
     * @param dir Directorio donde se guardará el archivo.
     * @param path Ruta completa del archivo incluyendo el nombre del fichero dentro del directorio en que se guarda.
     * @param datos Datos de la clase serializada en un flujo de bytes.
     * @throws FalloPersistencia Si ocurre un error inesperado de persistencia al guardar.
     */
    public void escribirClaseSerializada(Path dir, Path path, ByteArrayOutputStream datos) throws FalloPersistencia {
        try {
            // Creamos los directorios si no existen
            Files.createDirectories(dir);
            // Guardamos el fichero
            Files.write(path, datos.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new FalloPersistencia("Error inesperado de persistencia al guardar: " + e.getMessage());
        }
    }

    /**
     * Lee una clase serializada desde el sistema de archivos.
     * @param path Ruta completa del archivo incluyendo el nombre del fichero a leer.
     * @return Datos de la clase serializada en un flujo de bytes.
     * @throws LecturaNula Si el fichero no existe o está vacío.
     * @throws FalloPersistencia Si ocurre un error inesperado de persistencia al cargar.
     */
    public ByteArrayInputStream leerClaseSerializada(Path path) throws LecturaNula, FalloPersistencia {
        try {
            if (Files.notExists(path)) {
                throw new LecturaNula("El fichero en " + path.toString() + " no existe");
            }
            byte[] datos = Files.readAllBytes(path);
            if (datos.length == 0) throw new LecturaNula("El fichero esta vacio");
            return new ByteArrayInputStream(datos);
        }
        catch (IOException e) {
            throw new FalloPersistencia("Error inesperado de persistencia al cargar: " + e.getMessage());
        }
    }
}
