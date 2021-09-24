package com.test.toolboxmobile.ui.splash

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.toolboxmobile.data.local.getFromSharedPreferences
import com.test.toolboxmobile.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        if (!activity?.getFromSharedPreferences("token", "").isNullOrEmpty()) {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }
}