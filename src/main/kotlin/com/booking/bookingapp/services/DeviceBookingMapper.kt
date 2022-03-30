package com.booking.bookingapp.services

import com.booking.bookingapp.db.entities.DeviceBookingEntity
import com.booking.bookingapp.dto.PhoneBookingResponse
import org.springframework.stereotype.Component

@Component
class DeviceBookingMapper {

    fun map(bookingEntity: DeviceBookingEntity) =
        PhoneBookingResponse(
            bookingEntity.modelName,
            bookingEntity.bookedBy,
            bookingEntity.isAvailable,
            bookingEntity.lastBookedDateTime
        )

}