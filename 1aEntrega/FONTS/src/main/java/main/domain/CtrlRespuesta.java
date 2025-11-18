package main.domain;

import main.domain.exceptions.NoHayRespuestasSeleccionadas;
import main.domain.types.TDatos;
import main.persistence.CtrlPersistencia;
import java.util.HashSet;
import java.util.TreeMap;

public class CtrlRespuesta {
        private RespuestaEncuesta respuestaActual;
        private HashSet<RespuestaEncuesta> respuestasCargadas;

        public CtrlRespuesta() {
            this.respuestaActual = null;
            this.respuestasCargadas = new HashSet<>();
        }

    public void cargarRespuesta(String emailCreador, String titulo, String emailRespuesta) {
        RespuestaEncuesta respuestaCargada = CtrlPersistencia.getInstance().cargarRespuestaEncuesta(emailCreador, titulo, emailRespuesta);

        if (respuestaCargada != null) {
            this.respuestaActual = respuestaCargada;
            this.respuestasCargadas.add(respuestaCargada);
        }
    }

    public void cargarRespuestas(String emailCreador, String titulo) {
        this.respuestasCargadas = CtrlPersistencia.getInstance().cargarRespuestasDeEncuesta(emailCreador, titulo);
    }


    public void crearNuevaRespuesta(Encuesta encuesta, String emailAutorRespuesta) {
        if (encuesta == null) {
            throw new IllegalArgumentException("La encuesta no puede ser null");
        }
        if (emailAutorRespuesta == null) {
            throw new IllegalArgumentException("El email del autor de la respuesta no puede ser null");
        }

        String tituloEncuesta = encuesta.getTitulo(); //
        String emailCreadorEncuesta = encuesta.getCreador(); //

        RespuestaEncuesta nuevaRespuesta = new RespuestaEncuesta(
                tituloEncuesta,
                emailCreadorEncuesta,
                emailAutorRespuesta
        );

        this.respuestaActual = nuevaRespuesta;
        this.respuestasCargadas.add(nuevaRespuesta);
    }

        public void guardarRespuesta() {
            checkRespuestaActual();
            CtrlPersistencia.getInstance().guardarRespuestaEncuesta(this.respuestaActual);
        }

        public void borrarRespuestaActual() {
        checkRespuestaActual();

        String emailCreador = this.respuestaActual.getEmailrespuesta(); // Creador de la encuesta
        String titulo = this.respuestaActual.getEncuesta();         // Título de la encuesta
        String emailRespuesta = this.respuestaActual.getCreador();

        this.respuestasCargadas.remove(this.respuestaActual);
        this.respuestaActual = null;
        CtrlPersistencia.getInstance().borrarRespuestaEncuesta(emailCreador, titulo, emailRespuesta);
    }
/*
    public void borrarDatoRespuesta(Pregunta pregunta) {
        checkRespuestaActual();
        this.respuestaActual.borrarRespuesta(pregunta);
    }

 */

        public void addDatoRespuesta(int idPregunta, TDatos datos) {
            checkRespuestaActual();
            this.respuestaActual.addRespuesta(idPregunta, datos);
        }

        public RespuestaEncuesta getRespuestaActual() {
            checkRespuestaActual();
            return this.respuestaActual;
        }

        public HashSet<RespuestaEncuesta> getRespuestasCargadas() {
            return this.respuestasCargadas;
        }

        public TDatos getDatoRespuesta(int idPregunta) {
            checkRespuestaActual();
            return this.respuestaActual.getIdRespuesta().get(idPregunta);
        }

        public TreeMap<Integer, TDatos> getMapaRespuestasActual() {
            checkRespuestaActual();
            return this.respuestaActual.getIdRespuesta();
        }

        public void deseleccionarRespuesta() {
            checkRespuestaActual();
            this.respuestaActual = null;
        }


        private void checkRespuestaActual() {
            if (this.respuestaActual == null) {
                throw new IllegalArgumentException("No hay ninguna respuesta");
            }
        }
}
