
package gestionempleados.controlador;

import gestionempleados.modelo.DatosTemporales;
import gestionempleados.modelo.Empleado;
import gestionempleados.modelo.Rol;
import gestionempleados.modelo.Turno;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NuevoEmpleadoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDireccion;
    @FXML private DatePicker fechaIngreso;
    @FXML private TextField txtSalario;
    @FXML private ComboBox<Rol> cmbRol;
    @FXML private ComboBox<Turno> cmbTurno;
    @FXML private ComboBox<String> cmbEstado;

    private Empleado empleadoEditar;

    @FXML
    public void initialize() {
        DatosTemporales.inicializar();

        cmbRol.setItems(DatosTemporales.getRoles());
        cmbTurno.setItems(DatosTemporales.getTurnos());

        cmbEstado.setItems(FXCollections.observableArrayList(
            "Activo", "Inactivo", "Vacaciones", "Suspendido"
        ));

        cmbEstado.setValue("Activo");
        fechaIngreso.setValue(LocalDate.now());
    }

    public void setEmpleadoEditar(Empleado empleado) {
        empleadoEditar = empleado;

        txtNombre.setText(empleado.getNombre());
        txtApellidos.setText(empleado.getApellidos());
        txtTelefono.setText(empleado.getTelefono());
        txtEmail.setText(empleado.getEmail());
        txtDireccion.setText(empleado.getDireccion());
        fechaIngreso.setValue(empleado.getFechaIngreso());
        txtSalario.setText(String.valueOf(empleado.getSalario()));

        cmbRol.setValue(empleado.getRol());
        cmbTurno.setValue(empleado.getTurno());
        cmbEstado.setValue(empleado.getEstado());
    }

    @FXML
    private void registrarEmpleado() {

        if (!validarCampos()) {
            return;
        }

        try {
            double salario = Double.parseDouble(txtSalario.getText());

            if (empleadoEditar == null) {
                crearEmpleado(salario);
            } else {
                modificarEmpleado(salario);
            }

        } catch (Exception e) {
            mostrarMensaje(
                Alert.AlertType.ERROR,
                "Error",
                "No fue posible guardar los datos del empleado."
            );
        }
    }

    private void crearEmpleado(double salario) {

        Empleado empleado = new Empleado(
            0,
            0,
            txtNombre.getText().trim(),
            txtApellidos.getText().trim(),
            txtTelefono.getText().trim(),
            txtEmail.getText().trim(),
            txtDireccion.getText().trim(),
            fechaIngreso.getValue(),
            salario,
            cmbEstado.getValue(),
            cmbRol.getValue(),
            cmbTurno.getValue()
        );

        DatosTemporales.agregarEmpleado(empleado);

        mostrarMensaje(
            Alert.AlertType.INFORMATION,
            "Registro correcto",
            "El empleado fue registrado correctamente."
        );

        cerrarVentana();
    }

    private void modificarEmpleado(double salario) {

        empleadoEditar.setNombre(txtNombre.getText().trim());
        empleadoEditar.setApellidos(txtApellidos.getText().trim());
        empleadoEditar.setTelefono(txtTelefono.getText().trim());
        empleadoEditar.setEmail(txtEmail.getText().trim());
        empleadoEditar.setDireccion(txtDireccion.getText().trim());
        empleadoEditar.setFechaIngreso(fechaIngreso.getValue());
        empleadoEditar.setSalario(salario);
        empleadoEditar.setEstado(cmbEstado.getValue());
        empleadoEditar.setRol(cmbRol.getValue());
        empleadoEditar.setTurno(cmbTurno.getValue());

        DatosTemporales.registrarHistorial(
            empleadoEditar,
            "Empleado modificado",
            "Se modificaron los datos del empleado."
        );

        mostrarMensaje(
            Alert.AlertType.INFORMATION,
            "Modificación correcta",
            "Los datos fueron actualizados correctamente."
        );

        cerrarVentana();
    }

    private boolean validarCampos() {

        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String salarioTexto = txtSalario.getText().trim();

        if (nombre.isEmpty() || apellidos.isEmpty()
                || telefono.isEmpty() || email.isEmpty()
                || salarioTexto.isEmpty()
                || txtDireccion.getText().trim().isEmpty()) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Campos obligatorios",
                "Completa todos los campos."
            );
            return false;
        }

        // Validar nombre
        if (!nombre.matches("[\\p{L} ]+")) {
            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Nombre inválido",
                "El nombre solo debe contener letras y espacios."
            );
            txtNombre.requestFocus();
            return false;
        }

        // Validar apellidos
        if (!apellidos.matches("[\\p{L} ]+")) {
            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Apellidos inválidos",
                "Los apellidos solo deben contener letras y espacios."
            );
            txtApellidos.requestFocus();
            return false;
        }

        // Validar correo
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Correo inválido",
                "Ingresa un correo electrónico válido."
            );
            txtEmail.requestFocus();
            return false;
        }

        // Validar teléfono: 10 dígitos
        if (!telefono.matches("[0-9]{10}")) {
            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Teléfono inválido",
                "El teléfono debe contener exactamente 10 dígitos."
            );
            txtTelefono.requestFocus();
            return false;
        }

        // Validar fecha
        if (fechaIngreso.getValue() == null) {
            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Fecha requerida",
                "Selecciona la fecha de ingreso."
            );
            return false;
        }

        if (fechaIngreso.getValue().isAfter(LocalDate.now())) {
            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Fecha inválida",
                "La fecha de ingreso no puede ser futura."
            );
            return false;
        }

        // Validar salario
        try {
            double salario = Double.parseDouble(salarioTexto);

            if (salario < 0 || Double.isInfinite(salario)
                    || Double.isNaN(salario)) {
                mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Salario inválido",
                    "El salario debe ser un número mayor o igual a cero."
                );
                txtSalario.requestFocus();
                return false;
            }

        } catch (NumberFormatException e) {
            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Salario inválido",
                "Ingresa un salario numérico válido."
            );
            txtSalario.requestFocus();
            return false;
        }

        if (cmbRol.getValue() == null
                || cmbTurno.getValue() == null
                || cmbEstado.getValue() == null) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Datos incompletos",
                "Selecciona un rol, un turno y un estado."
            );
            return false;
        }

        return true;
    }

    @FXML
    private void cerrarVentana() {
        Stage ventana = (Stage) txtNombre.getScene().getWindow();
        ventana.close();
    }

    private void mostrarMensaje(
            Alert.AlertType tipo, String titulo, String mensaje) {

        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
   
}
