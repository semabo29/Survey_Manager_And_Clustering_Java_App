package main.domain.exceptions;

public class InicializadorYAlgoritmoIncompatibles extends RuntimeException {
    public InicializadorYAlgoritmoIncompatibles(String message) {
        super(message);
    }
}
