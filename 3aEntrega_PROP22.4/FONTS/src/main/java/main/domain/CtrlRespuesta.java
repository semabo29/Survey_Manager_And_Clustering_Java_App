package main.domain;

import main.domain.exceptions.NoHayRespuestasSeleccionadas;
import main.domain.types.TDatos;
import main.persistence.CtrlPersistencia;
import main.persistence.exceptions.FalloPersistencia;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * Controlador de Respuestas de Encuestas.
 * Permite crear, cargar, guardar y manipular respuestas de encuestas.
 * @author Hadeer Abbas Khalil Wysocka
 * @author Sergi Malaguilla Bombin
 * @author Andres Lafuente Patau
 */
public class CtrlRespuesta {
    private RespuestaEncuesta respuestaActual;
    private HashSet<RespuestaEncuesta> respuestasCargadas;

    public CtrlRespuesta() {
        this.respuestaActual = null;
        this.respuestasCargadas = new HashSet<>();
    }

    /**
     * Carga una respuesta de encuesta específica desde la persistencia.
     * @param emailCreador El email del creador de la encuesta.
     * @param titulo El título de la encuesta.
     * @param emailRespuesta El email del autor de la respuesta.
     * @throws NoHayRespuestasSeleccionadas Si la respuesta no existe o no pudo ser leída.
     * @throws FalloPersistencia Si ocurre un error al deserializar la respuesta.
     */
    public void cargarRespuesta (String emailCreador, String titulo,  String emailRespuesta) {
            ByteArrayInputStream datos = CtrlPersistencia.getInstance().cargarRespuestaEncuesta(emailCreador, titulo, emailRespuesta);
            if (datos == null)
                throw new NoHayRespuestasSeleccionadas("La respuesta no existe o no pudo ser leída");
            try (ObjectInputStream ois = new ObjectInputStream(datos)) {
                this.respuestaActual = (RespuestaEncuesta) ois.readObject();
                this.respuestasCargadas.add(this.respuestaActual);
            }
            catch (IOException | ClassNotFoundException e) {
                throw new FalloPersistencia("Error al deserializar respuesta: " + e.getMessage());
           }
    }

    /**
     * Carga todas las respuestas de una encuesta específica desde la persistencia.
     * @param emailCreador El email del creador de la encuesta.
     * @param titulo El título de la encuesta.
     * @throws NoHayRespuestasSeleccionadas Si las respuestas no existen o no pudieron ser leídas.
     * @throws FalloPersistencia Si ocurre un error al deserializar alguna respuesta.
     */
    public void cargarRespuestas(String emailCreador, String titulo) {
        HashSet<ByteArrayInputStream> datos = CtrlPersistencia.getInstance().cargarRespuestasDeEncuesta(emailCreador, titulo);
        if (datos == null)
            throw new NoHayRespuestasSeleccionadas("Las respuestas no existen o no pudieron ser leídas");
        HashSet<RespuestaEncuesta> respuestas = new HashSet<>();
        for (ByteArrayInputStream dato : datos){
            try (ObjectInputStream ois = new ObjectInputStream(dato)) {
                RespuestaEncuesta respuesta = (RespuestaEncuesta) ois.readObject();
                respuestas.add(respuesta);
            }
            catch (IOException | ClassNotFoundException e) {
                throw new FalloPersistencia("Error al deserializar respuesta: " + e.getMessage());
            }
        }
        this.respuestasCargadas = respuestas;
    }

    /**
     * Carga todas las respuestas asociadas a una lista de emails desde la persistencia.
     * @param emails La lista de emails cuyos respuestas se desean cargar.
     * @throws NoHayRespuestasSeleccionadas Si alguna respuesta no existe o no pudo ser leída.
     * @throws FalloPersistencia Si ocurre un error al deserializar alguna respuesta.
     */
    public void cargarRespuestasPorEmails (ArrayList<String> emails) {
        for (String email : emails) {
            HashSet<SimpleEntry<String,String>> encuestasRespondidas = CtrlPersistencia.getInstance().getRespuestasByEmail(email);
            for (SimpleEntry<String,String> encuesta : encuestasRespondidas) {
                ByteArrayInputStream dato = CtrlPersistencia.getInstance().cargarRespuestaEncuesta(encuesta.getValue(), encuesta.getKey(), email);
                if (dato == null)
                    throw new NoHayRespuestasSeleccionadas("Una respuesta no existe o no pudo ser leída");
                try (ObjectInputStream ois = new ObjectInputStream(dato)) {
                    RespuestaEncuesta r = (RespuestaEncuesta) ois.readObject();
                    this.respuestasCargadas.add(r);
                }
                catch (IOException | ClassNotFoundException e) {
                    throw new FalloPersistencia("Error al deserializar respuesta: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Guarda la respuesta actual en la persistencia.
     * @throws IllegalArgumentException Si no hay ninguna respuesta actual seleccionada.
     * @throws FalloPersistencia Si ocurre un error al guardar la respuesta.
     */
    public void guardarRespuesta( ) {
        checkRespuestaActual();
        try (ByteArrayOutputStream datos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(datos)){
            oos.writeObject(this.respuestaActual);
            oos.flush();
            CtrlPersistencia.getInstance().guardarRespuestaEncuesta(this.respuestaActual.getCreador(), this.respuestaActual.getEncuesta(), this.respuestaActual.getEmailrespuesta(),  datos);
        }
        catch (IOException e) {
            throw new FalloPersistencia("Error al guardar la respuesta: " + e.getMessage());
        }
    }

    /**
     * Crea una nueva respuesta de encuesta y la establece como la respuesta actual.
     * @param encuesta La encuesta para la cual se crea la respuesta.
     * @param emailAutorRespuesta El email del autor de la respuesta.
     * @throws IllegalArgumentException Si la encuesta o el email del autor son null.
     */
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

    /**
     * Borra la respuesta actual de la persistencia y de la memoria.
     * @throws IllegalArgumentException Si no hay ninguna respuesta actual seleccionada.
     */
    public void borrarRespuestaActual() {
        checkRespuestaActual();

        String emailCreador = this.respuestaActual.getCreador(); // Creador de la encuesta
        String titulo = this.respuestaActual.getEncuesta();         // Título de la encuesta
        String emailRespuesta = this.respuestaActual.getEmailrespuesta();

        this.respuestasCargadas.remove(this.respuestaActual);
        this.respuestaActual = null;
        CtrlPersistencia.getInstance().borrarRespuestaEncuesta(emailCreador, titulo, emailRespuesta);
    }

    /**
     * Añade un dato de respuesta a la respuesta actual.
     * @param idPregunta El ID de la pregunta a la que se añade el dato.
     * @param datos El dato que se añade a la respuesta.
     * @throws IllegalArgumentException Si no hay ninguna respuesta actual seleccionada.
     */
    public void addDatoRespuesta(int idPregunta, TDatos datos) {
        checkRespuestaActual();
        this.respuestaActual.addRespuesta(idPregunta, datos);
    }

    /**
     * Obtiene la respuesta actual seleccionada.
     * @return La respuesta actual.
     * @throws IllegalArgumentException Si no hay ninguna respuesta actual seleccionada.
     */
    public RespuestaEncuesta getRespuestaActual() {
        checkRespuestaActual();
        return this.respuestaActual;
    }

    /**
     * Obtiene el conjunto de respuestas cargadas en memoria.
     * @return Un HashSet conteniendo las respuestas cargadas.
     */
    public HashSet<RespuestaEncuesta> getRespuestasCargadas() {
        return this.respuestasCargadas;
    }

    /**
     * Obtiene el dato de respuesta para una pregunta específica en la respuesta actual.
     * @param idPregunta El ID de la pregunta cuyo dato se desea obtener.
     * @return El dato de respuesta correspondiente a la pregunta.
     * @throws IllegalArgumentException Si no hay ninguna respuesta actual seleccionada.
     */
    public TDatos getDatoRespuesta(int idPregunta) {
        checkRespuestaActual();
        return this.respuestaActual.getDatosRespuesta().get(idPregunta);
    }

    /**
     * Obtiene el mapa completo de datos de respuesta en la respuesta actual.
     * @return Un TreeMap que mapea los IDs de las preguntas a sus respectivos datos de respuesta.
     * @throws IllegalArgumentException Si no hay ninguna respuesta actual seleccionada.
     */
    public TreeMap<Integer, TDatos> getMapaRespuestasActual() {
        checkRespuestaActual();
        return this.respuestaActual.getDatosRespuesta();
    }

    /**
     * Deselecciona la respuesta actual, estableciéndola a null.
     * @throws IllegalArgumentException Si no hay ninguna respuesta actual seleccionada.
     */
    public void deseleccionarRespuesta() {
        checkRespuestaActual();
        this.respuestaActual = null;
    }

    /**
     * Obtiene una lista de los emails de las respuestas cargadas en memoria.
     * @return Una ArrayList conteniendo los emails de las respuestas.
     */
    public ArrayList<String> getEmailRespuestasEnMemoria() {
        ArrayList<String> emails = new ArrayList<>();
        for (RespuestaEncuesta respuesta : this.respuestasCargadas) {
            emails.add(respuesta.getEmailrespuesta());
        }
        return emails;
    }

    /**
     * Deselecciona todas las respuestas cargadas en memoria.
     */
    public void deseleccionarRespuestas() {
        this.respuestaActual = null;
        this.respuestasCargadas.clear();
    }

    /**
     * Carga una respuesta de encuesta desde un path específico en la persistencia.
     * @param Spath El path desde donde se carga la respuesta.
     * @throws NoHayRespuestasSeleccionadas Si la respuesta no existe o no pudo ser leída.
     * @throws FalloPersistencia Si ocurre un error al deserializar la respuesta.
     */
    public void cargarRespuestaPorPath(String Spath) {
        Path path = Paths.get(Spath);
        ByteArrayInputStream datos = CtrlPersistencia.getInstance().leerClaseSerializada(path);
        if (datos == null)
            throw new NoHayRespuestasSeleccionadas("La respuesta no existe o no pudo ser leída");
        try (ObjectInputStream ois = new ObjectInputStream(datos)) {
            this.respuestaActual = (RespuestaEncuesta) ois.readObject();
            guardarRespuesta();
            this.respuestasCargadas.add(this.respuestaActual);
        }
        catch (IOException | ClassNotFoundException e) {
            throw new FalloPersistencia("Error al deserializar respuesta: " + e.getMessage());
        }
    }

    //funcion auxiliar para comprobar que hay una respuesta actual seleccionada
    private void checkRespuestaActual() {
        if (this.respuestaActual == null) {
            throw new IllegalArgumentException("No hay ninguna respuesta");
        }
    }
}
