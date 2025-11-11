package com.example.safagive.ui.article

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safagive.Chat
import com.example.safagive.Login
import com.example.safagive.Message
import com.example.safagive.Product
import com.example.safagive.databinding.ArticleBinding
import com.example.safagive.ui.chat.message.ChatMessage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArticleDetails : AppCompatActivity() {
    private lateinit var binding: ArticleBinding

    // Lista de URLs de imágenes
    private val imageUrls = arrayOf(
        "https://sevilla.abc.es/estilo/bulevarsur/wp-content/uploads/sites/14/2019/07/Siete-consejos-para-conseguir-la-foto-perfecta-en-Instagram.jpg",
        "https://img.freepik.com/foto-gratis/amor-romance-perforado-corazon-papel_53876-87.jpg",
        "https://media.sproutsocial.com/uploads/2022/06/profile-picture.jpeg"
        // Agrega más URLs de imágenes según sea necesario
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getSerializableExtra("producto") as Product

        binding.titleArticle.text = product.title
        binding.categoryArticle.text = product.category
        binding.descriptionArticle.text = product.description
        binding.chatButtom.setOnClickListener{

            var message = Message(getActualDate(), "Chat creado", "")
            val chatTable: List<Message> = mutableListOf(message)
            val newChat = mapOf(
                "usuario1" to Login.email,
                "usuario2" to product.user,
                "mensajes" to chatTable
            )
            addChat(newChat)
        }

        // Configurar el ViewFlipper
        val viewFlipper = binding.imageSlider

        // Agregar imágenes al ViewFlipper
        for (imageUrl in imageUrls) {
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            // Cargar imagen desde la URL utilizando Picasso o Glide
            // Aquí asumo que estás usando Picasso

            Picasso.get().load(imageUrl).into(imageView)
            viewFlipper.addView(imageView)
        }

        // Opcional: configurar la animación del ViewFlipper
        viewFlipper.setInAnimation(this, android.R.anim.fade_in)
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out)
    }

    private fun addChat(chat: Map<String, Any?>) {
        val database = FirebaseDatabase.getInstance("https://safagive-44998-default-rtdb.europe-west1.firebasedatabase.app/")
        val chatRef = database.getReference("mensajeria")

        val user1 = chat["usuario1"] as String
        val user2 = chat["usuario2"] as String
        val messages = chat["mensajes"] as List<Message>

        val query = chatRef.orderByChild("usuario1").equalTo(user1)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var existingChat = false
                for (chatSnapshot in dataSnapshot.children) {
                    val chatUser1 = chatSnapshot.child("usuario1").getValue(String::class.java)
                    val chatUser2 = chatSnapshot.child("usuario2").getValue(String::class.java)
                    if ((chatUser2 == user2 && chatUser1 == user1) || (chatUser1 == user2 && chatUser2 == user1)) {
                        existingChat = true
                        break
                    }
                }
                val newChat = Chat(user1, user2, messages)

                if (existingChat) {
                    val context = binding.chatButtom.context
                    Toast.makeText(context, "Ya existe un chat entre estos usuarios", Toast.LENGTH_LONG).show()
                    val intent = Intent(binding.chatButtom.context, ChatMessage::class.java).apply {
                        putExtra("chatObject", newChat)
                    }
                    startActivity(intent)
                } else {
                    val newChatRef = chatRef.push()
                    newChatRef.setValue(chat)
                        .addOnCompleteListener { task ->
                            val context = binding.chatButtom.context
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Chat creado correctamente", Toast.LENGTH_LONG).show()
                                val intent = Intent(binding.chatButtom.context, ChatMessage::class.java).apply {
                                    putExtra("chatObject", newChat)
                                }
                                startActivity(intent)
                            } else {
                                Toast.makeText(context, "Error al crear el chat, intentelo de nuevo", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val context = binding.chatButtom.context
                Toast.makeText(context, "Error al verificar el chat: ${databaseError.message}", Toast.LENGTH_LONG).show()
            }
        })
    }



    private fun getActualDate(): String {
        val formato = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val fechaActual = Date()
        return formato.format(fechaActual)
    }
}
