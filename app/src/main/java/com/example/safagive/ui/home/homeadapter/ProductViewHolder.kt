package com.example.safagive.ui.home.homeadapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.safagive.Product
import com.example.safagive.databinding.ItemLayoutProductBinding
import com.example.safagive.ui.article.ArticleDetails

class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemLayoutProductBinding.bind(view)

    fun render(product: Product){
        binding.textViewCardProductName.text = product.title
        binding.item.setOnClickListener{
            val intent = Intent(binding.textViewCardProductName.context, ArticleDetails::class.java).apply {
                putExtra("producto", product)
            }
            binding.productCard.context.startActivity(intent)
        }
        //Glide.with(itemView).load(itemView.context.resources.getIdentifier(superHeroe.foto, "drawable", itemView.context.packageName)).into(binding.ivSuperHeroe)
    }


}