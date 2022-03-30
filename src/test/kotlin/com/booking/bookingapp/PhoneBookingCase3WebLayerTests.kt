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
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class PhoneBookingCase3WebLayerTests : AbstractTestCase() {

    @ParameterizedTest
    @MethodSource("factory")
    fun `booking two free devices with the same modelName`(
        modelName: ModelName, user: String, asserter: (input: ResultActionsDsl) -> Unit
    ) {
        val request = mapper.writeValueAsBytes(PhoneBookingRequest(modelName, user))

        val andExpect: ResultActionsDsl = mockMvc.post("/booking/phones/") {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }

        asserter(andExpect)
    }


    companion object {
        private const val USER_1 = "user-1"
        private const val USER_2 = "user-2"

        @JvmStatic
        fun factory(): List<Arguments> =
            listOf(of(ModelName.Samsung_Galaxy_S8, USER_1, { input: ResultActionsDsl ->
                input.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.result.bookedBy") {
                        value(USER_1)
                    }
                    jsonPath("$.result.isAvailable") {
                        value("false")
                    }
                    jsonPath("$.result.modelName") {
                        value(ModelName.Samsung_Galaxy_S8.toString())
                    }
                    jsonPath("$.result.lastBookedDateTime") {
                        exists()
                    }
                }
            }), of(ModelName.Samsung_Galaxy_S8, USER_2, { input: ResultActionsDsl ->
                input.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.result.bookedBy") {
                        value(USER_2)
                    }
                    jsonPath("$.result.isAvailable") {
                        value("false")
                    }
                    jsonPath("$.result.modelName") {
                        value(ModelName.Samsung_Galaxy_S8.toString())
                    }
                    jsonPath("$.result.lastBookedDateTime") {
                        exists()
                    }
                }
            })
            )

    }

}
