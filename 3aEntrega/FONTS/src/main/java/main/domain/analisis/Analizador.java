package main.domain.analisis;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.algoritmo.AlgoritmoTipo;
import main.domain.analisis.algoritmo.InterfazAlgoritmo;
import main.domain.analisis.algoritmo.KMeansOptimizado;
import main.domain.analisis.evaluador.InterfazEvaluadorCalidad;
import main.domain.analisis.evaluador.Silhouette;
import main.domain.analisis.inicializador.InicializadorKMeansPlusPlus;
import main.domain.analisis.inicializador.InterfazInicializadorAlgoritmo;

import java.util.ArrayList;

/**
 * Clase singleton encargada de coordinar el proceso de análisis y clustering.
 * <p>
 * Gestiona el algoritmo de clustering, el inicializador de centroides,
 * el evaluador de calidad y el cálculo automático de {@code k}.
 * </p>
 * @author Javier Zhangpan
 */
public class Analizador {
    /**
     * Única instancia de la clase Analizar
     */
    private static final Analizador INSTANCE = new Analizador();

    private int k = 1;
    private final Comparador comparador = new Comparador();

    //defaults son: KMeansOptimizado, KMeans++, Silhouette
    private InterfazAlgoritmo<? extends AlgoritmoTipo> algoritmo = new KMeansOptimizado();
    private InterfazEvaluadorCalidad evaluador = new Silhouette();
    private InterfazInicializadorAlgoritmo<? extends AlgoritmoTipo> inicializadorAlgoritmo = new InicializadorKMeansPlusPlus();

    private final float ELBOW_THRESHOLD = 0.15f;

    private ArrayList<ArrayList<RespuestaEncuesta>> resultados;

    /**
     * Devuelve la instancia única del analizador.
     *
     * @return Instancia singleton de {@code Analizador}.
     */
    public static Analizador getInstance() {
        return INSTANCE;
    }

    /**
     * Devuelve el evaluador.
     *
     * @return {@link Object} que representa el evaluador.
     */
    public Object getEvaluador() {
        return evaluador;
    }

    /**
     * Devuelve el algoritmo.
     *
     * @return {@link Object} que representa el algoritmo.
     */
    public Object getAlgoritmo() {
        return algoritmo;
    }

    /**
     * Devuelve el inicializador.
     *
     * @return {@link Object} que representa el inicializador.
     */
    public Object getInicializador() {
        return inicializadorAlgoritmo;
    }

    /**
     * Devuelve el número de clusters.
     *
     * @return {@link Integer} que representa el número de clusters.
     */
    public Integer getK() {return k;}

    /**
     * Cambia el número de clusters.
     * @param k número de clusters.
     */
    public void setK(int k) {
        this.k = k;
    }


    /**
     * Devuelve una matriz de distancias internas por cada cluster.
     *
     * @return Matriz de distancias por cluster.
     */
    public ArrayList<ArrayList<ArrayList<Float>>> getDistanciasClusters() {
        ArrayList<ArrayList<ArrayList<Float>>> mat = new ArrayList<>();

        for (ArrayList<RespuestaEncuesta> cluster : resultados) {
            ArrayList<ArrayList<Float>> clusterMat = new ArrayList<>();
            for (RespuestaEncuesta r1 : cluster) {
                ArrayList<Float> fila = new ArrayList<>();
                for (RespuestaEncuesta r2 : cluster) {
                    fila.add(comparador.getDistancia(r1, r2));
                }
                clusterMat.add(fila);
            }
            mat.add(clusterMat);
        }

        return mat;
    }


    /**
     * Devuelve la matriz de distancias global entre todas las respuestas.
     *
     * @return Matriz de distancias.
     */
    public ArrayList<ArrayList<Float>> getDistancias() {
        ArrayList<RespuestaEncuesta> todas = new ArrayList<>();
        //aplanar matriz a vector
        for (ArrayList<RespuestaEncuesta> cluster : resultados) {
            todas.addAll(cluster);
        }

        ArrayList<ArrayList<Float>> mat = new ArrayList<>();
        for (RespuestaEncuesta toda : todas) {
            ArrayList<Float> fila = new ArrayList<>();
            for (RespuestaEncuesta respuestaEncuesta : todas) {
                fila.add(comparador.getDistancia(toda, respuestaEncuesta));
            }
            mat.add(fila);
        }
        return mat;
    }

    /**
     * Devuelve los emails de las respuestas agrupadas por cluster.
     *
     * @return Lista de emails por cluster.
     */
    public ArrayList<ArrayList<String>> getAutoresClusters() {
        ArrayList<ArrayList<String>> mat = new ArrayList<>();

        for (ArrayList<RespuestaEncuesta> cluster : resultados) {
            ArrayList<String> fila = new ArrayList<>();
            for (RespuestaEncuesta respuesta : cluster) {
                fila.add(respuesta.getEmailrespuesta());
            }
            mat.add(fila);
        }
        return mat;
    }

    /**
     * Cambia el algoritmo e inicializador de algoritmo del analizador.
     * <p>
     * Hay ciertas combinaciones no soportadas.
     *
     * @param alg algoritmo de clustering.
     * @param ini inicializador de algoritmo.
     */
    public <T extends AlgoritmoTipo> void cambiarAlgoritmo(InterfazAlgoritmo<T> alg, InterfazInicializadorAlgoritmo<T> ini) {
        this.algoritmo = alg;
        this.inicializadorAlgoritmo = ini;
    }

    /**
     * Cambia el evaluador de calidad del clustering
     *
     * @param eval algoritmo evaluación de clustering.
     */
    public void cambiarEvaluador(InterfazEvaluadorCalidad eval) {
        this.evaluador = eval;
    }

    /**
     * Ejecuta el clustering sobre las respuestas proporcionadas.
     *
     * @param respuestas Respuestas a analizar.
     */
    public void analizarRespuestas(ArrayList<RespuestaEncuesta> respuestas) {
        resultados = analizar(respuestas, k);
    }

    /**
     * Evalúa la calidad del clustering actual.
     *
     * @return Valor de calidad calculado.
     */
    public Float evaluarCalidadClustering() {
        return evaluador.evaluarCalidad(resultados, comparador);
    }

    /**
     * Calcula automáticamente el valor óptimo de {@code k} usando el Elbow Method.
     *
     * @param respuestas Respuestas a analizar.
     */
    public void calcularK(ArrayList<RespuestaEncuesta> respuestas) {
        if(respuestas.size() == 1) {
            k = 1;
            return;
        }

        Integer numK = 1;
        ArrayList<ArrayList<RespuestaEncuesta>> clustering = analizar(respuestas, numK);

        double WCSS;
        double prevWCSS = WCSS(clustering);

        while(numK < respuestas.size()) {
            ++numK;
            clustering = analizar(respuestas, numK);

            WCSS = WCSS(clustering);

            if(prevWCSS == WCSS) break;
            double delta = (prevWCSS - WCSS) / prevWCSS;
            if(delta < ELBOW_THRESHOLD) break;

            prevWCSS = WCSS;
        }

        this.k = --numK;
    }

    private ArrayList<ArrayList<RespuestaEncuesta>> analizar(ArrayList<RespuestaEncuesta> respuestas, Integer numK) {
        //el inicializador selecciona puntos iniciales de entre las respuestas
        ArrayList<RespuestaEncuesta> iniciales = inicializadorAlgoritmo.generaIniciales(respuestas, numK, comparador);
        return algoritmo.analizar(iniciales, respuestas, numK, comparador);
    }

    private double WCSS (ArrayList<ArrayList<RespuestaEncuesta>> clusters) {
        double WCSS = 0.0;
        for(ArrayList<RespuestaEncuesta> cluster : clusters) {
            if(!cluster.isEmpty()) {
                RespuestaEncuesta centroide = comparador.calcularNuevoCentroide(cluster);
                for(RespuestaEncuesta respuesta : cluster) {
                    WCSS += Math.pow(comparador.getDistancia(centroide, respuesta),2);
                }
            }
        }
        return WCSS;
    }
}