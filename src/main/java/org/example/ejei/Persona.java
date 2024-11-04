package org.example.ejei;

import java.util.Objects;

/**
 * Clase que representa una persona con nombre, apellidos y edad.
 */
public class Persona {
    private String nombre;     // Nombre de la persona
    private String apellidos;  // Apellidos de la persona
    private int edad;         // Edad de la persona

    /**
     * Constructor que inicializa una nueva instancia de Persona.
     *
     * @param nombre    El nombre de la persona.
     * @param apellidos Los apellidos de la persona.
     * @param edad      La edad de la persona. Debe ser un número no negativo.
     */
    public Persona(String nombre, String apellidos, int edad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        setEdad(edad); // Usar el setter para validar la edad
    }

    // Getters

    /**
     * Obtiene el nombre de la persona.
     *
     * @return El nombre de la persona.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene los apellidos de la persona.
     *
     * @return Los apellidos de la persona.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Obtiene la edad de la persona.
     *
     * @return La edad de la persona.
     */
    public int getEdad() {
        return edad;
    }

    // Setters

    /**
     * Establece el nombre de la persona.
     *
     * @param nombre El nuevo nombre de la persona.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece los apellidos de la persona.
     *
     * @param apellidos Los nuevos apellidos de la persona.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Establece la edad de la persona.
     *
     * @param edad La nueva edad de la persona. Debe ser un número no negativo.
     * @throws IllegalArgumentException Si la edad es negativa.
     */
    public void setEdad(int edad) {
        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa."); // Validación
        }
        this.edad = edad;
    }

    /**
     * Compara este objeto Persona con otro objeto.
     *
     * @param obj El objeto a comparar.
     * @return true si son iguales, false de lo contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Persona)) return false;
        Persona persona = (Persona) obj;
        return edad == persona.edad &&
                Objects.equals(nombre, persona.nombre) &&
                Objects.equals(apellidos, persona.apellidos);
    }

    /**
     * Devuelve un código hash para este objeto Persona.
     *
     * @return Un entero que representa el código hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, apellidos, edad);
    }

    /**
     * Devuelve una representación en forma de cadena de la persona.
     *
     * @return Una cadena que describe a la persona.
     */
    @Override
    public String toString() {
        return nombre + " " + apellidos + " (" + edad + " años)";
    }
}
