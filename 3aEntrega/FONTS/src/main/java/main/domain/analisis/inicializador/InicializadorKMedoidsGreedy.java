package main.domain.analisis.inicializador;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;
import main.domain.analisis.algoritmo.KMedoidsTipo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implementación de {@link InterfazInicializadorAlgoritmo} para el algoritmo {@link KMedoidsTipo}.
 * Este inicializador selecciona los medoids de forma iterativa, eligiendo en cada paso
 * el punto que minimiza la suma total de distancias al conjunto actual de medoids.
 * @author Sergi Malaguilla Bombin
 */

public class InicializadorKMedoidsGreedy implements InterfazInicializadorAlgoritmo<KMedoidsTipo> {
    private final Random rand = new Random(); //inicio random para selección del primer medoid
    public InicializadorKMedoidsGreedy() {}
    @Override
    /*
     Un inicializador greedy construye el conjunto de k medoids iterativamente: partiendo de un medoid inicial aleatorio,
     en cada paso se prueba como candidato cada punto que aún no es medoid y se calcula la suma total de distancias
     de todas las respuestas al conjunto actual de medoids si añadimos ese candidato. Se selecciona el candidato
     que minimiza esa suma total. Repetir hasta tener k.
    */
    /**
     * Genera una lista inicial de medoids utilizando un enfoque greedy.
     * @param respuestas La lista de respuestas de la encuesta.
     * @param k El número de medoids a seleccionar.
     * @param comp El comparador utilizado para calcular las distancias entre respuestas.
     * @return Una lista de respuestas seleccionadas como medoids iniciales.
     */
    public ArrayList<RespuestaEncuesta> generaIniciales(ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp) {
        ArrayList<RespuestaEncuesta> medoids = new ArrayList<>();

        if (respuestas.isEmpty() || k < 1)//error?
            return medoids;

        //selecciona el primer medoid aleatoriamente
        int randIndex = rand.nextInt(respuestas.size());
        medoids.add(respuestas.get(randIndex));

        //iterativamente selecciona los siguientes k-1 medoids
        while (medoids.size() < k) {
            RespuestaEncuesta mejorCandidato = null;
            float mejorSumaDistancias = Float.POSITIVE_INFINITY;

            //para cada punto que no sea ya un medoid
            for (RespuestaEncuesta candidato : respuestas) {
                //si ya es medoid, lo ignora, sino calcula la suma total de distancias al conjunto actual de medoids
                if (!medoids.contains(candidato)) {
                    float sumaDistancias = 0;
                    //calcula la distancia mínima de cada respuesta (a un medoid) del conjunto actual de medoids
                    for (RespuestaEncuesta respuesta : respuestas) {
                        float distMin = Float.POSITIVE_INFINITY;
                        for (RespuestaEncuesta medoid : medoids) {
                            float dist = comp.getDistancia(respuesta, medoid);
                            if (dist < distMin) distMin = dist;
                        }

                        //la distancia actual de la respuesta es la distancia mínima es el mínimo entre la
                        //distancia actual al medoid mas cercano y la distancia al candidato
                        float distCandidato = comp.getDistancia(respuesta, candidato);
                        float DistanciaRespuestaActual = Math.min(distMin, distCandidato);
                        //se suma dicha distancia a la suma total de distancias del actual candidato
                        sumaDistancias += DistanciaRespuestaActual;
                    }

                    //el mejor candidato es el que produce la menor suma total de distancias
                    if (sumaDistancias < mejorSumaDistancias) {
                        mejorSumaDistancias = sumaDistancias;
                        mejorCandidato = candidato;
                    }
                }
            }
            //añade el mejor candidato como nuevo medoid
            medoids.add(mejorCandidato);
        }
        return medoids;
    }
}