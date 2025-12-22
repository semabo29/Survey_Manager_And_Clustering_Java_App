package main.presentation;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interfaz del presentador para encuestas.
 * Define los métodos que tienen que ver con la gestión de encuestas.
 */
public interface PresenterEncuesta {
    /**
     * Obtiene el titulo de la encuesta cargada.
     * @return Título de la encuesta.
     */
    public String getTituloEncuestaCargada();

    /**
     * Obtiene el autor de la encuesta cargada.
     * @return Autor de la encuesta.
     */
    public String getAutorEncuestaCargada();

    /**
     * Crea una encuesta con un título y como creador el usuario actual.
     * @param titulo Título de la encuesta.
     */
    public void crearEncuesta(String titulo);

    /**
     * Crea una pregunta para la encuesta cargada.
     * @param datosPregunta Datos de la pregunta.
     */
    public void crearPregunta(HashMap<String, String> datosPregunta);

    /**
     * Carga una encuesta.
     * @param titulo Título de la encuesta.
     * @param autor Autor de la encuesta.
     */
    public void importarEncuesta(String titulo, String autor);

    /**
     * Borra la encuesta cargada.
     */
    public void borrarEncuestaActual();

    /**
     * Borra una pregunta por su ID.
     * @param id ID de la pregunta.
     */
    public void borrarPreguntaPorId(int id);

    /**
     * Obtiene las preguntas de la encuesta cargada.
     * @return Preguntas de la encuesta.
     */
    public ArrayList<HashMap<String, String>> getPreguntasEncuestaCargada();

    /**
     * Guarda la encuesta cargada en persistencia.
     */
    public void guardarEncuesta();

    /**
     * Deselecciona la encuesta cargada.
     */
    public void deseleccionarEncuestaActual();
    // Primer atributo titulo, segundo autor

    /**
     * Obtiene el título y autor de todas las encuestas disponibles en el sistema.
     * @return Lista de títulos y autores.
     */
    public ArrayList<AbstractMap.SimpleEntry<String, String>> getTituloAutorAllEncuestas();

    /**
     * Importar una encuesta externa.
     * @param path Dirección del archivo encuesta.
     */
    public void importarEncuestaPorPath(String path);

}
