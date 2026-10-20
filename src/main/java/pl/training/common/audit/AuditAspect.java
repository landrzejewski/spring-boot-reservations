package pl.training.common.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

import static pl.training.common.audit.AuditEntry.Outcome.FAILURE;
import static pl.training.common.audit.AuditEntry.Outcome.SUCCESS;

@Order(1)
@Aspect
@Component
public class AuditAspect {

    private final AuditLog auditLog;

    public AuditAspect(final AuditLog auditLog) {
        this.auditLog = auditLog;
    }

    @Pointcut("execution(public * pl.training.hotel.application..*(..))")
    void applicationLayer() {
    }

    @AfterReturning(value = "applicationLayer() && @annotation(audited)", returning = "result")
    public void onSuccess(final JoinPoint joinPoint, final Audited audited, final Object result) {
        var details = "args=%s result=%s".formatted(Arrays.toString(joinPoint.getArgs()), result);
        auditLog.add(new AuditEntry(Instant.now(), audited.value(), SUCCESS, details));
    }

    @AfterThrowing(value = "applicationLayer() && @annotation(audited)", throwing = "exception")
    public void onFailure(final JoinPoint joinPoint, final Audited audited, final RuntimeException exception) {
        var details = "args=%s exception=%s".formatted(Arrays.toString(joinPoint.getArgs()), exception.getClass().getSimpleName());
        auditLog.add(new AuditEntry(Instant.now(), audited.value(), FAILURE, details));
    }

}
