package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.listactivities.ListActivitiesPresenter
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])

interface ListActivitiesInjector {
    fun inject(activityPresenter: ListActivitiesPresenter)

    @Component.Builder
    interface Builder {
        fun build(): ListActivitiesInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}