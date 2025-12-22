package main.domain.exceptions;

public class InicializadorNoReconocido extends RuntimeException {
    public InicializadorNoReconocido(String message) {
        super(message);
    }
}
