package com.hicham.wcstoreapp.data.db

import com.squareup.sqldelight.db.SqlDriver

expect class DriverFactory {
  fun createDriver(): SqlDriver
}