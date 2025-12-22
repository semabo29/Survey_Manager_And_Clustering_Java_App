package main.domain;

import main.domain.exceptions.NoHayEncuestaSeleccionada;
import main.domain.exceptions.NoHayPreguntaSeleccionada;
import main.domain.exceptions.PreguntaNoEnEncuesta;
import main.persistence.CtrlPersistencia;
import main.persistence.exceptions.FalloPersistencia;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Controlador de dominio encargado de la gestión de encuestas.
 * Permite crear, cargar, modificar y guardar encuestas en el sistema.
 * Utiliza {@link CtrlPersistencia} para las operaciones de almacenamiento.
 * Cada {@link Encuesta} contiene {@link Pregunta} que pueden ser añadidas, modificadas o eliminadas.
 * @author Sergi Malaguilla Bombin
 */
public class CtrlEncuesta {
    private Encuesta encuesta;
    private int idPreguntaModificada = -1;

    public CtrlEncuesta() {
        this.encuesta = null;
    }

    /**
     * Crea una nueva encuesta y la guarda en el sistema de persistencia.
     * @param titulo El título de la encuesta.
     * @param emailCreador El correo electrónico del usuario que crea la encuesta.
     */
    public void crearEncuesta(String titulo, String emailCreador) {
        this.encuesta = new Encuesta(titulo, emailCreador);
        guardarEncuesta();
    }

    /**
     * Guarda el estado actual de la encuesta cargada en memoria en la persistencia.
     * @throws NoHayEncuestaSeleccionada Si no hay ninguna encuesta cargada en el controlador.
     * @throws FalloPersistencia Si ocurre un error de E/S durante la serialización.
     */
    public void guardarEncuesta() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        try (ByteArrayOutputStream datos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(datos)){
            oos.writeObject(this.encuesta);
            oos.flush();
            CtrlPersistencia.getInstance().guardarEncuesta(this.encuesta.getCreador(), this.encuesta.getTitulo(), datos);

        }
        catch (IOException e) {
            throw new FalloPersistencia("Error al guardar la encuesta: " + e.getMessage());
        }
    }

    /**
     * Carga una encuesta existente desde la persistencia a la memoria del controlador.
     * @param email El email del autor de la encuesta.
     * @param titulo El título de la encuesta a cargar.
     * @throws NoHayEncuestaSeleccionada Si la encuesta no existe o no se puede leer.
     * @throws FalloPersistencia Si ocurre un error al deserializar el objeto.
     */
    public void cargarEncuesta(String email, String titulo) {
        ByteArrayInputStream datos = CtrlPersistencia.getInstance().cargarEncuesta(email, titulo);
        if (datos == null)
            throw new NoHayEncuestaSeleccionada("La encuesta no existe o no pudo ser leída");
        try (ObjectInputStream ois = new ObjectInputStream(datos)) {
            this.encuesta = (Encuesta) ois.readObject();
            this.idPreguntaModificada = -1;
        }
        catch (IOException | ClassNotFoundException e) {
            throw new FalloPersistencia("Error al deserializar encuesta: " + e.getMessage());
        }
    }

    /**
     * Importa una encuesta desde un archivo externo especificado por una ruta.
     * Tras cargarla, la guarda automáticamente en el sistema de persistencia local.
     * @param Spath Ruta absoluta o relativa del archivo serializado.
     * @throws NoHayEncuestaSeleccionada Si el archivo no existe o es ilegible.
     * @throws FalloPersistencia Si ocurre un error durante la carga o el guardado inicial.
     */
    public void cargarEncuestaPorPath(String Spath) {
        Path path = Paths.get(Spath);
        ByteArrayInputStream datos = CtrlPersistencia.getInstance().leerClaseSerializada(path);
        if (datos == null)
            throw new NoHayEncuestaSeleccionada("La encuesta no existe o no pudo ser leída");
        try (ObjectInputStream ois = new ObjectInputStream(datos)) {
            this.encuesta = (Encuesta) ois.readObject();
            guardarEncuesta();
            this.idPreguntaModificada = -1;
        }
        catch (IOException | ClassNotFoundException e) {
            throw new FalloPersistencia("Error al deserializar encuesta: " + e.getMessage());
        }
    }

    /**
     * Obtiene el objeto Encuesta actualmente cargado en memoria.
     * @return El objeto Encuesta o null si no hay ninguno.
     */
    public Encuesta getEncuesta() {
        return this.encuesta;
    }

    /**
     * Obtiene el identificador de la pregunta que está siendo modificada actualmente.
     * @return El ID de la pregunta seleccionada, o -1 si no hay selección.
     */
    public int getIdPreguntaModificada() {return this.idPreguntaModificada;}

    /**
     * Añade una nueva pregunta a la encuesta actual o reemplaza una existente con el mismo ID.
     * @param id Identificador único de la pregunta.
     * @param texto El enunciado de la pregunta.
     * @param obligatorio Indica si el encuestado debe responderla obligatoriamente.
     * @param tipo El tipo de pregunta (FormatoLibre, Numerica, o ConOpciones).
     * @throws NoHayEncuestaSeleccionada Si no hay una encuesta cargada.
     */
    public void addPregunta(int id, String texto, boolean obligatorio, TipoPregunta tipo) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = new Pregunta(this.encuesta, id, texto, obligatorio, tipo);
        this.encuesta.addPregunta(p);
    }

    /**
     * Busca y devuelve una pregunta de la encuesta actual por su ID.
     * @param id El identificador de la pregunta.
     * @return El objeto Pregunta correspondiente.
     * @throws NoHayEncuestaSeleccionada Si no hay una encuesta cargada.
     * @throws PreguntaNoEnEncuesta Si el ID no corresponde a ninguna pregunta de la encuesta.
     */
    public Pregunta getPreguntaById(int id) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(id);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + id + " no encontrada");
        return p;
    }

    /**
     * Elimina una pregunta de la encuesta actual utilizando su ID.
     * @param id El identificador de la pregunta a borrar.
     * @throws NoHayEncuestaSeleccionada Si no hay una encuesta cargada.
     * @throws PreguntaNoEnEncuesta Si la pregunta no existe en la encuesta.
     */
    public void removePreguntaById(int id) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(id);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + id + " no encontrada");
        this.encuesta.removePregunta(p);
    }

    /**
     * Elimina la pregunta que se encuentra seleccionada actualmente en el controlador.
     * Tras la eliminación, se limpia la selección (idPreguntaModificada = -1).
     * @throws NoHayEncuestaSeleccionada Si no hay una encuesta cargada.
     * @throws PreguntaNoEnEncuesta Si no hay una pregunta seleccionada o esta ya no existe.
     */
    public void removePreguntaActual() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(idPreguntaModificada);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + idPreguntaModificada + " no encontrada");
        this.encuesta.removePregunta(p);
        this.idPreguntaModificada = -1;
    }

    /**
     * Selecciona una pregunta de la encuesta para realizar ediciones posteriores sobre ella.
     * @param id El identificador de la pregunta a seleccionar.
     * @throws NoHayEncuestaSeleccionada Si no hay una encuesta cargada.
     * @throws PreguntaNoEnEncuesta Si el ID no se encuentra en la encuesta.
     */
    public void seleccionarPreguntaPorId(int id) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(id);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + id + " no encontrada");
        this.idPreguntaModificada = id;
    }

    /**
     * Desmarca cualquier pregunta que estuviera seleccionada para modificación.
     * @throws NoHayEncuestaSeleccionada Si no hay una encuesta cargada.
     */
    public void cancelarSeleccion() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        this.idPreguntaModificada = -1;
    }

    /**
     * Cambia el tipo de una pregunta específica identificada por su ID.
     * @param id El identificador de la pregunta.
     * @param nuevoTipo El nuevo objeto de tipo TipoPregunta.
     * @throws NoHayEncuestaSeleccionada Si no hay una encuesta cargada.
     * @throws PreguntaNoEnEncuesta Si la pregunta no existe.
     */
    public void modificarPreguntaTipoPorId(int id, TipoPregunta nuevoTipo) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = findPreguntaById(id);
        if (p == null) throw new PreguntaNoEnEncuesta("Pregunta con id " + id + " no encontrada");
        p.setTipoPregunta(nuevoTipo);
    }

    /**
     * Modifica el texto (enunciado) de la pregunta seleccionada actualmente.
     * @param nuevoTexto El nuevo texto de la pregunta.
     * @throws NoHayEncuestaSeleccionada Si no hay encuesta cargada.
     * @throws NoHayPreguntaSeleccionada Si no se ha llamado previamente a seleccionarPreguntaPorId.
     */
    public void modificarPreguntaTextoSeleccionada(String nuevoTexto) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = getpreguntaseleccionada();
        p.setTexto(nuevoTexto);
    }

    /**
     * Modifica el tipo de la pregunta seleccionada actualmente.
     * @param nuevoTipo El nuevo objeto de tipo TipoPregunta.
     * @throws NoHayEncuestaSeleccionada Si no hay encuesta cargada.
     * @throws NoHayPreguntaSeleccionada Si no hay una pregunta seleccionada.
     */
    public void modificarPreguntaTipoSeleccionada(TipoPregunta nuevoTipo) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = getpreguntaseleccionada();
        p.setTipoPregunta(nuevoTipo);
    }

    /**
     * Establece la descripción o instrucción adicional de la pregunta seleccionada.
     * @param descripcion Texto descriptivo de la pregunta.
     * @throws NoHayEncuestaSeleccionada Si no hay encuesta cargada.
     * @throws NoHayPreguntaSeleccionada Si no hay una pregunta seleccionada.
     */
    public void setDescripcionPreguntaSeleccionada(String descripcion) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = getpreguntaseleccionada();
        p.setDescripcion(descripcion);
    }

    /**
     * Descarga la encuesta de la memoria del controlador sin afectar a la persistencia.
     * @throws NoHayEncuestaSeleccionada Si no había ninguna encuesta cargada.
     */
    public void deseleccionarEncuesta() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        this.encuesta = null;
    }

    /**
     * Elimina definitivamente la encuesta actual tanto de la memoria como del sistema de persistencia.
     * @throws NoHayEncuestaSeleccionada Si no hay encuesta cargada para borrar.
     */
    public void borrarEncuestaActual() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        CtrlPersistencia.getInstance().borrarEncuesta(this.encuesta.getCreador(), this.encuesta.getTitulo());
        this.encuesta = null;
    }

    /**
     * Cambia el estado de obligatoriedad de la pregunta seleccionada.
     * @param obligatorio True si la pregunta debe ser obligatoria, false en caso contrario.
     * @throws NoHayEncuestaSeleccionada Si no hay encuesta cargada.
     * @throws NoHayPreguntaSeleccionada Si no hay una pregunta seleccionada.
     */
    public void setObligatorioPreguntaSeleccionada(boolean obligatorio) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        Pregunta p = getpreguntaseleccionada();
        p.setObligatorio(obligatorio);
    }


    /**
     * Crea un objeto de tipo TipoPregunta basado en un mapa de datos.
     * Soporta los tipos "FormatoLibre", "Numerica" y "ConOpciones".
     * @param datosPregunta Mapa con las claves necesarias (TIPO, MIN, MAX, NUMOPCIONES, OPCIONi, etc.).
     * @return Una instancia de la subclase de TipoPregunta correspondiente.
     * @throws IllegalArgumentException Si el tipo especificado no es reconocido.
     */
    public TipoPregunta getTipo(HashMap<String,String> datosPregunta){
        String tipo = datosPregunta.get("TIPO");
        switch (tipo) {
            case "FormatoLibre" -> {
                return new FormatoLibre();
            }
            case "Numerica" -> {
                int min = Integer.parseInt(datosPregunta.get("MIN"));
                int max = Integer.parseInt(datosPregunta.get("MAX"));
                return new Numerica(min, max);
            }
            case "ConOpciones" -> {
                int maxSelect = Integer.parseInt(datosPregunta.get("MAXSELECT"));
                ConOpciones tipoOpciones = new ConOpciones();
                if(maxSelect > 0) {
                    tipoOpciones.setMaxSelect(maxSelect);
                }
                int numOpciones = Integer.parseInt(datosPregunta.get("NUMOPCIONES"));
                for(int i = 1; i <= numOpciones; i++) {
                    String textoOpcion = datosPregunta.get("OPCION" + i);
                    Opcion opcion = new Opcion(i, textoOpcion);
                    tipoOpciones.addOpcion(opcion);
                }
                return tipoOpciones;
            }
            default -> throw new IllegalArgumentException("Tipo de pregunta desconocido: " + tipo);
        }
    }

    /**
     * Busca una pregunta por su ID dentro de la encuesta cargada.
     * @param id El identificador a buscar.
     * @return La Pregunta encontrada o null si no existe.
     * @throws NoHayEncuestaSeleccionada Si no hay encuesta cargada.
     */
    public Pregunta findPreguntaById(int id) {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        for (Pregunta p : this.encuesta.getPreguntas()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * Recupera una lista de todas las encuestas disponibles en el sistema de persistencia.
     * @return Una lista de pares (Título, Autor) de todas las encuestas.
     */
    public ArrayList<AbstractMap.SimpleEntry<String, String>> getTituloAutorAllEncuestas() {
        return CtrlPersistencia.getInstance().getTituloAutorAllEncuestas();
    }

    /**
     * Obtiene una representación estructurada de todas las preguntas de la encuesta en memoria.
     * Las preguntas se devuelven ordenadas por su ID.
     * @return Una lista de Mapas, donde cada mapa contiene los atributos de una pregunta (ID, TEXTO, TIPO, etc.).
     * @throws NoHayEncuestaSeleccionada Si no hay una encuesta cargada.
     */
    public ArrayList<HashMap<String, String>> getPreguntasEncuestaEnMemoria() {
        if (this.encuesta == null) throw new NoHayEncuestaSeleccionada("No hay encuesta cargada");
        ArrayList<HashMap<String, String>> preguntasInfo = new ArrayList<>();

        //ordenar preguntas por id
        TreeMap<Integer, Pregunta> preguntasOrdenadas = new TreeMap<>();
        for (Pregunta p: this.encuesta.getPreguntas()) {
            preguntasOrdenadas.put(p.getId(), p);
        }
        for (int i = 1; i <= preguntasOrdenadas.size(); i++) {
            Pregunta p = preguntasOrdenadas.get(i);
            HashMap<String, String> info = new HashMap<>();
            TipoPregunta tipo = p.getTipoPregunta();

            info.put("ID", String.valueOf(p.getId()));
            info.put("OBLIGATORIO", String.valueOf(p.isObligatorio()));
            info.put("TEXTO", p.getTexto());
            info.put("DESCRIPCION", p.getDescripcion());
            info.put("TIPO", tipo.getClass().getSimpleName());

            if (tipo instanceof ConOpciones c) {
                info.put("NUMOPCIONES", String.valueOf(c.getOpciones().size()));
                info.put("MAXSELECT", String.valueOf(c.getMaxSelect()));
                for (int j = 1; j <= c.getOpciones().size(); j++) {
                    info.put("OPCION" + j, c.getOpciones().get(j - 1).getTexto());
                }
            } else if (tipo instanceof Numerica n) {
                info.put("MIN", String.valueOf(n.getMin()));
                info.put("MAX", String.valueOf(n.getMax()));
            }

            preguntasInfo.add(info);
        }
        return preguntasInfo;
    }

    //metodo auxiliar para obtener la pregunta seleccionada
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
}
