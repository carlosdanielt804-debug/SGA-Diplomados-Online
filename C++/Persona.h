#ifndef PERSONA_H
#define PERSONA_H

#include <string>

class Persona {
protected:
    std::string cedula;
    std::string nombre;
    std::string correo;

public:
    Persona(const std::string& cedula, const std::string& nombre, const std::string& correo);
    virtual ~Persona() {}

    std::string getCedula() const;
    std::string getNombre() const;
    std::string getCorreo() const;

    // Método virtual puro (abstracto)
    virtual std::string mostrarInformacion() const = 0;
};

#endif