package test.main.domain;

import main.domain.exceptions.EmailInvalido;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import main.domain.Perfil;
import main.domain.Encuesta;
import main.domain.RespuestaEncuesta;

@RunWith(MockitoJUnitRunner.class)
public class TestPerfil {
    @Mock
    private Encuesta encuesta1;
    @Mock
    private Encuesta encuesta2;
    @Mock
    private RespuestaEncuesta respuesta1;
    @Mock
    private RespuestaEncuesta respuesta2;

    @Test
    public void testCreadora () {
        Perfil p = new Perfil("foo@bar.com", "Foo", "password123");

        assertEquals("foo@bar.com", p.getEmail());
        assertEquals("Foo", p.getNombre());
        assertEquals("password123", p.getContrasena());
        assertTrue(p.getEncuestasCreadas().isEmpty());
        assertTrue(p.getRespuestasHechas().isEmpty());
    }

    @Test (expected = EmailInvalido.class)
    public void testCreadoraEmailInvalido () {
        Perfil p = new Perfil("invalid-email", "Foo", "password123");
    }

    @Test
    public void testSetters () {
        Perfil p = new Perfil("foo@bar.com", "Foo", "password123");

        p.setNombre("Bar");
        p.setContrasena("newpassword");

        assertEquals("Bar", p.getNombre());
        assertEquals("newpassword", p.getContrasena());
    }

    @Test
    public void testAddersAndRemovers () {
        Perfil p = new Perfil("foo@bar.com", "Foo", "password123");

        p.addEncuestaCreada(encuesta1);
        p.addRespuestaHecha(respuesta1);

        HashSet<String> encuestas = p.getEncuestasCreadas();
        assertEquals(1, encuestas.size());
        assertTrue(encuestas.contains(encuesta1.getTitulo()));

        HashSet<SimpleEntry<String,String>> respuestas = p.getRespuestasHechas();
        assertEquals(1, respuestas.size());
        assertTrue(respuestas.contains(new SimpleEntry<>(respuesta1.getEncuesta(), respuesta1.getCreador())));

        p.removeEncuestaCreada(encuesta1);
        p.removeRespuestaHecha(respuesta1);

        assertTrue(p.getEncuestasCreadas().isEmpty());
        assertTrue(p.getRespuestasHechas().isEmpty());
    }
}
