package com.bgomez.evaluacion3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin, btnRegister;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Database
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        // Referencias a los views
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Listener para el botón de iniciar sesión
        btnLogin.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                loginUser(username, password);
            } else {
                Toast.makeText(MainActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener para el botón de registrarse
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String username, String password) {
        databaseUsers.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if(user.password.equals(password)) {
                        if(!user.consent) {
                            // Si no ha dado consentimiento, mostrar ConsentActivity
                            Intent intent = new Intent(MainActivity.this, ConsentActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            // Mostrar WelcomeActivity
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("nombre", user.nombre);
                            startActivity(intent);
                            // Opcional: finalizar MainActivity si no deseas que el usuario pueda regresar con el botón atrás
                            finish();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "El usuario no existe.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error en la base de datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
