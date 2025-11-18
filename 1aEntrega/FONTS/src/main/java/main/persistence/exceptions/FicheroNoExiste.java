package main.persistence.exceptions;

public class FicheroNoExiste extends RuntimeException {
    public FicheroNoExiste(String message) {
        super(message);
    }
}
