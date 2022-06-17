package com.hicham.wcstoreapp.android.ui.checkout

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.android.ui.theme.WCStoreAppTheme
import com.hicham.wcstoreapp.models.PaymentCard
import com.hicham.wcstoreapp.models.PaymentMethod
import kotlin.math.exp

@Composable
fun CreditCardForm(
    onAddPaymentMethod: (PaymentMethod) -> Unit,
    onCancel: () -> Unit
) {
    var cardNumber by rememberSaveable {
        mutableStateOf("")
    }
    var cvc by rememberSaveable {
        mutableStateOf("")
    }
    var expiry by remember {
        mutableStateOf("")
    }

    val isValid by derivedStateOf {
        // TODO this is just for demonstration purposes
        cardNumber.length == 16 && cvc.length == 3 && expiry.length == 4
    }

    Column(modifier = Modifier.padding(16.dp)) {
        CreditCardField(cardNumber) { cardNumber = it }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            OutlinedTextField(
                value = cvc,
                onValueChange = { value ->
                    cvc = value.filter { it.isDigit() && it != '.' }.take(3)
                },
                label = {
                    Text(text = "CVC")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            ExpiryTextField(expiry = expiry, onExpiryChanged = { expiry = it })
        }
        Row(modifier = Modifier.align(Alignment.End)) {
            TextButton(onClick = onCancel) {
                Text(text = "Cancel")
            }
            TextButton(
                onClick = {
                    val paymentMethod = PaymentMethod.WCPayCard(
                        card = PaymentCard(
                            number = cardNumber,
                            cvc = cvc,
                            expiryMonth = expiry.take(2).toInt(),
                            expiryYear = expiry.drop(2).toInt()
                        )
                    )
                    onAddPaymentMethod(paymentMethod)
                },
                enabled = isValid
            ) {
                Text(text = "Add")
            }
        }
    }
}

@Composable
private fun ColumnScope.CreditCardField(cardNumber: String, onCardNumberChange: (String) -> Unit) {
    val expiryTextTransformation = remember {
        VisualTransformation {
            val annotatedString = buildAnnotatedString {
                for (i in it.text.indices) {
                    append(it.text[i])
                    if (i % 4 == 3 && i != 15) {
                        append("  ")
                    }
                }
            }

            val creditCardOffsetTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset <= 3) return offset
                    if (offset <= 7) return offset + 2
                    if (offset <= 11) return offset + 4
                    if (offset <= 16) return offset + 6
                    return 22
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (offset <= 4) return offset
                    if (offset <= 9) return offset - 2
                    if (offset <= 14) return offset - 4
                    if (offset <= 19) return offset - 6
                    return 16
                }
            }

            TransformedText(annotatedString, creditCardOffsetTranslator)
        }
    }

    OutlinedTextField(
        value = cardNumber,
        onValueChange = { value ->
            onCardNumberChange(value.filter { it.isDigit() && it != '.' }.take(16))
        },
        label = {
            Text(text = "Card number")
        },
        visualTransformation = expiryTextTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun RowScope.ExpiryTextField(expiry: String, onExpiryChanged: (String) -> Unit) {
    val expiryTextTransformation = remember {
        VisualTransformation {

            if (it.text.length < 2) {
                return@VisualTransformation TransformedText(it, OffsetMapping.Identity)
            }

            return@VisualTransformation TransformedText(
                text = buildAnnotatedString {
                    append(it.text.take(2))
                    withStyle(SpanStyle(Color.Gray)) {
                        append("/")
                    }
                    append(it.text.drop(2))
                },
                offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int =
                        if (offset < 2) offset else offset + 1

                    override fun transformedToOriginal(offset: Int): Int =
                        if (offset < 2) offset else offset - 1
                }
            )
        }
    }

    OutlinedTextField(
        value = expiry,
        onValueChange = { value ->
            val filteredText = value.take(4).filter { it.isDigit() && it != '.' }
            val month = filteredText.take(2)
                .let { if (it.length == 1) it + "0" else it }.toInt()
            if (month in 0..12) {
                onExpiryChanged(filteredText)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = expiryTextTransformation,
        label = {
            Text(text = "Expiry")
        },
        modifier = Modifier.weight(1f)
    )
}

@Preview
@Composable
private fun CreditCardFormPreview() {
    WCStoreAppTheme {

    }
}