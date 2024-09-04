@file:Suppress("TooManyFunctions", "UnusedParameter", "UnusedPrivateProperty", "MaximumLineLength")

package com.lonwulf.core.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.os.Build
import android.text.TextUtils
import com.github.hussainderry.securepreferences.SecurePreferences
import com.github.hussainderry.securepreferences.model.SecurityConfig
import java.util.*

object Prefs {
    private val DEFAULT_SUFFIX = "_preferences"
    private val LENGTH = "#LENGTH"
    private var mPrefs: SharedPreferences? = null

    /**
     * Initialize the Prefs helper class to keep a reference to the SharedPreference for this
     * application the SharedPreference will use the package name of the application as the Key.
     * This method is deprecated please us the new builder.
     *
     * @param context the Application context.
     */
    @Deprecated("")
    fun initPrefs(context: Context?) {
        Builder().setContext(context).build()
    }

    fun initPrefs(
        context: Context,
        prefsName: String,
        mode: Int
    ) {
        // Minimum Configurations
        val minimumConfig = SecurityConfig.Builder(Constant.getPrefEncryptKey())
            .build()

        // Full Configurations
//        val fullConfig = SecurityConfig.Builder(Constant.getPrefEncryptKey())
//            .setKeySize(256) // key size in bits
//            .setPbkdf2SaltSize(32) // salt size in bytes
//            .setPbkdf2Iterations(24000)
//            .setEncryptionAlgorithm(EncryptionAlgorithm.AES)
//            .setDigestType(DigestType.SHA256)
//            .build()

        mPrefs = SecurePreferences.getInstance(context, prefsName, minimumConfig)
        // mPrefs = context.getSharedPreferences(prefsName, mode);
    }

    /**
     * Returns the underlying SharedPreference instance
     *
     * @return an instance of the SharedPreference
     * @throws RuntimeException if SharedPreference instance has not been instantiated yet.
     */
    fun getPreferences(): SharedPreferences {
        if (mPrefs != null) {
            return mPrefs as SharedPreferences
        }
        throw RuntimeException(
            "Prefs class not correctly instantiated. Please call Builder.setContext().build() in the Application class onCreate."
        )
    }

    /**
     * @return Returns a map containing a list of pairs key/value representing
     * the preferences.
     * @see SharedPreferences.getAll
     */
    fun getAll(): Map<String?, *>? {
        return getPreferences().all
    }

    /**
     * Retrieves a stored int value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not
     * an int.
     * @see SharedPreferences.getInt
     */
    fun getInt(key: String?, defValue: Int): Int {
        return getPreferences().getInt(key, defValue)
    }

    /**
     * Retrieves a stored int value, or 0 if the preference does not exist.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * @throws ClassCastException if there is a preference with this name that is not
     * an int.
     * @see SharedPreferences.getInt
     */
    fun getInt(key: String?): Int {
        return getPreferences().getInt(key, 0)
    }

    /**
     * Retrieves a stored boolean value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a boolean.
     * @see SharedPreferences.getBoolean
     */
    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return getPreferences().getBoolean(key, defValue)
    }

    /**
     * Retrieves a stored boolean value, or false if the preference does not exist.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or false.
     * @throws ClassCastException if there is a preference with this name that is not a boolean.
     * @see SharedPreferences.getBoolean
     */
    fun getBoolean(key: String?): Boolean {
        return getPreferences().getBoolean(key, false)
    }

    /**
     * Retrieves a stored long value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see SharedPreferences.getLong
     */
    fun getLong(key: String?, defValue: Long): Long {
        return getPreferences().getLong(key, defValue)
    }

    /**
     * Retrieves a stored long value, or 0 if the preference does not exist.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see SharedPreferences.getLong
     */
    fun getLong(key: String?): Long {
        return getPreferences().getLong(key, 0L)
    }

    /**
     * Returns the double that has been saved as a long raw bits value in the long preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue the double Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see SharedPreferences.getLong
     */
    fun getDouble(key: String?, defValue: Double): Double {
        return java.lang.Double.longBitsToDouble(
            getPreferences().getLong(
                key,
                java.lang.Double.doubleToLongBits(defValue)
            )
        )
    }

    /**
     * Returns the double that has been saved as a long raw bits value in the long preferences.
     * Returns 0 if the preference does not exist.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * @throws ClassCastException if there is a preference with this name that is not a long.
     * @see SharedPreferences.getLong
     */
    fun getDouble(key: String?): Double {
        return java.lang.Double.longBitsToDouble(
            getPreferences().getLong(
                key,
                java.lang.Double.doubleToLongBits(0.0)
            )
        )
    }

    /**
     * Retrieves a stored float value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a float.
     * @see SharedPreferences.getFloat
     */
    fun getFloat(key: String?, defValue: Float): Float {
        return getPreferences().getFloat(key, defValue)
    }

    /**
     * Retrieves a stored float value, or 0 if the preference does not exist.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * @throws ClassCastException if there is a preference with this name that is not a float.
     * @see SharedPreferences.getFloat
     */
    fun getFloat(key: String?): Float {
        return getPreferences().getFloat(key, 0.0f)
    }

    /**
     * Retrieves a stored String value.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     * @throws ClassCastException if there is a preference with this name that is not a String.
     * @see SharedPreferences.getString
     */
    fun getString(key: String?, defValue: String?): String? {
        return getPreferences().getString(key, defValue)
    }

    /**
     * Retrieves a stored String value, or an empty string if the preference does not exist.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or "".
     * @throws ClassCastException if there is a preference with this name that is not a String.
     * @see SharedPreferences.getString
     */
    fun getString(key: String?): String? {
        return getPreferences().getString(key, "")
    }

    /**
     * Retrieves a Set of Strings as stored by [.putStringSet]. On Honeycomb and
     * later this will call the native implementation in SharedPreferences, on older SDKs this will
     * call [.getOrderedStringSet].
     * **Note that the native implementation of [SharedPreferences.getStringSet] does not reliably preserve the order of the Strings in the Set.**
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues otherwise.
     * @throws ClassCastException if there is a preference with this name that is not a Set.
     * @see SharedPreferences.getStringSet
     * @see .getOrderedStringSet
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(
        key: String,
        defValue: Set<String?>?
    ): Set<String?>? {
        val prefs = getPreferences()
        return try {
            prefs.getStringSet(key, defValue)
        } catch (e: ClassCastException) {
            // Workaround for Samsung I7500 1.6 devices
            if (e.message?.contains("java.lang.String") == true) {
                prefs.getStringSet(key, null)
            } else {
                null
            }
        }
    }

    /**
     * Retrieves a Set of Strings as stored by [.putOrderedStringSet],
     * preserving the original order. Note that this implementation is heavier than the native
     * [.getStringSet] method (which does not guarantee to preserve order).
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValues otherwise.
     * @throws ClassCastException if there is a preference with this name that is not a Set of
     * Strings.
     * @see .getStringSet
     */
    fun getOrderedStringSet(
        key: String,
        defValue: Set<String?>?
    ): Set<String?>? {
        val prefs = getPreferences()
        if (prefs.contains(key + LENGTH)) {
            val set =
                LinkedHashSet<String?>()
            val stringSetLength = prefs.getInt(key + LENGTH, -1)
            if (stringSetLength >= 0) {
                for (i in 0 until stringSetLength) {
                    set.add(prefs.getString("$key[$i]", null))
                }
            }
            return set
        }
        return defValue
    }

    /**
     * Stores a long value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor.putLong
     */
    fun putLong(key: String?, value: Long) {
        val editor = getPreferences().edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * Stores an integer value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor.putInt
     */
    fun putInt(key: String?, value: Int) {
        val editor = getPreferences().edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * Stores a double value as a long raw bits value.
     *
     * @param key   The name of the preference to modify.
     * @param value The double value to be save in the preferences.
     * @see Editor.putLong
     */
    fun putDouble(key: String?, value: Double) {
        val editor = getPreferences().edit()
        editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        editor.apply()
    }

    /**
     * Stores a float value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor.putFloat
     */
    fun putFloat(key: String?, value: Float) {
        val editor = getPreferences().edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    /**
     * Stores a boolean value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor.putBoolean
     */
    fun putBoolean(key: String?, value: Boolean) {
        val editor = getPreferences().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * Stores a String value.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor.putString
     */
    fun putString(key: String?, value: String?) {
        var value = value
        val editor = getPreferences().edit()
        if (value == null) {
            value = ""
        }
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * Stores a Set of Strings. On Honeycomb and later this will call the native implementation in
     * SharedPreferences.Editor, on older SDKs this will call [.putOrderedStringSet].
     * **Note that the native implementation of [Editor.putStringSet] does not reliably preserve the order of the Strings in the Set.**
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor.putStringSet
     * @see .putOrderedStringSet
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun putStringSet(
        key: String,
        value: Set<String?>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val editor = getPreferences().edit()
            editor.putStringSet(key, value)
            editor.apply()
        } else {
            // Workaround for pre-HC's lack of StringSets
            putOrderedStringSet(key, value)
        }
    }

    /**
     * Stores a Set of Strings, preserving the order.
     * Note that this method is heavier that the native implementation [.putStringSet] (which does not reliably preserve the order of the Set). To preserve the order of the
     * items in the Set, the Set implementation must be one that as an iterator with predictable
     * order, such as [LinkedHashSet].
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @see .putStringSet
     * @see .getOrderedStringSet
     */
    fun putOrderedStringSet(
        key: String,
        value: Set<String?>
    ) {
        val editor = getPreferences().edit()
        var stringSetLength = 0
        if (mPrefs?.contains(key + LENGTH) == true) {
            // First read what the value was
            mPrefs?.let {
                stringSetLength = it.getInt(key + LENGTH, -1)
            }
        }
        editor.putInt(key + LENGTH, value.size)
        var i = 0
        for (aValue in value) {
            editor.putString("$key[$i]", aValue)
            i++
        }
        while (i < stringSetLength) {
            // Remove any remaining values
            editor.remove("$key[$i]")
            i++
        }
        editor.apply()
    }

    /**
     * Removes a preference value.
     *
     * @param key The name of the preference to remove.
     * @see Editor.remove
     */
    fun remove(key: String) {
        val prefs = getPreferences()
        val editor = prefs.edit()
        if (prefs.contains(key + LENGTH)) {
            // Workaround for pre-HC's lack of StringSets
            val stringSetLength = prefs.getInt(key + LENGTH, -1)
            if (stringSetLength >= 0) {
                editor.remove(key + LENGTH)
                for (i in 0 until stringSetLength) {
                    editor.remove("$key[$i]")
                }
            }
        }
        editor.remove(key)
        editor.apply()
    }

    /**
     * Checks if a value is stored for the given key.
     *
     * @param key The name of the preference to check.
     * @return `true` if the storage contains this key value, `false` otherwise.
     * @see SharedPreferences.contains
     */
    operator fun contains(key: String?): Boolean {
        return getPreferences().contains(key)
    }

    /**
     * Removed all the stored keys and values.
     *
     * @return the [Editor] for chaining. The changes have already been committed/applied
     * through the execution of this method.
     * @see Editor.clear
     */
    fun clear(): SharedPreferences.Editor? {
        val editor = getPreferences().edit().clear()
        editor.apply()
        return editor
    }

    /**
     * Returns the Editor of the underlying SharedPreferences instance.
     *
     * @return An Editor
     */
    fun edit(): SharedPreferences.Editor? {
        return getPreferences().edit()
    }

    /**
     * Builder class for the EasyPrefs instance. You only have to call this once in the Application
     * onCreate. And in the rest of the code base you can call Prefs.method name.
     */
    class Builder {
        private var mKey: String? = null
        private var mContext: Context? = null
        private var mMode = -1
        private var mUseDefault = false

        /**
         * Set the filename of the SharedPreference instance. Usually this is the application's
         * packagename.xml but it can be modified for migration purposes or customization.
         *
         * @param prefsName the filename used for the SharedPreference
         * @return the [com.pixplicity.easyprefs.library.Prefs.Builder] object.
         */
        fun setPrefsName(prefsName: String?): Builder {
            mKey = prefsName
            return this
        }

        /**
         * Set the Context used to instantiate the SharedPreferences
         *
         * @param context the application context
         * @return the [com.pixplicity.easyprefs.library.Prefs.Builder] object.
         */
        fun setContext(context: Context?): Builder {
            mContext = context
            return this
        }

        /**
         * Set the mode of the SharedPreference instance.
         *
         * @param mode Operating mode.  Use 0 or [Context.MODE_PRIVATE] for the
         * default operation, [Context.MODE_WORLD_READABLE]
         * @return the [com.pixplicity.easyprefs.library.Prefs.Builder] object.
         * @see Context.getSharedPreferences
         */
        @SuppressLint("WorldReadableFiles", "WorldWriteableFiles")
        fun setMode(mode: Int): Builder {
            mMode =
                if (mode == ContextWrapper.MODE_PRIVATE || mode == ContextWrapper.MODE_WORLD_READABLE || mode == ContextWrapper.MODE_WORLD_WRITEABLE || mode == ContextWrapper.MODE_MULTI_PROCESS) {
                    mode
                } else {
                    throw RuntimeException("The mode in the SharedPreference can only be set too ContextWrapper.MODE_PRIVATE, ContextWrapper.MODE_WORLD_READABLE, ContextWrapper.MODE_WORLD_WRITEABLE or ContextWrapper.MODE_MULTI_PROCESS")
                }
            return this
        }

        /**
         * Set the default SharedPreference file name. Often the package name of the application is
         * used, but if the [android.preference.PreferenceActivity] or [ ] is used the system will append that with
         * _preference.
         *
         * @param defaultSharedPreference true if default SharedPreference name should used.
         * @return the [com.pixplicity.easyprefs.library.Prefs.Builder] object.
         */
        fun setUseDefaultSharedPreference(defaultSharedPreference: Boolean): Builder {
            mUseDefault = defaultSharedPreference
            return this
        }

        /**
         * Initialize the SharedPreference instance to used in the application.
         *
         * @throws RuntimeException if Context has not been set.
         */
        fun build() {
            if (mContext == null) {
                throw RuntimeException("Context not set, please set context before building the Prefs instance.")
            }
            if (TextUtils.isEmpty(mKey)) {
                mKey = mContext?.packageName
            }
            if (mUseDefault) {
                mKey += "_preferences"
            }
            if (mMode == -1) {
                mMode = ContextWrapper.MODE_PRIVATE
            }
            mContext?.let { context ->
                mKey?.let { prefsName ->
                    initPrefs(context, prefsName, mMode)
                }
            }
        }
    }
}
