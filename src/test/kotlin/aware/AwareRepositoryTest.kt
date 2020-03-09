package aware

import aware.database.TestDatabaseConfiguration
import aware.domain.Camera
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Application::class, TestDatabaseConfiguration::class])
@TestPropertySource("classpath:test.properties")
@Transactional
class AwareRepositoryTest {
    private val FIRST_CAMERA_NAME = "First"
    private val FIRST_CAMERA_LAT = 0.toFloat()
    private val FIRST_CAMERA_LONG = 1.toFloat()
    private val SECOND_CAMERA_NAME = "Second"
    private val SECOND_CAMERA_LAT = 0.000003.toFloat()

    @Autowired
    private lateinit var awareRepository: AwareRepository
    @Autowired
    private lateinit var parameterJdbcTemplate: NamedParameterJdbcTemplate

    @Test
    fun getNearbyCameras_givenLocation_expectNoCameras() {
        val firstCamera = Camera(FIRST_CAMERA_NAME, FIRST_CAMERA_LAT, FIRST_CAMERA_LONG)
        awareRepository.insertCamera(firstCamera)
        val latitude = 1.toFloat()
        val longitude = 1.toFloat()

        val cameras = awareRepository.getNearbyCameras(latitude, longitude)

        Assert.assertTrue(cameras.isEmpty())
    }

    @Test
    fun getNearbyCameras_givenLocation_expectCamera() {
        val firstCamera = Camera(FIRST_CAMERA_NAME, FIRST_CAMERA_LAT, FIRST_CAMERA_LONG)
        awareRepository.insertCamera(firstCamera)
        val latitude = 0.000005.toFloat()

        val cameras = awareRepository.getNearbyCameras(latitude, FIRST_CAMERA_LONG)

        Assert.assertEquals(firstCamera.name, cameras[0].name)
        Assert.assertEquals(firstCamera.latitude, cameras[0].latitude)
        Assert.assertEquals(firstCamera.longitude, cameras[0].longitude)
    }

    @Test
    fun getNearbyCameras_givenLocation_expectCameras() {
        val firstCamera = Camera(FIRST_CAMERA_NAME, FIRST_CAMERA_LAT, FIRST_CAMERA_LONG)
        val secondCamera = Camera(SECOND_CAMERA_NAME, SECOND_CAMERA_LAT, FIRST_CAMERA_LONG)
        awareRepository.insertCamera(firstCamera)
        awareRepository.insertCamera(secondCamera)
        val latitude = 0.000005.toFloat()
        val longitude = 1.toFloat()

        val cameras = awareRepository.getNearbyCameras(latitude, longitude)

        Assert.assertEquals(firstCamera.name, cameras[0].name)
        Assert.assertEquals(firstCamera.latitude, cameras[0].latitude)
        Assert.assertEquals(firstCamera.longitude, cameras[0].longitude)
        Assert.assertEquals(secondCamera.name, cameras[1].name)
        Assert.assertEquals(secondCamera.latitude, cameras[1].latitude)
        Assert.assertEquals(secondCamera.longitude, cameras[1].longitude)
    }
}