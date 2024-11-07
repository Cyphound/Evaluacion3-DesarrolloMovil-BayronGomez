package com.bgomez.evaluacion3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    TextView txtWelcome;
    ImageView imgWelcome;
    Button btnEditProfile;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Referencias a los views
        txtWelcome = findViewById(R.id.txtWelcome);
        imgWelcome = findViewById(R.id.imgWelcome);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Obtener el nombre de usuario desde el intent
        username = getIntent().getStringExtra("username");
        String nombre = getIntent().getStringExtra("nombre");

        // Configurar el mensaje de bienvenida
        txtWelcome.setText("¡Bienvenido, " + nombre + "!");

        // Configurar la imagen (puedes cambiarla según tus necesidades)
        imgWelcome.setImageResource(R.drawable.logo); // Asegúrate de tener esta imagen en drawable

        // Configurar el listener para el botón de editar perfil
        btnEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, EditProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}
