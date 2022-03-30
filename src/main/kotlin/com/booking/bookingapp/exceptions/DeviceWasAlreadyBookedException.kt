package com.booking.bookingapp.exceptions

import com.booking.bookingapp.db.entities.ModelName

class DeviceWasAlreadyBookedException(val modelName: ModelName) : RuntimeException()
