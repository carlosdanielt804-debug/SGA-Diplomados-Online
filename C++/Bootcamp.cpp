#include "Bootcamp.h"

Bootcamp::Bootcamp() : ProgramaAcademico("Bootcamp") {}

bool Bootcamp::evaluarAprobacion(const std::vector<float>& notas) const {
    if (notas.empty()) {
        return false;
    }
    for (float n : notas) {
        if (n < 14.0f) {
            return false;
        }
    }
    return true;
}