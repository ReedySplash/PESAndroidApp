package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.changepassword.ChangePasswordPresenter
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface ChangePasswordInjector {

    fun inject(changePasswordPresenter: ChangePasswordPresenter)

    @Component.Builder
    interface Builder {
        fun build(): ChangePasswordInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}