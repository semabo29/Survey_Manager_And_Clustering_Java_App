package main.domain;

import main.domain.exceptions.TDatosIncorrecto;
import main.domain.types.TDatos;
import main.domain.types.TDatosString;

/**
 * Tipo de pregunta de respuesta libre.
 * Esta clase representa una pregunta de formato libre.
 * @author Yimin Jin
 */
public class FormatoLibre implements TipoPregunta {
    private static final String tipoDatosIncorrecto = "La respuesta no es de formato libre";
    private static final String tipoObligatorio = "La pregunta es obligatoria";

    /**
     * Constructor de la clase.
     */
    public FormatoLibre() {}

    /**
     * Operación que comprueba que una respuesta a una pregunta de formato libre sea válida.
     * @param datos Datos de una respuesta.
     * @param obligatorio Obligatoriedad de la pregunta.
     * @return Validez de la respuesta.
     * Una respuesta es válida si los datos corresponden a una pregunta de formato libre y que no esté vacía
     * si la pregunta es obligatoria.
     */
    @Override
    public boolean isValid(TDatos datos, boolean obligatorio) {
        if (!(datos instanceof TDatosString)) return false;
        TDatosString datosString = (TDatosString) datos;
        if (obligatorio && (datosString.getTexto().isEmpty() || datosString.getTexto().isBlank())) return false;
        return true;
    }

    /**
     * Valida los datos de una respuesta.
     * @param datos Datos incompletos de una respuesta.
     * @param obligatorio Obligatoriedad de la pregunta.
     * @throws Exception Lanza la excepción {@link TDatosIncorrecto} si la respuesta no es del tipo correspondiente o
     * si es vacía y la pregunta es obligatoria.
     */
    @Override
    public void validar(TDatos datos, boolean obligatorio) throws Exception {
        if (!(datos instanceof TDatosString)) {
            throw new TDatosIncorrecto(tipoDatosIncorrecto);
        }

        TDatosString datosString = (TDatosString) datos;
        if (obligatorio && (datosString.getTexto().isEmpty() || datosString.getTexto().isBlank()))
            throw new TDatosIncorrecto(tipoObligatorio);
    }
}
