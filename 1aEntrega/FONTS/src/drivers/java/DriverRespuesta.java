/*
import main.domain.*;
import main.persistence.exceptions.FicheroNoExiste;

import java.util.Scanner;
*/

// ES UN BORRADOR PROVISIONAL, COMENTO PARA QUE PUEDA COMPILAR

/*
public class DriverRespuesta {
    private static CtrlDominio cd = new CtrlDominio();
    private static Scanner sc = new Scanner(System.in);

    public static void main (String[] args) {

        boolean fin = false;

        while (!fin) {
            System.out.println();
            System.out.println("=== Driver Respuesta ===");
            System.out.println("Eliga una opcion:");
            System.out.println("1 (Crear Respuesta)");
            System.out.println("2 (Guardar Respuesta)");
            System.out.println("3 (Cargar Respuesta)");
            System.out.println("4 (Responder Encuesta)");
            System.out.println("5 (Salir)");
            System.out.println();
            int seleccion = sc.nextInt();
            sc.nextLine();
            switch (seleccion) {
                case 1:
                    crearRespuesta();
                    break;
                case 2:
                    guardarRespuesta();
                    break;
                case 3:
                    cargarRespuesta();
                    break;
                case 4:
                    responderEncuesta();
                    break;
                case 5:
                    fin = true;
                    break;
                default:
                    System.out.println("Seleccion no valida.");
            }
        }
    }

    private static boolean iniciarSesion() throws FicheroNoExiste {
        System.out.println("Ingrese su email: ");
        String email = sc.nextLine();
        try {
            cd.importarPerfil(email);
            System.out.println("Para este driver, no se pide contrasena.");
            return true;
        } catch (FicheroNoExiste e) {
            System.out.println("Su perfil no esta registrado, desea crear uno?");
            System.out.println("1 (Si)");
            System.out.println("<Cualquier otro numero> (No, y salir)");
            int seleccion = sc.nextInt();
            sc.nextLine();
            if (seleccion == 1) {
                return crearSesion(email);
            }
            else return false;
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error inesperado: " + e.getMessage());
            System.out.println("Desea reintentar?");
            System.out.println("1 (Si)");
            System.out.println("<Cualquier otro numero> (No)");
            int seleccion = sc.nextInt();
            sc.nextLine();
            if (seleccion == 1) return iniciarSesion();
            else return false;
        }
    }

    private static boolean crearSesion(String email) {
        System.out.println("Ingrese su nombre/alias: ");
        String nombre = sc.nextLine();
        System.out.println("Ingrese su contrasena: ");
        String contrasena = sc.nextLine();
        try {
            cd.crearPerfil(email, nombre, contrasena);
            System.out.println("Perfil creado exitosamente.");
            return true;
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error inesperado al crear el perfil: " + e.getMessage());
            System.out.println("Desea reintentar?");
            System.out.println("1 (Si)");
            System.out.println("<Cualquier otro numero> (No)");
            int seleccion = sc.nextInt();
            sc.nextLine();
            if (seleccion == 1) return crearSesion(email);
            else return false;
        }
    }

    private static void crearRespuesta() {
        System.out.println("Ingrese el titulo de la encuesta: ");
        String titulo = sc.nextLine();
        System.out.println("Ingrese el creador de la encuesta: ");
        String creador = sc.nextLine();
        try {
            Encuesta encuesta = cd.importarEncuesta(creador, titulo);
            cd.crearRespuesta(encuesta);
            System.out.println("Respuesta creada exitosamente.");
        } catch (FicheroNoExiste e) {
            System.out.println("No se encontro la encuesta especificada: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error inesperado al crear la respuesta: " + e.getMessage());
        }
    }
}
*/