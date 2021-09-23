package com.example.syscredit.core.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.view.doOnDetach
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*


fun <T> EditText.changeListener(listener: (field: EditText?, s: String) -> T?) {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener.invoke(this@changeListener, s.toString())
        }
    }

    addTextChangedListener(textWatcher)

    this.doOnDetach {
        this.removeTextChangedListener(textWatcher)
    }
}

/**
 * Add easy validations on edit text
 */

fun EditText.validator(): Validator<EditText> = Validator(text.toString(), this)

fun TextInputEditText.validator(): Validator<TextInputEditText> = Validator(text.toString(), this)

fun TextInputLayout.validator(): Validator<TextInputLayout> = Validator(editText?.text.toString(),
    this)

/*
* Class to process all the filters provided by the user
* */
class Validator<T>(val text: String, val view: T?) {

    /*
    * Boolean to determine whether all the validations has passed successfully.
    * If any validation fails the state is changed to false.
    * Final result is returned to the user
    * */
    private var isValidated = true

    /*
    * If validation fails then this callback is invoked to notify the user about
    * and error
    * */
    private var errorCallback: ((error: ValidationError?, field: T?) -> Unit)? = null

    /*
    * If validation is passes then this callback is invoked to notify the user
    * for the same
    * */
    private var successCallback: ((field: T?) -> Unit)? = null

    /*
    * User settable limits for the numbers of characters that the string can contain
    * */
    private var MINIMUM_LENGTH = 0
    private var MAXIMUM_LENGTH = Int.MAX_VALUE

    private var VALIDATION_ERROR_TYPE: ValidationError? = null

    fun validate(): Boolean {
        //Check if the string characters count is in limits
        if (text.length < MINIMUM_LENGTH) {
            isValidated = false
            setErrorType(ValidationError.MINIMUM_LENGTH)
        } else if (text.length > MAXIMUM_LENGTH) {
            isValidated = false
            setErrorType(ValidationError.MAXIMUM_LENGTH)
        }

        //Invoke the error callback if supplied by the user
        if (isValidated) {
            successCallback?.invoke(view)
        } else {
            errorCallback?.invoke(VALIDATION_ERROR_TYPE, view)
        }

        return isValidated
    }

    fun nonEmpty(): Validator<T> {
        if (text.isEmpty()) {
            setErrorType(ValidationError.NON_EMPTY)
        }
        return this
    }

    fun onlyNumbers(): Validator<T> {
        if (!text.matches(Regex("\\d+"))) {
            setErrorType(ValidationError.ONLY_NUMBERS)
        }
        return this
    }

    fun numberDecimals(): Validator<T> {
        if (!text.matches(Regex("^[0-9]+([.][0-9]+)?\$"))) {
            setErrorType(ValidationError.ONLY_NUMBERS)
        }
        return this
    }

    fun addErrorCallback(callback: (error: ValidationError?, field: T?) -> Unit): Validator<T> {
        errorCallback = callback
        return this
    }

    fun addSuccessCallback(callback: (field: T?) -> Unit): Validator<T> {
        successCallback = callback
        return this
    }

    fun contains(string: String): Validator<T> {
        if (!text.contains(string)) {
            setErrorType(ValidationError.CONTAINS)
        }
        return this
    }

    @Suppress("CovariantEquals")
    fun equals(string: String): Validator<T> {
        if (text != string) {
            setErrorType(ValidationError.EQUALS)
        }
        return this
    }

    private fun setErrorType(validationError: ValidationError) {
        isValidated = false
        if (VALIDATION_ERROR_TYPE == null) {
            VALIDATION_ERROR_TYPE = validationError
        }
    }
}

/*
* Enums that serve for identification of error while validation text.
* Every enum is the name of a function with the corresponding validation
* */
enum class ValidationError {
    MINIMUM_LENGTH,
    MAXIMUM_LENGTH,
    AT_LEAST_ONE_UPPER_CASE,
    AT_LEAST_ONE_LOWER_CASE,
    ALL_LOWER_CASE,
    ALL_UPPER_CASE,
    ONLY_NUMBERS,
    BETWEEN,
    NON_EMPTY,
    NO_NUMBERS,
    EMAIL,
    CONTAINS,
    EQUALS,
}
