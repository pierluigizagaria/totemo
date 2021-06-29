package com.pierluigizagaria.totemo.routes.product

import com.pierluigizagaria.totemo.WebServer
import com.pierluigizagaria.totemo.repositories.ProductsRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.deleteProduct(mProductsRepository: ProductsRepository) {
    delete("{id}") {
        val id: Long = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                WebServer.Error(HttpStatusCode.BadRequest)
            )
        if (mProductsRepository.delete(id) == 0) {
            call.respond(HttpStatusCode.NotFound, WebServer.Error(HttpStatusCode.NotFound))
            return@delete
        }
        call.respond(HttpStatusCode.OK, HttpStatusCode.NoContent)
    }
}