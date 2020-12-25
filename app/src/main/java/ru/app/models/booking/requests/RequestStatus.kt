package ru.app.models.booking.requests

import com.google.gson.annotations.SerializedName
import ru.app.models.auth.User

data class RequestStatus (
    @SerializedName("field")
    var field:Int,
    @SerializedName("status")
    var status: Int
)