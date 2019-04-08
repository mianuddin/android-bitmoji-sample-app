package com.example.chatsample

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import com.example.chatsample.message.ImageMessage
import com.example.chatsample.message.MessageAdapter
import com.example.chatsample.message.TextMessage
import com.snapchat.kit.sdk.bitmoji.OnBitmojiSelectedListener
import com.snapchat.kit.sdk.bitmoji.ui.BitmojiIconFragment
import com.snapchat.kit.sdk.bitmoji.ui.BitmojiFragment



class MainActivity : AppCompatActivity(), OnBitmojiSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var messageInput: EditText
    private var stickerPickerVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        viewManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter()
        messageAdapter.setHasStableIds(true)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = messageAdapter
        }

        messageInput = findViewById(R.id.message_input)

        messageInput.requestFocus()

        messageInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text = messageInput.text.toString()
                if (!TextUtils.isEmpty(text)) {
                    messageAdapter.addMessage(TextMessage(text))
                    messageInput.setText("")
                    messageInput.requestFocus()
                }
                recyclerView.scrollToPosition(0)
            }

            true
        }

        messageInput.setOnClickListener {
            if (stickerPickerVisible) toggleStickerPickerVisibility()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.bitmoji_icon, BitmojiIconFragment())
            .commit()

        findViewById<FrameLayout>(R.id.bitmoji_icon).setOnClickListener {
            if (currentFocus == messageInput) {
                toggleStickerPickerVisibility()
            }
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.sticker_picker, BitmojiFragment())
            .commit()
    }

    override fun onBitmojiSelected(imageUrl: String, previewDrawable: Drawable) {
        messageAdapter.addMessage(ImageMessage(previewDrawable))
        recyclerView.scrollToPosition(0)
    }

    private fun toggleStickerPickerVisibility() {
        val stickerPickerView = findViewById<FrameLayout>(R.id.sticker_picker)
        stickerPickerView.visibility = if (stickerPickerVisible) View.GONE else View.VISIBLE
        stickerPickerVisible = !stickerPickerVisible
    }
}
