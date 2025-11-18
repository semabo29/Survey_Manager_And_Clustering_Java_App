package main.persistence;

import main.domain.RespuestaEncuesta;
import main.persistence.exceptions.FicheroNoExiste;
import main.persistence.exceptions.FormatoInvalido;
import main.persistence.exceptions.LecturaNula;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GestorRespuestaEncuesta {
    public GestorRespuestaEncuesta() {}

    //convierte un titulo en un nombre de fichero valido
    //Reemplaza caracteres no permitidos por '_' (todo lo que no sea letra, numero, punto o guion)
    public String normalizarNombre(String titulo) {
        return titulo.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }

    public void guardarRespuestaEncuesta (RespuestaEncuesta resp) {
        // Se guardan los datos en el fichero especial

        String nombreFichero = resp.getEmailrespuesta() + ".res";
        String ubicacion = "ficheros/creadores/" + resp.getCreador() + "/" + normalizarNombre(resp.getEncuesta()) + "/respuestas";

        // Path que hemos de poner segun la estructura de como se guardan los ficheros establecida
        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        // La logica bajo nivel de guardar el fichero en java
        try {
            // Creamos los directorios si no existen
            Files.createDirectories(dir);
            // Guardamos el fichero con try-with-resources, cierra automaticamente si falla
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
                oos.writeObject(resp);
            }
        } catch (IOException e) {
            System.err.println("Error inesperado de E/S al guardar RespuestaEncuesta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public RespuestaEncuesta cargarRespuestaEncuesta (String emailCreador, String titulo, String emailRespuesta) throws LecturaNula, FormatoInvalido {
        // Se cargan los datos desde el fichero especial
        // No hace falta comprobar que los Strings son validos ya que si no lo es por fuerza no existira el fichero
        String nombreFichero = emailRespuesta + ".res";
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + normalizarNombre(titulo) + "/respuestas";

        // Path que hemos de poner segun la estructura de como se guardan los ficheros establecida
        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        // La logica bajo nivel de cargar el fichero en java
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))){
            Object obj = ois.readObject();
            if (obj == null) {
                throw new LecturaNula("El fichero de RespuestaEncuesta está vacío.");
            }
            if (!(obj instanceof RespuestaEncuesta)) {
                throw new FormatoInvalido("El fichero no contiene una instancia de RespuestaEncuesta válida.");
            }
            return (RespuestaEncuesta) obj;
        }
        catch (FileNotFoundException e) {
            throw new FicheroNoExiste("No se encontró el fichero de RespuestaEncuesta: " + nombreFichero);
        }
        catch (ClassNotFoundException e) {
            throw new FormatoInvalido("Clase RespuestaEncuesta no encontrada al deserializar.");
        }
        catch (IOException e) {
            System.err.println("Error inesperado de E/S al cargar RespuestaEncuesta: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    public void borrarRespuestaEncuesta (String emailCreador, String titulo, String emailRespuesta) throws FicheroNoExiste {
        String nombreFichero = emailRespuesta + ".res";
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + normalizarNombre(titulo) + "/respuestas";

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            throw new FicheroNoExiste("No se encontró el fichero de respuesta para borrar: " + nombreFichero);
        } catch (IOException e) {
            System.err.println("Error inesperado de E/S al borrar respuesta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public HashSet<RespuestaEncuesta> cargarRespuestasDeEncuesta (String emailCreador, String titulo) {
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + normalizarNombre(titulo) + "/respuestas";

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        HashSet<RespuestaEncuesta> respuestas = new HashSet<>();

        try (Stream<Path> walk = Files.walk(dir)) {
            // Busca ficheros .res
            Set<Path> resFiles = walk.filter(Files::isRegularFile).filter(p->p.toString().endsWith(".res")).collect(Collectors.toSet());
            // Deserializa y añade
            for (Path path: resFiles) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                    Object obj = ois.readObject();
                    if (obj == null) {
                        throw new LecturaNula("El fichero de RespuestaEncuesta está vacío.");
                    }
                    if (!(obj instanceof RespuestaEncuesta)) {
                        throw new FormatoInvalido("El fichero no contiene una instancia de RespuestaEncuesta válida.");
                    }
                    respuestas.add((RespuestaEncuesta) obj);
                } catch (Exception e) {
                    System.err.println("Error inesperado: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (NoSuchFileException e) {
            System.err.println("Path especificado no existe: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error inesperado de E/S al cargar respuestas de encuesta: " + e.getMessage());
            e.printStackTrace();
        }
        return respuestas;
    }
}
