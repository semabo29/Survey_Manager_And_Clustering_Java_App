package main.persistence;

import main.persistence.exceptions.FalloPersistencia;
import main.persistence.exceptions.FicheroNoExiste;
import main.persistence.exceptions.LecturaNula;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Gestor para la persistencia de respuestas a encuestas.
 * Usa persistencia de ficheros para guardar, cargar y borrar respuestas serializadas.
 * También proporciona métodos de consulta arbitraria.
 * @author Hadeer Abbas Khalil Wysocka
 */
public class GestorRespuestaEncuesta {
    /**
     * Constructor por defecto de la clase GestorRespuestaEncuesta.
     */
    public GestorRespuestaEncuesta() {}

    /**
     * Guarda la respuesta a una encuesta en un fichero .res
     * @param emailCreador Email del creador/autor de la encuesta (parte de su clave).
     * @param titulo Título de la encuesta (parte de su clave).
     * @param emailRespuesta Email del usuario que responde la encuesta (parte de su clave).
     * @param datos Datos de la respuesta en un flujo de bytes.
     * @throws FalloPersistencia Si ocurre un error al guardar la respuesta.
     */
    public void guardarRespuestaEncuesta (String emailCreador, String titulo, String emailRespuesta, ByteArrayOutputStream datos) throws FalloPersistencia {
        // Se guardan los datos en el fichero especial

        String nombreFichero = emailRespuesta + ".res";
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + titulo + "/respuestas";

        // Path que hemos de poner segun la estructura de como se guardan los ficheros establecida
        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        CtrlPersistencia.getInstance().escribirClaseSerializada(dir, path, datos);
    }

    /**
     * Carga la respuesta a una encuesta desde un fichero .res
     * @param emailCreador Email del creador/autor de la encuesta (parte de su clave).
     * @param titulo Título de la encuesta (parte de su clave).
     * @param emailRespuesta Email del usuario que responde la encuesta (parte de su clave).
     * @return Flujo de bytes con los datos de la respuesta.
     * @throws LecturaNula Si el fichero no existe o está vacío.
     * @throws FalloPersistencia Si ocurre un error al cargar la respuesta.
     */
    public ByteArrayInputStream cargarRespuestaEncuesta (String emailCreador, String titulo, String emailRespuesta) throws LecturaNula, FalloPersistencia {
        // Se cargan los datos desde el fichero especial
        // No hace falta comprobar que los Strings son validos ya que si no lo es por fuerza no existira el fichero
        String nombreFichero = emailRespuesta + ".res";
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + titulo + "/respuestas";

        // Path que hemos de poner segun la estructura de como se guardan los ficheros establecida
        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        return CtrlPersistencia.getInstance().leerClaseSerializada(path);
    }

    /**
     * Borra el fichero de respuesta a una encuesta.
     * @param emailCreador Email del creador/autor de la encuesta (parte de su clave).
     * @param titulo Título de la encuesta (parte de su clave).
     * @param emailRespuesta Email del usuario que responde la encuesta (parte de su clave).
     * @throws FicheroNoExiste Si el fichero de respuesta no existe.
     * @throws FalloPersistencia Si ocurre un error al borrar la respuesta.
     */
    public void borrarRespuestaEncuesta (String emailCreador, String titulo, String emailRespuesta) throws FicheroNoExiste, FalloPersistencia {
        String nombreFichero = emailRespuesta + ".res";
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + titulo + "/respuestas";

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        Path path = dir.resolve(nombreFichero);

        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            throw new FicheroNoExiste("No se encontró el fichero de respuesta para borrar: " + nombreFichero);
        } catch (IOException e) {
            throw new FalloPersistencia("Error inesperado al borrar respuesta: " + e.getMessage());
        }
    }

    /**
     * Carga todas las respuestas de una encuesta específica.
     * @param emailCreador Email del creador/autor de la encuesta (parte de la clave de Encuesta).
     * @param titulo Título de la encuesta (parte de la clave de Encuesta).
     * @return Flujos de bytes con los datos de las respuestas en un HashSet.
     * @throws LecturaNula Si no hay respuestas para esta encuesta o la encuesta no está creada.
     * @throws FalloPersistencia Si ocurre un error inesperado al cargar las respuestas de la encuesta.
     */
    public HashSet<ByteArrayInputStream> cargarRespuestasDeEncuesta (String emailCreador, String titulo) throws LecturaNula, FalloPersistencia {
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + titulo + "/respuestas";

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        HashSet<ByteArrayInputStream> respuestas = new HashSet<>();

        try (Stream<Path> walk = Files.walk(dir)) {
            // Busca ficheros .res
            Set<Path> resFiles = walk.filter(Files::isRegularFile).filter(p->p.toString().endsWith(".res")).collect(Collectors.toSet());
            // Añade
            for (Path path: resFiles) {
                ByteArrayInputStream resp = CtrlPersistencia.getInstance().leerClaseSerializada(path);
                respuestas.add(resp);
            }
        } catch (NoSuchFileException e) {
            throw new LecturaNula("No hay respuestas para esta encuesta o la encuesta no esta creada");
        } catch (IOException e) {
            throw new FalloPersistencia("Error inesperado al cargar respuestas de encuesta: " + e.getMessage());
        }
        return respuestas;
    }

    /**
     * Consulta arbitraria que devuelve los emails de los usuarios que han respondido a una encuesta específica.
     * @param emailCreador Email del creador de la encuesta (parte de la clave de Encuesta).
     * @param titulo Título de la encuesta (parte de la clave de Encuesta).
     * @return Emails de los usuarios que han respondido a la encuesta.
     * @throws FalloPersistencia Si ocurre un error inesperado al conseguir los emails de las respuestas a la encuesta.
     */
    public HashSet<String> getEmailsRespuestasDeEncuesta (String emailCreador, String titulo) throws FalloPersistencia {
        String ubicacion = "ficheros/creadores/" + emailCreador + "/" + titulo + "/respuestas";

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        HashSet<String> emails = new HashSet<>();

        try (Stream<Path> walk = Files.walk(dir)) {
            Set<Path> resFiles = walk.filter(Files::isRegularFile).filter(p->p.toString().endsWith(".res")).collect(Collectors.toSet());
            for (Path path : resFiles) {
                String nombreFichero = path.getFileName().toString();
                int idx = nombreFichero.lastIndexOf('.');
                String email = nombreFichero.substring(0, idx);
                emails.add(email);
            }
        } catch (NoSuchFileException e) {
            // Solo deberia fallar aqui si no hay ninguna encuesta creada o no hay respuestas, en cuyo caso simplemente devolvemos el
            // conjunto que deberia estar vacio
            return emails;
        } catch (IOException e) {
            throw new FalloPersistencia("Error inesperado al conseguir emails de respuestas a la encuesta: " + e.getMessage());
        }

        return emails;
    }

    // Key = titulo, Value = creador

    /**
     * Consulta arbitraria que devuelve las respuestas realizadas por el perfil de un usuario específico.
     * @param email Email del perfil (clave de Perfil)
     * @return Pares (titulo de la encuesta y autor de la encuesta) de identificadores de encuestas respondidas por el usuario en un HashSet de SimpleEntry.
     */
    public HashSet<SimpleEntry<String,String>> getRespuestasByEmail (String email) {
        String ubicacion = "ficheros/creadores";
        String ficheroABuscar = email + ".res";

        Path cwd = Paths.get("").toAbsolutePath();
        Path dir = cwd.resolve(ubicacion);
        HashSet<SimpleEntry<String,String>> respuestasEmail = new HashSet<>();

        try (Stream<Path> walk = Files.walk(dir).skip(1)) {
            Set<Path> emailsCreadores = walk.filter(Files::isDirectory).collect(Collectors.toSet());;
            for (Path pathCreador : emailsCreadores) {
                try (Stream<Path> walk2 = Files.walk(pathCreador).skip(1)) {
                    Set<Path> titulos = walk2.filter(Files::isDirectory).collect(Collectors.toSet());
                    for (Path pathTitulo : titulos) {
                        String find = "respuestas/" + ficheroABuscar;
                        Path posibleRespuesta = pathTitulo.resolve(find);
                        if (Files.exists(posibleRespuesta) && Files.isRegularFile(posibleRespuesta) && Files.isReadable(posibleRespuesta)) {
                            // La respuesta a esta encuesta existe, anadir a respuestasEmail
                            String titulo = pathTitulo.getFileName().toString();
                            String creador = pathCreador.getFileName().toString();
                            respuestasEmail.add(new SimpleEntry<String,String>(titulo, creador));
                        }
                    }
                }
            }
        }
        catch (NoSuchFileException e) {
            // Solo deberia fallar aqui si no hay ninguna encuesta creada, en cuyo caso simplemente devolvemos el
            // conjunto que deberia estar vacio
            return respuestasEmail;
        }
        catch (IOException e) {
            throw new FalloPersistencia("Error inesperado al conseguir las respuestas de un email");
        }

        return respuestasEmail;
    }
}
