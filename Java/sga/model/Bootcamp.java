package sga.model;

import java.util.List;

public class Bootcamp extends ProgramaAcademico {

    public Bootcamp() {
        super("Bootcamp");
    }

    @Override
    public boolean evaluarAprobacion(List<Float> notas) {
        if (notas == null || notas.isEmpty()) {
            return false;
        }
        for (float n : notas) {
            if (n < 14.0f) {
                return false;
            }
        }
        return true;
    }
}