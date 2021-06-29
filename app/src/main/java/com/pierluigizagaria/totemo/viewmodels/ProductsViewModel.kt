package com.pierluigizagaria.totemo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.pierluigizagaria.totemo.models.Product
import com.pierluigizagaria.totemo.repositories.ProductsRepository

class ProductsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductsRepository = ProductsRepository(application)
    private val allProducts: LiveData<List<Product>> = repository.allLiveData

    val allProductsLiveData: LiveData<List<Product>>
        get() = allProducts

    fun insert(product: Product) {
        repository.insert(product)
    }

    fun update(product: Product) {
        repository.update(product)
    }

    fun delete(id: Long) {
        repository.delete(id)
    }
}