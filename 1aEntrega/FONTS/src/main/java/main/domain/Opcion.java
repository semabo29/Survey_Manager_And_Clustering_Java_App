package main.domain;

import java.io.Serializable;

public class Opcion implements Serializable {
    private int id;
    private String texto;

    public Opcion(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setId(int id) {
        this.id = id;
    }
}
