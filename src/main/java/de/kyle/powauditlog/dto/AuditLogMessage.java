package de.kyle.powauditlog.dto;

import java.time.LocalDateTime;

public record AuditLogMessage(String message, LocalDateTime date) {
}
