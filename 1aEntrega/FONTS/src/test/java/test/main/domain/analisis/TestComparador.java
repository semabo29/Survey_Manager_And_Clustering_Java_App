package test.main.domain.analisis;

import main.domain.analisis.Comparador;
import main.domain.RespuestaEncuesta;
import main.domain.types.TDatos;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.mockito.Mockito.when;

@RunWith(value= MockitoJUnitRunner.class)
public class TestComparador {
    @Test
    public void testDistancia() {
        RespuestaEncuesta r1 = Mockito.mock(RespuestaEncuesta.class);
        RespuestaEncuesta r2 = Mockito.mock(RespuestaEncuesta.class);
        TreeMap<Integer, TDatos> map1 = new TreeMap<>();
        TreeMap<Integer, TDatos> map2 = new TreeMap<>();
        TDatos t1 = Mockito.mock(TDatos.class);
        TDatos t2 = Mockito.mock(TDatos.class);
        map1.put(1, t1);
        map2.put(1, t2);
        when(t1.calcularDistancia(t2)).thenReturn(3.f);
        when(r1.getIdRespuesta()).thenReturn(map1);
        when(r2.getIdRespuesta()).thenReturn(map2);
        Comparador comparador = new Comparador();
        assertEquals(3.f, comparador.getDistancia(r1, r2), 0.1f);
    }

    @Test
    public void testCentroide() {
        RespuestaEncuesta r1 =  Mockito.mock(RespuestaEncuesta.class);
        RespuestaEncuesta r2 =  Mockito.mock(RespuestaEncuesta.class);
        TDatos t1 = Mockito.mock(TDatos.class);
        TDatos t2 = Mockito.mock(TDatos.class);
        TDatos t3 = Mockito.mock(TDatos.class);
        ArrayList<TDatos> datos1 = new ArrayList<>();
        ArrayList<TDatos> datos2 = new ArrayList<>();
        ArrayList<RespuestaEncuesta> cluster = new ArrayList<>();
        cluster.add(r1);
        cluster.add(r2);
        datos1.add(t1);
        datos2.add(t2);
        when(r1.getDatos()).thenReturn(datos1);
        when(r2.getDatos()).thenReturn(datos2);
        when(t1.getComponenteCentroide(Mockito.<ArrayList<TDatos>>any())).thenReturn(t3);
        Comparador comparador = new Comparador();
        RespuestaEncuesta res = comparador.calcularNuevoCentroide(cluster);
        assertTrue(res.getDatos().size() == 1 && res.getDatos().contains(t3));
    }
}
