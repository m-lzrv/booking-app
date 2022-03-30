package com.booking.bookingapp.db.repositories

import com.booking.bookingapp.db.entities.DeviceBookingEntity
import com.booking.bookingapp.db.entities.ModelName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.*

@Repository
interface DeviceBookingRepository : JpaRepository<DeviceBookingEntity, UUID> {

    fun findAllByModelNameAndIsAvailable(modelName: ModelName, isAvailable: Boolean): List<DeviceBookingEntity>

    fun findAllByModelName(modelName: ModelName): List<DeviceBookingEntity>

    fun findAllByModelNameAndBookedByOrderByLastBookedDateTimeDesc(
        modelName: ModelName,
        bookedBy: String
    ): List<DeviceBookingEntity>

    @Modifying
    @Query(
        "update DeviceBookingEntity d " +
                "set d.isAvailable = false , d.bookedBy = ?1 , d.lastBookedDateTime = ?2 " +
                "where  d.id = ?3 and d.isAvailable = true"
    )
    fun bookDevice(bookedBy: String, bookingDateTIme: ZonedDateTime, id: UUID): Int

    @Modifying
    @Query(
        "update DeviceBookingEntity d " +
                "set d.isAvailable = true , d.bookedBy = null " +
                "where d.id = ?1"
    )
    fun releaseDevice(id: UUID): Int
}