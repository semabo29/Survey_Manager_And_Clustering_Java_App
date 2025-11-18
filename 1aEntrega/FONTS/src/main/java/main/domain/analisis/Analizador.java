package main.domain.analisis;

import java.util.ArrayList;

import main.domain.RespuestaEncuesta;
import main.domain.analisis.algoritmo.AlgoritmoTipo;
import main.domain.analisis.algoritmo.InterfazAlgoritmo;
import main.domain.analisis.algoritmo.KMeans;
import main.domain.analisis.evaluador.InterfazEvaluadorCalidad;
import main.domain.analisis.evaluador.Silhouette;
import main.domain.analisis.inicializador.InicializadorKMeansPlusPlus;
import main.domain.analisis.inicializador.InterfazInicializadorAlgoritmo;
import main.domain.types.TDatosOpciones;
import main.domain.types.TDatosString;

public class Analizador {
    private static final Analizador INSTANCE = new Analizador();
    private int k = 1;
    private final Comparador comparador = new Comparador();
    private InterfazAlgoritmo<? extends AlgoritmoTipo> algoritmo = new KMeans();
    private InterfazEvaluadorCalidad evaluador = new Silhouette();
    private InterfazInicializadorAlgoritmo<? extends AlgoritmoTipo> inicializadorAlgoritmo = new InicializadorKMeansPlusPlus();

    public static Analizador getInstance() {
        return INSTANCE;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Integer getK() {return k;}

    public <T extends AlgoritmoTipo> void cambiarAlgoritmo(InterfazAlgoritmo<T> alg, InterfazInicializadorAlgoritmo<T> ini) {
        this.algoritmo = alg;
        this.inicializadorAlgoritmo = ini;
    }

    public void cambiarEvaluador(InterfazEvaluadorCalidad eval) {
        this.evaluador = eval;
    }

    public ArrayList<ArrayList<RespuestaEncuesta>> analizarRespuestas(ArrayList<RespuestaEncuesta> respuestas) {
        return analizar(respuestas, k);
    }

    public Object getEvaluadorActual() {
        return evaluador;
    }

    public Float evaluarCalidadClustering(ArrayList<ArrayList<RespuestaEncuesta>> clusters) {
        return evaluador.evaluarCalidad(clusters, comparador);
    }

    public void calcularK(ArrayList<RespuestaEncuesta> respuestas) {
        Integer numK = 1;
        ArrayList<ArrayList<RespuestaEncuesta>> clustering = analizar(respuestas, numK);

        double WCSS;
        double prevWCSS = WCSS(clustering);

        while(numK <= respuestas.size()) {
            ++numK;
            clustering = analizar(respuestas, numK);

            WCSS = WCSS(clustering);

            double delta = (prevWCSS - WCSS) / prevWCSS;
            if(delta < 0.1) break;

            prevWCSS = WCSS;
        }

        this.k = numK;
    }

    private ArrayList<ArrayList<RespuestaEncuesta>> analizar(ArrayList<RespuestaEncuesta> respuestas, Integer numK) {
        //el inicializador selecciona puntos iniciales de entre las respuestas
        ArrayList<RespuestaEncuesta> iniciales = inicializadorAlgoritmo.generaIniciales(respuestas, numK, comparador);
        return algoritmo.analizar(iniciales, respuestas, numK, comparador);
    }

    private double WCSS (ArrayList<ArrayList<RespuestaEncuesta>> clusters) {
        double WCSS = 0.0;
        for(ArrayList<RespuestaEncuesta> cluster : clusters) {
            RespuestaEncuesta centroide = comparador.calcularNuevoCentroide(cluster);
            for(RespuestaEncuesta respuesta : cluster) {
                WCSS += Math.pow(comparador.getDistancia(centroide, respuesta),2);
            }
        }
        return WCSS;
    }
}