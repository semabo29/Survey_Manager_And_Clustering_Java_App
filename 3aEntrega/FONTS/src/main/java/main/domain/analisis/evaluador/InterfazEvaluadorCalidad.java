package main.domain.analisis.evaluador;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;

import java.util.ArrayList;

/**
 * Interfaz con la operación para evaluar la calidad de un clustering.
 * @author Javier Zhangpan
 */
public interface InterfazEvaluadorCalidad {
    /**
     * Método que evalua la calidad de un clustering.
     * @param clusters Grupos de respuestas.
     * @param comp Comparador para calcular distancias.
     * @return Valor que representa la calidad del clustering.
     */
    public Float evaluarCalidad(ArrayList<ArrayList<RespuestaEncuesta>> clusters, Comparador comp);
}
