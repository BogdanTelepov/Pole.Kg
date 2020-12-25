package com.example.organicinkgandroid.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
 import ru.app.database.settings.SettingsDAO
import ru.app.database.settings.SettingsEntity

// Declares the DAO as a private property in the constructor. Pass in the DAO instead of the whole database, because you only need access to the DAO
class Repository(private val filtrationDao: SettingsDAO) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allSettings: LiveData<List<SettingsEntity>> = filtrationDao.loadAllSettings()

    // The suspend modifier tells the compiler that this needs to be called from a coroutine or another suspending function.
    /*@Suppress("RedundantSuspendModifier")*/

    // Settings functions
    @WorkerThread
    suspend fun insertSetting(setting: SettingsEntity) {
        filtrationDao.insertSetting(setting)
    }
    @WorkerThread
    suspend fun updateSetting(setting: SettingsEntity) {
        filtrationDao.updateSetting(setting)
    }
    @WorkerThread
    suspend fun deleteSetting(setting: SettingsEntity) {
        filtrationDao.deleteSetting(setting)
    }
    @WorkerThread
    suspend fun deleteAllSettings() {
        filtrationDao.deleteAllSettings()
    }

}
