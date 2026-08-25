#ifndef ACCIONNOTA_H
#define ACCIONNOTA_H

#include <string>

class AccionNota {
private:
    std::string cedula;
    float nota;

public:
    AccionNota(const std::string& cedula, float nota);
    ~AccionNota() {}

    std::string getCedula() const;
    float getNota() const;
};

#endif