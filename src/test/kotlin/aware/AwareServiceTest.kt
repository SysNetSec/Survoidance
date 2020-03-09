package aware

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class AwareServiceTest {
    private val LATITUDE = 0.toFloat()
    private val LONGITUDE = 1.toFloat()
    private lateinit var awareService: AwareService

    @Mock
    private lateinit var awareRepository: AwareRepository

    @Before
    fun setUp() {
        awareService = AwareService(awareRepository)
    }

    @Test
    fun getNearbyCameras_givenLocation_expectValuesPassedToRepository() {
        awareService.getNearbyCameras(LATITUDE, LONGITUDE)

        verify(awareRepository).getNearbyCameras(LATITUDE, LONGITUDE)
    }
}