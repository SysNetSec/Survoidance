package aware

import aware.domain.Camera
import aware.domain.GetCamerasRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/aware")
class AwareController(val awareService: AwareService) {
    @GetMapping
    fun getNearbyCameras(@RequestBody request: GetCamerasRequest): List<Camera> {
        return awareService.getNearbyCameras(request.latitude, request.longitude)
    }
}