package de.kyle.powauditlog.repository;

import de.kyle.powauditlog.dto.PowAuditLogEntry;

import java.util.LinkedList;

public class InMemoryPowAuditLogRepository implements PowAuditLogRepository {
    private final LinkedList<PowAuditLogEntry> entries = new LinkedList<>();

    @Override
    public LinkedList<PowAuditLogEntry> load() {
        return entries;
    }

    @Override
    public void save(PowAuditLogEntry powAuditLogEntry) {
        entries.push(powAuditLogEntry);
    }
}
