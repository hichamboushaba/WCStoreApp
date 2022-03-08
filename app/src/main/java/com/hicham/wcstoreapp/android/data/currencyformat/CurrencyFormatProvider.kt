package com.hicham.wcstoreapp.android.data.currencyformat

import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import kotlinx.coroutines.flow.Flow

interface CurrencyFormatProvider {
    val formatSettings: Flow<CurrencyFormatSettings>
}