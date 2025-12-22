package test.main.domain.types;

import main.domain.exceptions.NumMaxIgualANumMin;
import main.domain.types.TDatosInteger;
import main.domain.types.TDatos;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

//no testeo funcionalidades básicas de setters y getters porque la implementación es trivial
public class TestTDatosInteger {

    //testea funcionalidad básica de la constructora por parámetros
    //como los parámetros ya son validados por los controladores no creo necesario probar valores extremos
    @Test
    public void testCreadoraPorParametros() {
        TDatosInteger tdi = new TDatosInteger(0,10,-10);
        assertEquals(0, tdi.getNum());
        assertEquals(10, tdi.getNumMax());
        assertEquals(-10, tdi.getNumMin());
    }

    //testea el funcionamiento básico de la función calcularDistancia
    //como los parámetros ya son validados por la clase Comparador no creo necesario probar valores extremos
    @Test
    public void testCalcularDistancia() {
        TDatosInteger tdi1 = new TDatosInteger(0,10,-10);
        TDatosInteger tdi2 = new TDatosInteger(5,10,-10);

        assertEquals(0.25f, tdi1.calcularDistancia(tdi2), 0.0001f);
    }

    //testea si la función de distancia cumple correctamente con la desigualdad triangular
    @Test
    public void testDesigualdadTriangular() {

        int nMin = -10;
        int nMax = 10;
        float epsilon = 1e-6f;

        TDatosInteger A = new TDatosInteger(-5, nMax, nMin);
        TDatosInteger B = new TDatosInteger(0,  nMax, nMin);
        TDatosInteger C = new TDatosInteger(5,  nMax, nMin);

        float dAB = A.calcularDistancia(B);
        float dBC = B.calcularDistancia(C);
        float dAC = A.calcularDistancia(C);

        //comportamiento esperado: d(A,C) ≤ d(A,B) + d(B,C)
        assertTrue(dAC <= dAB + dBC + epsilon);
    }

    @Test
    public void testIgualdad() {
        int nMin = -10;
        int nMax = 10;
        float epsilon = 1e-6f;

        TDatosInteger A = new TDatosInteger(0, nMax, nMin);
        float dAA = A.calcularDistancia(A);

        assertEquals(0, dAA, epsilon);
    }

    //testea si calcularDistancia lanza correctamente la excepción NumMaxIgualANumMin
    //al intentar calcular la distancia de 2 datos con nMax igual a nMin
    @Test
    public void testCalcularDistanciaExcepcionNumMaxIgualANumMin() {
        TDatosInteger tdi1 = new TDatosInteger(0,10,10);
        TDatosInteger tdi2 = new TDatosInteger(5,10,10);

        try {
            tdi1.calcularDistancia(tdi2);
            fail("No ha saltado ninguna excepción");
        } catch (Exception e) {
            if (!(e instanceof NumMaxIgualANumMin)) fail("Ha saltado una excepción inesperada");
        }
    }

    //testea la funcionalidad básica de getComponenteCentroide
    //no hace falta testear el caso de Array vacío porque la comprobación ya la hace la clase Comparador
    @Test
    public void testGetComponenteCentroide() {
        TDatosInteger tdi1 = new TDatosInteger(0,10,-10);
        TDatosInteger tdi2 = new TDatosInteger(5,10,-10);

        ArrayList<TDatos> tdis = new ArrayList<>();
        tdis.add(tdi1);
        tdis.add(tdi2);

        TDatos componente = tdi1.getComponenteCentroide(tdis);
        TDatosInteger aux = (TDatosInteger) componente;

        assertEquals(2, aux.getNum());
        assertEquals(10, aux.getNumMax());
        assertEquals(-10, aux.getNumMin());
    }

}
