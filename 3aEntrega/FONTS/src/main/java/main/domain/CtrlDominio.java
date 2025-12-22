package main.domain;

import main.domain.analisis.Analizador;
import main.domain.analisis.algoritmo.KMeans;
import main.domain.analisis.algoritmo.KMeansOptimizado;
import main.domain.analisis.algoritmo.KMedoids;
import main.domain.analisis.evaluador.CalinskiHarabasz;
import main.domain.analisis.evaluador.DaviesBouldinen;
import main.domain.analisis.evaluador.Silhouette;
import main.domain.analisis.inicializador.InicializadorKMeansPlusPlus;
import main.domain.analisis.inicializador.InicializadorKMedoidsGreedy;
import main.domain.analisis.inicializador.InicializadorRandom;
import main.domain.exceptions.*;
import main.domain.types.TDatos;
import main.domain.types.TDatosInteger;
import main.domain.types.TDatosOpciones;
import main.domain.types.TDatosString;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * Controlador del dominio que gestiona las operaciones relacionadas con encuestas, respuestas, perfiles y análisis.
 * Funciona como orquestador entre la capa de presentación y los controladores específicos de cada área.
 * @author Sergi Malaguilla Bombin
 */
public class CtrlDominio {
    CtrlEncuesta ce;
    CtrlRespuesta cr;
    CtrlPerfil cp;

    public CtrlDominio() {
        this.ce = new CtrlEncuesta();
        this.cr = new CtrlRespuesta();
        this.cp = new CtrlPerfil();
    }

/*
     -------------------------------------------------------------
    |                            ENCUESTA                         |
     -------------------------------------------------------------
*/
    /** Crea una nueva encuesta con el titulo dado y el email del perfil cargado en memoria (creador)
     * @param titulo Titulo de la encuesta a crear
     * @throws NoHayPerfilCargado Si no hay un perfil cargado en el sistema
     */
    public void crearEncuesta(String titulo) {
        if(cp.getPerfilCargado() == null) {
            throw new NoHayPerfilCargado("No hay perfil cargado para crear la encuesta");
        }
        ce.crearEncuesta(titulo, cp.getPerfilCargado().getEmail());
        cp.addEncuestaCreada(ce.getEncuesta());
    }

    /** Importa una encuesta existente dada su titulo y el email del creador
     * @param titulo Titulo de la encuesta a importar
     * @param emailCreador Email del creador de la encuesta a importar
     */
    public void importarEncuesta(String titulo, String emailCreador) {
        ce.cargarEncuesta(emailCreador, titulo);
    }

    /** Importa una encuesta existente dada la ruta del fichero
     * @param path Ruta del fichero de la encuesta a importar
     */
    public void importarEncuestaPorPath(String path) {ce.cargarEncuestaPorPath(path);}

    /** Guarda la encuesta actualmente en memoria
     */
    public void guardarEncuesta() {
        ce.guardarEncuesta();
    }

    /** Obtiene la encuesta actualmente en memoria
     * @return Encuesta actualmente en memoria
     */
    public Encuesta getEncuestaEnMemoria() {
        return ce.getEncuesta();
    }

    /** Obtiene el titulo de la encuesta actualmente en memoria
     * @return Titulo de la encuesta actualmente en memoria
     */
    public String getTituloEncuestaEnMemoria() {
        return ce.getEncuesta().getTitulo();
    }

    /** Obtiene el autor de la encuesta actualmente en memoria
     * @return Autor de la encuesta actualmente en memoria
     */
    public String getAutorEncuestaEnMemoria() {
        return ce.getEncuesta().getCreador();
    }

    /** Borra la encuesta actualmente en memoria tanto de la persistencia como de la memoria
     */
    public void borrarEncuestaActual() {
        cp.removeEncuestaCreada(ce.getEncuesta());
        ce.borrarEncuestaActual();
    }

    /** Obtiene una lista de pares (titulo, autor) de todas las encuestas existentes en el sistema
     * @return Lista de pares (titulo, autor) de todas las encuestas existentes en el sistema
     */
    public ArrayList<SimpleEntry<String, String>> getTituloAutorAllEncuestas() {
        return ce.getTituloAutorAllEncuestas();
    }

    /** Deselecciona la encuesta actualmente en memoria
     */
    public void deseleccionarEncuesta() {
        ce.deseleccionarEncuesta();
    }

    /** Agrega una nueva pregunta a la encuesta actualmente en memoria
     * @param id Identificador de la pregunta
     * @param texto Texto de la pregunta
     * @param obligatorio Si la pregunta es obligatoria o no
     * @param tipo Tipo de la pregunta
     */
    public void addPregunta(int id, String texto, boolean obligatorio, TipoPregunta tipo) {
        ce.addPregunta(id, texto, obligatorio, tipo);
    }

    /** Crea una nueva pregunta a partir de un hashmap con los datos de la pregunta
     * @param datosPregunta Hashmap con los datos de la pregunta
     */
    public void crearPregunta(HashMap<String, String> datosPregunta) {
        int id = Integer.parseInt(datosPregunta.get("ID"));
        String texto = datosPregunta.get("TEXTO");
        boolean obligatorio = Boolean.parseBoolean(datosPregunta.get("OBLIGATORIO"));
        TipoPregunta tipo = ce.getTipo(datosPregunta);
        String descripcion = datosPregunta.get("DESCRIPCION");
        ce.addPregunta(id, texto, obligatorio, tipo);
        ce.seleccionarPreguntaPorId(id);
        ce.setDescripcionPreguntaSeleccionada(descripcion);
    }

    /** Obtiene una pregunta de la encuesta actualmente en memoria dado su identificador
     * @param id Identificador de la pregunta a obtener
     * @return Pregunta con el identificador dado
     */
    public Pregunta getPreguntaById(int id) {
        return ce.getPreguntaById(id);
    }

    /** Elimina una pregunta de la encuesta actualmente en memoria dado su identificador
     * @param id Identificador de la pregunta a eliminar
     */
    public void removePreguntaById(int id) {
        ce.removePreguntaById(id);
    }

    /** Elimina la pregunta actualmente seleccionada en memoria
     */
    public void removePreguntaActual() {
        ce.removePreguntaActual();
    }

    /** Selecciona una pregunta de la encuesta actualmente en memoria dado su identificador
     * @param id Identificador de la pregunta a seleccionar
     */
    public void seleccionarPreguntaPorId(int id) {
        ce.seleccionarPreguntaPorId(id);
    }

    /** Cancela la seleccion de la pregunta actualmente en memoria
     */
    public void cancelarSeleccionPregunta() {
        ce.cancelarSeleccion();
    }

    /** Modifica el tipo de una pregunta dada su id
     * @param id Identificador de la pregunta a modificar
     * @param nuevoTipo Nuevo tipo que se asignará a la pregunta
     */
    public void modificarPreguntaTipoPorId(int id, TipoPregunta nuevoTipo) {
        ce.modificarPreguntaTipoPorId(id, nuevoTipo);
    }

    /** Modifica el texto de la pregunta actualmente seleccionada en memoria
     * @param nuevoTexto Nuevo texto de la pregunta
     */
    public void modificarPreguntaTextoSeleccionada(String nuevoTexto) {
        ce.modificarPreguntaTextoSeleccionada(nuevoTexto);
    }

    /** Modifica el tipo de la pregunta actualmente seleccionada en memoria
     * @param nuevoTipo Nuevo tipo de la pregunta
     */
    public void modificarPreguntaTipoSeleccionada(TipoPregunta nuevoTipo) {
        ce.modificarPreguntaTipoSeleccionada(nuevoTipo);
    }

    /** Establece la descripción de la pregunta actualmente seleccionada en memoria
     * @param descripcion Nueva descripción para la pregunta
     */
    public void setDescripcionPreguntaSeleccionada(String descripcion) {
        ce.setDescripcionPreguntaSeleccionada(descripcion);
    }

    /** Modifica si la pregunta actualmente seleccionada en memoria es obligatoria o no
     * @param obligatorio Si la pregunta es obligatoria o no
     */
    public void setObligatorioPreguntaSeleccionada(boolean obligatorio) {
        ce.setObligatorioPreguntaSeleccionada(obligatorio);
    }

    /** Obtiene las preguntas de la encuesta actualmente en memoria en formato de lista de hashmaps
     * @return Lista de hashmaps con las preguntas de la encuesta actualmente en memoria
     */
    public ArrayList<HashMap<String, String>> getPreguntasEncuestaCargada() {
        return ce.getPreguntasEncuestaEnMemoria();
    }


/*
     -------------------------------------------------------------
    |                            PERFIL                           |
     -------------------------------------------------------------
*/

    /** Crea un nuevo perfil con el email, nombre y contraseña dados
     * @param email Email del perfil a crear
     * @param nombre Nombre del perfil a crear
     * @param contrasena Contraseña del perfil a crear
     */
    public void crearPerfil(String email, String nombre, String contrasena) {
        cp.crearPerfil(email, nombre, contrasena);
    }

    /** Importa un perfil existente dado la ruta del fichero
     * @param path Ruta del fichero del perfil a importar
     */
    public void importarPerfilPorPath(String path) {cp.cargarPerfilPorPath(path);}

    /** Importa un perfil existente dado su email
     * @param email Email del perfil a importar
     */
    public void importarPerfil(String email) {
        cp.cargarPerfil(email);
    }

    /**
     * Obtiene el perfil cargado en memoria.
     * @return Perfil cargado en memoria.
     * */
    public Perfil getPerfilCargado() {
        return cp.getPerfilCargado();
    }

    /** Comprueba si existe un perfil con el email dado
     * @param email Email del perfil a comprobar
     * @return true si existe el perfil, false en caso contrario
     */
    public boolean existePerfil(String email) {
        return cp.existePerfil(email);
    }

    /**
     * Comprueba si la contraseña dada coincide con la del perfil cargado en memoria.
     * Si no coincide, deselecciona el perfil actual.
     * @param password Contraseña a comprobar.
     * @throws IncorrectPassword Si la contraseña no coincide.
     */
    public void comprobarPassword(String password){
        if (!password.equals(cp.getPerfilCargado().getContrasena())){
            cp.deseleccionarPerfil();
            throw new IncorrectPassword("el password es incorrecto");
        }
    }

    /** Deselecciona el perfil actualmente en memoria
     */
    public void deseleccionarPerfil() {
        cp.deseleccionarPerfil();
    }

    /**
     * Obtiene el email del perfil cargado en memoria.
     * @return Email del perfil cargado en memoria.
     * */
    public String getEmailPerfilCargado() {
        if (cp.getPerfilCargado() == null) throw new NoHayPerfilCargado("No hay perfil cargado");
        return cp.getPerfilCargado().getEmail();
    }

    /**
     * Obtiene el nombre del perfil cargado en memoria.
     * @return Nombre del perfil cargado en memoria.
     * */
    public String getNombrePerfilCargado() {
        if (cp.getPerfilCargado() == null) throw new NoHayPerfilCargado("No hay perfil cargado");
        return cp.getPerfilCargado().getNombre();
    }

    /**
     * Obtiene las respuestas hechas por el perfil cargado en memoria.
     * @return Conjunto de respuestas hechas por el perfil cargado en memoria.
     * */
    public HashSet<SimpleEntry<String, String>> getRespuestasPerfilCargado(){
        return cp.getRespuestasPerfilCargado();
    }

    /**
     * Borra el perfil cargado en memoria.
     * */
    public void borrarPerfilCargado () {
        cp.borrarPerfilCargado();
    }

/*
     -------------------------------------------------------------
    |                          RESPUESTA                          |
     -------------------------------------------------------------
*/

    /** Importa una respuesta existente para la encuesta actual dado el email del respondedor
     * @param emailRespondedor Email del perfil que respondió a la encuesta
     */
    public void importarRespuesta(String emailRespondedor ) {
        cr.cargarRespuesta(ce.getEncuesta().getCreador(), ce.getEncuesta().getTitulo(), emailRespondedor);
    }

    /** Importa todas las respuestas existentes de la encuesta actualmente en memoria
     */
    public void importarRespuestas(){
        cr.cargarRespuestas(ce.getEncuesta().getCreador(), ce.getEncuesta().getTitulo());
    }

    /** Importa una respuesta existente dada la ruta del fichero
     * @param path Ruta del fichero de la respuesta a importar
     */
    public void importarRespuestaPorPath(String path) {cr.cargarRespuestaPorPath(path);}

    /** Carga un conjunto de respuestas dadas sus emails
     * @param emails Lista de emails de los respondedores de las respuestas a cargar
     */
    public void cargarConjuntoRespuestas (ArrayList<String> emails) {
        cr.deseleccionarRespuestas();
        for (String email: emails) {
            cr.cargarRespuesta(ce.getEncuesta().getCreador(), ce.getEncuesta().getTitulo(), email);
        }
    }

    /** Obtiene los emails de las respuestas cargadas en memoria
     * @return Lista de emails de las respuestas cargadas en memoria
     */
    public ArrayList<String> getEmailsRespuestasEnMemoria() {
        return cr.getEmailRespuestasEnMemoria();
    }

    /** Deselecciona todas las respuestas cargadas en memoria
     */
    public void deseleccionarRespuestas() {
        cr.deseleccionarRespuestas();
    }

    /** Guarda la respuesta actualmente en memoria
     */
    public void guardarRespuesta() {
        cr.guardarRespuesta();
    }

    /** Obtiene las respuestas cargadas en memoria
     * @return Conjunto de respuestas cargadas en memoria
     */
    public HashSet<RespuestaEncuesta> getRespuestasCargadas() {
        return cr.getRespuestasCargadas();
    }

    /** Crea una nueva respuesta para la encuesta actualmente en memoria y el perfil cargado en memoria
     */
    public void responderEncuesta() {
        String emailRespondedor = cp.getPerfilCargado().getEmail();
        Encuesta encuesta = ce.getEncuesta();
        cr.crearNuevaRespuesta(encuesta, emailRespondedor);
        cp.addRespuestaHecha(cr.getRespuestaActual());
    }

    /** Obtiene los datos de la respuesta del perfil logueado para la encuesta cargada.
     * Transforma los tipos internos (TDatos) a una representación legible en String.
     * @return Lista de strings con los valores de cada respuesta ordenados por ID de pregunta.
     */
    public ArrayList<String> getRespuestaAEncuestaCargadaPerfilActual(){
        String emailPerfil = cp.getPerfilCargado().getEmail();
        String tituloEncuesta = ce.getEncuesta().getTitulo();
        String emailCreadorEncuesta = ce.getEncuesta().getCreador();
        cr.cargarRespuesta(emailCreadorEncuesta, tituloEncuesta, emailPerfil);
        RespuestaEncuesta respuesta = cr.getRespuestaActual();
        ArrayList<String> datosRespuesta = new ArrayList<>();

        TreeMap<Integer, Pregunta> preguntasOrdenadas = new TreeMap<>();
        for (Pregunta p: ce.getEncuesta().getPreguntas()) {
            preguntasOrdenadas.put(p.getId(), p);
        }

        for (int i = 1; i <= preguntasOrdenadas.size(); i++) {
            Pregunta p = preguntasOrdenadas.get(i);

            TDatos dato = respuesta.getDatosRespuesta().get(p.getId());

            if (dato != null) {
                if(dato instanceof TDatosOpciones){
                    String opcionesString = getOpcionesString((TDatosOpciones) dato);
                    datosRespuesta.add(opcionesString);
                }
                else if(dato instanceof TDatosInteger){
                    datosRespuesta.add(Integer.toString(((TDatosInteger) dato).getNum()));
                }
                else if(dato instanceof TDatosString) {
                    datosRespuesta.add(((TDatosString) dato).getTexto());
                }
                else {
                    datosRespuesta.add("Tipo de dato desconocido");
                }
            }
            else {
                datosRespuesta.add("No respondida");
            }
        }
        return datosRespuesta;
    }

    /** Convierte las opciones seleccionadas en un TDatosOpciones a un String separado por espacios
     * @param dato TDatosOpciones a convertir
     * @return String con las opciones seleccionadas separadas por espacios
     */
    private String getOpcionesString(TDatosOpciones dato) {
        ArrayList<Integer> opcionesSeleccionadas = dato.getIdOpciones();
        String opcionesString = "";
        for (int i = 0; i < opcionesSeleccionadas.size(); i++) {
            opcionesString += opcionesSeleccionadas.get(i).toString();
            if (i < opcionesSeleccionadas.size() - 1) {
                opcionesString += " ";
            }
        }
        return opcionesString;
    }

    /** Obtiene la respuesta actualmente en memoria
     * @return Respuesta actualmente en memoria
     */
    public RespuestaEncuesta getRespuestaActual() {
        return cr.getRespuestaActual();
    }

    /** Agrega un dato de respuesta a la respuesta actualmente en memoria
     * @param idPregunta Identificador de la pregunta a la que se le agrega el dato de respuesta
     * @param datos Dato de respuesta a agregar
     * @throws Exception Si el dato de respuesta no es valido para la pregunta
     */
    public void addDatoRespuesta(int idPregunta, TDatos datos) throws Exception {
        ce.getPreguntaById(idPregunta).validar(datos);
        cr.addDatoRespuesta(idPregunta, datos);
    }

    /** Borra la respuesta actualmente en memoria tanto de la persistencia como de la memoria
     */
    public void borrarRespuestaActual() {
        if(cr.getRespuestaActual() != null)
            cp.removeRespuestaHecha(cr.getRespuestaActual());
        cr.borrarRespuestaActual();
    }

    /** Agrega una respuesta de tipo opciones a la respuesta actualmente en memoria
     * @param id Identificador de la pregunta a la que se le agrega el dato de respuesta
     * @param opciones Opciones seleccionadas en la respuesta
     * @throws Exception Si el dato de respuesta no es valido para la pregunta
     */
    public void responderPreguntaOpciones(int id, ArrayList<Integer> opciones) throws Exception {
        TDatos datos = new TDatosOpciones(opciones);
        addDatoRespuesta(id, datos);
    }

    /** Agrega una respuesta de tipo numerica a la respuesta actualmente en memoria
     * @param id Identificador de la pregunta a la que se le agrega el dato de respuesta
     * @param value Valor numerico de la respuesta
     * @throws Exception Si el dato de respuesta no es valido para la pregunta
     */
    public void responderPreguntaNumerica(int id, int value) throws Exception {
        TDatos datos = new TDatosInteger(value);
        addDatoRespuesta(id, datos);
    }

    /** Agrega una respuesta de tipo formato libre a la respuesta actualmente en memoria
     * @param id Identificador de la pregunta a la que se le agrega el dato de respuesta
     * @param text Texto de la respuesta
     * @throws Exception Si el dato de respuesta no es valido para la pregunta
     */
    public void responderPreguntaFormatoLibre(int id, String text) throws Exception {
        TDatos datos = new TDatosString(text);
        addDatoRespuesta(id, datos);
    }

/*
     -------------------------------------------------------------
    |                          ANALISIS                           |
     -------------------------------------------------------------
 */

    /**
     * Obtiene el nombre del algoritmo de analisis actualmente en uso.
     * @return Nombre del algoritmo de analisis actualmente en uso.
     * */
    public String getAlgoritmo() {
        return Analizador.getInstance().getAlgoritmo().getClass().getSimpleName();
    }

    /**
     * Obtiene el nombre del inicializador actualmente en uso.
     * @return Nombre del inicializador actualmente en uso.
     * */
    public String getInicializador() {
        return Analizador.getInstance().getInicializador().getClass().getSimpleName();
    }

    /**
     * Obtiene el nombre del evaluador de calidad actualmente en uso.
     * @return Nombre del evaluador de calidad actualmente en uso.
     * */
    public String getEvaluadorCalidad() {
        return Analizador.getInstance().getEvaluador().getClass().getSimpleName();
    }

    /**
     * Establece el valor de K para el análisis de clustering.
     * @param k Número de clusters (debe ser mayor que 0).
     * @throws IllegalArgumentException Si k es menor o igual a 0.
     */
    public void setK(int k) {
        if (k <= 0) throw new IllegalArgumentException("K debe ser mayor que 0");
        Analizador.getInstance().setK(k);
    }

    /**
     * Calcula el valor de K utilizando el metodo del codo.
     * @return Valor de K calculado.
     * @throws NoHayRespuestasSeleccionadas Si no hay respuestas cargadas para calcular K.
     * */
    public int calcularK() {
        ArrayList<RespuestaEncuesta> respuestas = new ArrayList<>(cr.getRespuestasCargadas());

        if (respuestas.isEmpty()) {
            throw new NoHayRespuestasSeleccionadas("No hay respuestas cargadas para calcular K");
        }

        Analizador.getInstance().calcularK(respuestas);

        return Analizador.getInstance().getK();
    }

    /**
     * Obtiene el valor de K actualmente establecido.
     * @return Valor de K actualmente establecido.
     * */
    public int getK() {
        return Analizador.getInstance().getK();
    }

    /**
     * Analiza las respuestas cargadas en memoria.
     * @throws EncuestaDiferente Si alguna de las respuestas no pertenece a la encuesta actualmente en memoria.
     * @throws NoHayRespuestasSeleccionadas Si no hay respuestas cargadas para analizar.
     * */
    public void analizarEncuesta() throws EncuestaDiferente, NoHayRespuestasSeleccionadas {
        HashSet<RespuestaEncuesta> respuestas = cr.getRespuestasCargadas();
        if(respuestas.isEmpty()) {
            throw new NoHayRespuestasSeleccionadas("No hay respuestas para analizar");
        }
        for(RespuestaEncuesta r : respuestas) {
            if (!ce.getEncuesta().getTitulo().equals(r.getEncuesta()) ) {
                throw new EncuestaDiferente("No se puede analizar la encuesta porque una o más respuestas pertenecen a otra encuesta");
            }
        }
        Analizador.getInstance().analizarRespuestas(new ArrayList<>(respuestas));
    }

    /**
     * Obtiene las distancias dentro de los clusters generados tras el analisis.
     * @return Distancias dentro de los clusters generados tras el analisis.
     * */
    public ArrayList<ArrayList<ArrayList<Float>>> getDistanciasClusters() {
        return Analizador.getInstance().getDistanciasClusters();
    }

    /**
     * Obtiene las distancias entre respuestas.
     * @return Distancias entre respuestas.
     * */
    public ArrayList<ArrayList<Float>> getDistancias() {
        return Analizador.getInstance().getDistancias();
    }

    /**
     * Obtiene los autores de las respuestas en cada cluster tras el analisis.
     * @return Autores de las respuestas en cada cluster tras el analisis.
     * */
    public ArrayList<ArrayList<String>> getAutoresClusters() {
        return Analizador.getInstance().getAutoresClusters();
    }

    /**
     * Analiza las respuestas cargadas en memoria.
     * */
    public void analizar() {
        Analizador.getInstance().analizarRespuestas(new ArrayList<>(cr.getRespuestasCargadas()));
    }

    /**
     * Obtiene la evaluacion de calidad del clustering realizado.
     * @return Evaluacion de calidad del clustering realizado.
     * */
    public float getEvaluacionCalidad() {
        return Analizador.getInstance().evaluarCalidadClustering();
    }

    private boolean esAlgorimoValido(String nombreAlgoritmo) {
        return nombreAlgoritmo.equals("KMeans") || nombreAlgoritmo.equals("KMedoids" ) || nombreAlgoritmo.equals("KMeansOptimizado");
    }

    private boolean esInicializadorValido(String nombreInicializador) {
        return nombreInicializador.equals("KMeans++") || nombreInicializador.equals("Aleatorio") || nombreInicializador.equals("Greedy");
    }

    /**
     * Establece el algoritmo de análisis y el inicializador de centroides a utilizar.
     * @param nombreAlgoritmo Nombre del algoritmo (KMeans, KMedoids, KMeansOptimizado).
     * @param nombreInicializador Nombre del algoritmo de inicialización (KMeans++, Aleatorio, Greedy).
     * @throws AlgoritmoNoReconocido Si el nombre del algoritmo no es válido.
     * @throws InicializadorNoReconocido Si el nombre del inicializador no es válido.
     * @throws InicializadorYAlgoritmoIncompatibles Si la combinación elegida no está soportada.
     */
    public void elegirAlgoritmoAnalisis(String nombreAlgoritmo, String nombreInicializador) {
        if(!esAlgorimoValido(nombreAlgoritmo)){throw new AlgoritmoNoReconocido("Algoritmo no reconocido");}
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
        else if(nombreAlgoritmo.equals("KMeansOptimizado") && nombreInicializador.equals("Aleatorio")) {
            Analizador.getInstance().cambiarAlgoritmo(new KMeansOptimizado(), new InicializadorRandom());
        }
        else if(nombreAlgoritmo.equals("KMeansOptimizado") && nombreInicializador.equals("KMeans++")) {
            Analizador.getInstance().cambiarAlgoritmo(new KMeansOptimizado(), new InicializadorKMeansPlusPlus());
        }
        else throw new InicializadorYAlgoritmoIncompatibles("El inicializador" + nombreInicializador + " no es compatible con el algoritmo" + nombreAlgoritmo);
    }

    /**
     * Establece el evaluador de calidad a utilizar.
     * @param nombreEvaluador Nombre del evaluador de calidad a utilizar.
     * @throws EvaluadorNoReconocido Si el nombre del evaluador no es reconocido.
     * */
    public void elegirEvaluadorCalidad(String nombreEvaluador) {
        switch (nombreEvaluador) {
            case "Silhouette" -> Analizador.getInstance().cambiarEvaluador(new Silhouette());
            case "CalinskiHarabasz" -> Analizador.getInstance().cambiarEvaluador(new CalinskiHarabasz());
            case "DaviesBouldinen" -> Analizador.getInstance().cambiarEvaluador(new DaviesBouldinen());
            default -> throw new EvaluadorNoReconocido("Evaluador no reconocido");
        }
    }
}
