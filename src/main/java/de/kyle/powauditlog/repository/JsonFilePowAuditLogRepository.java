package de.kyle.powauditlog.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.kyle.powauditlog.dto.PowAuditLogEntry;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class JsonFilePowAuditLogRepository implements PowAuditLogRepository {
    private final LinkedList<PowAuditLogEntry> entries = new LinkedList<>();
    private final ObjectMapper objectMapper;
    private final File file = new File("powAuditLog.json");

    public JsonFilePowAuditLogRepository() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if (file.exists()) {
            PowAuditLogEntry[] powAuditLogEntries = objectMapper.reader().readValue(file, PowAuditLogEntry[].class);
            entries.addAll(Arrays.asList(powAuditLogEntries));
            return;
        }
        file.createNewFile();
        objectMapper.writer().writeValue(file, new LinkedList<>());
    }

    @Override
    public LinkedList<PowAuditLogEntry> load() {
        return entries;
    }

    @Override
    public void save(PowAuditLogEntry powAuditLogEntry) {
        entries.push(powAuditLogEntry);
        try {
            objectMapper.writer().writeValue(file, entries);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
