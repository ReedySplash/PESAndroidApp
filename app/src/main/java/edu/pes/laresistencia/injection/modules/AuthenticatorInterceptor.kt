package edu.pes.laresistencia.injection.modules

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorInterceptor @Inject constructor() : Interceptor {
    var authToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val builder = request.newBuilder()
        if (authToken != null) {
            builder.addHeader("Authorization", "bearer $authToken");
        }

        return chain.proceed(builder.build())
    }
}