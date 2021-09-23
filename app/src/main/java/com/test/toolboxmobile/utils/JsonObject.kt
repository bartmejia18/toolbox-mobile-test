package com.test.toolboxmobile.utils

import android.os.Bundle
import com.google.gson.JsonElement
import com.google.gson.JsonObject


/**
 * Returns a new [Bundle] with the given key/value pairs as elements.
 *
 * @throws IllegalArgumentException When a value is not a supported type of [Bundle].
 */
fun jsonObjectOf(vararg pairs: Pair<String, Any?>) = JsonObject().apply {
    for ((key, value) in pairs) {
        when (value) {
            // Scalars
            is Boolean -> addProperty(key, value)
            is Byte -> addProperty(key, value)
            is Char -> addProperty(key, value)
            is Double -> addProperty(key, value)
            is Float -> addProperty(key, value)
            is Int -> addProperty(key, value)
            is Long -> addProperty(key, value)
            is Short -> addProperty(key, value)

            // String
            is String -> addProperty(key, value)

            // JsonElement
            is JsonElement -> add(key, value)
        }
    }
}
