package test.main.domain;

import main.domain.*;
import main.domain.exceptions.NoHayEncuestaSeleccionada;
import main.domain.exceptions.PreguntaNoEnEncuesta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestCtrlEncuesta {

    @Mock
    private TipoPregunta mockTipoPregunta;

    @InjectMocks
    private CtrlEncuesta ctrlEncuesta;

    @Test
    public void testCrearEncuesta() {
        ctrlEncuesta.crearEncuesta("Titulo", "creador@mail.com");
        assertNotNull(ctrlEncuesta.getEncuesta());
        assertEquals("Titulo", ctrlEncuesta.getEncuesta().getTitulo());
        assertEquals("creador@mail.com", ctrlEncuesta.getEncuesta().getCreador());
    }

    @Test
    public void testGuardarEncuestaYCargarEncuesta() {
        ctrlEncuesta.crearEncuesta("Titulo", "creador@mail.com");
        assertNotNull(ctrlEncuesta.getEncuesta());
        ctrlEncuesta.guardarEncuesta();
        ctrlEncuesta.cargarEncuesta("creador@mail.com", "Titulo");
        assertNotNull(ctrlEncuesta.getEncuesta());
    }

    @Test(expected = NoHayEncuestaSeleccionada.class)
    public void testGuardarEncuestaSinEncuesta() {
        ctrlEncuesta.guardarEncuesta(); // debe lanzar excepción
    }

    @Test
    public void testAddYRemovePregunta() {
        ctrlEncuesta.crearEncuesta("Encuesta", "a@mail.com");
        ctrlEncuesta.addPregunta(1, "¿Qué tal?", true, mockTipoPregunta);

        assertEquals(1, ctrlEncuesta.getEncuesta().getPreguntas().size());

        ctrlEncuesta.removePreguntaById(1);
        assertTrue(ctrlEncuesta.getEncuesta().getPreguntas().isEmpty());
    }

    @Test(expected = PreguntaNoEnEncuesta.class)
    public void testRemovePreguntaInexistente() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail");
        ctrlEncuesta.removePreguntaById(99);
    }

    @Test
    public void testSeleccionarPreguntaPorIdYCancelarSeleccion() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail");
        ctrlEncuesta.addPregunta(1, "Texto", true, mockTipoPregunta);

        Pregunta p = ctrlEncuesta.getEncuesta().getPreguntas().iterator().next();
        ctrlEncuesta.seleccionarPreguntaPorId(1);
        assertEquals(p.getId(), ctrlEncuesta.getIdPreguntaModificada());

        ctrlEncuesta.cancelarSeleccion();
        assertEquals(-1, ctrlEncuesta.getIdPreguntaModificada());
    }

    @Test(expected = PreguntaNoEnEncuesta.class)
    public void testSeleccionarPreguntaPorIdInexistente() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail");
        ctrlEncuesta.addPregunta(1, "Texto", true, mockTipoPregunta);
        ctrlEncuesta.seleccionarPreguntaPorId(999);
    }

    @Test
    public void testRemovePreguntaActual() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail");
        ctrlEncuesta.addPregunta(1, "Texto", true, mockTipoPregunta);

        Pregunta p = ctrlEncuesta.getEncuesta().getPreguntas().iterator().next();
        ctrlEncuesta.seleccionarPreguntaPorId(p.getId());
        ctrlEncuesta.removePreguntaActual();

        assertTrue(ctrlEncuesta.getEncuesta().getPreguntas().isEmpty());
        assertEquals(-1, ctrlEncuesta.getIdPreguntaModificada());
    }

    @Test
    public void testModificarPreguntaTextoSeleccionada() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail");
        ctrlEncuesta.addPregunta(1, "Original", true, mockTipoPregunta);
        Pregunta p = ctrlEncuesta.getEncuesta().getPreguntas().iterator().next();
        ctrlEncuesta.seleccionarPreguntaPorId(p.getId());

        ctrlEncuesta.modificarPreguntaTextoSeleccionada("Nuevo texto");
        assertEquals("Nuevo texto", p.getTexto());
    }

    @Test
    public void testModificarPreguntaTipoSeleccionada() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail");
        ctrlEncuesta.addPregunta(1, "Texto", true, mockTipoPregunta);
        Pregunta p = ctrlEncuesta.getEncuesta().getPreguntas().iterator().next();
        ctrlEncuesta.seleccionarPreguntaPorId(p.getId());

        TipoPregunta nuevoTipo = mock(TipoPregunta.class);
        ctrlEncuesta.modificarPreguntaTipoSeleccionada(nuevoTipo);
        assertEquals(nuevoTipo, ctrlEncuesta.getPreguntaById(1).getTipoPregunta());
    }

    @Test
    public void testSetDescripcionPreguntaSeleccionada() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail");
        ctrlEncuesta.addPregunta(1, "Texto", true, mockTipoPregunta);
        Pregunta p = ctrlEncuesta.getEncuesta().getPreguntas().iterator().next();
        ctrlEncuesta.seleccionarPreguntaPorId(p.getId());

        ctrlEncuesta.setDescripcionPreguntaSeleccionada("Descripción de prueba");
        assertEquals("Descripción de prueba", p.getDescripcion());
    }

    @Test
    public void testSetObligatorioPreguntaSeleccionada() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail@gmail.com");
        ctrlEncuesta.addPregunta(1, "Texto", false, mockTipoPregunta);
        Pregunta p = ctrlEncuesta.getEncuesta().getPreguntas().iterator().next();
        ctrlEncuesta.seleccionarPreguntaPorId(p.getId());

        ctrlEncuesta.setObligatorioPreguntaSeleccionada(true);
        assertTrue(p.isObligatorio());
    }

    @Test
    public void testFindPreguntaById() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail@gmail.com");
        ctrlEncuesta.addPregunta(1, "Texto", true, mockTipoPregunta);
        Pregunta p = ctrlEncuesta.getEncuesta().getPreguntas().iterator().next();
        Pregunta encontrada = ctrlEncuesta.findPreguntaById(p.getId());
        assertEquals(p, encontrada);
    }

    @Test
    public void testDeseleccionarEncuesta() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail");
        assertNotNull(ctrlEncuesta.getEncuesta());

        ctrlEncuesta.deseleccionarEncuesta();
        assertNull(ctrlEncuesta.getEncuesta());
    }

    @Test(expected = NoHayEncuestaSeleccionada.class)
    public void testAddPreguntaSinEncuesta() {
        ctrlEncuesta.addPregunta(1, "Texto", true, mockTipoPregunta);
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testGetPreguntaByIdSinEncuesta() {
        ctrlEncuesta.getPreguntaById(1);
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testRemovePreguntaByIdSinEncuesta() {
        ctrlEncuesta.removePreguntaById(1);
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testRemovePreguntaActualSinEncuesta() {
        ctrlEncuesta.removePreguntaActual();
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testSeleccionarPreguntaPorIdSinEncuesta() {
        ctrlEncuesta.seleccionarPreguntaPorId(1);
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testCancelarSeleccionSinEncuesta() {
        ctrlEncuesta.cancelarSeleccion();
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testModificarPreguntaTextoSeleccionadaSinEncuesta() {
        ctrlEncuesta.modificarPreguntaTextoSeleccionada("Nuevo texto");
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testModificarPreguntaTipoSeleccionadaSinEncuesta() {
        ctrlEncuesta.modificarPreguntaTipoSeleccionada(mockTipoPregunta);
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testSetDescripcionPreguntaSeleccionadaSinEncuesta() {
        ctrlEncuesta.setDescripcionPreguntaSeleccionada("Descripción");
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testSetObligatorioPreguntaSeleccionadaSinEncuesta() {
        ctrlEncuesta.setObligatorioPreguntaSeleccionada(true);
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testFindPreguntaByIdSinEncuesta() {
        ctrlEncuesta.findPreguntaById(1);
    }

    @Test (expected = NoHayEncuestaSeleccionada.class)
    public void testDeseleccionarEncuestaSinEncuesta() {
        ctrlEncuesta.deseleccionarEncuesta();
    }

    @Test (expected = PreguntaNoEnEncuesta.class)
    public void testModificarPreguntaTipoPorIdInexistente() {
        ctrlEncuesta.crearEncuesta("Encuesta", "mail");
        ctrlEncuesta.addPregunta(1, "Texto", true, mockTipoPregunta);
        ctrlEncuesta.modificarPreguntaTipoPorId(999, mockTipoPregunta);
    }
}
