package de.kyle.powauditlog;


import de.kyle.powauditlog.dto.AuditLogMessage;
import de.kyle.powauditlog.dto.PowAuditLogEntry;
import de.kyle.powauditlog.dto.PowAuditLogNonceHashPair;
import de.kyle.powauditlog.repository.PowAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PowAuditLogger {
    private final int batchSize;
    private final int difficulty;
    private final List<AuditLogMessage> messages = new ArrayList<>();
    private final PowAuditLogRepository auditLogRepository;

    public void addMessageToAuditLog(String message) {
        if (messages.size() < batchSize - 1) {
            messages.add(new AuditLogMessage(message, LocalDateTime.now()));
            return;
        }
        messages.add(new AuditLogMessage(message, LocalDateTime.now()));

        LinkedList<PowAuditLogEntry> entries = new LinkedList<>(auditLogRepository.load());
        String previousEntryHash = "";
        if (!entries.isEmpty()) {
            PowAuditLogEntry previousEntry = entries.pop();
            previousEntryHash = previousEntry.currentEntryHash();
        }

        PowAuditLogNonceHashPair powAuditLogNonceHashPair = calculateNonceAndEntryHash(messages, previousEntryHash);
        PowAuditLogEntry powAuditLogEntry = new PowAuditLogEntry(List.copyOf(messages),
                previousEntryHash,
                powAuditLogNonceHashPair.hash(),
                powAuditLogNonceHashPair.nonce(),
                difficulty
        );

        auditLogRepository.save(powAuditLogEntry);
        messages.clear();
    }

    public boolean isAuditLogValid() {
        LinkedList<PowAuditLogEntry> entries = auditLogRepository.load();
        for (PowAuditLogEntry entry : entries) {
            String hashForCurrentEntryWithoutNonce = concatenateMessagesWithPreviousEntryHash(entry.messages(), entry.previousEntryHash());
            String currentEntryHash = DigestUtils.sha256Hex(hashForCurrentEntryWithoutNonce + entry.nonce());
            if (!currentEntryHash.startsWith("0".repeat(entry.difficulty()))) {
                return false;
            }
        }
        return true;
    }

    public Optional<PowAuditLogEntry> getManipulatedPowAuditLogEntry() {
        LinkedList<PowAuditLogEntry> entries = auditLogRepository.load();
        for (PowAuditLogEntry entry : entries) {
            String hashForCurrentEntryWithoutNonce = concatenateMessagesWithPreviousEntryHash(entry.messages(), entry.previousEntryHash());
            String currentEntryHash = DigestUtils.sha256Hex(hashForCurrentEntryWithoutNonce + entry.nonce());
            if (!currentEntryHash.startsWith("0".repeat(entry.difficulty()))) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    private String concatenateMessagesWithPreviousEntryHash(List<AuditLogMessage> messages, String previousEntryHash) {
        StringBuilder stringBuilder = new StringBuilder();
        messages.forEach(auditLogMessage -> {
            stringBuilder.append(auditLogMessage.message());
            stringBuilder.append(auditLogMessage.date().toString());
        });
        stringBuilder.append(previousEntryHash);
        return stringBuilder.toString();
    }


    private PowAuditLogNonceHashPair calculateNonceAndEntryHash(List<AuditLogMessage> messages, String previousEntryHash) {
        String hashForCurrentEntryWithoutNonce = concatenateMessagesWithPreviousEntryHash(messages, previousEntryHash);

        int nonce = 0;
        do {
            String currentEntryHash = DigestUtils.sha256Hex(hashForCurrentEntryWithoutNonce + nonce);
            if (currentEntryHash.startsWith("0".repeat(difficulty))) {
                return new PowAuditLogNonceHashPair(nonce, currentEntryHash);
            }
            nonce++;
        } while (nonce < Integer.MAX_VALUE);
        throw new RuntimeException("No nonce found for current entry");
    }
}
