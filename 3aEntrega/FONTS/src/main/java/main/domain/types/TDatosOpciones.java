package main.domain.types;

import main.domain.exceptions.NumeroModalidadesMenorQueDos;
import main.domain.exceptions.UnionEntreConjuntosDeOpcionesVacio;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Representa un dato cualitativo.
 * Puede ser ordinal (si existe orden entre opciones) o no ordinal.
 * <p>
 * Implementa {@link TDatos}.
 * </p>
 * @author Javier Zhangpan
 */
public class TDatosOpciones implements TDatos{
    //índices de opciones elegidas
    ArrayList<Integer> idOpciones;
    //si existe un orden entre las opciones
    boolean orden;
    //número total de opciones posibles
    int numOpciones;


    /**
     * Constructora por defecto.
     */
    public TDatosOpciones() {
        this.idOpciones = new ArrayList<>();
        this.orden = false;
        this.numOpciones = 0;
    }

    /**
     * Constructora con conjunto de opciones.
     *
     * @param idOp opciones seleccionadas
     */
    public TDatosOpciones(ArrayList<Integer> idOp) {
        this.idOpciones = idOp;
        this.orden = false;
        this.numOpciones = 0;
    }

    /**
     * Constructora completa.
     *
     * @param op opciones seleccionadas
     * @param ord si las opciones tienen orden
     * @param numOp número total de opciones posibles
     */
    public TDatosOpciones(ArrayList<Integer> op, boolean ord, int numOp) {
        this.idOpciones = op;
        this.orden = ord;
        this.numOpciones = numOp;
    }

    /**
     * Devuelve el conjunto de opciones seleccionadas en la respuesta.
     * @return Valor numérico de la respuesta.
     */
    public ArrayList<Integer> getIdOpciones() {
        return idOpciones;
    }

    /**
     * Devuelve si existe un orden en el conjunto de opciones seleccionadas en la respuesta.
     * @return Si existe un orden en el conjunto de opciones seleccionadas en la respuesta.
     */
    public boolean isOrden() {
        return orden;
    }

    /**
     * Devuelve el número total de opciones que puede alcanzar la respuesta.
     * @return Número total de opciones que puede alcanzar la respuesta.
     */
    public int getNumOpciones() {
        return numOpciones;
    }

    /**
     * Cambia el conjunto de opciones seleccionadas en la respuesta.
     * @param idOpciones conjunto de opciones seleccionadas en la respuesta.
     */
    public void setIdOpciones(ArrayList<Integer> idOpciones) {
        this.idOpciones = idOpciones;
    }

    /**
     * Cambia si existe un orden el conjunto de opciones seleccionadas en la respuesta.
     * @param orden si existe un orden el conjunto de opciones seleccionadas en la respuesta.
     */
    public void setOrden(boolean orden) {
        this.orden = orden;
    }

    /**
     * Cambia el número total de opciones que puede alcanzar la respuesta.
     * @param numOpciones número total de opciones que puede alcanzar la respuesta.
     */
    public void setNumOpciones(int numOpciones) {
        this.numOpciones = numOpciones;
    }

    /**
     * Calcula la distancia normalizada entre este dato y otro {@link TDatosOpciones}.
     *
     * @param t otro dato
     * @return distancia normalizada entre los datos
     */
    @Override
    public Float calcularDistancia(TDatos t) {
        TDatosOpciones aux = (TDatosOpciones) t;
        if(aux.getIdOpciones().size() == 1 && this.idOpciones.size() == 1) {
            if(orden) return cualitativaOrdinalDistancia(idOpciones.getFirst(), aux.getIdOpciones().getFirst(), numOpciones);
            else return cualitativaNoOrdinalDistancia(idOpciones.getFirst(), aux.getIdOpciones().getFirst());
        }
        else {
            return cualitativaConjuntoDistancia(idOpciones, aux.getIdOpciones());
        }
    }

    /**
     * Calcula el componente de tipo {@link TDatosOpciones} de centroide de un cluster.
     *
     * @param datos conjunto de datos del cluster de tipo {@link TDatosOpciones}
     * @return nuevo {@link TDatosOpciones} representando la media (moda)
     */
    public TDatos getComponenteCentroide(ArrayList<TDatos> datos) {
        if(idOpciones.size() == 1) {
            return getModa(datos);
        }
        else {
            TreeMap<Integer, Integer> diccionario = new TreeMap<>();
            for(TDatos d : datos) {
                TDatosOpciones dato = (TDatosOpciones) d;
                for(Integer i : dato.getIdOpciones()) {
                    diccionario.put(i, diccionario.getOrDefault(i, 0) + 1);
                }
            }


            if (diccionario.isEmpty()) {
                return new TDatosOpciones(new ArrayList<>(), orden, numOpciones);
            }

            Integer maxFreqIdx = 0;
            int max = 0;
            for(Map.Entry<Integer,Integer> entry : diccionario.entrySet()) {
                if(entry.getValue() > max) {
                    max = entry.getValue();
                    maxFreqIdx = entry.getKey();
                }
            }

            ArrayList<Integer> opc = new ArrayList<>();
            opc.addFirst(maxFreqIdx);

            return new TDatosOpciones(opc,orden,numOpciones);
        }
    }

    private TDatos getModa(ArrayList<TDatos> datos) {
        TreeMap<Integer, Integer> diccionario = new TreeMap<>();

        for(TDatos d : datos) {
            TDatosOpciones dato = (TDatosOpciones) d;
            if(dato.getIdOpciones().isEmpty()) continue;

            Integer idx = dato.getIdOpciones().getFirst();
            if(diccionario.containsKey(idx)) diccionario.replace(idx, diccionario.get(idx), diccionario.get(idx)+1);
            else diccionario.put(idx,1);
        }

        if(diccionario.isEmpty()) return new TDatosOpciones(new ArrayList<>(), orden, numOpciones);

        int moda = 0;
        int max = 0;
        for(Map.Entry<Integer,Integer> entry : diccionario.entrySet()) {
            if(entry.getValue() > max) {
                max = entry.getValue();
                moda = entry.getKey();
            }
        }

        ArrayList<Integer> opc = new ArrayList<>();
        opc.addFirst(moda);
        return new TDatosOpciones(opc,orden,numOpciones);
    }

    private static Float cualitativaOrdinalDistancia(int numeralModalidadA, int numeralModalidadB, int numModalidades) {
        if(numModalidades < 2) throw new NumeroModalidadesMenorQueDos("Hay menos de dos modalidades totales, por lo que se divide por cero");

        float diffAB = Math.abs(numeralModalidadA-numeralModalidadB);
        return diffAB /(numModalidades-1);
    }

    private static Float cualitativaNoOrdinalDistancia(Integer modalidadA, Integer modalidadB) {
        float retval = 1;
        if(modalidadA.equals(modalidadB)) retval = 0;
        return retval;
    }

    private static Float cualitativaConjuntoDistancia(ArrayList<Integer> conjuntoA, ArrayList<Integer> conjuntoB) {
        float interseccion = interseccion(conjuntoA,conjuntoB).size();
        float union = union(conjuntoA, conjuntoB).size();

        if(union == 0.0) throw new UnionEntreConjuntosDeOpcionesVacio("Ambas respuestas no contienen ninguna opción");

        return 1 - (interseccion/union);
    }

    private static TreeSet<Integer> interseccion(ArrayList<Integer> conjuntoA, ArrayList<Integer> conjuntoB) {
        TreeSet<Integer> interseccion = new TreeSet<>(conjuntoA);
        interseccion.retainAll(conjuntoB);
        return interseccion;
    }

    private static TreeSet<Integer> union(ArrayList<Integer> conjuntoA, ArrayList<Integer> conjuntoB) {
        TreeSet<Integer> union = new TreeSet<>(conjuntoA);
        union.addAll(conjuntoB);
        return union;
    }

    /**
     * Comprueba si otro {@link TDatosOpciones} tiene las mismas opciones seleccionadas.
     * La función asume que pertenecen a la misma pregunta.
     *
     * @return {@code true} si tienen las mismas opciones seleccionadas,
     * {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TDatosOpciones other = (TDatosOpciones) obj;

        return  idOpciones.equals(other.idOpciones);
    }

    /**
     * Devuelve la representación en texto del contenido del dato.
     *
     * @return contenido del dato
     */
    @Override
    public String getContenido() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < idOpciones.size(); i++) {
            str.append(idOpciones.get(i));
            if (i != idOpciones.size() - 1) {
                str.append(" ");
            }
        }
        return str.toString();
    }
}
