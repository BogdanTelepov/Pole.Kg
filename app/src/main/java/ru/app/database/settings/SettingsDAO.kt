package ru.app.database.settings

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.app.database.settings.SettingsEntity

@Dao
interface SettingsDAO {

    @Query("select * from settings_table")
    fun loadAllSettings () : LiveData<List<SettingsEntity>>

    @Insert
    fun insertSetting (setting: SettingsEntity)

    @Update
    fun updateSetting (setting: SettingsEntity)

    @Delete
    fun deleteSetting (setting: SettingsEntity)

    @Query("DELETE FROM settings_table")
    fun deleteAllSettings()

}