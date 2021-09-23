package com.test.toolboxmobile.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


fun Activity.hideKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Fragment.hideKeyboard() {
    (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.doPhoneCall(number: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
    try {
        startActivity(intent)
    } catch (e: Exception) {
        startActivity(Intent.createChooser(intent, null))
    }
}
