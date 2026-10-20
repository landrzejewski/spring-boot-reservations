package pl.training.common.audit;

import java.time.Instant;

public record AuditEntry(Instant timestamp, String action, Outcome outcome, String details) {

    public enum Outcome {

        SUCCESS, FAILURE

    }

}
