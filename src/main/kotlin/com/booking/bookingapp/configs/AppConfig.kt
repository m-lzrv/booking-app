package com.booking.bookingapp.configs

import com.booking.bookingapp.db.repositories.DeviceBookingRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EnableJpaRepositories(basePackageClasses = [DeviceBookingRepository::class])
@EnableConfigurationProperties(AvailablePhonesProperties::class)
@EnableTransactionManagement
class AppConfig