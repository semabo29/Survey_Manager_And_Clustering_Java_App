package main.domain.exceptions;

public class PreguntaNoEnEncuesta extends RuntimeException {
    public PreguntaNoEnEncuesta(String message) {
        super(message);
    }
}
