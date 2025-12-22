package test.main.domain.types;

import main.domain.exceptions.AmbosTextosSonVacios;
import main.domain.types.TDatos;
import main.domain.types.TDatosString;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

//no testeo funcionalidades básicas de setters y getters porque su implementación es trivial
public class TestTDatosString {

    //testea funcionalidad básica de la constructora por parámetros
    //como los parámetros ya son validados por el controlador no creo necesario probar valores extremos
    @Test
    public void testCreadoraPorParametros() {
        TDatosString tds = new TDatosString("test");
        assertEquals("test", tds.getTexto());
    }

    //testea la funcionalidad básica de calcularDistancia
    //como los parámetros ya son validados por la clase Comparador no creo necesario probar valores extremos
    @Test
    public void testCalcularDistancia() {
        TDatosString tds1 = new TDatosString("test");
        TDatosString tds2 = new TDatosString("taste");

        assertEquals(2.0f/5.0f , tds1.calcularDistancia(tds2), 0.0001f);
    }

    //testea si la función de distancia cumple correctamente con la desigualdad triangular
    @Test
    public void testDesigualdadTriangular() {
        float epsilon = 1e-6f;

        TDatosString a1 = new TDatosString("testing");
        TDatosString b1 = new TDatosString("test");
        TDatosString c1 = new TDatosString("tester");

        float dAB1 = a1.calcularDistancia(b1);
        float dBC1 = b1.calcularDistancia(c1);
        float dAC1 = a1.calcularDistancia(c1);

        assertTrue(dAC1 <= dAB1 + dBC1 + epsilon);

        TDatosString a2 = new TDatosString("aaa");
        TDatosString b2 = new TDatosString("zzz");
        TDatosString c2 = new TDatosString("foo");

        float dAB2 = a2.calcularDistancia(b2);
        float dBC2 = b2.calcularDistancia(c2);
        float dAC2 = a2.calcularDistancia(c2);

        assertTrue(dAC2 <= dAB2 + dBC2 + epsilon);
    }

    @Test
    public void testIgualdad() {
        float epsilon = 1e-6f;
        TDatosString td = new TDatosString("test");
        float d = td.calcularDistancia(td);
        assertEquals(0.0f, d, epsilon);
    }

    //testea si calcularDistancia lanza correctamente la excepción AmbosTextosSonVacios
    //al intentar calcular la distancia de 2 datos vacíos
    @Test
    public void testCalcularDistanciaExcepcionAmbosTextosSonVacios() {
        TDatosString tds1 = new TDatosString("");
        TDatosString tds2 = new TDatosString("");

        try {
            tds1.calcularDistancia(tds2);
            fail("No ha saltado ninguna excepción");
        } catch (Exception e) {
            if (!(e instanceof AmbosTextosSonVacios)) fail("Ha saltado una excepción inesperada");
        }
    }

    //testea la funcionalidad básica de getComponenteCentroide
    //como los parámetros ya son validados por la clase Comparador no creo necesario probar valores extremos
    @Test
    public void testGetComponenteCentroide() {
        TDatosString tds1 = new TDatosString("este es un texto de test");
        TDatosString tds2 = new TDatosString("texto test test texto test");

        ArrayList<TDatos> tdss = new ArrayList<>();
        tdss.add(tds1);
        tdss.add(tds2);

        TDatos componente = tds1.getComponenteCentroide(tdss);
        TDatosString aux = (TDatosString) componente;

        assertEquals("test", aux.getTexto());
    }
}
