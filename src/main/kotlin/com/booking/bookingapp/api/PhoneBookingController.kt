package com.booking.bookingapp.api

import com.booking.bookingapp.db.entities.ModelName
import com.booking.bookingapp.dto.PhoneBookingRequest
import com.booking.bookingapp.dto.PhoneBookingResponse
import com.booking.bookingapp.dto.ReleasePhoneBookingRequest
import com.booking.bookingapp.services.BookingService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import mu.KotlinLogging
import org.springframework.web.bind.annotation.*

@OpenAPIDefinition(
    info = Info(
        title = "Booking API",
        version = "0.1",
        description = "This is a service that allows a phone to be booked / returned"
    )
)
@RestController
@RequestMapping("/booking/phones/")
class PhoneBookingController(
    val bookingService: BookingService<PhoneBookingRequest, ReleasePhoneBookingRequest, PhoneBookingResponse>
) {
    @PostMapping
    fun bookPhone(@RequestBody phoneBookingRequest: PhoneBookingRequest) = bookingService.book(phoneBookingRequest)

    @PatchMapping
    fun returnPhone(@RequestBody request: ReleasePhoneBookingRequest) = bookingService.releaseBooking(request)

    @GetMapping("/{modelName}")
    fun getPhoneInfo(@PathVariable modelName: ModelName) = bookingService.getBookingInfo(modelName)

    @GetMapping("/all")
    fun getAllPhonesInfo() = bookingService.getAllBookings()


    companion object {
        private val log = KotlinLogging.logger {}

    }

}