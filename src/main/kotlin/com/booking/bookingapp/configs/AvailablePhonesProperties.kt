package com.booking.bookingapp.configs

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "available")
class AvailablePhonesProperties(
    val phones: List<String> // ???? mb rm?
)