package com.pierluigizagaria.totemo.ui.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pierluigizagaria.totemo.R
import com.pierluigizagaria.totemo.models.Product
import com.pierluigizagaria.totemo.repositories.ProductsRepository
import kotlinx.coroutines.launch


class ScannedProductActivity : AppCompatActivity() {
    companion object {
        const val BARCODE_EXTRA = "barcode"
        private val TAG = ScannedProductActivity::class.simpleName
        private const val INGREDIENTS_FONT_PERCENTAGE = 4
    }

    private lateinit var productsRepository: ProductsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_product_details)
        setupBackButton()

        productsRepository = ProductsRepository(application)

        lifecycleScope.launch {
            val product = getScannedProduct()
            if (product == null) {
                Toast.makeText(baseContext, "Prodotto non trovato", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                setContent(product.name, product.ingredients, productsRepository.getImage(product)?.absolutePath)
            }
        }
    }

    fun getPluFromBarCode(barCode: String): Int? = try {
        barCode.substring(1, 6).toInt()
    } catch (ex: IndexOutOfBoundsException) {
        null
    }

    suspend fun getScannedProduct(): Product? {
        val scannedBarcode = intent.getStringExtra(BARCODE_EXTRA)
        scannedBarcode?.let {
            getPluFromBarCode(scannedBarcode)?.let {
                return productsRepository.getByPluAsync(it)
            }
        }
        return null
    }

    fun setContent(name: String, ingredients: Array<String>, imageUrl: String?) {
        startPostponedEnterTransition()

        findViewById<TextView>(R.id.productNameText)
            .text = name.lowercase().replaceFirstChar{ it.uppercase() }

        val ingredientsText = ingredients
            .joinToString(", ")
            .lowercase()
            .replaceFirstChar { it.uppercase() }

        val productIngredientsText = findViewById<TextView>(R.id.productIngredientText)
        productIngredientsText.text = String.format("%s.", ingredientsText)

        val fontSize = window.decorView.width * INGREDIENTS_FONT_PERCENTAGE / 100
        productIngredientsText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize.toFloat())

        imageUrl?.let {
            Glide.with(this)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                })
                .into(findViewById(R.id.productImage))
        }
    }

    fun setupBackButton() {
        findViewById<ImageButton>(R.id.backButton)
            .setOnClickListener { finish() }
    }
}