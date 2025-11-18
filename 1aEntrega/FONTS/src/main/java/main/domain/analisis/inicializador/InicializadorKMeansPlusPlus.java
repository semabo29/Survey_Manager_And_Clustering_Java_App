package main.domain.analisis.inicializador;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;
import main.domain.analisis.algoritmo.KMeansTipo;
import main.domain.exceptions.KMenorQueUno;
import main.domain.exceptions.NoHayRespuestasParaAnalizar;

import java.util.ArrayList;
import java.util.Random;

public class InicializadorKMeansPlusPlus implements InterfazInicializadorAlgoritmo<KMeansTipo> {

    public InicializadorKMeansPlusPlus() {}

    //podríamos poner opción de seleccionar seed
    private final Random rand = new Random();

    //dada un array de respuestas y una k, selecciona de forma aleatoria siguiendo una distribución normal
    //k respuestas para hacer de centroides iniciales
    //funciona también para k-medoids, ya que selecciona de entre las respuestas
    @Override
    public ArrayList<RespuestaEncuesta> generaIniciales(ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp) {
        if(respuestas.isEmpty()) throw new NoHayRespuestasParaAnalizar("No hay respuestas que analizar");
        else if(k < 1) throw new KMenorQueUno("La K es menor que 1");

        ArrayList<RespuestaEncuesta> centroides = new ArrayList<>();

        //selecciona primer inicial de forma aleatoria
        int primerIndice = rand.nextInt(respuestas.size());
        centroides.add(respuestas.get(primerIndice));

        while(centroides.size() < k) {
            ArrayList<Float> distanciasCuadradas = new ArrayList<>();

            for (RespuestaEncuesta respuesta : respuestas) {
                //distancia del resto de respuestas al primer inicial
                //distancia consigo mismo es de 0, por lo que nunca se escoge 2 veces el mismo
                Float distanciaMinima = comp.getDistancia(respuesta, centroides.getFirst());
                for (int j = 1; j < centroides.size(); ++j) {
                    Float distancia = comp.getDistancia(respuesta, centroides.get(j));
                    if (distancia < distanciaMinima) distanciaMinima = distancia;
                }

                //para cada respuesta me guardo su distancia mínima cuadrada para usar luego
                distanciasCuadradas.add(distanciaMinima * distanciaMinima);
            }

            //sumatorio de las distancias
            float total = 0;
            for(Float d : distanciasCuadradas) total += d;

            //se usa un umbral aleatorio de 0 al total
            float umbral;
            if(total == 0) umbral = 0;
            else umbral = rand.nextFloat(total);
            float acumulado = 0;

            //se añade a centroides la primera respuesta que tenga un sumatorio acumulado
            //por encima del umbral
            for(int i = 0; i < respuestas.size(); ++i) {
                acumulado += distanciasCuadradas.get(i);
                if(acumulado >= umbral) {
                    centroides.add(respuestas.get(i));
                    break;
                }
            }
        }
        return centroides;
    }
}
