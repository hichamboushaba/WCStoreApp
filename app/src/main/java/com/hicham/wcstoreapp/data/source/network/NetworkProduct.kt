package com.hicham.wcstoreapp.data.source.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

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

    val type: String,
    val status: String,
    val featured: Boolean,

    @SerialName("catalog_visibility")
    val catalogVisibility: String,

    val description: String,

    @SerialName("short_description")
    val shortDescription: String,

    val sku: String,
    val price: String,

    @SerialName("regular_price")
    val regularPrice: String,

    @SerialName("sale_price")
    val salePrice: String,

    @SerialName("date_on_sale_from")
    val dateOnSaleFrom: String? = null,

    @SerialName("date_on_sale_from_gmt")
    val dateOnSaleFromGmt: JsonObject? = null,

    @SerialName("date_on_sale_to")
    val dateOnSaleTo: String? = null,

    @SerialName("date_on_sale_to_gmt")
    val dateOnSaleToGmt: String? = null,

    @SerialName("price_html")
    val priceHTML: String,

    @SerialName("on_sale")
    val onSale: Boolean,

    val purchasable: Boolean,

    @SerialName("total_sales")
    val totalSales: Long,

    val virtual: Boolean,
    val downloadable: Boolean,
    val downloads: JsonArray,

    @SerialName("download_limit")
    val downloadLimit: Long,

    @SerialName("download_expiry")
    val downloadExpiry: Long,

    @SerialName("external_url")
    val externalURL: String,

    @SerialName("button_text")
    val buttonText: String,

    @SerialName("tax_status")
    val taxStatus: String,

    @SerialName("tax_class")
    val taxClass: String,

    @SerialName("manage_stock")
    val manageStock: Boolean,

    @SerialName("stock_quantity")
    val stockQuantity: JsonObject? = null,

    @SerialName("stock_status")
    val stockStatus: String,

    val backorders: String,

    @SerialName("backorders_allowed")
    val backordersAllowed: Boolean,

    val backordered: Boolean,

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

    @SerialName("related_ids")
    val relatedIDS: List<Long>,

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
    val groupedProducts: JsonArray,

    @SerialName("menu_order")
    val menuOrder: Long,

    @SerialName("meta_data")
    val metaData: JsonArray,

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
