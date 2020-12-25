package ru.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.fragment_history_bookings.view.*
import ru.app.R
import ru.app.adapters.UserCardAdapter
import ru.app.models.booking.FieldBookingResponse

class HistoryBookingFragment : Fragment() {


    lateinit var navController: NavController
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_history_bookings, container, false)
        rootView.history_booking_back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        return rootView
    }

}