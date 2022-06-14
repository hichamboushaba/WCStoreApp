package com.hicham.wcstoreapp.android.di

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class HiltAppCoroutineScope

object AppCoroutineScopeQualifier: Qualifier {
    override val value: QualifierValue
        get() = "AppCoroutineScope"
}