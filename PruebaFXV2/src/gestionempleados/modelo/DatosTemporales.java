package gestionempleados.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DatosTemporales {

    private static final ObservableList<Empleado> empleados = FXCollections.observableArrayList();
    private static final ObservableList<HistorialAccion> historial = FXCollections.observableArrayList();
    private static final ObservableList<Rol> roles = FXCollections.observableArrayList();
    private static final ObservableList<Turno> turnos = FXCollections.observableArrayList();

    private static int siguienteEmpleado = 1;
    private static int siguientePersona = 1;
    private static int siguienteHistorial = 1;

    public static void inicializar() {
        if (!roles.isEmpty()) return;

        cargarRoles();
        cargarTurnos();
        cargarEmpleadosDemo();
    }

    private static void cargarRoles() {
        roles.add(new Rol(1, "Administrador", "Control general del sistema"));
        roles.add(new Rol(2, "Supervisor", "Supervisión de empleados y operaciones"));
        roles.add(new Rol(3, "Cajero", "Operaciones de caja y ventas"));
        roles.add(new Rol(4, "Vendedor", "Atención al cliente y ventas"));
    }

    private static void cargarTurnos() {
        turnos.add(new Turno(1, "Matutino", LocalTime.of(8, 0), LocalTime.of(15, 0)));
        turnos.add(new Turno(2, "Vespertino", LocalTime.of(15, 0), LocalTime.of(22, 0)));
        turnos.add(new Turno(3, "Completo", LocalTime.of(8, 0), LocalTime.of(17, 0)));
    }

    private static void cargarEmpleadosDemo() {
        Rol cajero = obtenerRol("Cajero");
        Rol supervisor = obtenerRol("Supervisor");

        Turno matutino = obtenerTurno("Matutino");
        Turno vespertino = obtenerTurno("Vespertino");
        Turno completo = obtenerTurno("Completo");

        Empleado maria = new Empleado(
            siguienteEmpleado++, siguientePersona++, "María", "Ramírez",
            "5551234567", "maria@nova.com", "Dirección de María",
            LocalDate.now(), 5500, "Activo", cajero, matutino
        );

        Empleado jose = new Empleado(
            siguienteEmpleado++, siguientePersona++, "José", "López",
            "5551234568", "jose@nova.com", "Dirección de José",
            LocalDate.now(), 3599, "Activo", cajero, vespertino
        );

        Empleado ana = new Empleado(
            siguienteEmpleado++, siguientePersona++, "Ana", "González",
            "5551234569", "ana@nova.com", "Dirección de Ana",
            LocalDate.now(), 6500, "Activo", supervisor, completo
        );

        empleados.addAll(maria, jose, ana);

        registrarHistorial(maria, "Venta", "Proceso venta #578");
        registrarHistorial(ana, "Edición", "Modificó precio de la Leche 1L Lala");
    }

    public static void agregarEmpleado(Empleado empleado) {
        if (empleado.getIdEmpleado() == 0) {
            empleado.setIdEmpleado(siguienteEmpleado++);
        }
        if (empleado.getIdPersona() == 0) {
            empleado.setIdPersona(siguientePersona++);
        }

        empleados.add(empleado);
        registrarHistorial(empleado, "Empleado creado", "Se registró el empleado " + empleado.getNombreCompleto());
    }

    public static void eliminarEmpleado(Empleado empleado) {
        if (empleado == null) return;

        empleados.remove(empleado);
        registrarHistorial(empleado, "Empleado eliminado", "Se eliminó el empleado " + empleado.getNombreCompleto());
    }

    public static void registrarHistorial(Empleado empleado, String accion, String descripcion) {
        HistorialAccion registro = new HistorialAccion(siguienteHistorial++, empleado, accion, descripcion, LocalDateTime.now());
        historial.add(0, registro);
    }

    public static ObservableList<Empleado> getEmpleados() { return empleados; }
    public static ObservableList<HistorialAccion> getHistorial() { return historial; }
    public static ObservableList<Rol> getRoles() { return roles; }
    public static ObservableList<Turno> getTurnos() { return turnos; }

    public static Rol obtenerRol(String nombre) {
        for (Rol rol : roles) {
            if (rol.getNombre().equalsIgnoreCase(nombre)) return rol;
        }
        return null;
    }

    public static Turno obtenerTurno(String nombre) {
        for (Turno turno : turnos) {
            if (turno.getNombre().equalsIgnoreCase(nombre)) return turno;
        }
        return null;
    }
}