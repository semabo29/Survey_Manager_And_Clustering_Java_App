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
    private Encuesta encuestaDif;

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
        assertEquals("", p.getImagen());
        assertTrue(p.getDependientes().isEmpty());
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
    public void testAddPreguntaDependiente() {
        Pregunta p1 = new Pregunta(encuesta, 1, "Pregunta 1", false, tipoPregunta);
        Pregunta p2 = new Pregunta(encuesta, 2, "Pregunta 2", false, tipoPregunta);
        Pregunta p3 = new Pregunta(encuesta, 3, "Pregunta 3", false, tipoPregunta);
        Pregunta p4 = new Pregunta(encuesta, 4, "Pregunta 4", false, tipoPregunta);
        Pregunta pDif = new Pregunta(encuestaDif, 10, "Pregunta Dif", false,  tipoPregunta);

        try {
            pDif.addPreguntaDependiente(p1);
        } catch (Exception e) {
            assertTrue(e instanceof EncuestaDiferente);
        }

        try {
            p4.addPreguntaDependiente(p1);
            p4.addPreguntaDependiente(p2);
            p4.addPreguntaDependiente(p3);
        } catch(Exception e) {
            fail("addPreguntaDependiente ha fallado inesperadamente");
        }
        assertTrue(p4.getDependientes().contains(p1));
        assertTrue(p4.getDependientes().contains(p2));
        assertTrue(p4.getDependientes().contains(p3));

        try {
            p1.addPreguntaDependiente(p4);
            fail("No ha saltado la excepcion PreguntaPosterior");
        } catch (Exception e) {
            assertTrue(e instanceof PreguntaPosterior);
        }

        try {
            p1.addPreguntaDependiente(p1);
            fail("No ha saltado la excepcion PreguntaPosterior");
        } catch (Exception e) {
            assertTrue(e instanceof PreguntaPosterior);
        }
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
    public void testSetImagen() {
        Pregunta p1 = new Pregunta(encuesta, 1, "Pregunta 1", false, tipoPregunta);
        p1.setImagen("imagen");
        assertEquals("imagen", p1.getImagen());
        p1.setImagen("");
        assertEquals("", p1.getImagen());
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

    @Test
    public void testRemovePreguntaDependiente() {
        Pregunta p1 = new Pregunta(encuesta, 1, "Pregunta 1", false, tipoPregunta);
        Pregunta p2 = new Pregunta(encuesta, 2, "Pregunta 2", false, tipoPregunta);
        Pregunta p3 = new Pregunta(encuesta, 3, "Pregunta 3", false, tipoPregunta);

        try {
            p3.addPreguntaDependiente(p1);
            p3.addPreguntaDependiente(p2);
        } catch (Exception e) {
            fail("addPreguntaDependiente ha fallado inesperadamente");
        }

        p3.removePreguntaDependiente(p1);
        assertFalse(p3.getDependientes().contains(p1));
        p3.removePreguntaDependiente(p2);
        assertFalse(p3.getDependientes().contains(p2));
    }
}
