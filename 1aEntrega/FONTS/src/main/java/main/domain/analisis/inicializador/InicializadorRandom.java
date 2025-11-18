package main.domain.analisis.inicializador;

import main.domain.analisis.Comparador;
import main.domain.analisis.algoritmo.KMeansTipo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import main.domain.RespuestaEncuesta;
import main.domain.exceptions.KMenorQueUno;
import main.domain.exceptions.NoHayRespuestasParaAnalizar;

public class InicializadorRandom implements InterfazInicializadorAlgoritmo<KMeansTipo> {

    public InicializadorRandom() {}

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
        //set para evitar duplicados
        Set<Integer> indices = new HashSet<>();

        //genera una lista de indices de forma aleatoria
        while(indices.size() < k) {
            indices.add(rand.nextInt(respuestas.size()));
        }

        //asigna centroides en base a las respuestas escogidas
        for(Integer i : indices) {
            centroides.add(respuestas.get(i));
        }

        return centroides;
    }
}
