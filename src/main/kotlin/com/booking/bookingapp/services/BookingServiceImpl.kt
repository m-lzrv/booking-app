package com.booking.bookingapp.services

import com.booking.bookingapp.db.entities.DeviceBookingEntity
import com.booking.bookingapp.db.entities.ModelName
import com.booking.bookingapp.db.repositories.DeviceBookingRepository
import com.booking.bookingapp.dto.ExecutionResult
import com.booking.bookingapp.dto.PhoneBookingRequest
import com.booking.bookingapp.dto.PhoneBookingResponse
import com.booking.bookingapp.dto.ReleasePhoneBookingRequest
import com.booking.bookingapp.exceptions.DeviceWasAlreadyBookedException
import com.booking.bookingapp.exceptions.ReleaseDeviceBookingException
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class BookingServiceImpl(
    private val deviceBookingRepository: DeviceBookingRepository, private val deviceBookingMapper: DeviceBookingMapper
) : BookingService<PhoneBookingRequest, ReleasePhoneBookingRequest, PhoneBookingResponse> {


    override fun getAllBookings(): ExecutionResult<List<PhoneBookingResponse>> {
        return ExecutionResult(deviceBookingRepository.findAll().map {
            deviceBookingMapper.map(it)
        })
    }

    @Transactional
    override fun book(bookingDto: PhoneBookingRequest): ExecutionResult<PhoneBookingResponse> {
        val bookingEntity = bookDevice(bookingDto)
        return ExecutionResult(
            result = deviceBookingMapper.map(bookingEntity)
        )


    }

    fun bookDevice(bookingDto: PhoneBookingRequest): DeviceBookingEntity {
        val availableDeviceList = deviceBookingRepository.findAllByModelNameAndIsAvailable(bookingDto.modelName, true)
        if (availableDeviceList.isEmpty()) throw DeviceWasAlreadyBookedException(bookingDto.modelName)

        val availableDevice = availableDeviceList.first()

        val bookingDateTIme = ZonedDateTime.now()

        availableDevice.bookedBy = bookingDto.bookedBy
        availableDevice.isAvailable = false
        availableDevice.lastBookedDateTime = bookingDateTIme

        return deviceBookingRepository.save(availableDevice)
    }


    @Transactional
    override fun releaseBooking(dto: ReleasePhoneBookingRequest): ExecutionResult<PhoneBookingResponse> {

        val bookingEntities = deviceBookingRepository.findAllByModelName(dto.modelName).ifEmpty {
            throw ReleaseDeviceBookingException(dto.modelName, dto.bookedBy)
        }

        val bookedEntity = (bookingEntities.firstOrNull { it.bookedBy == dto.bookedBy && !it.isAvailable }
            ?: throw ReleaseDeviceBookingException(dto.modelName, dto.bookedBy))

        tryReleaseBooking(bookedEntity, dto)

        val bookingEntity = deviceBookingRepository.findAllByModelNameAndIsAvailable(dto.modelName, true)
            .ifEmpty { throw ReleaseDeviceBookingException(dto.modelName, dto.bookedBy) }.first()

        return ExecutionResult(deviceBookingMapper.map(bookingEntity))
    }

    private fun tryReleaseBooking(
        bookedEntity: DeviceBookingEntity, dto: ReleasePhoneBookingRequest
    ) {
        if ((deviceBookingRepository.releaseDevice(bookedEntity.id) < 1)) throw ReleaseDeviceBookingException(
            dto.modelName, dto.bookedBy
        )
    }

    override fun getBookingInfo(modelName: ModelName): ExecutionResult<List<PhoneBookingResponse>> {
        return ExecutionResult(
            deviceBookingRepository.findAllByModelName(modelName).map { deviceBookingMapper.map(it) })
    }

}