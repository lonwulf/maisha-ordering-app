package com.lonwulf.core.objectboxdb

import android.util.Log
import com.lonwulf.core.objectboxdb.services.BoxStoreService

interface BoxStoreObject {
    var objectBoxID: Long
}

inline infix fun <reified T : BoxStoreObject> BoxStoreService.store(t: T): Long {
    catchException(issueWarning = false) { getBoxStore().boxFor(t.javaClass).attach(t) }
    return getBoxStore().boxFor(T::class.java).put(t)
}

inline infix fun <reified T : BoxStoreObject> BoxStoreService.store(t: List<T>) {
    catchException(issueWarning = false) { getBoxStore().boxFor(t.javaClass).attach(t) }
    getBoxStore().boxFor(T::class.java).put(t)
}

fun <T> catchException(issueWarning: Boolean = true, throwingLambda: () -> T): T? {
    return try {
        throwingLambda()
    } catch (e: Exception) {
        if (issueWarning) Log.e("Caught exception.", e.message ?: "An error has occurred")
        null
    }
}
