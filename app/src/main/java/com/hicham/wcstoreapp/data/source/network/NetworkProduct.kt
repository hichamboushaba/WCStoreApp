@file:UseSerializers(BigDecimalSerializer::class)

package com.hicham.wcstoreapp.data.source.network

import com.hicham.wcstoreapp.util.BigDecimalSerializer
import kotlinx.serialization.*
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import java.math.BigDecimal

@Serializable
data class NetworkProduct(
    val id: Long,
    val name: String,
    val slug: String,
    val permalink: String,

    @SerialName("date_created")
    val dateCreated: String,

    @SerialName("date_created_gmt")
    val dateCreatedGmt: String,

    @SerialName("date_modified")
    val dateModified: String,

    @SerialName("date_modified_gmt")
    val dateModifiedGmt: String,

    val type: Type,
    val status: Status,
    val featured: Boolean,

    @SerialName("catalog_visibility")
    val catalogVisibility: CatalogVisibility,

    val description: String,

    @SerialName("short_description")
    val shortDescription: String,

    val sku: String,
    val price: BigDecimal,

    @SerialName("regular_price")
    val regularPrice: BigDecimal?,

    @SerialName("sale_price")
    val salePrice: BigDecimal?,

    @SerialName("date_on_sale_from")
    val dateOnSaleFrom: JsonObject? = null,

    @SerialName("date_on_sale_from_gmt")
    val dateOnSaleFromGmt: JsonObject? = null,

    @SerialName("date_on_sale_to")
    val dateOnSaleTo: JsonObject? = null,

    @SerialName("date_on_sale_to_gmt")
    val dateOnSaleToGmt: JsonObject? = null,

    @SerialName("on_sale")
    val onSale: Boolean,

    val purchasable: Boolean,

    @SerialName("total_sales")
    val totalSales: Long,

    val virtual: Boolean,
    val downloadable: Boolean,
    val downloads: List<Download>,

    @SerialName("download_limit")
    val downloadLimit: Long,

    @SerialName("download_expiry")
    val downloadExpiry: Long,

    @SerialName("external_url")
    val externalURL: String,

    @SerialName("button_text")
    val buttonText: String,

    @SerialName("tax_status")
    val taxStatus: TaxStatus,

    @SerialName("tax_class")
    val taxClass: String,

    @SerialName("manage_stock")
    val manageStock: Boolean,

    @SerialName("stock_quantity")
    val stockQuantity: Double? = null,

    val backorders: Backorders,

    @SerialName("backorders_allowed")
    val backordersAllowed: Boolean,

    val backordered: Boolean,

    @SerialName("low_stock_amount")
    val lowStockAmount: JsonObject? = null,

    @SerialName("sold_individually")
    val soldIndividually: Boolean,

    val weight: String,
    val dimensions: Dimensions,

    @SerialName("shipping_required")
    val shippingRequired: Boolean,

    @SerialName("shipping_taxable")
    val shippingTaxable: Boolean,

    @SerialName("shipping_class")
    val shippingClass: String,

    @SerialName("shipping_class_id")
    val shippingClassID: Long,

    @SerialName("reviews_allowed")
    val reviewsAllowed: Boolean,

    @SerialName("average_rating")
    val averageRating: String,

    @SerialName("rating_count")
    val ratingCount: Long,

    @SerialName("upsell_ids")
    val upsellIDS: JsonArray,

    @SerialName("cross_sell_ids")
    val crossSellIDS: JsonArray,

    @SerialName("parent_id")
    val parentID: Long,

    @SerialName("purchase_note")
    val purchaseNote: String,

    val categories: List<Category>,
    val tags: JsonArray,
    val images: List<Image>,
    val attributes: List<Attribute>,

    @SerialName("default_attributes")
    val defaultAttributes: JsonArray,

    val variations: JsonArray,

    @SerialName("grouped_products")
    val groupedProducts: List<Long>,

    @SerialName("menu_order")
    val menuOrder: Long,

    @SerialName("price_html")
    val priceHTML: String,

    @SerialName("related_ids")
    val relatedIDS: List<Long>,

    @SerialName("meta_data")
    val metaData: List<MetaDatum>,

    @SerialName("stock_status")
    val stockStatus: StockStatus,

    @SerialName("jetpack_publicize_connections")
    val jetpackPublicizeConnections: JsonArray,

    @SerialName("jetpack_likes_enabled")
    val jetpackLikesEnabled: Boolean,

    @SerialName("jetpack_sharing_enabled")
    val jetpackSharingEnabled: Boolean,

    @SerialName("amp_enabled")
    val ampEnabled: Boolean,

    @SerialName("_links")
    val links: Links
)

@Serializable
data class Attribute(
    val id: Long,
    val name: String,
    val position: Long,
    val visible: Boolean,
    val variation: Boolean,
    val options: List<String>
)

@Serializable
enum class Backorders {
    @SerialName("no")
    No,

    @SerialName("yes")
    Yes
}

@Serializable
enum class CatalogVisibility {
    @SerialName("hidden")
    Hidden,

    @SerialName("visible")
    Visible
}

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val slug: String
)

@Serializable
data class Dimensions(
    val length: String,
    val width: String,
    val height: String
)

@Serializable
data class Download(
    val id: String,
    val name: String,
    val file: String
)

@Serializable
data class Image(
    val id: Long,

    @SerialName("date_created")
    val dateCreated: String,

    @SerialName("date_created_gmt")
    val dateCreatedGmt: String,

    @SerialName("date_modified")
    val dateModified: String,

    @SerialName("date_modified_gmt")
    val dateModifiedGmt: String,

    val src: String,
    val name: String,
    val alt: String
)

@Serializable
data class Links(
    val self: List<Collection>,
    val collection: List<Collection>
)

@Serializable
data class Collection(
    val href: String
)

@Serializable
data class MetaDatum(
    val id: Long,
    val key: String,
    val value: JsonElement
)

@Serializable
data class PurpleValue(
    val name: String,

    @SerialName("title_format")
    val titleFormat: String,

    @SerialName("description_enable")
    val descriptionEnable: Long,

    val description: String,
    val type: String,
    val display: String,
    val position: Long,
    val required: Long,
    val restrictions: Long,

    @SerialName("restrictions_type")
    val restrictionsType: String,

    @SerialName("adjust_price")
    val adjustPrice: Long,

    @SerialName("price_type")
    val priceType: String,

    val price: String,
    val min: Long,
    val max: Long,
    val options: List<Option>
)

@Serializable
data class Option(
    val label: String,
    val price: String,
    val image: String,

    @SerialName("price_type")
    val priceType: String
)

@Serializable
data class FluffyValue(
    val description: String? = null,

    @SerialName("hs_tariff_number")
    val hsTariffNumber: String? = null,

    @SerialName("origin_country")
    val originCountry: String? = null,

    val id: String? = null,
    val type: String? = null,
    val layout: Layout? = null,
    val fields: List<Field>? = null,

    @SerialName("rule_groups")
    val ruleGroups: List<RuleGroup>? = null
)

@Serializable
data class Field(
    val id: String,
    val label: String,
    val description: JsonObject? = null,
    val type: String,
    val required: Boolean,

    @SerialName("class")
    val fieldClass: JsonObject? = null,

    val width: JsonObject? = null,
    val options: JsonArray,
    val conditionals: JsonArray,
    val pricing: Pricing
)

@Serializable
data class Pricing(
    val type: String,
    val amount: Long,
    val enabled: Boolean
)

@Serializable
data class Layout(
    @SerialName("labels_position")
    val labelsPosition: String,

    @SerialName("instructions_position")
    val instructionsPosition: String,

    @SerialName("mark_required")
    val markRequired: Boolean
)

@Serializable
data class RuleGroup(
    val rules: List<Rule>
)

@Serializable
data class Rule(
    val value: List<RuleValue>,
    val condition: String,
    val subject: String
)

@Serializable
data class RuleValue(
    val id: String,
    val text: String
)

@Serializable
enum class Status {
    @SerialName("publish")
    Published
}

@Serializable
enum class StockStatus {
    @SerialName("instock")
    Instock
}

@Serializable
enum class TaxStatus {
    @SerialName("taxable")
    Taxable
}

@Serializable
enum class Type {
    @SerialName("grouped")
    Grouped,

    @SerialName("simple")
    Simple,

    @SerialName("variable")
    Variable
}
