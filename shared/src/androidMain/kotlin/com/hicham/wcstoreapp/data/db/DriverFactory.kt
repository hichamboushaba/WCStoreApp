package com.hicham.wcstoreapp.data.db

import android.content.Context
import com.hicham.wcstoreapp.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DriverFactory(private val context: Context) {
  actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(Database.Schema, context, "database.db")
  }
}