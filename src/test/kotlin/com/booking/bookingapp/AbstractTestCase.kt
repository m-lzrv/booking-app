package com.booking.bookingapp

import com.booking.bookingapp.db.repositories.DeviceBookingRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

abstract class AbstractTestCase {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var deviceBookingRepository: DeviceBookingRepository

    @AfterAll
    fun releaseDevices() {
        val deviceBookingEntities = deviceBookingRepository.findAll().map {
            it.apply {
                bookedBy = null
                lastBookedDateTime = null
                isAvailable = true
            }
        }
        deviceBookingRepository.saveAllAndFlush(deviceBookingEntities)
    }

}
