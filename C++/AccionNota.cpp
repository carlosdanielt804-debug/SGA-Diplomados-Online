#include "AccionNota.h"

AccionNota::AccionNota(const std::string& cedula, float nota)
    : cedula(cedula), nota(nota) {}

std::string AccionNota::getCedula() const {
    return cedula;
}

float AccionNota::getNota() const {
    return nota;
}