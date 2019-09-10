package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.config.ConfigPresenter
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface ConfigInjector {

    fun inject(configPresenter: ConfigPresenter)

    @Component.Builder
    interface Builder {
        fun build(): ConfigInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}