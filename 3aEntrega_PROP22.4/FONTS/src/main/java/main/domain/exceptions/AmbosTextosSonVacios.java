package main.domain.exceptions;

public class AmbosTextosSonVacios extends IllegalArgumentException {
    public AmbosTextosSonVacios(String message) {
        super(message);
    }
}
