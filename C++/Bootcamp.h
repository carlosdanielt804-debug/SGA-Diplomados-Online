#ifndef BOOTCAMP_H
#define BOOTCAMP_H

#include "ProgramaAcademico.h"

class Bootcamp : public ProgramaAcademico {
public:
    Bootcamp();
    virtual ~Bootcamp() {}

    bool evaluarAprobacion(const std::vector<float>& notas) const override;
};

#endif