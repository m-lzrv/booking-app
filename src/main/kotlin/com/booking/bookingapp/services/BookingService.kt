package com.booking.bookingapp.services

import com.booking.bookingapp.db.entities.ModelName
import com.booking.bookingapp.dto.ExecutionResult

interface BookingService<BOOKING_DTO, RELEASE_DTO, BOOKING_RESULT> {

    fun book(bookingDto: BOOKING_DTO): ExecutionResult<BOOKING_RESULT>

    fun releaseBooking(dto: RELEASE_DTO): ExecutionResult<BOOKING_RESULT>

    fun getBookingInfo(modelName: ModelName): ExecutionResult<List<BOOKING_RESULT>>

    fun getAllBookings(): ExecutionResult<List<BOOKING_RESULT>>

}
