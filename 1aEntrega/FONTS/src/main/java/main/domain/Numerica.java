package main.domain;

import main.domain.exceptions.TDatosIncorrecto;
import main.domain.types.TDatos;
import main.domain.types.TDatosInteger;

public class Numerica implements TipoPregunta {
    private int min;
    private int max;

    public Numerica(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("min es mayor a max");
        }
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        if (min < max) this.min = min;
        else throw new IllegalArgumentException("min es mayor a max");
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > min) this.max = max;
        else throw new IllegalArgumentException("min es mayor a max");
    }

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

    @Override
    public void validar(TDatos datos, boolean obligatorio) throws Exception {
        if (!(datos instanceof TDatosInteger)) {
            throw new TDatosIncorrecto("Numerica debe ser TDatosInteger");
        }

        TDatosInteger datosInt = (TDatosInteger) datos;
        if (numValido(datosInt.getNum()) || !obligatorio) {
            datosInt.setNumMin(min);
            datosInt.setNumMax(max);
        }
        else throw new TDatosIncorrecto("La respuesta no es valida");
    }

    // Comprueba que num este entre min y max
    private boolean numValido(int num) {
        return num >= min && num <= max;
    }
}
