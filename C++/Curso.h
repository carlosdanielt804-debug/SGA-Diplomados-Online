#ifndef CURSO_H
#define CURSO_H

#include "ProgramaAcademico.h"

class Curso : public ProgramaAcademico {
public:
    Curso();
    virtual ~Curso() {}

    bool evaluarAprobacion(const std::vector<float>& notas) const override;
};

#endif
