package com.bgomez.evaluacion3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;

public class ConsentActivity extends AppCompatActivity {

    TextView txtDatos;
    Button btnAceptar, btnNegar;
    DatabaseReference databaseUsers;
    String username;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        // Referencias a los views
        txtDatos = findViewById(R.id.txtDatos);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnNegar = findViewById(R.id.btnNegar);

        username = getIntent().getStringExtra("username");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        // Obtener datos del usuario y mostrarlos
        databaseUsers.child(username).addListenerForSingleValueEvent(new ValueEventListener() {



            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                    User user = snapshot.getValue(User.class);
                    String datos = "Nombre: " + user.nombre + "\n" +
                            "Apellido: " + user.apellido + "\n" +
                            "Email: " + (user.email != null ? user.email : "No proporcionado") + "\n" +
                            "Teléfono: " + (user.telefono != null ? user.telefono : "No proporcionado") + "\n" +
                            "Dirección: " + (user.direccion != null ? user.direccion : "No proporcionado") + "\n\n" +
                            "¿Aceptas la divulgación de esta información?";
                    txtDatos.setText(datos);
                } else {
                    Toast.makeText(ConsentActivity.this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConsentActivity.this, "Error en la base de datos.", Toast.LENGTH_SHORT).show();
            }
        });

        // Listeners para los botones
        btnAceptar.setOnClickListener(view -> {
            // Actualizar consentimiento a true
            updateConsent(true);
        });

        btnNegar.setOnClickListener(view -> {
            // Actualizar consentimiento a false
            updateConsent(false);
        });
    }

    private void updateConsent(boolean consent) {
        databaseUsers.child(username).child("consent").setValue(consent).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String decision = consent ? "has aceptado" : "has negado";
                Toast.makeText(this, "Has " + decision + " la divulgación de datos.", Toast.LENGTH_SHORT).show();
                if(consent) {
                    // Si el usuario acepta, dirigir a WelcomeActivity
                    Intent intent = new Intent(ConsentActivity.this, WelcomeActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("nombre", user.nombre);
                    startActivity(intent);
                    finish(); // Finalizar ConsentActivity
                } else {
                    // Si el usuario niega, regresar a MainActivity o finalizar
                    finish();
                }
            } else {
                Toast.makeText(this, "Error al guardar decisión.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
