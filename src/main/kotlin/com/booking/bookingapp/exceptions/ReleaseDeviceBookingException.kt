package com.booking.bookingapp.exceptions

import com.booking.bookingapp.db.entities.ModelName

class ReleaseDeviceBookingException(val modelName: ModelName, val bookedBy: String) : RuntimeException() {

}
