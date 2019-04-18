package com.example.chatsample

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.chatsample.message.ImageMessage
import com.example.chatsample.message.MessageAdapter
import com.example.chatsample.message.TextMessage
import com.snapchat.kit.sdk.bitmoji.OnBitmojiSelectedListener
import com.snapchat.kit.sdk.bitmoji.ui.BitmojiIconFragment
import com.snapchat.kit.sdk.bitmoji.ui.BitmojiFragment
import kotlin.random.Random


class MainActivity : AppCompatActivity() /* Add OnBitmojiSelectedListener here! */ {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var messageInput: EditText
    private lateinit var bitmojiIcon: FrameLayout
    private lateinit var friendmojiToggle: ImageView
    private lateinit var search: ImageView
    private var stickerPickerVisible = false
    private var friendmojiSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Setup Recycler View
        viewManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter()
        messageAdapter.setHasStableIds(true)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = messageAdapter
        }

        // Setup message input
        messageInput = findViewById(R.id.message_input)

        messageInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text = messageInput.text.toString()
                if (!TextUtils.isEmpty(text)) {
                    messageAdapter.addMessage(TextMessage(text))
                    recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
                    generateResponse()
                    messageInput.setText("")
                    messageInput.requestFocus()
                }
            }

            true
        }

        messageInput.setOnFocusChangeListener { _, b ->
            if (b && stickerPickerVisible) toggleStickerPickerVisibility()
        }

        messageInput.requestFocus()

        // Setup Bitmoji icon

        // Add your BitmojiIconFragment() here!

        bitmojiIcon = findViewById(R.id.bitmoji_icon)

        bitmojiIcon.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (currentFocus == messageInput) {
                messageInput.clearFocus()
                toggleStickerPickerVisibility()
            }
        }

        // Setup Bitmoji sticker picker

        // Add your BitmojiFragment() here!

        // Setup search
        search = findViewById(R.id.search)

        // Add your search snippet here!

        // Setup Friendmoji toggle
        friendmojiToggle = findViewById(R.id.friendmoji)

        // Add your friendmoji snippet here!
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_info -> {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Override onBitmojiSelected here!

    private fun generateResponse() {
        Handler().postDelayed({
            val responses = "Cool!;Really?;k;lol;wow;Ok;Aww;rofl".split(";")
            messageAdapter.addMessage(TextMessage(responses[Random.nextInt(0, responses.size)], true))
            recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
        }, (Random.nextInt(0, 5) * 500).toLong())
    }

    private fun toggleStickerPickerVisibility() {
        val stickerPickerView = findViewById<FrameLayout>(R.id.sticker_picker)
        stickerPickerView.visibility = if (stickerPickerVisible) View.GONE else View.VISIBLE
        stickerPickerVisible = !stickerPickerVisible
    }
}
