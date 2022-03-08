package com.hicham.wcstoreapp.android.data.currencyformat.fake

import com.hicham.wcstoreapp.android.data.currencyformat.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FakeCurrencyFormatProvider @Inject constructor() : CurrencyFormatProvider {
    override val formatSettings: StateFlow<CurrencyFormatSettings> = MutableStateFlow(
        CurrencyFormatSettings(
            currencyPrefix = "$ ",
            currencySuffix = "",
        )
    )
}