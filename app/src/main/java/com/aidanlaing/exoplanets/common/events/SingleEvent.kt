package com.aidanlaing.exoplanets.common.events

class SingleEvent {

    private var handled: Boolean = false

    fun invokeIfNotHandled(body: () -> Unit) {
        if (!handled) {
            body()
            handled = true
        }
    }
}