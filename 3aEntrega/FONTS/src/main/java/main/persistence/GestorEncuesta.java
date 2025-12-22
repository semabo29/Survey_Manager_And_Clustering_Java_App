package main.persistence;

import main.persistence.exceptions.FalloPersistencia;
import main.persistence.exceptions.FicheroNoExiste;
import main.persistence.exceptions.LecturaNula;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Gestor para la persistencia de encuestas.
 * Usa persistencia de ficheros para guardar, cargar y borrar encuestas serializadas.
 * También proporciona métodos de consulta arbitraria.
 * @author Hadeer Abbas Khalil Wysocka
 * @author Sergi Malaguilla Bombín
 */
public class GestorEncuesta {
    /**
     * Constructor por defecto de la clase GestorEncuesta.
     */
    public GestorEncuesta() {}

    //El formato del fichero guarda una encuesta completa (con preguntas y tipos).enc

    /**
     * Guarda una encuesta y sus preguntas en un fichero .enc
     * @param emailCreador Email del creador/autor de la encuesta (parte de su clave).
     * @param titulo Título de la encuesta (parte de su clave).
     * @param datos Datos de la encuesta en un flujo de bytes.
     * @throws FalloPersistencia Si ocurre un error al guardar la encuesta.
     */
    public void guardarEncuesta(String emailCreador, String titulo, ByteArrayOutputStream datos) throws FalloPersistencia {
        String nombreFichero = titulo + ".enc";
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + titulo;

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        CtrlPersistencia.getInstance().escribirClaseSerializada(dir, path, datos);
    }

    //Cargar encuesta desde fichero .enc y devuelve la instancia de Encuesta

    /**
     * Carga una encuesta y sus preguntas desde un fichero .enc
     * @param emailCreador Email del creador/autor de la encuesta (parte de su clave).
     * @param titulo Título de la encuesta (parte de su clave).
     * @return Flujo de bytes con los datos de la encuesta.
     * @throws LecturaNula Si el fichero no existe o está vacío.
     * @throws FalloPersistencia Si ocurre un error al cargar la encuesta.
     */
    public ByteArrayInputStream cargarEncuesta(String emailCreador, String titulo) throws LecturaNula, FalloPersistencia {
        //Logica de bajo nivel para leer el fichero en java
        String nombreFichero = titulo + ".enc";
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + titulo;

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        return CtrlPersistencia.getInstance().leerClaseSerializada(path);
    }

    /**
     * Borra la carpeta conteniendo el fichero de una encuesta y las respuestas.
     * @param emailCreador Email del creador/autor de la encuesta (parte de su clave).
     * @param titulo Título de la encuesta (parte de su clave).
     * @throws FicheroNoExiste Si el directorio de la encuesta no existe.
     * @throws FalloPersistencia Si ocurre un error al borrar.
     */
    public void borrarEncuesta(String emailCreador, String titulo) throws FicheroNoExiste, FalloPersistencia {
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + titulo;

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);

        if (Files.exists(dir)) {
            // Borrar recursivamente...
            try (Stream<Path> paths = Files.walk(dir)) {
                // Ordenar en orden inverso para borrar primero los ficheros y luego los directorios
                paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e2) {
                        throw new FalloPersistencia("Error inesperado al borrar la encuesta y sus respuestas: " + e2.getMessage());
                    }
                });
            } catch (IOException e1) {
                throw new FalloPersistencia("Error inesperado al borrar la encuesta y sus respuestas: " + e1.getMessage());
            }
        }
        else {
            throw new FicheroNoExiste("No existe el directorio de la encuesta: " + ubicacion);
        }
    }

    // Key = titulo, Value = autor/creador. Por conveniencia, los directorios tambien "son" ficheros en el contexto del proyecto
    /**
     * Consulta arbitraria que devuelve una lista de pares (título, autor) de todas las encuestas.
     * @return Lista de pares (título, autor) de todas las encuestas en un ArrayList de SimpleEntry.
     * @throws FicheroNoExiste Si no existe el directorio base de encuestas.
     * @throws FalloPersistencia Si ocurre un error inesperado al acceder al sistema de ficheros.
     */
    public ArrayList<SimpleEntry<String,String>> getTituloAutorAllEncuestas () throws FicheroNoExiste, FalloPersistencia {
        String ubicacion = "ficheros/creadores";

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);

        ArrayList<SimpleEntry<String,String>> lista = new ArrayList<>();

        if (Files.exists(dir) && Files.isDirectory(dir)) {
            // Iterar sobre autores
            try (DirectoryStream<Path> creadores = Files.newDirectoryStream(dir)) {
                for (Path creador : creadores) {
                    if (Files.isDirectory(creador)) {
                        String autor = creador.getFileName().toString();
                        // Iterar sobre titulos
                        try (DirectoryStream<Path> titulos = Files.newDirectoryStream(creador)) {
                            for (Path tit : titulos) {
                                if (Files.isDirectory(tit)) {
                                    String titulo = tit.getFileName().toString();
                                    Path ficheroEnc = tit.resolve(titulo + ".enc");
                                    if (Files.exists(ficheroEnc)) {
                                        // Se muestra si existe el fichero .enc
                                        lista.add(new SimpleEntry<>(titulo, autor));
                                    }
                                }
                            }
                        } catch (IOException e2) {
                            throw new FalloPersistencia("Error inesperado en getTituloAutorAllEncuestas: " + e2.getMessage());
                        }
                    }
                }
            } catch (IOException e1) {
                throw new FalloPersistencia("Error inesperado en getTituloAutorAllEncuestas: " + e1.getMessage());
            }
        }

        return lista;
    }
}