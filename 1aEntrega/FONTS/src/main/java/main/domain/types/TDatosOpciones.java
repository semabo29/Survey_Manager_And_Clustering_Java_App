package main.domain.types;

import main.domain.exceptions.*;

import java.util.*;

public class TDatosOpciones implements TDatos{
    //índices de opciones elegidas
    ArrayList<Integer> idOpciones;
    //si existe un orden entre las opciones
    boolean orden;
    //número total de opciones posibles
    int numOpciones;

    public TDatosOpciones() {
        this.idOpciones = new ArrayList<>();
        this.orden = false;
        this.numOpciones = 0;
    }

    public TDatosOpciones(ArrayList<Integer> idOp) {
        this.idOpciones = idOp;
        this.orden = false;
        this.numOpciones = 0;
    }

    public TDatosOpciones(ArrayList<Integer> op, boolean ord, int numOp) {
        this.idOpciones = op;
        this.orden = ord;
        this.numOpciones = numOp;
    }

    public ArrayList<Integer> getIdOpciones() {
        return idOpciones;
    }
    public boolean isOrden() {
        return orden;
    }
    public int getNumOpciones() {
        return numOpciones;
    }

    public void setIdOpciones(ArrayList<Integer> idOpciones) {
        this.idOpciones = idOpciones;
    }
    public void setOrden(boolean orden) {
        this.orden = orden;
    }
    public void setNumOpciones(int numOpciones) {
        this.numOpciones = numOpciones;
    }

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

    public TDatos getComponenteCentroide(ArrayList<TDatos> datos) {
        if(idOpciones.size() == 1) {
            return getModa(datos);
        }
        else {
            TreeMap<Integer, Integer> diccionario = new TreeMap<>();
            for(TDatos d : datos) {
                TDatosOpciones dato = (TDatosOpciones) d;
                for(Integer i : dato.getIdOpciones()) {
                    if(diccionario.containsKey(i)) diccionario.replace(i, diccionario.get(i), diccionario.get(i)+1);
                    else diccionario.put(i,1);
                }
            }

            Integer maxFreqIdx = -1;
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
            Integer idx = dato.getIdOpciones().getFirst();
            if(diccionario.containsKey(idx)) diccionario.replace(idx, diccionario.get(idx), diccionario.get(idx)+1);
            else diccionario.put(idx,1);
        }

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
}
