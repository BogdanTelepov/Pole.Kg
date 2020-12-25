package ru.app.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.request_closed_item.view.*
import ru.app.R
import ru.app.models.booking.BookingItem
import ru.app.models.booking.FieldBookingResponse

class ClosedRequestsAdapter (
        val context: Context, var bookingItems: List<BookingItem>)
    : RecyclerView.Adapter<ClosedRequestsAdapter.ClosedRequestsHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosedRequestsHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.request_closed_item, parent, false)
        return ClosedRequestsHolder(itemView)
    }


    override fun getItemCount() = bookingItems.size



    inner class ClosedRequestsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.requests_user_name
        val field = itemView.requests_field_name
        val bookingDate: TextView = itemView.request_date
        val bookingTime: TextView = itemView.request_time
        val number = itemView.requests_phone_number


    }

    override fun onBindViewHolder(holder: ClosedRequestsAdapter.ClosedRequestsHolder, position: Int) {
        val currentItem = bookingItems[position]
        holder.name.text = currentItem.user.full_name
        holder.field.text = currentItem.field.name
        holder.bookingDate.text = currentItem.booking_date
        var start = currentItem.timeStart.subSequence(0,5)
        var end = currentItem.timeEnd.subSequence(0,5)
        holder.bookingTime.text = "$start-$end"
        holder.number.text = currentItem.user.phone_number

    }

}
