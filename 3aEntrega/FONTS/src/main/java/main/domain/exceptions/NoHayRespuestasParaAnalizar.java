package main.domain.exceptions;

public class NoHayRespuestasParaAnalizar extends RuntimeException {
    public NoHayRespuestasParaAnalizar(String message) {
        super(message);
    }
}
