package gestionempleados.modelo;

import java.time.LocalDateTime;

public class HistorialAccion {

    private int idHistorial;
    private Empleado empleado;
    private String accion;
    private String descripcion;
    private LocalDateTime fechaHora;

    public HistorialAccion() {
    }

    public HistorialAccion(int idHistorial, Empleado empleado,
                           String accion, String descripcion,
                           LocalDateTime fechaHora) {

        this.idHistorial = idHistorial;
        this.empleado = empleado;
        this.accion = accion;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}