package main.domain.exceptions;

public class NumeroModalidadesMenorQueDos extends IllegalArgumentException {
    public NumeroModalidadesMenorQueDos(String message) {
        super(message);
    }
}
