package main.persistence;

import main.domain.Encuesta;
import main.persistence.exceptions.FicheroNoExiste;
import main.persistence.exceptions.LecturaNula;
import main.persistence.exceptions.FormatoInvalido;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GestorEncuesta {
    public GestorEncuesta() {}

    //convierte un titulo en un nombre de fichero valido
    //Reemplaza caracteres no permitidos por '_' (todo lo que no sea letra, numero, punto o guion)
    public String normalizarNombre(String titulo) {
        return titulo.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }

    //El formato del fichero guarda una encuesta completa (con preguntas y tipos).enc
    public void guardarEncuesta(Encuesta encuesta) throws LecturaNula, FormatoInvalido{
        String nombreFichero = normalizarNombre(encuesta.getTitulo()) + ".enc";
        String ubicacion = "ficheros/creadores/" + encuesta.getCreador() + "/" + normalizarNombre(encuesta.getTitulo());

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        try {
            Files.createDirectories(dir);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
                oos.writeObject(encuesta);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la encuesta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Cargar encuesta desde fichero .enc y devuelve la instancia de Encuesta
    public Encuesta cargarEncuesta(String emailCreador, String titulo) throws FormatoInvalido, LecturaNula, FicheroNoExiste {
        //Logica de bajo nivel para leer el fichero en java
        String nombreFichero = normalizarNombre(titulo) + ".enc";
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + normalizarNombre(titulo);

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            //Lee el objeto del fichero (la encuesta serializada)
            Object obj = ois.readObject();

            //Si el fichero leido es nulo, lanza excepcion
            if (obj == null) {
                throw new LecturaNula("El fichero de encuesta está vacío.");
            }
            //Si el fichero leido no contiene una encuesta valida, lanza excepcion
            if (!(obj instanceof Encuesta)) {
                throw new FormatoInvalido("El fichero no contiene una instancia de Encuesta válida.");
            }

            return (Encuesta) obj;

        }
        catch (FileNotFoundException e) {
            throw new FicheroNoExiste("No se encontró el fichero de encuesta: " + nombreFichero);
        }
        catch (ClassNotFoundException e) {
            throw new FormatoInvalido("Clase Encuesta no encontrada al deserializar.");
        }
        catch (IOException e) {
            System.err.println("Error al leer el fichero de encuesta: " + e.getMessage());
            return null;
        }
    }

    public void borrarEncuesta(String emailCreador, String titulo) throws FicheroNoExiste {
        String nombreFichero = normalizarNombre(titulo) + ".enc";
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + normalizarNombre(titulo);

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        File fichero = path.toFile();
        if (fichero.exists()) {
            if (!fichero.delete()) {
                System.err.println("No se pudo borrar el fichero de encuesta: " + nombreFichero);
            }
        }
        else {
            throw new FicheroNoExiste("No se encontró el fichero de encuesta para borrar: " + nombreFichero);
        }
    }
}