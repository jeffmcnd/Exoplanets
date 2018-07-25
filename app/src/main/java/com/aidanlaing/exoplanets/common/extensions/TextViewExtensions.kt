package com.aidanlaing.exoplanets.common.extensions

import android.widget.TextView

fun TextView?.setFormattedTextWithDefaultIfBlank(
        text: String,
        formattedText: String,
        defaultTextIfBlank: String
) {
    this?.let {
        it.text = if (text.isBlank()) {
            defaultTextIfBlank
        } else {
            formattedText
        }
    }
}