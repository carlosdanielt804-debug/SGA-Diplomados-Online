#include "SistemaGestionAcademica.h"
#include "Curso.h"
#include "Diplomado.h"
#include "Bootcamp.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <algorithm>

SistemaGestionAcademica::SistemaGestionAcademica()
    : archivoAlumnos("alumnos.txt"), 
      archivoProfesores("profesores.txt"),
      archivoCertificados("certificados_pendientes.txt") {
    cargarArchivosTxt();
}

SistemaGestionAcademica::~SistemaGestionAcademica() {
    limpiarMemoria();
}

void SistemaGestionAcademica::limpiarMemoria() {
    // Liberar memoria de alumnos
    for (Alumno* a : alumnos) {
        delete a;
    }
    alumnos.clear();

    // Liberar memoria de profesores
    for (Profesor* p : profesores) {
        delete p;
    }
    profesores.clear();

    // Liberar memoria de la pila
    while (!pilaComandos.empty()) {
        delete pilaComandos.top();
        pilaComandos.pop();
    }

    // Liberar memoria de la cola
    while (!colaCertificados.empty()) {
        colaCertificados.pop();
    }
}

void SistemaGestionAcademica::registrarAlumno() {
    std::cout << "\n--- Registrar Alumno ---\n";
    
    std::string cedula, nombre, correo, opc;
    std::cout << "Cédula/ID: ";
    std::getline(std::cin, cedula);
    std::cout << "Nombre Completo: ";
    std::getline(std::cin, nombre);
    std::cout << "Correo Electrónico: ";
    std::getline(std::cin, correo);
    
    std::cout << "Seleccione Programa: 1. Curso | 2. Diplomado | 3. Bootcamp\n";
    std::cout << "Opción (1-3): ";
    std::getline(std::cin, opc);

    ProgramaAcademico* prog = nullptr;
    if (opc == "1") {
        prog = new Curso();
    } else if (opc == "2") {
        prog = new Diplomado();
    } else if (opc == "3") {
        prog = new Bootcamp();
    } else {
        std::cout << "❌ Opción de programa inválida.\n";
        return;
    }

    Alumno* alumno = new Alumno(cedula, nombre, correo, prog);
    alumnos.push_back(alumno);
    guardarArchivosTxt();
    std::cout << "✅ Alumno registrado exitosamente.\n";
}

void SistemaGestionAcademica::registrarProfesor() {
    std::cout << "\n--- Registrar Profesor ---\n";
    
    std::string cedula, nombre, correo, especialidad, materia;
    std::cout << "Cédula/ID: ";
    std::getline(std::cin, cedula);
    std::cout << "Nombre Completo: ";
    std::getline(std::cin, nombre);
    std::cout << "Correo Electrónico: ";
    std::getline(std::cin, correo);
    std::cout << "Especialidad: ";
    std::getline(std::cin, especialidad);
    std::cout << "Materia Asignada: ";
    std::getline(std::cin, materia);

    Profesor* profesor = new Profesor(cedula, nombre, correo, especialidad, materia);
    profesores.push_back(profesor);
    guardarArchivosTxt();
    std::cout << "✅ Profesor registrado exitosamente.\n";
}

void SistemaGestionAcademica::registrarNota() {
    std::cout << "\n--- Registrar Nota ---\n";
    std::cout << "Ingrese Cédula del Alumno: ";
    std::string cedula;
    std::getline(std::cin, cedula);

    Alumno* alumno = nullptr;
    for (Alumno* a : alumnos) {
        if (a->getCedula() == cedula) {
            alumno = a;
            break;
        }
    }

    if (alumno == nullptr) {
        std::cout << "❌ Alumno no encontrado.\n";
        return;
    }

    try {
        std::cout << "Ingrese nota (0 - 20): ";
        std::string input;
        std::getline(std::cin, input);
        float nota = std::stof(input);

        if (nota < 0 || nota > 20) {
            std::cout << "❌ La nota debe estar entre 0 y 20.\n";
            return;
        }

        if (alumno->agregarNota(nota)) {
            pilaComandos.push(new AccionNota(cedula, nota)); // LIFO
            guardarArchivosTxt();
            std::cout << "✅ Nota registrada exitosamente.\n";
        } else {
            std::cout << "❌ El alumno ya tiene el máximo de 3 notas ingresadas.\n";
        }
    } catch (const std::exception& e) {
        std::cout << "❌ Error: Ingrese un valor numérico válido.\n";
    }
}

void SistemaGestionAcademica::deshacerUltimaNota() {
    std::cout << "\n--- Deshacer Último Registro de Nota (LIFO) ---\n";
    
    if (pilaComandos.empty()) {
        std::cout << "⚠️ No hay acciones recientes para deshacer.\n";
        return;
    }

    AccionNota* ultimaAccion = pilaComandos.top();
    pilaComandos.pop();

    for (Alumno* a : alumnos) {
        if (a->getCedula() == ultimaAccion->getCedula()) {
            a->deshacerNota();
            guardarArchivosTxt();
            std::cout << "✅ Se removió la nota " << ultimaAccion->getNota() 
                      << " del alumno " << a->getNombre() << ".\n";
            delete ultimaAccion;
            return;
        }
    }
    delete ultimaAccion;
}

void SistemaGestionAcademica::generarColaCertificados() {
    std::cout << "\n--- Generar Cola de Certificados (FIFO) ---\n";
    
    // Vaciar cola anterior
    while (!colaCertificados.empty()) {
        colaCertificados.pop();
    }

    // Filtrar alumnos aprobados
    for (Alumno* a : alumnos) {
        if (a->estaAprobado()) {
            colaCertificados.push(a); // FIFO
        }
    }

    std::cout << "Total graduandos procesados en cola: " << colaCertificados.size() << "\n";

    // Exportar a certificados_pendientes.txt
    std::ofstream file(archivoCertificados);
    if (file.is_open()) {
        file << "=========================================\n";
        file << "REPORTE DE CERTIFICADOS PENDIENTES\n";
        file << "=========================================\n";
        file << "Total de graduandos en cola: " << colaCertificados.size() << "\n\n";

        int idx = 1;
        std::queue<Alumno*> tempCola = colaCertificados;
        while (!tempCola.empty()) {
            Alumno* al = tempCola.front();
            tempCola.pop();
            
            file << idx << ". [" << al->getCedula() << "] " << al->getNombre() << "\n";
            file << "   - Programa: " << al->getPrograma()->getNombrePrograma() << "\n";
            file << std::fixed << std::setprecision(1);
            file << "   - Promedio Final: " << al->obtenerPromedio() << "\n";
            file << "   - Estatus: APROBADO\n\n";
            idx++;
        }

        file << "=========================================\n";
        file << "* Fin del reporte - Generado por SGA-DO *\n";
        file.close();
        std::cout << "✅ Archivo '" << archivoCertificados << "' generado con éxito.\n";
    } else {
        std::cout << "❌ Error al escribir archivo de certificados.\n";
    }
}

void SistemaGestionAcademica::mostrarReporteGeneral() {
    std::cout << "\n=========================================\n";
    std::cout << "          REPORTE GENERAL SGA-DO         \n";
    std::cout << "=========================================\n";
    
    std::cout << "\n--- PROFESORES ACTIVOS ---\n";
    if (profesores.empty()) {
        std::cout << "No hay profesores registrados.\n";
    } else {
        for (Profesor* p : profesores) {
            std::cout << p->mostrarInformacion() << "\n";
        }
    }

    std::cout << "\n--- ALUMNOS REGISTRADOS ---\n";
    if (alumnos.empty()) {
        std::cout << "No hay alumnos registrados.\n";
    } else {
        for (Alumno* a : alumnos) {
            std::cout << a->mostrarInformacion() << "\n";
        }
    }
    std::cout << "=========================================\n\n";
}

void SistemaGestionAcademica::guardarArchivosTxt() {
    // Guardar alumnos
    std::ofstream fileAlumnos(archivoAlumnos);
    if (fileAlumnos.is_open()) {
        for (Alumno* a : alumnos) {
            std::vector<float> notas = a->getNotas();
            float n1 = notas.size() > 0 ? notas[0] : 0;
            float n2 = notas.size() > 1 ? notas[1] : 0;
            float n3 = notas.size() > 2 ? notas[2] : 0;
            
            fileAlumnos << a->getCedula() << ","
                        << a->getNombre() << ","
                        << a->getCorreo() << ","
                        << a->getPrograma()->getNombrePrograma() << ","
                        << n1 << "," << n2 << "," << n3 << "\n";
        }
        fileAlumnos.close();
    }

    // Guardar profesores
    std::ofstream fileProfesores(archivoProfesores);
    if (fileProfesores.is_open()) {
        for (Profesor* p : profesores) {
            fileProfesores << p->getCedula() << ","
                           << p->getNombre() << ","
                           << p->getCorreo() << ","
                           << p->getEspecialidad() << ","
                           << p->getMateriaAsignada() << "\n";
        }
        fileProfesores.close();
    }
}

void SistemaGestionAcademica::cargarArchivosTxt() {
    // Cargar alumnos
    std::ifstream fileAlumnos(archivoAlumnos);
    if (fileAlumnos.is_open()) {
        std::string linea;
        while (std::getline(fileAlumnos, linea)) {
            std::stringstream ss(linea);
            std::string cedula, nombre, correo, tipoPrograma;
            float n1, n2, n3;
            
            std::getline(ss, cedula, ',');
            std::getline(ss, nombre, ',');
            std::getline(ss, correo, ',');
            std::getline(ss, tipoPrograma, ',');
            ss >> n1;
            ss.ignore();
            ss >> n2;
            ss.ignore();
            ss >> n3;

            ProgramaAcademico* prog = nullptr;
            if (tipoPrograma == "Curso") {
                prog = new Curso();
            } else if (tipoPrograma == "Diplomado") {
                prog = new Diplomado();
            } else {
                prog = new Bootcamp();
            }

            Alumno* a = new Alumno(cedula, nombre, correo, prog);
            if (n1 > 0) a->agregarNota(n1);
            if (n2 > 0) a->agregarNota(n2);
            if (n3 > 0) a->agregarNota(n3);
            alumnos.push_back(a);
        }
        fileAlumnos.close();
    }

    // Cargar profesores
    std::ifstream fileProfesores(archivoProfesores);
    if (fileProfesores.is_open()) {
        std::string linea;
        while (std::getline(fileProfesores, linea)) {
            std::stringstream ss(linea);
            std::string cedula, nombre, correo, especialidad, materia;
            
            std::getline(ss, cedula, ',');
            std::getline(ss, nombre, ',');
            std::getline(ss, correo, ',');
            std::getline(ss, especialidad, ',');
            std::getline(ss, materia, ',');

            Profesor* p = new Profesor(cedula, nombre, correo, especialidad, materia);
            profesores.push_back(p);
        }
        fileProfesores.close();
    }
}