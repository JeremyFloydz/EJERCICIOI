package org.example.ejei;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * La clase ConexionBBDD se encarga de establecer una conexión
 * a una base de datos MariaDB. Implementa la interfaz AutoCloseable
 * para permitir el uso de try-with-resources, asegurando que la
 * conexión se cierre automáticamente al finalizar su uso.
 */
public class ConexionBBDD implements AutoCloseable {
    private Connection conexion;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     *
     * @throws SQLException Si ocurre un error al establecer la conexión
     *                      o si no se puede cargar el driver de MariaDB.
     */
    public ConexionBBDD() throws SQLException {
        try {
            // Cargar el driver de MariaDB
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("No se pudo cargar el driver de MariaDB", e);
        }

        // Configurar los parámetros de la conexión
        Properties connConfig = new Properties();
        connConfig.setProperty("user", "root");  // Usuario de la BD
        connConfig.setProperty("password", "myPass");  // Contraseña de la BD

        // Establecer la conexión a la base de datos MariaDB
        String url = "jdbc:mariadb://localhost:3310/personas?serverTimezone=Europe/Madrid";
        conexion = DriverManager.getConnection(url, connConfig);
        conexion.setAutoCommit(true);

        // Información de la base de datos para depuración
        DatabaseMetaData databaseMetaData = conexion.getMetaData();
        System.out.println("--- Datos de conexión ------------------------------------------");
        System.out.printf("Base de datos: %s%n", databaseMetaData.getDatabaseProductName());
        System.out.printf("Versión: %s%n", databaseMetaData.getDatabaseProductVersion());
        System.out.printf("Driver: %s%n", databaseMetaData.getDriverName());
        System.out.printf("Versión: %s%n", databaseMetaData.getDriverVersion());
        System.out.println("----------------------------------------------------------------");
    }

    /**
     * Obtiene la conexión a la base de datos.
     *
     * @return La conexión a la base de datos.
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     * Este método se llama automáticamente al finalizar el bloque
     * try-with-resources.
     */
    @Override
    public void close() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
