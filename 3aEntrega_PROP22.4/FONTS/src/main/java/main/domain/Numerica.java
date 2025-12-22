package main.domain;

import main.domain.exceptions.TDatosIncorrecto;
import main.domain.types.TDatos;
import main.domain.types.TDatosInteger;

/**
 * Tipo de pregunta numérica.
 * Esta clase representa una pregunta numérica con un intervalo que limita el valor que pueden tener las respuestas.
 * Solo acepta números enteros.
 * @author Yimin Jin
 */
public class Numerica implements TipoPregunta {
    private int min;
    private int max;

    private static final String minMayor = "El mínimo es mayor al máximo";
    private static final String tipoDatosIncorrecto = "La respuesta no numérica";
    private static final String tipoInvalido = "La respuesta es inválida";
    /**
     * Constructor de la clase.
     * @param min Número mínimo.
     * @param max Número máximo. Este número tiene que ser mayor al mínimo.
     */
    public Numerica(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException(minMayor);
        }
        this.min = min;
        this.max = max;
    }

    /**
     * Obtiene el valor mínimo aceptado.
     * @return Valor mínimo.
     */
    public int getMin() {
        return min;
    }

    /**
     * Añade el valor mínimo. El valor tiene que ser menor al máximo.
     * @param min Valor mínimo.
     */
    public void setMin(int min) {
        if (min < max) this.min = min;
        else throw new IllegalArgumentException(minMayor);
    }

    /**
     * Obtiene el valor máximo aceptado.
     * @return Valor máximo.
     */
    public int getMax() {
        return max;
    }

    /**
     * Añade el valor máximo. El valor tiene que ser mayor al mínimo.
     * @param max Valor máximo.
     */
    public void setMax(int max) {
        if (max > min) this.max = max;
        else throw new IllegalArgumentException(minMayor);
    }

    /**
     * Operación que comprueba que una respuesta a una pregunta numérica sea válida.
     * @param datos Datos de una respuesta.
     * @param obligatorio Obligatoriedad de la pregunta.
     * @return Validez de la respuesta.
     * Una respuesta es válida si los datos corresponden a una pregunta numérica,
     * su valor está dentro del intervalo y que su máximo y mínimo correspondan a los de la pregunta.
     */
    @Override
    public boolean isValid(TDatos datos, boolean obligatorio) {
        if (!(datos instanceof TDatosInteger)) {
            return false;
        }
        TDatosInteger datosInt = (TDatosInteger) datos;
        // Comprueba si el valor es correcto dependiendo de obligatorio
        if (obligatorio && !numValido(datosInt.getNum())) return false;
        // Comprueba que min y max coincidan
        return datosInt.getNumMin() == min && datosInt.getNumMax() == max;
    }

    /**
     * Valida los datos de una respuesta. Añade a los datos los valores mínimo y máximo.
     * @param datos Datos incompletos de una respuesta.
     * @param obligatorio Obligatoriedad de la pregunta.
     * @throws Exception Lanza la excepción {@link TDatosIncorrecto} si la respuesta no es del tipo correspondiente,
     * si es vacía y la pregunta es obligatoria, o si no cumple con las restricciones.
     */
    @Override
    public void validar(TDatos datos, boolean obligatorio) throws Exception {
        if (!(datos instanceof TDatosInteger)) {
            throw new TDatosIncorrecto(tipoDatosIncorrecto);
        }

        TDatosInteger datosInt = (TDatosInteger) datos;
        if (numValido(datosInt.getNum()) || !obligatorio) {
            datosInt.setNumMin(min);
            datosInt.setNumMax(max);
        }
        else throw new TDatosIncorrecto(tipoInvalido);
    }

    // Comprueba que num este entre min y max
    private boolean numValido(int num) {
        return num >= min && num <= max;
    }
}
