package main.domain.exceptions;

public class NoHayPerfilCargado extends RuntimeException {
    public NoHayPerfilCargado(String message) {
        super(message);
    }
}
