package main.domain.analisis.inicializador;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;
import main.domain.analisis.algoritmo.AlgoritmoTipo;
import java.util.ArrayList;

/**
 * Interfaz genérica para inicializadores de algoritmos de clustering.
 *
 * @param <T> Tipo de algoritmo al que da soporte el inicializador, debe implementar {@link AlgoritmoTipo}.
 * @author Javier Zhangpan
 */
public interface InterfazInicializadorAlgoritmo <T extends AlgoritmoTipo> {
    /**
     * Genera los centroides iniciales a partir de un conjunto de respuestas.
     *
     * @param respuestas Respuestas que se usarán.
     * @param k Número de centroides a generar.
     * @param comp Comparador a usar para calcular distancias
     * @return Centroides iniciales.
     */
    ArrayList<RespuestaEncuesta> generaIniciales(ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp);
}
