package com.hicham.wcstoreapp.data.db

import com.hicham.wcstoreapp.util.DB
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


/**
 * I'm not sure this is safe, check: https://github.com/cashapp/sqldelight/issues/1420
 * TODO: investigate this more
 */
suspend fun Transacter.suspendTransaction(block: suspend () -> Unit) =
    block()

suspend fun <R> Transacter.suspendTransactionWithResult(block: suspend () -> R) =
    block()
