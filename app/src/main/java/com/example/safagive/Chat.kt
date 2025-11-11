package com.example.safagive

import com.google.firebase.database.PropertyName
import java.io.Serializable
data class Chat(
    @PropertyName("usuario1") val user1:String  = "",
    @PropertyName("usuario2") val user2:String  = "",
    @PropertyName("mensajes") val messages: List<Message>  = emptyList()
):Serializable{
    constructor() : this("", "", emptyList())
}