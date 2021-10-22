@file:UseSerializers(BigDecimalSerializer::class)

package com.hicham.wcstoreapp.data.api

import com.hicham.wcstoreapp.util.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.JsonElement
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
    val featured: Boolean,

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
    val dateOnSaleFrom: String? = null,

    @SerialName("date_on_sale_from_gmt")
    val dateOnSaleFromGmt: String? = null,

    @SerialName("date_on_sale_to")
    val dateOnSaleTo: String? = null,

    @SerialName("date_on_sale_to_gmt")
    val dateOnSaleToGmt: String? = null,

    @SerialName("on_sale")
    val onSale: Boolean,

    val purchasable: Boolean,

    val virtual: Boolean,
    val downloadable: Boolean,
    val downloads: List<Download>,

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
    val lowStockAmount: Double? = null,

    @SerialName("sold_individually")
    val soldIndividually: Boolean,

    val weight: String,
    val dimensions: Dimensions? = null,

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

    @SerialName("parent_id")
    val parentID: Long? = null,

    val categories: List<NetworkCategory>,
    val tags: List<String>,
    val images: List<Image>,
    val attributes: List<Attribute>,

    @SerialName("grouped_products")
    val groupedProducts: List<Long>,

    @SerialName("related_ids")
    val relatedIDS: List<Long>,

    @SerialName("meta_data")
    val metaData: List<MetaDataItem>,

    @SerialName("stock_status")
    val stockStatus: StockStatus
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
data class MetaDataItem(
    val id: Long,
    val key: String,
    val value: JsonElement
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
enum class StockStatus {
    @SerialName("instock")
    Instock,

    @SerialName("outofstock")
    OutOfStock,

    @SerialName("onbackorder")
    OnBackOrder
}

@Serializable
enum class TaxStatus {
    @SerialName("taxable")
    Taxable,

    @SerialName("shipping")
    Shipping,

    @SerialName("none")
    None
}

@Serializable
enum class Type {
    @SerialName("grouped")
    Grouped,

    @SerialName("simple")
    Simple,

    @SerialName("variable")
    Variable,

    @SerialName("external")
    External
}
