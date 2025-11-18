package main.domain.types;

import main.domain.RespuestaEncuesta;
import main.domain.exceptions.*;
import java.util.ArrayList;

public class TDatosInteger implements TDatos{

    int num;
    int numMax;
    int numMin;

    public TDatosInteger() {
        this.num = 0;
        this.numMax = Integer.MAX_VALUE;
        this.numMin = Integer.MIN_VALUE;
    }

    public TDatosInteger(int n) {
        this.num = n;
        this.numMax = Integer.MAX_VALUE;
        this.numMin = Integer.MIN_VALUE;
    }

    public TDatosInteger(int n, int nMax, int nMin) {
        this.num = n;
        this.numMax = nMax;
        this.numMin = nMin;
    }

    public int getNum() {
        return num;
    }
    public int getNumMax() {
        return numMax;
    }
    public int getNumMin() {
        return numMin;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public void setNumMax(int numMax) {
        this.numMax = numMax;
    }
    public void setNumMin(int numMin) {
        this.numMin = numMin;
    }

    @Override
    public Float calcularDistancia(TDatos t) {
        TDatosInteger aux = (TDatosInteger) t;
        return numericaDistancia(this.num, aux.getNum(), this.numMax, this.numMin);
    }

    @Override
    public TDatos getComponenteCentroide(ArrayList<TDatos> datos) {
        int sum = 0;
        for(TDatos d : datos) {
            TDatosInteger dato = (TDatosInteger) d;
            sum += dato.getNum();
        }
        sum /= datos.size();

        return new TDatosInteger(sum, this.numMax, this.numMin);
    }

    private static Float numericaDistancia(int numA, int numB, int numMax, int numMin) {
        if(numMax == numMin) throw new NumMaxIgualANumMin("NumMax es igual a numMin, lo que provoca una división por cero");
        if(numA >= Integer.MAX_VALUE/2 && numB <= Integer.MIN_VALUE/2 || numA <= Integer.MIN_VALUE/2 && numB >= Integer.MAX_VALUE/2
        || numMax >= Integer.MAX_VALUE/2 && numMin <= Integer.MIN_VALUE/2) throw new DistanciaEntreIntegersDemasiadoGrande("La distancia entre " +
                "los integers es demasiado grande");
        float diffAB = Math.abs(numA-numB);
        float diffMaxMin = numMax-numMin;
        return diffAB/diffMaxMin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TDatosInteger other = (TDatosInteger) obj;

        return  num == other.num;
    }
}
