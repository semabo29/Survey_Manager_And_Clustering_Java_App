package main.persistence;

import main.domain.Perfil;

import java.io.*;
import java.nio.file.*;
import java.lang.Exception;

import main.persistence.exceptions.FicheroNoExiste;
import main.persistence.exceptions.LecturaNula;
import main.persistence.exceptions.FormatoInvalido;

public class GestorPerfil {
    public GestorPerfil() {}

    public void guardarPerfil (Perfil perfil) {
        // Se guardan los datos del perfil en el fichero especial

        String nombreFichero = perfil.getEmail() + ".perf";

        // Path que hemos de poner segun la estructura de como se guardan los ficheros establecida
        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve("ficheros/perfiles");
        Path path = dir.resolve(nombreFichero);

        // La logica bajo nivel de guardar el fichero en java
        try {
            // Creamos los directorios si no existen
            Files.createDirectories(dir);
            // Guardamos el fichero con try-with-resources, cierra automaticamente si falla
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
                oos.writeObject(perfil);
            }
        } catch (IOException e) {
            System.err.println("Error inesperado de E/S al guardar perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Perfil cargarPerfil (String email) throws LecturaNula, FormatoInvalido {
        // Se cargan los datos del perfil desde el fichero especial
        // No hace falta comprobar que es un email valido ya que si no lo es por fuerza no existira el fichero
        String nombreFichero = email + ".perf";

        // Path que hemos de poner segun la estructura de como se guardan los ficheros establecida
        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve("ficheros/perfiles");
        Path path = dir.resolve(nombreFichero);

        // La logica bajo nivel de cargar el fichero en java
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))){
            Object obj = ois.readObject();
            if (obj == null) {
                throw new LecturaNula("El fichero de perfil está vacío.");
            }
            if (!(obj instanceof Perfil)) {
                throw new FormatoInvalido("El fichero no contiene una instancia de Perfil válida.");
            }
            return (Perfil) obj;
        }
        catch (FileNotFoundException e) {
            //devuelve null si no existe el fichero (perfil no existe)
            return null;
        }
        catch (ClassNotFoundException e) {
            throw new FormatoInvalido("Clase Perfil no encontrada al deserializar.");
        }
        catch (IOException e) {
            System.err.println("Error inesperado de E/S al cargar perfil: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    public void borrarPerfil (String email) throws FicheroNoExiste {
        String nombreFichero = email + ".perf";

        // Path que hemos de poner segun la estructura de como se guardan los ficheros establecida
        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve("ficheros/perfiles");
        Path path = dir.resolve(nombreFichero);

        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            throw new FicheroNoExiste("No se encontró el fichero de perfil para borrar: " + nombreFichero);
        } catch (IOException e) {
            System.err.println("Error inesperado de E/S al borrar perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
