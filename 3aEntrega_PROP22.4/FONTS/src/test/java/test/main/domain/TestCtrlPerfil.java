package test.main.domain;

import java.util.AbstractMap.SimpleEntry;

import main.domain.CtrlPerfil;
import main.domain.Perfil;
import main.domain.Encuesta;
import main.domain.RespuestaEncuesta;
import main.domain.exceptions.NoHayPerfilCargado;
import main.domain.exceptions.YaExistePerfil;
import main.persistence.CtrlPersistencia;

import main.persistence.exceptions.FicheroNoExiste;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;

import static org.junit.Assert.*;

import main.domain.exceptions.EmailInvalido;

@RunWith(MockitoJUnitRunner.class)
public class TestCtrlPerfil {

    @Mock
    private CtrlPersistencia mockPersistencia;

    @InjectMocks
    private CtrlPerfil cp;

    @Before
    public void setup () {
        // Creamos un perfil para todos los tests y asi evitar que salta YaExistePerfil cuando no debe, se omite testCrearPerfil
        try {
            cp.crearPerfil("foo@bar.com", "Foo", "password123");
        } catch (YaExistePerfil e) {
            // No se hace nada
        }
    }

    @After
    public void tearDown () {
        try {
            cp.borrarPerfilCargado();
        } catch (NoHayPerfilCargado e) {
            // No se hace nada
        } catch (FicheroNoExiste e) {
            // No se hace nada
        }
    }

    @Test
    public void cargarPerfil () {
        cp.cargarPerfil("foo@bar.com");
        Perfil p = cp.getPerfilCargado();
        assertNotNull(p);
        assertEquals("foo@bar.com", p.getEmail());
        assertEquals("Foo", p.getNombre());
        assertEquals("password123", p.getContrasena());
    }

    @Test
    public void testCrearPerfilYaExiste () {
        cp.cargarPerfil("foo@bar.com");
        try {
            cp.crearPerfil("foo@bar.com", "Foo", "password124");
            fail("Se esperaba una excepcion YaExistePerfil");
        } catch (YaExistePerfil e) {
            Perfil p = cp.getPerfilCargado();
            assertNotNull(p);
            assertEquals(p.getEmail(), "foo@bar.com");
            assertEquals(p.getNombre(), "Foo");
            assertEquals(p.getContrasena(), "password123");
        }
    }

    @Test
    public void testCrearPerfilEmailInvalido () {
        cp.cargarPerfil("foo@bar.com");
        try {
            cp.crearPerfil("invalid-email", "Foo", "password123");
            fail("Se esperaba una excepcion EmailInvalido");
        } catch (EmailInvalido e) {
            // Hay que comprobar que no se ha guardado nada
            Perfil p = cp.getPerfilCargado();
            assertNotNull(p);
            assertEquals(p.getEmail(), "foo@bar.com");
            assertEquals(p.getNombre(), "Foo");
            assertEquals(p.getContrasena(), "password123");
        }
    }

    @Test
    public void testAddEncuestaCreada () {
        cp.cargarPerfil("foo@bar.com");
        assertTrue(cp.getPerfilCargado().getEncuestasCreadas().isEmpty());
        Encuesta e = new Encuesta("Encuesta1", "foo@bar.com");
        cp.addEncuestaCreada(e);

        // Comprobar que se ha hecho el cambio en el perfil cargado
        Perfil p = cp.getPerfilCargado();
        assertEquals(1, p.getEncuestasCreadas().size());
        assertTrue(p.getEncuestasCreadas().contains(e.getTitulo()));
    }

    @Test
    public void testAddRespuestaHecha () {
        cp.cargarPerfil("foo@bar.com");
        assertTrue(cp.getPerfilCargado().getRespuestasHechas().isEmpty());
        RespuestaEncuesta r = new RespuestaEncuesta("Encuesta1", "bar@foo.com", "foo@bar.com");
        cp.addRespuestaHecha(r);

        // Comprobar que se ha hecho el cambio en el perfil cargado
        Perfil p = cp.getPerfilCargado();
        assertEquals(1, p.getRespuestasHechas().size());
        assertTrue(p.getRespuestasHechas().contains(new SimpleEntry<String,String>(r.getEncuesta(), r.getCreador())));
    }

    @Test (expected = NoHayPerfilCargado.class)
    public void testAddEncuestaCreadaSinPerfilCargado () {
        cp.deseleccionarPerfil();
        Encuesta e = new Encuesta("Encuesta1", "foo@bar.com");
        cp.addEncuestaCreada(e);
    }

    @Test (expected = NoHayPerfilCargado.class)
    public void testAddRespuestaHechaSinPerfilCargado () {
        cp.deseleccionarPerfil();
        RespuestaEncuesta r = new RespuestaEncuesta("Encuesta1", "bar@foo.com", "foo@bar.com");
        cp.addRespuestaHecha(r);
    }

    @Test
    public void testDeseleccionarPerfil () {
        cp.cargarPerfil("foo@bar.com");
        assertNotNull(cp.getPerfilCargado());
        cp.deseleccionarPerfil();
        assertNull(cp.getPerfilCargado());
    }

    @Test (expected = NoHayPerfilCargado.class)
    public void testBorrarPerfilCargadoSinPerfilCargado () {
        cp.deseleccionarPerfil();
        cp.borrarPerfilCargado();
    }

    // El resto de metodos no se hacen tests estan muy acoplados a la persistencia que no se puede mockear correctamente
}
