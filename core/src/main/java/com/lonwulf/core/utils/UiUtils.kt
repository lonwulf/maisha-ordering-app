package com.lonwulf.core.utils

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

object UiUtils {
    const val SHIMMER_DURATION = 1800L

    /**
     * Hide the soft keyboard from view
     */
    fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE)
            as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE)
            as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.rotate(from: Float, to: Float) {
    val rotateAnimation = RotateAnimation(
        from,
        to,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    this.startAnimation(rotateAnimation)
}

fun Fragment?.sendSms(shortCodeNumber: String, smsBody: String) {
    this?.let {
        val uri = Uri.parse("smsto:$shortCodeNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
            .apply {
                putExtra("sms_body", smsBody)
            }
        activity?.startActivity(intent)
    }
}

fun DialogFragment?.sendSms(shortCodeNumber: String, smsBody: String) {
    this?.let {
        val uri = Uri.parse("smsto:$shortCodeNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("sms_body", smsBody)
            }
        activity?.startActivity(intent)
    }
}

fun runOnUIDelay(delay: Long, block: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        block()
    }, delay)
}
