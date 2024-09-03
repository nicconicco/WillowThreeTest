package com.nicco.willowthreedemo

import com.nicco.willowthreedemo.framework.di.appModule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class AppDIModuleTest: KoinTest {

    @Test
    fun checkAllModules() {
        appModule.verify()
    }
}