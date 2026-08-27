#include "Profesor.h"
#include <sstream>

Profesor::Profesor(const std::string& cedula, const std::string& nombre,
                   const std::string& correo, const std::string& especialidad,
                   const std::string& materiaAsignada)
    : Persona(cedula, nombre, correo), especialidad(especialidad), materiaAsignada(materiaAsignada) {}

std::string Profesor::getEspecialidad() const {
    return especialidad;
}

std::string Profesor::getMateriaAsignada() const {
    return materiaAsignada;
}

std::string Profesor::mostrarInformacion() const {
    std::ostringstream oss;
    oss << "[Profesor] ID: " << cedula
        << " | Nombre: " << nombre
        << " | Correo: " << correo
        << " | Esp: " << especialidad
        << " | Materia: " << materiaAsignada;
    return oss.str();
}