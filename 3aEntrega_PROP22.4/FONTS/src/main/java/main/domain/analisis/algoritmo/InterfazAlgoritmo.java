package main.domain.analisis.algoritmo;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;

import java.util.ArrayList;

/**
 * Interfaz genérica para algoritmos de clustering de encuestas.
 *
 * @param <T> Tipo de algoritmo, debe implementar {@link AlgoritmoTipo}.
 * @author Javier Zhangpan
 */
public interface InterfazAlgoritmo <T extends AlgoritmoTipo> {
    /**
     * Aplica un algoritmo de clustering sobre un conjunto de respuestas a una encuesta
     *
     * @param centroides Centroides iniciales. Debe contener {@code k} elementos.
     * @param respuestas Las respuestas que se quieren agrupar.
     * @param k          Número de clusters deseados
     * @param comp       Comparador a usar para el cálculo de distancias y centroides
     * @return Devuelve un {@link ArrayList} con {@code k} clusters, donde cada cluster es a su vez un
     *  {@link ArrayList} de {@link RespuestaEncuesta}.
     */
    ArrayList<ArrayList<RespuestaEncuesta>> analizar(ArrayList<RespuestaEncuesta> centroides, ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp);
}
