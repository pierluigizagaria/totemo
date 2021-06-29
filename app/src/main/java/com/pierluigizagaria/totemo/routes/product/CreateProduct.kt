package com.pierluigizagaria.totemo.routes.product

import com.pierluigizagaria.totemo.WebServer
import com.pierluigizagaria.totemo.models.Product
import com.pierluigizagaria.totemo.repositories.ProductsRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createProduct(mProductsRepository: ProductsRepository) {
    fun trimString(string: String)
            = string.lowercase().trim().replace(Regex("\\s+"), " ");
    post {
        var product = try {
            call.receive(Product::class)
        } catch (e: Exception) {
            return@post call.respond(
                HttpStatusCode.BadRequest,
                WebServer.Error(HttpStatusCode.BadRequest)
            )
        }
        if (product.plu == 0 ||
            product.name.isBlank() ||
            product.ingredients.isNullOrEmpty()
        ) {
            return@post call.respond(
                HttpStatusCode.BadRequest,
                WebServer.Error(HttpStatusCode.BadRequest)
            )
        }
        product.ingredients.forEachIndexed { index, value ->
            product.ingredients[index] = trimString(value);
        }
        product.id = mProductsRepository.insert(
            Product(product.plu, trimString(product.name), product.ingredients)
        )
        call.respond(HttpStatusCode.Created, product)
    }
}