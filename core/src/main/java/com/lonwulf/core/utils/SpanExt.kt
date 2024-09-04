package com.lonwulf.core.utils

import android.text.Spannable
import android.text.SpannableString
import timber.log.Timber

fun String.addSpan(message: String, span: Any): SpannableString {
    val result = SpannableString(this)
    result.addSpan(message, span)
    return result
}

fun SpannableString.addSpan(message: String, span: Any): SpannableString {
    val startIndex = indexOf(message)
    if (startIndex > -1) {
        setSpan(span, startIndex, startIndex + message.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    } else {
        Timber.tag("SpanExt").e("   Warning !!!")
        Timber.tag("SpanExt").e("Missing $message in ${toString()} - Cannot set SPAN")
    }
    return this
}
