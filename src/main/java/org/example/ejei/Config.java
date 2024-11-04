package org.example.ejei;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * La clase Config se encarga de cargar archivos de propiedades
 * basados en el idioma especificado mediante un objeto Locale.
 * Proporciona métodos para acceder a las propiedades cargadas.
 */
public class Config {
    private Properties properties;

    /**
     * Constructor que inicializa la clase Config y carga las propiedades
     * según el locale proporcionado.
     *
     * @param locale El locale que determina el idioma de las propiedades
     *               a cargar.
     */
    public Config(Locale locale) {
        properties = new Properties();
        loadProperties(locale);
    }

    /**
     * Carga las propiedades desde un archivo correspondiente al idioma del locale.
     *
     * @param locale El locale que determina el archivo de propiedades a cargar.
     */
    private void loadProperties(Locale locale) {
        String language = locale.getLanguage(); // Obtiene el código del idioma
        String fileName = "messages_" + language + ".properties"; // Construye el nombre del archivo

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println("No se pudo encontrar el archivo de propiedades: " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el valor asociado a una clave específica en el archivo de propiedades.
     *
     * @param key La clave cuya propiedad se desea obtener.
     * @return El valor de la propiedad correspondiente a la clave,
     *         o un mensaje por defecto si la clave no existe.
     */
    public String getString(String key) {
        return properties.getProperty(key, "No se encontró la clave: " + key); // Mensaje por defecto si la clave no existe
    }
}
