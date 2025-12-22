package test.main.domain;

import main.domain.FormatoLibre;
import main.domain.exceptions.TDatosIncorrecto;
import main.domain.types.TDatos;
import main.domain.types.TDatosString;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(value= MockitoJUnitRunner.class)
public class TestFormatoLibre {
    @Mock
    private TDatosString datosString;
    @Mock
    private TDatos datos;

    @Test
    public void testIsValid() {
        FormatoLibre formatoLibre = new FormatoLibre();
        assertFalse(formatoLibre.isValid(datos, false));
        when(datosString.getTexto()).thenReturn("");
        assertTrue(formatoLibre.isValid(datosString, false));
        assertFalse(formatoLibre.isValid(datosString, true));
        when(datosString.getTexto()).thenReturn("Algo");
        assertTrue(formatoLibre.isValid(datosString, true));
    }

    @Test
    public void testValidar() {
        FormatoLibre formatoLibre = new FormatoLibre();
        try {
            formatoLibre.validar(datos, false);
            fail("No ha saltado ninguna excepcion");
        } catch (Exception e) {
            if (!(e instanceof TDatosIncorrecto)) fail("Ha saltado una excepcion inesperada");
        }
        try {
            when(datosString.getTexto()).thenReturn("");
            formatoLibre.validar(datosString, true);
            fail("No ha saltado ninguna excepcion");
        } catch (Exception e) {
            if (!(e instanceof TDatosIncorrecto)) fail("Ha saltado una excepcion inesperada");
        }

        try {
            when(datosString.getTexto()).thenReturn("");
            formatoLibre.validar(datosString, false);
            when(datosString.getTexto()).thenReturn("Algo");
            formatoLibre.validar(datosString, true);
        } catch (Exception e) {
            fail("Ha saltado una excepcion inesperada");
        }
    }

}
