package com.booking.bookingapp

import com.booking.bookingapp.db.entities.ModelName
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
class PhoneBookingCase6WebLayerTests : AbstractTestCase() {

    @ParameterizedTest
    @MethodSource("factory")
    fun `get device booking info by modelName`(
        modelName: ModelName, asserter: (input: ResultActionsDsl) -> Unit
    ) {

        val resultActionsDsl = mockMvc.get("/booking/phones/{modelName}", modelName) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }

        asserter(resultActionsDsl)

    }


    @ParameterizedTest
    @MethodSource("factory2")
    fun `get device booking info by modelName - with multiply values in DB`(
        modelName: ModelName, asserter: (input: ResultActionsDsl) -> Unit
    ) {

        val resultActionsDsl = mockMvc.get("/booking/phones/{modelName}", modelName) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }

        asserter(resultActionsDsl)

    }


    companion object {

        @JvmStatic
        fun factory(): List<Arguments> = ModelName.values().filter { it != ModelName.Samsung_Galaxy_S8 }.map {
            of(it, { input: ResultActionsDsl ->
                input.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.result") {
                        exists()
                        isNotEmpty()
                    }
                    jsonPath("$.result[*].isAvailable") {
                        value(true)
                    }
                    jsonPath("$.result[*].modelName") {
                        value(it.toString())
                    }
                }
            })
        }

        @JvmStatic
        fun factory2(): List<Arguments> = ModelName.values().filter { it == ModelName.Samsung_Galaxy_S8 }.map {
            of(it, { input: ResultActionsDsl ->
                input.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.result") {
                        exists()
                        isNotEmpty()
                    }
                }
            })
        }

    }

}
