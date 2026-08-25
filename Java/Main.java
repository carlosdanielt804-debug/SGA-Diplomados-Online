import sga.service.SistemaGestionAcademica;
import java.util.Scanner;

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

            if (opcion.equals("1")) {
                sistema.registrarAlumno(sc);
            } else if (opcion.equals("2")) {
                sistema.registrarProfesor(sc);
            } else if (opcion.equals("3")) {
                sistema.registrarNota(sc);
            } else if (opcion.equals("4")) {
                sistema.deshacerUltimaNota();
            } else if (opcion.equals("5")) {
                sistema.generarColaCertificados();
            } else if (opcion.equals("6")) {
                sistema.mostrarReporteGeneral();
            } else if (opcion.equals("7")) {
                System.out.println("\n💾 Guardando datos y cerrando sistema de forma segura. ¡Hasta luego!");
                break;
            } else {
                System.out.println("❌ Opción inválida. Intente de nuevo.");
            }
        }
        sc.close();
    }
}