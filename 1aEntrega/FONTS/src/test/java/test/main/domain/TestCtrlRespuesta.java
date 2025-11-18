package test.main.domain;

import java.util.HashMap;

import main.domain.CtrlRespuesta;
import main.domain.Pregunta;
import main.domain.FormatoLibre;
import main.domain.types.TDatosString;
import main.domain.RespuestaEncuesta;
import main.domain.Encuesta;
import main.domain.exceptions.NoHayRespuestasSeleccionadas;
import main.persistence.CtrlPersistencia;

import main.persistence.exceptions.FicheroNoExiste;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class TestCtrlRespuesta {
    /*
    // Esta clase nesteada es un helper/stub que emula las operaciones I/O con respuestas del CtrlPersistencia, para este Unit Test
    private class Memoria {
        HashMap<String, RespuestaEncuesta> almacen;

        public Memoria() {
            almacen = new java.util.HashMap<>();
        }

        public void guardar(RespuestaEncuesta r) {
            almacen.put(r.getCreador() + r.getEncuesta() + r.getEmailrespuesta(), r);
        }

        public RespuestaEncuesta cargar(String emailCreador, String tituloEncuesta, String emailRespuesta) {
            return almacen.get(emailCreador + tituloEncuesta + emailRespuesta);
        }

        public boolean existe(String emailCreador, String tituloEncuesta, String emailRespuesta) {
            return almacen.containsKey(emailCreador + tituloEncuesta + emailRespuesta);
        }
    }

    @Mock
    private Memoria memoria = new Memoria();
    */
    @Mock
    private CtrlPersistencia mockPersistencia;

    @InjectMocks
    private CtrlRespuesta cr;

    @Test (expected = IllegalArgumentException.class)
    public void testAlInicializar () {
        assertTrue(cr.getRespuestasCargadas().isEmpty());
        cr.getRespuestaActual();
    }

    @Test
    public void testCrearRespuesta () {
        Encuesta e = new Encuesta("Encuesta1", "a@x.com");
        cr.crearNuevaRespuesta(e, "b@x.com");
        RespuestaEncuesta r = cr.getRespuestaActual();
        assertNotNull(r);
        assertEquals("Encuesta1", r.getEncuesta());
        assertEquals("a@x.com", r.getCreador());
        assertEquals("b@x.com", r.getEmailrespuesta());
        assertTrue(cr.getRespuestasCargadas().contains(r));
    }

    @Test
    public void testCrearGuardarYCargarRespuesta () {
        Encuesta e = new Encuesta("Encuesta1", "a@x.com");
        cr.crearNuevaRespuesta(e, "b@x.com");
        assertEquals("b@x.com", cr.getRespuestaActual().getEmailrespuesta());
        cr.guardarRespuesta();
        cr.crearNuevaRespuesta(e, "c@x.com");
        assertEquals("c@x.com", cr.getRespuestaActual().getEmailrespuesta());
        cr.cargarRespuesta("a@x.com", "Encuesta1", "b@x.com");
        assertEquals("b@x.com", cr.getRespuestaActual().getEmailrespuesta());
    }

    @Test
    public void testCargarRespuestaInexistente () {
        try {
            cr.cargarRespuesta("no@existe.com", "EncuestaX", "tampoco@existe.com");
            fail("Deberia haber lanzado FicheroNoExiste");
        } catch (FicheroNoExiste e) {
            assertTrue(cr.getRespuestasCargadas().isEmpty());
        }
    }

    @Test
    public void testBorrarRespuestaActual () {
        Encuesta e = new Encuesta("Encuesta1", "a@x.com");
        cr.crearNuevaRespuesta(e, "b@x.com");
        assertEquals("b@x.com", cr.getRespuestaActual().getEmailrespuesta());
        assertTrue(cr.getRespuestasCargadas().contains(cr.getRespuestaActual()));
        try {
            cr.borrarRespuestaActual();
            fail("Deberia haber lanzado FicheroNoExiste");
        } catch (FicheroNoExiste f) {
            // Debido a que en el mock de la persistencia no se creen ficheros, no habria y tendra que saltar la excepcion
            assertTrue(cr.getRespuestasCargadas().isEmpty());
            try {
                cr.getRespuestaActual();
                fail("Deberia haber lanzado IllegalArgumentException");
            } catch (IllegalArgumentException i) {
                // Correcto, ya no hay respuesta actual
            }
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBorrarRespuestaActualVacia () {
        cr.borrarRespuestaActual();
    }

    @Test
    public void testAddDatoRespuesta () {
        Encuesta e = new Encuesta("Encuesta1", "a@x.com");
        Pregunta p = new Pregunta(e, 1, "Pregunta1", false, new FormatoLibre());
        e.addPregunta(p);
        cr.crearNuevaRespuesta(e, "b@x.com");
        cr.addDatoRespuesta(1, new TDatosString("Respuesta a Pregunta1"));
        assertEquals(1, cr.getMapaRespuestasActual().size());
        assertTrue(cr.getMapaRespuestasActual().containsKey(1));
        TDatosString tds = (TDatosString) cr.getDatoRespuesta(1);
        assertEquals("Respuesta a Pregunta1", tds.getTexto());
        /* No hay borrado
        cr.borrarDatoRespuesta(p); // Mejor pasarle el id
        assertTrue(cr.getMapaRespuestasActual().isEmpty());
        assertFalse(cr.getMapaRespuestasActual().containsKey(1));
        */
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddDatoRespuestaSinRespuestaActual () {
        Encuesta e = new Encuesta("Encuesta1", "a@x.com");
        Pregunta p = new Pregunta(e, 1, "Pregunta1", false, new FormatoLibre());
        e.addPregunta(p);
        cr.addDatoRespuesta(1, new TDatosString("Respuesta a Pregunta1"));
    }

    @Test (expected =  IllegalArgumentException.class)
    public void testDeseleccionarRespuesta () {
        Encuesta e = new Encuesta("Encuesta1", "a@x.com");
        cr.crearNuevaRespuesta(e, "b@x.com");
        assertEquals("b@x.com", cr.getRespuestaActual().getEmailrespuesta());
        assertTrue(cr.getRespuestasCargadas().contains(cr.getRespuestaActual()));
        cr.deseleccionarRespuesta();
        assertTrue(cr.getRespuestasCargadas().contains(cr.getRespuestaActual()));
        assertNull(cr.getRespuestaActual());
    }

    // No hace falta testear los getters de CtrlRespuesta porque son triviales
}
