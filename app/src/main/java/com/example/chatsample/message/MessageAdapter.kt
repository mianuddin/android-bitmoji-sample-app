package com.example.chatsample.message

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.chatsample.R

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: MutableList<Message> = mutableListOf(
        TextMessage(
            "Hi there!",
            true
        )
    )

    class MessageViewHolder(val messageView: LinearLayout) : RecyclerView.ViewHolder(messageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view = R.layout.chat_received

        when (viewType) {
            0 -> view = R.layout.chat_received
            1 -> view = R.layout.chat_img_received
            2 -> view = R.layout.chat_sent
            3 -> view = R.layout.chat_img_sent
        }

        return MessageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(view, parent, false) as LinearLayout
        )
    }

    override fun getItemViewType(position: Int): Int {
        if (getMessage(position).received) {
            return if (getMessage(position) is TextMessage) 0 else 1
        }
        return if (getMessage(position) is TextMessage) 2 else 3
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = getMessage(position)

        if (currentMessage is TextMessage)
            holder.messageView.findViewById<TextView>(R.id.chat_bubble).text = currentMessage.content
        if (currentMessage is ImageMessage)
            holder.messageView.findViewById<ImageView>(R.id.chat_image).setImageDrawable(currentMessage.content)
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }


    private fun getMessage(position: Int): Message {
        return messages.get(position)
    }
}