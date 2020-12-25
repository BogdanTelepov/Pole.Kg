package ru.app.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.custom_dialog_2.view.*
import kotlinx.android.synthetic.main.custom_dialog_2.view.custom_dialog_2_btn_cancel
import kotlinx.android.synthetic.main.custom_dialog_3.view.*
import kotlinx.android.synthetic.main.feedback_alert_dialog.*
import kotlinx.android.synthetic.main.feedback_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_playground_owner_profile.*
import kotlinx.android.synthetic.main.fragment_playground_owner_profile.view.*
import kotlinx.android.synthetic.main.fragment_requests.view.*
import kotlinx.android.synthetic.main.playground_owner_card.*
import retrofit2.Call
import retrofit2.Response
import ru.app.MainActivity
import ru.app.R
import ru.app.adapters.OwnerCardAdapter
import ru.app.models.auth.RefreshToken
import ru.app.models.booking.BookingItem
import ru.app.models.booking.FieldBookingResponse
import ru.app.models.booking.FieldReviewRequest
import ru.app.models.booking.requests.RequestStatus
import ru.app.models.fields.FieldReviewsItem
import ru.app.network.ApiService
import ru.app.network.RetrofitInstance
import ru.app.network.SessionManager

class PlaygroundOwnerProfileFragment : Fragment(), OwnerCardAdapter.OnItemClickListener{

    private var bookingItems: ArrayList<BookingItem> = ArrayList()
    lateinit var adapter : OwnerCardAdapter
    lateinit var navController : NavController
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var sessionManager: SessionManager
    lateinit var token: String
    lateinit var retrofitInstance: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_playground_owner_profile, container, false)
        setUpDrawerMenu(rootView)
        sessionManager = SessionManager(requireContext())
        token = sessionManager.fetchAuthToken().toString()
        retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        getBookings(rootView)

        rootView.playground_owner_swipe_refresher.setColorSchemeColors(Color.parseColor("#0D8549"))
        rootView.playground_owner_swipe_refresher.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                getBookings(rootView)

            }, 200)
        }


        return rootView
    }

    private fun getBookings(view: View) {
        val call = token.let { retrofitInstance.getBookings("Bearer $it") }
        call.enqueue(object : retrofit2.Callback<ArrayList<BookingItem>> {
            override fun onFailure(call: Call<ArrayList<BookingItem>>, t: Throwable) {
                Log.e("error", "getting bookings ${t.message}")
               Toast.makeText(context, "Проверьте интернет соединение", Toast.LENGTH_SHORT).show()
            }
            
            override fun onResponse(
                call: Call<ArrayList<BookingItem>>,
                response: Response<ArrayList<BookingItem>>
            ) {
                if (response.isSuccessful && response.body()?.size != 0) {
                    profile_empty_bookings_text_view.visibility = View.GONE
                    bookingItems.clear()
                    for (i in 0..response.body()!!.size - 1) {
                        if (response.body()!![i].status == "Одобрено" || response.body()!![i].status == "Открыто") {
                            bookingItems.add(response.body()!![i])
                        }
                    }
                    playground_owner_swipe_refresher.isRefreshing = false
                   // Log.e("user id", bookingItems[0].user.full_name.toString())
                    Log.e("id: ", sessionManager.fetchUserId().toString())
                    if (sessionManager.fetchUserId() == -1){
                        sessionManager.saveUserId(bookingItems[0].user.id, bookingItems[0].user.full_name)
                    }


                }
                else if(response.code() == 403){
                    retrofitInstance.refreshToken(RefreshToken("$token")).enqueue(object:
                        retrofit2.Callback<RefreshToken> {
                        override fun onFailure(call: Call<RefreshToken>, t: Throwable) {
                            Toast.makeText(context, "проверьте интернет соединение", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<RefreshToken>,
                            response: Response<RefreshToken>
                        ) {
                            if (response.isSuccessful){
                                sessionManager.deleteOldToken()
                                sessionManager.saveAuthToken(response.body()!!.token)
                                getBookings(view)
                            }
                            else{
                                Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                            }
                        }

                    })
                              }
                else{
                    profile_empty_bookings_text_view.visibility = View.VISIBLE
                }
                updateUI(view, bookingItems)
            }
            })
    }
    private fun updateUI(view: View, bookingItems: List<BookingItem>) {
        adapter = context?.let { OwnerCardAdapter(it, this, bookingItems) }!!
        view.playground_owner_profile_recycler_view.adapter = adapter
    }
    //drawer menu
    private fun setUpDrawerMenu(rootView: View) {
        rootView.toolbar.setNavigationIcon(R.drawable.ic_burger_menu)
        toggle = ActionBarDrawerToggle(rootView.context as Activity?, rootView.drawer_layout, rootView.toolbar, R.string.open, R.string.close)
        rootView.drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        rootView.nav_view.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.requests -> navController.navigate(R.id.action_playgroundOwnerProfileFragment_to_requestsFragment)
                R.id.history_booking -> navController.navigate(R.id.action_playgroundOwnerProfileFragment_self)
                R.id.my_playgrounds -> navController.navigate(R.id.action_playgroundOwnerProfileFragment_to_myFieldsFragment)
                 R.id.blacklist -> navController.navigate(R.id.action_playgroundOwnerProfileFragment_to_blackListFragment)
                R.id.personal_info -> navController.navigate(R.id.action_playgroundOwnerProfileFragment_to_personalInformationFragment)
                R.id.add_playground -> navController.navigate(R.id.action_playgroundOwnerProfileFragment_to_addFieldFragment)
            };true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onItemCancel(position: Int, id: Int, bookingItem: BookingItem, v: View) {
        if(bookingItem.is_finished) {
            val fieldId = bookingItem.field.id
            val userId = bookingItem.user.id
            var rating = -1
            var description = ""

            // Custom Alert Dialog
            val alertDialog = LayoutInflater.from(context).inflate(R.layout.feedback_alert_dialog, null)
            val builder = AlertDialog.Builder(context).setView(alertDialog).show()
            builder.setView(v);
            alertDialog.feedback_cancel_button.setOnClickListener {
                builder.dismiss()
            }
            alertDialog.feedback_rating_bar.setOnRatingBarChangeListener{ ratingBar: RatingBar, fl: Float, b: Boolean ->
                rating = fl.toInt()
                Toast.makeText(context, "$rating", Toast.LENGTH_SHORT).show()
            }
            alertDialog.feedback_send_button.setOnClickListener {
                description = alertDialog.feedback_edit_text.text.toString()
                if (rating == -1) {
                    Toast.makeText(context, getString(R.string.rate), Toast.LENGTH_SHORT).show()

                } else if (description == "") {
                    Toast.makeText(context, getString(R.string.add_description), Toast.LENGTH_SHORT).show()

                } else {
                    val body = FieldReviewRequest(description,fieldId,rating,userId)
                    retrofitInstance.createFieldReview("Bearer $token", body)
                            .enqueue(object:retrofit2.Callback<FieldReviewsItem> {

                                override fun onResponse(
                                        call: Call<FieldReviewsItem>,
                                        response: Response<FieldReviewsItem>
                                ) {
                                    if (response.isSuccessful) {

                                        Toast.makeText(context, getString(R.string.description_added), Toast.LENGTH_SHORT).show()
                                        builder.dismiss()

                                    } else{
                                        Toast.makeText(context, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<FieldReviewsItem>, t: Throwable) {
                                    Toast.makeText(context, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                                }
                            })
                }
            }

        } else {
            retrofitInstance.request("Bearer $token", id, RequestStatus(bookingItem.field.id, 3))
                    .enqueue(object:retrofit2.Callback<FieldBookingResponse> {
                        override fun onFailure(call: Call<FieldBookingResponse>, t: Throwable) {
                            Toast.makeText(context, "Проверьте интернет соединение", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(
                                call: Call<FieldBookingResponse>,
                                response: Response<FieldBookingResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Бронь отменена", Toast.LENGTH_SHORT).show()
                                bookingItems.remove(bookingItem)
                                adapter.notifyItemRemoved(position)

                            }
                            else{
                                Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
        }
    }
}
