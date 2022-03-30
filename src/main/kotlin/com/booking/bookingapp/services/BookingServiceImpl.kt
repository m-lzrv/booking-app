package com.booking.bookingapp.services

import com.booking.bookingapp.db.entities.DeviceBookingEntity
import com.booking.bookingapp.dto.ExecutionResult
import com.booking.bookingapp.db.entities.ModelName
import com.booking.bookingapp.db.repositories.DeviceBookingRepository
import com.booking.bookingapp.dto.PhoneBookingRequest
import com.booking.bookingapp.dto.PhoneBookingResponse
import com.booking.bookingapp.dto.ReleasePhoneBookingRequest
import com.booking.bookingapp.exceptions.ReleaseDeviceBookingException
import com.booking.bookingapp.exceptions.DeviceWasAlreadyBookedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Service
class BookingServiceImpl(
    private val deviceBookingRepository: DeviceBookingRepository,
    private val deviceBookingMapper: DeviceBookingMapper,
    private val applicationContext: ApplicationContext
) : BookingService<PhoneBookingRequest, ReleasePhoneBookingRequest, PhoneBookingResponse> {

    lateinit var self: BookingServiceImpl

    @PostConstruct
    fun after(){
        val bookingService = applicationContext.getBean(BookingService::class.java)
        self = bookingService as BookingServiceImpl
    }


    override fun getAllBookings(): ExecutionResult<List<PhoneBookingResponse>> {
        return ExecutionResult(result = deviceBookingRepository.findAll().map {
            deviceBookingMapper.map(it)
        })
    }

    override fun book(bookingDto: PhoneBookingRequest): ExecutionResult<PhoneBookingResponse> {
        val tryToBookDevice = tryToBookDevice(bookingDto)
        return ExecutionResult(
            result = deviceBookingMapper.map(tryToBookDevice)
        )


    }

    private fun getLastBookedByThisUser(bookingDto: PhoneBookingRequest): DeviceBookingEntity {
        val result = deviceBookingRepository.findAllByModelNameAndBookedByOrderByLastBookedDateTimeDesc(
            bookingDto.modelName, bookingDto.bookedBy
        ).minByOrNull {
            it.lastBookedDateTime ?: OUTDATED_LAST_UPDATE_DATE_TIME
        }!!
        return result
    }

    fun tryToBookDevice(bookingDto: PhoneBookingRequest): DeviceBookingEntity {
        val booking = self!!.bookDevice(bookingDto)

        if (booking.first) throw DeviceWasAlreadyBookedException(bookingDto.modelName)

        return deviceBookingRepository.findByIdOrNull(booking.second)!!
    }

    @Transactional
    fun bookDevice(bookingDto: PhoneBookingRequest): Pair<Boolean, UUID> {

        val availableDeviceList =
            deviceBookingRepository.findAllByModelNameAndIsAvailable(bookingDto.modelName, true)
        if (availableDeviceList.isEmpty()) throw  DeviceWasAlreadyBookedException(bookingDto.modelName)

        val availableDevice = availableDeviceList.first()

        val bookingDateTIme = ZonedDateTime.now()

        val failedWhileBooking = isFailedWhileBooking(
            bookingDto,
            bookingDateTIme,
            availableDevice
        )
        return failedWhileBooking to availableDevice.id

    }

    private fun isFailedWhileBooking(
        bookingDto: PhoneBookingRequest,
        bookingDateTIme: ZonedDateTime,
        availableDevice: DeviceBookingEntity
    ) = (deviceBookingRepository.bookDevice(
        bookingDto.bookedBy, bookingDateTIme, availableDevice.id
    ) < 1)


    @Transactional
    override fun releaseBooking(dto: ReleasePhoneBookingRequest): ExecutionResult<PhoneBookingResponse> {

        val bookingEntities = deviceBookingRepository.findAllByModelName(dto.modelName)
            .ifEmpty {
                throw ReleaseDeviceBookingException(dto.modelName, dto.bookedBy)
            }

        val bookedEntity = (bookingEntities.firstOrNull { it.bookedBy == dto.bookedBy && !it.isAvailable }
            ?: throw ReleaseDeviceBookingException(dto.modelName, dto.bookedBy))

        tryReleaseBooking(bookedEntity, dto)

        val bookingEntity = deviceBookingRepository.findAllByModelNameAndIsAvailable(dto.modelName, true)
            .ifEmpty { throw ReleaseDeviceBookingException(dto.modelName, dto.bookedBy) }
            .first()

        return ExecutionResult(deviceBookingMapper.map(bookingEntity))
    }

    private fun tryReleaseBooking(
        bookedEntity: DeviceBookingEntity,
        dto: ReleasePhoneBookingRequest
    ) {
        if ((deviceBookingRepository.releaseDevice(bookedEntity.id) < 1)
        ) throw ReleaseDeviceBookingException(dto.modelName, dto.bookedBy)
    }

    override fun getBookingInfo(modelName: ModelName): ExecutionResult<List<PhoneBookingResponse>> {
        return ExecutionResult(deviceBookingRepository.findAllByModelName(modelName)
            .map { deviceBookingMapper.map(it) })
    }

    companion object {
        private val OUTDATED_LAST_UPDATE_DATE_TIME = ZonedDateTime.now().minusYears(50)
    }
}