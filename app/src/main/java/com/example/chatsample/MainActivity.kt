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

        supportFragmentManager.beginTransaction()
            .replace(R.id.bitmoji_icon, BitmojiIconFragment())
            .commit()

        findViewById<FrameLayout>(R.id.bitmoji_icon).setOnClickListener {
            if (currentFocus == messageInput) {
                messageInput.clearFocus()
                toggleStickerPickerVisibility()
            }
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.sticker_picker, BitmojiFragment.builder().withShowSearchBar(false).build() )
            .commit()

        findViewById<ImageView>(R.id.search).setOnClickListener {
            var stickerPicker = supportFragmentManager.findFragmentById(R.id.sticker_picker) as? BitmojiFragment
            val text = messageInput.text.toString()

            stickerPicker?.setSearchText(text)

            messageInput.clearFocus()

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (!stickerPickerVisible) {
                toggleStickerPickerVisibility()
            }
        }

        findViewById<ImageView>(R.id.friendmoji).setOnClickListener {
            var stickerPicker = supportFragmentManager.findFragmentById(R.id.sticker_picker) as? BitmojiFragment
            val text = messageInput.text.toString()
            if (!TextUtils.isEmpty(text)) {
                stickerPicker?.setFriend(text)

                messageInput.setText("")
                messageInput.clearFocus()

                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                if (!stickerPickerVisible) {
                    toggleStickerPickerVisibility()
                }
            }
        }
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

    override fun onBitmojiSelected(imageUrl: String, previewDrawable: Drawable) {
        messageAdapter.addMessage(ImageMessage(previewDrawable))
        recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
        generateResponse()
    }

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
