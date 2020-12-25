package ru.app.models

import android.os.Parcelable

data class Stadium(
        val name: String,
        val image: String,
        val address: String,
        val price: String,
        val rating: Float,
        val description: String
)