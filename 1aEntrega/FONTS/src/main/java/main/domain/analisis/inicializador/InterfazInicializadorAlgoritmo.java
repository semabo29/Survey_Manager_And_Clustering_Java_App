package main.domain.analisis.inicializador;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.Comparador;
import main.domain.analisis.algoritmo.AlgoritmoTipo;
import java.util.ArrayList;

public interface InterfazInicializadorAlgoritmo <T extends AlgoritmoTipo> {
    ArrayList<RespuestaEncuesta> generaIniciales(ArrayList<RespuestaEncuesta> respuestas, Integer k, Comparador comp);
}
