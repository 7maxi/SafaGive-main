package com.example.safagive

import com.google.firebase.database.PropertyName
import java.io.Serializable


data class Product(
    @PropertyName("categoria") val category: String,
    @PropertyName("descripcion") val description: String,
    @PropertyName("fotos") val photos: Map<String, String> = emptyMap(),
    @PropertyName("titulo") val title: String,
    @PropertyName("usuario") val user: String
): Serializable {
    constructor() : this("", "", emptyMap(), "", "")
}

