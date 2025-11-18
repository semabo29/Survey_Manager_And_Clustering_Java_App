package main.domain;
import main.domain.analisis.algoritmo.KMeans;
import main.domain.analisis.algoritmo.KMedoids;
import main.domain.analisis.evaluador.CalinskiHarabasz;
import main.domain.analisis.evaluador.DaviesBouldinen;
import main.domain.analisis.evaluador.Silhouette;
import main.domain.analisis.inicializador.InicializadorKMeansPlusPlus;
import main.domain.analisis.inicializador.InicializadorKMedoidsGreedy;
import main.domain.analisis.inicializador.InicializadorRandom;
import main.domain.exceptions.*;
import main.domain.analisis.Analizador;
import main.domain.types.TDatos;

import java.util.ArrayList;
import java.util.HashSet;

public class CtrlDominio {
    CtrlEncuesta ce;
    CtrlRespuesta cr;
    CtrlPerfil cp;

    public CtrlDominio() {
        this.ce = new CtrlEncuesta();
        this.cr = new CtrlRespuesta();
        this.cp = new CtrlPerfil();
    }

    /**
     *
     *  casos de uso:
     *  crearEncuesta -
     *  ModificarEncuesta
     *    set titulo -
     *    add pregunta -
     *    remove pregunta -
     *    modificar pregunta -
     *      set enunciado -
     *      set tipo -
     *      set opciones -
     *  guardarEncuesta -
     *  borrarEncuesta
     *  cargarEncuesta -
     *  responderEncuesta
     *  crearPerfil -
     *  cargarPerfil -
     *  guardarRespuesta
     *  cargarRespuesta
     *  modificarRespuesta
     *  borrarRespuesta
     *  consultarRespuesta
     *  Elegir algoritmo+inicializador de analisis & Elegir evaluador de calidad -
     *  analizarEncuesta -
     *
     */
//ENCUESTA
    //crear encuesta con el perfil cargado
    public void crearEncuesta(String titulo) {
        if(cp.getPerfilCargado() == null) {
            throw new NoHayPerfilCargado("No hay perfil cargado para crear la encuesta");
        }
        ce.crearEncuesta(titulo, cp.getPerfilCargado().getEmail());
        cp.addEncuestaCreada(ce.getEncuesta());
    }

    //cargar encuesta
    public void importarEncuesta(String titulo, String emailCreador) {
        ce.cargarEncuesta(emailCreador, titulo);
    }

    //guardar encuesta
    public void guardarEncuesta() {
        ce.guardarEncuesta();
    }

    //get encuesta actual (necesario?)
    public Encuesta getEncuestaEnMemoria() {
        return ce.getEncuesta();
    }

    public void borrarEncuestaActual() {
        cp.removeEncuestaCreada(ce.getEncuesta());
        ce.borrarEncuestaActual();
    }

    //deseleccionar encuesta
    public void deseleccionarEncuesta() {
        ce.deseleccionarEncuesta();
    }

    /* eliminado por problemas de integridad
    // Cambiar título de la encuesta actual
    public void setTituloEncuesta(String nuevoTitulo) {
        ce.setTituloEncuesta(nuevoTitulo);
    }
    */

    //MODIFICAR PREGUNTA
    public void addPregunta(int id, String texto, boolean obligatorio, TipoPregunta tipo) {
        ce.addPregunta(id, texto, obligatorio, tipo);
    }

    public Pregunta getPreguntaById(int id) {
        return ce.getPreguntaById(id);
    }

    public void removePreguntaById(int id) {
        ce.removePreguntaById(id);
    }

    public void removePreguntaActual() {
        ce.removePreguntaActual();
    }

    // Selección por id
    public void seleccionarPreguntaPorId(int id) {
        ce.seleccionarPreguntaPorId(id);
    }

    public void cancelarSeleccionPregunta() {
        ce.cancelarSeleccion();
    }

    public void modificarPreguntaTipoPorId(int id, TipoPregunta nuevoTipo) {
        ce.modificarPreguntaTipoPorId(id, nuevoTipo);
    }

    // Modificaciones sobre la pregunta seleccionada (delegadas)
    public void modificarPreguntaTextoSeleccionada(String nuevoTexto) {
        ce.modificarPreguntaTextoSeleccionada(nuevoTexto);
    }

    public void modificarPreguntaTipoSeleccionada(TipoPregunta nuevoTipo) {
        ce.modificarPreguntaTipoSeleccionada(nuevoTipo);
    }

    public void setDescripcionPreguntaSeleccionada(String descripcion) {
        ce.setDescripcionPreguntaSeleccionada(descripcion);
    }

    public void setImagenPreguntaSeleccionada(String imagen) {
        ce.setImagenPreguntaSeleccionada(imagen);
    }

    public void setObligatorioPreguntaSeleccionada(boolean obligatorio) {
        ce.setObligatorioPreguntaSeleccionada(obligatorio);
    }


//PERFIL
    //crear perfil
    public void crearPerfil(String email, String nombre, String contrasena) {
        cp.crearPerfil(email, nombre, contrasena);
    }

    //cargar perfil
    public void importarPerfil(String email) {
        cp.cargarPerfil(email);
    }

    public Perfil getPerfilCargado() {
        return cp.getPerfilCargado();
    }

    public boolean existePerfil(String email) {
        return cp.existePerfil(email);
    }

//RESPUESTA
    //cargar respuesta
    public void importarRespuesta(String emailRespondedor ) {
        cr.cargarRespuesta(ce.getEncuesta().getCreador(), ce.getEncuesta().getTitulo(), emailRespondedor);
    }

    public void importarResuestas(){
        cr.cargarRespuestas(ce.getEncuesta().getCreador(), ce.getEncuesta().getTitulo());
    }

    //guardar respuesta
    public void guardarRespuesta() {
        cr.guardarRespuesta(); //controlador de respuesta se encarga de pasarle la respuesta actual
    }

    public HashSet<RespuestaEncuesta> getRespuestasCargadas() {
        return cr.getRespuestasCargadas();
    }

    //Responder encuesta
    public void responderEncuesta() {
        String emailRespondedor = cp.getPerfilCargado().getEmail();
        Encuesta encuesta = ce.getEncuesta();
        cr.crearNuevaRespuesta(encuesta, emailRespondedor);
        cp.addRespuestaHecha(cr.getRespuestaActual());
    }

    public RespuestaEncuesta getRespuestaActual() {
        return cr.getRespuestaActual();
    }

    public void addDatoRespuesta(int idPregunta, TDatos datos) throws Exception {
        ce.getPreguntaById(idPregunta).validar(datos);
        cr.addDatoRespuesta(idPregunta, datos);
    }

    public void borrarRespuestaActual() {
        if(cr.getRespuestaActual() != null)
            cp.removeRespuestaHecha(cr.getRespuestaActual());
        cr.borrarRespuestaActual();
    }
    //ANALISIS

    public void setK(int k) {
        if (k <= 0) throw new IllegalArgumentException("K debe ser mayor que 0");
        Analizador.getInstance().setK(k);
    }

    public int calcularK() {
        ArrayList<RespuestaEncuesta> respuestas = new ArrayList<>(cr.getRespuestasCargadas());

        if (respuestas.isEmpty()) {
            throw new NoHayRespuestasSeleccionadas("No hay respuestas cargadas para calcular K");
        }

        Analizador.getInstance().calcularK(respuestas);

        return Analizador.getInstance().getK();
    }

    public int getK() {
        return Analizador.getInstance().getK();
    }

    public ArrayList<ArrayList<RespuestaEncuesta>> analizarEncuesta() throws EncuestaDiferente, NoHayRespuestasSeleccionadas {
        HashSet<RespuestaEncuesta> respuestas = cr.getRespuestasCargadas();
        if(respuestas.isEmpty()) {
            throw new NoHayRespuestasSeleccionadas("No hay respuestas para analizar");
        }
        for(RespuestaEncuesta r : respuestas) {
            if (!ce.getEncuesta().getTitulo().equals(r.getEncuesta()) ) {
                throw new EncuestaDiferente("No se puede analizar la encuesta porque una o más respuestas pertenecen a otra encuesta");
            }
        }
        //devuelve los clusters generados
        return Analizador.getInstance().analizarRespuestas(new ArrayList<>(respuestas));
    }

    public float getEvaluacionCalidad(ArrayList<ArrayList<RespuestaEncuesta>> clusters) {
        return Analizador.getInstance().evaluarCalidadClustering(clusters);
    }

    private boolean esAlgorimoValido(String nombreAlgoritmo) {
        return nombreAlgoritmo.equals("KMeans") || nombreAlgoritmo.equals("KMedoids");
    }

    private boolean esInicializadorValido(String nombreInicializador) {
        return nombreInicializador.equals("KMeans++") || nombreInicializador.equals("Aleatorio") || nombreInicializador.equals("Greedy");
    }
    public void elegirAlgoritmoAnalisis(String nombreAlgoritmo, String nombreInicializador) {
        if(!esAlgorimoValido(nombreAlgoritmo)){throw new AlgoritmoNoReonocido("Algoritmo no reconocido");}
        if(!esInicializadorValido(nombreInicializador)){throw new InicializadorNoReconocido("Inicializador no reconocido");}
        if(nombreAlgoritmo.equals("KMeans") && nombreInicializador.equals("KMeans++")) {
            Analizador.getInstance().cambiarAlgoritmo(new KMeans(), new InicializadorKMeansPlusPlus());
        }
        else if(nombreAlgoritmo.equals("KMeans") && nombreInicializador.equals("Aleatorio")) {
            Analizador.getInstance().cambiarAlgoritmo(new KMeans(), new InicializadorRandom());
        }
        else if(nombreAlgoritmo.equals("KMedoids") && nombreInicializador.equals("Greedy")) {
            Analizador.getInstance().cambiarAlgoritmo(new KMedoids(), new InicializadorKMedoidsGreedy());
        }
        else throw new InicializadorYAlgoritmoIncompatibles("El inicializador" + nombreInicializador + "no es compatible con el algoritmo" + nombreAlgoritmo);
    }

    public void elegirEvaluadorCalidad(String nombreEvaluador) {
        switch (nombreEvaluador) {
            case "Silhouette" -> Analizador.getInstance().cambiarEvaluador(new Silhouette());
            case "CalinskiHarabasz" -> Analizador.getInstance().cambiarEvaluador(new CalinskiHarabasz());
            case "DaviesBouldinen" -> Analizador.getInstance().cambiarEvaluador(new DaviesBouldinen());
            default -> throw new EvaluadorNoReconocido("Evaluador no reconocido");
        }

    }

    public String getEvaluadorCalidad() {
        return Analizador.getInstance().getEvaluadorActual().getClass().getSimpleName();
    }
}
