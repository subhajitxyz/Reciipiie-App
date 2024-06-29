package com.example.reciipiie.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.reciipiie.R
import com.example.reciipiie.RecipeDetailsActivity
import com.example.reciipiie.models.Recipe
import com.example.reciipiie.models.SearchModel


class VerticleRecipeAdapter(private val context: Context,private var recipes: List<SearchModel>) :
    RecyclerView.Adapter<VerticleRecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.all_recipes_rec_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipe: SearchModel) {
            itemView.findViewById<TextView>(R.id.item_recipe_name).text = recipe.title
            itemView.findViewById<TextView>(R.id.item_recipe_time).text =
                "ready in 40 min"

            // Load image using Glide or any other image loading library
            Glide.with(context)
                .load(recipe.image)
                .apply(RequestOptions()
                    .transform(CenterCrop(),RoundedCorners(20)))
                .placeholder(R.drawable.placeholder_icon)
                .into(itemView.findViewById(R.id.recipe_item_image_view))


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, RecipeDetailsActivity::class.java).apply {
                    putExtra("recipe_id", recipe.id)
                }
                itemView.context.startActivity(intent)
            }



        }
    }
}