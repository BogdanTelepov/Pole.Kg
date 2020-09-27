package ru.app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.app.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, LoginFragment::class.java)
        startActivity(intent)
        finish()
//        val navController = Navigation.findNavController(this, R.id.NavHostFragment)
//        NavigationUI.setupActionBarWithNavController(this, navController)
//
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(
//            Navigation.findNavController(this, R.id.NavHostFragment),
//            null
//        )
    }
}
