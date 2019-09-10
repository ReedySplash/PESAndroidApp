package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.profile.ProfilePresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface ProfileInjector {

    fun inject(profilePresenter: ProfilePresenter)

    @Component.Builder
    interface Builder {
        fun build(): ProfileInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}