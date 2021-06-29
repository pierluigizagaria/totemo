package com.pierluigizagaria.totemo

import android.app.Application
import com.pierluigizagaria.totemo.repositories.ProductsRepository
import com.pierluigizagaria.totemo.routes.product.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import java.io.File

class WebServer(application: Application) {
    companion object {
        const val PORT = 8080
    }

    private val productsRepository = ProductsRepository(application)
    private val webClient = WebClient.getInstance(application.applicationContext)
    private val embeddedServerKt = embeddedServer(Jetty, PORT) {
        install(DefaultHeaders)
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing {
            route("/products") {
                getAllProducts(productsRepository, application.applicationContext)
                getProductById(productsRepository)
                createProduct(productsRepository)
                editProduct(productsRepository)
                deleteProduct(productsRepository)
                setProductImage(productsRepository)
                getProductImage(productsRepository)
            }
            static {
                val webClientPath = webClient.path
                files(webClientPath)
                default(File(webClientPath, "index.html"))
            }
        }
    }

    fun start() {
        embeddedServerKt.start(wait = false)
    }

    fun stop() {
        embeddedServerKt.stop(0, 0)
    }

    data class StatusObject(val status: Int, val message: String)

    class Error(httpStatusCode: HttpStatusCode) {
        val error: StatusObject = StatusObject(httpStatusCode.value, httpStatusCode.description)
    }
}

