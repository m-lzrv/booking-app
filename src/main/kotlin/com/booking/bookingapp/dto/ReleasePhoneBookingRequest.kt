package com.booking.bookingapp.dto

import com.booking.bookingapp.db.entities.ModelName

data class ReleasePhoneBookingRequest(
        val modelName: ModelName,
        val bookedBy: String
        )