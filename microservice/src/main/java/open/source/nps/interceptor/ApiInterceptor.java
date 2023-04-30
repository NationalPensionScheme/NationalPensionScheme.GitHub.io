package open.source.nps.interceptor;

import org.apache.log4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@RequiredArgsConstructor
public class ApiInterceptor implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

		String requestId = serverWebExchange.getRequest().getId();
		MDC.put("requestId", requestId);

		String apiUriPath = serverWebExchange.getRequest().getPath().value();

		log.info("request interceptor -> (requestId) {} (apiUriPath) {}", requestId, apiUriPath);

		long startTimeStamp = System.currentTimeMillis();
		long nanoFrameStart = System.nanoTime();

		log.info("identified -> (startTimeStamp) {} (nanosFrameStart) {}", startTimeStamp, nanoFrameStart);

		return webFilterChain.filter(serverWebExchange)
				.doFinally(signalType -> {

					log.info("response interceptor -> (requestId) {} (signalType) {} ", requestId, signalType);

					long endTimeStamp = System.currentTimeMillis();
					long nanoFrameEnd = System.nanoTime();

					log.info("identified -> (endTimeStamp) {} (nanoFrameEnd) {}", endTimeStamp, nanoFrameEnd);

					long milliTimeTurnAround = endTimeStamp - startTimeStamp;
					long nanoFrameTurnAround = nanoFrameEnd - nanoFrameStart;

					log.info("performance -> (milliTimeTurnAround) {} (nanoFrameTurnAround) {}", milliTimeTurnAround, nanoFrameTurnAround);
				});
	}

}
