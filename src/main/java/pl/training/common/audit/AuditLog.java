package pl.training.common.audit;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class AuditLog {

    private static final Logger LOGGER = Logger.getLogger(AuditLog.class.getName());

    private final List<AuditEntry> entries = new ArrayList<>();

    public synchronized void add(final AuditEntry entry) {
        LOGGER.info("AUDIT %s %s %s".formatted(entry.action(), entry.outcome(), entry.details()));
        entries.add(entry);
    }

    public synchronized List<AuditEntry> getEntries() {
        return new ArrayList<>(entries);
    }

}
