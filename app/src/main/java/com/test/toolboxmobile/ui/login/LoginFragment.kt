package com.test.toolboxmobile.ui.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.syscredit.core.extensions.validator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.toolboxmobile.R
import com.test.toolboxmobile.core.extensions.DEFAULT_ANIMATION_DURATION_TIME
import com.test.toolboxmobile.core.extensions.fadeOut
import com.test.toolboxmobile.core.extensions.hideKeyboard
import com.test.toolboxmobile.core.extensions.show
import com.test.toolboxmobile.databinding.FragmentLoginBinding
import com.test.toolboxmobile.utils.Status
import com.test.toolboxmobile.utils.jsonObjectOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val binding = FragmentLoginBinding.bind(view)

        with( binding ) {
            loginButton.setOnClickListener {
                when {
                    reservedWordTextInput
                        .validator()
                        .nonEmpty()
                        .addErrorCallback { _, field ->
                            field?.isErrorEnabled = true
                            field?.error = getString(R.string.message_error_field_required)
                            field?.editText?.requestFocus()
                        }
                        .addSuccessCallback { it?.error = null }
                        .validate() -> {
                            hideKeyboard()
                            viewModel.doLogin(
                                jsonObjectOf(
                                    "sub" to reservedWordTextInput.editText?.text.toString()
                                )
                            )
                        }
                }
            }

            with(viewModel) {
                auth.observe(viewLifecycleOwner) {
                    Log.d("--->", "observe $it")
                    when (it.status) {
                        Status.LOADING -> {
                            loginButton.text = ""
                            progressBar.show()
                        }
                        Status.SUCCESS -> progressBar.fadeOut(DEFAULT_ANIMATION_DURATION_TIME, object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                Toast.makeText(requireContext(), "LOGIN SUCCESS", Toast.LENGTH_SHORT).show()
                            }
                        })
                        Status.ERROR -> progressBar.fadeOut(DEFAULT_ANIMATION_DURATION_TIME, object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                loginButton.text = getString(R.string.label_sign_in)
                                MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_AppCompat_Dialog_Alert)
                                    .setTitle(R.string.title_error_login)
                                    .setMessage(it.message)
                                    .setPositiveButton(R.string.action_accept) { dialog, _ -> dialog.dismiss() }
                                    .create().show()
                            }
                        })
                    }
                }
            }
        }
    }
}