package sga.model;

import java.util.List;

public abstract class ProgramaAcademico {
    protected String nombrePrograma;

    public ProgramaAcademico(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public abstract boolean evaluarAprobacion(List<Float> notas);
}