package com.lonwulf.core.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.lang.ref.WeakReference

class ReceiverManager(context: Context) {
    private val cReference: WeakReference<Context> = WeakReference(context)
    fun registerReceiver(receiver: BroadcastReceiver, intentFilter: IntentFilter): Intent? {
        if (receivers == null) receivers = receiver
        return cReference.get()?.registerReceiver(receivers, intentFilter)
    }

    private fun isReceiverRegistered(receiver: BroadcastReceiver): Boolean? {
        return receivers == receiver
    }

    fun unregisterReceiver(receiver: BroadcastReceiver) {
        isReceiverRegistered(receiver)?.let {
            if (it) {
                receivers?.abortBroadcast
                cReference.get()?.unregisterReceiver(receivers)
            }
        }
    }

    companion object {
        private var receivers: BroadcastReceiver? = null
        private var receiverManager: ReceiverManager? = null

        @Synchronized
        fun init(context: Context): ReceiverManager? {
            if (receiverManager == null) receiverManager = ReceiverManager(context)
            return receiverManager
        }
    }
}
