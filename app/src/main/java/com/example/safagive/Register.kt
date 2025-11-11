package com.example.safagive

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safagive.databinding.RegisterBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var binding: RegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.RegisterSimpleButton.setOnClickListener {
            val password = binding.textInputLayoutPassword.editText?.text.toString()
            val confirmPassword = binding.textInputLayoutPasswordR.editText?.text.toString()
            val context = binding.RegisterSimpleButton.context

            if (password == confirmPassword) {
                registerUser(binding.textInputLayoutEmail.editText?.text.toString(), password)
            } else {
                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerUser(user: String, password: String) {
        val database: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://safagive-44998-default-rtdb.europe-west1.firebasedatabase.app/")
        val usuariosRef: DatabaseReference = database.getReference("usuario")
        val context = binding.RegisterSimpleButton.context
        usuariosRef.child(user).setValue(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Login.email = user
                    Toast.makeText(
                        context,
                        "¡Usuario registrado exitosamente!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    val intent = Intent(context, VistaMainFragment::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        context,
                        "No se pudo registrar...inténtalo de nuevo",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
