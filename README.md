## WCStoreApp (wip)

This is just a fun app to learn about Jetpack Compose, and play with WooCommerce's API, it acts as a
front for the store to offer a mobile shopping experience.

The project is a work-in-progress, but even when it's done, it wouldn't probably be appropriate for
production use, since WooCoomerce's API is not ready for a headless usage:

- WooCommerce by itself doesn't support a cart API
- Rest API keys permissions are too basic, they can't be customized to offer mixed writing and
  reading permissions.

## Demo

![Demo](./screenshots/demo.gif)

## Features

- [x] List all products with pagination
- [x] Price formatting
- [ ] Fetch price formatting settings from the store
- [ ] Offline support
- [ ] Search and filtering
- [x] Check basic details about products
- [ ] Check more details about products: stock, sale, reviews...
- [x] Local cart
- [x] Shipping Addresses: save and list saved addresses.
- [ ] Edit/Delete addresses
- [ ] Address form validation
- [ ] Billing Address
- [ ] Select Payment Method
- [ ] Complete Checkout flow

## Architecture and technologies

The app's goal is to learn about Compose, and experiment on unidirectional reactive architecture
patterns, it uses the following technologies/patterns:

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- MVVM using [Jetpack's ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- Kotlin coroutines with an extensive use of [Flow](https://kotlinlang.org/docs/flow.html) APIs.
- Gradle's [version catalogues](https://docs.gradle.org/current/userguide/platforms.html#sub:conventional-dependencies-toml)
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Navigation Component](https://developer.android.com/guide/navigation)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection
- [Retrofit](https://square.github.io/retrofit/) for API calls
- [Coil](https://coil-kt.github.io/coil/) for image's loading
- [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)
- ...

## Setup Instructions
1. Clone the project
```
$ git clone https://github.com/woocommerce/woocommerce-android.git
$ cd woocommerce-android
```
2. Generate REST API key for authentication using this [guide](https://woocommerce.github.io/woocommerce-rest-api-docs/#rest-api-keys)
3. Generate the local.properties file for this app:
```
$ cp ./local.properties-example ./local.properties
```
4. Open and modify the file `local.properties` using your site's URL and the keys you generated above. 
5. Open the project using Android Studio