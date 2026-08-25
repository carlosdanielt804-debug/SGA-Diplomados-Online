package sga.model;

import java.util.List;

public class Curso extends ProgramaAcademico {

    public Curso() {
        super("Curso");
    }

    @Override
    public boolean evaluarAprobacion(List<Float> notas) {
        if (notas == null || notas.isEmpty()) {
            return false;
        }
        float suma = 0;
        for (float n : notas) {
            suma += n;
        }
        return (suma / notas.size()) >= 10.0f;
    }
}