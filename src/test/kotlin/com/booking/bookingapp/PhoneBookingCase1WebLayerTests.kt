package com.booking.bookingapp

import com.booking.bookingapp.db.entities.ModelName
import com.booking.bookingapp.dto.PhoneBookingRequest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class PhoneBookingCase1WebLayerTests : AbstractTestCase() {


    @ParameterizedTest
    @MethodSource("bookAllPhones")
    fun `book all devices by one user`(modelName: ModelName, user: String) {

        val request = mapper.writeValueAsBytes(PhoneBookingRequest(modelName, user))

        mockMvc.post("/booking/phones/") {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.result.bookedBy") {
                value(user)
            }
            jsonPath("$.result.isAvailable") {
                value("false")
            }
            jsonPath("$.result.modelName") {
                value(modelName.toString())
            }
            jsonPath("$.result.lastBookedDateTime") {
                exists()
            }
        }
    }


    companion object {
        private const val USER_1 = "user-1"

        @JvmStatic
        fun bookAllPhones(): List<Arguments> = ModelName.values().map { of(it, USER_1) }


    }
}
