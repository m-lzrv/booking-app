package com.booking.bookingapp.db.entities

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "device_booking")
data class DeviceBookingEntity(
    @Id @GeneratedValue
    var id: UUID,
    @Column
    @Enumerated(value = EnumType.STRING)
    val modelName: ModelName,
    @Column
    var isAvailable: Boolean = true,
    @Column
    var lastBookedDateTime: ZonedDateTime? = null,
    @Column
    var bookedBy: String? = null,
    @Column
    var technology: String,
    @Column
    var bands2g: String,
    @Column
    var bands3g: String,
    @Column
    var bands4g: String,
    @Version
    var version: Int = 0
)