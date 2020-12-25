package ru.app.models.booking


import com.google.gson.annotations.SerializedName

data class FieldReviewRequest(
    @SerializedName("description")
    val description: String?,
    @SerializedName("field")
    val fieldId: Int?,
    @SerializedName("rate")
    val rate: Int?,
    @SerializedName("user")
    val userId: Int?
)