package test.main.domain;

import main.domain.Encuesta;
import main.domain.Pregunta;
import main.domain.TipoPregunta;
import main.domain.exceptions.EncuestaDiferente;
import main.domain.exceptions.PreguntaPosterior;
import main.domain.types.TDatos;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(value= MockitoJUnitRunner.class)
public class TestPregunta {
    @Mock
    private TipoPregunta tipoPregunta;

    @Mock
    private TipoPregunta tipoPregunta2;

    @Mock
    private Encuesta encuesta;

    @Mock
    private TDatos datos;

    @Test
    public void testCreadora() {
        int id = 1;
        String texto = "Primera pregunta";
        boolean obligatorio = true;

        Pregunta p = new Pregunta(encuesta, id, texto, obligatorio, tipoPregunta);

        assertEquals(id, p.getId());
        assertEquals(texto, p.getTexto());
        assertEquals(obligatorio, p.isObligatorio());
        assertSame(encuesta, p.getEncuesta());
        assertEquals("", p.getDescripcion());
        assertSame(tipoPregunta, p.getTipoPregunta());
    }

    @Test
    public void testSetDescripcion() {
        Pregunta p = new Pregunta(encuesta, 1, "Pregunta 1", false, tipoPregunta);

        p.setDescripcion("Descripcion 1");
        assertEquals("Descripcion 1", p.getDescripcion());

        p.setDescripcion("");
        assertEquals("", p.getDescripcion());
    }

    @Test
    public void testIsValid() {
        Pregunta p1 = new Pregunta(encuesta, 1, "Pregunta 1", false, tipoPregunta);
        when(tipoPregunta.isValid(datos, false)).thenReturn(true);
        assertTrue(p1.isValid(datos));

        when(tipoPregunta.isValid(datos, false)).thenReturn(false);
        assertFalse(p1.isValid(datos));
    }

    @Test
    public void testValidar() {
        Pregunta p1 = new Pregunta(encuesta, 1, "Pregunta 1", false, tipoPregunta);

        try {
            p1.validar(datos);
        } catch (Exception e) {
            fail("testValidar ha fallado con una excepcion inesperada");
        }
    }

    @Test
    public void testSetObligatorio() {
        Pregunta p1 = new Pregunta(encuesta, 1, "Pregunta 1", false, tipoPregunta);
        p1.setObligatorio(true);
        assertTrue(p1.isObligatorio());
        p1.setObligatorio(false);
        assertFalse(p1.isObligatorio());
    }

    @Test
    public void testSetTipoPregunta() {
        Pregunta p1 = new Pregunta(encuesta, 1, "Pregunta 1", false, tipoPregunta);
        p1.setTipoPregunta(tipoPregunta2);
        assertSame(tipoPregunta2, p1.getTipoPregunta());
        p1.setTipoPregunta(tipoPregunta);
        assertSame(tipoPregunta, p1.getTipoPregunta());
    }

    @Test
    public void testSetTexto() {
        Pregunta p1 = new Pregunta(encuesta, 1, "Pregunta 1", false, tipoPregunta);
        p1.setTexto("texto");
        assertEquals("texto", p1.getTexto());
        p1.setTexto("");
        assertEquals("", p1.getTexto());
    }
}
