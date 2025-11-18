package main.domain.analisis;

import main.domain.RespuestaEncuesta;
import main.domain.exceptions.*;
import main.domain.types.TDatos;

import java.util.ArrayList;
import java.util.TreeMap;

public class Comparador {
    public Comparador() {}

    public Float getDistancia(RespuestaEncuesta r1, RespuestaEncuesta r2) {
        Float suma = 0.f;
        TreeMap<Integer, TDatos> datosR1 = r1.getIdRespuesta();
        TreeMap<Integer, TDatos> datosR2 = r2.getIdRespuesta();
        for (int i = 1; i <= datosR1.size(); i++) {
            float dist = 0.0f;
            try {
                dist = datosR1.get(i).calcularDistancia(datosR2.get(i));
            }
            catch (AmbosTextosSonVacios | NumeroModalidadesMenorQueDos | NumMaxIgualANumMin |
                   UnionEntreConjuntosDeOpcionesVacio e) {
                dist = 0.0f;
            }
            catch (DistanciaEntreIntegersDemasiadoGrande e) {
                dist = 1.0f;
            }
            suma += dist;
            //System.out.println(dist);
        }
        return suma / datosR1.size();
    }

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
