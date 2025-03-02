package com.zybooks.workway.model

import java.time.LocalDateTime

class Meeting {
    var meetingID: String? = null
    var title: String? = null
    var description: String? = null
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null
    var organizerID: String? = null
}