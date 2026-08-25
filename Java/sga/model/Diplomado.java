package sga.model;

import java.util.List;

public class Diplomado extends ProgramaAcademico {

    public Diplomado() {
        super("Diplomado");
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
        return (suma / notas.size()) >= 14.0f;
    }
}