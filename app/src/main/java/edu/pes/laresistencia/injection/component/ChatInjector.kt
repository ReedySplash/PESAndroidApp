package edu.pes.laresistencia.injection.component

import dagger.Component
import edu.pes.laresistencia.chat.ChatPresenter
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface ChatInjector {
    fun inject(chatsPresenter: ChatPresenter)

    @Component.Builder
    interface Builder {
        fun build(): ChatInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}