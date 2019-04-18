package com.example.chatsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.snapchat.kit.sdk.bitmoji.networking.FetchAvatarUrlCallback
import com.snapchat.kit.sdk.Bitmoji
import com.snapchat.kit.sdk.SnapKit
import com.squareup.picasso.Picasso


class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Info"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.unlink).setOnClickListener {
            SnapKit.unlink(baseContext)
            finish()
            startActivity(intent)
        }

        Bitmoji.fetchAvatarUrl(baseContext, object : FetchAvatarUrlCallback {
            override fun onSuccess(avatarUrl: String?) {
                val avatarView = findViewById<ImageView>(R.id.avatar)
                Picasso.get().load(avatarUrl).into(avatarView)
                findViewById<Button>(R.id.unlink).visibility = View.VISIBLE
            }

            override fun onFailure(isNetworkError: Boolean, statusCode: Int) {
                findViewById<TextView>(R.id.avatarPrompt).visibility = View.VISIBLE
            }
        })
    }
}
