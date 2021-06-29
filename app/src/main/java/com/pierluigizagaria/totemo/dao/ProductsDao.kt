package com.pierluigizagaria.totemo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pierluigizagaria.totemo.models.Product

@Dao
interface ProductsDao {
    @Insert
    fun insert(product: Product): Long

    @Update
    fun update(product: Product): Int

    @Query("DELETE FROM products_table WHERE id = (:arg0)")
    fun delete(arg0: Long): Int

    @Query("SELECT * FROM products_table WHERE id = (:arg0)")
    fun getById(arg0: Long): Product?

    @Query("SELECT * FROM products_table WHERE plu = (:arg0)")
    fun getByPlu(arg0: Int): Product?

    @Query("SELECT * FROM products_table WHERE plu = (:arg0)")
    suspend fun getByPluAsync(arg0: Int): Product?

    @get:Query("SELECT * FROM products_table ORDER BY name")
    val all: List<Product>

    @get:Query("SELECT * FROM products_table ORDER BY name")
    val allLiveData: LiveData<List<Product>>
}