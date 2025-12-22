package main.domain;

import main.domain.exceptions.NoHayPerfilCargado;
import main.domain.exceptions.YaExistePerfil;
import main.persistence.CtrlPersistencia;
import main.persistence.exceptions.FalloPersistencia;
import main.persistence.exceptions.LecturaNula;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;

/**
 * Controlador de la capa de dominio encargado de gestionar los perfiles de usuario.
 * @author Hadeer Abbas Khalil Wysocka
 */

public class CtrlPerfil {

    /**
     * Perfil actualmente cargado en memoria.
     */
    private Perfil perfilCargado;

    /**
     * Constructor por defecto.
     */
    public CtrlPerfil () {
        this.perfilCargado = null;
    }

    /**
     * Getter del perfil cargado en memoria.
     * @return Perfil cargado en memoria.
     */
    public Perfil getPerfilCargado() { return this.perfilCargado; }

    /**
     * Crea un nuevo perfil, lo guarda en la persistencia y lo carga en memoria.
     * @param email Email del nuevo perfil.
     * @param nombre Nombre del nuevo perfil.
     * @param contrasena Contraseña del nuevo perfil.
     * @throws YaExistePerfil Si ya existe un perfil con el mismo email.
     */
    public void crearPerfil(String email, String nombre, String contrasena) throws YaExistePerfil {
        if (existePerfil(email)) {
            throw new YaExistePerfil("Ya existe un perfil con email: " + email);
        }
        this.perfilCargado = new Perfil(email, nombre, contrasena);
        guardarPerfil();
    }

    /**
     * Guarda el perfil cargado en memoria en la persistencia.
     * @throws NoHayPerfilCargado Si no hay ningun perfil cargado en memoria.
     */
    public void guardarPerfil() throws NoHayPerfilCargado {
        if (this.perfilCargado == null) throw new NoHayPerfilCargado("No hay ningun perfil cargado");
        try (ByteArrayOutputStream datos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(datos)) {
            oos.writeObject(perfilCargado);
            oos.flush();
            CtrlPersistencia.getInstance().guardarPerfil(perfilCargado.getEmail(), datos);
        }
        catch (IOException e) {
            throw new FalloPersistencia("Error guardando perfil: " + e.getMessage());
        }
    }

    /**
     * Carga un perfil de la persistencia en memoria.
     * @param email Email del perfil a cargar.
     * @throws LecturaNula Si no existe un perfil con el email especificado.
     */
    public void cargarPerfil(String email) {
        ByteArrayInputStream datos = CtrlPersistencia.getInstance().cargarPerfil(email);
        if (datos == null)
            throw new LecturaNula("No existe perfil con email: " + email);

        try (ObjectInputStream ois = new ObjectInputStream(datos)) {
            this.perfilCargado = (Perfil) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new FalloPersistencia("Error leyendo perfil serializado: " + e.getMessage());
        }
    }

    /**
     * Comprueba si existe un perfil con el email especificado en la persistencia.
     * @param email Email del perfil a comprobar.
     * @return true si existe el perfil, false en caso contrario.
     */
    public boolean existePerfil(String email) {
        try {
            // Implementacion trivial para evitar que se sobreescriba el perfil cargado cuando se ejecute existePerfil()
            Perfil aux = perfilCargado;
            cargarPerfil(email);
            boolean exists = perfilCargado != null;
            perfilCargado = aux;
            return exists;
        } catch (Exception e) {
            return false;
        }
    }

    // Borra el perfil cargado actualmente de la persistencia y lo desasigna
    /**
     * Borra el perfil cargado actualmente de la persistencia y lo desasigna.
     * @throws NoHayPerfilCargado Si no hay ningun perfil cargado en memoria.
     */
    public void borrarPerfilCargado() throws NoHayPerfilCargado {
        // Teoricamente esta excepcion nunca la deberia de llegar a tirar, siempre habra un perfil cargado en el programa completo
        if (this.perfilCargado == null) throw new NoHayPerfilCargado("No hay ningun perfil cargado");
        CtrlPersistencia.getInstance().borrarPerfil(this.perfilCargado.getEmail());
        this.perfilCargado = null;
    }

    // Cada vez que se crea/borra una encuesta/respuesta, hay que guardar el cambio en la persistencia

    /**
     * Añade un identificador a una encuesta creada al perfil cargado y guarda el cambio en la persistencia.
     * @param enc Encuesta creada a añadir.
     * @throws NoHayPerfilCargado Si no hay ningun perfil cargado en memoria.
     */
    public void addEncuestaCreada (Encuesta enc) throws NoHayPerfilCargado {
        if (this.perfilCargado == null) throw new NoHayPerfilCargado("No hay ningun perfil cargado");
        else {
            try {
                this.perfilCargado.addEncuestaCreada(enc);
                guardarPerfil();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.perfilCargado.removeEncuestaCreada(enc);
            }
        }
    }

    /**
     * Elimina un identificador de una encuesta creada del perfil cargado y guarda el cambio en la persistencia.
     * @param enc Encuesta creada a eliminar.
     * @throws NoHayPerfilCargado Si no hay ningun perfil cargado en memoria.
     */
    public void removeEncuestaCreada (Encuesta enc) throws NoHayPerfilCargado {
        if (this.perfilCargado == null) throw new NoHayPerfilCargado("No hay ningun perfil cargado");
        else {
            try {
                this.perfilCargado.removeEncuestaCreada(enc);
                guardarPerfil();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.perfilCargado.addEncuestaCreada(enc);
            }
        }
    }

    /**
     * Añade un identificador a una respuesta hecha al perfil cargado y guarda el cambio en la persistencia.
     * @param res Respuesta hecha a añadir.
     * @throws NoHayPerfilCargado Si no hay ningun perfil cargado en memoria.
     */
    public void addRespuestaHecha (RespuestaEncuesta res) throws NoHayPerfilCargado {
        if (this.perfilCargado == null) throw new NoHayPerfilCargado("No hay ningun perfil cargado");
        else {
            try {
                this.perfilCargado.addRespuestaHecha(res);
                guardarPerfil();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.perfilCargado.removeRespuestaHecha(res);
            }
        }
    }

    /**
     * Elimina un identificador de una respuesta hecha del perfil cargado y guarda el cambio en la persistencia.
     * @param res Respuesta hecha a eliminar.
     * @throws NoHayPerfilCargado Si no hay ningun perfil cargado en memoria.
     */
    public void removeRespuestaHecha (RespuestaEncuesta res) throws NoHayPerfilCargado {
        if (this.perfilCargado == null) throw new NoHayPerfilCargado("No hay ningun perfil cargado");
        else {
            try {
                this.perfilCargado.removeRespuestaHecha(res);
                guardarPerfil();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.perfilCargado.addRespuestaHecha(res);
            }
        }
    }

    /**
     * Deselecciona el perfil cargado en memoria.
     */
    public void deseleccionarPerfil() { this.perfilCargado = null; }

    // Key = titulo, Value = creador

    /**
     * Obtiene las respuestas hechas por el perfil cargado en formato [Titulo,Creador].
     * @return Conjunto de pares [Titulo,Creador] de las respuestas hechas por el perfil cargado.
     */
    public HashSet<SimpleEntry<String,String>> getRespuestasPerfilCargado () {
        return CtrlPersistencia.getInstance().getRespuestasByEmail(this.perfilCargado.getEmail());
    }

    // No carga el perfil en memoria, solo lo importa a la persistencia

    /**
     * Importa un perfil serializado desde la ruta especificada y lo guarda en la persistencia.
     * No la carga en memoria, el perfil cargado en memoria sigue siendo el mismo y para cambiarlo hay que iniciar sesión.
     * @param Spath Ruta del perfil serializado a importar.
     */
    public void cargarPerfilPorPath(String Spath) {
        Path path = Paths.get(Spath);
        ByteArrayInputStream datos = CtrlPersistencia.getInstance().leerClaseSerializada(path);
        if (datos == null)
            throw new NoHayPerfilCargado("No existe perfil en la ruta especificada");

        try (ObjectInputStream ois = new ObjectInputStream(datos)) {
            Perfil p = (Perfil) ois.readObject();
            try (ByteArrayOutputStream output = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(output)) {
                oos.writeObject(p);
                oos.flush();
                CtrlPersistencia.getInstance().guardarPerfil(p.getEmail(), output);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            throw new FalloPersistencia("Error importando perfil serializado: " + e.getMessage());
        }
    }
}
