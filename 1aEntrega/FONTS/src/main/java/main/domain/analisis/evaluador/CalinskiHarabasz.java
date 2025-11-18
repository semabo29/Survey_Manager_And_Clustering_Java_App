package main.domain.analisis.evaluador;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;

import java.util.ArrayList;

public class CalinskiHarabasz implements InterfazEvaluadorCalidad {
    public CalinskiHarabasz() {}

    @Override
    public Float evaluarCalidad(ArrayList<ArrayList<RespuestaEncuesta>> clusters, Comparador comp) {
        clusters.removeIf(ArrayList::isEmpty);
        int k = clusters.size();
        int n = clusters.stream().mapToInt(ArrayList::size).sum();

        if (k < 2 || n <= k) return 0.f; // evitar divisiones inválidas

        // Calcular centroide global
        ArrayList<RespuestaEncuesta> todo = new ArrayList<>();
        for (ArrayList<RespuestaEncuesta> cluster : clusters) todo.addAll(cluster);

        RespuestaEncuesta cg = comp.calcularNuevoCentroide(todo);

        Float wcss = 0.f;
        Float bcss = 0.f;

        for (ArrayList<RespuestaEncuesta> cluster : clusters) {
            // Centroide del cluster

            RespuestaEncuesta ci = comp.calcularNuevoCentroide(cluster);

            // WCSS
            for (RespuestaEncuesta punto : cluster) {
                Float distancia = comp.getDistancia(punto, ci);
                wcss += distancia * distancia;
            }

            // BCSS
            Float distanciaCentroide = comp.getDistancia(ci, cg);
            bcss += cluster.size() * distanciaCentroide * distanciaCentroide;
        }

        if (wcss == 0.f) return 0.f;

        return (bcss / (k - 1)) / (wcss / (n - k));
    }
}
