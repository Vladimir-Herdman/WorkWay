package com.zybooks.workway.model

import java.time.LocalDateTime

class Message {
    var messageID: String? = null
    var senderID: String? = null
    var recipientID: String? = null
    var content: String? = null
    var timestamp: LocalDateTime? = null
}