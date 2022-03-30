package com.booking.bookingapp

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class PhoneBookingCase5WebLayerTests : AbstractTestCase() {

    @ParameterizedTest
    @MethodSource("factory")
    fun `get all bookings`(
        asserter: (input: ResultActionsDsl) -> Unit
    ) {

        val resultActionsDsl = mockMvc.get("/booking/phones/all") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }

        asserter(resultActionsDsl)

    }


    companion object {

        @JvmStatic
        fun factory(): List<Arguments> =
            listOf(of(
                { input: ResultActionsDsl ->
                    input.andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                        jsonPath("$.result") {
                            exists()
                            isNotEmpty()
                            isArray()
                        }
                    }
                }
            ))

    }

}
