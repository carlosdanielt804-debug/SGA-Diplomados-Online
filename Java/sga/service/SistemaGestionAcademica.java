package sga.service;

import sga.model.*;
import java.io.*;
import java.util.*;

public class SistemaGestionAcademica {
    private List<Alumno> alumnos;
    private List<Profesor> profesores;
    private Stack<AccionNota> pilaComandos;
    private Queue<Alumno> colaCertificados;

    private final String archivoAlumnos = "Data/alumnos.txt";
    private final String archivoProfesores = "Data/profesores.txt";
    private final String archivoCertificados = "Data/certificados_pendientes.txt";

    public SistemaGestionAcademica() {
        this.alumnos = new ArrayList<>();
        this.profesores = new ArrayList<>();
        this.pilaComandos = new Stack<>();
        this.colaCertificados = new LinkedList<>();
        cargarArchivosTxt();
    }

    public void registrarAlumno(Scanner sc) {
        System.out.println("\n--- Registrar Alumno ---");
        System.out.print("Cedula/ID: ");
        String cedula = sc.nextLine().trim();
        System.out.print("Nombre Completo: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Correo Electronico: ");
        String correo = sc.nextLine().trim();
        System.out.println("Seleccione Programa: 1. Curso | 2. Diplomado | 3. Bootcamp");
        System.out.print("Opcion (1-3): ");
        String opc = sc.nextLine().trim();

        ProgramaAcademico prog;
        if (opc.equals("1")) {
            prog = new Curso();
        } else if (opc.equals("2")) {
            prog = new Diplomado();
        } else if (opc.equals("3")) {
            prog = new Bootcamp();
        } else {
            System.out.println("Opcion de programa invalida.");
            return;
        }

        Alumno alumno = new Alumno(cedula, nombre, correo, prog);
        alumnos.add(alumno);
        guardarArchivosTxt();
        System.out.println("Alumno registrado exitosamente.");
    }

    public void registrarProfesor(Scanner sc) {
        System.out.println("\n--- Registrar Profesor ---");
        System.out.print("Cedula/ID: ");
        String cedula = sc.nextLine().trim();
        System.out.print("Nombre Completo: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Correo Electronico: ");
        String correo = sc.nextLine().trim();
        System.out.print("Especialidad: ");
        String especialidad = sc.nextLine().trim();
        System.out.print("Materia Asignada: ");
        String materia = sc.nextLine().trim();

        Profesor profesor = new Profesor(cedula, nombre, correo, especialidad, materia);
        profesores.add(profesor);
        guardarArchivosTxt();
        System.out.println("Profesor registrado exitosamente.");
    }

    public void registrarNota(Scanner sc) {
        System.out.println("\n--- Registrar Nota ---");
        System.out.print("Ingrese Cedula del Alumno: ");
        String cedula = sc.nextLine().trim();

        Alumno alumno = null;
        for (Alumno a : alumnos) {
            if (a.getCedula().equals(cedula)) {
                alumno = a;
                break;
            }
        }

        if (alumno == null) {
            System.out.println("Alumno no encontrado.");
            return;
        }

        try {
            System.out.print("Ingrese nota (0 - 20): ");
            float nota = Float.parseFloat(sc.nextLine().trim());
            if (nota < 0 || nota > 20) {
                System.out.println("La nota debe estar entre 0 y 20.");
                return;
            }

            if (alumno.agregarNota(nota)) {
                pilaComandos.push(new AccionNota(cedula, nota));
                guardarArchivosTxt();
                System.out.println("Nota registrada exitosamente.");
            } else {
                System.out.println("El alumno ya tiene el maximo de 3 notas ingresadas.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un valor numerico valido.");
        }
    }

    public void deshacerUltimaNota() {
        System.out.println("\n--- Deshacer Ultimo Registro de Nota (LIFO) ---");
        if (pilaComandos.isEmpty()) {
            System.out.println("No hay acciones recientes para deshacer.");
            return;
        }

        AccionNota ultimaAccion = pilaComandos.pop();
        for (Alumno a : alumnos) {
            if (a.getCedula().equals(ultimaAccion.getCedula())) {
                a.deshacerNota();
                guardarArchivosTxt();
                System.out.println("Se removio la nota " + ultimaAccion.getNota() +
                                   " del alumno " + a.getNombre() + ".");
                return;
            }
        }
    }

    public void generarColaCertificados() {
        System.out.println("\n--- Generar Cola de Certificados (FIFO) ---");
        colaCertificados.clear();

        for (Alumno a : alumnos) {
            if (a.estaAprobado()) {
                colaCertificados.add(a);
            }
        }

        System.out.println("Total graduandos procesados en cola: " + colaCertificados.size());

        try (PrintWriter writer = new PrintWriter(new FileWriter(archivoCertificados))) {
            writer.println("=========================================");
            writer.println("REPORTE DE CERTIFICADOS PENDIENTES");
            writer.println("=========================================");
            writer.println("Total de graduandos en cola: " + colaCertificados.size() + "\n");

            int idx = 1;
            for (Alumno al : colaCertificados) {
                writer.println(idx + ". [" + al.getCedula() + "] " + al.getNombre());
                writer.println("   - Programa: " + al.getPrograma().getNombrePrograma());
                writer.println(String.format("   - Promedio Final: %.1f", al.obtenerPromedio()));
                writer.println("   - Estatus: APROBADO\n");
                idx++;
            }

            writer.println("=========================================");
            writer.println("* Fin del reporte - Generado por SGA-DO *");
            System.out.println("Archivo '" + archivoCertificados + "' generado con exito.");
        } catch (IOException e) {
            System.out.println("Error al escribir archivo de certificados.");
        }
    }

    public void mostrarReporteGeneral() {
        System.out.println("\n=========================================");
        System.out.println("          REPORTE GENERAL SGA-DO         ");
        System.out.println("=========================================");
        System.out.println("\n--- PROFESORES ACTIVOS ---");
        if (profesores.isEmpty()) {
            System.out.println("No hay profesores registrados.");
        } else {
            for (Profesor p : profesores) {
                System.out.println(p.mostrarInformacion());
            }
        }

        System.out.println("\n--- ALUMNOS REGISTRADOS ---");
        if (alumnos.isEmpty()) {
            System.out.println("No hay alumnos registrados.");
        } else {
            for (Alumno a : alumnos) {
                System.out.println(a.mostrarInformacion());
            }
        }
        System.out.println("=========================================\n");
    }

    private void guardarArchivosTxt() {
        File dataDir = new File("Data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoAlumnos))) {
            for (Alumno a : alumnos) {
                List<Float> notas = a.getNotas();
                float n1 = notas.size() > 0 ? notas.get(0) : 0;
                float n2 = notas.size() > 1 ? notas.get(1) : 0;
                float n3 = notas.size() > 2 ? notas.get(2) : 0;
                pw.println(a.getCedula() + "," + a.getNombre() + "," + a.getCorreo() + "," +
                           a.getPrograma().getNombrePrograma() + "," + n1 + "," + n2 + "," + n3);
            }
        } catch (IOException e) {
            System.out.println("Error al guardar archivo de alumnos.");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoProfesores))) {
            for (Profesor p : profesores) {
                pw.println(p.getCedula() + "," + p.getNombre() + "," + p.getCorreo() + "," +
                           p.getEspecialidad() + "," + p.getMateriaAsignada());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar archivo de profesores.");
        }
    }

    private void cargarArchivosTxt() {
        File dataDir = new File("Data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        File fAlumnos = new File(archivoAlumnos);
        if (fAlumnos.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fAlumnos))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length >= 7) {
                        String cedula = partes[0];
                        String nombre = partes[1];
                        String correo = partes[2];
                        String tipoPrograma = partes[3];

                        ProgramaAcademico prog;
                        if (tipoPrograma.equals("Curso")) {
                            prog = new Curso();
                        } else if (tipoPrograma.equals("Diplomado")) {
                            prog = new Diplomado();
                        } else {
                            prog = new Bootcamp();
                        }

                        Alumno a = new Alumno(cedula, nombre, correo, prog);
                        for (int i = 4; i <= 6; i++) {
                            float nota = Float.parseFloat(partes[i]);
                            if (nota > 0) {
                                a.agregarNota(nota);
                            }
                        }
                        alumnos.add(a);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al cargar archivo de alumnos.");
            }
        }

        File fProfesores = new File(archivoProfesores);
        if (fProfesores.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fProfesores))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length >= 5) {
                        String cedula = partes[0];
                        String nombre = partes[1];
                        String correo = partes[2];
                        String especialidad = partes[3];
                        String materia = partes[4];
                        profesores.add(new Profesor(cedula, nombre, correo, especialidad, materia));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al cargar archivo de profesores.");
            }
        }
    }
}