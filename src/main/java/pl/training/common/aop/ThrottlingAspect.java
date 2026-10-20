package pl.training.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class ThrottlingAspect {

    private final Map<String, Deque<Instant>> invocations = new ConcurrentHashMap<>();

    @Around("@annotation(throttled)")
    public Object throttle(final ProceedingJoinPoint joinPoint, final Throttled throttled) throws Throwable {
        var methodName = joinPoint.getSignature().toLongString();
        var timestamps = invocations.computeIfAbsent(methodName, name -> new ArrayDeque<>());
        registerInvocation(timestamps, throttled, methodName);
        return joinPoint.proceed();
    }

    private void registerInvocation(final Deque<Instant> timestamps, final Throttled throttled, final String methodName) {
        var now = Instant.now();
        var windowStart = now.minus(Duration.ofSeconds(throttled.periodInSeconds()));
        synchronized (timestamps) {
            while (!timestamps.isEmpty() && timestamps.peekFirst().isBefore(windowStart)) {
                timestamps.pollFirst();
            }
            if (timestamps.size() >= throttled.permits()) {
                throw new ThrottlingException("Limit of %d call(s) per %d s exceeded for %s"
                        .formatted(throttled.permits(), throttled.periodInSeconds(), methodName));
            }
            timestamps.addLast(now);
        }
    }

}
