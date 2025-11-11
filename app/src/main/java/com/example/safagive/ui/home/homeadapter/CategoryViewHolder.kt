package com.example.safagive.ui.home.homeadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.safagive.databinding.ItemLayoutCategoriesBinding
import com.example.safagive.ui.home.HomeFragment

class CategoryViewHolder(view: View, private val updateProductTable: () -> Unit): RecyclerView.ViewHolder(view) {
    private val binding = ItemLayoutCategoriesBinding.bind(view)

    fun render(category: String){
        binding.button.text = category
        binding.button.setOnClickListener{
            HomeFragment.Filter.name = binding.button.text as String
            updateProductTable()
        }
    }
}