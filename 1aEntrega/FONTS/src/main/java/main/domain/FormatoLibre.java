package main.domain;

import main.domain.exceptions.TDatosIncorrecto;
import main.domain.types.TDatos;
import main.domain.types.TDatosString;

public class FormatoLibre implements TipoPregunta {

    public FormatoLibre() {}

    @Override
    public boolean isValid(TDatos datos, boolean obligatorio) {
        if (!(datos instanceof TDatosString)) return false;
        TDatosString datosString = (TDatosString) datos;
        if (obligatorio && (datosString.getTexto().isEmpty() || datosString.getTexto().isBlank())) return false;
        return true;
    }

    @Override
    public void validar(TDatos datos, boolean obligatorio) throws Exception {
        if (!(datos instanceof TDatosString)) {
            throw new TDatosIncorrecto("FormatoLibre debe ser TDatosString");
        }

        TDatosString datosString = (TDatosString) datos;
        if (obligatorio && (datosString.getTexto().isEmpty() || datosString.getTexto().isBlank()))
            throw new TDatosIncorrecto("La pregunta es obligatoria");
    }
}
