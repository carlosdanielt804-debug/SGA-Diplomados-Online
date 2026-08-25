#ifndef PROFESOR_H
#define PROFESOR_H

#include "Persona.h"

class Profesor : public Persona {
private:
    std::string especialidad;
    std::string materiaAsignada;

public:
    Profesor(const std::string& cedula, const std::string& nombre, 
             const std::string& correo, const std::string& especialidad, 
             const std::string& materiaAsignada);
    virtual ~Profesor() {}

    std::string getEspecialidad() const;
    std::string getMateriaAsignada() const;

    std::string mostrarInformacion() const override;
};

#endif