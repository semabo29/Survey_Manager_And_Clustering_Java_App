package test.main.domain;

import main.domain.Pregunta;
import main.domain.Encuesta;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
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
    @Mock
    private Pregunta p3;

    private Encuesta encuesta;

    @Before
    public void setUp() {
        encuesta = new Encuesta("Encuesta Test", "test@gmail.com");
        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        when(p3.getId()).thenReturn(3);
    }

    @Test
    public void testCreadora() {
        assertEquals("Encuesta Test", encuesta.getTitulo());
        assertEquals("test@gmail.com", encuesta.getCreador());
        assertTrue(encuesta.getPreguntas().isEmpty());
    }

    @Test
    public void testAddPregunta() {
        encuesta.addPregunta(p1);
        encuesta.addPregunta(p2);

        Set<Pregunta> preguntas = encuesta.getPreguntas();
        assertEquals(2, preguntas.size());
        assertTrue(preguntas.contains(p1));
        assertTrue(preguntas.contains(p2));
    }

    @Test
    public void testRemovePreguntaYRenumeracion() {
        encuesta.addPregunta(p1);
        encuesta.addPregunta(p2);
        encuesta.addPregunta(p3);

        encuesta.removePregunta(p1);

        assertEquals(2, encuesta.getPreguntas().size());
        assertFalse(encuesta.getPreguntas().contains(p1));
        assertTrue(encuesta.getPreguntas().contains(p2));
        assertTrue(encuesta.getPreguntas().contains(p3));
        verify(p2).setId(1);
        verify(p3).setId(2);
    }

    @Test
    public void testAddPreguntaMismoIdReemplaza() {
        Pregunta p1Repetida = mock(Pregunta.class);
        when(p1Repetida.getId()).thenReturn(1);

        encuesta.addPregunta(p1);
        encuesta.addPregunta(p1Repetida);

        assertEquals(1, encuesta.getPreguntas().size());
        assertTrue(encuesta.getPreguntas().contains(p1Repetida));
        assertFalse(encuesta.getPreguntas().contains(p1));
    }

    @Test
    public void testSetPreguntas() {
        HashSet<Pregunta> nuevasPreguntas = new HashSet<>();
        nuevasPreguntas.add(p1);
        nuevasPreguntas.add(p2);

        encuesta.setPreguntas(nuevasPreguntas);

        HashSet<Pregunta> preguntas = encuesta.getPreguntas();
        assertEquals(2, preguntas.size());
        assertTrue(preguntas.contains(p1));
        assertTrue(preguntas.contains(p2));
    }

    @Test
    public void testConsistenciaHashSetTrasEliminar() {
        encuesta.addPregunta(p1);
        encuesta.addPregunta(p2);

        encuesta.removePregunta(p1);

        assertTrue(encuesta.getPreguntas().contains(p2));
    }
}