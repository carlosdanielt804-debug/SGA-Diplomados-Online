#include "Curso.h"

Curso::Curso() : ProgramaAcademico("Curso") {}

bool Curso::evaluarAprobacion(const std::vector<float>& notas) const {
    if (notas.empty()) {
        return false;
    }
    float suma = 0;
    for (float n : notas) {
        suma += n;
    }
    return (suma / notas.size()) >= 10.0f;
}