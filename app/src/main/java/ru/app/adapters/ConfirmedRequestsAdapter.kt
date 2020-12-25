package ru.app.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import kotlinx.android.synthetic.main.request_confirmed_item.view.*
import ru.app.R
import ru.app.models.booking.BookingItem
import ru.app.models.booking.FieldBookingResponse

class ConfirmedRequestsAdapter (
        val context: Context, var bookingItems: List<BookingItem>)
    : RecyclerView.Adapter<ConfirmedRequestsAdapter.ConfirmedRequestsHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmedRequestsHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.request_confirmed_item, parent, false)
        return ConfirmedRequestsHolder(itemView)
    }


    override fun getItemCount() = bookingItems.size


    //**********************************************************************************************

    inner class ConfirmedRequestsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.requests_user_name
        val field = itemView.requests_field_name
        val bookingDate: TextView = itemView.request_date
        val bookingTime: TextView = itemView.request_time
        val number = itemView.requests_phone_number


    }

    override fun onBindViewHolder(holder: ConfirmedRequestsAdapter.ConfirmedRequestsHolder, position: Int) {
        val currentItem = bookingItems[position]
        holder.name.text = currentItem.user.full_name
        holder.field.text = currentItem.field.name
        holder.bookingDate.text = currentItem.booking_date
        val start = currentItem.timeStart.subSequence(0,5)
        val end = currentItem.timeEnd.subSequence(0,5)
        holder.bookingTime.text = "$start-$end"
        holder.number.text = currentItem.user.phone_number

    }

}
