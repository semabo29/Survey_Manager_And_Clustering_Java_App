package main.domain.analisis.algoritmo;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;
import main.domain.exceptions.KMenorQueUno;
import main.domain.exceptions.NoHayRespuestasParaAnalizar;

import java.util.ArrayList;

/**
 * Implementación de {@link InterfazAlgoritmo} con el algoritmo de clustering K-Means versión optimizada.
 * Explota la desigualdad triangular para ahorrar cálculos de distancias
 * <p>
 * Esta clase agrupa las respuestas de la encuesta en {@code k} grupos en base a sus distancias.
 * La métrica de distancia y cálculo de centroides se relega a {@link Comparador}.
 * </p>
 * @author Javier Zhangpan
 */
public class KMeansOptimizado implements InterfazAlgoritmo<KMeansTipo> {

    private static final String textoNoHayRespuesta = "No hay respuestas que analizar";
    private static final String textoKMenor = "La K es menor que 1";

    /**
     * Aplica el algoritmo de clustering K-Means sobre un conjunto de respuestas a una encuesta
     * <p>
     * El algoritmo asigna de manera iterativa cada respuesta a su centroide más cercano y
     * recalcula estos centroides al final de la iteración hasta alcanzar convergencia.
     * <p>
     * Además, el algoritmo mantiene estructuras de datos adicionales para poder
     * ahorrarse cálculos de distancias.
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
        validarEntradas(respuestas, k);

        ArrayList<Float> upperBounds = initFloats(respuestas.size());
        ArrayList<ArrayList<Float>> lowerBounds = initMat(respuestas.size(), centroides.size());
        ArrayList<Integer> asignaciones = initInts(respuestas.size());
        ArrayList<ArrayList<Float>> distanciasEntreCentroides = initMatDistancias(centroides, comp);
        ArrayList<ArrayList<Float>> distanciasACentroides = initMat(respuestas.size(), centroides.size());
        ArrayList<Float> S = computarS(distanciasEntreCentroides);
        ArrayList<RespuestaEncuesta> nuevosCentroides = initCentroides(centroides.size());

        initBounds(upperBounds, lowerBounds, asignaciones, centroides, respuestas,
                comp, distanciasEntreCentroides, distanciasACentroides);

        ArrayList<Boolean> respuestasQueNoNecesitanRecalcular =
                identificarRespuestasQueNoNecesitanRecalcular(respuestas, upperBounds, S, asignaciones);

        boolean convergido = false;

        while (!convergido) {
            actualizarAsignaciones(respuestas, centroides, upperBounds, lowerBounds,
                    distanciasEntreCentroides, distanciasACentroides,
                    asignaciones, respuestasQueNoNecesitanRecalcular, comp);

            recalcularCentroides(nuevosCentroides, centroides, respuestas, asignaciones, comp);

            distanciasEntreCentroides = initMatDistancias(nuevosCentroides, comp);
            S = computarS(distanciasEntreCentroides);

            convergenciaYActualizacion(respuestas, centroides, nuevosCentroides,
                    upperBounds, lowerBounds, asignaciones,
                    respuestasQueNoNecesitanRecalcular, comp);

            if (nuevosCentroides.equals(centroides)) convergido = true;
            else centroides = nuevosCentroides;
        }

        return generarClusters(respuestas, asignaciones, k);
    }

    private static void validarEntradas(ArrayList<RespuestaEncuesta> respuestas, int k) {
        if (respuestas.isEmpty())
            throw new NoHayRespuestasParaAnalizar(textoNoHayRespuesta);
        if (k < 1)
            throw new KMenorQueUno(textoKMenor);
    }

    private static ArrayList<RespuestaEncuesta> initCentroides(int k) {
        ArrayList<RespuestaEncuesta> nuevos = new ArrayList<>();
        for (int i = 0; i < k; ++i) nuevos.add(null);
        return nuevos;
    }

    private static ArrayList<Float> initFloats(int n) {
        ArrayList<Float> array = new ArrayList<>();
        for (int i = 0; i < n; ++i) array.add(0.0f);
        return array;
    }

    private static ArrayList<Integer> initInts(int n) {
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < n; ++i) array.add(0);
        return array;
    }

    private static ArrayList<ArrayList<Float>> initMat(int n, int m) {
        ArrayList<ArrayList<Float>> mat = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            ArrayList<Float> row = new ArrayList<>();
            for (int j = 0; j < m; ++j) row.add(0.0f);
            mat.add(row);
        }
        return mat;
    }

    private static ArrayList<ArrayList<Float>> initMatDistancias(
            ArrayList<RespuestaEncuesta> centroides,
            Comparador comp) {
        int n = centroides.size();
        ArrayList<ArrayList<Float>> mat = initMat(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                float d = comp.getDistancia(centroides.get(i), centroides.get(j));
                mat.get(i).set(j, d);
                mat.get(j).set(i, d);
            }
        }
        return mat;
    }

    private static void initBounds(
            ArrayList<Float> upperBounds,
            ArrayList<ArrayList<Float>> lowerBounds,
            ArrayList<Integer> asignaciones,
            ArrayList<RespuestaEncuesta> centroides,
            ArrayList<RespuestaEncuesta> respuestas,
            Comparador comp,
            ArrayList<ArrayList<Float>> distanciasEntreCentroides,
            ArrayList<ArrayList<Float>> distanciasACentroides
    ) {
        int k = centroides.size();

        for (int i = 0; i < respuestas.size(); ++i) {
            RespuestaEncuesta respuesta = respuestas.get(i);
            int indiceMasCercano = 0;
            float distanciaMinima = comp.getDistancia(respuesta, centroides.get(0));
            distanciasACentroides.get(i).set(0, distanciaMinima);
            lowerBounds.get(i).set(0, distanciaMinima);

            for (int j = 1; j < k; ++j) {
                float mitadDistEntreCentroides =
                        0.5f * distanciasEntreCentroides.get(indiceMasCercano).get(j);

                if (distanciaMinima > mitadDistEntreCentroides) {
                    float distancia = comp.getDistancia(respuesta, centroides.get(j));
                    distanciasACentroides.get(i).set(j, distancia);
                    lowerBounds.get(i).set(j, distancia);
                    if (distancia < distanciaMinima) {
                        distanciaMinima = distancia;
                        indiceMasCercano = j;
                    }
                } else {
                    lowerBounds.get(i).set(j, mitadDistEntreCentroides);
                }
            }

            upperBounds.set(i, distanciaMinima);
            asignaciones.set(i, indiceMasCercano);
            distanciasACentroides.get(i).set(indiceMasCercano, distanciaMinima);
        }
    }

    private static ArrayList<Float> computarS(ArrayList<ArrayList<Float>> distMat) {
        ArrayList<Float> s = new ArrayList<>();
        int k = distMat.size();
        for (int i = 0; i < k; ++i) {
            float minDist = Float.POSITIVE_INFINITY;
            for (int j = 0; j < k; ++j) {
                if (i == j) continue;
                float d = distMat.get(i).get(j);
                if (d < minDist) minDist = d;
            }
            s.add((minDist == Float.POSITIVE_INFINITY) ? 0.0f : 0.5f * minDist);
        }
        return s;
    }

    private static ArrayList<Boolean> identificarRespuestasQueNoNecesitanRecalcular(
            ArrayList<RespuestaEncuesta> respuestas,
            ArrayList<Float> upperBounds,
            ArrayList<Float> S,
            ArrayList<Integer> asignaciones) {

        ArrayList<Boolean> result = new ArrayList<>();
        for (int i = 0; i < respuestas.size(); ++i) {
            result.add(upperBounds.get(i) <= S.get(asignaciones.get(i)));
        }
        return result;
    }

    private static void actualizarAsignaciones(
            ArrayList<RespuestaEncuesta> respuestas,
            ArrayList<RespuestaEncuesta> centroides,
            ArrayList<Float> upperBounds,
            ArrayList<ArrayList<Float>> lowerBounds,
            ArrayList<ArrayList<Float>> distanciasEntreCentroides,
            ArrayList<ArrayList<Float>> distanciasACentroides,
            ArrayList<Integer> asignaciones,
            ArrayList<Boolean> respuestasQueNoNecesitanRecalcular,
            Comparador comp
    ) {
        for (int i = 0; i < centroides.size(); ++i) {
            for (int j = 0; j < respuestas.size(); ++j) {

                if (i != asignaciones.get(j)
                        && upperBounds.get(j) > lowerBounds.get(j).get(i)
                        && upperBounds.get(j) > 0.5f * distanciasEntreCentroides.get(asignaciones.get(j)).get(i)) {

                    if (!respuestasQueNoNecesitanRecalcular.get(j)) {
                        float newDist = comp.getDistancia(
                                centroides.get(asignaciones.get(j)),
                                respuestas.get(j)
                        );
                        distanciasACentroides.get(j).set(asignaciones.get(j), newDist);
                    } else {
                        distanciasACentroides.get(j).set(asignaciones.get(j), upperBounds.get(j));
                    }

                    if (distanciasACentroides.get(j).get(asignaciones.get(j)) > lowerBounds.get(j).get(i)
                            || distanciasACentroides.get(j).get(asignaciones.get(j))
                            > 0.5f * distanciasEntreCentroides.get(asignaciones.get(j)).get(i)) {

                        float newDist = comp.getDistancia(respuestas.get(j), centroides.get(i));

                        if (newDist < distanciasACentroides.get(j).get(asignaciones.get(j))) {
                            distanciasACentroides.get(j).set(i, newDist);
                            asignaciones.set(j, i);
                            upperBounds.set(j, newDist);
                        }
                    }
                }
            }
        }
    }

    private static void recalcularCentroides(
            ArrayList<RespuestaEncuesta> nuevosCentroides,
            ArrayList<RespuestaEncuesta> centroides,
            ArrayList<RespuestaEncuesta> respuestas,
            ArrayList<Integer> asignaciones,
            Comparador comp
    ) {
        for (int i = 0; i < centroides.size(); ++i) {
            ArrayList<RespuestaEncuesta> clustering = new ArrayList<>();
            for (int j = 0; j < respuestas.size(); ++j) {
                if (asignaciones.get(j) == i) clustering.add(respuestas.get(j));
            }
            nuevosCentroides.set(i,
                    clustering.isEmpty() ? centroides.get(i) : comp.calcularNuevoCentroide(clustering));
        }
    }

    private static void convergenciaYActualizacion(
            ArrayList<RespuestaEncuesta> respuestas,
            ArrayList<RespuestaEncuesta> centroides,
            ArrayList<RespuestaEncuesta> nuevosCentroides,
            ArrayList<Float> upperBounds,
            ArrayList<ArrayList<Float>> lowerBounds,
            ArrayList<Integer> asignaciones,
            ArrayList<Boolean> respuestasQueNoNecesitanRecalcular,
            Comparador comp
    ) {
        ArrayList<Float> deltaCentroide = new ArrayList<>();
        for (int i = 0; i < centroides.size(); ++i) {
            deltaCentroide.add(comp.getDistancia(centroides.get(i), nuevosCentroides.get(i)));
        }

        for (int j = 0; j < respuestas.size(); ++j) {
            for (int i = 0; i < centroides.size(); ++i) {
                float m = Math.max(lowerBounds.get(j).get(i) - deltaCentroide.get(i), 0);
                lowerBounds.get(j).set(i, m);
            }
        }

        for (int j = 0; j < respuestas.size(); ++j) {
            upperBounds.set(j, upperBounds.get(j) + deltaCentroide.get(asignaciones.get(j)));
            respuestasQueNoNecesitanRecalcular.set(j, upperBounds.get(j) <= 0.5f * deltaCentroide.get(asignaciones.get(j)));
        }
    }

    private static ArrayList<ArrayList<RespuestaEncuesta>> generarClusters(
            ArrayList<RespuestaEncuesta> respuestas,
            ArrayList<Integer> asignaciones,
            int k
    ) {
        ArrayList<ArrayList<RespuestaEncuesta>> clusters = new ArrayList<>();
        for (int i = 0; i < k; ++i) clusters.add(new ArrayList<>());

        for (int j = 0; j < respuestas.size(); ++j) {
            clusters.get(asignaciones.get(j)).add(respuestas.get(j));
        }

        return clusters;
    }
}
