package main.domain.analisis.inicializador;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;
import main.domain.analisis.algoritmo.KMeansTipo;
import main.domain.exceptions.KMenorQueUno;
import main.domain.exceptions.NoHayRespuestasParaAnalizar;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implementación de {@link InterfazInicializadorAlgoritmo} basado en el algoritmo K-Means++.
 * <p>
 * Selecciona los centroides iniciales de forma estocástica,
 * favoreciendo respuestas alejadas de los centroides ya seleccionados.
 * </p>
 * @author Javier Zhangpan
 */
public class InicializadorKMeansPlusPlus implements InterfazInicializadorAlgoritmo<KMeansTipo> {

    private static final String textoNoHayRespuesta = "No hay respuestas que analizar";
    private static final String textoKMenor = "La K es menor que 1";

    /**
     * Constructora por defecto
     */
    public InicializadorKMeansPlusPlus() {}

    private final Random rand = new Random();

    /**
     * Genera los centroides iniciales siguiendo el algoritmo de K-Means++.
     *
     * @param respuestas Respuestas que se usarán para generar los centroides.
     * @param k Número de centroides a generar.
     * @param comp Comparador a usar para calcular distancias.
     * @return Centroides iniciales seleccionados.
     * @throws NoHayRespuestasParaAnalizar Si {@code respuestas} es vacío.
     * @throws KMenorQueUno Si {@code k} es menor a 1.
     */
    @Override
    public ArrayList<RespuestaEncuesta> generaIniciales(
            ArrayList<RespuestaEncuesta> respuestas,
            Integer k,
            Comparador comp
    ) {
        validarParametros(respuestas, k);

        ArrayList<RespuestaEncuesta> centroides = new ArrayList<>();
        seleccionarPrimerCentroide(respuestas, centroides);

        while (centroides.size() < k) {
            ArrayList<Float> distanciasCuadradas =
                    calcularDistanciasCuadradas(respuestas, centroides, comp);

            centroides.add(
                    seleccionarSiguienteCentroide(respuestas, distanciasCuadradas)
            );
        }

        return centroides;
    }

    private void validarParametros(ArrayList<RespuestaEncuesta> respuestas, Integer k) {
        if (respuestas.isEmpty())
            throw new NoHayRespuestasParaAnalizar(textoNoHayRespuesta);
        if (k < 1)
            throw new KMenorQueUno(textoKMenor);
    }

    private void seleccionarPrimerCentroide(
            ArrayList<RespuestaEncuesta> respuestas,
            ArrayList<RespuestaEncuesta> centroides
    ) {
        //selecciona primer inicial de forma aleatoria
        int indice = rand.nextInt(respuestas.size());
        centroides.add(respuestas.get(indice));
    }

    private ArrayList<Float> calcularDistanciasCuadradas(
            ArrayList<RespuestaEncuesta> respuestas,
            ArrayList<RespuestaEncuesta> centroides,
            Comparador comp
    ) {
        ArrayList<Float> distanciasCuadradas = new ArrayList<>();

        for (RespuestaEncuesta respuesta : respuestas) {
            //distancia del resto de respuestas al primer inicial
            // distancia consigo mismo es de 0, por lo que nunca se escoge 2 veces el mismo
            Float distanciaMinima =
                    comp.getDistancia(respuesta, centroides.getFirst());

            for (int j = 1; j < centroides.size(); ++j) {
                Float distancia = comp.getDistancia(respuesta, centroides.get(j));
                if (distancia < distanciaMinima) distanciaMinima = distancia;
            }

            //para cada respuesta me guardo su distancia mínima cuadrada para usar luego
            distanciasCuadradas.add(distanciaMinima * distanciaMinima);
        }

        return distanciasCuadradas;
    }

    private RespuestaEncuesta seleccionarSiguienteCentroide(
            ArrayList<RespuestaEncuesta> respuestas,
            ArrayList<Float> distanciasCuadradas
    ) {
        //sumatorio de las distancias
        float total = 0;
        for (Float d : distanciasCuadradas) total += d;

        //se usa un umbral aleatorio de 0 (inclusivo) al total (exclusivo)
        float umbral = (total == 0) ? 0 : rand.nextFloat(total);
        float acumulado = 0;

        //se añade a centroides la primera respuesta que tenga un sumatorio acumulado
        //por encima del umbral
        for (int i = 0; i < respuestas.size(); ++i) {
            acumulado += distanciasCuadradas.get(i);
            if (acumulado >= umbral) {
                return respuestas.get(i);
            }
        }
        //por si acaso no selecciona ninguna
        return respuestas.getLast();
    }
}