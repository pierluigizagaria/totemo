package com.pierluigizagaria.totemo.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.MaterialContainerTransform
import com.pierluigizagaria.totemo.R


class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {
    companion object {
        const val SHARED_TRANSITION_DURATION: Long = 350
        private val TAG = ProductDetailsFragment::class.simpleName
        private const val INGREDIENTS_FONT_PERCENTAGE = 4
    }

    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSharedElementTransition()
        setContent(args.productName, args.ingredients, args.imageUrl)
    }

    fun setContent(name: String, ingredients: Array<String>, imageUrl: String?) {
        postponeEnterTransition()
        requireView().findViewById<TextView>(R.id.productNameText)
            .text = name

        val ingredientsText = ingredients
            .joinToString(",")
            .replaceFirstChar { it.uppercase() }

        val productIngredientsText =
            requireView().findViewById<TextView>(R.id.productIngredientText)
        productIngredientsText.text = String.format("%s.", ingredientsText)

        val fontSize = requireActivity().window.decorView.width * INGREDIENTS_FONT_PERCENTAGE / 100
        productIngredientsText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize.toFloat())

        imageUrl?.let {
            Glide.with(requireContext().applicationContext)
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
                .into(requireView().findViewById(R.id.productImage))
        }
        imageUrl ?: startPostponedEnterTransition()

        startPostponedEnterTransition()
    }

    fun setSharedElementTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = SHARED_TRANSITION_DURATION
        }
        requireView().transitionName = args.transitionName
    }

    fun setupBackButton() {
        requireView().findViewById<ImageButton>(R.id.backButton)
            .setOnClickListener { findNavController().popBackStack() }
    }
}