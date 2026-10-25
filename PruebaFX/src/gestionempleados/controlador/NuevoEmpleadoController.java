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

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtDireccion;

    @FXML
    private DatePicker fechaIngreso;

    @FXML
    private TextField txtSalario;

    @FXML
    private ComboBox<Rol> cmbRol;

    @FXML
    private ComboBox<Turno> cmbTurno;

    @FXML
    private ComboBox<String> cmbEstado;

    private Empleado empleadoEditar;

    // inicializar

    @FXML
    public void initialize() {

        DatosTemporales.inicializar();

        cmbRol.setItems(
            DatosTemporales.getRoles()
        );

        cmbTurno.setItems(
            DatosTemporales.getTurnos()
        );

        cmbEstado.setItems(
            FXCollections.observableArrayList(
                "Activo",
                "Inactivo",
                "Vacaciones",
                "Suspendido"
            )
        );

        cmbEstado.setValue("Activo");

        fechaIngreso.setValue(
            LocalDate.now()
        );
    }

    // recibir empleado

    public void setEmpleadoEditar(
            Empleado empleado) {

        this.empleadoEditar = empleado;

        txtNombre.setText(
            empleado.getNombre()
        );

        txtApellidos.setText(
            empleado.getApellidos()
        );

        txtTelefono.setText(
            empleado.getTelefono()
        );

        txtEmail.setText(
            empleado.getEmail()
        );

        txtDireccion.setText(
            empleado.getDireccion()
        );

        fechaIngreso.setValue(
            empleado.getFechaIngreso()
        );

        txtSalario.setText(
            String.valueOf(
                empleado.getSalario()
            )
        );

        cmbRol.setValue(
            empleado.getRol()
        );

        cmbTurno.setValue(
            empleado.getTurno()
        );

        cmbEstado.setValue(
            empleado.getEstado()
        );
    }

    // registrar o modificar

    @FXML
    private void registrarEmpleado() {

        if (!validarCampos()) {
            return;
        }

        try {

            double salario =
                Double.parseDouble(
                    txtSalario
                        .getText()
                        .trim()
                );

            Rol rol =
                cmbRol.getValue();

            Turno turno =
                cmbTurno.getValue();

            if (empleadoEditar != null) {

                modificarEmpleado(
                    salario,
                    rol,
                    turno
                );

            } else {

                crearEmpleado(
                    salario,
                    rol,
                    turno
                );
            }

        } catch (NumberFormatException e) {

            mostrarMensaje(
                Alert.AlertType.ERROR,
                "Salario inválido",
                "El salario debe contener únicamente números."
            );
        }
    }

    // modificar empleado

    private void modificarEmpleado(
            double salario,
            Rol rol,
            Turno turno) {

        empleadoEditar.setNombre(
            txtNombre.getText().trim()
        );

        empleadoEditar.setApellidos(
            txtApellidos.getText().trim()
        );

        empleadoEditar.setTelefono(
            txtTelefono.getText().trim()
        );

        empleadoEditar.setEmail(
            txtEmail.getText().trim()
        );

        empleadoEditar.setDireccion(
            txtDireccion.getText().trim()
        );

        empleadoEditar.setFechaIngreso(
            fechaIngreso.getValue()
        );

        empleadoEditar.setSalario(
            salario
        );

        empleadoEditar.setEstado(
            cmbEstado.getValue()
        );

        empleadoEditar.setRol(
            rol
        );

        empleadoEditar.setTurno(
            turno
        );

        DatosTemporales.registrarHistorial(
            empleadoEditar,
            "Empleado modificado",
            "Se modificó la información del empleado "
            + empleadoEditar.getNombreCompleto()
        );

        mostrarMensaje(
            Alert.AlertType.INFORMATION,
            "Empleado modificado",
            "La información fue actualizada correctamente."
        );

        cerrarVentana();
    }

    // crear empleado

    private void crearEmpleado(
            double salario,
            Rol rol,
            Turno turno) {

        Empleado empleado =
            new Empleado(
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
                rol,
                turno
            );

        DatosTemporales.agregarEmpleado(
            empleado
        );

        mostrarMensaje(
            Alert.AlertType.INFORMATION,
            "Empleado registrado",
            "El empleado "
            + empleado.getNombreCompleto()
            + " fue registrado correctamente."
        );

        cerrarVentana();
    }

    // validar

    private boolean validarCampos() {

        if (txtNombre.getText()
            .trim()
            .isEmpty()) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Campo requerido",
                "Ingresa el nombre."
            );

            txtNombre.requestFocus();

            return false;
        }

        if (txtApellidos.getText()
            .trim()
            .isEmpty()) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Campo requerido",
                "Ingresa los apellidos."
            );

            txtApellidos.requestFocus();

            return false;
        }

        if (txtTelefono.getText()
            .trim()
            .isEmpty()) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Campo requerido",
                "Ingresa el teléfono."
            );

            txtTelefono.requestFocus();

            return false;
        }

        if (txtSalario.getText()
            .trim()
            .isEmpty()) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Campo requerido",
                "Ingresa el salario."
            );

            txtSalario.requestFocus();

            return false;
        }

        if (cmbRol.getValue() == null) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Rol requerido",
                "Selecciona un rol."
            );

            return false;
        }

        if (cmbTurno.getValue() == null) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Turno requerido",
                "Selecciona un turno."
            );

            return false;
        }

        if (cmbEstado.getValue() == null) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Estado requerido",
                "Selecciona el estado."
            );

            return false;
        }

        if (fechaIngreso.getValue() == null) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Fecha requerida",
                "Selecciona la fecha de ingreso."
            );

            return false;
        }

        try {

            double salario =
                Double.parseDouble(
                    txtSalario
                        .getText()
                        .trim()
                );

            if (salario < 0) {

                mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Salario inválido",
                    "El salario no puede ser negativo."
                );

                return false;
            }

        } catch (NumberFormatException e) {

            mostrarMensaje(
                Alert.AlertType.WARNING,
                "Salario inválido",
                "Ingresa un salario válido."
            );

            return false;
        }

        return true;
    }

    // cerrar

    @FXML
    private void cerrarVentana() {

        Stage stage =
            (Stage) txtNombre
                .getScene()
                .getWindow();

        stage.close();
    }

    // mostrar mensaje

    private void mostrarMensaje(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alerta =
            new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}