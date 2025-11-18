package main.domain.analisis.algoritmo;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;

import java.util.ArrayList;

public class KMedoids implements InterfazAlgoritmo<KMedoidsTipo> {

    public KMedoids() {}

    private float costeTotal (ArrayList<RespuestaEncuesta> medoids, ArrayList<RespuestaEncuesta> respuestas, Comparador comp) {
        float coste = 0.0f;
        for (RespuestaEncuesta respuesta : respuestas) {
            // Para cada respuesta, encontrar la distancia al medoid más cercano
            float distanciaMinima = Float.MAX_VALUE;
            for (RespuestaEncuesta medoid : medoids) {
                float distancia = comp.getDistancia(respuesta, medoid);
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                }
            }
            // Sumar la distancia mínima calculada al coste total
            coste += distanciaMinima;
        }
        return coste;
    }

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

    @Override
    public ArrayList<ArrayList<RespuestaEncuesta>> analizar(ArrayList<RespuestaEncuesta> medoids, ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp) {
        // Partitioning Around Medoids (PAM)

        // Inicializaciones
        boolean cambiado = true;
        float mejorCoste = costeTotal(medoids, respuestas, comp);

        while (cambiado) {
            cambiado = false;

            for (RespuestaEncuesta candidato: respuestas) {
                // Si el candidato es un medoid actual, saltar
                if (!medoids.contains(candidato)) {
                    for (RespuestaEncuesta medoidActual: medoids) {
                        // Conseguir el indice del medoid actual
                        int idx = medoids.indexOf(medoidActual);
                        // Intercambiar medoid actual con el candidato
                        medoids.set(idx, candidato);
                        // Calcular el nuevo coste total
                        float nuevoCoste = costeTotal(medoids, respuestas, comp);

                        // Si el nuevo coste es mejor, mantener el cambio
                        if (nuevoCoste < mejorCoste) {
                            mejorCoste = nuevoCoste;
                            cambiado = true;
                        } else {
                            // Revertir el cambio en caso que no lo sea
                            medoids.set(idx, medoidActual);
                        }
                    }
                }
            }
        }

        // Construir y devolver los clusters finales a partir de los medoids refinados
        return construirClusters(medoids, respuestas, comp);
    }
}
