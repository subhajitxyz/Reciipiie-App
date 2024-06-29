package com.example.reciipiie.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.reciipiie.R
import com.example.reciipiie.models.ExtendedIngredient

class IngredientAdapter(private val context: Context, private val ingredientList: List<ExtendedIngredient>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ingredients_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        holder.itemIngredientName.text = ingredient.name

        // Load image using Glide or any other image loading library
        Glide.with(context)
            .load(ingredient.image)
            .apply(
                RequestOptions()
                .transform(CenterCrop(), RoundedCorners(20)))
            .placeholder(R.drawable.placeholder_icon)
            .into(holder.itemIngredientImage)

        // Assuming you have a method to load the image into the ImageView
        // holder.itemIngredientImage.setImageResource(ingredient.imageResId)
        // Use Glide or another library to load image asynchronously
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemIngredientImage: ImageView = itemView.findViewById(R.id.item_ingredient_image)
        val itemIngredientName: TextView = itemView.findViewById(R.id.item_ingredient_name)
    }
}
