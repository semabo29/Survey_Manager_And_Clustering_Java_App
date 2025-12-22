package main.domain;

import main.domain.types.TDatos;

import java.io.Serializable;

/**
 * Interfaz que representa un tipo de pregunta de una encuesta. Tiene los métodos para validar respuestas.
 * @author Yimin Jin
 */
public interface TipoPregunta extends Serializable {
    // Comprueba que datos sea valido

    /**
     * Operación que comprueba que una respuesta a un tipo de pregunta sea válida.
     * @param datos Datos de una respuesta.
     * @param obligatorio Obligatoriedad de la pregunta.
     * @return true si los datos son válidos, false en caso contrario.
     */
    public boolean isValid(TDatos datos, boolean obligatorio);

    // Valida datos, añadiendo los atributos que le falten

    /**
     * Valida los datos de una respuesta. Añade los atributos que hacen falta.
     * @param datos Datos incompletos de una respuesta.
     * @param obligatorio Obligatoriedad de la pregunta.
     * @throws Exception Lanza una excepción si la respuesta no es válida para esta pregunta.
     */
    public void validar(TDatos datos, boolean obligatorio) throws Exception;
}
