package main.domain.exceptions;

public class NoHayEncuestaSeleccionada extends RuntimeException {
    public NoHayEncuestaSeleccionada(String message) {
        super(message);
    }
}
