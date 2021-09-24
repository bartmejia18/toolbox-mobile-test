package com.test.toolboxmobile.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.toolboxmobile.data.model.Carousel
import com.test.toolboxmobile.databinding.ItemCarouselBinding

class MainAdapter(
    private val customerList: List<Carousel>
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var customerFilterList = mutableListOf<Carousel>()

    init {
        customerFilterList = customerList as MutableList<Carousel>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        with (holder) {
            with (customerFilterList[position]) {
                binding.titleCarouselTextView.text = title
            }
        }
    }

    override fun getItemCount(): Int {
        return customerFilterList.size
    }

    inner class MainViewHolder (val binding: ItemCarouselBinding) : RecyclerView.ViewHolder(binding.root)

    interface ItemClickListener {
        fun click(credito: Carousel)
    }
}