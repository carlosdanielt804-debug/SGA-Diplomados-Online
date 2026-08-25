#include "Diplomado.h"

Diplomado::Diplomado() : ProgramaAcademico("Diplomado") {}

bool Diplomado::evaluarAprobacion(const std::vector<float>& notas) const {
    if (notas.empty()) {
        return false;
    }
    float suma = 0;
    for (float n : notas) {
        suma += n;
    }
    return (suma / notas.size()) >= 14.0f;
}