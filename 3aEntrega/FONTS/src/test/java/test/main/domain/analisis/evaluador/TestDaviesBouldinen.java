package test.main.domain.analisis.evaluador;

import main.domain.analisis.Comparador;
import main.domain.analisis.evaluador.DaviesBouldinen;
import main.domain.RespuestaEncuesta;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Mockito.when;

@RunWith(value= MockitoJUnitRunner.class)
public class TestDaviesBouldinen {
    ArrayList<ArrayList<RespuestaEncuesta>> clusters;
    HashMap<RespuestaEncuesta, Float> valors;

    @Mock
    Comparador comp;

    @Before
    public void setup() {
        when(comp.getDistancia(Mockito.any(RespuestaEncuesta.class), Mockito.any(RespuestaEncuesta.class)))
                .thenAnswer(inv -> {
                    return Math.abs(calcularIndex(inv.getArgument(0)) - calcularIndex(inv.getArgument(1)));
                });

        when(comp.calcularNuevoCentroide(Mockito.<ArrayList<RespuestaEncuesta>>any()))
                .thenAnswer(inv -> {return calcularCentroide(inv.getArgument(0));});
    }

    private Float calcularIndex(RespuestaEncuesta r) {
        Float valor = valors.get(r);
        if (valor == null) {
            valor = 0.0f;
        }
        return valor;
    }

    private RespuestaEncuesta calcularCentroide(ArrayList<RespuestaEncuesta> cluster) {
        RespuestaEncuesta centroid = Mockito.mock(RespuestaEncuesta.class);
        Float sum = 0.f;
        for (RespuestaEncuesta r : cluster) {
            sum += valors.get(r);
        }
        valors.put(centroid, sum / cluster.size());
        return centroid;
    }


    @Test
    public void testCalinskiHarabaszStrong() {
        DaviesBouldinen evaluador = new DaviesBouldinen();
        clusters = new ArrayList<>();
        valors = new HashMap<>();

        for (int i = 0; i < 4; ++i) {
            ArrayList<RespuestaEncuesta> cluster = new ArrayList<>();
            for (int j = 0; j < 4; ++j) {
                RespuestaEncuesta r = Mockito.mock(RespuestaEncuesta.class);
                cluster.add(r);
                valors.put(r, (float) (i*8 + j));
            }
            clusters.add(cluster);
        }
        Float evaluacion = evaluador.evaluarCalidad(clusters, comp);
        System.out.println("evaluacion: " + evaluacion);
        assertTrue(evaluacion < 0.4f);
    }

    @Test
    public void testCalinskiHarabaszWeak() {
        DaviesBouldinen evaluador = new DaviesBouldinen();
        clusters = new ArrayList<>();
        valors = new HashMap<>();

        for (int i = 0; i < 4; ++i) {
            ArrayList<RespuestaEncuesta> cluster = new ArrayList<>();
            for (int j = 0; j < 4; ++j) {
                RespuestaEncuesta r = Mockito.mock(RespuestaEncuesta.class);
                cluster.add(r);
                valors.put(r, (float) (i*4 + j));
            }
            clusters.add(cluster);
        }

        Float evaluacion = evaluador.evaluarCalidad(clusters, comp);
        System.out.println("evaluacion: " + evaluacion);
        assertTrue(evaluacion >= 0.4f);
    }
}
