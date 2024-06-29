package com.example.reciipiie.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reciipiie.models.FavoriteRecipeModel


@Database(entities = [FavoriteRecipeModel::class], version = 1)
abstract class FavoriteRecipeDatabase: RoomDatabase() {
    abstract fun favoriteRecipeDao():FavoriteRecipeDAO

    companion object{
        @Volatile
        private var INSTANCE: FavoriteRecipeDatabase? = null
        fun getDatabase(context: Context): FavoriteRecipeDatabase{
            if(INSTANCE== null){
                synchronized(this){
                    INSTANCE= Room.databaseBuilder(context.applicationContext,
                        FavoriteRecipeDatabase::class.java,"accountDB").build()
                }
            }
            return INSTANCE!!
        }
    }
}