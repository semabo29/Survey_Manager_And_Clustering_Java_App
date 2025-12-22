package main.domain.types;

import main.domain.exceptions.NumMaxIgualANumMin;
import java.util.ArrayList;


/**
 * Representa un dato numérico entero
 * <p>
 * Implementa {@link TDatos}
 * </p>
 * @author Javier Zhangpan
 */
public class TDatosInteger implements TDatos{

    int num;
    int numMax;
    int numMin;

    /**
     * Constructora por defecto.
     * Inicializa {@code num = 0},
     * {@code numMax = Integer.MAX_VALUE},
     * {@code numMin = Integer.MIN_VALUE}.
     */
    public TDatosInteger() {
        this.num = 0;
        this.numMax = Integer.MAX_VALUE;
        this.numMin = Integer.MIN_VALUE;
    }

    /**
     * Constructora con valor numérico.
     *
     * @param n valor del dato
     */
    public TDatosInteger(int n) {
        this.num = n;
        this.numMax = Integer.MAX_VALUE;
        this.numMin = Integer.MIN_VALUE;
    }


    /**
     * Constructora completa.
     *
     * @param n valor del dato
     * @param nMax valor máximo
     * @param nMin valor mínimo
     */
    public TDatosInteger(int n, int nMax, int nMin) {
        this.num = n;
        this.numMax = nMax;
        this.numMin = nMin;
    }

    /**
     * Devuelve el valor numérico de la respuesta.
     * @return Valor numérico de la respuesta.
     */
    public int getNum() {
        return num;
    }

    /**
     * Devuelve el valor numérico máximo que puede alcanzar la respuesta.
     * @return Valor numérico máximo que puede alcanzar la respuesta.
     */
    public int getNumMax() {
        return numMax;
    }

    /**
     * Devuelve el valor numérico mínimo que puede alcanzar la respuesta.
     * @return Valor numérico mínimo que puede alcanzar la respuesta.
     */
    public int getNumMin() {
        return numMin;
    }

    /**
     * Cambia el valor numérico de la respuesta.
     * @param num valor numérico
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Cambia el valor numérico máximo que puede alcanzar la respuesta
     * @param numMax valor numérico máximo que puede alcanzar la respuesta
     */
    public void setNumMax(int numMax) {
        this.numMax = numMax;
    }

    /**
     * Cambia el valor numérico mínimo que puede alcanzar la respuesta
     * @param numMin valor numérico mínimo que puede alcanzar la respuesta
     */
    public void setNumMin(int numMin) {
        this.numMin = numMin;
    }

    /**
     * Calcula la distancia normalizada entre este entero y otro {@link TDatosInteger}.
     *
     * @param t otro dato
     * @return distancia normalizada
     */
    @Override
    public Float calcularDistancia(TDatos t) {
        TDatosInteger aux = (TDatosInteger) t;
        return numericaDistancia(this.num, aux.getNum(), this.numMax, this.numMin);
    }

    /**
     * Calcula el componente de tipo {@link TDatosInteger} de centroide de un cluster.
     *
     * @param datos conjunto de datos del cluster de tipo {@link TDatosInteger}
     * @return nuevo {@link TDatosInteger} representando la media
     */
    @Override
    public TDatos getComponenteCentroide(ArrayList<TDatos> datos) {
        long sum = 0;
        for(TDatos d : datos) {
            TDatosInteger dato = (TDatosInteger) d;
            sum += dato.getNum();
        }
        int mean = (int)(sum / datos.size());
        return new TDatosInteger(mean, this.numMax, this.numMin);
    }

    private static Float numericaDistancia(int numA, int numB, int numMax, int numMin) {
        if(numMax == numMin) throw new NumMaxIgualANumMin("NumMax es igual a numMin, lo que provoca una división por cero");
        long diffAB = Math.abs((long)numA-(long)numB);
        long diffMaxMin = (long)numMax-(long)numMin;
        return (float)diffAB/(float)diffMaxMin;
    }

    /**
     * Comprueba si otro {@link TDatosInteger} tiene el mismo valor.
     * La función asume que pertenecen a la misma pregunta.
     *
     * @return {@code true} si tienen el mismo valor,
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
        final TDatosInteger other = (TDatosInteger) obj;

        return  num == other.num;
    }

    /**
     * Devuelve la representación en texto del contenido del dato.
     *
     * @return contenido del dato
     */
    @Override
    public String getContenido() {
        return String.valueOf(num);
    }
}
