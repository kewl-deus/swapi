package swapi.filter

import ch.qos.logback.classic.ClassicConstants
import org.slf4j.MDC
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class MDCInsertingWebFilter : WebFilter {


    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        insertIntoMDC(exchange.request)
        return chain.filter(exchange).doAfterTerminate {
            clearMDC()
        }
    }

    private fun insertIntoMDC(request: ServerHttpRequest) {
        request.remoteAddress?.let {
            MDC.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, it.hostName)
        }

        putIfNotEmpty(ClassicConstants.REQUEST_REQUEST_URI, request.uri.path)
        putIfNotEmpty(ClassicConstants.REQUEST_REQUEST_URL, request.uri.toURL().toString())
        putIfNotEmpty(ClassicConstants.REQUEST_METHOD, request.methodValue)
        putIfNotEmpty(ClassicConstants.REQUEST_QUERY_STRING, request.queryParams.toString())

        request.headers.filter { h -> h.key == "User-Agent" }.let { h ->
            putIfNotEmpty(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY, h.values.joinToString(","))
        }

        request.headers.filter { h -> h.key == "X-Forwarded-For" }.let { h ->
            putIfNotEmpty(ClassicConstants.REQUEST_X_FORWARDED_FOR, h.values.joinToString(","))
        }
    }



    private fun clearMDC() {
        MDC.remove(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY)
        MDC.remove(ClassicConstants.REQUEST_REQUEST_URI)
        MDC.remove(ClassicConstants.REQUEST_QUERY_STRING)
        // removing possibly inexistent item is OK
        MDC.remove(ClassicConstants.REQUEST_REQUEST_URL)
        MDC.remove(ClassicConstants.REQUEST_METHOD)
        MDC.remove(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY)
        MDC.remove(ClassicConstants.REQUEST_X_FORWARDED_FOR)
    }


    private fun putIfNotEmpty(key: String, value: String){
        value.let {
            if (value.isNotBlank()) {
                MDC.put(key, value)
            }
        }
    }
}