package main.persistence;

import java.io.*;
import java.nio.file.*;

import main.persistence.exceptions.FalloPersistencia;
import main.persistence.exceptions.FicheroNoExiste;
import main.persistence.exceptions.LecturaNula;

/**
 * Gestor para la persistencia de perfiles de usuario.
 * Usa persistencia de ficheros para guardar, cargar y borrar perfiles serializados.
 * @author Hadeer Abbas Khalil Wysocka
 */
public class GestorPerfil {

    /**
     * Constructor por defecto de la clase GestorPerfil.
     */
    public GestorPerfil() {}

    /**
     * Guarda el perfil de un usuario en un fichero .perf.
     * @param email Email del usuario (su clave).
     * @param datos Datos del perfil en un flujo de bytes.
     * @throws FalloPersistencia Si ocurre un error al guardar el perfil.
     */
    public void guardarPerfil (String email, ByteArrayOutputStream datos) throws FalloPersistencia {
        // Se guardan los datos del perfil en el fichero especial

        String nombreFichero = email + ".perf";

        // Path que hemos de poner segun la estructura de como se guardan los ficheros establecida
        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve("ficheros/perfiles");
        Path path = dir.resolve(nombreFichero);

        CtrlPersistencia.getInstance().escribirClaseSerializada(dir, path, datos);
    }

    /**
     * Carga el perfil de un usuario desde un fichero .perf.
     * @param email Email del usuario (su clave).
     * @return Flujo de bytes con los datos del perfil.
     * @throws LecturaNula Si el fichero no existe o está vacío.
     * @throws FalloPersistencia Si ocurre un error al cargar el perfil.
     */
    public ByteArrayInputStream cargarPerfil (String email) throws LecturaNula, FalloPersistencia {
        String nombreFichero = email + ".perf";

        // Path que hemos de poner segun la estructura de como se guardan los ficheros establecida
        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve("ficheros/perfiles");
        Path path = dir.resolve(nombreFichero);

        return CtrlPersistencia.getInstance().leerClaseSerializada(path);
    }

    /**
     * Borra el fichero de perfil de un usuario.
     * @param email Email del usuario (su clave).
     * @throws FicheroNoExiste Si el fichero de perfil no existe.
     * @throws FalloPersistencia Si ocurre un error al borrar el perfil.
     */
    public void borrarPerfil (String email) throws FicheroNoExiste, FalloPersistencia {
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
            throw new FalloPersistencia("Error inesperado al borrar perfil: " + e.getMessage());
        }
    }
}
