package main.persistence.exceptions;

public class FormatoInvalido extends RuntimeException {
    public FormatoInvalido() {
        super();
    }
    public FormatoInvalido(String message) {
        super(message);
    }
}
