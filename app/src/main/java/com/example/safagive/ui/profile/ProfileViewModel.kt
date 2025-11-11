package com.example.safagive.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _products = MutableLiveData<List<String>>()
    val products: LiveData<List<String>> get() = _products
    // En algún lugar de tu ViewModel, establece los productos
    init {
        // Datos de prueba
        val productosPrueba = listOf("Producto 1", "Producto 2", "Producto 3")
        _products.value = productosPrueba
    }
}
