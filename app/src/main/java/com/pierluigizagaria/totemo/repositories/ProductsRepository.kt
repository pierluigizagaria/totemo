package com.pierluigizagaria.totemo.repositories

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.pierluigizagaria.totemo.AppDatabase
import com.pierluigizagaria.totemo.ExternalStorageImageManager
import com.pierluigizagaria.totemo.dao.ProductsDao
import com.pierluigizagaria.totemo.models.Product
import java.io.File
import java.util.*

class ProductsRepository(application: Application) {
    companion object {
        const val IMAGES_SUB_PATH = "products/images"
    }

    private val productsDao: ProductsDao
    private val allProducts: LiveData<List<Product>>
    private val externalStorageImageManager: ExternalStorageImageManager

    init {
        val database = AppDatabase.getInstance(application)
        externalStorageImageManager =
            ExternalStorageImageManager(application.applicationContext, IMAGES_SUB_PATH)
        productsDao = database.productsDao()
        allProducts = productsDao.allLiveData
    }

    val all: List<Product>
        get() = productsDao.all

    val allLiveData: LiveData<List<Product>>
        get() = productsDao.allLiveData


    fun insert(product: Product): Long {
        return productsDao.insert(product)
    }

    fun update(product: Product): Int {
        return productsDao.update(product)
    }

    fun delete(id: Long): Int {
        return productsDao.delete(id)
    }

    fun getById(id: Long): Product? {
        return productsDao.getById(id)
    }

    fun getByPlu(plu: Int): Product? {
        return productsDao.getByPlu(plu)
    }

    suspend fun getByPluAsync(plu: Int): Product? {
        return productsDao.getByPluAsync(plu)
    }

    fun getImage(product: Product): File? {
        return if (product.imageUUID == null) null
        else externalStorageImageManager.setFileName(product.imageUUID!!).file
    }

    fun setImage(product: Product, bitmap: Bitmap) {
        if (product.imageUUID != null) {
            externalStorageImageManager.setFileName(product.imageUUID!!).file?.delete()
        }
        val uuid = UUID.randomUUID().toString()
        product.imageUUID = uuid
        externalStorageImageManager.setFileName(uuid).save(bitmap)
        productsDao.update(product)
    }
}