package com.example.safagive

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class Message(
    @PropertyName("fecha") val date: String,
    @PropertyName("mensaje") val message: String,
    @PropertyName("usuario") val user: String
): Serializable {
    constructor() : this("", "", "")
}