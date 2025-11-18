package main.persistence;

import main.domain.Perfil;
import main.domain.Encuesta;
import main.domain.RespuestaEncuesta;
import java.util.HashSet;

public class CtrlPersistencia {
    private static CtrlPersistencia INSTANCE = new CtrlPersistencia();

    private GestorPerfil gestorPerfil;
    private GestorEncuesta gestorEncuesta;
    private GestorRespuestaEncuesta gestorRespuestaEncuesta;

    private CtrlPersistencia() {
        gestorPerfil = new GestorPerfil();
        gestorEncuesta = new GestorEncuesta();
        gestorRespuestaEncuesta = new GestorRespuestaEncuesta();
    }

    // Metodo para testing, no usar fuera de los tests
    public void setGestores (GestorPerfil gp, GestorEncuesta ge, GestorRespuestaEncuesta gr) {
        gestorPerfil = gp;
        gestorEncuesta = ge;
        gestorRespuestaEncuesta = gr;
    }

    public static CtrlPersistencia getInstance() {
        return INSTANCE;
    }

    public void guardarPerfil(Perfil perfil) {
        gestorPerfil.guardarPerfil(perfil);
    }

    public Perfil cargarPerfil(String email) {
        return gestorPerfil.cargarPerfil(email);
    }

    public void borrarPerfil(String email) {
        gestorPerfil.borrarPerfil(email);
    }

    public void guardarEncuesta(Encuesta encuesta) {
        gestorEncuesta.guardarEncuesta(encuesta);
    }

    public Encuesta cargarEncuesta(String emailCreador, String titulo) {
        return gestorEncuesta.cargarEncuesta(emailCreador, titulo);
    }

    public void borrarEncuesta(String emailCreador, String titulo) {
        gestorEncuesta.borrarEncuesta(emailCreador, titulo);
    }

    public void guardarRespuestaEncuesta(RespuestaEncuesta respuesta) {
        gestorRespuestaEncuesta.guardarRespuestaEncuesta(respuesta);
    }

    public RespuestaEncuesta cargarRespuestaEncuesta(String emailCreador, String titulo, String emailRespuesta) {
        return gestorRespuestaEncuesta.cargarRespuestaEncuesta(emailCreador, titulo, emailRespuesta);
    }

    public void borrarRespuestaEncuesta(String emailCreador, String titulo, String emailRespuesta) {
        gestorRespuestaEncuesta.borrarRespuestaEncuesta(emailCreador, titulo, emailRespuesta);
    }

    public HashSet<RespuestaEncuesta> cargarRespuestasDeEncuesta(String emailCreador, String titulo) {
        return gestorRespuestaEncuesta.cargarRespuestasDeEncuesta(emailCreador, titulo);
    }
}
