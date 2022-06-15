package com.hicham.wcstoreapp.ui.common

expect abstract class InputField<T: InputField<T>>(content: String) {
    open val content: String
    val isValid: Boolean
    fun validate(): T
    abstract fun clone(content: String): InputField<T>
}

expect class RequiredField(content: String): InputField<RequiredField>
expect class OptionalField(content: String): InputField<OptionalField>
expect class PhoneField(content: String): InputField<PhoneField>