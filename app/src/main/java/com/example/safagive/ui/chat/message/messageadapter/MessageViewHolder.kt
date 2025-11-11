package com.example.safagive.ui.chat.message.messageadapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.safagive.Message
import com.example.safagive.databinding.ItemLayoutMessageBinding

class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemLayoutMessageBinding.bind(view)
    fun render(message: Message){
        Log.d("mensaje", message.message)
        binding.textViewContent.text = message.message
        binding.textViewDate.text = message.date
        binding.textViewUser.text = message.user
    }
}