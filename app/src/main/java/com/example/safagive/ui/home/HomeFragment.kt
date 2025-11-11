package com.example.safagive.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safagive.Login
import com.example.safagive.Product
import com.example.safagive.databinding.FragmentHomeBinding
import com.example.safagive.ui.home.homeadapter.CategoryAdapter
import com.example.safagive.ui.home.homeadapter.ProductAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private var database: DatabaseReference? = null
    private var productTable: MutableList<Product> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadCategoryReciclerView()

        database = FirebaseDatabase.getInstance("https://safagive-44998-default-rtdb.europe-west1.firebasedatabase.app/").reference

        updateProductTable()

        textFilter()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateProductTable() {
        val filter = Filter.name
        database?.child("productos")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                productTable.clear()
                if (dataSnapshot.exists()) {
                    for (productoSnapshot in dataSnapshot.children) {
                        val categoria = productoSnapshot.child("categoria").getValue(String::class.java) ?: ""
                        if (filter == "Todos" || categoria == filter){
                            val descripcion = productoSnapshot.child("descripcion").getValue(String::class.java) ?: ""
                            val fotosSnapshot = productoSnapshot.child("fotos").value as? Map<String, String> ?: emptyMap()
                            val titulo = productoSnapshot.child("titulo").getValue(String::class.java) ?: ""
                            val usuario = productoSnapshot.child("usuario").getValue(String::class.java) ?: ""
                            if(usuario != Login.email){
                                val producto = Product(categoria, descripcion, fotosSnapshot, titulo, usuario)
                                productTable.add(producto)
                            }
                        }
                    }
                }
                loadProductReciclerView(productTable)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadCategoryReciclerView(){
        val buttonNames = listOf("Todos", "Ropa y accesorios", "Libros", "Material escolar", "Tecnología")
        val categoriesRecicler = binding.categoryRecyclerView
        categoriesRecicler.adapter = CategoryAdapter(buttonNames) { updateProductTable() }
        categoriesRecicler.layoutManager = LinearLayoutManager(categoriesRecicler.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadProductReciclerView(productTable: MutableList<Product>){
        if (productTable.size == 0){
            binding.noResultsReciclerView.visibility = View.VISIBLE
            binding.productRecyclerView.visibility = View.INVISIBLE
        }else{
            binding.noResultsReciclerView.visibility = View.INVISIBLE
            val productsRecicler = binding.productRecyclerView
            productsRecicler.visibility = View.VISIBLE
            productsRecicler.layoutManager = GridLayoutManager(productsRecicler.context, 2)
            productsRecicler.adapter = ProductAdapter(productTable)
        }
    }

    private fun textFilter(){
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { searchTextChanged(it.toString()) }
            }

            override fun afterTextChanged(s: Editable?) {
                // Nothing
            }
        })
    }

    private fun searchTextChanged(stringToSearch: String) {
        val productFilteredTable: MutableList<Product> = mutableListOf()
        for (product in productTable){
            if (product.title.contains(stringToSearch)){
                productFilteredTable.add(product)
            }
        }
        loadProductReciclerView(productFilteredTable)
    }

    // Static variable to be used in ActionsListeners
    class Filter {
        companion object {
            var name : String = "Todos"
        }
    }

}