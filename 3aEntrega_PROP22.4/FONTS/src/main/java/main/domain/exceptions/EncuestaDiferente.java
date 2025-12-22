package main.domain.exceptions;

public class EncuestaDiferente extends Exception {
    public EncuestaDiferente() {
        super();
    }

    public EncuestaDiferente(String message) {
        super(message);
    }
}
