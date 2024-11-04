package org.example.ejei;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Clase principal de la aplicación que extiende {@link Application}.
 * Se encarga de inicializar la interfaz gráfica y gestionar la conexión a la base de datos.
 */
public class HelloApplication extends Application {

    /**
     * Método de inicio de la aplicación.
     * Carga el archivo FXML y establece la escena principal de la interfaz.
     *
     * @param stage El escenario principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Cargar el archivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // Cargar el logo como imagen
        Image icon = new Image(getClass().getResourceAsStream("/img/agenda.png"));
        stage.getIcons().add(icon); // Establecer el ícono de la ventana

        // Crear la escena con las dimensiones adecuadas
        Scene scene = new Scene(fxmlLoader.load(), 734, 474);

        // Agregar la hoja de estilos CSS
        scene.getStylesheets().add(getClass().getResource("/css/estilo.css").toExternalForm());

        // Establecer el título de la ventana
        stage.setTitle("Personas");

        // Mostrar la escena en la ventana
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método principal que se ejecuta al iniciar la aplicación.
     * Establece la conexión a la base de datos y lanza la interfaz gráfica.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        // Crear la conexión y manejar posibles excepciones
        try {
            ConexionBBDD conexioPrueba = new ConexionBBDD();
            // Crear una persona de prueba (descomentado si es necesario)
            // ModeloPersona personaPrueba = new ModeloPersona("69696969-Z");
            // Hacer una llamada al DAO (descomentado si es necesario)
            // DaoDni.nuevoDNI(personaPrueba);
        } catch (SQLException e) {
            // Manejo de excepciones para la conexión a la base de datos
            e.printStackTrace(); // Imprimir el stack trace para depuración
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return; // Salir si hay un error en la conexión
        }

        // Iniciar la aplicación
        launch();
    }
}
