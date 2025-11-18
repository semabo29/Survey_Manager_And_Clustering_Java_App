package main.domain;

import main.domain.exceptions.NoHayEncuestaSeleccionada;
import main.domain.exceptions.NoHayPreguntaSeleccionada;
import main.domain.exceptions.PreguntaNoEnEncuesta;
import main.persistence.CtrlPersistencia;

public class CtrlEncuesta {
    private Encuesta encuesta;
    private int idPreguntaModificada = -1;

    public CtrlEncuesta() {
        this.encuesta = null;
    }
    public void crearEncuesta(String titulo, String emailCreador) {
        Encuesta e = new Encuesta(titulo, emailCreador);
        this.encuesta = e;
        CtrlPersistencia.getInstance().guardarEncuesta(e);
    }

    public void guardarEncuesta() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        CtrlPersistencia.getInstance().guardarEncuesta(this.encuesta);
    }

    public void cargarEncuesta(String email, String titulo) {
        this.encuesta = CtrlPersistencia.getInstance().cargarEncuesta(email, titulo);
        this.idPreguntaModificada = -1;
    }
    public Encuesta getEncuesta() {
        return this.encuesta;
    }
    public int getIdPreguntaModificada() {return this.idPreguntaModificada;}

/*   eliminado por problemas de integridad
    public void setTituloEncuesta(String nuevoTitulo) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        this.encuesta.setTitulo(nuevoTitulo);
    }
*/
    public void addPregunta(int id, String texto, boolean obligatorio, TipoPregunta tipo) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = new Pregunta(this.encuesta, id, texto, obligatorio, tipo);
        this.encuesta.addPregunta(p);
    }

    public Pregunta getPreguntaById(int id) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(id);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + id + " no encontrada");
        return p;
    }

    public void removePreguntaById(int id) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(id);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + id + " no encontrada");
        this.encuesta.removePregunta(p);
    }

    public void removePreguntaActual() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(idPreguntaModificada);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + idPreguntaModificada + " no encontrada");
        this.encuesta.removePregunta(p);
        this.idPreguntaModificada = -1;
    }

    // SELECCIÓN POR ID (guardamos sólo el id)
    public void seleccionarPreguntaPorId(int id) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(id);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + id + " no encontrada");
        this.idPreguntaModificada = id;
    }

    public void cancelarSeleccion() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        this.idPreguntaModificada = -1;
    }

    // Modificar el tipo de una pregunta por id
    public void modificarPreguntaTipoPorId(int id, TipoPregunta nuevoTipo) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(id);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + id + " no encontrada");
        p.setTipoPregunta(nuevoTipo);
    }

    private Pregunta getpreguntaseleccionada() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        if (this.idPreguntaModificada < 0) throw new NoHayPreguntaSeleccionada("No hay pregunta seleccionada");
        Pregunta p = findPreguntaById(this.idPreguntaModificada);
        if (p == null) {
            this.idPreguntaModificada = -1;
            throw new PreguntaNoEnEncuesta("La pregunta seleccionada ya no existe en la encuesta");
        }
        return p;
    }

    // Modificaciones sobre la pregunta seleccionada
    public void modificarPreguntaTextoSeleccionada(String nuevoTexto) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = getpreguntaseleccionada();
        p.setTexto(nuevoTexto);
    }

    public void modificarPreguntaTipoSeleccionada(TipoPregunta nuevoTipo) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = getpreguntaseleccionada();
        p.setTipoPregunta(nuevoTipo);
    }

    public void setDescripcionPreguntaSeleccionada(String descripcion) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = getpreguntaseleccionada();
        p.setDescripcion(descripcion);
    }

    // Limpiar la encuesta(no borra persistencia)
    public void deseleccionarEncuesta() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        this.encuesta = null;
    }

    public void borrarEncuestaActual() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        CtrlPersistencia.getInstance().borrarEncuesta(this.encuesta.getCreador(), this.encuesta.getTitulo());
        this.encuesta = null;
    }

    public void setImagenPreguntaSeleccionada(String imagen) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = getpreguntaseleccionada();
        p.setImagen(imagen);
    }

    public void setObligatorioPreguntaSeleccionada(boolean obligatorio) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = getpreguntaseleccionada();
        p.setObligatorio(obligatorio);
    }

    // Helper: buscar pregunta por id en la encuesta en memoria
    //deberia ser privada pero la hacemos public para testearla
    public Pregunta findPreguntaById(int id) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        for (Pregunta p : this.encuesta.getPreguntas()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

}
