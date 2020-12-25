package ru.app.database.settings

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookingDao {

    @Query("select * from booking_table")
    fun getAllBookings () : LiveData<List<BookingEntity>>

    @Insert
    fun insertBooking (booking: BookingEntity)

    @Delete
    fun deleteBooking (booking: BookingEntity)

    @Query("DELETE FROM booking_table")
    fun deleteAllBookings()

}