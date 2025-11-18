package main.domain.analisis.evaluador;

import java.util.ArrayList;
import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;

public class Silhouette implements InterfazEvaluadorCalidad {
    public Silhouette() {}

    @Override
    public Float evaluarCalidad(ArrayList<ArrayList<RespuestaEncuesta>> clusters, Comparador comp) {
        int n = 0;
        Float sumaTotal = 0.f;

        for (int i = 0; i < clusters.size(); i++) {
            ArrayList<RespuestaEncuesta> cluster = clusters.get(i);

            // Calculamos solo si tiene mas de 1 elemento, si tiene solo 1 elemento s(i) valdra 0
            if (cluster.size() > 1) {
                for (int j = 0; j < cluster.size(); ++j) {
                    RespuestaEncuesta punto = cluster.get(j);

                    // a(i)
                    // Suma de las distancias entre puntos del mismo cluster
                    Float sumaCluster = 0.f;
                    for (int k = 0; k < cluster.size(); ++k) {
                        if (k != j) sumaCluster += comp.getDistancia(punto, cluster.get(k));
                    }
                    Float a = sumaCluster / (cluster.size() - 1);

                    // b(i)
                    // Calcula el minimo de las medias de las distancias entre el punto y los puntos de otros clusters
                    Float b = Float.MAX_VALUE;
                    for (int k = 0; k < clusters.size(); ++k) {
                        if (k != i) {
                            ArrayList<RespuestaEncuesta> clusterDif = clusters.get(k);
                            Float sumaClusterDif = 0.f;
                            for (int l = 0; l < clusterDif.size(); ++l)
                                sumaClusterDif += comp.getDistancia(punto, clusterDif.get(l));
                            // Division entre cero no por favor
                            if (clusterDif.size() > 0) {
                                b = Math.min(b, sumaClusterDif / clusterDif.size());
                            }
                        }
                    }

                    // Calculamos s(i) y lo sumamos al total
                    Float s = (b - a) / Math.max(a, b);
                    sumaTotal += s;
                }
            }
            n += cluster.size();
        }

        return sumaTotal / n;
    }
}
