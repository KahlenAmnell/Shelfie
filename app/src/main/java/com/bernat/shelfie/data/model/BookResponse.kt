package com.bernat.shelfie.data.model

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("items")
    val items: List<BookItem>?
)

data class BookItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("volumeInfo")
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    @SerializedName("title")
    val title: String?,
    @SerializedName("authors")
    val authors: List<String>?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("pageCount")
    val pageCount: Int?,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks?,
    @SerializedName("industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifier>?
)

data class ImageLinks(
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("smallThumbnail")
    val smallThumbnail: String?
)

data class IndustryIdentifier(
    @SerializedName("type")
    val type: String?, // np. ISBN_10 lub ISBN_13
    @SerializedName("identifier")
    val identifier: String?
)