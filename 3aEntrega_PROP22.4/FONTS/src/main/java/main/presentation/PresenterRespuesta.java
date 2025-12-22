package main.presentation;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Interfaz del presentador para respuestas.
 * Define los métodos que tienen que ver con la gestión de respuestas.
 */
public interface PresenterRespuesta {
    // Encuestas a las que ha respondido el perfil

    /**
     * Obtiene las respuestas que ha creado el usuario que ha iniciado sesión.
     * @return El titulo y el creador de las encuestas que ha respondido.
     */
    public HashSet<SimpleEntry<String, String>> getRespuestasPerfilCargado();

    /**
     * Prepara la modificación de una respuesta.
     * @param tituloEncuesta Titulo de la encuesta de la respuesta.
     * @param emailCreadorEncuesta Creador de la encuesta de la respuesta.
     */
    public void modificarRespuesta(String tituloEncuesta, String emailCreadorEncuesta);

    // Emails de los perfiles que han respondido

    /**
     * Obtiene los respondedores de la encuesta cargada.
     * @return Emails de los respondedores.
     */
    public ArrayList<String> getRespuestasEncuestaCargada();

    // Las respuestas a la encuesta cargada de un perfil

    /**
     * Obtiene la respuesta del usuario para la encuesta cargada.
     * @return Respuesta de la encuesta cargada respondida por el usuario.
     */
    public ArrayList<String> getRespuestaAEncuestaCargadaPerfilActual();

    /**
     * Carga las respuestas de un conjunto de perfiles para la encuesta cargada.
     * @param emails Lista de emails de perfiles.
     */
    public void cargarConjuntoRespuestas(ArrayList<String> emails);

    /**
     * Carga todas las respuestas de la encuesta cargada.
     */
    public void importarRespuestasEnMemoria();

    /**
     * Prepara una respuesta para responder a la encuesta cargada.
     */
    public void responderEncuesta();

    /**
     * Borra la respuesta cargada.
     */
    public void borrarRespuestaActual();

    /**
     * Guarda la respuesta cargada en persistencia.
     */
    public void guardarRespuesta();

    /**
     * Responder a una pregunta de múltiples opciones.
     * @param id ID de la pregunta.
     * @param opciones Opciones seleccionadas.
     * @throws Exception Lanza excepción si la respuesta no es válida.
     */
    public void responderPreguntaOpciones(int id, ArrayList<Integer> opciones) throws Exception;

    /**
     * Responder a una pregunta númerica.
     * @param id ID de la pregunta.
     * @param value Valor de la respuesta.
     * @throws Exception Lanza excepción si la respuesta no es válida.
     */
    public void responderPreguntaNumerica(int id, int value) throws Exception;

    /**
     * Responder a una pregunta de formato libre.
     * @param id ID de la pregunta.
     * @param text Texto de la respuesta.
     * @throws Exception Lanza excepción si la respuesta no es válida.
     */
    public void responderPreguntaFormatoLibre(int id, String text) throws Exception;

    /**
     * Importar una respuesta externa.
     * @param path Dirección del archivo respuesta.
     */
    public void importarRespuestaPorPath(String path);

}
