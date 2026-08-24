package sga.model;

import java.util.List;

public class Diplomado extends ProgramaAcademico {

    public Diplomado() {
        super("Diplomado");
    }

    @Override
    public boolean evaluarAprobacion(List<Double> notas) {
        if (notas == null || notas.isEmpty()) {
            return false;
        }
        double promedio = notas.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return promedio >= 14.0;
    }
}