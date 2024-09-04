package com.lonwulf.core.di.modules

import android.content.Context
import com.lonwulf.core.BuildConfig
import com.lonwulf.core.network.HeadersInterceptor
import com.lonwulf.core.utils.Prefs
import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpCache(context: Context): Cache {
        val cacheSize: Long = 10 * 10 * 1024
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideAuthenticator(): Authenticator {
        return object : Authenticator {
            override fun authenticate(route: Route?, response: Response): Request {
                return response.request.newBuilder()
                    .header("Authorization", Prefs.getString("access_token") ?: "").build()
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        cache: Cache,
        authenticator: Authenticator
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.cache(cache)
        client.connectTimeout(60, TimeUnit.SECONDS)
        client.readTimeout(60, TimeUnit.SECONDS)
        client.writeTimeout(60, TimeUnit.SECONDS)
        client.retryOnConnectionFailure(true)
        client.authenticator(authenticator)
        client.addInterceptor(HeadersInterceptor(context = context))
        if (BuildConfig.DEBUG) {
            client.addInterceptor(loggingInterceptor)
        }
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("")
            .addConverterFactory(MoshiConverterFactory.create()).client(okHttpClient).build()
    }

}
