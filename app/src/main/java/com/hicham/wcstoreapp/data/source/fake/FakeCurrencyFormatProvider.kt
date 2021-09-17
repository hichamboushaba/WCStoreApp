package com.hicham.wcstoreapp.data.source.fake

import com.hicham.wcstoreapp.data.CurrencyFormatProvider
import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import com.hicham.wcstoreapp.models.CurrencyPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FakeCurrencyFormatProvider @Inject constructor() : CurrencyFormatProvider {
    override val formatSettings: StateFlow<CurrencyFormatSettings> = MutableStateFlow(
        CurrencyFormatSettings(
            currencyCode = "USD",
            currencyPosition = CurrencyPosition.LEFT,
        )
    )
}