package com.example.chatsample.message

data class TextMessage(val content: String, override val received: Boolean = false):
    Message