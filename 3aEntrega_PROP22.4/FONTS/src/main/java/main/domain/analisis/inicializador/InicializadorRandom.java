package main.domain.analisis.inicializador;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;
import main.domain.analisis.algoritmo.KMeansTipo;
import main.domain.exceptions.KMenorQueUno;
import main.domain.exceptions.NoHayRespuestasParaAnalizar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Implementación de {@link InterfazInicializadorAlgoritmo} basado en selección aleatoria.
 * <p>
 * Selecciona {@code k} respuestas distintas con distribución uniforme
 * </p>
 * @author Javier Zhangpan
 */
public class InicializadorRandom implements InterfazInicializadorAlgoritmo<KMeansTipo> {

    private static final String textoNoHayRespuesta = "No hay respuestas que analizar";
    private static final String textoKMenor = "La K es menor que 1";

    /**
     * Constructora por defecto
     */
    public InicializadorRandom() {}

    private final Random rand = new Random();

    /**
     * Genera los centroides iniciales siguiendo una selección aleatoria
     *
     * @param respuestas Respuestas que se usarán.
     * @param k Número de centroides a generar.
     * @param comp Comparador (no utilizado en este inicializador).
     * @return Centroides iniciales seleccionados.
     * @throws NoHayRespuestasParaAnalizar Si {@code respuestas} es vacío.
     * @throws KMenorQueUno Si {@code k} es menor a 1.
     */
    @Override
    public ArrayList<RespuestaEncuesta> generaIniciales(ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp) {
        if(respuestas.isEmpty()) throw new NoHayRespuestasParaAnalizar(textoNoHayRespuesta);
        else if(k < 1) throw new KMenorQueUno(textoKMenor);

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
