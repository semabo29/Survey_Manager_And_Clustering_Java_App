package test.main.domain;

import main.domain.*;
import main.domain.types.TDatos;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestRespuestaEncuesta {

    @Mock
    private Pregunta mockPregunta;
    @Mock
    private Pregunta mockPregunta2;
    @Mock
    private Encuesta mockEncuesta;
    @Mock
    private Encuesta mockEncuestaDiferente;
    @Mock
    private TDatos mockDatos1;
    @Mock
    private TDatos mockDatos2;
    @Mock
    private RespuestaEncuesta mockOtraRespuesta;
    @Mock
    private TDatos mockOtroDato1;
    @Mock
    private TDatos mockOtroDato2;

    @Test
    public void testConstructoraExitosa() {
        RespuestaEncuesta res = new RespuestaEncuesta("Titulo Encuesta", "autor@email.com", "creador@email.com");

        assertEquals("Titulo Encuesta", res.getEncuesta());
        assertEquals("autor@email.com", res.getCreador());
        assertEquals("creador@email.com", res.getEmailrespuesta());
        assertNotNull(res.getIdRespuesta());
        assertTrue(res.getIdRespuesta().isEmpty());
    }


    @Test
    public void testSetters() {
        RespuestaEncuesta res = new RespuestaEncuesta("Encuesta1", "Autor1", "Creador1");

        res.setPerfil("Autor2");
        assertEquals("Autor2", res.getCreador());

        res.setEmailrespuesta("Creador2");
        assertEquals("Creador2", res.getEmailrespuesta());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPerfilNull() {
        RespuestaEncuesta res = new RespuestaEncuesta("Encuesta1", "Autor1", "Creador1");
        res.setPerfil(null);
    }

    @Test
    public void testAddRespuesta() {
        RespuestaEncuesta res = new RespuestaEncuesta("Encuesta1", "Autor1", "Creador1");

        res.addRespuesta(1, mockDatos1);

        assertEquals(1, res.getIdRespuesta().size());
        assertEquals(mockDatos1, res.getIdRespuesta().get(1));

        res.addRespuesta(1, mockDatos2);
        assertEquals(1, res.getIdRespuesta().size());
        assertEquals(mockDatos2, res.getIdRespuesta().get(1));
    }

    @Test
    public void testGetDatos() {
        RespuestaEncuesta res = new RespuestaEncuesta("Encuesta1", "Autor1", "Creador1");
        res.addRespuesta(1, mockDatos1);
        res.addRespuesta(5, mockDatos2);

        ArrayList<TDatos> datos = res.getDatos();

        assertEquals(2, datos.size());
        assertTrue(datos.contains(mockDatos1));
        assertTrue(datos.contains(mockDatos2));
    }
}