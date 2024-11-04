package org.example.ejei;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text; // Importar Text
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Controlador principal de la interfaz de usuario de la aplicación.
 * Gestiona la interacción del usuario con la tabla de personas y las acciones de agregar, modificar y eliminar personas.
 */
public class HelloController {

    @FXML
    private TableView<Persona> tableView; // Tabla para mostrar la lista de personas
    @FXML
    private Text filtroNombreText; // Campo para el texto "Filtrar por nombre:"
    @FXML
    private TableColumn<Persona, String> nombreColumn; // Columna para el nombre
    @FXML
    private TableColumn<Persona, String> apellidosColumn; // Columna para los apellidos
    @FXML
    private TableColumn<Persona, Integer> edadColumn; // Columna para la edad
    @FXML
    private Button agregarButton; // Botón para agregar una nueva persona
    @FXML
    private Button modificarButton; // Botón para modificar una persona existente
    @FXML
    private Button eliminarButton; // Botón para eliminar una persona
    @FXML
    private Button cambiarIdioma; // Botón para cambiar el idioma de la interfaz
    @FXML
    private TextField filtroNombreField; // Campo de texto para filtrar por nombre

    private ObservableList<Persona> personas; // Lista observable de personas
    private Properties config; // Configuración de propiedades para la internacionalización
    private String currentLanguage = "es"; // Idioma por defecto
    private ContextMenu contextMenu; // Menú contextual

    /**
     * Constructor vacío que inicializa la configuración de propiedades.
     */
    public HelloController() {
        this.config = new Properties(); // Inicializa config en el constructor vacío
    }

    /**
     * Método que se llama al inicializar el controlador.
     * Configura la tabla, los botones y el filtrado.
     */
    @FXML
    public void initialize() {
        personas = FXCollections.observableArrayList();
        tableView.setItems(personas);

        // Cargar el idioma por defecto
        cargarIdioma(currentLanguage);

        // Configurar columnas de la tabla
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        apellidosColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellidos()));
        edadColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());

        // Asignar acciones a los botones
        agregarButton.setOnAction(e -> agregarPersona());
        modificarButton.setOnAction(e -> modificarPersona());
        eliminarButton.setOnAction(e -> eliminarPersona());

        // Configurar el filtrado de la tabla
        filtroNombreField.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabla(newValue));

        // Crear el menú contextual
        crearMenuContextual();

        // Mostrar el menú contextual al seleccionar una fila
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                int index = tableView.getSelectionModel().getSelectedIndex();
                if (index >= 0) { // Si hay una fila seleccionada
                    contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
                }
            }
        });

        // Asignar acción al botón de cambiar idioma
        cambiarIdioma.setOnAction(e -> cambiarIdioma());
    }

    /**
     * Carga las propiedades de idioma desde un archivo de configuración.
     *
     * @param language El código del idioma a cargar (ej. "es", "en", "eu").
     */
    private void cargarIdioma(String language) {
        try (InputStream input = getClass().getResourceAsStream("/messages_" + language + ".properties")) {
            if (input != null) {
                config.load(input);
                currentLanguage = language; // Actualiza el idioma actual
                actualizarTextos(); // Actualiza los textos en la interfaz
                crearMenuContextual(); // Re-crea el menú contextual
            } else {
                System.err.println("Properties file not found for language: " + language);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea el menú contextual para la tabla de personas.
     */
    private void crearMenuContextual() {
        contextMenu = new ContextMenu();

        MenuItem modificarItem = new MenuItem(config.getProperty("modify.person", "Modificar persona"));
        modificarItem.setOnAction(e -> modificarPersona());

        MenuItem eliminarItem = new MenuItem(config.getProperty("delete.person", "Eliminar persona"));
        eliminarItem.setOnAction(e -> eliminarPersona());

        contextMenu.getItems().clear(); // Limpia los elementos anteriores
        contextMenu.getItems().addAll(modificarItem, eliminarItem);
    }

    /**
     * Cambia entre los idiomas disponibles: español, inglés y euskera.
     */
    private void cambiarIdioma() {
        // Cambia entre español, inglés y euskera
        if (currentLanguage.equals("es")) {
            cargarIdioma("en"); // Cambiar a inglés
        } else if (currentLanguage.equals("en")) {
            cargarIdioma("eu"); // Cambiar a euskera
        } else {
            cargarIdioma("es"); // Volver a español
        }
    }

    /**
     * Actualiza los textos de la interfaz de usuario según el idioma seleccionado.
     */
    private void actualizarTextos() {
        agregarButton.setText(config.getProperty("add.person", "Agregar")); // Texto por defecto si no se encuentra la propiedad
        modificarButton.setText(config.getProperty("modify.person", "Modificar"));
        eliminarButton.setText(config.getProperty("delete.person", "Eliminar"));
        cambiarIdioma.setText(config.getProperty("change.language", "Idiomas")); // Asegúrate de tener esta propiedad en los archivos de propiedades

        // Actualiza los encabezados de las columnas
        nombreColumn.setText(config.getProperty("column.name", "Nombre")); // Texto por defecto si no se encuentra la propiedad
        apellidosColumn.setText(config.getProperty("column.surname", "Apellidos"));
        edadColumn.setText(config.getProperty("column.age", "Edad"));

        filtroNombreText.setText(config.getProperty("filter.label", "Filtrar por nombre")); // Asegúrate de tener esta propiedad en los archivos de propiedades
        // Actualiza el texto del filtro
        filtroNombreField.setPromptText(config.getProperty("filter.name", "Filtrar nombre")); // Texto por defecto si no se encuentra la propiedad
    }

    /**
     * Abre una ventana para agregar una nueva persona.
     */
    private void agregarPersona() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ventana.fxml"));
            Parent root = loader.load();

            NuevaPersonaController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle(config.getProperty("add.person", "Agregar persona")); // Título traducido
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", config.getProperty("error.open.add", "Error al abrir la ventana de agregar"));
        }
    }

    /**
     * Abre una ventana para modificar la persona seleccionada.
     */
    private void modificarPersona() {
        Persona personaSeleccionada = tableView.getSelectionModel().getSelectedItem();
        if (personaSeleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("editarventana.fxml"));
                Parent root = loader.load();

                EditarPersonaController controller = loader.getController();
                controller.setParentController(this);
                controller.cargarDatos(personaSeleccionada);

                Stage stage = new Stage();
                stage.setTitle(config.getProperty("modify.person", "Modificar persona")); // Título traducido
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                actualizarTabla();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", config.getProperty("error.open.edit", "Error al abrir la ventana de edición"));
            }
        } else {
            mostrarAlerta("Advertencia", config.getProperty("select.person.modify", "Seleccione una persona para modificar"));
        }
    }

    /**
     * Elimina la persona seleccionada de la tabla y de la base de datos.
     */
    private void eliminarPersona() {
        Persona personaSeleccionada = tableView.getSelectionModel().getSelectedItem();
        if (personaSeleccionada != null) {
            boolean eliminadoEnBD = DaoPersona.eliminarPersona(personaSeleccionada);
            if (eliminadoEnBD) {
                personas.remove(personaSeleccionada);
                mostrarAlerta("Éxito", config.getProperty("success.delete", "Persona eliminada con éxito"));
            } else {
                mostrarAlerta("Error", config.getProperty("error.delete", "Error al eliminar la persona"));
            }
        } else {
            mostrarAlerta("Advertencia", config.getProperty("select.person.delete", "Seleccione una persona para eliminar"));
        }
    }

    /**
     * Agrega una nueva persona a la lista y a la base de datos.
     *
     * @param nuevaPersona La nueva persona a agregar.
     */
    public void agregarPersona(Persona nuevaPersona) {
        if (!personas.contains(nuevaPersona)) {
            boolean guardadoEnBD = DaoPersona.nuevaPersona(nuevaPersona);
            if (guardadoEnBD) {
                personas.add(nuevaPersona);
                mostrarAlerta("Éxito", config.getProperty("success.add", "Persona agregada con éxito"));
            } else {
                mostrarAlerta("Error", config.getProperty("error.save", "Error al guardar la persona"));
            }
        } else {
            mostrarAlerta("Error", config.getProperty("error.duplicate", "La persona ya existe"));
        }
    }

    /**
     * Muestra una alerta en la interfaz de usuario.
     *
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje a mostrar en la alerta.
     */
    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Actualiza la tabla de personas en la interfaz de usuario.
     */
    public void actualizarTabla() {
        tableView.refresh();
    }

    /**
     * Filtra la tabla de personas según el nombre proporcionado.
     *
     * @param nombre El nombre para filtrar.
     */
    private void filtrarTabla(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            tableView.setItems(personas);
        } else {
            ObservableList<Persona> filtradas = FXCollections.observableArrayList();
            for (Persona persona : personas) {
                if (persona.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                    filtradas.add(persona);
                }
            }
            tableView.setItems(filtradas);
        }
    }
}
