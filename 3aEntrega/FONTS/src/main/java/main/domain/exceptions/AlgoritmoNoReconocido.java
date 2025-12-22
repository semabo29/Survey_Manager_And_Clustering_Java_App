package main.domain.exceptions;

public class AlgoritmoNoReconocido extends RuntimeException {
    public AlgoritmoNoReconocido(String message) {
        super(message);
    }
}
