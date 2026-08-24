package sga.model;

import java.util.List;

public class Bootcamp extends ProgramaAcademico {

    public Bootcamp() {
        super("Bootcamp");
    }

    @Override
    public boolean evaluarAprobacion(List<Double> notas) {
        if (notas == null || notas.isEmpty()) {
            return false;
        }
        // Exige estrictamente que ninguna nota individual sea menor a 14/20
        for (Double nota : notas) {
            if (nota < 14.0) {
                return false;
            }
        }
        return true;
    }
}