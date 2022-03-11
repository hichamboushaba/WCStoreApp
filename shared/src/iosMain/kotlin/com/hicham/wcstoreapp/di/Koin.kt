package com.hicham.wcstoreapp.di

import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

object KoinWrapper {
    val koinApplication by lazy {
        initKoin(
            module { }
        )
    }

    fun get(objCProtocol: ObjCProtocol): Any {
        val klass = getOriginalKotlinClass(objCProtocol)!!
        return koinApplication.koin.get(klass, null)
    }

    fun get(objCProtocol: ObjCProtocol, qualifier: Qualifier? = null): Any {
        val klass = getOriginalKotlinClass(objCProtocol)!!
        return koinApplication.koin.get(klass, qualifier)
    }

    fun get(objCClass: ObjCClass): Any {
        val klass = getOriginalKotlinClass(objCClass)!!
        return koinApplication.koin.get(klass, null)
    }

    fun get(objCClass: ObjCClass, qualifier: Qualifier? = null): Any {
        val klass = getOriginalKotlinClass(objCClass)!!
        return koinApplication.koin.get(klass, qualifier)
    }
}