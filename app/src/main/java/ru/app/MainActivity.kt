package ru.app

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.app.R
import ru.app.database.SettingsViewModel
import ru.app.database.settings.SettingsEntity
import ru.app.models.addfield.WorkingHourX
import ru.app.models.fields.FieldListItem
import ru.app.models.fieldtypes.FieldTypesResponse
import ru.app.models.fieldtypes.FieldTypesResponseItem
import ru.app.network.ApiService
import ru.app.network.RetrofitInstance
import ru.app.ui.main.*
import ru.app.utils.Constants

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    var fieldTypeList = ArrayList<FieldTypesResponseItem>()
    var fieldTypeNamesList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        // Status Bar icon color
        val window: Window = this.window
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)


        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)

        getFieldTypes()

        }


    private fun getFieldTypes() {
        val retroInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

        val call = retroInstance.getFieldTypes()

        call.enqueue(object : Callback<FieldTypesResponse> {

            override fun onResponse(call: Call<FieldTypesResponse>, response: Response<FieldTypesResponse>) {

                if (response.isSuccessful) {

                    fieldTypeList = response.body()!!

                    for (item in 0 until response.body()!!.size) {
                        fieldTypeNamesList.add(response.body()!![item].name!!)
                    }

                    Log.e(TAG, "fieldTypeNamesList: $fieldTypeNamesList")

                } else {
                    Log.e(TAG, "onResponse: ${response.body()}")
                    Toast.makeText(this@MainActivity, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<FieldTypesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: $t")
                Toast.makeText(this@MainActivity, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.fragment),
            null
        )
    }
}