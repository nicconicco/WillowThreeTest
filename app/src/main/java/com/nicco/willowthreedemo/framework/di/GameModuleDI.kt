package com.nicco.willowthreedemo.framework.di

import com.nicco.willowthreedemo.data.repository.UserRepositoryImpl
import com.nicco.willowthreedemo.domain.UserRepository
import com.nicco.willowthreedemo.domain.WillowUseCase
import com.nicco.willowthreedemo.domain.WillowUseCaseImpl
import com.nicco.willowthreedemo.domain.rules.WillowGameRules
import com.nicco.willowthreedemo.domain.rules.WillowGameRulesImpl
import com.nicco.willowthreedemo.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single { provideHttpClient() }
    single { provideConverterFactory() }
    single { provideRetrofit(get(), get()) }
    single { provideService(get()) }
    single { provideDispatcher() }

    singleOf(::WillowGameRulesImpl) { bind<WillowGameRules>() }
    factoryOf(::WillowUseCaseImpl) { bind<WillowUseCase>() }

    factoryOf(::UserRepositoryImpl) { bind<UserRepository>() }
    viewModelOf(::GameViewModel)
}

fun provideDispatcher(): CoroutineDispatcher {
    return Dispatchers.IO
}
