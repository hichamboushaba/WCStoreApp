package com.hicham.wcstoreapp.data.source

import com.hicham.wcstoreapp.data.source.network.WooCommerceApi
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val api: WooCommerceApi) {
}