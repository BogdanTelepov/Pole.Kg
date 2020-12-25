package ru.app.models.booking


import com.google.gson.annotations.SerializedName
import ru.app.models.auth.User
import ru.app.models.auth.UserCreateResponse
import ru.app.models.fields.FieldListItem

data class FieldBookingResponse(
        @SerializedName("id")
    var id: Int,
        @SerializedName("field")
    var field:Int,
        @SerializedName("user")
    var user: Int,
        @SerializedName("booking_date")
    var booking_date: String,
        @SerializedName("time_start")
    var timeStart: String,
        @SerializedName("time_end")
    var timeEnd: String,
        @SerializedName("status")
        var status: Int,
        @SerializedName("created_at")
        var created_at: String

)