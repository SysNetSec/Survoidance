package aware

import aware.domain.Camera
import org.springframework.stereotype.Service

@Service
class AwareService(val awareRepository: AwareRepository) {

    fun getNearbyCameras(latitude: Float, longitude: Float): List<Camera> {
        return awareRepository.getNearbyCameras(latitude, longitude)
    }
}