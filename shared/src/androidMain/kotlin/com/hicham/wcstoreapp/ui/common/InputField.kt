package com.hicham.wcstoreapp.ui.common

import android.os.Parcelable
import android.util.Patterns
import androidx.annotation.StringRes
import com.hicham.wcstoreapp.R
import kotlinx.parcelize.Parcelize

/**
 * Base class for representing an input field, it allows holding the current content, to allow prefilling
 * and restoring it.
 * And allows representing [error] message that can be displayed if the input is not valid, the [error] is not
 * calculated until [validate] is called.
 * [isValid] will return the current validation status independently of whether an error is displayed or not.
 *
 * Child classes will have to implement the validation logic.
 *
 * This class is using a reverse generic type to allow returning the exact type of the class in [validate] function.
 */
actual abstract class InputField<T : InputField<T>> actual constructor(
    actual open val content: String
) : Parcelable, Cloneable {
    @StringRes
    var error: Int? = null
        private set
    private var hasBeenValidated: Boolean = false
    actual val isValid: Boolean
        get() {
            return if (!hasBeenValidated) validateInternal() == null
            else error == null
        }

    actual fun validate(): T {
        val clone = this.clone() as T
        clone.error = validateInternal()
        clone.hasBeenValidated = true
        return clone
    }

    /**
     * Marking the implementation as final to avoid overriding it by Kotlin's data classes, as the generated one
     * doesn't check the parent class's fields, and would skip important details.
     */
    final override fun hashCode(): Int {
        var result = content.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + hasBeenValidated.hashCode()
        return result
    }

    /**
     * Marking the implementation as final to avoid overriding it by Kotlin's data classes, as the generated one
     * doesn't check the parent class's fields, and would skip important details.
     */
    final override fun equals(other: Any?): Boolean {
        if (other !is InputField<*>) return false
        return content == other.content &&
                error == other.error &&
                hasBeenValidated == other.hasBeenValidated
    }

    /**
     * Perform specific field's validation
     * @return [UiString] holding the error to be displayed or null if it's valid
     */
    @StringRes
    protected abstract fun validateInternal(): Int?

    actual abstract fun clone(content: String): InputField<T>
}

@Parcelize
actual data class RequiredField actual constructor(
    override val content: String
) : InputField<RequiredField>(content) {
    override fun validateInternal(): Int? {
        return if (content.isBlank()) R.string.error_required_field
        else null
    }

    override fun clone(content: String): InputField<RequiredField> = copy(content = content)
}

@Parcelize
actual data class OptionalField actual constructor(
    override val content: String
) : InputField<OptionalField>(content) {
    override fun validateInternal(): Int? = null

    override fun clone(content: String): InputField<OptionalField> = copy(content = content)
}

@Parcelize
actual data class PhoneField actual constructor(override val content: String) :
    InputField<PhoneField>(content) {
    override fun validateInternal(): Int? {
        return if (content.isEmpty() || Patterns.PHONE.matcher(content).matches()) null
        else R.string.error_invalid_phone
    }

    override fun clone(content: String): InputField<PhoneField> = copy(content = content)
}