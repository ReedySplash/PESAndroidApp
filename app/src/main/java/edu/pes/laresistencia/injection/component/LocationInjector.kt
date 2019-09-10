package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.location.LocationPresenter
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])

interface LocationInjector {
    fun inject(locationPresenter: LocationPresenter)

    @Component.Builder
    interface Builder {
        fun build(): LocationInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}