package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.register.RegisterPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface RegisterInjector {

    fun inject(registerPresenter: RegisterPresenter)

    @Component.Builder
    interface Builder {
        fun build(): RegisterInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}