package ru.app.ui.main

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.fragment_feedbacks.*
import kotlinx.android.synthetic.main.fragment_requests.view.*
import kotlinx.android.synthetic.main.fragment_requests_confirmed.*
import kotlinx.android.synthetic.main.fragment_requests_confirmed.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.app.R
import ru.app.adapters.ConfirmedRequestsAdapter
import ru.app.models.booking.BookingItem
import ru.app.models.fields.FieldReviewsItem
import ru.app.network.ApiService
import ru.app.network.RetrofitInstance
import ru.app.network.SessionManager

class ConfirmedRequestsFragment : Fragment() {

    private val TAG = "ConfirmedRequestsFragment"
    private lateinit var confirmedRequestsAdapter: ConfirmedRequestsAdapter
    lateinit var sessionManager: SessionManager
    lateinit var token: String
    var requests : ArrayList<BookingItem> = ArrayList<BookingItem>()
    lateinit var retrofitInstance: ApiService
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_requests_confirmed, container, false)
        progressBar = rootView.confirmed_requests_fragment_progress_bar
        progressBar.visibility = View.VISIBLE
        sessionManager = SessionManager(requireContext())
        token = sessionManager.fetchAuthToken().toString()
        retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        getRequests(rootView)
        rootView.confirmed_requests_swipe_refresher.setColorSchemeColors(Color.parseColor("#0D8549"))
        rootView.confirmed_requests_swipe_refresher.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                getRequests(rootView)

            }, 200)
        }

        return rootView

    }

    private fun getRequests(view : View) {
        retrofitInstance.getRequests("Bearer $token", 2)
            .enqueue(object: Callback<ArrayList<BookingItem>>{
                override fun onFailure(call: Call<ArrayList<BookingItem>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<BookingItem>>,
                    response: Response<ArrayList<BookingItem>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body()!!.size != 0){
                        requests_fragment_nothing_found.visibility = View.GONE
                        requests.clear()
                        requests = response.body()!!
                        updateUI(view)
                        confirmed_requests_swipe_refresher.isRefreshing = false
                    }
                    else {
                        requests_fragment_nothing_found.visibility = View.VISIBLE
                    }
                }

            })
    }

    private fun updateUI(view: View) {
        confirmedRequestsAdapter = ConfirmedRequestsAdapter(requireContext(), requests)
        view.requests_confirmed_recycler_view.adapter = confirmedRequestsAdapter
    }

}