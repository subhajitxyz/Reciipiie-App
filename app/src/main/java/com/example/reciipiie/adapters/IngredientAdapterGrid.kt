package com.example.reciipiie.adapters

// IngredientAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.reciipiie.R
import com.example.reciipiie.models.ExtendedIngredient


class IngredientAdapterGrid(
    private val context: Context,
    private val ingredients: List<ExtendedIngredient>
) : BaseAdapter() {

    override fun getCount(): Int = ingredients.size

    override fun getItem(position: Int): Any = ingredients[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.ingredients_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val ingredient = ingredients[position]
        viewHolder.itemIngredientName.text = ingredient.name

        // Load image using Glide or any other image loading library
        Glide.with(context)
            .load(ingredient.image)
            .apply(
                RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(20)))
            .placeholder(R.drawable.placeholder_icon)
            .into(viewHolder.itemIngredientImage)

        return view
    }

    private class ViewHolder(view: View) {
        val itemIngredientImage: ImageView = view.findViewById(R.id.item_ingredient_image)
        val itemIngredientName: TextView = view.findViewById(R.id.item_ingredient_name)
    }
}
