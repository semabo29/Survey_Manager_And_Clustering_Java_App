package main.domain.exceptions;

public class YaExistePerfil extends RuntimeException {
    public YaExistePerfil() {}
    public YaExistePerfil(String message) {
        super(message);
    }
}
