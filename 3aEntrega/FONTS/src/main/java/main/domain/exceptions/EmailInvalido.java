package main.domain.exceptions;

public class EmailInvalido extends RuntimeException {
    public EmailInvalido() {}
    public EmailInvalido(String message) {
        super(message);
    }
}
