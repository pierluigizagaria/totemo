package com.pierluigizagaria.totemo.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.Hold
import com.pierluigizagaria.totemo.R
import com.pierluigizagaria.totemo.models.Product
import com.pierluigizagaria.totemo.ui.EditProductsBottomSheet
import com.pierluigizagaria.totemo.utils.GridSpacingItemDecoration
import com.pierluigizagaria.totemo.viewmodels.ProductsViewModel
import java.io.File


class ProductsFragment : Fragment(R.layout.fragment_products), ProductsAdapter.OnItemClickListener {
    companion object {
        private val TAG = ProductsFragment::class.simpleName
        private const val CLICKS_WINDOW_MILLIS = 1000
        private const val REQUIRED_CLICKS = 5
        private const val MOTION_TRANSITION_COMPLETED = 1F
    }

    private var clicks = 0
    private var firstClickTime: Long = 0
    private var hasMotionScrolled = false

    override fun onPause() {
        super.onPause()
        saveMotionLayoutProgress()
    }

    override fun onResume() {
        super.onResume()
        restoreMotionLayoutProgress()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransitionAnimations()
        setupProductRecycleView()
        setupSecretBar()
    }

    fun setupProductRecycleView() {
        val recycleView = requireView().findViewById<RecyclerView>(R.id.productList)
        recycleView.addItemDecoration(GridSpacingItemDecoration(requireContext(), 2, 24))
        recycleView.setHasFixedSize(true)

        val adapter = ProductsAdapter(requireActivity().application)
        adapter.setOnItemClickListener(this)
        recycleView.adapter = adapter

        val factory = AndroidViewModelFactory.getInstance(requireActivity().application)
        val productsViewModel = ViewModelProvider(this, factory).get(
            ProductsViewModel::class.java
        )

        productsViewModel.allProductsLiveData.observe(viewLifecycleOwner) { list: List<Product?> ->
            adapter.submitList(list)
        }
    }

    fun setupSecretBar() {
        val secretBar = requireView().findViewById<View>(R.id.secretBar)
        secretBar.setOnClickListener {
            if (System.currentTimeMillis() - firstClickTime >= CLICKS_WINDOW_MILLIS) clicks = 0
            if (clicks == 0) firstClickTime = System.currentTimeMillis()
            if (++clicks == REQUIRED_CLICKS) showEditProductBottomSheet()
        }
    }

    fun setupTransitionAnimations() {
        exitTransition = Hold().apply {
            duration = ProductDetailsFragment.SHARED_TRANSITION_DURATION
        }
        postponeEnterTransition()
        requireView().doOnPreDraw {
            restoreMotionLayoutProgress()
            startPostponedEnterTransition()
        }
    }

    fun showEditProductBottomSheet() {
        val bottomSheet = EditProductsBottomSheet()
        bottomSheet.show(requireActivity().supportFragmentManager, TAG)
    }

    fun saveMotionLayoutProgress() {
        hasMotionScrolled = requireView().findViewById<MotionLayout>(R.id.collapsableToolbar)
            .progress > MOTION_TRANSITION_COMPLETED / 2
    }

    fun restoreMotionLayoutProgress() {
        if (hasMotionScrolled) requireView().findViewById<MotionLayout>(R.id.collapsableToolbar)
            .progress = MOTION_TRANSITION_COMPLETED
    }

    override fun onItemClick(view: View, product: Product, image: File?) {
        /*
        val action = ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
            product.name,
            product.ingredients,
            image?.absolutePath,
            view.transitionName
        )
        val extras = FragmentNavigatorExtras(
            view to view.transitionName,
        )
        findNavController().navigate(action, extras)
         */
    }

    fun navigate(destination: NavDirections, extras: Navigator.Extras) = with(findNavController()) {
        currentDestination?.getAction(destination.actionId)
            ?.let { navigate(destination, extras) }
    }
}
