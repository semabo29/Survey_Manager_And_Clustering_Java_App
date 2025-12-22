package main.presentation;

/**
 * Interfaz del presentador para paneles de perfil.
 * Define los métodos que son wrappers a las llamadas a dominio que necesitan realizar los paneles que usen el presentador.
 * Los métodos wrappers se implementan en el CtrlPresentacion, que tiene acceso al CtrlDominio.
 */
public interface PresenterPerfil {
    /**
     * Cargar el perfil de un usuario dado su email y contraseña.
     * @param email Email del perfil.
     * @param password Contraseña del perfil.
     */
    public void cargarPerfil(String email, String password);

    /**
     * Crear un nuevo perfil de usuario dado un email, nombre y contraseña.
     * @param email Email del perfil.
     * @param nombre Nombre del perfil.
     * @param contrasena Contraseña del perfil.
     */
    public void crearPerfil(String email, String nombre, String contrasena);

    /**
     * Obtener el nombre del perfil cargado actualmente en memoria.
     * @return Nombre del perfil cargado.
     */
    public String getNombrePerfilCargado();

    /**
     * Obtener el email del perfil cargado actualmente en memoria.
     * @return Email del perfil cargado.
     */
    public String getEmailPerfilCargado();

    /**
     * Deseleccionar el perfil cargado actualmente en memoria.
     */
    public void deseleccionarPerfil();

    /**
     * Borrar el fichero associado al perfil cargado actualmente en memoria y deseleccionarlo.
     */
    public void borrarPerfilCargado();

    /**
     * Mostrar el menú principal de la aplicación una vez ya hecha el registro o inicio de sesión en PanelPerfil.
     */
    public void mostrarMenuPrincipal();

    /**
     * Copiar un fichero de perfil dado su path a la estructura de persistencia de la aplicación.
     * Para cargarlo en memoria, el usuario deberá iniciar sesión posteriormente escribiendo su correo y contraseña.
     * @param path Path del fichero del perfil a importar.
     */
    public void importarPerfilPorPath(String path);
}
