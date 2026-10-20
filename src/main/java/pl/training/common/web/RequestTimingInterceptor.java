package pl.training.common.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;
import java.util.logging.Logger;

public class RequestTimingInterceptor implements HandlerInterceptor {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";

    private static final Logger LOGGER = Logger.getLogger(RequestTimingInterceptor.class.getName());
    private static final String START_TIME_ATTRIBUTE = RequestTimingInterceptor.class.getName() + ".startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.nanoTime());
        response.setHeader(REQUEST_ID_HEADER, UUID.randomUUID().toString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        var startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        var durationInMs = (System.nanoTime() - startTime) / 1_000_000;
        LOGGER.info("%s %s -> %d in %d ms [%s]".formatted(request.getMethod(), request.getRequestURI(),
                response.getStatus(), durationInMs, response.getHeader(REQUEST_ID_HEADER)));
    }

}
