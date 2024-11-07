package com.bgomez.evaluacion3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.*;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtNombre, edtApellido, edtEmail, edtTelefono, edtDireccion;
    Button btnSubmit;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar Firebase Database
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        // Referencias a los views
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtNombre   = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtEmail    = findViewById(R.id.edtEmail);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtDireccion= findViewById(R.id.edtDireccion);
        btnSubmit   = findViewById(R.id.btnSubmit);

        // Listener para el botón de registrar
        btnSubmit.setOnClickListener(view -> {
            // Obtener datos y registrar usuario
            registerUser();
        });
    }

    private void registerUser() {
        // Obtener datos de los EditText
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String nombre   = edtNombre.getText().toString().trim();
        String apellido = edtApellido.getText().toString().trim();
        String email    = edtEmail.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();
        String direccion= edtDireccion.getText().toString().trim();

        if(username.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                email.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, password, nombre, apellido, email, telefono, direccion);

        // Verificar si el usuario ya existe
        databaseUsers.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Toast.makeText(RegisterActivity.this, "El nombre de usuario ya está en uso.", Toast.LENGTH_SHORT).show();
                } else {
                    // Guardar en Firebase
                    databaseUsers.child(username).setValue(user).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registro exitoso. Por favor, inicia sesión.", Toast.LENGTH_SHORT).show();
                            finish(); // Cierra la actividad y regresa al login
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error en el registro.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Error en la base de datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
