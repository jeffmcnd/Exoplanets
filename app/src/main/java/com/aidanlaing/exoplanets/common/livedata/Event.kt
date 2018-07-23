package com.aidanlaing.exoplanets.common.livedata

class Event<out T>(val content: T) {

    private var hasBeenHandled = false

    fun runIfNotHandled(body: () -> Unit) {
        if (!hasBeenHandled) {
            body()
            hasBeenHandled = true
        }
    }
}
