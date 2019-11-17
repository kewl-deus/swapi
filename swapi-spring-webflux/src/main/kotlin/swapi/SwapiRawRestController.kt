package swapi

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import swapi.util.TimerClock
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/swapi/raw")
class SwapiRawRestController {

    private val LOG = LoggerFactory.getLogger(SwapiRawRestController::class.qualifiedName)

    @GetMapping("/{resourceName}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getRawData(@PathVariable resourceName: String): Flux<String> {
        LOG.trace("GET /raw/$resourceName")
        val timer = TimerClock().start()

        val cls = SwapiRawRestController::class.java
        val resUrl = cls.getResource("/swapi/data/$resourceName.json")
        val resPath = Paths.get(resUrl.toURI())

        //val channel = Files.newByteChannel(resPath)

        LOG.trace("GET /raw/$resourceName took ${timer.stop().durationSeconds()}")
        return fluxify(resPath)
        //return fluxify(Files.newByteChannel(resPath))
    }

    private fun fluxify(sourcePath: Path): Flux<String> {
        val stream = Files.lines(sourcePath)
        return Flux.fromStream(stream)
    }

    //FIXME not working yet
    private fun fluxify(channel: ReadableByteChannel): Flux<String> {
        return Flux.create<String> { emitter ->
            val byteBuffer = ByteBuffer.allocate(512)
            var readCount: Int
            do {
                readCount = channel.read(byteBuffer)
                if (readCount > 0){
                    val str = byteBuffer.asCharBuffer().toString()
                    LOG.debug("Emitting $readCount bytes: $str")
                    emitter.next(str)
                }
            } while (readCount != -1)
            emitter.complete()
        }
    }

    /*
    fun read(cbuf: CharArray, off: Int, len: Int): Int {
        val byteBuffer = ByteBuffer.allocate(len)
        var readCount = 0
        do {
            readCount = channel.read(byteBuffer)
        } while (readCount != -1 && readCount < len)
        byteBuffer.asCharBuffer().get(cbuf, off, len)
        return readCount
    }
     */
}
