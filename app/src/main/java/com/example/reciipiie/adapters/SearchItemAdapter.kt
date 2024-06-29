package com.example.reciipiie.adapters

import android.content.Context
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.reciipiie.R
import com.example.reciipiie.api.RandomRecipeApi
import com.example.reciipiie.api.RetrofitHelper
import com.example.reciipiie.dao.FavoriteRecipeDatabase
import com.example.reciipiie.models.FavoriteRecipeModel
import com.example.reciipiie.models.Recipe
import com.example.reciipiie.models.SearchModel
import com.example.reciipiie.repository.RecipeRepository
import com.example.reciipiie.viewmodels.MainViewModel
import com.example.reciipiie.viewmodels.MainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchItemAdapter(private var items: List<SearchModel>) : RecyclerView.Adapter<SearchItemAdapter.ItemViewHolder>() {


    private lateinit var favoriteRecipeDatabase: FavoriteRecipeDatabase

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.recipe_item_txt)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_rec_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.textView.text = items[position].title
        val id = items[position].id

        holder.itemView.setOnClickListener {

            var mainViewModel: MainViewModel
            var recipeInformation: Recipe?= null


            val recipeDetailsService = RetrofitHelper.getInstance().create(RandomRecipeApi::class.java)
            val repository = RecipeRepository(recipeDetailsService)

            // Creating a new bottom sheet dialog.
             val dialog = BottomSheetDialog(holder.itemView.context)

            // Inflating the layout file which we have created.
            val inflater = LayoutInflater.from(holder.itemView.context)
            val view = inflater.inflate(R.layout.bottom_sheet_layout, null)


            mainViewModel = ViewModelProvider(
                holder.itemView.context as ViewModelStoreOwner,
                MainViewModelFactory(repository)
            ).get(MainViewModel::class.java)

            // Observe recipe details from ViewModel
            mainViewModel.fetchRecipeDetails(id)
            mainViewModel.recipe.observe(holder.itemView.context as LifecycleOwner, Observer { recipe ->
                // Update UI with recipe details
                recipeInformation = recipe
                loadEverything(recipeInformation!!, holder.itemView.context, view)

            })

            val addToWishlistBtn: ToggleButton = view.findViewById(R.id.add_to_wishlist_btn)
            val btnClose = view.findViewById<ImageView>(R.id.bottom_sheet_view_back_btn)
            btnClose.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    recipeInformation?.let {
                        if (addToWishlistBtn.isChecked) {
                            if (!favoriteRecipeDatabase.favoriteRecipeDao().isRecipeExists(id)) {
                                favoriteRecipeDatabase.favoriteRecipeDao()
                                    .insertRecipeDetails(FavoriteRecipeModel(id, it.title, it.image))
                            }
                        } else {
                            if (favoriteRecipeDatabase.favoriteRecipeDao().isRecipeExists(id)) {
                                favoriteRecipeDatabase.favoriteRecipeDao()
                                    .deleteRecipeDetails(FavoriteRecipeModel(it.id, it.title, it.image))
                            }
                        }
                    }

                    dialog.dismiss()
                }

            }



            val btnGetIngredients: Button = view.findViewById(R.id.get_ingredients_btn)
            val btnGetFullRecipe: Button = view.findViewById(R.id.get_full_recipe_btn)
            val btnGetSimilarRecipe: Button = view.findViewById(R.id.get_similar_recipe_btn)

            val lowerPartRecipeName = view.findViewById<View>(R.id.lower_part_recipe_name)
            val lowerPartIngredients = view.findViewById<View>(R.id.lower_part_ingredients)
            val lowerPartFullRecipe = view.findViewById<View>(R.id.lower_part_full_recipe)

            val fullRecipeTextView = view.findViewById<TextView>(R.id.full_recipe_txt)
            val ingredientsTextView = view.findViewById<TextView>(R.id.ingredients_text)



            lowerPartRecipeName.visibility = View.VISIBLE
            lowerPartIngredients.visibility = View.GONE
            lowerPartFullRecipe.visibility = View.GONE


            btnGetIngredients.setOnClickListener {
                lowerPartRecipeName.visibility = View.GONE
                btnGetIngredients.visibility = View.GONE
                fullRecipeTextView.visibility = View.GONE
                lowerPartFullRecipe.visibility = View.GONE

                ingredientsTextView.visibility=View.VISIBLE
                btnGetFullRecipe.visibility = View.VISIBLE
                lowerPartIngredients.visibility = View.VISIBLE
            }
            btnGetFullRecipe.setOnClickListener {
                btnGetFullRecipe.visibility = View.GONE
                lowerPartRecipeName.visibility = View.GONE
                lowerPartIngredients.visibility = View.GONE

                fullRecipeTextView.visibility = View.VISIBLE
                lowerPartFullRecipe.visibility = View.VISIBLE
                btnGetSimilarRecipe.visibility = View.VISIBLE
            }
            btnGetSimilarRecipe.setOnClickListener {
                btnGetSimilarRecipe.visibility = View.GONE
                // Implement similar recipe fetching logic here
            }

            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun loadEverything(recipeInformation: Recipe, context: Context, view:View) {


        val addToWishlistBtn: ToggleButton = view.findViewById(R.id.add_to_wishlist_btn)

        val currentId = recipeInformation.id
        favoriteRecipeDatabase = FavoriteRecipeDatabase.getDatabase(context)

        favoriteRecipeDatabase.favoriteRecipeDao().getAllRecipeDetails().observe(context as LifecycleOwner, Observer { recipes ->
            val isPresent = recipes.any { it.recipeId == currentId }
            addToWishlistBtn.isChecked = isPresent
        })





        Glide.with(context)
            .load(recipeInformation.image)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(50)))
            .placeholder(R.drawable.placeholder_icon)
            .into(view.findViewById(R.id.recipe_item_image_view))



        val recipeName = view.findViewById<TextView>(R.id.recipe_item_name)
        val recipeTime = view.findViewById<TextView>(R.id.recipe_time)
        val searvingNo = view.findViewById<TextView>(R.id.serving_no)
        val price = view.findViewById<TextView>(R.id.price)
        val ingredientsTextView = view.findViewById<TextView>(R.id.ingredients_text)

        recipeName.text= recipeInformation.title
        recipeTime.text= recipeInformation.readyInMinutes.toString()
        searvingNo.text =recipeInformation.servings.toString()
        price.text= recipeInformation.pricePerServing.toString()

        val gridView: GridView = view.findViewById(R.id.gridView)
        val ingredientList = recipeInformation.extendedIngredients
        //Grid view
        val adapter = IngredientAdapterGrid(view.context, ingredientList)
        gridView.adapter = adapter
        Log.d("Subha", ingredientList.toString())


        val instructionsDetails = view.findViewById<TextView>(R.id.instructions_details)
        val recipeQuickSummary =view.findViewById<TextView>(R.id.recipe_quick_summary)

        val badForHealthTextView= view.findViewById<TextView>(R.id.bad_for_health_text_view)
        val goodForHealthTextView= view.findViewById<TextView>(R.id.good_for_health_text_view)
        val nutritionTextView = view.findViewById<TextView>(R.id.all_detailed_nutrition)

        val similarRecipeTextView = view.findViewById<TextView>(R.id.similar_recipe)
        val similarRecView = view.findViewById<RecyclerView>(R.id.similar_recipe_rec_view)

        instructionsDetails.text = recipeInformation.instructions
        recipeQuickSummary.text = recipeInformation.summary

        if(recipeInformation.veryHealthy){
            badForHealthTextView.text="Very Healthy"
            goodForHealthTextView.text="Good for health"
        }else{
            badForHealthTextView.text="Not Very Healthy"
            goodForHealthTextView.text= "Not Good for Health"
        }
        nutritionTextView.text= "Nutrition for us"


    }


}
