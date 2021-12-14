// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json        = Json(JsonConfiguration.Stable)
// val networkCart = json.parse(NetworkCart.serializer(), jsonString)

package com.hicham.wcstoreapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

@Serializable
data class NetworkCart(
    val coupons: List<NetworkCoupon>,

    @SerialName("shipping_rates")
    val shippingRates: List<NetworkCartShippingRate>,

    @SerialName("shipping_address")
    val shippingAddress: ShippingAddress,

    val items: List<NetworkCartItem>,

    @SerialName("items_count")
    val itemsCount: Long,

    @SerialName("items_weight")
    val itemsWeight: Long,

    @SerialName("needs_payment")
    val needsPayment: Boolean,

    @SerialName("needs_shipping")
    val needsShipping: Boolean,

    @SerialName("has_calculated_shipping")
    val hasCalculatedShipping: Boolean,

    val totals: NetworkCartTotals,
    val errors: JsonArray,

    @SerialName("payment_requirements")
    val paymentRequirements: List<String>,

    val extensions: Extensions
)

@Serializable
data class NetworkCoupon(
    val code: String,
    val totals: CouponTotals
)

@Serializable
data class CouponTotals(
    @SerialName("currency_code")
    val currencyCode: String,

    @SerialName("currency_symbol")
    val currencySymbol: String,

    @SerialName("currency_minor_unit")
    val currencyMinorUnit: Long,

    @SerialName("currency_decimal_separator")
    val currencyDecimalSeparator: String,

    @SerialName("currency_thousand_separator")
    val currencyThousandSeparator: String,

    @SerialName("currency_prefix")
    val currencyPrefix: String,

    @SerialName("currency_suffix")
    val currencySuffix: String,

    @SerialName("total_discount")
    val totalDiscount: String,

    @SerialName("total_discount_tax")
    val totalDiscountTax: String
)

@Serializable
class Extensions()

@Serializable
data class NetworkCartItem(
    val key: String,
    val id: Long,
    val quantity: Long,

    @SerialName("quantity_limit")
    val quantityLimit: Long,

    val name: String,
    val summary: String,

    @SerialName("short_description")
    val shortDescription: String,

    val description: String,
    val sku: String,

    @SerialName("low_stock_remaining")
    val lowStockRemaining: JsonObject? = null,

    @SerialName("backorders_allowed")
    val backordersAllowed: Boolean,

    @SerialName("show_backorder_badge")
    val showBackorderBadge: Boolean,

    @SerialName("sold_individually")
    val soldIndividually: Boolean,

    val permalink: String,
    val images: List<Image>,
    val variation: JsonArray,
    val prices: Prices,
    val totals: ItemTotals
)

@Serializable
data class Image(
    val id: Long,
    val src: String,
    val thumbnail: String,
    val srcset: String,
    val sizes: String,
    val name: String,
    val alt: String
)

@Serializable
data class Prices(
    @SerialName("currency_code")
    val currencyCode: String,

    @SerialName("currency_symbol")
    val currencySymbol: String,

    @SerialName("currency_minor_unit")
    val currencyMinorUnit: Long,

    @SerialName("currency_decimal_separator")
    val currencyDecimalSeparator: String,

    @SerialName("currency_thousand_separator")
    val currencyThousandSeparator: String,

    @SerialName("currency_prefix")
    val currencyPrefix: String,

    @SerialName("currency_suffix")
    val currencySuffix: String,

    val price: String,

    @SerialName("regular_price")
    val regularPrice: String,

    @SerialName("sale_price")
    val salePrice: String,

    @SerialName("price_range")
    val priceRange: JsonObject? = null,

    @SerialName("raw_prices")
    val rawPrices: RawPrices
)

@Serializable
data class RawPrices(
    val precision: Long,
    val price: String,

    @SerialName("regular_price")
    val regularPrice: String,

    @SerialName("sale_price")
    val salePrice: String
)

@Serializable
data class ItemTotals(
    @SerialName("currency_code")
    val currencyCode: String,

    @SerialName("currency_symbol")
    val currencySymbol: String,

    @SerialName("currency_minor_unit")
    val currencyMinorUnit: Long,

    @SerialName("currency_decimal_separator")
    val currencyDecimalSeparator: String,

    @SerialName("currency_thousand_separator")
    val currencyThousandSeparator: String,

    @SerialName("currency_prefix")
    val currencyPrefix: String,

    @SerialName("currency_suffix")
    val currencySuffix: String,

    @SerialName("line_subtotal")
    val lineSubtotal: String,

    @SerialName("line_subtotal_tax")
    val lineSubtotalTax: String,

    @SerialName("line_total")
    val lineTotal: String,

    @SerialName("line_total_tax")
    val lineTotalTax: String
)

@Serializable
data class ShippingAddress(
    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    val company: String,

    @SerialName("address_1")
    val address1: String,

    @SerialName("address_2")
    val address2: String,

    val city: String,
    val state: String,
    val postcode: String,
    val country: String
)

@Serializable
data class NetworkCartShippingRate(
    @SerialName("package_id")
    val packageID: Long,

    val name: String,
    val destination: Destination,
    val items: List<ShippingRateItem>,

    @SerialName("shipping_rates")
    val shippingRates: List<ShippingRateShippingRate>
)

@Serializable
data class Destination(
    @SerialName("address_1")
    val address1: String,

    @SerialName("address_2")
    val address2: String,

    val city: String,
    val state: String,
    val postcode: String,
    val country: String
)

@Serializable
data class ShippingRateItem(
    val key: String,
    val name: String,
    val quantity: Long
)

@Serializable
data class ShippingRateShippingRate(
    @SerialName("rate_id")
    val rateID: String,

    val name: String,
    val description: String,

    @SerialName("delivery_time")
    val deliveryTime: String,

    val price: String,

    @SerialName("instance_id")
    val instanceID: Long,

    @SerialName("method_id")
    val methodID: String,

    @SerialName("meta_data")
    val metaData: List<MetaDatum>,

    val selected: Boolean,

    @SerialName("currency_code")
    val currencyCode: String,

    @SerialName("currency_symbol")
    val currencySymbol: String,

    @SerialName("currency_minor_unit")
    val currencyMinorUnit: Long,

    @SerialName("currency_decimal_separator")
    val currencyDecimalSeparator: String,

    @SerialName("currency_thousand_separator")
    val currencyThousandSeparator: String,

    @SerialName("currency_prefix")
    val currencyPrefix: String,

    @SerialName("currency_suffix")
    val currencySuffix: String
)

@Serializable
data class MetaDatum(
    val key: String,
    val value: String
)

@Serializable
data class NetworkCartTotals(
    @SerialName("currency_code")
    val currencyCode: String,

    @SerialName("currency_symbol")
    val currencySymbol: String,

    @SerialName("currency_minor_unit")
    val currencyMinorUnit: Long,

    @SerialName("currency_decimal_separator")
    val currencyDecimalSeparator: String,

    @SerialName("currency_thousand_separator")
    val currencyThousandSeparator: String,

    @SerialName("currency_prefix")
    val currencyPrefix: String,

    @SerialName("currency_suffix")
    val currencySuffix: String,

    @SerialName("total_items")
    val totalItems: String,

    @SerialName("total_items_tax")
    val totalItemsTax: String,

    @SerialName("total_fees")
    val totalFees: String,

    @SerialName("total_fees_tax")
    val totalFeesTax: String,

    @SerialName("total_discount")
    val totalDiscount: String,

    @SerialName("total_discount_tax")
    val totalDiscountTax: String,

    @SerialName("total_shipping")
    val totalShipping: String,

    @SerialName("total_shipping_tax")
    val totalShippingTax: String,

    @SerialName("total_price")
    val totalPrice: String,

    @SerialName("total_tax")
    val totalTax: String,

    @SerialName("tax_lines")
    val taxLines: JsonArray
)