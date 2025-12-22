package main.persistence.exceptions;

public class FalloPersistencia extends RuntimeException {
    public FalloPersistencia() {};
    public FalloPersistencia(String message) {
        super(message);
    }
}
