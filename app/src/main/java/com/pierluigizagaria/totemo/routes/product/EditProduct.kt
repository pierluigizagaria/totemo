package com.pierluigizagaria.totemo.routes.product

import com.pierluigizagaria.totemo.WebServer
import com.pierluigizagaria.totemo.models.Product
import com.pierluigizagaria.totemo.repositories.ProductsRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.editProduct(mProductsRepository: ProductsRepository) {
    fun trimString(string: String)
            = string.lowercase().trim().replace(Regex("\\s+"), " ");
    put patch@{
        var product = try {
            call.receive(Product::class)
        } catch (e: ContentTransformationException) {
            return@patch call.respond(
                HttpStatusCode.BadRequest,
                WebServer.Error(HttpStatusCode.BadRequest)
            )
        }
        if (product.id == null ||
            product.plu == 0 ||
            product.name.isBlank() ||
            product.ingredients.isNullOrEmpty()
        ) {
            return@patch call.respond(
                HttpStatusCode.BadRequest,
                WebServer.Error(HttpStatusCode.BadRequest)
            )
        }
        product.name = trimString(product.name);
        product.ingredients.forEachIndexed { index, value ->
            product.ingredients[index] = trimString(value);
        }
        if (mProductsRepository.update(product) == 0) {
            return@patch call.respond(
                HttpStatusCode.NotFound,
                WebServer.Error(HttpStatusCode.NotFound)
            )
        }
        call.respond(HttpStatusCode.OK, product)
    }
}

