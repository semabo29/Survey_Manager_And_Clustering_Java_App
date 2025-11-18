package main.domain.types;

import java.io.Serializable;
import java.util.ArrayList;

public interface TDatos extends Serializable {
    public Float calcularDistancia(TDatos t);
    public TDatos getComponenteCentroide(ArrayList<TDatos> datos);
}
