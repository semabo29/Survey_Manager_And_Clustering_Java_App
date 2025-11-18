package main.domain.analisis.evaluador;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;

import java.util.ArrayList;

public interface InterfazEvaluadorCalidad {
    public Float evaluarCalidad(ArrayList<ArrayList<RespuestaEncuesta>> clusters, Comparador comp);
}
