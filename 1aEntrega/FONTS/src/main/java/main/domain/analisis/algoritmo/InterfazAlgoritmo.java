package main.domain.analisis.algoritmo;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;

import java.util.ArrayList;

public interface InterfazAlgoritmo <T extends AlgoritmoTipo> {
    ArrayList<ArrayList<RespuestaEncuesta>> analizar(ArrayList<RespuestaEncuesta> centroides, ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp);
}
