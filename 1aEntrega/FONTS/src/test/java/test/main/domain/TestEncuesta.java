package test.main.domain;

import main.domain.Pregunta;
import main.domain.Encuesta;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestEncuesta {
    @Mock
    private Pregunta p1;
    @Mock
    private Pregunta p2;

    @Test
    public void testCreadora() {
        Encuesta e = new Encuesta("Encuesta Test", "test@gmail.com");
        assertEquals("Encuesta Test", e.getTitulo());
        assertEquals("test@gmail.com", e.getCreador());
        assertTrue(e.getPreguntas().isEmpty());
    }

    @Test
    public void testAddPregunta() {
        Encuesta e = new Encuesta("Encuesta Test", "test@gmail.com");
        e.addPregunta(p1);
        e.addPregunta(p2);

        Set<Pregunta> preguntas = e.getPreguntas();
        assertEquals(2, preguntas.size());
        assertTrue(preguntas.contains(p1));
        assertTrue(preguntas.contains(p2));
    }

    @Test
    public void testRemovePregunta() {
        Encuesta e = new Encuesta("Encuesta Test", "test@gmail.com");
        e.addPregunta(p1);
        e.addPregunta(p2);

        e.removePregunta(p1);

        Set<Pregunta> preguntas = e.getPreguntas();
        assertEquals(1, preguntas.size());
        assertFalse(preguntas.contains(p1));
        assertTrue(preguntas.contains(p2));
    }

    @Test
    public void testSetPreguntas() {
        Encuesta e = new Encuesta("Encuesta Test", "test@gmail.com");
        HashSet<Pregunta> nuevasPreguntas = new HashSet<>();
        nuevasPreguntas.add(p1);
        nuevasPreguntas.add(p2);
        e.setPreguntas(nuevasPreguntas);
        Set<Pregunta> preguntas = e.getPreguntas();
        assertEquals(2, preguntas.size());
        assertTrue(preguntas.contains(p1));
        assertTrue(preguntas.contains(p2));
    }

    /* eliminado por problemas de integridad
    @Test
    public void testSetTitulo() {
        Encuesta e = new Encuesta("Encuesta Test", "test@gmail.com");
        e.setTitulo("Nuevo Titulo");
        assertEquals("Nuevo Titulo", e.getTitulo());
    }
    */
}
