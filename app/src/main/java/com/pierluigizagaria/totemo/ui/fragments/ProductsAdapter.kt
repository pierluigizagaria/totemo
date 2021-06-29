package com.pierluigizagaria.totemo.ui.fragments

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pierluigizagaria.totemo.R
import com.pierluigizagaria.totemo.models.Product
import com.pierluigizagaria.totemo.repositories.ProductsRepository
import java.io.File

class ProductsAdapter(private val application: Application) :
    ListAdapter<Product, ProductsAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val TAG = ProductsAdapter::class.java.simpleName
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Product> =
            object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                    return oldItem.id === newItem.id
                }

                override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                    return oldItem.name == newItem.name &&
                            oldItem.ingredients.contentEquals(newItem.ingredients) &&
                            oldItem.imageUUID == newItem.imageUUID
                }
            }
    }

    private val productsRepository: ProductsRepository = ProductsRepository(application)
    private var mClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        val image = productsRepository.getImage(product)
        holder.itemView.transitionName = String.format("product_transition_%d", product.id)
        holder.mProductNameText.text = product.name

        image?.let {
            Glide.with(holder.itemView)
                .load(image)
                .into(holder.mProductImage)
        }
    }

    fun getProductAt(position: Int): Product {
        return getItem(position)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, product: Product, image: File?)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val mProductImage: ImageView = view.findViewById(R.id.itemProductImage)
        val mProductNameText: TextView = view.findViewById(R.id.itemProductName)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (mClickListener != null && position != RecyclerView.NO_POSITION) {
                val product = getItem(position)
                mClickListener!!.onItemClick(view, product, productsRepository.getImage(product))
            }
        }
    }
}