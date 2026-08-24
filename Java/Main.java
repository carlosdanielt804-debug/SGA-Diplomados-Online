import java.io.*;
import java.util.*;

// ==========================================
// JERARQUÍA DE PROGRAMAS ACADÉMICOS
// ==========================================
abstract class ProgramaAcademico {
    protected String nombrePrograma;

    public ProgramaAcademico(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public abstract boolean evaluarAprobacion(List<Float> notas);
}

class Curso extends ProgramaAcademico {
    public Curso() {
        super("Curso");
    }

    @Override
    public boolean evaluarAprobacion(List<Float> notas) {
        if (notas.isEmpty()) return false;
        float suma = 0;
        for (float n : notas) suma += n;
        return (suma / notas.size()) >= 10.0f;
    }
}

class Diplomado extends ProgramaAcademico {
    public Diplomado() {
        super("Diplomado");
    }

    @Override
    public boolean evaluarAprobacion(List<Float> notas) {
        if (notas.isEmpty()) return false;
        float suma = 0;
        for (float n : notas) suma += n;
        return (suma / notas.size()) >= 14.0f;
    }
}

class Bootcamp extends ProgramaAcademico {
    public Bootcamp() {
        super("Bootcamp");
    }

    @Override
    public boolean evaluarAprobacion(List<Float> notas) {
        if (notas.isEmpty()) return false;
        for (float n : notas) {
            if (n < 14.0f) return false; // Regla: Ninguna nota < 14
        }
        return true;
    }
}

// ==========================================
// JERARQUÍA DE PERSONAS
// ==========================================
abstract class Persona {
    protected String cedula;
    protected String nombre;
    protected String correo;

    public Persona(String cedula, String nombre, String correo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }

    public abstract String mostrarInformacion();
}

class Alumno extends Persona {
    private List<Float> notas;
    private ProgramaAcademico programa;

    public Alumno(String cedula, String nombre, String correo, ProgramaAcademico programa) {
        super(cedula, nombre, correo);
        this.programa = programa;
        this.notas = new ArrayList<>();
    }

    public boolean agregarNota(float nota) {
        if (notas.size() < 3) {
            notas.add(nota);
            return true;
        }
        return false;
    }

    public Float deshacerNota() {
        if (!notas.isEmpty()) {
            return notas.remove(notas.size() - 1);
        }
        return null;
    }

    public List<Float> getNotas() { return notas; }
    public ProgramaAcademico getPrograma() { return programa; }

    public float obtenerPromedio() {
        if (notas.isEmpty()) return 0.0f;
        float suma = 0;
        for (float n : notas) suma += n;
        return suma / notas.size();
    }

    public boolean estaAprobado() {
        return programa.evaluarAprobacion(notas);
    }

    @Override
    public String mostrarInformacion() {
        String estatus = estaAprobado() ? "APROBADO" : "REPROBADO";
        return String.format("[Alumno] ID: %s | Nombre: %s | Correo: %s | Prog: %s | Prom: %.1f | Estatus: %s",
                cedula, nombre, correo, programa.getNombrePrograma(), obtenerPromedio(), estatus);
    }
}

class Profesor extends Persona {
    private String especialidad;
    private String materiaAsignada;

    public Profesor(String cedula, String nombre, String correo, String especialidad, String materiaAsignada) {
        super(cedula, nombre, correo);
        this.especialidad = especialidad;
        this.materiaAsignada = materiaAsignada;
    }

    public String getEspecialidad() { return especialidad; }
    public String getMateriaAsignada() { return materiaAsignada; }

    @Override
    public String mostrarInformacion() {
        return String.format("[Profesor] ID: %s | Nombre: %s | Correo: %s | Esp: %s | Materia: %s",
                cedula, nombre, correo, especialidad, materiaAsignada);
    }
}

// ==========================================
// REGISTRO DE ACCIÓN PARA PILA (LIFO)
// ==========================================
class AccionNota {
    String cedula;
    float nota;

    public AccionNota(String cedula, float nota) {
        this.cedula = cedula;
        this.nota = nota;
    }
}

// ==========================================
// GESTIÓN CENTRAL Y PERSISTENCIA
// ==========================================
class SistemaGestionAcademica {
    private List<Alumno> alumnos;
    private List<Profesor> profesores;
    private Stack<AccionNota> pilaComandos; // Estructura LIFO
    private Queue<Alumno> colaCertificados; // Estructura FIFO

    private final String archivoAlumnos = "alumnos.txt";
    private final String archivoProfesores = "profesores.txt";
    private final String archivoCertificados = "certificados_pendientes.txt";

    public SistemaGestionAcademica() {
        this.alumnos = new ArrayList<>();
        this.profesores = new ArrayList<>();
        this.pilaComandos = new Stack<>();
        this.colaCertificados = new LinkedList<>();
        cargarArchivosTxt();
    }

    public void registrarAlumno(Scanner sc) {
        System.out.println("\n--- Registrar Alumno ---");
        System.out.print("Cédula/ID: "); String cedula = sc.nextLine().trim();
        System.out.print("Nombre Completo: "); String nombre = sc.nextLine().trim();
        System.out.print("Correo Electrónico: "); String correo = sc.nextLine().trim();
        System.out.println("Seleccione Programa: 1. Curso | 2. Diplomado | 3. Bootcamp");
        System.out.print("Opción (1-3): "); String opc = sc.nextLine().trim();

        ProgramaAcademico prog;
        if (opc.equals("1")) prog = new Curso();
        else if (opc.equals("2")) prog = new Diplomado();
        else if (opc.equals("3")) prog = new Bootcamp();
        else {
            System.out.println("❌ Opción de programa inválida.");
            return;
        }

        alumnos.add(new Alumno(cedula, nombre, correo, prog));
        guardarArchivosTxt();
        System.out.println("✅ Alumno registrado exitosamente.");
    }

    public void registrarProfesor(Scanner sc) {
        System.out.println("\n--- Registrar Profesor ---");
        System.out.print("Cédula/ID: "); String cedula = sc.nextLine().trim();
        System.out.print("Nombre Completo: "); String nombre = sc.nextLine().trim();
        System.out.print("Correo Electrónico: "); String correo = sc.nextLine().trim();
        System.out.print("Especialidad: "); String esp = sc.nextLine().trim();
        System.out.print("Materia Asignada: "); String mat = sc.nextLine().trim();

        profesores.add(new Profesor(cedula, nombre, correo, esp, mat));
        guardarArchivosTxt();
        System.out.println("✅ Profesor registrado exitosamente.");
    }

    public void registrarNota(Scanner sc) {
        System.out.println("\n--- Registrar Nota ---");
        System.out.print("Ingrese Cédula del Alumno: ");
        String cedula = sc.nextLine().trim();

        Alumno alumno = null;
        for (Alumno a : alumnos) {
            if (a.getCedula().equals(cedula)) { alumno = a; break; }
        }

        if (alumno == null) {
            System.out.println("❌ Alumno no encontrado.");
            return;
        }

        try {
            System.out.print("Ingrese nota (0 - 20): ");
            float nota = Float.parseFloat(sc.nextLine().trim());
            if (nota < 0 || nota > 20) {
                System.out.println("❌ La nota debe estar entre 0 y 20.");
                return;
            }

            if (alumno.agregarNota(nota)) {
                pilaComandos.push(new AccionNota(cedula, nota)); // LIFO
                guardarArchivosTxt();
                System.out.println("✅ Nota registrada exitosamente.");
            } else {
                System.out.println("❌ El alumno ya tiene 3 notas registradas.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese un valor numérico válido.");
        }
    }

    public void deshacerUltimaNota() {
        System.out.println("\n--- Deshacer Último Registro de Nota (LIFO) ---");
        if (pilaComandos.isEmpty()) {
            System.out.println("⚠️ No hay acciones recientes para deshacer.");
            return;
        }

        AccionNota ultimaAccion = pilaComandos.pop(); // LIFO
        for (Alumno a : alumnos) {
            if (a.getCedula().equals(ultimaAccion.cedula)) {
                a.deshacerNota();
                guardarArchivosTxt();
                System.out.println("✅ Se removió la nota " + ultimaAccion.nota + " del alumno " + a.getNombre() + ".");
                break;
            }
        }
    }

    public void generarColaCertificados() {
        System.out.println("\n--- Generar Cola de Certificados (FIFO) ---");
        colaCertificados.clear();

        for (Alumno a : alumnos) {
            if (a.estaAprobado()) {
                colaCertificados.add(a); // Encolar FIFO
            }
        }

        System.out.println("Total graduandos procesados en cola: " + colaCertificados.size());

        try (PrintWriter writer = new PrintWriter(new FileWriter(archivoCertificados))) {
            writer.println("=========================================");
            writer.println("REPORTE DE CERTIFICADOS PENDIENTES");
            writer.println("=========================================");
            writer.println("Total de graduandos en cola: " + colaCertificados.size() + "\n");

            int idx = 1;
            while (!colaCertificados.isEmpty()) {
                Alumno al = colaCertificados.poll(); // Desencolar FIFO
                writer.println(idx + ". [" + al.getCedula() + "] " + al.getNombre());
                writer.println("   - Programa: " + al.getPrograma().getNombrePrograma());
                writer.println(String.format("   - Promedio Final: %.1f", al.obtenerPromedio()));
                writer.println("   - Estatus: APROBADO\n");
                idx++;
            }
            writer.println("=========================================");
            writer.println("* Fin del reporte - Generado por SGA-DO *");
            System.out.println("✅ Archivo '" + archivoCertificados + "' generado con éxito.");
        } catch (IOException e) {
            System.out.println("❌ Error al escribir archivo de certificados.");
        }
    }

    public void mostrarReporteGeneral() {
        System.out.println("\n=========================================");
        System.out.println("          REPORTE GENERAL SGA-DO         ");
        System.out.println("=========================================");
        System.out.println("\n--- PROFESORES ACTIVOS ---");
        for (Profesor p : profesores) System.out.println(p.mostrarInformacion());

        System.out.println("\n--- ALUMNOS REGISTRADOS ---");
        for (Alumno a : alumnos) System.out.println(a.mostrarInformacion());
        System.out.println("=========================================\n");
    }

    private void guardarArchivosTxt() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoAlumnos))) {
            for (Alumno a : alumnos) {
                List<Float> n = a.getNotas();
                float n1 = n.size() > 0 ? n.get(0) : 0;
                float n2 = n.size() > 1 ? n.get(1) : 0;
                float n3 = n.size() > 2 ? n.get(2) : 0;
                pw.println(a.getCedula() + "," + a.getNombre() + "," + a.getCorreo() + "," +
                        a.getPrograma().getNombrePrograma() + "," + n1 + "," + n2 + "," + n3);
            }
        } catch (IOException ignored) {}

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoProfesores))) {
            for (Profesor p : profesores) {
                pw.println(p.getCedula() + "," + p.getNombre() + "," + p.getCorreo() + "," +
                        p.getEspecialidad() + "," + p.getMateriaAsignada());
            }
        } catch (IOException ignored) {}
    }

    private void cargarArchivosTxt() {
        File fAlumnos = new File(archivoAlumnos);
        if (fAlumnos.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fAlumnos))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] p = linea.split(",");
                    if (p.length >= 7) {
                        ProgramaAcademico prog = p[3].equals("Curso") ? new Curso() :
                                (p[3].equals("Diplomado") ? new Diplomado() : new Bootcamp());
                        Alumno a = new Alumno(p[0], p[1], p[2], prog);
                        for (int i = 4; i <= 6; i++) {
                            float val = Float.parseFloat(p[i]);
                            if (val > 0) a.agregarNota(val);
                        }
                        alumnos.add(a);
                    }
                }
            } catch (Exception ignored) {}
        }

        File fProf = new File(archivoProfesores);
        if (fProf.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fProf))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] p = linea.split(",");
                    if (p.length >= 5) {
                        profesores.add(new Profesor(p[0], p[1], p[2], p[3], p[4]));
                    }
                }
            } catch (Exception ignored) {}
        }
    }
}

// ==========================================
// CLASE PRINCIPAL
// ==========================================
public class Main {
    public static void main(String[] args) {
        SistemaGestionAcademica sistema = new SistemaGestionAcademica();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=========================================");
            System.out.println("     SGA-DO: SISTEMA DIPLOMADOSONLINE    ");
            System.out.println("=========================================");
            System.out.println("1. Registrar Alumno");
            System.out.println("2. Registrar Profesor");
            System.out.println("3. Registrar Notas a un Alumno");
            System.out.println("4. Deshacer Último Registro de Nota");
            System.out.println("5. Generar Cola de Certificados");
            System.out.println("6. Mostrar Reporte General");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción (1-7): ");

            String opcion = sc.nextLine().trim();

            if (opcion.equals("1")) sistema.registrarAlumno(sc);
            else if (opcion.equals("2")) sistema.registrarProfesor(sc);
            else if (opcion.equals("3")) sistema.registrarNota(sc);
            else if (opcion.equals("4")) sistema.deshacerUltimaNota();
            else if (opcion.equals("5")) sistema.generarColaCertificados();
            else if (opcion.equals("6")) sistema.mostrarReporteGeneral();
            else if (opcion.equals("7")) {
                System.out.println("\n💾 Guardando datos y cerrando sistema de forma segura. ¡Hasta luego!");
                break;
            } else {
                System.out.println("❌ Opción inválida. Intente de nuevo.");
            }
        }
    }
}