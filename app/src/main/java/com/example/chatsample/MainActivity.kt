package com.example.chatsample

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.snapchat.kit.sdk.bitmoji.OnBitmojiSelectedListener
import com.snapchat.kit.sdk.bitmoji.ui.BitmojiIconFragment
import com.snapchat.kit.sdk.bitmoji.ui.BitmojiFragment



class MainActivity : AppCompatActivity(), OnBitmojiSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MessageAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var messageInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        viewManager = LinearLayoutManager(this)
        viewAdapter = MessageAdapter()
        viewAdapter.setHasStableIds(true)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        messageInput = findViewById(R.id.message_input)

        messageInput.requestFocus()

        messageInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text = messageInput.text.toString()
                if (!TextUtils.isEmpty(text)) {
                    viewAdapter.addMessage(TextMessage(text))
                    messageInput.setText("")
                    messageInput.requestFocus()
                }
                recyclerView.scrollToPosition(0)
            }

            true
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.bitmoji_icon, BitmojiIconFragment())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.sticker_picker, BitmojiFragment())
            .commit()
    }

    override fun onBitmojiSelected(imageUrl: String, previewDrawable: Drawable) {
        viewAdapter.addMessage(ImageMessage(previewDrawable))
        recyclerView.scrollToPosition(0)
    }

    fun extraOptions(v: View) {
        var sticker_picker = supportFragmentManager.findFragmentById(R.id.sticker_picker) as? BitmojiFragment
        sticker_picker?.setFriend("bostonbruin2")
        sticker_picker?.setSearchText("tired")
    }
}
