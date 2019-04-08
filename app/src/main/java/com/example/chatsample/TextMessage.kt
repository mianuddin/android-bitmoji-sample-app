package com.example.chatsample

data class TextMessage(val content: String, override val received: Boolean = false): Message