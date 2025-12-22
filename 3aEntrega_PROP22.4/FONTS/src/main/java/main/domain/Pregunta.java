package main.domain;

import main.domain.types.TDatos;

import java.io.Serializable;

/**
 * Clase que representa una pregunta de una encuesta.
 * Esta clase contiene el ID de la pregunta, el texto de la pregunta, la obligatoriedad de la pregunta,
 * la {@link Encuesta} a la cuál pertenece, un {@link TipoPregunta},
 * y una opcionalmente una descripción de la pregunta.
 * @author Yimin Jin
 */
public class Pregunta implements Serializable {
    private int id;
    private String texto;
    private boolean obligatorio;

    private Encuesta encuesta;
    private TipoPregunta tipoPregunta;

    // Variables opcionales
    private String descripcion;

    // Creadora

    /**
     * Constructor de la clase. Crea una pregunta que pertenece a una encuesta y con los datos que obligatoriamente
     * debe tener. Por defecto la descripción está vacía.
     * @param encuesta Encuesta a la cuál pertenece.
     * @param id ID de la pregunta.
     * @param texto Texto de la pregunta.
     * @param obligatorio Obligatoriedad de la pregunta.
     * @param tipoPregunta Tipo de pregunta.
     */
    public Pregunta(Encuesta encuesta, int id, String texto, boolean obligatorio, TipoPregunta tipoPregunta) {
        this.id = id;
        this.texto = texto;
        this.obligatorio = obligatorio;
        this.encuesta = encuesta;
        this.tipoPregunta = tipoPregunta;
        descripcion = "";
    }

    // Getters

    /**
     * Obtiene el ID de la pregunta.
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el texto de la pregunta.
     * @return Texto.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Obtiene la obligatoriedad de la pregunta.
     * @return Si es obligatoria o no.
     */
    public boolean isObligatorio() {
        return obligatorio;
    }

    /**
     * Obtiene la descripción de la pregunta.
     * @return Descripción.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene la encuesta a la cuál pertenece.
     * @return Encuesta.
     */
    public Encuesta getEncuesta() {
        return encuesta;
    }

    /**
     * Obtiene el tipo de la pregunta.
     * @return Tipo de la pregunta.
     */
    public TipoPregunta getTipoPregunta() {
        return tipoPregunta;
    }

    // Setters

    /**
     * Añade un nuevo id.
     * @param id ID nuevo.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Añade una nueva descripción.
     * @param descripcion Descripción nueva.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Añade una nueva obligatoriedad.
     * @param obligatorio Obligatoriedad.
     */
    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    /**
     * Añade un nuevo texto.
     * @param texto Texto nuevo.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Añade un tipo de pregunta nuevo.
     * @param tipoPregunta Tipo de pregunta nuevo.
     */
    public void setTipoPregunta(TipoPregunta tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    // Validar

    /**
     * Operación que comprueba si una respuesta es válida para esta pregunta.
     * La pregunta utiliza su tipo para comprobar la validez de la respuesta.
     * @param datos Datos de la respuesta.
     * @return Validez de la respuesta.
     */
    public boolean isValid(TDatos datos) {
        return tipoPregunta.isValid(datos, obligatorio);
    }

    /**
     * Operación de validación de una respuesta.
     * La pregunta utiliza su tipo para validar la respuesta y añadir los datos que falten.
     * @param datos Datos incompletos de una respuesta.
     * @throws Exception Lanza una excepción si la respuesta no es validable para el tipo de pregunta.
     */
    public void validar(TDatos datos) throws Exception {
        tipoPregunta.validar(datos, obligatorio);
    }
}