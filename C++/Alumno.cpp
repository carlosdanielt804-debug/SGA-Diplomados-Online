#include "Alumno.h"
#include <sstream>
#include <iomanip>

Alumno::Alumno(const std::string& cedula, const std::string& nombre, 
               const std::string& correo, ProgramaAcademico* programa)
    : Persona(cedula, nombre, correo), programa(programa) {}

Alumno::~Alumno() {
    // No eliminamos programa aquí porque lo gestiona SistemaGestionAcademica
}

bool Alumno::agregarNota(float nota) {
    if (notas.size() < 3) {
        notas.push_back(nota);
        return true;
    }
    return false;
}

float Alumno::deshacerNota() {
    if (!notas.empty()) {
        float ultima = notas.back();
        notas.pop_back();
        return ultima;
    }
    return -1.0f;
}

std::vector<float> Alumno::getNotas() const {
    return notas;
}

ProgramaAcademico* Alumno::getPrograma() const {
    return programa;
}

float Alumno::obtenerPromedio() const {
    if (notas.empty()) {
        return 0.0f;
    }
    float suma = 0;
    for (float n : notas) {
        suma += n;
    }
    return suma / notas.size();
}

bool Alumno::estaAprobado() const {
    return programa->evaluarAprobacion(notas);
}

std::string Alumno::mostrarInformacion() const {
    std::ostringstream oss;
    std::string estatus = estaAprobado() ? "APROBADO" : "REPROBADO";
    
    oss << "[Alumno] ID: " << cedula 
        << " | Nombre: " << nombre 
        << " | Correo: " << correo
        << " | Prog: " << programa->getNombrePrograma()
        << " | Prom: " << std::fixed << std::setprecision(1) << obtenerPromedio()
        << " | Estatus: " << estatus;
    
    return oss.str();
}