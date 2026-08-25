package sga.model;

public class AccionNota {
    private String cedula;
    private float nota;

    public AccionNota(String cedula, float nota) {
        this.cedula = cedula;
        this.nota = nota;
    }

    public String getCedula() {
        return cedula;
    }

    public float getNota() {
        return nota;
    }
}