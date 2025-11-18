package main.domain;

import main.domain.types.TDatos;

import java.io.Serializable;

public interface TipoPregunta extends Serializable {
    // Comprueba que datos sea valido
    public boolean isValid(TDatos datos, boolean obligatorio);

    // Valida datos, añadiendo los atributos que le falten
    public void validar(TDatos datos, boolean obligatorio) throws Exception;
}
