package com.example.safagive.ui.chat.message

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safagive.Chat
import com.example.safagive.Login
import com.example.safagive.Message
import com.example.safagive.databinding.ChatLayoutBinding
import com.example.safagive.ui.chat.message.messageadapter.MessageAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatMessage : AppCompatActivity(){
    private lateinit var binding: ChatLayoutBinding
    private var database: DatabaseReference? = null
    private var messageTable: MutableList<Message> = mutableListOf()
    private var user1: String = ""
    private var user2: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chat = intent.getSerializableExtra("chatObject") as Chat
        user1 = chat.user1
        user2 = chat.user2

        database = FirebaseDatabase.getInstance("https://safagive-44998-default-rtdb.europe-west1.firebasedatabase.app/").reference

        updateUserTable()

        addMessage()

    }

    private fun updateUserTable(){
        val context = binding.editTextMessage.context
        database?.child("mensajeria")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageTable.clear()
                for (chatSnapshot in dataSnapshot.children) {
                    val chatUsuario1 = chatSnapshot.child("usuario1").getValue(String::class.java)
                    val chatUsuario2 = chatSnapshot.child("usuario2").getValue(String::class.java)

                    if ((chatUsuario1 == user1 && chatUsuario2 == user2) ||
                        (chatUsuario1 == user2 && chatUsuario2 == user1)
                    ) {

                        val messagesSnapshot = chatSnapshot.child("mensajes").children

                        for (messageSnapshot in messagesSnapshot) {
                            val messageUser = messageSnapshot.child("usuario").getValue(String::class.java)
                            val messageDate = messageSnapshot.child("fecha").getValue(String::class.java)
                            val messageMessage = messageSnapshot.child("mensaje").getValue(String::class.java)
                            if (messageUser != null && messageDate != null && messageMessage != null){
                                val message = Message(messageDate, messageMessage, messageUser)
                                messageTable.add(message)
                            }
                        }
                        for (mesage in messageTable){
                            println(mesage.message)
                        }

                        loadMessageReciclerView()
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadMessageReciclerView(){
        if(messageTable.isEmpty()){
            Toast.makeText(binding.recyclerMessages.context, "Error al cargar los mensajes", Toast.LENGTH_LONG).show()
        }else{
            val recyclerMessages = binding.recyclerMessages
            recyclerMessages.layoutManager = LinearLayoutManager(recyclerMessages.context)
            recyclerMessages.adapter = MessageAdapter(messageTable)
        }

    }

    private fun addMessage(){
        binding.buttonSend.setOnClickListener(){
            val context = binding.editTextMessage.context
            val message = binding.editTextMessage.text.toString()

            if (message != "" || !onlyHaveSpacesAndLineEnds(message)){
                database?.child("mensajeria")?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (chatSnapshot in dataSnapshot.children) {
                            val chatUsuario1 = chatSnapshot.child("usuario1").getValue(String::class.java)
                            val chatUsuario2 = chatSnapshot.child("usuario2").getValue(String::class.java)

                            if ((chatUsuario1 == user1 && chatUsuario2 == user2) ||
                                (chatUsuario1 == user2 && chatUsuario2 == user1)
                            ) {
                                val mensajesRef = chatSnapshot.child("mensajes").ref

                                val messageObject = mapOf(
                                    "fecha" to getActualDate(),
                                    "mensaje" to message,
                                    "usuario" to Login.email
                                )

                                val newMessageRef = mensajesRef.push()
                                newMessageRef.setValue(messageObject)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Mensaje enviado correctamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Error al enviar el mensaje",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        loadMessageReciclerView()
                                    }
                                return
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    private fun getActualDate(): String {
        val formato = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val fechaActual = Date()
        return formato.format(fechaActual)
    }

    private fun onlyHaveSpacesAndLineEnds(input: String): Boolean {
        return input.matches("[\\s\\n]*".toRegex())
    }

}