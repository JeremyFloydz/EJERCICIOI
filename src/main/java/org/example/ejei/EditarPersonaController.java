package org.example.ejei;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la interfaz de edición de una persona.
 * Permite cargar los datos de una persona, modificarlos y guardarlos
 * en la base de datos, así como cancelar la operación de edición.
 */
public class EditarPersonaController {
    @FXML
    private TextField nombreField; // Campo para nombre
    @FXML
    private TextField apellidosField; // Campo para apellidos
    @FXML
    private TextField edadField; // Campo para edad
    @FXML
    private Button guardarButton; // Botón para guardar
    @FXML
    private Button cancelarButton; // Botón para cancelar

    private HelloController parentController; // Controlador padre
    private Persona personaEdicion; // Persona que se está editando

    /**
     * Inicializa el controlador y asigna acciones a los botones.
     */
    @FXML
    public void initialize() {
        // Asignar acción al botón de guardar
        guardarButton.setOnAction(e -> guardarPersona());
        cancelarButton.setOnAction(e -> cancelar());
    }

    /**
     * Carga los datos de la persona seleccionada en los campos de texto.
     *
     * @param persona La persona cuyos datos se van a cargar.
     */
    public void cargarDatos(Persona persona) {
        this.personaEdicion = persona; // Guardar la referencia de la persona
        nombreField.setText(persona.getNombre());
        apellidosField.setText(persona.getApellidos());
        edadField.setText(String.valueOf(persona.getEdad()));
    }

    /**
     * Guarda los cambios realizados en los campos de texto
     * y actualiza la base de datos.
     */
    private void guardarPersona() {
        String nombre = nombreField.getText();
        String apellidos = apellidosField.getText();
        String edadStr = edadField.getText();

        // Validación de datos
        if (nombre.isEmpty() || apellidos.isEmpty() || edadStr.isEmpty()) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        try {
            int edad = Integer.parseInt(edadStr);
            // Crear un nuevo objeto Persona con los datos modificados
            Persona personaNueva = new Persona(nombre, apellidos, edad);

            // Modificar la persona en la base de datos
            boolean modificadoEnBD = DaoPersona.modificarPersona(personaEdicion, personaNueva);
            if (modificadoEnBD) {
                // Actualizar la persona en la referencia actual
                personaEdicion.setNombre(nombre);
                personaEdicion.setApellidos(apellidos);
                personaEdicion.setEdad(edad);

                // Notificar al controlador padre que los datos han cambiado
                parentController.actualizarTabla(); // Asegúrate de implementar este método

                // Cerrar la ventana
                cerrarVentana();
            } else {
                mostrarAlerta("No se pudo actualizar la persona en la base de datos.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("La edad debe ser un número válido.");
        }
    }

    /**
     * Cancela la edición y cierra la ventana actual.
     */
    private void cancelar() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta al usuario con un mensaje específico.
     *
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Establece el controlador padre para permitir la comunicación
     * entre controladores.
     *
     * @param parentController El controlador padre que se establecerá.
     */
    public void setParentController(HelloController parentController) {
        this.parentController = parentController;
    }
}
