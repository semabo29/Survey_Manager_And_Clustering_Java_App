package test.main.domain;

import main.domain.Numerica;
import main.domain.exceptions.TDatosIncorrecto;
import main.domain.types.TDatos;
import main.domain.types.TDatosInteger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(value= MockitoJUnitRunner.class)
public class TestNumerica {
    @Mock
    private TDatosInteger datosInt;
    @Mock
    private TDatos datos;

    @Test
    public void testCreadora() {
        Numerica num = new Numerica(0, 10);
        assertEquals(0, num.getMin());
        assertEquals(10, num.getMax());
    }

    @Test
    public void testIsValid() {
        Numerica num = new Numerica(0, 10);

        when(datosInt.getNum()).thenReturn(5);
        when(datosInt.getNumMin()).thenReturn(0);
        when(datosInt.getNumMax()).thenReturn(10);

        // TDatos diferente a TDatosInteger
        assertFalse(num.isValid(datos, true));

        // Correcto
        assertTrue(num.isValid(datosInt, true));

        // Num fuera de rango
        when(datosInt.getNum()).thenReturn(-1);
        assertFalse(num.isValid(datosInt, true));
        assertTrue(num.isValid(datosInt, false));

        when(datosInt.getNum()).thenReturn(5);

        // Rango diferente
        when(datosInt.getNumMin()).thenReturn(2);
        assertFalse(num.isValid(datosInt, true));

        when(datosInt.getNumMin()).thenReturn(0);
        when(datosInt.getNumMax()).thenReturn(8);
        assertFalse(num.isValid(datosInt, true));
    }

    @Test
    public void testValidar() {
        Numerica num = new Numerica(0, 10);

        // TDatos incorrecto
        try {
            num.validar(datos, true);
            fail("No ha saltado ninguna excepcion");
        } catch (Exception e) {
            if (!(e instanceof TDatosIncorrecto)) fail("Ha saltado una excepcion inesperada");
        }
        // TDatosInteger incorrecto
        try {
            when(datosInt.getNum()).thenReturn(11);
            num.validar(datosInt, true);
            fail("No ha saltado ninguna excepcion");
        } catch (Exception e) {
            if (!(e instanceof TDatosIncorrecto)) fail("Ha saltado una excepcion inesperada");
        }


        try {
            when(datosInt.getNum()).thenReturn(5);
            num.validar(datosInt, true);
        } catch (Exception e) {
            fail("Ha saltado una excepcion inesperada");
        }
        try {
            when(datosInt.getNum()).thenReturn(-1);
            num.validar(datosInt, false);
        } catch (Exception e) {
            fail("Ha saltado una excepcion inesperada");
        }
    }

    @Test
    public void testSetMin() {
        Numerica num = new Numerica(0, 10);
        try {
            num.setMin(12);
            fail("No salta excepcion");
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException))
                fail("Ha saltado una excepcion inesperada");
        }
        num.setMin(5);
        assertEquals(5, num.getMin());
    }

    @Test
    public void testSetMax() {
        Numerica num = new Numerica(0, 10);
        try {
            num.setMax(-1);
            fail("No salta excepcion");
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException))
                fail("Ha saltado una excepcion inesperada");
        }
        num.setMax(8);
        assertEquals(8, num.getMax());
    }
}
