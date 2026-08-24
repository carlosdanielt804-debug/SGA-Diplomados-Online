package sga.model;

import java.util.List;

public class Curso extends ProgramaAcademico {

    public Curso() {
        super("Curso");
    }

    @Override
    public boolean evaluarAprobacion(List<Double> notas) {
        if (notas == null || notas.isEmpty()) {
            return false;
        }
        double promedio = notas.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return promedio >= 10.0;
    }
}