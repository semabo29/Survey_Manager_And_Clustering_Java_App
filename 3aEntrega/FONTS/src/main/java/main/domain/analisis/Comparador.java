package main.domain.analisis;

import main.domain.RespuestaEncuesta;
import main.domain.exceptions.*;
import main.domain.types.TDatos;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Clase con las operaciones de calculo de distancias entre respuestas.
 * @author Yimin Jin
 */
public class Comparador {
    /**
     * Constructor de la clase.
     */
    public Comparador() {}

    /**
     * Operación que calcula la distancia entre dos respuestas.
     * @param r1 Una respuesta a una encuesta.
     * @param r2 Una respuesta a la misma encuesta que r1.
     * @return La distancia entre las dos respuestas. Esta distancia está normalizada.
     */
    public Float getDistancia(RespuestaEncuesta r1, RespuestaEncuesta r2) {
        Float suma = 0.f;
        TreeMap<Integer, TDatos> datosR1 = r1.getDatosRespuesta();
        TreeMap<Integer, TDatos> datosR2 = r2.getDatosRespuesta();
        for (int i = 1; i <= datosR1.size(); i++) {
            float dist = 0.0f;
            try {
                dist = datosR1.get(i).calcularDistancia(datosR2.get(i));
            }
            catch (AmbosTextosSonVacios | NumeroModalidadesMenorQueDos | NumMaxIgualANumMin |
                   UnionEntreConjuntosDeOpcionesVacio e) {
                dist = 0.0f;
            }
            suma += dist;
        }
        return suma / datosR1.size();
    }

    /**
     * Operación que calcula el centroide de una lista de respuestas.
     * @param cluster Lista de respuestas a una misma encuesta.
     * @return Una respuesta que solo contiene los centroides de cada pregunta de la lista de respuestas.
     */
    public RespuestaEncuesta calcularNuevoCentroide(ArrayList<RespuestaEncuesta> cluster) {
        //inicializa matriz que guarda datos
        ArrayList<ArrayList<TDatos>> aux = new ArrayList<>();

        int size = cluster.getFirst().getDatos().size();
        for (int i = 0; i < size; i++) {
            aux.add(new ArrayList<>());
        }
        //rellena matriz con datos
        for(int i = 0; i < cluster.size(); ++i) {
            ArrayList<TDatos> datos = cluster.get(i).getDatos();
            for (int j = 0; j < size; ++j) {
                aux.get(j).add(datos.get(j));
            }
        }

        RespuestaEncuesta centro = new RespuestaEncuesta("", "", "");

        for(int i = 0; i < aux.size(); ++i) {
            ArrayList<TDatos> datos = aux.get(i);
            TDatos primero = datos.getFirst();
            TDatos d = primero.getComponenteCentroide(datos);
            centro.addRespuesta(i + 1, d);
        }

        return centro;
    }
}
