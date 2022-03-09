package com.hicham.wcstoreapp.data.currencyformat.fake

import com.hicham.wcstoreapp.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import com.hicham.wcstoreapp.util.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeCurrencyFormatProvider @Inject constructor() : CurrencyFormatProvider {
    override val formatSettings: StateFlow<CurrencyFormatSettings> = MutableStateFlow(
        CurrencyFormatSettings(
            currencyPrefix = "$ ",
            currencySuffix = "",
        )
    )
}