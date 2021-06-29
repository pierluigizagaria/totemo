package com.pierluigizagaria.totemo.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pierluigizagaria.totemo.R
import com.pierluigizagaria.totemo.ui.EditProductsBottomSheet
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class ProductsCarousel : Fragment(R.layout.fragment_products_carousel) {
    companion object {
        private val TAG = ProductsCarousel::class.simpleName
        private const val CLICKS_WINDOW_MILLIS = 1000
        private const val REQUIRED_CLICKS = 5
    }

    private var clicks = 0
    private var firstClickTime: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupCarousel()
        setupSecretBar()
    }

    fun setupSecretBar() {
        val secretBar = requireView().findViewById<View>(R.id.secretBarCarousel)
        secretBar.setOnClickListener {
            if (System.currentTimeMillis() - firstClickTime >= CLICKS_WINDOW_MILLIS) clicks = 0
            if (clicks == 0) firstClickTime = System.currentTimeMillis()
            if (++clicks == REQUIRED_CLICKS) showEditProductBottomSheet()
        }
    }

    fun showEditProductBottomSheet() {
        val bottomSheet = EditProductsBottomSheet()
        bottomSheet.show(requireActivity().supportFragmentManager, TAG)
    }

    fun setupCarousel() {
        val carousel: ImageCarousel = requireView().findViewById(R.id.productsImageCarousel)
        carousel.registerLifecycle(lifecycle)
        val list = mutableListOf<CarouselItem>()

        /*
        list.add(
            CarouselItem(
                imageUrl = "https://scontent-fco1-1.cdninstagram.com/v/t51.2885-15/e35/s1080x1080/191157725_865605397503166_8735988420992489898_n.jpg?tp=1&_nc_ht=scontent-fco1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=bj96r6vc_jEAX_IPHZR&edm=AABBvjUBAAAA&ccb=7-4&oh=40835a1d597cd9b377216da1451a7ac8&oe=60B78BB7&_nc_sid=83d603",
                caption = "Lorem ipsum dolor sir"
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://scontent-fco1-1.cdninstagram.com/v/t51.2885-15/e35/s1080x1080/189745464_341322590714194_460294038508229659_n.jpg?tp=1&_nc_ht=scontent-fco1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=v5d8eBGVs94AX8TjC0a&edm=AABBvjUBAAAA&ccb=7-4&oh=4f09a0efe2b56ac7f49e401c2497bdfd&oe=60B789D4&_nc_sid=83d603",
                caption = "Amiamo la natura e per questo scegliamo solo carni provenienti da allevamenti sani e sostenibili.\n" +
                        "Ogni gesto può fare la differenza.\n" +
                        "➡️ Scopri il nostro distributore automatico per ridurre il consumo di plastica.\n" +
                        "RIDUCI. RIUSA. RICICLA.♻️"
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://scontent-fco1-1.cdninstagram.com/v/t51.2885-15/e35/181909092_3943867695697638_3012455663504407750_n.jpg?tp=1&_nc_ht=scontent-fco1-1.cdninstagram.com&_nc_cat=106&_nc_ohc=OJM8ryu0OasAX8LUJ3X&edm=AABBvjUBAAAA&ccb=7-4&oh=6b24850d1249014271ddbf3f8dd0caa9&oe=60B673A2&_nc_sid=83d603",
                caption = "Il carpaccio di manzo è un secondo veloce e leggero ma pieno di sapore!\n" +
                        "Provalo condito con olio, limone e capperi."
            )
        )
        carousel.setData(list)
        */
    }
}