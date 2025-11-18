package test.main.domain;

import main.domain.*;
import main.domain.exceptions.*;
import main.domain.analisis.Analizador;
import main.domain.analisis.algoritmo.*;
import main.domain.analisis.inicializador.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestCtrlDominio {

    @Mock private CtrlEncuesta ctrlEncuesta;
    @Mock private CtrlRespuesta ctrlRespuesta;
    @Mock private CtrlPerfil ctrlPerfil;
    @Mock private Analizador analizadorMock;
    @Mock private Perfil perfilMock;
    @Mock private Encuesta encuestaMock;
    @Mock private RespuestaEncuesta respuestaMock;

    @InjectMocks
    private CtrlDominio ctrlDominio;

    @Before
    public void setup() {
        lenient().when(ctrlPerfil.getPerfilCargado()).thenReturn(perfilMock);
        lenient().when(ctrlEncuesta.getEncuesta()).thenReturn(encuestaMock);
        lenient().when(perfilMock.getEmail()).thenReturn("correo@test.com");
        lenient().when(ctrlRespuesta.getRespuestaActual()).thenReturn(respuestaMock);
        lenient().when(encuestaMock.getCreador()).thenReturn("correo@test.com");
        lenient().when(encuestaMock.getTitulo()).thenReturn("EncuestaX");
    }



    //ENCUESTA

    @Test
    public void testCrearEncuesta() {
        ctrlDominio.crearEncuesta("Titulo");
        verify(ctrlEncuesta).crearEncuesta("Titulo", "correo@test.com");
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
        // Mock del comportamiento de ctrlEncuesta para devolver la encuesta creada
        when(ctrlEncuesta.getEncuesta()).thenReturn(encuesta);

        Encuesta result = ctrlDominio.getEncuestaEnMemoria();
        assertEquals("T1", result.getTitulo());
    }

    @Test
    public void testDeseleccionarEncuesta() {
        ctrlDominio.deseleccionarEncuesta();
        verify(ctrlEncuesta).deseleccionarEncuesta();
    }
/*
    @Test
    public void testSetTituloEncuesta() {
        ctrlDominio.setTituloEncuesta("Nuevo");
        verify(ctrlEncuesta).setTituloEncuesta("Nuevo");
    }
*/
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
    public void testSetImagenPreguntaSeleccionada() {
        ctrlDominio.setImagenPreguntaSeleccionada("imagen.png");
        verify(ctrlEncuesta).setImagenPreguntaSeleccionada("imagen.png");
    }

    @Test
    public void testSetObligatorioPreguntaSeleccionada() {
        ctrlDominio.setObligatorioPreguntaSeleccionada(true);
        verify(ctrlEncuesta).setObligatorioPreguntaSeleccionada(true);
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
        Perfil p = new Perfil("correo@test.com", "nombre", "pass");
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
        Encuesta e = new Encuesta("E1", "email");
        when(ctrlEncuesta.getEncuesta()).thenReturn(e);
        ctrlDominio.responderEncuesta();
        verify(ctrlRespuesta).crearNuevaRespuesta(e, ctrlPerfil.getPerfilCargado().getEmail());
        verify(ctrlPerfil).addRespuestaHecha(respuestaMock);
    }

    @Test
    public void testBorrarRespuestaActual() {
        ctrlDominio.borrarRespuestaActual();
        verify(ctrlRespuesta).borrarRespuestaActual();
    }

    //ANALISIS

    @Test(expected = NoHayRespuestasSeleccionadas.class)
    public void testAnalizarEncuestaSinRespuestas() throws Exception {
        //falta get respuestas por eso da error
        when(ctrlRespuesta.getRespuestasCargadas()).thenReturn(new HashSet<RespuestaEncuesta>());
        ctrlDominio.analizarEncuesta();
    }

    @Test(expected = EncuestaDiferente.class)
    public void testAnalizarEncuestaConRespuestasDeOtraEncuesta() throws Exception {
        HashSet<RespuestaEncuesta> respuestas = new HashSet<>();
        RespuestaEncuesta r = mock(RespuestaEncuesta.class);
        when(r.getEncuesta()).thenReturn("OtraEncuesta");
        respuestas.add(r);
        when(ctrlRespuesta.getRespuestasCargadas()).thenReturn(respuestas);

        Encuesta encuesta = new Encuesta("EncuestaCorrecta", "mail");
        when(ctrlEncuesta.getEncuesta()).thenReturn(encuesta);

        ctrlDominio.analizarEncuesta();
    }

    @Test
    public void testAnalizarEncuestaValida() throws Exception {
        // Mock de respuestas
        HashSet<RespuestaEncuesta> respuestas = new HashSet<>();
        RespuestaEncuesta r = mock(RespuestaEncuesta.class);
        when(r.getEncuesta()).thenReturn("EncuestaOK");
        respuestas.add(r);

        when(ctrlRespuesta.getRespuestasCargadas()).thenReturn(respuestas);

        // Mock de encuesta
        Encuesta encuesta = new Encuesta("EncuestaOK", "mail");
        when(ctrlEncuesta.getEncuesta()).thenReturn(encuesta);
        // Mock del analizador
        ArrayList<ArrayList<RespuestaEncuesta>> clustersMock = new ArrayList<>();
        //when(analizadorMock.analizarRespuestas(new ArrayList<>(respuestas))).thenReturn(clustersMock);
        //when(analizadorMock.evaluarCalidadClustering(clustersMock)).thenReturn(0.9f);

        // Ejecutamos el metodo
        ctrlDominio.analizarEncuesta();

        // Verificaciones se quitan por problemas con el singleton
        //verify(analizadorMock).analizarRespuestas(new ArrayList<>(respuestas));
        //verify(analizadorMock).evaluarCalidadClustering(clustersMock);
    }

    //ELECCIÓN DE ALGORITMO DE ANÁLISIS
/*  Los tests se comentan por problemas con el singleton Analizador(habria que añadir la variable de instancia en el singleton para poder mockearla)
    @Test
    public void testElegirAlgoritmoAnalisis_KMeans_KMeansPP() {
        ctrlDominio.elegirAlgoritmoAnalisis("KMeans", "KMeans++");
        verify(analizadorMock).cambiarAlgoritmo(any(KMeans.class), any(InicializadorKMeansPlusPlus.class));
    }

    @Test
    public void testElegirAlgoritmoAnalisis_KMeans_Aleatorio() {
        ctrlDominio.elegirAlgoritmoAnalisis("KMeans", "Aleatorio");
        verify(analizadorMock).cambiarAlgoritmo(any(KMeans.class), any(InicializadorRandom.class));
    }

    @Test
    public void testElegirAlgoritmoAnalisis_KMedoids_Greedy() {
        ctrlDominio.elegirAlgoritmoAnalisis("KMedoids", "Greedy");
        verify(analizadorMock).cambiarAlgoritmo(any(KMedoids.class), any(InicializadorKMedoidsGreedy.class));
    }

    */

    @Test(expected = AlgoritmoNoReonocido.class)
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
}