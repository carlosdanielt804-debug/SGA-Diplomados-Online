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
    std::vector<Alumno*> alumnos;
    std::vector<Profesor*> profesores;
    std::stack<AccionNota*> pilaComandos;
    std::queue<Alumno*> colaCertificados;

    const std::string archivoAlumnos = "Data/alumnos.txt";
    const std::string archivoProfesores = "Data/profesores.txt";
    const std::string archivoCertificados = "Data/certificados_pendientes.txt";

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