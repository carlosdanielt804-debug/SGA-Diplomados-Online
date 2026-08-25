#ifndef PROGRAMAACADEMICO_H
#define PROGRAMAACADEMICO_H

#include <string>
#include <vector>

class ProgramaAcademico {
protected:
    std::string nombrePrograma;

public:
    ProgramaAcademico(const std::string& nombrePrograma);
    virtual ~ProgramaAcademico() {}

    std::string getNombrePrograma() const;

    // Método virtual puro (abstracto) - POLIMORFISMO
    virtual bool evaluarAprobacion(const std::vector<float>& notas) const = 0;
};

#endif