package main.presentation;

import main.domain.CtrlDominio;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Controlador de la capa de presentación. Es el encargado de crear toda la aplicación, conteniendo
 * el {@link CtrlDominio} y la {@link VistaPrincipal}.
 * Implementa las operaciones de {@link PresenterEncuesta}, {@link PresenterRespuesta}, {@link PresenterAnalisis}
 * y {@link PresenterPerfil}, siendo el único de la capa de presentación en comunicarse con la capa de dominio.
 *
 * @author Yimin Jin
 */
public class CtrlPresentacion implements PresenterEncuesta, PresenterRespuesta, PresenterPerfil, PresenterAnalisis {
    private CtrlDominio ctrlDominio;
    private VistaPrincipal vistaPrincipal;

    /**
     * Constructor de la clase.
     */
    public CtrlPresentacion() {
        ctrlDominio = new CtrlDominio();
        vistaPrincipal = new VistaPrincipal(this, this, this, this);
    }

    /**
     * Inicializa la visualización de la aplicación.
     */
    public void inicializarPresentacion() {
        vistaPrincipal.hacerVisible();
    }

    // Mostrar paneles

    @Override
    public void mostrarMenuPrincipal() {
        vistaPrincipal.mostrarMenuPrincipal();
    }

    // Llamadas a dominio

    // Encuestas
    @Override
    public String getTituloEncuestaCargada() {
        return ctrlDominio.getTituloEncuestaEnMemoria();
    }

    @Override
    public String getAutorEncuestaCargada() {
        return ctrlDominio.getAutorEncuestaEnMemoria();
    }

    @Override
    public void guardarEncuesta() {
        ctrlDominio.guardarEncuesta();
    }

    @Override
    public void deseleccionarEncuestaActual() {
        ctrlDominio.deseleccionarEncuesta();
    }

    // Primer atributo titulo, segundo autor
    @Override
    public ArrayList<SimpleEntry<String, String>> getTituloAutorAllEncuestas() {
        return ctrlDominio.getTituloAutorAllEncuestas();
    }

    @Override
    public void crearEncuesta(String titulo) {
        ctrlDominio.crearEncuesta(titulo);
    }

    @Override
    public void crearPregunta(HashMap<String, String> datosPregunta) {
        ctrlDominio.crearPregunta(datosPregunta);
    }

    @Override
    public void importarEncuesta(String titulo, String autor) {
        ctrlDominio.importarEncuesta(titulo, autor);
    }

    @Override
    public ArrayList<HashMap<String, String>> getPreguntasEncuestaCargada() {
        return ctrlDominio.getPreguntasEncuestaCargada();
    }

    @Override
    public void borrarEncuestaActual() {
        ctrlDominio.borrarEncuestaActual();
    }

    @Override
    public void borrarPreguntaPorId(int id) {
        ctrlDominio.removePreguntaById(id);
    }

    @Override
    public void importarEncuestaPorPath(String path) {
        ctrlDominio.importarEncuestaPorPath(path);
    }

    // Perfil
    @Override
    public void cargarPerfil(String email, String password) {
        ctrlDominio.importarPerfil(email);
        ctrlDominio.comprobarPassword(password);
    }

    @Override
    public void crearPerfil(String email, String nombre, String contrasena) {
        ctrlDominio.crearPerfil(email, nombre, contrasena);
    }

    @Override
    public String getNombrePerfilCargado() {
        return ctrlDominio.getNombrePerfilCargado();
    }

    @Override
    public String getEmailPerfilCargado() {
        return ctrlDominio.getEmailPerfilCargado();
    }

    @Override
    public void deseleccionarPerfil () {
        ctrlDominio.deseleccionarPerfil();
    }

    @Override
    public void borrarPerfilCargado () {
        ctrlDominio.borrarPerfilCargado();
        vistaPrincipal.setPanelPerfil();
    }

    // Solo guarda el fichero en la persistencia, el usuario ha de iniciar sesion manualmente despues
    @Override
    public void importarPerfilPorPath(String path) {
        ctrlDominio.importarPerfilPorPath(path);
    }

    // Respuestas

    @Override
    public HashSet<SimpleEntry<String, String>> getRespuestasPerfilCargado() {
        return ctrlDominio.getRespuestasPerfilCargado();
    }

    @Override
    public void modificarRespuesta(String tituloEncuesta, String emailCreadorEncuesta) {
        ctrlDominio.importarEncuesta(tituloEncuesta, emailCreadorEncuesta);
        ctrlDominio.importarRespuesta(getEmailPerfilCargado());
    }

    @Override
    public ArrayList<String> getRespuestasEncuestaCargada() {
        return ctrlDominio.getEmailsRespuestasEnMemoria();
    }

    @Override
    public ArrayList<String> getRespuestaAEncuestaCargadaPerfilActual() {
        return ctrlDominio.getRespuestaAEncuestaCargadaPerfilActual();
    }

    @Override
    public void importarRespuestasEnMemoria() {
        ctrlDominio.importarRespuestas();
    }

    @Override
    public void responderPreguntaOpciones(int id, ArrayList<Integer> opciones) throws Exception {
        ctrlDominio.responderPreguntaOpciones(id, opciones);
    }

    @Override
    public void responderPreguntaNumerica(int id, int value) throws Exception {
        ctrlDominio.responderPreguntaNumerica(id, value);
    }

    @Override
    public void responderPreguntaFormatoLibre(int id, String text) throws Exception {
        ctrlDominio.responderPreguntaFormatoLibre(id, text);
    }

    @Override
    public void responderEncuesta() {
        ctrlDominio.responderEncuesta();
    }

    @Override
    public void borrarRespuestaActual() {
        ctrlDominio.borrarRespuestaActual();
    }

    @Override
    public void guardarRespuesta() {
        ctrlDominio.guardarRespuesta();
    }

    @Override
    public void importarRespuestaPorPath(String path) {
        ctrlDominio.importarRespuestaPorPath(path);
    }

    @Override
    public void cargarConjuntoRespuestas(ArrayList<String> emails) {
        ctrlDominio.cargarConjuntoRespuestas(emails);
    }

    // Analisis

    @Override
    public void setAlgoritmoInicializador(String algoritmo, String inicializador) {
        ctrlDominio.elegirAlgoritmoAnalisis(algoritmo, inicializador);
    }

    @Override
    public void setEvaluador(String evaluador) {
        ctrlDominio.elegirEvaluadorCalidad(evaluador);
    }

    @Override
    public void setK(int k) {
        ctrlDominio.setK(k);
    }

    @Override
    public int calcularK() {
        return ctrlDominio.calcularK();
    }

    @Override
    public void analizar() {
        ctrlDominio.analizar();
    }

    @Override
    public ArrayList<ArrayList<ArrayList<Float>>> getDistanciasClusters() {
        return ctrlDominio.getDistanciasClusters();
    }

    @Override
    public ArrayList<ArrayList<String>> getAutoresClusters() {
        return ctrlDominio.getAutoresClusters();
    }

    @Override
    public ArrayList<ArrayList<Float>> getDistancias() { return ctrlDominio.getDistancias(); }

    @Override
    public float getEval() { return ctrlDominio.getEvaluacionCalidad(); }
}
