package main.domain.analisis.algoritmo;

import main.domain.analisis.Comparador;
import main.domain.exceptions.KMenorQueUno;
import main.domain.exceptions.NoHayRespuestasParaAnalizar;

import main.domain.RespuestaEncuesta;
import main.domain.types.TDatos;
import main.domain.types.TDatosInteger;

import java.util.ArrayList;

public class KMeans implements InterfazAlgoritmo<KMeansTipo> {

    public KMeans() {}

    @Override
    public ArrayList<ArrayList<RespuestaEncuesta>> analizar(ArrayList<RespuestaEncuesta> centroides, ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp) {
        if(respuestas.isEmpty()) throw new NoHayRespuestasParaAnalizar("No hay respuestas que analizar");
        else if(k < 1) throw new KMenorQueUno("La K es menor que 1");

        boolean acaba = false;
        ArrayList<ArrayList<RespuestaEncuesta>> resultado = new ArrayList<>();

        while(!acaba) {
            //inicializa clusters
            ArrayList<ArrayList<RespuestaEncuesta>> clusters = new ArrayList<>(k);
            for (int i = 0; i < k; i++) {
                clusters.add(new ArrayList<>());
            }

            //encuentra centroide más cercano para esa respuesta
            //y añade esa respuesta al clustering asociado a ese centroide
            for (RespuestaEncuesta respuesta : respuestas) {
                int indiceMasCercano = 0;
                float distanciaMinima = comp.getDistancia(respuesta, centroides.getFirst());

                for (int j = 1; j < k; ++j) {
                    float distancia = comp.getDistancia(respuesta, centroides.get(j));
                    if (distancia < distanciaMinima) {
                        distanciaMinima = distancia;
                        indiceMasCercano = j;
                    }
                }

                clusters.get(indiceMasCercano).add(respuesta);
            }

            //recalcula centroides
            ArrayList<RespuestaEncuesta> nuevosCentroides = new ArrayList<>();
            for(int i = 0; i < k; ++i) {
                RespuestaEncuesta nuevoCentroide;
                if(clusters.get(i).isEmpty()) nuevoCentroide = centroides.get(i);
                else nuevoCentroide = comp.calcularNuevoCentroide(clusters.get(i));
                nuevosCentroides.add(nuevoCentroide);
            }

            //comprueba si el algoritmo a convergido
            if(nuevosCentroides.equals(centroides)) {
                acaba = true;
                resultado = clusters;
            }
            else centroides = nuevosCentroides;
        }
        return resultado;
    }
}
