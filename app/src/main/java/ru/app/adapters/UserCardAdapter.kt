package ru.app.adapters

import android.annotation.SuppressLint
import android.widget.Button
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.book_item.view.*
import retrofit2.Call
import retrofit2.Response
import ru.app.R
import ru.app.models.booking.BookingItem
import ru.app.models.booking.FieldBookingResponse
import ru.app.models.fields.FieldListItem
import ru.app.network.ApiService
import ru.app.network.RetrofitInstance

class UserCardAdapter(
        val context: Context, var bookingItems: List<BookingItem>, var listener: OnItemClickListener)
    : RecyclerView.Adapter<UserCardAdapter.UserCardHolder>(){

    interface OnItemClickListener{
        fun onItemCancel(position: Int, id:Int, bookingItem: BookingItem, v :View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.book_item, parent, false)

        return UserCardHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserCardHolder, position: Int) {
        val currentItem = bookingItems[position]
        holder.name.text = currentItem.field.name
        holder.bookingDate.text = currentItem.booking_date
        var start  = currentItem.timeStart.subSequence(0,5)

        var end = currentItem.timeEnd.subSequence(0,5)
        holder.bookingTime.text = "$start-$end"
        if (currentItem.is_finished){
            holder.cancelBooking.text = "Оставить отзыв"
        }
        holder.cancelBooking.setOnClickListener {
            if (listener != null){
                if (position != RecyclerView.NO_POSITION){
                    listener.onItemCancel(position, currentItem.id, currentItem, holder.itemView.rootView)
                }
            }
        }
        Glide.with(context)
                .load(currentItem.image)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .into(holder.image)
    }

    override fun getItemCount() = bookingItems.size

    //**********************************************************************************************

    inner class UserCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.book_list_playground_name
        val image: ImageView = itemView.book_list_image_view
        val bookingDate: TextView = itemView.book_list_date
        val bookingTime: TextView = itemView.book_list_time
        val cancelBooking: Button = itemView.book_list_cancel_button

    }

}