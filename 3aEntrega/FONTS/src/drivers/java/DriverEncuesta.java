import main.domain.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class DriverEncuesta {
    private static CtrlEncuesta ce = new CtrlEncuesta();
    private static Scanner sc = new Scanner(System.in);

    // Para colorear los mensajes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private static String rojo(String s) {
        return ANSI_RED + s + ANSI_RESET;
    }

    private static String verde(String s) {
        return ANSI_GREEN + s + ANSI_RESET;
    }

    private static String cyan(String s) {
        return  ANSI_CYAN + s + ANSI_RESET;
    }

    // Lee una línea no vacía desde la entrada
    private static String readNonEmptyLine() {
        String line = sc.nextLine();
        while (line == null || line.trim().isEmpty()) {
            line = sc.nextLine();
        }
        return line;
    }

    public static void main(String[] args) {
        boolean fin = false;
        while (!fin) {
            System.out.println();
            System.out.println("¿Que quieres hacer?");
            System.out.println("1 - Crear una nueva encuesta");
            System.out.println("2 - Modificar una encuesta existente");
            System.out.println("3 - Consultar una encuesta existente");
            System.out.println("4 - Salir");
            System.out.println();
            while (!sc.hasNextInt()) {
                sc.next(); // Consumir la entrada inválida
            }
            int seleccion = sc.nextInt();
            sc.nextLine();
            switch (seleccion) {
                case 1:
                    crearNuevaEncuesta();
                    break;
                case 2:
                    modificarUnaEncuesta();
                    break;
                case 3:
                    consultarEncuesta();
                    break;
                case 4:
                    fin = true;
                    break;
                default:
                    System.out.println(rojo("Opcion no valida"));
                    System.out.println();
            }
        }
    }

    // Desde el main

    private static void crearNuevaEncuesta() {
        System.out.println("Introduzca el titulo del encuesta: ");
        String titulo = readNonEmptyLine();
        System.out.println("Introduzca el email del creador: ");
        String email = readNonEmptyLine();
        ce.crearEncuesta(titulo, email);
        modificarEncuestaActual();
    }

    private static void modificarUnaEncuesta() {
        System.out.println("Introduzca el email del creador: ");
        String email = readNonEmptyLine();
        System.out.println("Introduzca el titulo de la encuesta: ");
        String titulo = readNonEmptyLine();
        try {
            ce.cargarEncuesta(email, titulo);
            modificarEncuestaActual();
        } catch (Exception e) {
            exceptionHandler(e);
        }
    }

    private static void consultarEncuesta() {
        System.out.println("Introduzca el email del creador: ");
        String email = readNonEmptyLine();
        System.out.println("Introduzca el titulo de la encuesta: ");
        String titulo = readNonEmptyLine();
        try {
            ce.cargarEncuesta(email, titulo);
            printEncuesta(ce.getEncuesta());
        } catch (Exception e) {
            exceptionHandler(e);
        }
    }

    // Desde las funciones

    private static void modificarEncuestaActual() {
        boolean fin = false;
        while (!fin) {
            System.out.println();
            System.out.println(cyan("Encuesta: " + ce.getEncuesta().getTitulo()));
            System.out.println(cyan("Autor: " + ce.getEncuesta().getCreador()));
            System.out.println("¿Que quieres hacer con la encuesta?");
            System.out.println("1 - Añadir nueva pregunta");
            System.out.println("2 - Modificar una pregunta");
            System.out.println("3 - Guardar la encuesta");
            System.out.println("4 - Eliminar encuesta");
            System.out.println("5 - Finalizar");
            System.out.println();
            while (!sc.hasNextInt()) {
                sc.next(); // Consumir la entrada inválida
            }
            int selection = sc.nextInt();
            sc.nextLine();
            switch (selection) {
                case 1:
                    addPreguntaNueva();
                    break;
                case 2:
                    modificarPreguntasActual();
                    break;
                case 3:
                    try {
                        ce.guardarEncuesta();
                        System.out.println(verde("Encuesta guardada correctamente"));
                        System.out.println();
                    } catch (Exception e) {
                        exceptionHandler(e);
                    }
                    break;
                case 4:
                    try {
                        ce.borrarEncuestaActual();
                        System.out.println(verde("Encuesta borrada correctamente"));
                        System.out.println();
                    } catch (Exception e) {
                        exceptionHandler(e);
                    }
                    break;
                case 5:
                    ce.deseleccionarEncuesta();
                    fin = true;
                    break;
                default:
                    System.out.println(rojo("Opcion no valida"));
                    System.out.println();
            }
        }
    }

    private static void modificarPreguntasActual() {
        Encuesta encuesta = ce.getEncuesta();
        Set<Pregunta> aux = encuesta.getPreguntas();
        if (aux.isEmpty()) {
            System.out.println(rojo("La encuesta no tiene preguntas"));
            System.out.println();
        } else {
            HashMap<Integer, Pregunta> preguntas = new HashMap<>();
            for (Pregunta p : aux) {
                preguntas.put(p.getId(), p);
            }
            System.out.println("¿Que pregunta quieres modificar?");
            for (int i = 1; i <= preguntas.size(); i++) {
                Pregunta p = preguntas.get(i);
                System.out.println(i + " - " + p.getTexto());
            }
            System.out.println();
            while (!sc.hasNextInt()) {
                sc.next(); // Consumir la entrada inválida
            }
            int idSelect = sc.nextInt();
            sc.nextLine();
            if (preguntas.containsKey(idSelect)) {
                ce.seleccionarPreguntaPorId(idSelect);
                System.out.println();
                printPregunta(preguntas.get(idSelect));
                System.out.println("¿Que quieres modificar?");
                System.out.println("1 - Titulo de la pregunta");
                System.out.println("2 - Obligatoriedad de la pregunta");
                System.out.println("3 - Tipo de la pregunta");
                System.out.println("4 - Añadir descripcion");
                System.out.println("5 - Eliminar pregunta");
                System.out.println("6 - Finalizar");
                System.out.println();

                while (!sc.hasNextInt()) {
                    sc.next(); // Consumir la entrada inválida
                }
                int opcion = sc.nextInt();
                sc.nextLine();
                switch (opcion) {
                    case 1:
                        System.out.println("Introduzca el nuevo titulo:");
                        String titulo = readNonEmptyLine();
                        try {
                            ce.modificarPreguntaTextoSeleccionada(titulo);
                            System.out.println(verde("Titulo  modificado correctamente"));
                            System.out.println();
                        } catch (Exception e) {
                            exceptionHandler(e);
                        }
                        break;
                    case 2:
                        System.out.println("¿Quieres que sea obligatoria? (Si) (No)");
                        String ob = readNonEmptyLine();
                        try {
                            if (ob.equalsIgnoreCase("si")) {
                                ce.setObligatorioPreguntaSeleccionada(true);
                                System.out.println(verde("Obligatoriedad modificada correctamente"));
                                System.out.println();
                            } else if (ob.equalsIgnoreCase("no")) {
                                ce.setObligatorioPreguntaSeleccionada(false);
                                System.out.println(verde("Obligatoriedad modificada correctamente"));
                                System.out.println();
                            } else {
                                System.out.println(rojo("Opcion no valida"));
                            }
                        } catch (Exception e) {
                            exceptionHandler(e);
                        }
                        break;
                    case 3:
                        modificarTipoPreguntaSeleccionada();
                        break;
                    case 4:
                        System.out.println("Introduzca la descripcion:");
                        String desc = readNonEmptyLine();
                        try {
                            ce.setDescripcionPreguntaSeleccionada(desc);
                            System.out.println(verde("Descripcion añadida correctamente"));
                            System.out.println();
                        } catch (Exception e) {
                            exceptionHandler(e);
                        }
                        break;
                    case 5:
                        try {
                            ce.removePreguntaActual();
                            System.out.println(verde("Pregunta eliminada correctamente"));
                            System.out.println();
                        } catch (Exception e) {
                            exceptionHandler(e);
                        }
                        break;
                    case 6:
                        break;
                    default:
                        System.out.println(rojo("Opcion no valida"));
                }
            } else {
                System.out.println(rojo("Pregunta no existe"));
                System.out.println();
            }
        }
    }

    private static void modificarTipoPreguntaSeleccionada() {
        TipoPregunta tipo = seleccionarTipoPregunta();
        try {
            ce.modificarPreguntaTipoSeleccionada(tipo);
            System.out.println(verde("Tipo modificado correctamente"));
            System.out.println();
        } catch (Exception e) {
            exceptionHandler(e);
        }
    }

    private static void addPreguntaNueva() {
        System.out.println("Introduzca el titulo de la pregunta:");
        String titulo = readNonEmptyLine();
        System.out.println("¿Es obligatoria? (Si) (No)");
        String ob = readNonEmptyLine();
        while (!ob.equalsIgnoreCase("si") && !ob.equalsIgnoreCase("no")) {
            System.out.println(rojo("Opcion no valida, vuelva a introducir la opcion"));
            ob = readNonEmptyLine();
        }
        boolean obligatorio = ob.equalsIgnoreCase("si");
        TipoPregunta tipoPregunta = seleccionarTipoPregunta();
        Encuesta enc = ce.getEncuesta();
        int id = enc.getPreguntas().size() + 1;
        try {
            ce.addPregunta(id, titulo, obligatorio, tipoPregunta);
        } catch (Exception e) {
            exceptionHandler(e);
        }
        System.out.println(verde("Pregunta creada correctamente"));
        try {
            ce.guardarEncuesta();
            System.out.println(verde("Encuesta guardada correctamente"));
            System.out.println();
        } catch (Exception e) {
            exceptionHandler(e);
        }
        System.out.println();
    }

    private static TipoPregunta seleccionarTipoPregunta() {
        boolean finalizar = false;
        TipoPregunta tipoPregunta = null;
        while (!finalizar) {
            System.out.println();
            System.out.println("¿Que tipo de pregunta es?");
            System.out.println("1 - Con diferentes opciones");
            System.out.println("2 - De respuesta numerica");
            System.out.println("3 - Formato libre");
            System.out.println();

            while (!sc.hasNextInt()) {
                sc.next(); // Consumir la entrada inválida
            }
            int selection = sc.nextInt();
            sc.nextLine();
            switch (selection) {
                case 1:
                    tipoPregunta = crearConOpciones();
                    finalizar = true;
                    break;
                case 2:
                    tipoPregunta = crearNumerica();
                    finalizar = true;
                    break;
                case 3:
                    tipoPregunta = crearFormatoLibre();
                    finalizar = true;
                    break;
                default:
                    System.out.println(rojo("Opcion no valida"));
                    System.out.println();
            }
        }
        return tipoPregunta;
    }

    private static TipoPregunta crearConOpciones() {
        /*
        System.out.println("¿Cuantas opciones puede seleccionar el usuario como maximo?");
        System.out.println("(Responda con un numero)");
        while (!sc.hasNextInt()) {
            sc.next(); // Consumir la entrada inválida
        }
        int maxOpciones = sc.nextInt();
        sc.nextLine();
        while (maxOpciones < 1) {
            System.out.println(rojo("Responda con un numero > 0"));
            while (!sc.hasNextInt()) {
                sc.next(); // Consumir la entrada inválida
            }
            maxOpciones = sc.nextInt();
            sc.nextLine();
        }

         */
        ConOpciones tipoPregunta = null;
        try {
            tipoPregunta = new ConOpciones();
            boolean fin = false;
            while (!fin) {
                System.out.println();
                System.out.println("¿Que quieres hacer a continuacion?");
                System.out.println("1 - Añadir una opcion nueva");
                System.out.println("2 - Eliminar una opcion");
                System.out.println("3 - Finalizar");
                System.out.println();

                while (!sc.hasNextInt()) {
                    sc.next(); // Consumir la entrada inválida
                }
                int selection = sc.nextInt();
                sc.nextLine();
                switch (selection) {
                    case 1:
                        int id = tipoPregunta.getIdNextOpcion();
                        System.out.println("Introduzca el texto de esta opcion:");
                        String texto = readNonEmptyLine();
                        Opcion opcion = new Opcion(id, texto);
                        tipoPregunta.addOpcion(opcion);
                        System.out.println(verde("Opcion creada correctamente"));
                        System.out.println();
                        break;
                    case 2:
                        System.out.println();
                        System.out.println("¿Que opcion quieres eliminar?");
                        for (Opcion o : tipoPregunta.getOpciones()) {
                            System.out.println(o.getId() + " - " + o.getTexto());
                        }
                        while (!sc.hasNextInt()) {
                            sc.next(); // Consumir la entrada inválida
                        }
                        int idSelect = sc.nextInt();
                        sc.nextLine();
                        if (idSelect < 1 || idSelect >= tipoPregunta.getIdNextOpcion()) {
                            System.out.println(rojo("Opcion no valida, vuelva a intentarlo"));
                            System.out.println();
                        } else {
                            tipoPregunta.removeOpcion(tipoPregunta.getOpciones().get(idSelect - 1));
                            System.out.println(verde("Opcion eliminada correctamente"));
                            System.out.println();
                        }
                        break;
                    case 3:
                        fin = true;
                        break;
                    default:
                        System.out.println(rojo("Opcion no valida"));
                        System.out.println();
                }
            }
        } catch (Exception e) {
            exceptionHandler(e);
        }
        return tipoPregunta;
    }

    private static TipoPregunta crearNumerica() {
        System.out.println("Introduzca el valor minimo y maximo:");
        while (!sc.hasNextInt()) {
            sc.next(); // Consumir la entrada inválida
        }
        int min = sc.nextInt();
        while (!sc.hasNextInt()) {
            sc.next(); // Consumir la entrada inválida
        }
        int max = sc.nextInt();
        sc.nextLine();
        while (max <= min) {
            System.out.println(rojo("El valor maximo no puede ser mas pequeño o igual que el minimo, vuelva a intentarlo"));
            System.out.println("Introduzca el valor minimo y maximo:");
            while (!sc.hasNextInt()) {
                sc.next(); // Consumir la entrada inválida
            }
            min = sc.nextInt();
            while (!sc.hasNextInt()) {
                sc.next(); // Consumir la entrada inválida
            }
            max = sc.nextInt();
            sc.nextLine();
        }
        return new Numerica(min, max);
    }

    private static TipoPregunta crearFormatoLibre() {
        return new FormatoLibre();
    }

    private static void exceptionHandler(Exception e) {
        System.out.println(rojo("Error: " + e.getMessage()));
    }

    private static void printPregunta(Pregunta p) {
        System.out.println(cyan("Pregunta " + p.getId() + ": " + p.getTexto()));
        TipoPregunta t = p.getTipoPregunta();
        if (t instanceof ConOpciones) {
            ConOpciones conOpciones = (ConOpciones) t;
            for (Opcion opcion : conOpciones.getOpciones()) {
                System.out.println(cyan(opcion.getId() + " - " + opcion.getTexto()));
            }
        } else if (t instanceof Numerica) {
            Numerica numerica = (Numerica) t;
            System.out.println(cyan("Min: " + numerica.getMin()));
            System.out.println(cyan("Max: " + numerica.getMax()));
        }
    }

    private static void printEncuesta(Encuesta e) {
        System.out.println();
        System.out.println(cyan("Titulo: " + e.getTitulo()));
        System.out.println();
        HashMap<Integer, Pregunta> preguntas = new HashMap<>();
        for (Pregunta p : e.getPreguntas()) {
            preguntas.put(p.getId(), p);
        }
        for (int i = 1; i <= preguntas.size(); ++i) {
            printPregunta(preguntas.get(i));
            System.out.println();
        }
        System.out.println();
    }
}
