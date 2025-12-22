package main.domain.analisis.evaluador;

import java.util.ArrayList;
import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;

/**
 * Implementación de {@link InterfazEvaluadorCalidad} con el índice de Davies-Bouldin.
 * @author Yimin Jin
 */
public class DaviesBouldinen implements InterfazEvaluadorCalidad {
    /**
     * Constructor de la clase.
     */
    public DaviesBouldinen() {}

    /**
     * Evalua la calidad del clustering con el índice de Davies-Bouldin.
     * @param clusters Grupos de respuestas.
     * @param comp Comparador para calcular distancias.
     * @return Un valor comenzando desde 0 y sin límite superior.
     * Los valores más cercanos al 0 indican un mejor agrupamiento.
     */
    @Override
    public Float evaluarCalidad(ArrayList<ArrayList<RespuestaEncuesta>> clusters, Comparador comp) {
        clusters.removeIf(ArrayList::isEmpty);
        int k = clusters.size();
        if (k < 2) return 0.f;

        // Calcular centroides
        ArrayList<RespuestaEncuesta> centroides = new ArrayList<>();

        for (ArrayList<RespuestaEncuesta> cluster : clusters) {
            RespuestaEncuesta c = comp.calcularNuevoCentroide(cluster);
            centroides.add(c);
        }

        // Calcular dispersion de cada cluster
        ArrayList<Float> dispersion = calcDispersion(clusters, centroides, k, comp);

        // Calcular Rij y sumarlos
        Float sumaTotal = calcSumaTotal(centroides, dispersion, k, comp);

        return sumaTotal / k;
    }

    private ArrayList<Float> calcDispersion(ArrayList<ArrayList<RespuestaEncuesta>> clusters,
                                            ArrayList<RespuestaEncuesta> centroides, int k, Comparador comp) {
        ArrayList<Float> dispersion = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            ArrayList<RespuestaEncuesta> cluster = clusters.get(i);
            if (cluster.isEmpty()) dispersion.add(0.f);
            else {
                RespuestaEncuesta centroide = centroides.get(i);
                Float suma = 0.f;
                for (RespuestaEncuesta punto : cluster) suma += comp.getDistancia(punto, centroide);
                dispersion.add(suma / cluster.size());
            }
        }
        return dispersion;
    }

    private Float calcSumaTotal(ArrayList<RespuestaEncuesta> centroides, ArrayList<Float> dispersion, int k, Comparador comp) {
        Float sumaTotal = 0.f;
        for (int i = 0; i < k; i++) {
            Float max = Float.NEGATIVE_INFINITY;
            for (int j = 0; j < k; j++) {
                if (j != i) {
                    Float mij = comp.getDistancia(centroides.get(i), centroides.get(j));
                    if (mij > 0.f) {
                        Float rij = (dispersion.get(i) + dispersion.get(j)) / mij;
                        max = Math.max(max, rij);
                    }
                }
            }
            sumaTotal += max;
        }
        return sumaTotal;
    }
}