package com.hicham.wcstoreapp.di

import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier

val koinApplication by lazy {
    initKoin(appModule)
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
    return get(objCClass = objCClass, qualifier = null)
}

fun get(objCClass: ObjCClass, qualifier: Qualifier?): Any {
    return get(objCClass = objCClass, qualifier = qualifier, parameters = null)
}

fun get(objCClass: ObjCClass, parameters: List<Any?>?): Any {
    return get(objCClass = objCClass, qualifier = null, parameters = parameters)
}

fun get(
    objCClass: ObjCClass,
    qualifier: Qualifier? = null,
    parameters: List<Any?>? = null
): Any {
    val klass = getOriginalKotlinClass(objCClass)!!
    return koinApplication.koin.get(klass, qualifier, parameters?.let {
        { ParametersHolder(it.toMutableList()) }
    })
}