package com.hicham.wcstoreapp.ui.common

/**
 * Empty implementations
 */

actual abstract class InputField<T : InputField<T>> actual constructor(actual open val content: String) {
    actual val isValid: Boolean = true
    actual fun validate(): T = this as T
    actual abstract fun clone(content: String): InputField<T>
}

actual class RequiredField actual constructor(content: String) :
    InputField<RequiredField>(content) {
    override fun clone(content: String): InputField<RequiredField> {
        return RequiredField(content)
    }
}

actual class OptionalField actual constructor(content: String) :
    InputField<OptionalField>(content) {
    override fun clone(content: String): InputField<OptionalField> {
        return OptionalField(content)
    }
}

actual class PhoneField actual constructor(content: String) : InputField<PhoneField>(content) {
    override fun clone(content: String): InputField<PhoneField> {
        return PhoneField(content)
    }
}
