#include "Persona.h"

Persona::Persona(const std::string& cedula, const std::string& nombre, const std::string& correo)
    : cedula(cedula), nombre(nombre), correo(correo) {}

std::string Persona::getCedula() const {
    return cedula;
}

std::string Persona::getNombre() const {
    return nombre;
}

std::string Persona::getCorreo() const {
    return correo;
}