package main.domain;

import main.domain.exceptions.TDatosIncorrecto;
import main.domain.types.TDatosOpciones;
import main.domain.types.TDatos;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Tipo de pregunta de múltiples opciones.
 * Esta clase guarda las opciones de una pregunta con múltiples opciones.
 * También limita la cantidad máxima de opciones seleccionables.
 * @author Yimin Jin
 */
public class ConOpciones implements TipoPregunta {
    private ArrayList<Opcion> opciones;
    // Numero maximo de opciones seleccionables
    private int maxSelect;
    private int idNextOpcion;
    private boolean orden;

    private static final String excepcionMaxMenorA1 = "El número máximo de opciones seleccionables es menor a 1";
    private static final String tipoDatosIncorrecto = "La respuesta no es de múltiples opciones";
    private static final String tipoObligatorio = "La pregunta es obligatoria";
    private static final String tipoInvalido = "La respuesta es inválida";

    /**
     * Constructor de la clase con un máximo número de opciones seleccionables.
     * @param maxSelect Número máximo de opciones seleccionables. Este número tiene que ser mayor a 0.
     */
    public ConOpciones(int maxSelect) {
        if (maxSelect <= 0) {
            throw new IllegalArgumentException(excepcionMaxMenorA1);
        }
        this.maxSelect = maxSelect;
        opciones = new ArrayList<>();
        idNextOpcion = 1;
        orden = false;
    }

    /**
     * Constructor de la clase sin argumentos.
     * Por defecto, solo se puede seleccionar una opción como máximo al responder.
     */
    public ConOpciones() {
        maxSelect = 1;
        opciones = new ArrayList<>();
        idNextOpcion = 1;
        orden = false;
    }

    /**
     * Añade del número máximo de opciones seleccionables.
     * @param maxSelect El número máximo de opciones seleccionables. Este número tiene que ser mayor a 0.
     */
    public void setMaxSelect(int maxSelect) {
        if (maxSelect <= 0) {
            throw new IllegalArgumentException(excepcionMaxMenorA1);
        }
        this.maxSelect = maxSelect;
    }

    /**
     * Obtiene el número máximo de opciones seleccionables.
     * @return El número máximo de opciones seleccionables.
     */
    public int getMaxSelect() {
        return maxSelect;
    }

    /**
     * Cambia el texto de una opción. Si no existe la opción, no pasa nada.
     * @param id ID de la opción.
     * @param texto Texto nuevo.
     */
    public void setTexto(int id, String texto) {
        if (id > 0 && id <= opciones.size()) {
            opciones.get(id - 1).setTexto(texto);
        }
    }

    /**
     * Elimina una opción por su ID. Si no existe la opción, no pasa nada.
     * @param id ID de la opción.
     */
    public void removeOpcion(int id) {
        if (id > 0 && id <= opciones.size()) {
            opciones.remove(id - 1);
            for (int i = id - 1; i < opciones.size(); ++i) {
                opciones.get(i).setId(i + 1);
            }
        }
    }

    /**
     * Obtiene el ID de la siguiente opción a insertar.
     * @return ID que tendría que tener la siguiente opción a insertar.
     */
    public int getIdNextOpcion() {
        return idNextOpcion;
    }

    /**
     * Añade una opción a esta pregunta.
     * @param opcion Opción para añadir.
     * @return true si el ID de la opción corresponde a la siguiente opción, false en caso contrario.
     */
    public boolean addOpcion(Opcion opcion) {
        if (opcion.getId() != idNextOpcion) {
            return false;
        }
        opciones.add(opcion);
        idNextOpcion++;
        return true;
    }

    /**
     * Obtiene las opciones de esta pregunta.
     * @return Lista de opciones de esta pregunta.
     */
    public ArrayList<Opcion> getOpciones() {
        return opciones;
    }

    // Elimina opcion y reduce los id de sus opciones posteriores

    /**
     * Elimina una opción de la pregunta.
     * Si la opción existía en la pregunta, se elimina y se reasignan los IDs de las opciones posteriores.
     * @param opcion Opción a eliminar.
     */
    public void removeOpcion(Opcion opcion) {
        if (opciones.remove(opcion)) {
            for (int i = opcion.getId() - 1; i < opciones.size(); ++i)
                opciones.get(i).setId(i + 1);
            idNextOpcion--;
        }
    }

    /**
     * Devuelve si el orden de las opciones tiene importancia.
     * @return Si las opciones tienen orden.
     */
    public boolean isOrden() {
        return orden;
    }

    /**
     * Establece el orden de las opciones.
     * @param orden Si las opciones tienen orden.
     */
    public void setOrden(boolean orden) {
        this.orden = orden;
    }

    /**
     * Operación que comprueba que una respuesta a una pregunta de múltiples opciones sea válida.
     * @param datos Datos de una respuesta.
     * @param obligatorio Obligatoriedad de la pregunta.
     * @return Validez de la respuesta.
     * Una respuesta es válida si los datos corresponden a una pregunta de múltiples opciones,
     * no supera el máximo de opciones seleccionables, tiene el mismo número de opciones que la pregunta,
     * no es vacía si la pregunta es obligatoria y no repite opciones con el mismo ID.
     */
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
        // Comprobamos que maxSelect sea igual y no tenga mas opciones seleccionadas que maxSelect
        valid = arrayOpciones.size() == ids.size() && datosOpciones.getNumOpciones() == opciones.size()
                && arrayOpciones.size() <= maxSelect && datosOpciones.isOrden() == orden;

        // Comprobamos si los textos de las opciones de datosOpciones son de opciones
        if (valid && !arrayOpciones.isEmpty()) {
            valid = opcionesContiene(arrayOpciones);
        }

        return valid;
    }

    /**
     * Valida los datos de una respuesta. Añade a los datos el número de opciones que tiene la pregunta
     * y si tiene en cuenta el orden de las opciones.
     * @param datos Datos incompletos de una respuesta.
     * @param obligatorio Obligatoriedad de la pregunta.
     * @throws Exception Lanza la excepción {@link TDatosIncorrecto} si la respuesta no es del tipo correspondiente,
     * si es vacía y la pregunta es obligatoria, o si no cumple con las restricciones.
     */
    @Override
    public void validar(TDatos datos, boolean obligatorio) throws Exception {
        if (!(datos instanceof TDatosOpciones)) {
            throw new TDatosIncorrecto(tipoDatosIncorrecto);
        }

        // Comprobamos que no sea vacio si es obligatorio
        TDatosOpciones datosOpciones = (TDatosOpciones) datos;
        ArrayList<Integer> arrayOpciones = datosOpciones.getIdOpciones();
        if (obligatorio && arrayOpciones.isEmpty()) {
            throw new TDatosIncorrecto(tipoObligatorio);
        }
        HashSet<Integer> ids = new HashSet<>(arrayOpciones);

        if (arrayOpciones.size() == ids.size() && opcionesContiene(arrayOpciones) && arrayOpciones.size() <= maxSelect) {
            datosOpciones.setNumOpciones(opciones.size());
            datosOpciones.setOrden(orden);
        } else {
            throw new TDatosIncorrecto(tipoInvalido);
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
