package de.kyle.powauditlog.dto;

import java.util.List;

public record PowAuditLogEntry(
        List<AuditLogMessage> messages,
        String previousEntryHash,
        String currentEntryHash,
        int nonce,
        int difficulty
) {
}
