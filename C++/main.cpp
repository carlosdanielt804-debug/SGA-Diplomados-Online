#include "SistemaGestionAcademica.h"
#include <iostream>
#include <limits>

int main() {
    SistemaGestionAcademica sistema;
    std::string opcion;

    while (true) {
        std::cout << "\n=========================================\n";
        std::cout << "     SGA-DO: SISTEMA DIPLOMADOSONLINE    \n";
        std::cout << "=========================================\n";
        std::cout << "1. Registrar Alumno\n";
        std::cout << "2. Registrar Profesor\n";
        std::cout << "3. Registrar Notas a un Alumno\n";
        std::cout << "4. Deshacer Último Registro de Nota\n";
        std::cout << "5. Generar Cola de Certificados\n";
        std::cout << "6. Mostrar Reporte General\n";
        std::cout << "7. Salir\n";
        std::cout << "Seleccione una opción (1-7): ";

        std::getline(std::cin, opcion);

        if (opcion == "1") {
            sistema.registrarAlumno();
        } else if (opcion == "2") {
            sistema.registrarProfesor();
        } else if (opcion == "3") {
            sistema.registrarNota();
        } else if (opcion == "4") {
            sistema.deshacerUltimaNota();
        } else if (opcion == "5") {
            sistema.generarColaCertificados();
        } else if (opcion == "6") {
            sistema.mostrarReporteGeneral();
        } else if (opcion == "7") {
            std::cout << "\n💾 Guardando datos y cerrando sistema de forma segura. ¡Hasta luego!\n";
            break;
        } else {
            std::cout << "❌ Opción inválida. Intente de nuevo.\n";
        }
    }

    return 0;
}