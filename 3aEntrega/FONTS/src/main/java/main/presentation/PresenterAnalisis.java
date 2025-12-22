package main.presentation;

import java.util.ArrayList;

/**
 * Interfaz del presentador para análisis.
 * Define los métodos para usar el analizador.
 */
public interface PresenterAnalisis {
    /**
     * Indica qué algoritmos utilizar para el análisis.
     * @param algoritmo Algoritmo de clustering.
     * @param inicializador Inicializador del algoritmo.
     */
    public void setAlgoritmoInicializador(String algoritmo, String inicializador);

    /**
     * Indica el evaluador de calidad del análisis.
     * @param evaluador Evaluador de calidad.
     */
    public void setEvaluador(String evaluador);

    /**
     * Indica el número de grupos que hay que crear.
     * @param k Número de grupos.
     */
    public void setK(int k);

    /**
     * Cálculo automático del número de grupos.
     * @return Número de grupos calculado.
     */
    public int calcularK();

    /**
     * Iniciar análisis sobre las respuestas cargadas de la encuesta cargada.
     */
    public void analizar();

    /**
     * Obtiene las distancias dentro de los clusters generados tras el análisis.
     * @return Distancias dentro de los clusters.
     */
    public ArrayList<ArrayList<ArrayList<Float>>> getDistanciasClusters();

    /**
     * Obtiene las distancias entre respuestas.
     * @return Distancias entre respuestas.
     */
    public ArrayList<ArrayList<Float>> getDistancias();

    /**
     * Obtiene los autores de las respuestas de cada cluster tras el análisis.
     * @return Autores de las respuestas de cada cluster.
     */
    public ArrayList<ArrayList<String>> getAutoresClusters();

    /**
     * Obtiene el valor de la evaluación de calidad tras el análisis.
     * @return Valor de la evaluación de calidad.
     */
    public float getEval();
}
