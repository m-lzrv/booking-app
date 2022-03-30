package com.booking.bookingapp.dto

import com.booking.bookingapp.db.entities.ModelName
import java.time.ZonedDateTime

data class PhoneBookingResponse(
    val modelName: ModelName,
    val bookedBy: String? = null,
    val isAvailable: Boolean,
    val lastBookedDateTime: ZonedDateTime? = null
)