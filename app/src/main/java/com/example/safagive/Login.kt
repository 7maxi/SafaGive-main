package com.example.safagive

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialUnsupportedException
import com.auth0.android.jwt.JWT
import com.example.safagive.databinding.LoginBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    companion object {
        var email: String = ""
    }
    private lateinit var binding: LoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btnLoginSimpleListener()
        btnLoginGoogleListener()
        btnRegisterListener()
    }

    private fun btnLoginSimpleListener() {
        val context = this
        binding.loginSimpleButton.setOnClickListener {
            val email = binding.textfieldEmail.text.toString()
            val password = binding.textfieldPassword.text.toString()

            val database = FirebaseDatabase.getInstance("https://safagive-44998-default-rtdb.europe-west1.firebasedatabase.app/")
            val usersRef = database.getReference("usuario")

            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var emailExists = false
                    var passwordMatches = false
                    for (userSnapshot in dataSnapshot.children) {
                        val dbEmail = userSnapshot.key
                        val dbPassword = userSnapshot.getValue(String::class.java)

                        if (dbEmail == email && dbPassword == password) {
                            emailExists = true
                            passwordMatches = true
                            break
                        } else if (dbEmail == email) {
                            emailExists = true
                            passwordMatches = false
                        }
                    }

                    if (emailExists && passwordMatches) {
                        Login.email = email
                        val intent = Intent(context, VistaMainFragment::class.java)
                        startActivity(intent)
                    } else if (emailExists) {
                        binding.textInputLayoutPassword.error = "Contraseña incorrecta"
                    } else {
                        binding.textInputLayoutEmail.error = "Email no registrado"
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "Error al iniciar sesión: ${databaseError.message}", Toast.LENGTH_LONG).show()
                }
            })

        }
    }

    private fun btnLoginGoogleListener() {
        val context = this

        binding.loginButton.setOnClickListener {

            val coroutineScope = MainScope()
            val credentialManager = CredentialManager.create(this)
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("908263026546-ef02t045fols5mvosgsf2v3u2qkrkai5.apps.googleusercontent.com")
                .setAutoSelectEnabled(true)
                .build()
            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            coroutineScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                    val credential = result.credential
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val googleIdToken = googleIdTokenCredential.idToken
                    if (googleIdToken.isNotEmpty()) {
                        val jwt = JWT(googleIdToken)
                        email = jwt.getClaim("email").asString().toString()
                        val fullName = jwt.getClaim("name").asString().toString()
                        Toast.makeText(context, "Email: $email\nName: $fullName", Toast.LENGTH_LONG)
                            .show()
                        val intentG = Intent(context, VistaMainFragment::class.java).apply {
                            putExtra("GOOGLE_ID_TOKEN", googleIdToken)
                        }
                        startActivity(intentG)
                    }
                }catch (e: GetCredentialUnsupportedException) {
                    Toast.makeText(context, "No ha sido posible iniciar sesión con Google. Por favor, pruebe de otra forma.", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al obtener credencial: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun btnRegisterListener() {
        val context = this

        binding.registerText.setOnClickListener {
            val intent = Intent(context, Register::class.java).apply {
            }
            startActivity(intent)
        }
    }

}
