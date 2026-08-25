#ifndef ALUMNO_H
#define ALUMNO_H

#include "Persona.h"
#include "ProgramaAcademico.h"
#include <vector>

class Alumno : public Persona {
private:
    std::vector<float> notas;
    ProgramaAcademico* programa;  // Puntero para manejo de memoria

public:
    Alumno(const std::string& cedula, const std::string& nombre, 
           const std::string& correo, ProgramaAcademico* programa);
    virtual ~Alumno();

    bool agregarNota(float nota);
    float deshacerNota();
    std::vector<float> getNotas() const;
    ProgramaAcademico* getPrograma() const;
    float obtenerPromedio() const;
    bool estaAprobado() const;

    std::string mostrarInformacion() const override;
};

#endif