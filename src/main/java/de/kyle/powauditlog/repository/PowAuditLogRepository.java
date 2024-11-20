package de.kyle.powauditlog.repository;

import de.kyle.powauditlog.dto.PowAuditLogEntry;

import java.util.LinkedList;

public interface PowAuditLogRepository {
    LinkedList<PowAuditLogEntry> load();

    void save(PowAuditLogEntry powAuditLogEntry);
}
