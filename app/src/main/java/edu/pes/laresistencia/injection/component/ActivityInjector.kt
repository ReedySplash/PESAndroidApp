package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.activity.ActivityPresenter
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])

interface ActivityInjector {
    fun inject(activityPresenter: ActivityPresenter)

    @Component.Builder
    interface Builder {
        fun build(): ActivityInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}