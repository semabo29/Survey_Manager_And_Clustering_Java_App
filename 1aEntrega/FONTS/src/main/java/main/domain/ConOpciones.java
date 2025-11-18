package main.domain;

import main.domain.exceptions.TDatosIncorrecto;
import main.domain.types.TDatosOpciones;
import main.domain.types.TDatos;
import main.domain.Opcion;

import java.util.ArrayList;
import java.util.HashSet;

public class ConOpciones implements TipoPregunta {
    private ArrayList<Opcion> opciones;
    // Hay que cambiar numRespuestas
    private int numRespuestas;
    private int idNextOpcion;
    private boolean orden;

    public ConOpciones(int numRespuestas) {
        if (numRespuestas <= 0) {
            throw new IllegalArgumentException("numRespuestas es menor a 1");
        }
        this.numRespuestas = numRespuestas;
        opciones = new ArrayList<>();
        idNextOpcion = 1;
        orden = false;
    }

    public ConOpciones() {
        numRespuestas = 1;
        opciones = new ArrayList<>();
        idNextOpcion = 1;
        orden = false;
    }

    public void setNumRespuestas(int numRespuestas) {
        if (numRespuestas <= 0) {
            throw new IllegalArgumentException("numRespuestas es menor a 1");
        }
        this.numRespuestas = numRespuestas;
    }

    public int getNumRespuestas() {
        return numRespuestas;
    }

    public void setTexto(int id, String texto) {
        if (id > 0 && id <= opciones.size()) {
            opciones.get(id - 1).setTexto(texto);
        }
    }

    public void removeOpcion(int id) {
        if (id > 0 && id <= opciones.size()) {
            opciones.remove(id - 1);
            for (int i = id - 1; i < opciones.size(); ++i) {
                opciones.get(i).setId(i + 1);
            }
        }
    }

    public int getIdNextOpcion() {
        return idNextOpcion;
    }

    public boolean addOpcion(Opcion opcion) {
        if (opcion.getId() != idNextOpcion) {
            return false;
        }

        opciones.add(opcion);
        idNextOpcion++;
        if (opciones.size() > numRespuestas) numRespuestas = opciones.size();
        return true;
    }

    public ArrayList<Opcion> getOpciones() {
        return opciones;
    }

    // Elimina opcion y reduce los id de sus opciones posteriores
    public void removeOpcion(Opcion opcion) {
        if (opciones.remove(opcion)) {
            for (int i = opcion.getId() - 1; i < opciones.size(); ++i)
                opciones.get(i).setId(i + 1);
            idNextOpcion--;
            if (opciones.size() < numRespuestas) numRespuestas  = opciones.size();
        }
    }

    public boolean isOrden() {
        return orden;
    }

    public void setOrden(boolean orden) {
        this.orden = orden;
    }

    @Override
    public boolean isValid(TDatos datos, boolean obligatorio) {
        if (!(datos instanceof TDatosOpciones)) {
            return false;
        }

        TDatosOpciones datosOpciones = (TDatosOpciones) datos;
        boolean valid;

        // Comprobamos que no sea vacio si es obligatorio
        ArrayList<Integer> arrayOpciones = datosOpciones.getIdOpciones();
        if (obligatorio && arrayOpciones.isEmpty()) {
            return false;
        }

        HashSet<Integer> ids = new HashSet<>(arrayOpciones);
        // Comprobamos que numRespuestas sea igual y no tenga mas opciones seleccionadas que numRespuestas
        valid = arrayOpciones.size() == ids.size() && datosOpciones.getNumOpciones() == numRespuestas
                && arrayOpciones.size() <= numRespuestas && datosOpciones.isOrden() == orden;

        // Comprobamos si los textos de las opciones de datosOpciones son de opciones
        if (valid && !arrayOpciones.isEmpty()) {
            valid = opcionesContiene(arrayOpciones);
        }

        return valid;
    }

    @Override
    public void validar(TDatos datos, boolean obligatorio) throws Exception {
        if (!(datos instanceof TDatosOpciones)) {
            throw new TDatosIncorrecto("ConOpciones debe ser TDatosOpciones");
        }

        // Comprobamos que no sea vacio si es obligatorio
        TDatosOpciones datosOpciones = (TDatosOpciones) datos;
        ArrayList<Integer> arrayOpciones = datosOpciones.getIdOpciones();
        if (obligatorio && arrayOpciones.isEmpty()) {
            throw new TDatosIncorrecto("La pregunta es obligatoria");
        }
        HashSet<Integer> ids = new HashSet<>(arrayOpciones);

        if (arrayOpciones.size() == ids.size() && opcionesContiene(arrayOpciones) && arrayOpciones.size() <= numRespuestas) {
            datosOpciones.setNumOpciones(numRespuestas);
            datosOpciones.setOrden(orden);
        } else {
            throw new TDatosIncorrecto("La respuesta no es valida");
        }
    }

    // Comprueba que ids contenga identificadores de opciones
    private boolean opcionesContiene(ArrayList<Integer> arrayOpciones) {
        for (Integer i : arrayOpciones) {
            if (i >= idNextOpcion) {
                return false;
            }
        }
        return true;
    }
}
