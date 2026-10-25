package gestionempleados.modelo;

import java.time.LocalTime;

public class Turno {

    private int idTurno;
    private String nombre;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    public Turno() {
    }

    public Turno(int idTurno, String nombre,
                 LocalTime horaEntrada, LocalTime horaSalida) {

        this.idTurno = idTurno;
        this.nombre = nombre;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    @Override
    public String toString() {
        return nombre;
    }
}