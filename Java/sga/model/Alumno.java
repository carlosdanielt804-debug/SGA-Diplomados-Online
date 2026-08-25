package sga.model;

import java.util.ArrayList;
import java.util.List;

public class Alumno extends Persona {
    private List<Float> notas;
    private ProgramaAcademico programa;

    public Alumno(String cedula, String nombre, String correo, ProgramaAcademico programa) {
        super(cedula, nombre, correo);
        this.programa = programa;
        this.notas = new ArrayList<>();
    }

    public boolean agregarNota(float nota) {
        if (notas.size() < 3) {
            notas.add(nota);
            return true;
        }
        return false;
    }

    public Float deshacerNota() {
        if (!notas.isEmpty()) {
            return notas.remove(notas.size() - 1);
        }
        return null;
    }

    public List<Float> getNotas() {
        return notas;
    }

    public ProgramaAcademico getPrograma() {
        return programa;
    }

    public float obtenerPromedio() {
        if (notas.isEmpty()) {
            return 0.0f;
        }
        float suma = 0;
        for (float n : notas) {
            suma += n;
        }
        return suma / notas.size();
    }

    public boolean estaAprobado() {
        return programa.evaluarAprobacion(notas);
    }

    @Override
    public String mostrarInformacion() {
        String estatus = estaAprobado() ? "APROBADO" : "REPROBADO";
        String notasStr = notas.isEmpty() ? "Sin notas" : notas.toString();
        return String.format("[Alumno] ID: %s | Nombre: %s | Correo: %s | Prog: %s | Notas: %s | Prom: %.1f | Estatus: %s",
                cedula, nombre, correo, programa.getNombrePrograma(), notasStr, obtenerPromedio(), estatus);
    }
}