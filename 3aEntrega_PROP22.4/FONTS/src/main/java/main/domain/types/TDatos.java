package main.domain.types;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Interfaz base para tipos de datos analizables.
 * Define operaciones necesarias para comparación y cálculo de centroides.
 * @author Javier Zhangpan
 */
public interface TDatos extends Serializable {
    /**
     * Calcula la distancia normalizada entre este dato y otro.
     *
     * @param t dato con el que se compara
     * @return distancia entre ambos datos
     */
    public Float calcularDistancia(TDatos t);

    /**
     * Calcula el componente correspondiente del centroide
     * a partir de una colección de datos.
     *
     * @param datos conjunto de datos del cluster
     * @return valor del componente del centroide
     */
    public TDatos getComponenteCentroide(ArrayList<TDatos> datos);

    /**
     * Devuelve la representación en texto del contenido del dato.
     *
     * @return contenido del dato
     */
    public String getContenido();
}
