package test.main.domain;

import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;

import main.domain.CtrlPerfil;
import main.domain.Perfil;
import main.domain.Encuesta;
import main.domain.RespuestaEncuesta;
import main.persistence.CtrlPersistencia;

import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import main.domain.exceptions.EmailInvalido;

@RunWith(MockitoJUnitRunner.class)
public class TestCtrlPerfil {
    /*
    // Esta clase nesteada es un helper/stub que emula las operaciones I/O con perfiles del CtrlPersistencia, para este Unit Test
    private class Memoria {
        HashMap<String,Perfil> almacen;

        public Memoria() {
            almacen = new java.util.HashMap<>();
        }

        public void guardar (Perfil p) {
            almacen.put(p.getEmail(), p);
        }

        public Perfil cargar (String email) {
            return almacen.get(email);
        }

        public boolean existe (String email) {
            return almacen.containsKey(email);
        }
    }

    @Mock
    private Memoria memoria = new Memoria();
    */

    @Mock
    private CtrlPersistencia mockPersistencia;

    @InjectMocks
    private CtrlPerfil cp;

    @Test
    public void testAlInicializar () {
        assertNull(cp.getPerfilCargado());
    }

    @Test
    public void testCrearPerfil () {
        cp.crearPerfil("foo@bar.com", "Foo", "password123");
        Perfil p = cp.getPerfilCargado();
        assertNotNull(p);
        assertEquals("foo@bar.com", p.getEmail());
        assertEquals("Foo", p.getNombre());
        assertEquals("password123", p.getContrasena());
    }

    @Test
    public void testCrearYCargarPerfil () {
        cp.crearPerfil("foo@bar.com", "Foo", "password123");
        cp.crearPerfil("bar@foo.com", "Bar", "password124");
        Perfil p = cp.getPerfilCargado();
        assertNotNull(p);
        assertEquals("bar@foo.com", p.getEmail());
        assertEquals("Bar", p.getNombre());
        assertEquals("password124", p.getContrasena());
        cp.cargarPerfil("foo@bar.com");
        p = cp.getPerfilCargado();
        assertNotNull(p);
        assertEquals("foo@bar.com", p.getEmail());
        assertEquals("Foo", p.getNombre());
        assertEquals("password123", p.getContrasena());
    }

    @Test
    public void testCrearPerfilEmailInvalido () {
        try {
            cp.crearPerfil("invalid-email", "Foo", "password123");
            fail("Se esperaba una excepcion EmailInvalido");
        } catch (EmailInvalido e) {
            // Hay que comprobar que no se ha guardado nada
            Perfil p = cp.getPerfilCargado();
            assertNull(p);
        }
    }

    @Test
    public void testAddEncuestaCreada () {
        cp.crearPerfil("foo@bar.com", "Foo", "password123");
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
        cp.crearPerfil("foo@bar.com", "Foo", "password123");
        assertTrue(cp.getPerfilCargado().getRespuestasHechas().isEmpty());
        RespuestaEncuesta r = new RespuestaEncuesta("Encuesta1", "bar@foo.com", "foo@bar.com");
        cp.addRespuestaHecha(r);

        // Comprobar que se ha hecho el cambio en el perfil cargado
        Perfil p = cp.getPerfilCargado();
        assertEquals(1, p.getRespuestasHechas().size());
        assertTrue(p.getRespuestasHechas().contains(new SimpleEntry<String,String>(r.getEncuesta(), r.getCreador())));
    }

    @Test (expected = IllegalStateException.class)
    public void testAddEncuestaCreadaSinPerfilCargado () {
        cp = new CtrlPerfil();
        Encuesta e = new Encuesta("Encuesta1", "foo@bar.com");
        cp.addEncuestaCreada(e);
    }

    @Test (expected = IllegalStateException.class)
    public void testAddRespuestaHechaSinPerfilCargado () {
        cp = new CtrlPerfil();
        RespuestaEncuesta r = new RespuestaEncuesta("Encuesta1", "bar@foo.com", "foo@bar.com");
        cp.addRespuestaHecha(r);
    }

    // Los tests de removers se excluyen dado a que la funcionalidad no se utiliza por ahora

}
