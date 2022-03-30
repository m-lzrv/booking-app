package com.booking.bookingapp.api

import com.booking.bookingapp.exceptions.DeviceWasAlreadyBookedException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(DeviceWasAlreadyBookedException::class)
    fun deviceWasBookedHandler(err: DeviceWasAlreadyBookedException): ResponseEntity<Any> {
        return ResponseEntity.badRequest()
            .body("${err.modelName} was already booked!")
    }


}