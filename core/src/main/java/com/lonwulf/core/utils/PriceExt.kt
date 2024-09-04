package com.lonwulf.core.utils

import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

@Suppress("MagicNumber")
fun Double.mapSavings(): CharSequence {
    val save = "SAVE"
    val currency = Prefs.getString(Constant.MultiCompanyKeys.COUNTRY_CURRENCY_NAME).orEmpty()
    val savings = "$save\n$currency ${this.toInt()}"
    return savings.addSpan(save, StyleSpan(Typeface.BOLD))
        .addSpan(save, RelativeSizeSpan(0.7f))
        .addSpan("${this.toInt()}", StyleSpan(Typeface.BOLD))
        .addSpan(currency, RelativeSizeSpan(0.7f))
}
