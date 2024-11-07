package com.bgomez.evaluacion3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.*;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtNombre, edtApellido, edtEmail, edtTelefono, edtDireccion;
    CheckBox chkDeleteEmail, chkDeleteTelefono, chkDeleteDireccion;
    Button btnSaveChanges, btnDeleteSelected;
    DatabaseReference databaseUsers;
    String username;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inicializar Firebase Database
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        // Referencias a los views
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtDireccion = findViewById(R.id.edtDireccion);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        chkDeleteEmail = findViewById(R.id.chkDeleteEmail);
        chkDeleteTelefono = findViewById(R.id.chkDeleteTelefono);
        chkDeleteDireccion = findViewById(R.id.chkDeleteDireccion);
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected);

        // Obtener el nombre de usuario desde el intent
        username = getIntent().getStringExtra("username");

        // Cargar los datos del usuario
        loadUserData();

        // Configurar el listener para el botón de guardar cambios
        btnSaveChanges.setOnClickListener(view -> {
            saveUserData();
        });

        // Listener para el botón de eliminar datos seleccionados
        btnDeleteSelected.setOnClickListener(view -> {
            deleteSelectedData();
        });

    }

    private void loadUserData() {
        databaseUsers.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    user = snapshot.getValue(User.class);

                    // Rellenar los campos con los datos actuales
                    edtNombre.setText(user.nombre);
                    edtApellido.setText(user.apellido);

                    // Manejar posibles campos nulos
                    edtEmail.setText(user.email != null ? user.email : "");
                    edtTelefono.setText(user.telefono != null ? user.telefono : "");
                    edtDireccion.setText(user.direccion != null ? user.direccion : "");
                } else {
                    Toast.makeText(EditProfileActivity.this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Error al cargar datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData() {
        // Obtener los datos actualizados
        String nombre = edtNombre.getText().toString().trim();
        String apellido = edtApellido.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();
        String direccion = edtDireccion.getText().toString().trim();

        if(nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "El nombre y apellido son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar los datos del usuario
        user.nombre = nombre;
        user.apellido = apellido;
        user.email = email.isEmpty() ? null : email;
        user.telefono = telefono.isEmpty() ? null : telefono;
        user.direccion = direccion.isEmpty() ? null : direccion;

        // Guardar en Firebase
        databaseUsers.child(username).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad
            } else {
                Toast.makeText(this, "Error al actualizar datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSelectedData() {
        // Eliminar Email si el CheckBox está marcado
        if (chkDeleteEmail.isChecked()) {
            user.email = null;
            edtEmail.setText("");
        }

        // Eliminar Teléfono si el CheckBox está marcado
        if (chkDeleteTelefono.isChecked()) {
            user.telefono = null;
            edtTelefono.setText("");
        }

        // Eliminar Dirección si el CheckBox está marcado
        if (chkDeleteDireccion.isChecked()) {
            user.direccion = null;
            edtDireccion.setText("");
        }

        // Actualizar en Firebase
        databaseUsers.child(username).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Datos eliminados correctamente.", Toast.LENGTH_SHORT).show();
                // Desmarcar los CheckBox
                chkDeleteEmail.setChecked(false);
                chkDeleteTelefono.setChecked(false);
                chkDeleteDireccion.setChecked(false);
            } else {
                Toast.makeText(this, "Error al eliminar datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
