package main.domain;

import main.domain.Encuesta;
import main.domain.TipoPregunta;
import main.domain.exceptions.EncuestaDiferente;
import main.domain.exceptions.PreguntaPosterior;
import main.domain.types.TDatos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.lang.Exception;

public class Pregunta implements Serializable {
    private int id;
    private String texto;
    private boolean obligatorio;

    private Encuesta encuesta;
    private TipoPregunta tipoPregunta;

    // Variables opcionales
    private String imagen;
    private String descripcion;
    private HashSet<Pregunta> dependientes;

    // Creadora
    public Pregunta(Encuesta encuesta, int id, String texto, boolean obligatorio, TipoPregunta tipoPregunta) {
        this.id = id;
        this.texto = texto;
        this.obligatorio = obligatorio;
        this.encuesta = encuesta;
        this.tipoPregunta = tipoPregunta;
        imagen = "";
        descripcion = "";
        dependientes = new HashSet<>();
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    public String getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public Set<Pregunta> getDependientes() {
        return dependientes;
    }

    public TipoPregunta getTipoPregunta() {
        return tipoPregunta;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void addPreguntaDependiente(Pregunta pregunta) throws Exception {
        if (!pregunta.getEncuesta().equals(encuesta)) {
            throw new EncuestaDiferente("La encuesta de la pregunta dependiente es diferente");
        }
        if (pregunta.getId() >= id) {
            throw new PreguntaPosterior("La pregunta a depender es posterior");
        }

        dependientes.add(pregunta);
    }

    public void removePreguntaDependiente(Pregunta pregunta) {
        dependientes.remove(pregunta);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setTipoPregunta(TipoPregunta tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    // Validar

    public boolean isValid(TDatos datos) {
        return tipoPregunta.isValid(datos, obligatorio);
    }

    public void validar(TDatos datos) throws Exception {
        tipoPregunta.validar(datos, obligatorio);
    }
}