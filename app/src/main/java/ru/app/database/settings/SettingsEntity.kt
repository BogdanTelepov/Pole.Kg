package ru.app.database.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings_table")
data class SettingsEntity (

    @ColumnInfo(name = "priceFrom")
    var priceFrom: Int,

    @ColumnInfo(name = "priceUntil")
    var priceUntil: Int,

    @ColumnInfo(name = "playersFrom")
    var playersFrom: Int,

    @ColumnInfo(name = "playersUntil")
    var playersUntil: Int,

    @ColumnInfo(name = "desiredDate")
    var desiredDate: String,

    @ColumnInfo(name = "desiredTimeFrom")
    var desiredTimeFrom: String,

    @ColumnInfo(name = "desiredTimeUntil")
    var desiredTimeUntil: String,

    @ColumnInfo(name = "fieldType")
    var fieldType: String,

    @ColumnInfo(name = "isInDoor")
    var isInDoor: String,

    @ColumnInfo(name = "hasParking")
    var hasParking: String,

    @ColumnInfo(name = "hasShowers")
    var hasShowers: String,

    @ColumnInfo(name = "hasLockerRooms")
    var hasLockerRooms: String,

    @ColumnInfo(name = "hasLights")
    var hasLights: String,

    @ColumnInfo(name = "hasRostrum")
    var hasRostrum: String,

    @ColumnInfo(name = "hasEquipment")
    var hasEquipment: String,

    @ColumnInfo(name = "orderFilter")
    var orderFilter: String

) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}