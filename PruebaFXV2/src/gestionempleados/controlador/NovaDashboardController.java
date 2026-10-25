package gestionempleados.controlador;

import gestionempleados.modelo.DatosTemporales;
import gestionempleados.modelo.Empleado;
import gestionempleados.modelo.HistorialAccion;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class NovaDashboardController {

    @FXML
    private TableView<Empleado> tablaEmpleados;

    @FXML
    private TableColumn<Empleado, String> colEmpleado;

    @FXML
    private TableColumn<Empleado, String> colRol;

    @FXML
    private TableColumn<Empleado, String> colTurno;

    @FXML
    private TableColumn<Empleado, String> colEstado;

    @FXML
    private TableColumn<Empleado, String> colSalario;

    @FXML
    private TableView<HistorialAccion> tablaAcciones;

    @FXML
    private TableColumn<HistorialAccion, String> colAccionEmpleado;

    @FXML
    private TableColumn<HistorialAccion, String> colAccion;

    @FXML
    private TableColumn<HistorialAccion, String> colDescripcion;

    @FXML
    private TableColumn<HistorialAccion, String> colFecha;

    @FXML
    private Label lblTotalEmpleados;

    @FXML
    private Label lblEmpleadosActivos;

    @FXML
    private Label lblEmpleadosNoActivos;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private void initialize() {

        DatosTemporales.inicializar();

        inicializarTablaEmpleados();
        inicializarTablaAcciones();

        actualizarTablas();
    }

 
    // TABLA DE EMPLEADOS
    

    private void inicializarTablaEmpleados() {

        colEmpleado.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getNombreCompleto()
                )
        );

        colRol.setCellValueFactory(data -> {

            if (data.getValue().getRol() == null) {
                return new SimpleStringProperty("-");
            }

            return new SimpleStringProperty(
                    data.getValue()
                            .getRol()
                            .getNombre()
            );
        });

        colTurno.setCellValueFactory(data -> {

            if (data.getValue().getTurno() == null) {
                return new SimpleStringProperty("-");
            }

            return new SimpleStringProperty(
                    data.getValue()
                            .getTurno()
                            .getNombre()
            );
        });

        colEstado.setCellValueFactory(data -> {

            if (data.getValue().getEstado() == null) {
                return new SimpleStringProperty("-");
            }

            return new SimpleStringProperty(
                    data.getValue().getEstado()
            );
        });

        colSalario.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.format(
                                "$%.2f",
                                data.getValue().getSalario()
                        )
                )
        );
    }

    
    // TABLA DE HISTORIAL
    

    private void inicializarTablaAcciones() {

        colAccionEmpleado.setCellValueFactory(data -> {

            HistorialAccion historial =
                    data.getValue();

            if (historial.getEmpleado() == null) {
                return new SimpleStringProperty("-");
            }

            return new SimpleStringProperty(
                    historial
                            .getEmpleado()
                            .getNombreCompleto()
            );
        });

        colAccion.setCellValueFactory(data -> {

            if (data.getValue().getAccion() == null) {
                return new SimpleStringProperty("-");
            }

            return new SimpleStringProperty(
                    data.getValue().getAccion()
            );
        });

        colDescripcion.setCellValueFactory(data -> {

            if (data.getValue().getDescripcion() == null) {
                return new SimpleStringProperty("-");
            }

            return new SimpleStringProperty(
                    data.getValue().getDescripcion()
            );
        });

        colFecha.setCellValueFactory(data -> {

            if (data.getValue().getFechaHora() == null) {
                return new SimpleStringProperty("-");
            }

            return new SimpleStringProperty(
                    data.getValue()
                            .getFechaHora()
                            .format(formatoFecha)
            );
        });
    }

    
    // ACTUALIZAR TABLAS
    

    public void actualizarTablas() {

        ObservableList<Empleado> empleadosActivos =
                FXCollections.observableArrayList(
                        DatosTemporales
                                .getEmpleados()
                                .stream()
                                .filter(this::estaActivo)
                                .collect(Collectors.toList())
                );

        tablaEmpleados.setItems(
                empleadosActivos
        );

        tablaAcciones.setItems(
                DatosTemporales.getHistorial()
        );

        actualizarContadores();
    }

    
    // COMPROBAR EMPLEADO ACTIVO
    

    private boolean estaActivo(Empleado empleado) {

        if (empleado == null) {
            return false;
        }

        if (empleado.getEstado() == null) {
            return false;
        }

        return empleado
                .getEstado()
                .equalsIgnoreCase("Activo");
    }

   
    // CONTADORES
    

    private void actualizarContadores() {

        int total =
                DatosTemporales
                        .getEmpleados()
                        .size();

        int activos =
                (int) DatosTemporales
                        .getEmpleados()
                        .stream()
                        .filter(this::estaActivo)
                        .count();

        int noActivos =
                total - activos;

        lblTotalEmpleados.setText(
                String.valueOf(total)
        );

        lblEmpleadosActivos.setText(
                String.valueOf(activos)
        );

        lblEmpleadosNoActivos.setText(
                String.valueOf(noActivos)
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

            if (loader.getLocation() == null) {

                throw new IOException(
                        "No se encontró el archivo " +
                        "/gestionempleados/NuevoEmpleado.fxml"
                );
            }

            Parent root =
                    loader.load();

            Stage ventana =
                    new Stage();

            ventana.setTitle(
                    "Nuevo empleado"
            );

            ventana.setScene(
                    new Scene(root)
            );

            ventana.initModality(
                    Modality.APPLICATION_MODAL
            );

            ventana.setResizable(false);

            ventana.showAndWait();

            actualizarTablas();

        } catch (Exception e) {

            e.printStackTrace();

            mostrarError(
                    "No se pudo abrir el formulario",
                    e
            );
        }
    }

    
    // GESTION DE EMPLEADOS
    

    @FXML
    private void abrirGestionEmpleados() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/gestionempleados/Empleados.fxml"
                            )
                    );

            if (loader.getLocation() == null) {

                throw new IOException(
                        "No se encontró el archivo " +
                        "/gestionempleados/Empleados.fxml"
                );
            }

            Parent root =
                    loader.load();

            Stage ventana =
                    new Stage();

            ventana.setTitle(
                    "Gestión de empleados"
            );

            ventana.setScene(
                    new Scene(root)
            );

            ventana.initModality(
                    Modality.APPLICATION_MODAL
            );

            ventana.setResizable(true);

            ventana.showAndWait();

            actualizarTablas();

        } catch (Exception e) {

            e.printStackTrace();

            mostrarError(
                    "No se pudo abrir la gestión de empleados",
                    e
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

            mostrarAviso(
                    "Selecciona un empleado",
                    "Selecciona primero un empleado de la tabla."
            );

            return;
        }

        abrirFormularioEdicion(
                seleccionado
        );
    }

    
    // FORMULARIO DE EDICIÓN
    

    private void abrirFormularioEdicion(
            Empleado empleado
    ) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/gestionempleados/NuevoEmpleado.fxml"
                            )
                    );

            if (loader.getLocation() == null) {

                throw new IOException(
                        "No se encontró el archivo " +
                        "/gestionempleados/NuevoEmpleado.fxml"
                );
            }

            Parent root =
                    loader.load();

            NuevoEmpleadoController controller =
                    loader.getController();

            if (controller == null) {

                throw new IOException(
                        "No se pudo obtener " +
                        "NuevoEmpleadoController."
                );
            }

            controller.setEmpleadoEditar(
                    empleado
            );

            Stage ventana =
                    new Stage();

            ventana.setTitle(
                    "Modificar empleado"
            );

            ventana.setScene(
                    new Scene(root)
            );

            ventana.initModality(
                    Modality.APPLICATION_MODAL
            );

            ventana.setResizable(false);

            ventana.showAndWait();

            actualizarTablas();

        } catch (Exception e) {

            e.printStackTrace();

            mostrarError(
                    "No se pudo abrir la modificación",
                    e
            );
        }
    }

    
    // MENSAJE DE ERROR
    

    private void mostrarError(
            String titulo,
            Exception e
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle(
                "Error"
        );

        alerta.setHeaderText(
                titulo
        );

        String mensaje =
                e.getMessage();

        if (mensaje == null ||
                mensaje.isBlank()) {

            mensaje =
                    e.getClass()
                            .getSimpleName();
        }

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }

    
    // MENSAJE DE AVISO
    

    private void mostrarAviso(
            String titulo,
            String mensaje
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alerta.setTitle(
                "Aviso"
        );

        alerta.setHeaderText(
                titulo
        );

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }
}

