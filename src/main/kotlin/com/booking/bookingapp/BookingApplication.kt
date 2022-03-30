package com.booking.bookingapp

import com.booking.bookingapp.configs.AvailablePhonesProperties
import com.booking.bookingapp.db.repositories.DeviceBookingRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = [DeviceBookingRepository::class])
@EnableConfigurationProperties(AvailablePhonesProperties::class)
class BookingApplication

fun main(args: Array<String>) {
    runApplication<BookingApplication>(*args)
}
