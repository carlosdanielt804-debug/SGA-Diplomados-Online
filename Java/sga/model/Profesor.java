package sga.model;

public class Profesor extends Persona {
    private String especialidad;
    private String materiaAsignada;

    public Profesor(String cedula, String nombre, String correo, String especialidad, String materiaAsignada) {
        super(cedula, nombre, correo);
        this.especialidad = especialidad;
        this.materiaAsignada = materiaAsignada;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getMateriaAsignada() {
        return materiaAsignada;
    }

    @Override
    public String mostrarInformacion() {
        return String.format("[Profesor] ID: %s | Nombre: %s | Correo: %s | Esp: %s | Materia: %s",
                cedula, nombre, correo, especialidad, materiaAsignada);
    }
}