package org.example.ejei;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de creación de una nueva persona.
 * Maneja la entrada de datos del usuario y la comunicación con el controlador padre.
 */
public class NuevaPersonaController {
    @FXML
    private TextField nombreField; // Campo de nombre
    @FXML
    private TextField apellidosField; // Campo de apellidos
    @FXML
    private TextField edadField; // Campo de edad
    @FXML
    private Button guardarButton; // Botón para guardar
    @FXML
    private Button cancelarButton; // Botón para cancelar

    private HelloController parentController; // Controlador padre

    /**
     * Método que se llama al inicializar el controlador.
     * Configura las acciones de los botones.
     */
    @FXML
    public void initialize() {
        // Asignar acción al botón de guardar
        guardarButton.setOnAction(e -> guardarPersona());
        cancelarButton.setOnAction(e -> cancelar()); // Asignar acción al botón de cancelar
    }

    /**
     * Establece el controlador padre para esta ventana.
     *
     * @param parentController El controlador padre (HelloController) que maneja la lista de personas.
     */
    public void setParentController(HelloController parentController) {
        this.parentController = parentController;
    }

    /**
     * Guarda la nueva persona ingresada en los campos de texto.
     * Valida los datos antes de crear un nuevo objeto Persona y agregarlo al controlador padre.
     */
    private void guardarPersona() {
        // Obtener datos de los campos
        String nombre = nombreField.getText();
        String apellidos = apellidosField.getText();
        int edad;

        // Validación de los datos
        if (nombre.isEmpty() || apellidos.isEmpty() || edadField.getText().isEmpty()) {
            mostrarAlerta("Error", "Por favor, completa todos los campos.");
            return;
        }

        try {
            edad = Integer.parseInt(edadField.getText()); // Convertir a entero

            // Crear la nueva persona y agregarla al controlador padre
            Persona nuevaPersona = new Persona(nombre, apellidos, edad);
            parentController.agregarPersona(nuevaPersona); // Agregar a la lista del controlador padre

            // Cerrar la ventana
            Stage stage = (Stage) guardarButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            // Manejar error de conversión a entero
            mostrarAlerta("Error", "La edad debe ser un número válido.");
        }
    }

    /**
     * Cierra la ventana de creación de persona sin realizar ninguna acción.
     */
    private void cancelar() {
        // Cerrar la ventana sin hacer nada
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta en la interfaz de usuario.
     *
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
