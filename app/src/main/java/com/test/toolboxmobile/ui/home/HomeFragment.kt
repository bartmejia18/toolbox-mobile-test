package com.test.toolboxmobile.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.syscredit.data.local.sharedPreferences
import com.test.toolboxmobile.R
import com.test.toolboxmobile.core.adapters.getTypedAdapter
import com.test.toolboxmobile.core.adapters.initAdapter
import com.test.toolboxmobile.data.model.Carousel
import com.test.toolboxmobile.data.model.Video
import com.test.toolboxmobile.databinding.FragmentHomeBinding
import com.test.toolboxmobile.databinding.ItemCarouselBinding
import com.test.toolboxmobile.databinding.ItemPosterBinding
import com.test.toolboxmobile.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    lateinit var adapter: MainAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.carouselRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.carouselRecyclerView.initAdapter<Carousel>(R.layout.item_carousel).onBindView { itemView, item, _ ->
            val itemCarouselBinding = ItemCarouselBinding.bind(itemView)
            itemCarouselBinding.titleCarouselTextView.text = item?.title

            val layout = if (item?.type.equals("thumb")) {
                R.layout.item_thumb
            } else {
                R.layout.item_poster
            }

            itemCarouselBinding.videoRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            itemCarouselBinding.videoRecycler.initAdapter<Video>(layout).onBindView { itemViewVideo, itemVideo, _ ->
                val itemPosterBinding = ItemPosterBinding.bind(itemViewVideo)
                itemPosterBinding.textViewTitle.text = itemVideo?.title
                Glide.with(requireContext()).load(itemVideo?.imageUrl.toString()).into(itemPosterBinding.imageView)
            }.onClick { view, item, position ->
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToVideoActivity())
            }.setItems(item?.items?.toMutableList())
        }

        with(viewModel) {
            getData()
            data.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> Log.d("--->", "Loading")
                    Status.SUCCESS -> {
                        binding.carouselRecyclerView.getTypedAdapter<Carousel>().setItems(it.data?.toMutableList())
                    }
                    Status.ERROR -> {
                        requireContext().sharedPreferences {
                            putString("token", "")
                        }
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSplashFragment())
                    }
                }
            }
        }
    }
}