package com.hicham.wcstoreapp.di

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

object AppCoroutineScopeQualifier: Qualifier {
    override val value: QualifierValue
        get() = "AppCoroutineScope"
}