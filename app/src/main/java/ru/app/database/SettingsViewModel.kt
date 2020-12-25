package ru.app.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.organicinkgandroid.database.Repository
import ru.app.database.settings.SettingsEntity
import ru.app.database.settings.SettingsRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: Repository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allFiltration: LiveData<List<SettingsEntity>>

    init {

        val filtrationDao = SettingsRoomDatabase.getDatabase(application).settingsDAO()

        productRepository = Repository(filtrationDao)

        allFiltration = productRepository.allSettings

    }

    /**
     * Launching a new coroutine to insert/update/delete a in a non-blocking way
     */

    /**
     * Settings functions
     */
    fun insertSetting(setting: SettingsEntity) = viewModelScope.launch(Dispatchers.IO) {
        productRepository.insertSetting(setting)
    }

    fun updateSetting(setting: SettingsEntity) = viewModelScope.launch(Dispatchers.IO) {
        productRepository.updateSetting(setting)
    }

    fun deleteSetting(setting: SettingsEntity) = viewModelScope.launch(Dispatchers.IO) {
        productRepository.deleteSetting(setting)
    }

    fun deleteAllSettings() = viewModelScope.launch(Dispatchers.IO) {
        productRepository.deleteAllSettings()
    }

}
