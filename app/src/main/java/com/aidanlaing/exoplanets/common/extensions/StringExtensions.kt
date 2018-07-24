package com.aidanlaing.exoplanets.common.extensions

fun String.defaultIfBlank(replacement: String): String {
    return if (this.isBlank()) replacement
    else this
}