package main.domain.analisis.algoritmo;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;
import main.domain.exceptions.KMenorQueUno;
import main.domain.exceptions.NoHayRespuestasParaAnalizar;

import java.util.ArrayList;

/**
 * Implementación de {@link InterfazAlgoritmo} con el algoritmo de clustering K-Means versión Naive (algoritmo de Lloyd)
 * <p>
 * Esta clase agrupa las respuestas de la encuesta en {@code k} grupos en base a sus distancias.
 * La métrica de distancia y cálculo de centroides se relega a {@link Comparador}.
 * </p>
 * @author Javier Zhangpan
 */
public class KMeans implements InterfazAlgoritmo<KMeansTipo> {

    private static final String textoNoHayRespuesta = "No hay respuestas que analizar";
    private static final String textoKMenor = "La K es menor que 1";

    /**
     * Constructora por defecto
     */
    public KMeans() {}

    /**
     * Aplica el algoritmo de clustering K-Means sobre un conjunto de respuestas a una encuesta
     * <p>
     * El algoritmo asigna de manera iterativa cada respuesta a su centroide más cercano y
     * recalcula estos centroides al final de la iteración hasta alcanzar convergencia.
     * <p>
     * La condición de convergencia es que los centroides no cambien entre iteraciones.
     * </p>
     *
     * @param centroides Centroides iniciales. Debe contener {@code k} elementos.
     * @param respuestas Las respuestas que se quieren agrupar.
     * @param k          Número de clusters deseados
     * @param comp       Comparador a usar para el cálculo de distancias y centroides
     * @return Devuelve un {@link ArrayList} con {@code k} clusters, donde cada cluster es a su vez un
     *  {@link ArrayList} de {@link RespuestaEncuesta}.
     * @throws NoHayRespuestasParaAnalizar si {@code respuestas} es vacío.
     * @throws KMenorQueUno si {@code k} es menor a 1.
     */
    @Override
    public ArrayList<ArrayList<RespuestaEncuesta>> analizar(
            ArrayList<RespuestaEncuesta> centroides,
            ArrayList<RespuestaEncuesta> respuestas,
            Integer k,
            Comparador comp
    ) {
        validarParametros(respuestas, k);

        boolean acaba = false;
        ArrayList<ArrayList<RespuestaEncuesta>> resultado = new ArrayList<>();

        while (!acaba) {
            //inicializa clusters
            ArrayList<ArrayList<RespuestaEncuesta>> clusters = inicializarClusters(k);

            //encuentra centroide más cercano para esa respuesta
            //y añade esa respuesta al clustering asociado a ese centroide
            asignarRespuestasAClusters(respuestas, centroides, clusters, k, comp);

            //recalcula centroides
            ArrayList<RespuestaEncuesta> nuevosCentroides =
                    recalcularCentroides(clusters, centroides, k, comp);

            //comprueba si el algoritmo ha convergido
            if (nuevosCentroides.equals(centroides)) {
                acaba = true;
                resultado = clusters;
            } else {
                centroides = nuevosCentroides;
            }
        }
        return resultado;
    }

    private void validarParametros(ArrayList<RespuestaEncuesta> respuestas, Integer k) {
        if (respuestas.isEmpty())
            throw new NoHayRespuestasParaAnalizar(textoNoHayRespuesta);
        if (k < 1)
            throw new KMenorQueUno(textoKMenor);
    }

    private ArrayList<ArrayList<RespuestaEncuesta>> inicializarClusters(int k) {
        ArrayList<ArrayList<RespuestaEncuesta>> clusters = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }
        return clusters;
    }

    private void asignarRespuestasAClusters(
            ArrayList<RespuestaEncuesta> respuestas,
            ArrayList<RespuestaEncuesta> centroides,
            ArrayList<ArrayList<RespuestaEncuesta>> clusters,
            int k,
            Comparador comp
    ) {
        for (RespuestaEncuesta respuesta : respuestas) {
            int indiceMasCercano = encontrarCentroideMasCercano(respuesta, centroides, k, comp);
            clusters.get(indiceMasCercano).add(respuesta);
        }
    }

    private int encontrarCentroideMasCercano(
            RespuestaEncuesta respuesta,
            ArrayList<RespuestaEncuesta> centroides,
            int k,
            Comparador comp
    ) {
        int indiceMasCercano = 0;
        float distanciaMinima = comp.getDistancia(respuesta, centroides.getFirst());

        for (int j = 1; j < k; ++j) {
            float distancia = comp.getDistancia(respuesta, centroides.get(j));
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                indiceMasCercano = j;
            }
        }

        return indiceMasCercano;
    }

    private ArrayList<RespuestaEncuesta> recalcularCentroides(
            ArrayList<ArrayList<RespuestaEncuesta>> clusters,
            ArrayList<RespuestaEncuesta> centroides,
            int k,
            Comparador comp
    ) {
        ArrayList<RespuestaEncuesta> nuevosCentroides = new ArrayList<>();

        for (int i = 0; i < k; ++i) {
            RespuestaEncuesta nuevoCentroide;
            if (clusters.get(i).isEmpty())
                nuevoCentroide = centroides.get(i);
            else
                nuevoCentroide = comp.calcularNuevoCentroide(clusters.get(i));

            nuevosCentroides.add(nuevoCentroide);
        }

        return nuevosCentroides;
    }
}
