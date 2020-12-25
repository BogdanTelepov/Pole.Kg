package ru.app.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import kotlinx.android.synthetic.main.request_new_item.view.*
import ru.app.R
import ru.app.models.booking.BookingItem
import ru.app.models.booking.FieldBookingResponse

class NewRequestsAdapter(
    val context: Context,
    var listener: OnItemClickListener, var bookingItems: List<BookingItem>)
    : RecyclerView.Adapter<NewRequestsAdapter.NewRequestsHolder>(){
    interface OnItemClickListener{
        fun onItemAccept(position: Int, id: Int, currentItem: BookingItem)
        fun onItemDeny(position: Int, id: Int, currentItem: BookingItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRequestsHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_new_item, parent, false)
        return NewRequestsHolder(itemView)
    }


    override fun getItemCount() = bookingItems.size



    //********************************************************
    // **************************************

    inner class NewRequestsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.requests_user_name
        val field = itemView.requests_field_name
        val bookingDate: TextView = itemView.request_date
        val bookingTime: TextView = itemView.request_time
        val number = itemView.requests_phone_number
        val accept: Button = itemView.requests_accept_button
        val deny = itemView.requests_deny_button


    }

    override fun onBindViewHolder(holder: NewRequestsAdapter.NewRequestsHolder, position: Int) {
            val currentItem = bookingItems[position]
            holder.name.text = currentItem.user.full_name
            holder.field.text = currentItem.field.name
            holder.number.text = currentItem.user.phone_number
            holder.bookingDate.text = currentItem.booking_date
            var start = currentItem.timeStart.subSequence(0,5)
            var end = currentItem.timeEnd.subSequence(0,5)
            holder.bookingTime.text = "$start-$end"
        holder.deny.setOnClickListener {
            if (listener != null){
                if (position != RecyclerView.NO_POSITION){
                    listener.onItemDeny(position, currentItem.id, currentItem)
                }
            }
        }
        holder.accept.setOnClickListener {
            if (listener != null){
                if (position != RecyclerView.NO_POSITION){
                    listener.onItemAccept(position, currentItem.id, currentItem)
                }
            }
        }



    }

    }
