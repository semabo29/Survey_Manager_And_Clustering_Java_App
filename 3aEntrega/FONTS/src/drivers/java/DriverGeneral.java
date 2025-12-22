import main.domain.*;
import main.domain.types.TDatosInteger;
import main.domain.types.TDatosOpciones;
import main.domain.types.TDatosString;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class DriverGeneral {

    private static final CtrlDominio cd = new CtrlDominio();
    private static final Scanner sc = new Scanner(System.in);

    //MENU PRINCIPAL
    public static void main(String[] args){
        boolean fin = false;

        while (!fin) {
            if(cd.getPerfilCargado() == null) {
                System.out.println("No hay perfil cargado. Por favor, cree o cargue un perfil primero.");
                menuPerfil();
                continue;
            }
            System.out.println("==== Driver General ====");
            System.out.println("---- Menu principal ----");
            System.out.println("Elige una opcion:");
            System.out.println("1 Consultar Perfil Actual");
            System.out.println("2 Consultar Encuesta Actual");
            System.out.println("3 Consultar Respuestas Cargadas");
            System.out.println("4 Gestion de Encuestas");
            System.out.println("5 Gestion de Perfiles");
            System.out.println("6 Gestion de Respuestas");
            System.out.println("7 Cargar Juego de Pruebas");
            System.out.println("8 Algoritmos de Análisis");
            System.out.println("9 Analizar Encuesta");
            System.out.println("10 Elegir K para análisis");
            System.out.println("11 Elegir Evaluador de Calidad");
            System.out.println("12 Salir");
            int seleccion = sc.nextInt();
            sc.nextLine();
            //int seleccion = Integer.parseInt(sc.nextLine());

            switch (seleccion) {
                case 1: consultarPerfilActual(); break;
                case 2: consultarEncuestaActual(); break;
                case 3: consultarEmailsRespuestas(); break;
                case 4: menuEncuesta(); break;
                case 5: menuPerfil(); break;
                case 6: menuRespuesta(); break;
                case 7:
                    System.out.println("Ruta del archivo de perfiles:");
                    String rutaPerfiles = sc.nextLine();
                    cargarPerfilesDesdeArchivo(rutaPerfiles);

                    System.out.println("Ruta del archivo del juego de pruebas:");
                    String rutaJuego = sc.nextLine();
                    cargarJuegoPruebas(rutaJuego);
                    break;
                case 8: menuAlgoritmo(); break;
                case 9: ejecutarAnalisis(); break;
                case 10: menuConfigurarK();break;
                case 11: menuConfigurarEvaluadorCalidad();break;
                case 12:
                    System.out.println("Saliendo del programa.");
                    fin = true; break;
                default: System.out.println("Seleccion no valida.");
            }
        }
    }
    private static void consultarPerfilActual() {
        if (cd.getPerfilCargado() == null) {
            System.out.println("No hay perfil cargado.");
            return;
        }

        Perfil p = cd.getPerfilCargado();
        System.out.println("\n=== PERFIL INICIADO ===");
        System.out.println("Email: " + p.getEmail());
        System.out.println("Nombre: " + p.getNombre());
    }
    private static void consultarEncuestaActual() {
        if (cd.getEncuestaEnMemoria() == null) {
            System.out.println("No hay encuesta cargada.");
            return;
        }

        Encuesta e = cd.getEncuestaEnMemoria();
        System.out.println("\n=== ENCUESTA ACTUAL ===");
        System.out.println("Titulo: " + e.getTitulo());
        System.out.println("Preguntas:");

        TreeMap<Integer, Pregunta> mapaPreguntas = new TreeMap<>();
        //0rdenar el hash set para mostrar en orden las preguntas segun su ID
        for (Pregunta p : e.getPreguntas()) {
            mapaPreguntas.put(p.getId(), p);
        }

        for (Pregunta p : mapaPreguntas.values()) {
            System.out.println("ID " + p.getId() + " | " + p.getTexto() +
                    " | Obligatoria: " + p.isObligatorio() +
                    " | Tipo: " + p.getTipoPregunta().getClass().getSimpleName());

            if (p.getTipoPregunta() instanceof ConOpciones co) {
                System.out.println("   Opciones:");
                for (Opcion o : co.getOpciones()) {
                    System.out.println("     " + o.getId() + ". " + o.getTexto());
                }
            }
        }
    }

    private static void consultarEmailsRespuestas() {
        ArrayList<RespuestaEncuesta> respuestas = new ArrayList<>(cd.getRespuestasCargadas());

        if (respuestas.isEmpty()) {
            System.out.println("No hay respuestas cargadas.");
            return;
        }

        System.out.println("\n=== RESPUESTAS CARGADAS ===");
        for (RespuestaEncuesta r : respuestas) {
            System.out.println(" - " + r.getEmailrespuesta());
        }
    }

    private static void vistaPreviaPregunta(Pregunta p) {
        if (p == null) {
            System.out.println("La pregunta no existe.");
            return;
        }

        System.out.println("\n=== VISTA PREVIA DE LA PREGUNTA ===");
        System.out.println("ID: " + p.getId());
        System.out.println("Texto: " + p.getTexto());
        System.out.println("Obligatoria: " + (p.isObligatorio() ? "Sí" : "No"));
        System.out.println("Tipo: " + p.getTipoPregunta().getClass().getSimpleName());

        // Específico según tipo
        TipoPregunta tipo = p.getTipoPregunta();

        if (tipo instanceof Numerica n) {
            System.out.println("Rango numérico: " + n.getMin() + " - " + n.getMax());
        }
        else if (tipo instanceof ConOpciones co) {
            System.out.println("Opciones:");
            for (Opcion o : co.getOpciones()) {
                System.out.println("  " + o.getId() + ". " + o.getTexto());
            }
        }

        System.out.println("===================================\n");
    }





    //ENCUESTAS
    private static void menuEncuesta() {
        boolean fin = false;
        while (!fin) {
            System.out.println("---- Menu encuesta ----");
            System.out.println("Elige una opcion:");
            System.out.println("1 Crear encuesta");
            System.out.println("2 Guardar encuesta actual");
            System.out.println("3 Borrar encuesta actual");
            System.out.println("4 Modificar encuesta actual");
            System.out.println("5 Cargar encuesta");
            System.out.println("6 Volver");
            int seleccion = sc.nextInt();
            sc.nextLine();
            //int seleccion = Integer.parseInt(sc.nextLine());
            switch (seleccion) {
                case 1:
                    System.out.println("Introduzca el titulo de la encuesta: ");
                    String titulo = sc.nextLine();
                    cd.crearEncuesta(titulo);
                    System.out.println("Encuesta "+titulo+" creada, cargada y guardada.");
                    break;
                case 2:
                    cd.guardarEncuesta();
                    System.out.println("Encuesta actual guardada.");
                    break;

                case 3:
                    cd.borrarEncuestaActual();
                    System.out.println("Encuesta actual borrada.");
                    break;
                case 4:
                    modificarEncuesta();
                    break;
                case 5:
                    System.out.println("Introduzca el email del creador de la encuesta: ");
                    String email = sc.nextLine();
                    System.out.println("Introduzca el titulo de la encuesta a cargar: ");
                    String tituloCargar = sc.nextLine();
                    cd.importarEncuesta(tituloCargar, email);
                    System.out.println("Encuesta "+tituloCargar+" cargada.");
                    break;
                case 6:
                    fin = true;
                    break;

                default:
                    System.out.println("Seleccion no valida.");
            }
        }
    }

    private static void cargarPerfilesDesdeArchivo(String ruta) {
        try {
            File f = new File(ruta);
            if (!f.exists()) {
                System.out.println("ERROR: No existe el archivo de perfiles.");
                return;
            }

            Scanner file = new Scanner(f);
            while (file.hasNextLine()) {
                String linea = file.nextLine().trim();
                if (linea.isEmpty()) continue;

                // Formato esperado: PERFIL,email,nombre,password
                if (linea.startsWith("PERFIL,")) {
                    String[] partes = linea.split(",");
                    String email = partes[1];
                    String nombre = partes[2];
                    String password = partes[3];

                    if (!cd.existePerfil(email)) {
                        cd.crearPerfil(email, nombre, password);
                        System.out.println("Perfil creado: " + email);
                    } else {
                        System.out.println("Perfil ya existe: " + email);
                        cd.importarPerfil(email);
                    }
                }
            }
            file.close();
            System.out.println("Carga de perfiles completada.");
        } catch (Exception e) {
            System.out.println("ERROR al cargar perfiles: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void cargarJuegoPruebas(String ruta) {
        try {
            File f = new File(ruta);
            if (!f.exists()) {
                System.out.println("ERROR: No existe el archivo de juego de pruebas.");
                return;
            }

            Scanner file = new Scanner(f);

            boolean leyendoPreguntas = false;
            boolean leyendoPreguntasOpciones = false;
            boolean leyendoRespuestas = false;

            while (file.hasNextLine()) {
                String linea = file.nextLine().trim();
                if (linea.isEmpty()) continue;

                // === ENCUESTA ===
                if (linea.startsWith("ENCUESTA,")) {
                    String[] partes = linea.split(",");
                    String titulo = partes[1];
                    String emailCreador = partes[2];
                    cd.importarPerfil(emailCreador);
                    cd.crearEncuesta(titulo);
                    continue;
                }

                // === CABECERAS ===
                switch (linea) {
                    case "PREGUNTAS" -> {
                        leyendoPreguntas = true;
                        leyendoPreguntasOpciones = false;
                        leyendoRespuestas = false;
                        continue;
                    }
                    case "PREGUNTAS_OPCIONES" -> {
                        leyendoPreguntas = false;
                        leyendoPreguntasOpciones = true;
                        leyendoRespuestas = false;
                        continue;
                    }
                    case "RESPUESTAS" -> {
                        leyendoPreguntas = false;
                        leyendoPreguntasOpciones = false;
                        leyendoRespuestas = true;
                        continue;
                    }
                }

                // === PREGUNTAS SIN OPCIONES ===
                if (leyendoPreguntas && linea.startsWith("PREGUNTA,")) {
                    String[] p = linea.split(",");
                    int id = Integer.parseInt(p[1]);
                    String texto = p[2];
                    boolean obligatorio = Boolean.parseBoolean(p[3]);
                    String tipo = p[4];

                    switch (tipo) {
                        case "FormatoLibre" -> cd.addPregunta(id, texto, obligatorio, new FormatoLibre());
                        case "Numerica" -> {
                            int min = Integer.parseInt(p[5]);
                            int max = Integer.parseInt(p[6]);
                            cd.addPregunta(id, texto, obligatorio, new Numerica(min, max));
                        }
                        default -> System.out.println("Tipo no reconocido: " + tipo);
                    }
                    continue;
                }

                // === PREGUNTAS CON OPCIONES ===
                if (leyendoPreguntasOpciones && linea.startsWith("PREGUNTA,")) {
                    String[] p = linea.split(",");
                    int id = Integer.parseInt(p[1]);
                    String texto = p[2];
                    boolean obligatorio = Boolean.parseBoolean(p[3]);


                    String[] opciones = p[5].split("\\.");
                    ConOpciones co = new ConOpciones(opciones.length);
                    for (int i = 0; i < opciones.length; i++) {
                        co.addOpcion(new Opcion(i + 1, opciones[i]));
                    }

                    cd.addPregunta(id, texto, obligatorio, co);
                    continue;
                }

                // === RESPUESTAS ===
                if (leyendoRespuestas && linea.startsWith("RESPUESTA,")) {
                    String[] r = linea.split(",", -1);
                    String email = r[1];
                    cd.importarPerfil(email);
                    cd.responderEncuesta();
                    cd.getRespuestaActual().setEmailrespuesta(email);

                    int numPreguntas = cd.getEncuestaEnMemoria().getPreguntas().size();
                    int index = 2; // empieza después de RESPUESTA,email

                    for (int i = 1; i <= numPreguntas; i++) {
                        Pregunta p = cd.getPreguntaById(i);

                        if (index >= r.length) {
                            System.out.println("ERROR: faltan respuestas para la encuesta");
                            break;
                        }

                        String val = r[index];
                        index++;

                        if (p.getTipoPregunta() instanceof FormatoLibre) {
                            if (val.isEmpty()) cd.addDatoRespuesta(i, new TDatosString());
                            cd.addDatoRespuesta(i, new TDatosString(val));
                        }
                        else if (p.getTipoPregunta() instanceof Numerica) {
                            if (val.isEmpty()) cd.addDatoRespuesta(i, new TDatosInteger());
                            else cd.addDatoRespuesta(i, new TDatosInteger(Integer.parseInt(val)));
                        }
                        else if (p.getTipoPregunta() instanceof ConOpciones) {
                            if (val.isEmpty()) cd.addDatoRespuesta(i, new TDatosOpciones());
                            else {
                                ArrayList<Integer> list = new ArrayList<>();
                                String[] seleccionadas = val.split("\\."); // varias opciones separadas por '.'
                                for (String s : seleccionadas) {
                                    if (!s.isEmpty()) list.add(Integer.parseInt(s));
                                }
                                cd.addDatoRespuesta(i, new TDatosOpciones(list));
                            }
                        }
                    }

                    cd.guardarRespuesta();
                }

            }
            cd.guardarEncuesta();

            file.close();
            System.out.println("Juego de pruebas cargado correctamente.");
        } catch (Exception e) {
            System.out.println("ERROR al cargar: " + e.getMessage());
            e.printStackTrace();
        }
    }





    private static void modificarEncuesta(){
        boolean fin = false;
        while (!fin) {
            System.out.println("---- Modificar encuesta ----");
            System.out.println("Elige una opcion:");
            System.out.println("1 Añadir pregunta");
            System.out.println("2 Eliminar pregunta por ID");
            System.out.println("3 Modificar pregunta");
            System.out.println("4 Volver");

            int seleccion = sc.nextInt();
            sc.nextLine();
            //int seleccion = Integer.parseInt(sc.nextLine());

            switch (seleccion) {
                case 1:
                    addPregunta();
                    break;

                case 2:
                    System.out.println("Introduzca el ID de la pregunta a eliminar: ");
                    int idEliminar = sc.nextInt();
                    sc.nextLine();
                    cd.removePreguntaById(idEliminar);
                    System.out.println("Pregunta con ID "+idEliminar+" eliminada.");
                    //no borra de persistencia hasta que se de a guardar encuesta
                    break;
                case 3:
                    modificarPregunta();
                    break;

                case 4:
                    fin = true;
                    break;

                default:
                    System.out.println("Seleccion no valida.");
            }
        }
    }

    private static void addPregunta() {
        System.out.print("ID de la pregunta: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Texto del enunciado: ");
        String texto = sc.nextLine();

        System.out.print("Obligatoria? (true/false): ");
        boolean obligatorio = Boolean.parseBoolean(sc.nextLine());

        System.out.println("Tipo de pregunta:");
        System.out.println("1 Formato Libre");
        System.out.println("2 Numérica");
        System.out.println("3 Con Opciones");
        System.out.print("Seleccione: ");

        String tipo = sc.nextLine();

        switch (tipo) {
            case "1":
                cd.addPregunta(id, texto, obligatorio, new FormatoLibre());
                System.out.println("Vista previa de la pregunta creada:");
                vistaPreviaPregunta(cd.getPreguntaById(id));
                break;

            case "2":
                System.out.println("indique el rango minimo ");
                int min = Integer.parseInt(sc.nextLine());
                System.out.println("indique el rango maximo ");
                int max = Integer.parseInt(sc.nextLine());
                cd.addPregunta(id, texto, obligatorio, new Numerica(min,max));
                break;

            case "3":
                ArrayList<String> opciones = new ArrayList<>();
                System.out.println("Introduzca las opciones (escriba '.' para terminar):");
                while (true) {
                    String op = sc.nextLine();
                    if (op.equals(".")) break;
                    opciones.add(op);
                }
                cd.addPregunta(id, texto, obligatorio, new ConOpciones(opciones.size()));
                for (int i = 0; i < opciones.size(); i++) {
                    ((ConOpciones) cd.getPreguntaById(id).getTipoPregunta()).addOpcion(new Opcion(i + 1, opciones.get(i)));
                }
                break;

            default:
                System.out.println("Tipo no válido.");
        }

        System.out.println("Pregunta añadida.");
    }

    private static void modificarPregunta() {
        System.out.print("ID de la pregunta a modificar: ");
        int id = Integer.parseInt(sc.nextLine());

        cd.seleccionarPreguntaPorId(id);
        Pregunta p = cd.getPreguntaById(id);

        if (p == null) {
            System.out.println("La pregunta no existe.");
            return;
        }
        vistaPreviaPregunta(p);
        System.out.println("1 Cambiar texto");
        System.out.println("2 Cambiar tipo");
        System.out.println("3 Gestionar opciones (solo ConOpciones)");
        System.out.println("4 Volver");

        String op = sc.nextLine();

        switch (op) {

            case "1":   // CAMBIAR TEXTO
                System.out.print("Nuevo texto: ");
                cd.modificarPreguntaTextoSeleccionada(sc.nextLine());
                break;

            case "2":   // CAMBIAR TIPO
                cambiarTipoPregunta(id);
                break;

            case "3":   // GESTIONAR OPCIONES
                gestionarOpciones(id);
                break;

            case "4":
                cd.cancelarSeleccionPregunta();
                return;
            default:
                System.out.println("Opción no válida.");
        }

        cd.cancelarSeleccionPregunta();
        System.out.println("Pregunta modificada.");
    }

    private static void cambiarTipoPregunta(int id) {

        System.out.println("Nuevo tipo:");
        System.out.println("1 Formato Libre");
        System.out.println("2 Numérica");
        System.out.println("3 Con Opciones");

        String tipo = sc.nextLine();

        switch (tipo) {

            case "1":
                cd.modificarPreguntaTipoSeleccionada(new FormatoLibre());
                break;

            case "2":
                System.out.println("Rango mínimo:");
                int min = Integer.parseInt(sc.nextLine());

                System.out.println("Rango máximo:");
                int max = Integer.parseInt(sc.nextLine());

                cd.modificarPreguntaTipoSeleccionada(new Numerica(min, max));
                break;

            case "3":
                System.out.println("Número inicial de opciones: ");
                int n = Integer.parseInt(sc.nextLine());

                cd.modificarPreguntaTipoSeleccionada(new ConOpciones(n));

                // ahora se piden las opciones
                for (int i = 1; i <= n; i++) {
                    anadirOpcion((ConOpciones) cd.getPreguntaById(id).getTipoPregunta());
                }
                break;

            default:
                System.out.println("Tipo inválido.");
        }
    }

    private static void gestionarOpciones(int id) {
        Pregunta p = cd.getPreguntaById(id);

        if (!(p.getTipoPregunta() instanceof ConOpciones tipo)) {
            System.out.println("Esta pregunta no es de tipo ConOpciones.");
            return;
        }

        while (true) {
            System.out.println("Opciones actuales:");
            for (Opcion o : tipo.getOpciones()) {
                System.out.println(o.getId() + ". " + o.getTexto());
            }

            System.out.println("--- Gestión de opciones ---");
            System.out.println("1 Añadir opción");
            System.out.println("2 Modificar opción");
            System.out.println("3 Eliminar opción");
            System.out.println("4 Volver");

            System.out.print("Seleccione: ");
            String op = sc.nextLine();

            switch (op) {
                case "1":
                    anadirOpcion(tipo);
                    break;

                case "2":
                    modificarOpcion(tipo);
                    break;

                case "3":
                    eliminarOpcion(tipo);
                    break;

                case "4":
                    return;

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void anadirOpcion(ConOpciones tipo) {
        System.out.print("Texto de la nueva opción: ");
        String texto = sc.nextLine();

        int nuevoId = tipo.getOpciones().size() + 1;
        tipo.addOpcion(new Opcion(nuevoId, texto));

        System.out.println("Opción añadida.");
    }

    private static void modificarOpcion(ConOpciones tipo) {
        System.out.print("ID de la opción a modificar: ");
        int idOp = Integer.parseInt(sc.nextLine());
        if (idOp < 1 || idOp > tipo.getOpciones().size()) {
            System.out.println("ID inválido.");
            return;
        }

        System.out.print("Nuevo texto: ");
        String nuevoTexto = sc.nextLine();

        tipo.getOpciones().get(idOp - 1).setTexto(nuevoTexto);
        System.out.println("Opción modificada.");
    }

    private static void eliminarOpcion(ConOpciones tipo) {
        System.out.print("ID de la opción a eliminar: ");
        int idOp = Integer.parseInt(sc.nextLine());

        if (idOp < 1 || idOp > tipo.getOpciones().size()) {
            System.out.println("ID inválido.");
            return;
        }
        tipo.removeOpcion(tipo.getOpciones().get(idOp-1));

        // reindexar
        for (int i = 0; i < tipo.getOpciones().size(); i++) {
            tipo.getOpciones().get(i).setId(i + 1);
        }

        System.out.println("Opción eliminada y reindexada.");
    }


    //PERFILES
    private static void menuPerfil() {
        boolean fin = false;
        while (!fin) {
            System.out.println("---- Menu perfil ----");
            System.out.println("Elige una opcion:");
            System.out.println("1 Crear perfil");
            System.out.println("2 Cargar perfil");
            System.out.println("3 Ir al menu principal");
            System.out.println("4 Salir");

            int seleccion = sc.nextInt();
            sc.nextLine();
            //int seleccion = Integer.parseInt(sc.nextLine());
            switch (seleccion) {
                case 1:
                    System.out.println("Introduzca el email: ");
                    String email = sc.nextLine();
                    System.out.println("Introduzca el nombre: ");
                    String nombre = sc.nextLine();
                    System.out.println("Introduzca la contraseña: ");
                    String password = sc.nextLine();
                    cd.crearPerfil(email, nombre, password);
                    System.out.println("Perfil con email "+email+" creado y cargado.");
                    fin = true;
                    break;

                case 2:
                    System.out.println("Introduzca el email a cargar: ");
                    String emailCargar = sc.nextLine();
                    //AQUI IRIA EL INICIO DE SESION
                    cd.importarPerfil(emailCargar);
                    System.out.println("Perfil con email: "+emailCargar+" cargado.");
                    fin = true;
                    break;

                case 3:
                    if(cd.getPerfilCargado() == null) {
                        System.out.println("No hay perfil cargado. Por favor, cree o cargue un perfil primero.");
                    }
                    else {
                        fin = true;
                    }
                    break;
                case 4:
                    System.out.println("Saliendo del programa.");
                    System.exit(0);
                default:
                    System.out.println("Seleccion no valida.");
            }
        }
    }

    //RESPUESTAS
    private static void menuRespuesta() {
        boolean fin = false;

        while (!fin) {
            System.out.println("---- Menu respuestas ----");
            System.out.println("1 Cargar respuesta");
            System.out.println("2 Cargar todas las respuestas de la encuesta actual");
            System.out.println("3 Guardar respuesta actual");
            System.out.println("4 Borrar respuesta actual");
            System.out.println("5 Responder encuesta actual");
            System.out.println("6 Responder pregunta");
            System.out.println("7 Volver");

            int seleccion = sc.nextInt();
            sc.nextLine();
            //int seleccion = Integer.parseInt(sc.nextLine());

            try {
                switch (seleccion) {
                    case 1: //path o email?
                        System.out.println("Introduzca el email del respondedor: ");
                        String emailRespondedor = sc.nextLine();
                        cd.importarRespuesta(emailRespondedor);
                        System.out.println("Respuesta de "+emailRespondedor+" cargada.");
                        break;
                    case 2:
                        cd.importarRespuestas();
                        System.out.println("Todas las respuestas de la encuesta actual cargadas.");
                        break;
                    case 3:
                        cd.guardarRespuesta();
                        System.out.println("Respuesta actual guardada.");
                        break;

                    case 4:
                        cd.borrarRespuestaActual();
                        System.out.println("Respuesta actual borrada.");
                        break;

                    case 5:
                        responderEncuestaCompleta();
                        break;
                    case 6:
                        responderPregunta();
                    case 7:
                        fin = true;
                        break;

                    default: System.out.println("Seleccion no valida.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static void responderEncuestaCompleta() {
        try {
            if (cd.getEncuestaEnMemoria() == null) {
                System.out.println("No hay encuesta para responder.");
                return;
            }
            if (cd.getPerfilCargado() == null) {
                System.out.println("No hay perfil cargado.");
                return;
            }

            cd.responderEncuesta(); // crea nueva respuesta ligada al perfil

            System.out.println("=== Respondiendo encuesta ===");

            for (Pregunta p : cd.getEncuestaEnMemoria().getPreguntas()) {
                boolean respuestaValida = false;

                while (!respuestaValida) {
                    System.out.println("\nPregunta " + p.getId() + ": " + p.getTexto());
                    if (p.isObligatorio()) System.out.println("[Obligatoria]");

                    respuestaValida = responderPreguntaForzada(p);
                }
            }

            System.out.println("\nEncuesta completada.");
            cd.guardarRespuesta();

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    private static void responderPregunta() {
        try {
            if (cd.getEncuestaEnMemoria() == null) {
                System.out.println("No hay encuesta cargada.");
                return;
            }
            if (cd.getRespuestaActual() == null) {
                System.out.println("No hay respuesta cargada/creada.");
                return;
            }

            System.out.println("Introduzca el ID de la pregunta a responder: ");
            int idPregunta = Integer.parseInt(sc.nextLine());

            Pregunta p = cd.getPreguntaById(idPregunta);
            if (p == null) {
                System.out.println("La pregunta no existe.");
                return;
            }

            TipoPregunta tipo = p.getTipoPregunta();

            // ==== FORMATO LIBRE ====
            if (tipo instanceof FormatoLibre) {
                System.out.println("Introduzca su respuesta (texto): ");
                String dato = sc.nextLine();

                // validar obligatoria
                if (p.isObligatorio() && dato.isEmpty()) {
                    System.out.println("Esta pregunta es obligatoria. No puede quedar en blanco.");
                    return;
                }

                cd.addDatoRespuesta(idPregunta, new TDatosString(dato));
                System.out.println("Respuesta registrada.");
                return;
            }

            // ==== NUMÉRICA ====
            if (tipo instanceof Numerica n) {

                System.out.println("Introduzca un número entre " + n.getMin() + " y " + n.getMax() + " :");
                String dato = sc.nextLine();

                try {
                    int valor = Integer.parseInt(dato);

                    if (valor < n.getMin() || valor > n.getMax()) {
                        System.out.println("Valor fuera del rango permitido.");
                        return;
                    }

                    cd.addDatoRespuesta(idPregunta, new TDatosInteger(valor));
                    System.out.println("Respuesta registrada.");
                } catch (NumberFormatException e) {
                    System.out.println("Debe introducir un número válido.");
                }
                return;
            }

            // ==== CON OPCIONES ====
            if (tipo instanceof ConOpciones co) {

                System.out.println("Seleccione una o varias opciones (escriba '.' para terminar):");
                for (Opcion o : co.getOpciones()) {
                    System.out.println(o.getId() + ". " + o.getTexto());
                }

                ArrayList<Integer> seleccionadas = new ArrayList<>();

                while (true) {
                    System.out.print("Opción: ");
                    String entrada = sc.nextLine();

                    if (entrada.equals(".")) break;

                    try {
                        int idOp = Integer.parseInt(entrada);

                        if (idOp < 1 || idOp > co.getOpciones().size()) {
                            System.out.println("Opción inválida.");
                            continue;
                        }

                        if (!seleccionadas.contains(idOp)) {
                            seleccionadas.add(idOp);
                        } else {
                            System.out.println("Ya seleccionada.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Debe introducir un número o '.'");
                    }
                }

                if (p.isObligatorio() && seleccionadas.isEmpty()) {
                    System.out.println("Debe seleccionar al menos una opción (pregunta obligatoria).");
                    return;
                }

                cd.addDatoRespuesta(idPregunta, new TDatosOpciones(seleccionadas));
                System.out.println("Respuesta registrada.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static boolean responderPreguntaForzada(Pregunta p) {
        try {
            TipoPregunta tipo = p.getTipoPregunta();

            // FORMATO LIBRE
            if (tipo instanceof FormatoLibre) {
                System.out.print("Respuesta (texto): ");
                String txt = sc.nextLine();

                if (p.isObligatorio() && txt.isEmpty()) {
                    System.out.println("Esta pregunta es obligatoria.");
                    return false;
                }

                cd.addDatoRespuesta(p.getId(), new TDatosString(txt));
                return true;
            }

            // NUMÉRICA
            if (tipo instanceof Numerica n) {

                System.out.print("Numero (" + n.getMin() + " - " + n.getMax() + "): ");
                String txt = sc.nextLine();

                try {
                    int val = Integer.parseInt(txt);

                    if (val < n.getMin() || val > n.getMax()) {
                        System.out.println("Número fuera del rango.");
                        return false;
                    }

                    cd.addDatoRespuesta(p.getId(), new TDatosInteger(val));
                    return true;

                } catch (NumberFormatException e) {
                    System.out.println("Debe introducir un número válido.");
                    return false;
                }
            }

            // CON OPCIONES
            // CON OPCIONES
            if (tipo instanceof ConOpciones co) {

                System.out.println("Seleccione una o varias opciones ('.' para terminar):");
                for (Opcion o : co.getOpciones()) {
                    System.out.println(o.getId() + ". " + o.getTexto());
                }

                ArrayList<Integer> seleccionadas = new ArrayList<>();

                while (true) {
                    System.out.print("Opción: ");
                    String entrada = sc.nextLine();

                    if (entrada.equals(".")) break;

                    try {
                        int idOp = Integer.parseInt(entrada);

                        if (idOp < 1 || idOp > co.getOpciones().size()) {
                            System.out.println("Opción inválida.");
                            continue;
                        }

                        if (!seleccionadas.contains(idOp)) {
                            seleccionadas.add(idOp);
                        } else {
                            System.out.println("Ya seleccionada.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Número inválido.");
                    }
                }

                if (p.isObligatorio() && seleccionadas.isEmpty()) {
                    System.out.println("Debe seleccionar al menos una opción.");
                    return false;
                }

                cd.addDatoRespuesta(p.getId(), new TDatosOpciones(seleccionadas));
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }



    //ALGORITMOS + INICIALIZADORES
    private static void menuAlgoritmo() {
        System.out.println("---- ALGORTIMO DE ANÁLISIS ----");
        System.out.println("Elija algoritmo:");
        System.out.println("1 KMeans");
        System.out.println("2 KMedoids");

        int a = sc.nextInt();
        sc.nextLine();
        //int a = Integer.parseInt(sc.nextLine());
        String algoritmo = "KMeans"; //default
        boolean seleccionValida = false;
        while(!seleccionValida){
            switch (a) {
                case 1:
                    algoritmo = "KMeans";
                    seleccionValida = true;
                    break;
                case 2:
                    algoritmo = "KMedoids";
                    seleccionValida = true;
                    break;
                default:
                    System.out.println("Seleccion no valida. Elija nuevamente:");
                    a = sc.nextInt();
                    sc.nextLine();
                    //a = Integer.parseInt(sc.nextLine());
            }
        }
        System.out.println("Elija inicializador:");
        System.out.println("1 KMeans++");
        System.out.println("2 Aleatorio");
        System.out.println("3 Greedy");
        int i = sc.nextInt();
        sc.nextLine();
        //int i = Integer.parseInt(sc.nextLine());
        seleccionValida = false;
        String inicializador = "KMeans++"; //default
        while(!seleccionValida){
            switch (i) {
                case 1:
                    inicializador = "KMeans++";
                    seleccionValida = true;
                    break;
                case 2:
                    inicializador = "Aleatorio";
                    seleccionValida = true;
                    break;
                case 3:
                    inicializador = "Greedy";
                    seleccionValida = true;
                    break;
                default:
                    System.out.println("Seleccion no valida. Elija nuevamente:");
                    i = sc.nextInt();
                    sc.nextLine();
                    //i = Integer.parseInt(sc.nextLine());
            }
        }

        try {
            cd.elegirAlgoritmoAnalisis(algoritmo, inicializador);
            System.out.println("Algoritmo: "+algoritmo+" Inicializador: "+inicializador+" configurado correctamente.");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    //ANÁLISIS DE ENCUESTA

    private static void menuConfigurarK() {
        while (true) {
            System.out.println("\n=== CONFIGURAR K ===");
            System.out.println("1 Introducir K manualmente");
            System.out.println("2 Calcular K automáticamente");
            System.out.println("3 Mostrar K actual");
            System.out.println("4 Volver");

            int opcion = sc.nextInt();
            sc.nextLine();
            //int opcion = Integer.parseInt(sc.nextLine());

            try {
                switch (opcion) {
                    case 1:
                        System.out.print("Introduce un valor de K: ");
                        int kManual = Integer.parseInt(sc.nextLine());
                        cd.setK(kManual);
                        System.out.println("K establecido a " + kManual);
                        break;

                    case 2:
                        System.out.println("Calculando K automáticamente...");
                        int bestK = cd.calcularK();
                        System.out.println("El mejor valor estimado de K es: " + bestK);
                        break;

                    case 3:
                        System.out.println("K actual: " + cd.getK());
                        break;

                    case 4:
                        return;

                    default:
                        System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static void ejecutarAnalisis() {
        // Ya no se hace analisis en el driver
    }
    private static void menuConfigurarEvaluadorCalidad() {
        while (true) {
            System.out.println("\n=== CONFIGURAR EVALUADOR DE CALIDAD ===");
            System.out.println("Seleccione el evaluador a utilizar:");
            System.out.println("1 Silhouette");
            System.out.println("2 Calinski-Harabasz");
            System.out.println("3 Davies-Bouldin");
            System.out.println("4 Mostrar evaluador actual");
            System.out.println("5 Volver");

            int opcion = sc.nextInt();
            sc.nextLine();

            try {
                switch (opcion) {

                    case 1:
                        cd.elegirEvaluadorCalidad("Silhouette");
                        System.out.println("Evaluador configurado: Silhouette");
                        break;

                    case 2:
                        cd.elegirEvaluadorCalidad("CalinskiHarabasz");
                        System.out.println("Evaluador configurado: Calinski-Harabasz");
                        break;

                    case 3:
                        cd.elegirEvaluadorCalidad("DaviesBouldinen");
                        System.out.println("Evaluador configurado: Davies-Bouldin");
                        break;

                    case 4:
                        System.out.println("Evaluador actual: " + cd.getEvaluadorCalidad());
                        break;

                    case 5:
                        return;

                    default:
                        System.out.println("Opción no válida.");
                }

            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

}
