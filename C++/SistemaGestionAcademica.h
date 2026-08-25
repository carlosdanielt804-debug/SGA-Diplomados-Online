#ifndef SISTEMAGESTIONACADEMICA_H
#define SISTEMAGESTIONACADEMICA_H

#include <vector>
#include <stack>
#include <queue>
#include <string>
#include "Alumno.h"
#include "Profesor.h"
#include "AccionNota.h"

class SistemaGestionAcademica {
private:
    std::vector<Alumno*> alumnos;        // Punteros para manejo de memoria
    std::vector<Profesor*> profesores;
    std::stack<AccionNota*> pilaComandos; // LIFO
    std::queue<Alumno*> colaCertificados; // FIFO

    const std::string archivoAlumnos;
    const std::string archivoProfesores;
    const std::string archivoCertificados;

    void guardarArchivosTxt();
    void cargarArchivosTxt();
    void limpiarMemoria();

public:
    SistemaGestionAcademica();
    ~SistemaGestionAcademica();

    void registrarAlumno();
    void registrarProfesor();
    void registrarNota();
    void deshacerUltimaNota();
    void generarColaCertificados();
    void mostrarReporteGeneral();
};

#endif