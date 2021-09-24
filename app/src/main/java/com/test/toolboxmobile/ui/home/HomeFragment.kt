package com.test.toolboxmobile.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.test.toolboxmobile.R
import com.test.toolboxmobile.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        with(viewModel) {
            getData()
            data.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> Log.d("--->", "Loading")
                    Status.SUCCESS -> Log.d("--->", "Success ${it.data}")
                    Status.ERROR -> Log.d("--->", "error ${it.message}")
                }
            }
        }
    }
}