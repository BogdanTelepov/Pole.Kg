package ru.app.database.settings

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "booking_table")
data class BookingEntity(
        @ColumnInfo(name = "id")
        var bookingId: Int,
        @ColumnInfo(name = "field")
        var fieldId : Int,
        @ColumnInfo(name = "booking_date")
        var date: String,
        @ColumnInfo(name = "time_start")
        var time_start : String,
        @ColumnInfo(name = "time_end")
        var time_end: String


)