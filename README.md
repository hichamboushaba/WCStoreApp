## WCStoreApp (wip)

This is just a fun app to learn about KMM and Jetpack Compose, and play with WooCommerce's StoreApi, it acts as a
front for the store to offer a mobile shopping experience.

The project is a work-in-progress.

## Demo

![Demo](./screenshots/demo.gif)

## Features

- [x] KMM (Ongoing, the iOS app is missing a lot of features, but the presentation layer is shared)
- [x] List all products with pagination
- [x] Price formatting
- [x] Fetch price formatting settings from the store
- [x] Offline support
- [x] Search and filtering
- [x] Check basic details about products
- [ ] Check more details about products: stock, sale, reviews...
- [x] Cart
- [x] Shipping Addresses: save and list saved addresses.
- [ ] Edit/Delete addresses
- [x] Address form validation
- [ ] Billing Address
- [x] Select Payment Method
- [x] Complete Checkout flow

Most of the features (even the ones marked as done) still require improvements and refinements.

## Architecture and technologies

The app's goal is to learn about Compose, and experiment on unidirectional reactive architecture
patterns, it uses the following technologies/patterns:

- [KMM](https://kotlinlang.org/lp/mobile/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- MVVM using [Jetpack's ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) for the Android part.
- Kotlin coroutines with an extensive use of [Flow](https://kotlinlang.org/docs/flow.html) APIs.
- Gradle's [version catalogues](https://docs.gradle.org/current/userguide/platforms.html#sub:conventional-dependencies-toml)
- [Navigation Component](https://developer.android.com/guide/navigation)
- [Koin](https://insert-koin.io/) for dependency injection
- [Ktor](https://ktor.io/) for API calls
- [Coil](https://coil-kt.github.io/coil/) for image's loading
- [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)
- [SQLDelight](https://cashapp.github.io/sqldelight/)
- ...

## Setup Instructions
1. Clone the project
```
$ git clone https://github.com/hichamboushaba/WCStoreApp.git
$ cd WCStoreApp
```
2. Generate the local.properties file for this app:
```
$ cp ./local.properties-example ./local.properties
```
3. Open and modify the file `local.properties` using your site's URL. 
4. Open the project using Android Studio
