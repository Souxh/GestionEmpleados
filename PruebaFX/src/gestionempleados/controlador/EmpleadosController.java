package gestionempleados.controlador;

import gestionempleados.modelo.DatosTemporales;
import gestionempleados.modelo.Empleado;
import gestionempleados.modelo.Rol;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class EmpleadosController {

    @FXML
    private TableView<Empleado> tablaEmpleados;

    @FXML
    private TableColumn<Empleado, String> colId;

    @FXML
    private TableColumn<Empleado, String> colNombre;

    @FXML
    private TableColumn<Empleado, String> colTelefono;

    @FXML
    private TableColumn<Empleado, String> colEmail;

    @FXML
    private TableColumn<Empleado, String> colRol;

    @FXML
    private TableColumn<Empleado, String> colTurno;

    @FXML
    private TableColumn<Empleado, String> colSalario;

    @FXML
    private TableColumn<Empleado, String> colEstado;

    @FXML
    private TableColumn<Empleado, String> colFechaIngreso;

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> cmbEstadoFiltro;

    @FXML
    private ComboBox<Rol> cmbRolFiltro;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblActivos;

    @FXML
    private Label lblNoActivos;

    private FilteredList<Empleado> empleadosFiltrados;

    
    // INICIALIZAR
    

    @FXML
    public void initialize() {

        DatosTemporales.inicializar();

        configurarTabla();

        configurarFiltros();

        empleadosFiltrados =
                new FilteredList<>(
                        DatosTemporales.getEmpleados(),
                        empleado -> true
                );

        tablaEmpleados.setItems(
                empleadosFiltrados
        );

        configurarDobleClick();

        actualizarContadores();

        aplicarFiltros();
    }

    
    // CONFIGURAR TABLA
    

    private void configurarTabla() {

        colId.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        String.valueOf(
                                datos.getValue()
                                        .getIdEmpleado()
                        )
                )
        );

        colNombre.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue()
                                .getNombreCompleto()
                )
        );

        colTelefono.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue()
                                .getTelefono()
                )
        );

        colEmail.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue()
                                .getEmail()
                )
        );

        colRol.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getRol() != null
                                ? datos.getValue()
                                        .getRol()
                                        .getNombre()
                                : ""
                )
        );

        colTurno.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getTurno() != null
                                ? datos.getValue()
                                        .getTurno()
                                        .getNombre()
                                : ""
                )
        );

        colSalario.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        String.format(
                                "$%,.2f",
                                datos.getValue()
                                        .getSalario()
                        )
                )
        );

        colEstado.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue()
                                .getEstado()
                )
        );

        colFechaIngreso.setCellValueFactory(datos -> {

            if (datos.getValue()
                    .getFechaIngreso() == null) {

                return new SimpleStringProperty("");
            }

            DateTimeFormatter formato =
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy"
                    );

            return new SimpleStringProperty(
                    datos.getValue()
                            .getFechaIngreso()
                            .format(formato)
            );
        });
    }

    
    // CONFIGURAR FILTROS
    

    private void configurarFiltros() {

        cmbEstadoFiltro.setItems(
                FXCollections.observableArrayList(
                        "Todos",
                        "Activo",
                        "Inactivo",
                        "Vacaciones",
                        "Suspendido"
                )
        );

        cmbEstadoFiltro.setValue(
                "Todos"
        );

        cmbRolFiltro.setItems(
                DatosTemporales.getRoles()
        );

        txtBuscar.textProperty()
                .addListener(
                        (observable, anterior, nuevo) ->
                                aplicarFiltros()
                );

        cmbEstadoFiltro.valueProperty()
                .addListener(
                        (observable, anterior, nuevo) ->
                                aplicarFiltros()
                );

        cmbRolFiltro.valueProperty()
                .addListener(
                        (observable, anterior, nuevo) ->
                                aplicarFiltros()
                );
    }

    
    // APLICAR FILTROS
   

    private void aplicarFiltros() {

        if (empleadosFiltrados == null) {
            return;
        }

        String texto =
                txtBuscar.getText()
                        .trim()
                        .toLowerCase();

        String estado =
                cmbEstadoFiltro.getValue();

        Rol rol =
                cmbRolFiltro.getValue();

        empleadosFiltrados.setPredicate(
                empleado -> {

                    if (empleado == null) {
                        return false;
                    }

                    boolean coincideTexto = true;

                    if (!texto.isEmpty()) {

                        String nombre =
                                empleado
                                        .getNombreCompleto()
                                        .toLowerCase();

                        String telefono =
                                empleado.getTelefono() != null
                                        ? empleado
                                                .getTelefono()
                                                .toLowerCase()
                                        : "";

                        String email =
                                empleado.getEmail() != null
                                        ? empleado
                                                .getEmail()
                                                .toLowerCase()
                                        : "";

                        coincideTexto =
                                nombre.contains(texto)
                                || telefono.contains(texto)
                                || email.contains(texto);
                    }

                    boolean coincideEstado =
                            estado == null
                            || estado.equals("Todos")
                            || (
                                empleado.getEstado() != null
                                && empleado.getEstado()
                                        .equalsIgnoreCase(estado)
                            );

                    boolean coincideRol =
                            rol == null
                            || (
                                empleado.getRol() != null
                                && empleado.getRol()
                                        .getIdRol()
                                        == rol.getIdRol()
                            );

                    return coincideTexto
                            && coincideEstado
                            && coincideRol;
                }
        );
    }

    
    // DOBLE CLIC
    

    private void configurarDobleClick() {

        tablaEmpleados.setOnMouseClicked(
                event -> {

                    if (event.getClickCount() == 2) {

                        Empleado seleccionado =
                                tablaEmpleados
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (seleccionado != null) {

                            abrirModificarEmpleado(
                                    seleccionado
                            );
                        }
                    }
                }
        );
    }

    
    // NUEVO EMPLEADO
    

    @FXML
    private void abrirNuevoEmpleado() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/gestionempleados/NuevoEmpleado.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            Stage ventana =
                    new Stage();

            ventana.setTitle(
                    "Nuevo Empleado"
            );

            ventana.initModality(
                    Modality.APPLICATION_MODAL
            );

            ventana.setResizable(false);

            ventana.setScene(
                    new Scene(root)
            );

            ventana.showAndWait();

            actualizar();

        } catch (IOException e) {

            e.printStackTrace();

            mostrarError(
                    "No se pudo abrir la ventana de Nuevo Empleado."
            );
        }
    }

   
    // MODIFICAR EMPLEADO
    

    @FXML
    private void modificarEmpleado() {

        Empleado seleccionado =
                tablaEmpleados
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarAdvertencia(
                    "Selecciona un empleado para modificarlo."
            );

            return;
        }

        abrirModificarEmpleado(
                seleccionado
        );
    }

    
    // ABRIR MODIFICACIÓN
    

    private void abrirModificarEmpleado(
            Empleado empleado
    ) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/gestionempleados/NuevoEmpleado.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            NuevoEmpleadoController controller =
                    loader.getController();

            controller.setEmpleadoEditar(
                    empleado
            );

            Stage ventana =
                    new Stage();

            ventana.setTitle(
                    "Modificar Empleado"
            );

            ventana.initModality(
                    Modality.APPLICATION_MODAL
            );

            ventana.setResizable(false);

            ventana.setScene(
                    new Scene(root)
            );

            ventana.showAndWait();

            actualizar();

        } catch (IOException e) {

            e.printStackTrace();

            mostrarError(
                    "No se pudo abrir la ventana de modificación."
            );
        }
    }

    
    // ELIMINAR EMPLEADO
    

    @FXML
    private void eliminarEmpleado() {

        Empleado seleccionado =
                tablaEmpleados
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarAdvertencia(
                    "Selecciona un empleado."
            );

            return;
        }

        Alert confirmacion =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmacion.setTitle(
                "Eliminar empleado"
        );

        confirmacion.setHeaderText(
                "Eliminar empleado"
        );

        confirmacion.setContentText(
                "¿Deseas eliminar a "
                + seleccionado.getNombreCompleto()
                + "?"
        );

        confirmacion
                .showAndWait()
                .ifPresent(respuesta -> {

                    if (respuesta == ButtonType.OK) {

                        DatosTemporales
                                .eliminarEmpleado(
                                        seleccionado
                                );

                        actualizar();
                    }
                });
    }

    
    // LIMPIAR FILTROS
    

    @FXML
    private void limpiarFiltros() {

        txtBuscar.clear();

        cmbEstadoFiltro.setValue(
                "Todos"
        );

        cmbRolFiltro.setValue(
                null
        );

        aplicarFiltros();
    }

    
    // ACTUALIZAR
    

    private void actualizar() {

        actualizarContadores();

        aplicarFiltros();

        tablaEmpleados.refresh();
    }

    
    // ACTUALIZAR CONTADORES
    

    private void actualizarContadores() {

        int total =
                DatosTemporales
                        .getEmpleados()
                        .size();

        long activos =
                DatosTemporales
                        .getEmpleados()
                        .stream()
                        .filter(empleado ->
                                empleado != null
                                && empleado.getEstado() != null
                                && empleado.getEstado()
                                        .equalsIgnoreCase("Activo")
                        )
                        .count();

        int noActivos =
                total - (int) activos;

        lblTotal.setText(
                String.valueOf(total)
        );

        lblActivos.setText(
                String.valueOf(activos)
        );

        lblNoActivos.setText(
                String.valueOf(noActivos)
        );
    }

    
    // ADVERTENCIA
    

    private void mostrarAdvertencia(
            String mensaje
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alerta.setTitle(
                "Advertencia"
        );

        alerta.setHeaderText(
                null
        );

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }

    
    // ERROR
    

    private void mostrarError(
            String mensaje
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle(
                "Error"
        );

        alerta.setHeaderText(
                null
        );

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }
}

