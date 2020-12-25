package ru.app.models.auth

import com.google.gson.annotations.SerializedName

class RefreshToken(
        @SerializedName("token")
        var token:String
)