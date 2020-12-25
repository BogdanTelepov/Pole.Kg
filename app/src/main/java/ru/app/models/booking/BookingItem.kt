package ru.app.models.booking

import com.google.gson.annotations.SerializedName
import ru.app.models.auth.User
import ru.app.models.fields.FieldList
import ru.app.models.fields.FieldListItem

data class BookingItem(
        @SerializedName("id")
        var id: Int,
        @SerializedName("field")
        var field: FieldListItem,
        @SerializedName("image")
        var image: String,
        @SerializedName("user")
        var user: User,
        @SerializedName("booking_date")
        var booking_date: String,
        @SerializedName("time_start")
        var timeStart: String,
        @SerializedName("time_end")
        var timeEnd: String,
        @SerializedName("status")
        var status: String,
        @SerializedName("created_at")
        var created_at: String,
        @SerializedName("is_finished")
        var is_finished: Boolean

)