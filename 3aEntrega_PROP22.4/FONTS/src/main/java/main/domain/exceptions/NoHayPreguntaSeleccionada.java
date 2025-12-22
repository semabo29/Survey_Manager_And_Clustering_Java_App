package main.domain.exceptions;

public class NoHayPreguntaSeleccionada extends RuntimeException {
    public NoHayPreguntaSeleccionada(String message) {
        super(message);
    }
}
