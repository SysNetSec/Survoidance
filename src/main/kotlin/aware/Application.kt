package aware

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["aware"])
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
