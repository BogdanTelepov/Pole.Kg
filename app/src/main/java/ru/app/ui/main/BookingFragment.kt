package ru.app.ui.main

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.book_alert_dialog.view.*
import kotlinx.android.synthetic.main.custom_dialog_3.view.*
import kotlinx.android.synthetic.main.custom_dialog_4.view.*
import kotlinx.android.synthetic.main.fragment_book.*
import kotlinx.android.synthetic.main.fragment_book.view.*
import okhttp3.internal.headersContentLength
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.app.R
import ru.app.models.auth.RefreshToken
import ru.app.models.booking.FieldBookingCreate
import ru.app.models.booking.FieldBookingResponse
import ru.app.network.ApiClient
import ru.app.network.SessionManager
import java.util.*
import kotlin.collections.ArrayList

class BookingFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    lateinit var desiredDate: String
    var desiredTimeFrom: String = ""
    var desiredTimeUntil: String =""
    lateinit var name: String
    lateinit var number: String
    lateinit var sessionManager: SessionManager
    lateinit var navController: NavController
    var bookingIds: ArrayList<FieldBookingResponse>  = ArrayList()
    private val desiredTimeArray = arrayOf("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00",
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_book, container, false)
        sessionManager = SessionManager(requireContext())
        if (sessionManager.fetchAuthToken().toString() == "null"){
            showAuthDialog()
        }
        rootView.booking_edit_text_name.setText(sessionManager.fetchUserName())
        rootView.booking_edit_text_number.setText(sessionManager.fetchUserPhoneNumber())

        rootView.booking_edit_text_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (rootView.booking_edit_text_name.text.isEmpty()) {
                    rootView.booking_edit_text_name.setBackgroundResource(R.drawable.background_stroke_grey)
                } else {
                    rootView.booking_edit_text_name.setBackgroundResource(R.drawable.background_rectangle_black)
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        rootView.booking_edit_text_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (rootView.booking_edit_text_number.text.isEmpty()) {
                    rootView.booking_edit_text_number.setBackgroundResource(R.drawable.background_stroke_grey)
                } else {
                    rootView.booking_edit_text_number.setBackgroundResource(R.drawable.background_rectangle_black)
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        rootView.booking_choose_date_card.setOnClickListener {
            chooseDate()
        }
        rootView.booking_time_from.setOnClickListener {
            desiredTimeFrom = chooseTimeFrom(rootView.booking_time_from)
            rootView.booking_time_from_frame.setBackgroundResource(R.drawable.background_rectangle_black)
        }
        rootView.booking_time_until.setOnClickListener {
            desiredTimeUntil = chooseTimeUntil(rootView.booking_time_until)
            rootView.booking_time_to_frame.setBackgroundResource(R.drawable.background_rectangle_black)
        }
        rootView.booking_btn_book.setBackgroundResource(R.drawable.ic_button)
            rootView.booking_btn_book.setOnClickListener {
                val bundle = this.arguments
                val fieldId = bundle!!.getInt("DATA_ID")
                name = rootView.booking_edit_text_name.text.toString()
                number = rootView.booking_edit_text_number.text.toString()
                val isValid = inputCheck(name, number, desiredDate, desiredTimeUntil, desiredTimeFrom)
                val fieldBooking = FieldBookingCreate(fieldId, desiredDate, desiredTimeFrom, desiredTimeUntil)
                if (isValid){
                    fieldBooking?.let { it1 -> book(it1) }
                }
                else{
                    Toast.makeText(rootView.context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
        }
            return rootView
        }

    private fun showAuthDialog() {
        val alertDialog = LayoutInflater.from(context).inflate(R.layout.custom_dialog_4, null)
        val builder = AlertDialog.Builder(context)
                .setView(alertDialog)
                .show()
        alertDialog.custom_dialog_4_ok_button.setOnClickListener {
            builder.dismiss()
        }
    }

    private fun book(fieldBookingCreate: FieldBookingCreate) {
        val token = sessionManager.fetchAuthToken()
        val call = ApiClient().getApiService().bookTheField("Bearer $token",fieldBookingCreate)

        call.enqueue(object : retrofit2.Callback<FieldBookingResponse> {
            override fun onResponse(call: Call<FieldBookingResponse>, response: Response<FieldBookingResponse>) {
                if (response.isSuccessful) {
                    showDialog()
                    response.body()?.let { bookingIds.add(it) }
                }
                else if(response.code() == 400){
                    when(response.raw().headersContentLength().toString()){
                        "66" -> Toast.makeText(requireContext(), "Указанное время уже прошло", Toast.LENGTH_SHORT).show()
                        "109" -> alreadyBookedDialog()
                        else ->
                            Toast.makeText(requireContext(), "Невозможно совершить бронь на данное время", Toast.LENGTH_SHORT).show()
                }}

               // Log.e("response code", response.raw().code.toString())
                else if (response.code() == 403){
                    ApiClient().getApiService().refreshToken(RefreshToken("$token")).enqueue(object: Callback<RefreshToken>{
                        override fun onFailure(call: Call<RefreshToken>, t: Throwable) {
                            Log.e("failed to refresh", t.message.toString())
                        }
                        override fun onResponse(call: Call<RefreshToken>, response: Response<RefreshToken>) {
                            Log.e("response code", response?.raw()?.code.toString())
                            if (response.isSuccessful){
                                sessionManager.deleteOldToken()
                                sessionManager.saveAuthToken(response.body()!!.token)
                                Log.e("newToken: ", "${sessionManager.fetchAuthToken()}")
                                book(fieldBookingCreate)
                                Log.e("refresh", "${response.body()?.token}")
                            }
                        }
                    })
                }

            }

            override fun onFailure(call: Call<FieldBookingResponse>, t: Throwable) {
                Toast.makeText(context, "Произошла ошибка при бронировании поля, попробуйте еще раз", Toast.LENGTH_SHORT).show()
                Log.e("Error", t.message.toString())
            }

        })
    }

    private fun alreadyBookedDialog() {
        val alertDialog = LayoutInflater.from(context).inflate(R.layout.custom_dialog_3, null)
        val builder = AlertDialog.Builder(context)
                .setView(alertDialog)
                .show()
        alertDialog.custom_dialog_ok_button.setOnClickListener {
            builder.dismiss()
        }
    }

    private fun showDialog() {
        val alertDialog = LayoutInflater.from(context).inflate(R.layout.book_alert_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(alertDialog)
            .show()
        alertDialog.book_alert_btn_ok.setOnClickListener {
            builder.dismiss()
            navController.navigate(R.id.action_bookingFragment2_to_playgroundListFragment)
        }
    }

    private fun inputCheck(name: String, number: String,
                           desiredTimeFrom: String,
                           desiredTimeUntil: String,
                           desiredDate: String) : Boolean {
        return !(name.isEmpty()|| number.isEmpty()|| desiredTimeFrom.isEmpty() || desiredTimeUntil.isEmpty() || desiredDate.isEmpty())
    }


    private fun chooseTimeFrom(bookingTime: TextView) : String{
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.desired_time_from)
        builder.setSingleChoiceItems(desiredTimeArray,-1) { dialogInteface, i ->
            desiredTimeFrom = desiredTimeArray[i]
            bookingTime.text = desiredTimeFrom
            bookingTime.setTextColor(Color.parseColor("#000000"))
            Handler(Looper.getMainLooper()).postDelayed({
                dialogInteface.dismiss()
            }, 600)
        }
        val dialog = builder.create().show()
        return desiredTimeFrom.toString()

    }
    private fun chooseTimeUntil(bookingTime: TextView) : String{
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.desired_time_from)
        builder.setSingleChoiceItems(desiredTimeArray,-1) { dialogInteface, i ->
            desiredTimeUntil = desiredTimeArray[i]
            bookingTime.text = desiredTimeUntil
            bookingTime.setTextColor(Color.parseColor("#000000"))
            Handler(Looper.getMainLooper()).postDelayed({
                dialogInteface.dismiss()
            }, 600)
        }
        val dialog = builder.create().show()

        return desiredTimeUntil.toString()
    }



    private fun chooseDate() {
        val datePickerDialog = DatePickerDialog(requireContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
       desiredDate = "$year-${month.plus(1)}-$dayOfMonth"
        booking_date_tv.text = desiredDate
       booking_date_frame.setBackgroundResource(R.drawable.background_rectangle_black)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }
}
