package com.lonwulf.core.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.lonwulf.core.utils.Constant
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferenceModule {

    @Provides
    @Singleton
    fun providesPreference(context: Context): SharedPreferences = context.getSharedPreferences(
        Constant.PREF_NAME,
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun providesSharedPreference(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor = sharedPreferences.edit()
}
