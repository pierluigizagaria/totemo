package com.pierluigizagaria.totemo.routes.product

import com.pierluigizagaria.totemo.WebServer
import com.pierluigizagaria.totemo.repositories.ProductsRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getProductImage(mProductsRepository: ProductsRepository) {
    get("{id}/image") {
        val id: Long = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                WebServer.Error(HttpStatusCode.BadRequest)
            )
        val product = mProductsRepository.getById(id)
            ?: return@get call.respond(
                HttpStatusCode.NotFound,
                WebServer.Error(HttpStatusCode.NotFound)
            )
        val image = mProductsRepository.getImage(product)
        image?.let {
            call.response.status(HttpStatusCode.OK)
            return@get call.respondFile(it)
        }
        call.respond(
            HttpStatusCode.NotFound,
            WebServer.Error(HttpStatusCode.NotFound)
        )
    }
}