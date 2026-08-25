#include "Bootcamp.h"

Bootcamp::Bootcamp() : ProgramaAcademico("Bootcamp") {}

bool Bootcamp::evaluarAprobacion(const std::vector<float>& notas) const {
    if (notas.empty()) {
        return false;
    }
    // Regla: Ninguna nota menor a 14
    for (float n : notas) {
        if (n < 14.0f) {
            return false;
        }
    }
    return true;
}