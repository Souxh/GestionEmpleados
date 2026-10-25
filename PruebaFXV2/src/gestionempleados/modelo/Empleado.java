package gestionempleados.modelo;

import java.time.LocalDate;

public class Empleado extends Persona {

    private int idEmpleado;
    private LocalDate fechaIngreso;
    private double salario;
    private String estado;
    private Rol rol;
    private Turno turno;

    public Empleado() {
        super();
    }

    public Empleado(int idEmpleado, int idPersona, String nombre,
                    String apellidos, String telefono, String email,
                    String direccion, LocalDate fechaIngreso,
                    double salario, String estado,
                    Rol rol, Turno turno) {

        super(idPersona, nombre, apellidos, telefono, email, direccion);

        this.idEmpleado = idEmpleado;
        this.fechaIngreso = fechaIngreso;
        this.salario = salario;
        this.estado = estado;
        this.rol = rol;
        this.turno = turno;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
}