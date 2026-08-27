#ifndef UTILS_H
#define UTILS_H

#include <string>
#include <algorithm>

class Utils {
public:
    // Limpia la cédula eliminando puntos, guiones y espacios
    static std::string limpiarCedula(const std::string& cedula) {
        std::string limpia = cedula;
        // Eliminar puntos, guiones y espacios
        limpia.erase(std::remove(limpia.begin(), limpia.end(), '.'), limpia.end());
        limpia.erase(std::remove(limpia.begin(), limpia.end(), '-'), limpia.end());
        limpia.erase(std::remove(limpia.begin(), limpia.end(), ' '), limpia.end());
        return limpia;
    }

    // Compara dos cédulas ignorando puntos y guiones
    static bool compararCedula(const std::string& c1, const std::string& c2) {
        return limpiarCedula(c1) == limpiarCedula(c2);
    }
};

#endif