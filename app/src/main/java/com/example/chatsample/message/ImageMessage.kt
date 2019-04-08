package com.example.chatsample.message

import android.graphics.drawable.Drawable

data class ImageMessage(val content: Drawable, override val received: Boolean = false):
    Message