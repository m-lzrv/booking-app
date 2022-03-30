package com.booking.bookingapp.api

import com.booking.bookingapp.api.CustomHttpErrorCodes.DEVICE_WAS_ALREADY_BOOKED_CODE
import com.booking.bookingapp.api.CustomHttpErrorCodes.RELEASE_BOOKING_CODE
import com.booking.bookingapp.exceptions.DeviceWasAlreadyBookedException
import com.booking.bookingapp.exceptions.ReleaseDeviceBookingException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(DeviceWasAlreadyBookedException::class)
    fun deviceWasBookedHandler(err: DeviceWasAlreadyBookedException): ResponseEntity<Any> {
        return ResponseEntity.status(DEVICE_WAS_ALREADY_BOOKED_CODE)
            .body("${err.modelName} was already booked!")
    }

    @ExceptionHandler(ReleaseDeviceBookingException::class)
    fun releaseDeviceBookingHandler(err: ReleaseDeviceBookingException): ResponseEntity<Any> {
        return ResponseEntity.status(RELEASE_BOOKING_CODE)
            .body("modelName=${err.modelName} bookedBy=${err.bookedBy}cannot be released!")
    }
}