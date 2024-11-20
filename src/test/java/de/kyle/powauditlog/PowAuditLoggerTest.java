package de.kyle.powauditlog;

import de.kyle.powauditlog.dto.AuditLogMessage;
import de.kyle.powauditlog.dto.PowAuditLogEntry;
import de.kyle.powauditlog.repository.InMemoryPowAuditLogRepository;
import de.kyle.powauditlog.repository.PowAuditLogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PowAuditLoggerTest {
    @Test
    public void test_valid_batch_size() {
        PowAuditLogRepository auditLogRepository = new InMemoryPowAuditLogRepository();
        PowAuditLogger auditLogger = new PowAuditLogger(2, 2, auditLogRepository);
        auditLogger.addMessageToAuditLog("1");
        auditLogger.addMessageToAuditLog("2");
        Assertions.assertEquals(1, auditLogRepository.load().size());
        auditLogger.addMessageToAuditLog("3");
        auditLogger.addMessageToAuditLog("4");
        Assertions.assertEquals(2, auditLogRepository.load().size());
    }

    @Test
    public void test_manipulation_detection() {
        PowAuditLogRepository auditLogRepository = new InMemoryPowAuditLogRepository();
        PowAuditLogger auditLogger = new PowAuditLogger(2, 2, auditLogRepository);
        auditLogger.addMessageToAuditLog("1");
        auditLogger.addMessageToAuditLog("2");
        auditLogger.addMessageToAuditLog("3");
        auditLogger.addMessageToAuditLog("4");

        PowAuditLogEntry powAuditLogEntry = auditLogRepository.load().get(1);

        List<AuditLogMessage> messages = new ArrayList<>(powAuditLogEntry.messages());
        messages.add(new AuditLogMessage("Manipulation!", LocalDateTime.of(2020, 1, 1, 1, 1)));
        PowAuditLogEntry manipulatedPowAuditLogEntry = new PowAuditLogEntry(
                messages,
                powAuditLogEntry.previousEntryHash(),
                powAuditLogEntry.currentEntryHash(),
                powAuditLogEntry.nonce(),
                powAuditLogEntry.difficulty()
        );
        auditLogRepository.load().set(1, manipulatedPowAuditLogEntry);

        Assertions.assertFalse(auditLogger.isAuditLogValid());
        Assertions.assertTrue(auditLogger.getManipulatedPowAuditLogEntry().isPresent());

        PowAuditLogEntry detectedPowAuditLogEntry = auditLogger.getManipulatedPowAuditLogEntry().get();

        Assertions.assertEquals(manipulatedPowAuditLogEntry, detectedPowAuditLogEntry);
    }
}
