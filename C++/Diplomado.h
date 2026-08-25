#ifndef DIPLOMADO_H
#define DIPLOMADO_H

#include "ProgramaAcademico.h"

class Diplomado : public ProgramaAcademico {
public:
    Diplomado();
    virtual ~Diplomado() {}

    bool evaluarAprobacion(const std::vector<float>& notas) const override;
};

#endif