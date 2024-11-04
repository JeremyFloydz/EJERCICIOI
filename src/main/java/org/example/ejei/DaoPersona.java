package org.example.ejei;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * La clase DaoPersona proporciona métodos para realizar operaciones
 * de acceso a datos (DAO) sobre la entidad Persona en la base de datos.
 * Permite cargar, modificar, crear y eliminar registros de personas.
 */
public class DaoPersona {

    /**
     * Carga una lista de personas desde la base de datos.
     *
     * @return Un ObservableList de objetos Persona que contiene los
     *         registros cargados desde la base de datos.
     */
    public static ObservableList<Persona> cargarListadoPersonas() {
        ObservableList<Persona> listadoDePersonas = FXCollections.observableArrayList();

        // Usar try-with-resources para manejar la conexión y los recursos
        try (ConexionBBDD conexion = new ConexionBBDD();
             Connection conn = conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("SELECT nombre, apellidos, edad FROM Persona");
             ResultSet rs = pstmt.executeQuery()) {

            // Llenar la lista con los resultados
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                int edad = rs.getInt("edad");
                Persona persona = new Persona(nombre, apellidos, edad);
                listadoDePersonas.add(persona);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar listado de personas: " + e.getMessage());
        }
        return listadoDePersonas;
    }

    /**
     * Modifica un registro de persona en la base de datos.
     *
     * @param personaAntigua La persona existente que se desea modificar.
     * @param personaNueva   La nueva persona con los valores actualizados.
     * @return true si la modificación fue exitosa; false en caso contrario.
     */
    public static boolean modificarPersona(Persona personaAntigua, Persona personaNueva) {
        String consulta = "UPDATE Persona SET nombre = ?, apellidos = ?, edad = ? WHERE nombre = ? AND apellidos = ?";
        try (ConexionBBDD conexion = new ConexionBBDD();
             Connection conn = conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(consulta)) {

            // Establece los nuevos valores
            pstmt.setString(1, personaNueva.getNombre());
            pstmt.setString(2, personaNueva.getApellidos());
            pstmt.setInt(3, personaNueva.getEdad());

            // Utiliza los valores originales para la búsqueda
            pstmt.setString(4, personaAntigua.getNombre());
            pstmt.setString(5, personaAntigua.getApellidos());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar en la base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea una nueva persona en la base de datos.
     *
     * @param persona La persona a crear en la base de datos.
     * @return true si la creación fue exitosa; false en caso contrario.
     */
    public static boolean nuevaPersona(Persona persona) {
        String consulta = "INSERT INTO Persona (nombre, apellidos, edad) VALUES (?, ?, ?)";
        try (ConexionBBDD conexion = new ConexionBBDD();
             Connection conn = conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(consulta)) {

            pstmt.setString(1, persona.getNombre());
            pstmt.setString(2, persona.getApellidos());
            pstmt.setInt(3, persona.getEdad());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear persona en la base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una persona de la base de datos.
     *
     * @param personaAEliminar La persona que se desea eliminar.
     * @return true si la eliminación fue exitosa; false en caso contrario.
     */
    public static boolean eliminarPersona(Persona personaAEliminar) {
        String consulta = "DELETE FROM Persona WHERE nombre = ? AND apellidos = ?";
        try (ConexionBBDD conexion = new ConexionBBDD();
             Connection conn = conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(consulta)) {

            pstmt.setString(1, personaAEliminar.getNombre());
            pstmt.setString(2, personaAEliminar.getApellidos());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar en la base de datos: " + e.getMessage());
            return false;
        }
    }
}
