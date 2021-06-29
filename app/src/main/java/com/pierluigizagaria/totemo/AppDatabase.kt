package com.pierluigizagaria.totemo

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pierluigizagaria.totemo.dao.ProductsDao
import com.pierluigizagaria.totemo.models.Product
import com.pierluigizagaria.totemo.utils.Converters

@Database(entities = [Product::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productsDao(): ProductsDao

    companion object {

        private const val SUB_PATH = "database"
        private const val FILE_NAME = "app"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                getDatabasePath(context)
            )
                .fallbackToDestructiveMigration()
                .build()

        private fun getDatabasePath(context: Context): String =
            Uri.Builder()
                .scheme("file")
                .appendPath(context.getExternalFilesDir(null)!!.absolutePath)
                .appendPath(SUB_PATH)
                .appendPath(String.format("%s.db", FILE_NAME))
                .build().path.toString()
    }
}