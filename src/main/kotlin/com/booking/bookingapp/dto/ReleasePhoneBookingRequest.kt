package com.booking.bookingapp.dto

import com.booking.bookingapp.db.entities.ModelName

data class ReleasePhoneBookingRequest(
    var modelName: ModelName,
    var bookedBy: String
)