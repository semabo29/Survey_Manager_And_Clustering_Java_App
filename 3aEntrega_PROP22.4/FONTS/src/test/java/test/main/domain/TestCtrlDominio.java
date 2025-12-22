package test.main.domain;

import main.domain.*;
import main.domain.exceptions.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestCtrlDominio {

    @Mock private CtrlEncuesta ctrlEncuesta;
    @Mock private CtrlRespuesta ctrlRespuesta;
    @Mock private CtrlPerfil ctrlPerfil;
    @Mock private Perfil perfilMock;
    @Mock private Encuesta encuestaMock;
    @Mock private RespuestaEncuesta respuestaMock;

    @InjectMocks
    private CtrlDominio ctrlDominio;

    //ENCUESTA
    @Test
    public void testCrearEncuesta() {
        when(ctrlPerfil.getPerfilCargado()).thenReturn(perfilMock);
        when(perfilMock.getEmail()).thenReturn("autor@test.com");
        when(ctrlEncuesta.getEncuesta()).thenReturn(encuestaMock);
        ctrlDominio.crearEncuesta("Titulo Nuevo");
        verify(ctrlEncuesta).crearEncuesta("Titulo Nuevo", "autor@test.com");
        verify(ctrlPerfil).addEncuestaCreada(encuestaMock);
    }

    @Test
    public void testImportarEncuesta() {
        ctrlDominio.importarEncuesta("Titulo", "correo@test.com");
        verify(ctrlEncuesta).cargarEncuesta("correo@test.com", "Titulo");
    }

    @Test
    public void testGuardarEncuesta() {
        ctrlDominio.guardarEncuesta();
        verify(ctrlEncuesta).guardarEncuesta();
    }

    @Test
    public void testGetEncuestaEnMemoria() {
        Encuesta encuesta = new Encuesta("T1", "mail");
        when(ctrlEncuesta.getEncuesta()).thenReturn(encuesta);
        Encuesta result = ctrlDominio.getEncuestaEnMemoria();
        assertEquals("T1", result.getTitulo());
    }

    @Test
    public void testDeseleccionarEncuesta() {
        ctrlDominio.deseleccionarEncuesta();
        verify(ctrlEncuesta).deseleccionarEncuesta();
    }

    @Test
    public void testAddPregunta() {
        TipoPregunta tipo = mock(TipoPregunta.class);
        ctrlDominio.addPregunta(1, "texto", true, tipo);
        verify(ctrlEncuesta).addPregunta(1, "texto", true, tipo);
    }

    @Test
    public void testRemovePreguntaById() {
        ctrlDominio.removePreguntaById(1);
        verify(ctrlEncuesta).removePreguntaById(1);
    }

    @Test
    public void testRemovePreguntaActual() {
        ctrlDominio.removePreguntaActual();
        verify(ctrlEncuesta).removePreguntaActual();
    }

    @Test
    public void testSeleccionarPreguntaPorId() {
        ctrlDominio.seleccionarPreguntaPorId(1);
        verify(ctrlEncuesta).seleccionarPreguntaPorId(1);
    }

    @Test
    public void testCancelarSeleccionPregunta() {
        ctrlDominio.cancelarSeleccionPregunta();
        verify(ctrlEncuesta).cancelarSeleccion();
    }

    @Test
    public void testModificarPreguntaTipoPorId() {
        TipoPregunta tipo = mock(TipoPregunta.class);
        ctrlDominio.modificarPreguntaTipoPorId(1, tipo);
        verify(ctrlEncuesta).modificarPreguntaTipoPorId(1, tipo);
    }

    @Test
    public void testModificarPreguntaTextoSeleccionada() {
        ctrlDominio.modificarPreguntaTextoSeleccionada("nuevo texto");
        verify(ctrlEncuesta).modificarPreguntaTextoSeleccionada("nuevo texto");
    }

    @Test
    public void testModificarPreguntaTipoSeleccionada() {
        TipoPregunta tipo = mock(TipoPregunta.class);
        ctrlDominio.modificarPreguntaTipoSeleccionada(tipo);
        verify(ctrlEncuesta).modificarPreguntaTipoSeleccionada(tipo);
    }

    @Test
    public void testSetDescripcionPreguntaSeleccionada() {
        ctrlDominio.setDescripcionPreguntaSeleccionada("desc");
        verify(ctrlEncuesta).setDescripcionPreguntaSeleccionada("desc");
    }

    @Test
    public void testSetObligatorioPreguntaSeleccionada() {
        ctrlDominio.setObligatorioPreguntaSeleccionada(true);
        verify(ctrlEncuesta).setObligatorioPreguntaSeleccionada(true);
    }

    @Test
    public void testCrearPregunta_FlujoCompleto() {
        HashMap<String, String> datos = new HashMap<>();
        datos.put("ID", "5");
        datos.put("TEXTO", "Pregunta Test");
        datos.put("OBLIGATORIO", "true");
        datos.put("DESCRIPCION", "Instrucciones");
        datos.put("TIPO", "Numerica");
        TipoPregunta tipoMock = mock(TipoPregunta.class);
        when(ctrlEncuesta.getTipo(datos)).thenReturn(tipoMock);
        ctrlDominio.crearPregunta(datos);
        verify(ctrlEncuesta).addPregunta(5, "Pregunta Test", true, tipoMock);
        verify(ctrlEncuesta).seleccionarPreguntaPorId(5);
        verify(ctrlEncuesta).setDescripcionPreguntaSeleccionada("Instrucciones");
    }

    //PERFIL
    @Test
    public void testCrearPerfil() {
        ctrlDominio.crearPerfil("email", "nombre", "pass");
        verify(ctrlPerfil).crearPerfil("email", "nombre", "pass");
    }

    @Test
    public void testImportarPerfil() {
        ctrlDominio.importarPerfil("email");
        verify(ctrlPerfil).cargarPerfil("email");
    }


    //RESPUESTA
    @Test
    public void testImportarRespuesta() {
        when(ctrlEncuesta.getEncuesta()).thenReturn(encuestaMock);
        when(encuestaMock.getCreador()).thenReturn("correo@test.com");
        when(encuestaMock.getTitulo()).thenReturn("EncuestaX");
        ctrlDominio.importarRespuesta("persona@mail.com");
        verify(ctrlRespuesta).cargarRespuesta("correo@test.com", "EncuestaX", "persona@mail.com");
    }

    @Test
    public void testGuardarRespuesta() {
        ctrlDominio.guardarRespuesta();
        verify(ctrlRespuesta).guardarRespuesta();
    }

    @Test
    public void testResponderEncuesta() {
        when(ctrlPerfil.getPerfilCargado()).thenReturn(perfilMock);
        when(perfilMock.getEmail()).thenReturn("responder@test.com");
        when(ctrlEncuesta.getEncuesta()).thenReturn(encuestaMock);
        when(ctrlRespuesta.getRespuestaActual()).thenReturn(respuestaMock);
        ctrlDominio.responderEncuesta();
        verify(ctrlRespuesta).crearNuevaRespuesta(encuestaMock, "responder@test.com");
        verify(ctrlPerfil).addRespuestaHecha(respuestaMock);
    }

    @Test
    public void testBorrarRespuestaActual_ConReferenciaAlPerfil() {
        // Verificamos que si hay una respuesta actual, se borra también de la lista del perfil
        when(ctrlRespuesta.getRespuestaActual()).thenReturn(respuestaMock);

        ctrlDominio.borrarRespuestaActual();

        verify(ctrlPerfil).removeRespuestaHecha(respuestaMock);
        verify(ctrlRespuesta).borrarRespuestaActual();
    }


    //ANALISIS
    @Test(expected = NoHayRespuestasSeleccionadas.class)
    public void testAnalizarEncuestaSinRespuestas() throws Exception {
        when(ctrlRespuesta.getRespuestasCargadas()).thenReturn(new HashSet<>());
        ctrlDominio.analizarEncuesta();
    }

    @Test(expected = EncuestaDiferente.class)
    public void testAnalizarEncuestaConRespuestasDeOtraEncuesta() throws Exception {
        HashSet<RespuestaEncuesta> respuestas = new HashSet<>();
        RespuestaEncuesta r = mock(RespuestaEncuesta.class);
        when(r.getEncuesta()).thenReturn("Encuesta B");
        respuestas.add(r);
        when(ctrlRespuesta.getRespuestasCargadas()).thenReturn(respuestas);
        when(ctrlEncuesta.getEncuesta()).thenReturn(encuestaMock);
        when(encuestaMock.getTitulo()).thenReturn("Encuesta A");
        ctrlDominio.analizarEncuesta();
    }

    @Test
    public void testAnalizarEncuestaValida() throws Exception {
        HashSet<RespuestaEncuesta> respuestas = new HashSet<>();
        RespuestaEncuesta r1 = mock(RespuestaEncuesta.class);
        RespuestaEncuesta r2 = mock(RespuestaEncuesta.class);
        when(r1.getEncuesta()).thenReturn("EncuestaSatisfaccion");
        when(r2.getEncuesta()).thenReturn("EncuestaSatisfaccion");
        respuestas.add(r1);
        respuestas.add(r2);
        when(ctrlRespuesta.getRespuestasCargadas()).thenReturn(respuestas);
        when(ctrlEncuesta.getEncuesta()).thenReturn(encuestaMock);
        when(encuestaMock.getTitulo()).thenReturn("EncuestaSatisfaccion");
        ctrlDominio.analizarEncuesta();
        verify(ctrlRespuesta).getRespuestasCargadas();
        verify(r1).getEncuesta();
        verify(r2).getEncuesta();
    }

    @Test(expected = AlgoritmoNoReconocido.class)
    public void testElegirAlgoritmoAnalisis_AlgoritmoInvalido() {
        ctrlDominio.elegirAlgoritmoAnalisis("Inexistente", "KMeans++");
    }

    @Test(expected = InicializadorNoReconocido.class)
    public void testElegirAlgoritmoAnalisis_InicializadorInvalido() {
        ctrlDominio.elegirAlgoritmoAnalisis("KMeans", "Raro");
    }

    @Test(expected = InicializadorYAlgoritmoIncompatibles.class)
    public void testElegirAlgoritmoAnalisis_Incompatibles() {
        ctrlDominio.elegirAlgoritmoAnalisis("KMedoids", "KMeans++");
    }

    //ERRORES Y EXCEPCIONES
    @Test(expected = NoHayPerfilCargado.class)
    public void testCrearEncuestaSinPerfilLanzaExcepcion() {
        when(ctrlPerfil.getPerfilCargado()).thenReturn(null);
        ctrlDominio.crearEncuesta("Titulo Prohibido");
    }

    @Test(expected = IncorrectPassword.class)
    public void testComprobarPasswordIncorrectaDeseleccionaPerfil() {
        when(ctrlPerfil.getPerfilCargado()).thenReturn(perfilMock);
        when(perfilMock.getContrasena()).thenReturn("password123");
        try {
            ctrlDominio.comprobarPassword("password_erronea");
        } catch (IncorrectPassword e) {
            verify(ctrlPerfil).deseleccionarPerfil();
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetK_Invalido() {
        ctrlDominio.setK(0);
    }
}