package test.main.persistance;

import main.persistence.CtrlPersistencia;
import main.persistence.exceptions.*;
import main.persistence.GestorPerfil;
import main.persistence.GestorEncuesta;
import main.persistence.GestorRespuestaEncuesta;
import main.domain.Perfil;
import main.domain.Encuesta;
import main.domain.RespuestaEncuesta;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

@RunWith(value= MockitoJUnitRunner.class)
public class TestCtrlPersistencia {

    // Esta clase nesteada es un helper/stub que emula las operaciones I/O, para este Unit Test
    private class Memoria {
        HashMap<String,Object> almacen;

        public Memoria() {
            almacen = new java.util.HashMap<>();
        }

        public void guardar(String clave, Object valor) {
            almacen.put(clave, valor);
        }

        public Object cargar(String clave) {
            return almacen.get(clave);
        }
    }

    @Mock
    private GestorPerfil gp;
    @Mock
    private GestorEncuesta ge;
    @Mock
    private GestorRespuestaEncuesta gr;

    private Memoria memoria = new Memoria();

    @Before
    public void setup() {

        doAnswer(inv -> {
            Perfil p = inv.getArgument(0);
            memoria.guardar(p.getEmail(), p);
            return null;
        }).when(gp).guardarPerfil(Mockito.any(Perfil.class));

        when(gp.cargarPerfil(anyString())).thenAnswer(inv -> {
            String key = inv.getArgument(0);
            Object obj = memoria.cargar(key);
            if (obj == null) throw new LecturaNula();
            if (!(obj instanceof Perfil)) throw new FormatoInvalido();
            return (Perfil) obj;
        });

        doAnswer(inv -> {
            Encuesta e = inv.getArgument(0);
            memoria.guardar(e.getCreador()+e.getTitulo(), e);
            return null;
        }).when(ge).guardarEncuesta(Mockito.any(Encuesta.class));

        when(ge.cargarEncuesta(anyString(), anyString())).thenAnswer(inv -> {
            String emailCreador = inv.getArgument(0);
            String titulo = inv.getArgument(1);
            Object obj = memoria.cargar(emailCreador+titulo);
            if (obj == null) throw new LecturaNula();
            if (!(obj instanceof Encuesta)) throw new FormatoInvalido();
            return (Encuesta) obj;
        });

        doAnswer(inv -> {
            RespuestaEncuesta r = inv.getArgument(0);
            memoria.guardar(r.getCreador()+r.getEncuesta()+r.getEmailrespuesta(), r);
            return null;
        }).when(gr).guardarRespuestaEncuesta(Mockito.any(RespuestaEncuesta.class));

        when(gr.cargarRespuestaEncuesta(anyString(), anyString(), anyString())).thenAnswer(inv -> {
            String emailCreador = inv.getArgument(0);
            String titulo = inv.getArgument(1);
            String emailRespuesta = inv.getArgument(2);
            Object obj = memoria.cargar(emailCreador+titulo+emailRespuesta);
            if (obj == null) throw new LecturaNula();
            if (!(obj instanceof RespuestaEncuesta)) throw new FormatoInvalido();
            return (RespuestaEncuesta) obj;
        });

        CtrlPersistencia.getInstance().setGestores(gp, ge, gr);
    }

    @Test
    public void testGetInstance () {
        CtrlPersistencia cp = CtrlPersistencia.getInstance();
        assertNotNull(cp);
        CtrlPersistencia cp2 = CtrlPersistencia.getInstance();
        assertSame (cp, cp2);
    }

    @Test
    public void testGuardarYCargarPerfil () {
        Perfil p1 = new Perfil("foo@bar.com", "Foo", "password123");
        CtrlPersistencia.getInstance().guardarPerfil(p1);
        Perfil p2 = CtrlPersistencia.getInstance().cargarPerfil("foo@bar.com");
        assertEquals(p1.getEmail(), p2.getEmail());
        assertEquals(p1.getNombre(), p2.getNombre());
        assertEquals(p1.getContrasena(), p2.getContrasena());
    }

    @Test
    public void testGuardarYCargarEncuesta () {
        Encuesta e1 = new Encuesta("TFG-Foo", "foo@bar.com");
        CtrlPersistencia.getInstance().guardarEncuesta(e1);
        Encuesta e2 = CtrlPersistencia.getInstance().cargarEncuesta("foo@bar.com", "TFG-Foo");
        assertEquals(e1.getTitulo(), e2.getTitulo());
        assertEquals(e1.getCreador(), e2.getCreador());
    }

    @Test
    public void testGuardarYCargarRespuestaEncuesta () {
        RespuestaEncuesta r1 = new RespuestaEncuesta("TFG-Foo", "foo@bar.com", "buzz@bar.com");
        CtrlPersistencia.getInstance().guardarRespuestaEncuesta(r1);
        RespuestaEncuesta r2 = CtrlPersistencia.getInstance().cargarRespuestaEncuesta("foo@bar.com", "TFG-Foo", "buzz@bar.com");
        assertEquals(r1.getEncuesta(), r2.getEncuesta());
        assertEquals(r1.getCreador(), r2.getCreador());
        assertEquals(r1.getEmailrespuesta(), r2.getEmailrespuesta());
    }

    @Test (expected = LecturaNula.class)
    public void testCargarNoExistente () {
        Perfil p = CtrlPersistencia.getInstance().cargarPerfil("a@b.com");
    }

    @Test (expected = FormatoInvalido.class)
    public void testCargarFormatoInvalido () {
        memoria.guardar("a@b.com", "Esto sin duda no es un Perfil");
        Perfil p = CtrlPersistencia.getInstance().cargarPerfil("a@b.com");
    }
}
