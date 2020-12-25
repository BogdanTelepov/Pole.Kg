package ru.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.playground_owner_card.view.*
import ru.app.R
import ru.app.models.booking.BookingItem

class OwnerCardAdapter(var context: Context,
    val listener: OnItemClickListener, var bookingItems: List<BookingItem>)
    : RecyclerView.Adapter<OwnerCardAdapter.PlaygroundOwnerCardHolder>(){

    interface OnItemClickListener{
        fun onItemCancel(position: Int, id: Int, bookingItem: BookingItem, v: View)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaygroundOwnerCardHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.playground_owner_card, parent, false)
        return PlaygroundOwnerCardHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaygroundOwnerCardHolder, position: Int) {
        val currentItem = bookingItems[position]
        holder.name.text = currentItem.field.name
        holder.bookingDate.text = currentItem.booking_date
        holder.status.text = currentItem.status
//        if (currentItem.is_finished){
//            holder.cancelBooking.text = "Оставить отзыв"
//        }
        var start  = currentItem.timeStart.subSequence(0,5)
        var end = currentItem.timeEnd.subSequence(0,5)
        holder.bookingTime.text = "$start-$end"
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

    inner class PlaygroundOwnerCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.owner_playground_name
        val image: ImageView = itemView.owner_playground_image_view
        val bookingDate: TextView = itemView.owner_book_date
        val bookingTime: TextView = itemView.owner_book_time
        val cancelBooking: Button = itemView.owner_cancel_booking
        val status = itemView.owner_status

    }

}