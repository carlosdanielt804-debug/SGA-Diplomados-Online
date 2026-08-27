#include "ProgramaAcademico.h"

ProgramaAcademico::ProgramaAcademico(const std::string& nombrePrograma)
    : nombrePrograma(nombrePrograma) {}

std::string ProgramaAcademico::getNombrePrograma() const {
    return nombrePrograma;
}