package main.persistence.exceptions;

public class LecturaNula extends RuntimeException {
    public LecturaNula() {super();}

    public LecturaNula(String message) {super(message);}
}
