package com.bgomez.evaluacion3;

public class User {
    public String username;
    public String password;
    public String nombre;
    public String apellido;
    public String email;
    public String telefono;
    public String direccion;
    public boolean consent;

    public User() {
        // Constructor vacío necesario para Firebase
    }

    public User(String username, String password, String nombre, String apellido,
                String email, String telefono, String direccion) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.consent = false; // Por defecto, el consentimiento es falso
    }
}
