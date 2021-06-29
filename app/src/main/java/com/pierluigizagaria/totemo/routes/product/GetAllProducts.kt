package com.pierluigizagaria.totemo.routes.product

import android.content.Context
import com.pierluigizagaria.totemo.repositories.ProductsRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

class ProductsResponse(
    val id: Long,
    val plu: Int,
    val name: String,
    val ingredients: Array<String>)

fun Route.getAllProducts(mProductsRepository: ProductsRepository, mContext: Context) {
    get {
        val products = mProductsRepository.all
        val response: List<ProductsResponse> = products.map {
            ProductsResponse(it.id!!, it.plu, it.name, it.ingredients)
        }
        call.respond(HttpStatusCode.OK, response)
    }
}