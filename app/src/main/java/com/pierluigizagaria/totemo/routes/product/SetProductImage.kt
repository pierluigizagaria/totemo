package com.pierluigizagaria.totemo.routes.product

import android.graphics.BitmapFactory
import com.pierluigizagaria.totemo.WebServer
import com.pierluigizagaria.totemo.repositories.ProductsRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.setProductImage(mProductsRepository: ProductsRepository) {
    post("{id}/image") {
        val id: Long = call.parameters["id"]?.toLongOrNull()
            ?: return@post call.respond(
                HttpStatusCode.BadRequest,
                WebServer.Error(HttpStatusCode.BadRequest)
            )
        val product = mProductsRepository.getById(id)
            ?: return@post call.respond(
                HttpStatusCode.NotFound,
                WebServer.Error(HttpStatusCode.NotFound)
            )
        val multipartData = call.receiveMultipart()
        val files = multipartData.readAllParts().filterIsInstance<PartData.FileItem>()
        val bytes = files.first().streamProvider().readBytes()
        if (bytes.isEmpty()) return@post call.respond(
            HttpStatusCode.BadRequest,
            WebServer.Error(HttpStatusCode.BadRequest)
        )
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        mProductsRepository.setImage(product, bitmap)
        call.respond(HttpStatusCode.OK, HttpStatusCode.NoContent)
    }
}