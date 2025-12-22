package main.domain.exceptions;

public class EvaluadorNoReconocido extends RuntimeException {
    public EvaluadorNoReconocido(String message) {
        super(message);
    }
}
