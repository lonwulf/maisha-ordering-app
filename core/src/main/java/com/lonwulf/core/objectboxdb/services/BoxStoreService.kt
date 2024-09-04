@file:Suppress(
    "TooGenericExceptionCaught",
    "SwallowedException",
    "MagicNumber",
    "NestedBlockDepth",
    "ReturnCount"
)

package com.lonwulf.core.objectboxdb.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.lonwulf.core.BuildConfig
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.BoxStoreBuilder
import io.objectbox.android.Admin
import java.util.*

interface BoxStoreService {
    fun <T> getBoxStoreBoxModel(type: Class<T>): Box<T>
    fun getBoxStore(): BoxStore
    fun clearAllData()
}

class BoxStoreServiceImpl(
    private val context: Context,
    private val builderFactory: () -> BoxStoreBuilder,
) : BoxStoreService {
    private var boxStore: BoxStore
    private var boxStoreId: String

    init {
        boxStoreId = Prefs.getString(Constant.PrefKeys.BOX_STORE_ID) ?: createNewBoxStoreId()
        boxStore = openBoxStore()
    }

    private fun createNewBoxStoreId(): String {
        val newBoxStoreId = UUID.randomUUID().toString()
        Prefs.putString(Constant.PrefKeys.BOX_STORE_ID, newBoxStoreId)
        return newBoxStoreId
    }

    val TAG = BoxStoreService::class.java.simpleName
    override fun <T> getBoxStoreBoxModel(type: Class<T>): Box<T> {
        return getBoxStore().boxFor(type)
    }

    override fun getBoxStore() = if (boxStore.isClosed) openBoxStore() else boxStore

    @Synchronized
    fun openBoxStore(isRetry: Boolean = false): BoxStore {
        try {
            val newBoxStore =
                builderFactory().androidContext(context).name(boxStoreId).maxReaders(500).build()
            if (BuildConfig.DEBUG) {
                Admin(newBoxStore).apply {
                    notificationId = 1
                }.start(context)
            }
            return newBoxStore
        } catch (e: Exception) {
            Log.e(TAG, "opening boxStore ($isRetry)", e)
            if (isRetry) {
                throw e
            } else {
                try {
                    val newBoxStore = builderFactory().usePreviousCommit().androidContext(context)
                        .name(boxStoreId).maxReaders(500).build()
                    Log.e(TAG, "Switched to the previous box store.")
                    if (BuildConfig.DEBUG) {
                        Admin(newBoxStore).start(context)
                    }
                    return newBoxStore
                } catch (e: Exception) {
                    Log.e(TAG, "opening previous commit of boxStore ($isRetry)", e)
                    if (BuildConfig.DEBUG) {
                        Log.e(
                            TAG,
                            "default.json is corrupted. Move back to the last release version and rebuild it again"
                        )
                        Toast.makeText(context, "DB Schema corrupted", Toast.LENGTH_SHORT).show()
                    }
                    boxStoreId =
                        createNewBoxStoreId() // Likely a migration issue so switch to a new box store
                    return openBoxStore(true)
                }
            }
        }
    }

    @Synchronized
    override fun clearAllData() {
        boxStore.close()
        boxStore.deleteAllFiles()
        boxStore = openBoxStore()
    }
}
