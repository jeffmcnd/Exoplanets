package com.aidanlaing.exoplanets.data

interface Mappable<out T : Any> {
    fun mapToResult(): Result<T>
}