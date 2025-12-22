package main.domain.analisis.algoritmo;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;

import java.util.ArrayList;

/**
 * Implementación de {@link InterfazAlgoritmo} con el algoritmo K-Medoids para el análisis de respuestas.
 * @author Hadeer Abbas Khalil Wysocka
 */
public class KMedoids implements InterfazAlgoritmo<KMedoidsTipo> {

    /**
     * Constructor por defecto de la clase KMedoids.
     */
    public KMedoids() {}

    private ArrayList<ArrayList<RespuestaEncuesta>> construirClusters (ArrayList<RespuestaEncuesta> medoids, ArrayList<RespuestaEncuesta> respuestas, Comparador comp) {
        // Inicializar lista de clusters
        ArrayList<ArrayList<RespuestaEncuesta>> clusters = new ArrayList<>();
        for (int i = 0; i < medoids.size(); i++) clusters.add(new ArrayList<>());

        for (RespuestaEncuesta respuesta: respuestas) {
            // Para cada respuesta, encontrar el indice del medoid más cercano, que corresponde al cluster que se le asignara
            int indiceMedoidMasCercano = 0;
            float distanciaMinima = Float.MAX_VALUE;

            for (RespuestaEncuesta medoid: medoids) {
                int idx = medoids.indexOf(medoid);
                float distancia = comp.getDistancia(respuesta, medoid);
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    indiceMedoidMasCercano = idx;
                }
            }

            clusters.get(indiceMedoidMasCercano).add(respuesta);
        }

        return clusters;
    }

    private void actualizaCercanos (ArrayList<RespuestaEncuesta> respuestas, ArrayList<RespuestaEncuesta> medoids, Comparador comp, int[] indicesMedoidMasCercano, float[] distanciaMasCercana, float[] segundaDistanciaMasCercana, int n) {
        // Este helper simplemente actualiza los arrays de distancias mas cercanas y segundas distancias mas cercanas, implementacion "trivial"
        for (int i = 0; i < n; i++) {
            distanciaMasCercana[i] = Float.MAX_VALUE;
            segundaDistanciaMasCercana[i] = Float.MAX_VALUE;

            RespuestaEncuesta r = respuestas.get(i);
            for (RespuestaEncuesta m : medoids) {
                float distancia = comp.getDistancia(r, m);
                if (distancia < distanciaMasCercana[i]) {
                    segundaDistanciaMasCercana[i] = distanciaMasCercana[i];
                    distanciaMasCercana[i] = distancia;
                    indicesMedoidMasCercano[i] = medoids.indexOf(m);
                } else if (distancia < segundaDistanciaMasCercana[i]) {
                    segundaDistanciaMasCercana[i] = distancia;
                }
            }
        }
    }

    /**
     * Analiza las respuestas utilizando el algoritmo K-Medoids.
     * Utiliza la versión rápida Partitioning Around Medoids (FasterPAM) para agrupar respuestas en clusters basados en medoids.
     * Su complejidad temporal es de O(nk) en el caso promedio y O(n²k²) en el peor caso.
     * @param medoids Conjunto de medoids del inicializador Greedy en ArrayList.
     * @param respuestas Conjunto de respuestas a analizar en ArrayList.
     * @param k Número de clusters a formar.
     * @param comp Comparador utilizado para calcular distancias entre respuestas.
     * @return Lista de clusters, donde cada cluster es una lista de respuestas asignadas a ese cluster en un ArrayList de ArrayList.
     */
    @Override
    public ArrayList<ArrayList<RespuestaEncuesta>> analizar(ArrayList<RespuestaEncuesta> medoids, ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp) {
        // Partitioning Around Medoids version rapida (FasterPAM)

        // Inicializaciones
        int n = respuestas.size();
        int[] indicesMedoidMasCercano = new int[n];
        float[] distanciaMasCercana = new float[n];
        float[] segundaDistanciaMasCercana = new float[n];
        actualizaCercanos(respuestas, medoids, comp, indicesMedoidMasCercano, distanciaMasCercana, segundaDistanciaMasCercana, n);
        boolean cambiado = true;

        // Basicamente la idea de esta optimizacion es en vez de recalcular el coste total todo el rato,
        // mantenemos "caches" de las distancias mas cercanas y segundas distancias mas cercanas cambiando
        // el coste de forma incremental.

        while (cambiado) {
            cambiado = false;

            int mejorIndiceMedoid = -1;
            int mejorIndiceCandidato = -1;
            float mejoraActual = 0.0f;

            // En esta version se usan indices ya que el caching es de indices
            for (int r = 0; r < n; r++) {
                RespuestaEncuesta candidato = respuestas.get(r);
                if (medoids.contains(candidato)) continue; // Saltar si r ya es un medoid

                // Intenta intercambiar cada medoid con el candidato
                for (int m = 0; m < medoids.size(); m++) {
                    float mejora = 0.0f;

                    for (int i = 0; i < n; i++) {
                        float distanciaVieja = distanciaMasCercana[i];
                        float distanciaACandidato = comp.getDistancia(respuestas.get(i), candidato);

                        float nuevaDistancia;
                        if (indicesMedoidMasCercano[i] == m) {
                            nuevaDistancia = Math.min(distanciaACandidato, segundaDistanciaMasCercana[i]);
                            mejora += (distanciaVieja - nuevaDistancia);
                        } else {
                            nuevaDistancia = Math.min(distanciaVieja, distanciaACandidato);
                            mejora += (distanciaVieja - nuevaDistancia);
                        }
                    }

                    if (mejora > mejoraActual) {
                        mejoraActual = mejora;
                        mejorIndiceMedoid = m;
                        mejorIndiceCandidato = r;
                    }
                }
            }

            if (mejoraActual > 0.0f) {
                medoids.set(mejorIndiceMedoid, respuestas.get(mejorIndiceCandidato));
                // Recomputar los caches de distancias
                actualizaCercanos(respuestas, medoids, comp, indicesMedoidMasCercano, distanciaMasCercana, segundaDistanciaMasCercana, n);
                cambiado = true;
            }
        }

        // Construir y devolver los clusters finales a partir de los medoids refinados
        return construirClusters(medoids, respuestas, comp);
    }
}
