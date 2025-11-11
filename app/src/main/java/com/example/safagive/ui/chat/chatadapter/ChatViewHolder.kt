package com.example.safagive.ui.chat.chatadapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.safagive.Chat
import com.example.safagive.Login
import com.example.safagive.databinding.ItemLayoutChatBinding
import com.example.safagive.ui.chat.message.ChatMessage

class ChatViewHolder(view: View): RecyclerView.ViewHolder(view)  {
    private val binding = ItemLayoutChatBinding.bind(view)
    fun render(chat: Chat){
        if(chat.user1 == Login.email){
            binding.textViewUsername.text = chat.user2
        }else{
            binding.textViewUsername.text = chat.user1
        }
        binding.chat.setOnClickListener {
            val intent = Intent(binding.textViewUsername.context, ChatMessage::class.java).apply {
                putExtra("chatObject", chat)
            }
            binding.textViewUsername.context.startActivity(intent)
        }
    }
}