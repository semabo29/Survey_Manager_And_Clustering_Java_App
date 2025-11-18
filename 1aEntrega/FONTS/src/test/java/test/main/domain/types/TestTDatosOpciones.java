package test.main.domain.types;

import main.domain.exceptions.NumeroModalidadesMenorQueDos;
import main.domain.exceptions.UnionEntreConjuntosDeOpcionesVacio;
import main.domain.types.TDatos;
import main.domain.types.TDatosOpciones;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

//no testeo funcionalidades básicas de setters y getters porque su implementación es trivial
public class TestTDatosOpciones {

    //testea funcionalidad básica de la constructora por parámetros
    //como los parámetros ya son validados por los controladores no creo necesario probar valores extremos
    @Test
    public void testCreadoraPorParametros() {
        TDatosOpciones tdo = new TDatosOpciones(new ArrayList<>(), true, 1);
        assertEquals(new ArrayList<>(), tdo.getIdOpciones());
        assertTrue(tdo.isOrden());
        assertEquals(1, tdo.getNumOpciones());
    }

    //testea la funcionalidad básica de calcularDistancia para una respuesta cualitativa ordenada de 1 valor
    //como los parámetros ya son validados por la clase Comparador no creo necesario probar valores extremos
    @Test
    public void testCalcularDistanciaCualitativaOrdenada() {
        ArrayList<Integer> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();
        arr1.add(1);
        arr2.add(2);

        TDatosOpciones tdo1 = new TDatosOpciones(arr1, true, 2);
        TDatosOpciones tdo2 = new TDatosOpciones(arr2, true, 2);

        assertEquals(1.0f, tdo1.calcularDistancia(tdo2), 0.0001f);
    }

    //testea la funcionalidad básica de calcularDistancia para una respuesta cualitativa no ordenada de 1 valor
    //como los parámetros ya son validados por la clase Comparador no creo necesario probar valores extremos
    @Test
    public void testCalcularDistanciaCualitativaNoOrdenada() {
        ArrayList<Integer> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();
        arr1.add(1);
        arr2.add(2);

        TDatosOpciones tdo1 = new TDatosOpciones(arr1, false, 2);
        TDatosOpciones tdo2 = new TDatosOpciones(arr2, false, 2);

        assertEquals(1.0f, tdo1.calcularDistancia(tdo2), 0.0001f);
    }

    //testea la funcionalidad básica de calcularDistancia para una respuesta cualitativa de varios valores
    //como los parámetros ya son validados por la clase Comparador no creo necesario probar valores extremos
    @Test
    public void testCalcularDistanciaCualitativaVarias() {
        ArrayList<Integer> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();
        for(int i = 1; i <= 2; ++i) arr1.add(i);
        for(int i = 2; i <= 3; ++i) arr2.add(i);

        TDatosOpciones tdo1 = new TDatosOpciones(arr1, true, 3);
        TDatosOpciones tdo2 = new TDatosOpciones(arr2, true, 3);

        assertEquals(1.0f-(1.0f/3.0f), tdo1.calcularDistancia(tdo2), 0.0001f);
    }

    //testea si calcularDistancia lanza correctamente la excepción NumeroModalidadesMenorQueDos
    //al intentar calcular la distancia de 2 datos con número de modalidades menor que 2
    //en el caso de respuesta cualitativa ordenada
    @Test
    public void testCalcularDistanciaExcepcionNumeroModalidadesMenorQueDos() {
        ArrayList<Integer> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();
        arr1.add(1);
        arr2.add(2);

        TDatosOpciones tdo1 = new TDatosOpciones(arr1, true, 1);
        TDatosOpciones tdo2 = new TDatosOpciones(arr2, true, 1);

        try {
            tdo1.calcularDistancia(tdo2);
            fail("No ha saltado ninguna excepción");
        } catch (Exception e) {
            if (!(e instanceof NumeroModalidadesMenorQueDos)) fail("Ha saltado una excepción inesperada");
        }
    }

    //testea si calcularDistancia lanza correctamente la excepción UnionEntreConjuntosDeOpcionesVacio
    //al intentar calcular la distancia de 2 datos con opciones escogidas vacías
    //en el caso de respuesta cualitativa con varios valores
    @Test
    public void testCalcularDistanciaExcepcionUnionEntreConjuntosDeOpcionesVacio() {
        ArrayList<Integer> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();

        TDatosOpciones tdo1 = new TDatosOpciones(arr1, true, 3);
        TDatosOpciones tdo2 = new TDatosOpciones(arr2, true, 3);

        try {
            tdo1.calcularDistancia(tdo2);
            fail("No ha saltado ninguna excepción");
        } catch (Exception e) {
            if (!(e instanceof UnionEntreConjuntosDeOpcionesVacio)) fail("Ha saltado una excepción inesperada");
        }
    }

    //testea la funcionalidad básica de getComponenteCentroide para respuestas cualitativas de 1 valor
    //como los parámetros ya son validados por la clase Comparador no creo necesario probar valores extremos
    @Test
    public void testGetComponenteCentroideCualitativaConUnValor() {
        ArrayList<Integer> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();
        arr1.add(1);
        arr2.add(2);
        TDatosOpciones tdo1 = new TDatosOpciones(arr1, true, 2);
        TDatosOpciones tdo2 = new TDatosOpciones(arr2, true, 2);

        ArrayList<TDatos> tdos = new ArrayList<>();
        tdos.add(tdo1);
        tdos.add(tdo2);

        TDatos componente = tdo1.getComponenteCentroide(tdos);
        TDatosOpciones aux = (TDatosOpciones) componente;
        int moda = aux.getIdOpciones().getFirst();

        assertEquals(1,moda);
        assertTrue(aux.isOrden());
        assertEquals(2, aux.getNumOpciones());
    }

    //testea la funcionalidad básica de getComponenteCentroide para respuestas cualitativas de varios valores
    //como los parámetros ya son validados por la clase Comparador no creo necesario probar valores extremos
    @Test
    public void testGetComponenteCentroideCualitativaConVariosValores() {
        ArrayList<Integer> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();
        for(int i = 1; i <= 2; ++i) arr1.add(i);
        for(int i = 2; i <= 3; ++i) arr2.add(i);
        TDatosOpciones tdo1 = new TDatosOpciones(arr1, true, 3);
        TDatosOpciones tdo2 = new TDatosOpciones(arr2, true, 3);

        ArrayList<TDatos> tdos = new ArrayList<>();
        tdos.add(tdo1);
        tdos.add(tdo2);

        TDatos componente = tdo1.getComponenteCentroide(tdos);
        TDatosOpciones aux = (TDatosOpciones) componente;

        int maxFreq = aux.getIdOpciones().getFirst();

        assertEquals(2,maxFreq);
        assertTrue(aux.isOrden());
        assertEquals(3, aux.getNumOpciones());
    }
}
