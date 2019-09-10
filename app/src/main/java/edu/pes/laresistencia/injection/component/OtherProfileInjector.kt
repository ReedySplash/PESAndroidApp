package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.otherprofile.OtherProfilePresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface OtherProfileInjector {

    fun inject(otherProfilePresenter: OtherProfilePresenter)

    @Component.Builder
    interface Builder {
        fun build(): OtherProfileInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}