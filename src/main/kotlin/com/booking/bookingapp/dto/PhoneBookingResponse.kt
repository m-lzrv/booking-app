package com.booking.bookingapp.dto

import com.booking.bookingapp.db.entities.ModelName
import java.time.ZonedDateTime

data class PhoneBookingResponse(
    val modelName: ModelName,
    val bookedBy: String? = null,
    val isAvailable: Boolean,
    val lastBookedDateTime: ZonedDateTime? = null,
    //FIXME  Fonoapi  is not working anymore
    val technology: String = "In progress",
    val bands2g: String =    "850/900/1800 MHz",
    val bands3g: String =    "900/1800/2100 MHz",
    val bands4g: String =    "B1 B3 B5 B7 B8 B20",
)