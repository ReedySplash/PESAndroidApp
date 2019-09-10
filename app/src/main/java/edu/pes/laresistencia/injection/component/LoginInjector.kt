package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.login.LoginPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface LoginInjector {

    fun inject(loginPresenter: LoginPresenter)

    @Component.Builder
    interface Builder {
        fun build(): LoginInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}