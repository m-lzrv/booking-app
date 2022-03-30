package com.booking.bookingapp

import com.booking.bookingapp.db.entities.ModelName
import com.booking.bookingapp.db.repositories.DeviceBookingRepository
import com.booking.bookingapp.dto.PhoneBookingRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class BookingApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var deviceBookingRepository: DeviceBookingRepository

    @BeforeAll
    fun releaseDevices() {
        val deviceBookingEntities = deviceBookingRepository.findAll()
            .map {
                it.apply {
                    bookedBy = null
                    lastBookedDateTime = null
                    isAvailable = true
                }
            }
        deviceBookingRepository.saveAllAndFlush(deviceBookingEntities)
    }

    @ParameterizedTest
    @MethodSource("factoryCase1")
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


    @ParameterizedTest
    @MethodSource("factoryCase2")
    fun `book device several times`(modelName: ModelName, user: String, asserter: (input: ResultActionsDsl) -> Unit) {

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

    @ParameterizedTest
    @MethodSource("factoryCase3")
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
        fun factoryCase1(): List<Arguments> = ModelName.values().map { of(it, USER_1) }

        @JvmStatic
        fun factoryCase2(): List<Arguments> = listOf(of(ModelName.Oneplus_9, USER_1, { input: ResultActionsDsl ->
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
                    value(ModelName.Oneplus_9.toString())
                }
                jsonPath("$.result.lastBookedDateTime") {
                    exists()
                }
            }
        }), of(ModelName.Oneplus_9, USER_2, { input: ResultActionsDsl ->
            input.andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }
        })
        )

        @JvmStatic
        fun factoryCase3(): List<Arguments> =
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
