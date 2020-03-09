package aware

import aware.database.TestDatabaseConfiguration
import aware.domain.Camera
import aware.domain.GetCamerasRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Application::class, TestDatabaseConfiguration::class])
@TestPropertySource("classpath:test.properties")
@WebAppConfiguration
@Transactional
class AwareControllerTest {
    private val URL = "/aware"

    private val FIRST_CAMERA_NAME = "First"
    private val FIRST_CAMERA_LAT = 0.toFloat()
    private val FIRST_CAMERA_LONG = 1.toFloat()

    @Autowired
    private lateinit var wac: WebApplicationContext

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var awareRepository: AwareRepository

    private lateinit var mockMvc: MockMvc

    @Before
    fun setUp() {
        mockMvc =  MockMvcBuilders.webAppContextSetup(wac).build()
    }

    @Test
    fun getNearbyCameras_givenThatCamerasExist_expectCamerasReturned() {
        val firstCamera = Camera(FIRST_CAMERA_NAME, FIRST_CAMERA_LAT, FIRST_CAMERA_LONG)
        awareRepository.insertCamera(firstCamera)

        mockMvc.perform(
                        MockMvcRequestBuilders.get(URL)
                                .content(objectMapper.writeValueAsString(
                                        GetCamerasRequest(FIRST_CAMERA_LAT, FIRST_CAMERA_LONG)))
                                .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(FIRST_CAMERA_NAME))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].latitude").value(FIRST_CAMERA_LAT))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].longitude").value(FIRST_CAMERA_LONG))
        }
}
