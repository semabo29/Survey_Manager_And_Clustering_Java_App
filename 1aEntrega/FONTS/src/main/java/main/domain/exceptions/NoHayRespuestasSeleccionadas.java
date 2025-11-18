package main.domain.exceptions;

public class NoHayRespuestasSeleccionadas extends RuntimeException {
    public NoHayRespuestasSeleccionadas(String message) {
        super(message);
    }
}
