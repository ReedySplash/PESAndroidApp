package edu.pes.laresistencia.injection.modules

import android.content.Context
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dagger.Module
import dagger.Provides
import dagger.Reusable
import edu.pes.laresistencia.network.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Singleton


@Module
@Suppress("unused")
object NetworkModule {
    val authenticatorInterceptor = AuthenticatorInterceptor()

    const val baseUrl: String = "http://159.65.117.25:8080"
    // const val baseUrl: String = "http://10.0.2.2:8080"

    @Provides
    fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .addInterceptor(authenticatorInterceptor)
                .cache(cache)
                .build()
    }

    @Provides
    fun cache(cacheFile: File): Cache {
        return Cache(cacheFile, 10 * 1000 * 1000) // 10MB
    }

    @Provides
    fun cacheFile(context: Context): File {
        return File(context.cacheDir, "okhttp_cache")
    }

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.i("HttpLoggingInterceptor", it)
        })
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Date::class.java, MyDateTypeAdapter())
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideUserApi(retrofit: Retrofit): UserAPI {
        return retrofit.create(UserAPI::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOAuth(retrofit: Retrofit): OAuthAPI {
        return retrofit.create(OAuthAPI::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideLocationAPI(retrofit: Retrofit): LocationAPI {
        return retrofit.create(LocationAPI::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideHealthProfileAPI(retrofit: Retrofit): HealthProfileAPI {
        return retrofit.create(HealthProfileAPI::class.java)

    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideFirendshipAPI(retrofit: Retrofit): FriendshipAPI {
        return retrofit.create(FriendshipAPI::class.java)

    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideActivityAPI(retrofit: Retrofit): ActivityAPI {
        return retrofit.create(ActivityAPI::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideChatAPI(retrofit: Retrofit): ChatAPI {
        return retrofit.create(ChatAPI::class.java)
    }

}

class MyDateTypeAdapter : TypeAdapter<Date>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Date?) {
        if (value == null)
            out.nullValue()
        else
            out.value(value!!.getTime() / 1000)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader?): Date? {
        return if (`in` != null)
            Date(`in`!!.nextLong() * 1000)
        else
            null
    }
}