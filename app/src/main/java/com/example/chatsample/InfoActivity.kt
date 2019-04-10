package com.example.chatsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.snapchat.kit.sdk.bitmoji.networking.FetchAvatarUrlCallback
import com.snapchat.kit.sdk.Bitmoji
import com.squareup.picasso.Picasso


class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Info"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Bitmoji.fetchAvatarUrl(baseContext, object : FetchAvatarUrlCallback {
            override fun onSuccess(avatarUrl: String?) {
                val avatarView = findViewById<ImageView>(R.id.avatar)
                Picasso.get().load(avatarUrl).into(avatarView);
            }

            override fun onFailure(isNetworkError: Boolean, statusCode: Int) {
                // do something
            }
        })
    }
}
