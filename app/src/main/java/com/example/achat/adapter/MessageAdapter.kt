package com.example.achat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.achat.R
import com.example.achat.databinding.ReceiverItemLayoutBinding
import com.example.achat.databinding.SentItemLayoutBinding
import com.example.achat.model.MessageModel
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var context: Context, var list: ArrayList<MessageModel>): RecyclerView.Adapter<ViewHolder>() {

    var ITEM_SENT = 1
    var ITEM_RECEIVCE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return if (viewType == ITEM_SENT)
           SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout, parent, false))
        else
           ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_item_layout, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().uid == list[position].senderId)ITEM_SENT else ITEM_RECEIVCE
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val message = list[position]
        if (holder.itemViewType == ITEM_SENT){
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.userMessage.text = message.message
        } else{
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.userMessage.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ReceiverViewHolder(view: View): ViewHolder(view){
        var binding= SentItemLayoutBinding.bind(view)
    }

    inner class SentViewHolder(view: View): ViewHolder(view){
        var binding = ReceiverItemLayoutBinding.bind(view)
    }
}