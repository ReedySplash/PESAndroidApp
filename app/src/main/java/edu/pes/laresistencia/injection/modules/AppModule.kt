package edu.pes.laresistencia.injection.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@SuppressWarnings("unused")
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesApplication(context: Context): Application {
        return context.applicationContext as Application
    }

    @Provides
    @Singleton
    fun context(): Context {
        return context
    }
}