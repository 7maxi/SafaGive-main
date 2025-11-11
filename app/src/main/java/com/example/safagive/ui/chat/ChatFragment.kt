package com.example.safagive.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safagive.Chat
import com.example.safagive.Login
import com.example.safagive.Message
import com.example.safagive.databinding.FragmentChatBinding
import com.example.safagive.ui.chat.chatadapter.ChatAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null

    private val binding get() = _binding!!
    private var database: DatabaseReference? = null
    private var chatTable: MutableList<Chat> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = FirebaseDatabase.getInstance("https://safagive-44998-default-rtdb.europe-west1.firebasedatabase.app/").reference

        updateUserTable()

        return root
    }

    private fun updateUserTable(){
        database?.child("mensajeria")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatTable.clear()
                if (dataSnapshot.exists()) {
                    for (chatSnapshot in dataSnapshot.children) {
                        val usuario1 = chatSnapshot.child("usuario1").getValue(String::class.java) ?: ""
                        val usuario2 = chatSnapshot.child("usuario2").getValue(String::class.java) ?: ""
                        if(usuario1 == Login.email || usuario2 == Login.email){
                            val mensajesSnapshot = chatSnapshot.child("mensajes").children
                            val messages = mutableListOf<Message>()

                            for (mensajeSnapshot in mensajesSnapshot) {
                                val message = mensajeSnapshot.getValue(Message::class.java)
                                if (message != null) {
                                    messages.add(message)
                                }
                            }

                            val chat = Chat(usuario1, usuario2, messages)
                            chatTable.add(chat)
                        }
                    }
                }
                loadProductReciclerView(chatTable)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadProductReciclerView(chatTable: MutableList<Chat>){
        if (chatTable.size == 0){
            binding.noResultsReciclerView.visibility = View.VISIBLE
            binding.recyclerChats.visibility = View.INVISIBLE
        }else{
            binding.noResultsReciclerView.visibility = View.INVISIBLE
            val productsRecicler = binding.recyclerChats
            productsRecicler.visibility = View.VISIBLE
            productsRecicler.layoutManager = LinearLayoutManager(productsRecicler.context)
            productsRecicler.adapter = ChatAdapter(chatTable)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}