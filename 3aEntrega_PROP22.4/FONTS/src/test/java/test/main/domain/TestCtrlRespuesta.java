package test.main.domain;

import java.util.HashSet;

import main.domain.CtrlRespuesta;
import main.domain.Pregunta;
import main.domain.FormatoLibre;
import main.domain.types.TDatosString;
import main.domain.RespuestaEncuesta;
import main.domain.Encuesta;
import main.persistence.CtrlPersistencia;

import main.persistence.exceptions.LecturaNula;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestCtrlRespuesta {

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
        } catch (LecturaNula e) {
            assertTrue(cr.getRespuestasCargadas().isEmpty());
        }
    }

    @Test
    public void testBorrarRespuestaActual () {
        Encuesta e = new Encuesta("Encuesta1", "a@x.com");
        cr.crearNuevaRespuesta(e, "b@x.com");
        cr.guardarRespuesta();
        assertEquals("b@x.com", cr.getRespuestaActual().getEmailrespuesta());
        assertTrue(cr.getRespuestasCargadas().contains(cr.getRespuestaActual()));

        cr.borrarRespuestaActual();

        HashSet<RespuestaEncuesta> resps = cr.getRespuestasCargadas();
        boolean encontrado = false;
        for (RespuestaEncuesta r : resps) {
            if (r.getCreador() == "a@x.com" && r.getEncuesta() == "Encuesta1" && r.getEmailrespuesta() == "b@x.com") {
                encontrado = true;
            }
        }
        assertFalse(encontrado);
        try {
            cr.cargarRespuesta("a@x.com", "Encuesta1", "b@x.com");
            fail("Se esperaba una excepcion de LecturaNula");
        } catch (LecturaNula ex) {
            // Bien, no hagas nada
        }

        try {
            cr.getRespuestaActual();
            fail("Se esperaba una excepción de IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Bien, no hagas nada
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
    // El resto de metodos no se hacen tests estan muy acoplados a la persistencia que no se puede mockear correctamente
}
