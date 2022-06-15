package com.hicham.wcstoreapp.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

private val _DB = newSingleThreadContext("IO")
val Dispatchers.DB
    get() = _DB
