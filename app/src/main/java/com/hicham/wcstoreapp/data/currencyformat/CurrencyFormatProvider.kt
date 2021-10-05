package com.hicham.wcstoreapp.data.currencyformat

import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import kotlinx.coroutines.flow.StateFlow

interface CurrencyFormatProvider {
    val formatSettings: StateFlow<CurrencyFormatSettings>
}