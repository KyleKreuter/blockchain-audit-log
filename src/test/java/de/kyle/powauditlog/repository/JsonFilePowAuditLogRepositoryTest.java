package de.kyle.powauditlog.repository;

import de.kyle.powauditlog.PowAuditLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class JsonFilePowAuditLogRepositoryTest {
    @Test
    public void test_json_file_creation() throws IOException {
        JsonFilePowAuditLogRepository powAuditLogRepository = new JsonFilePowAuditLogRepository();
        Assertions.assertTrue(new File("powAuditLog.json").exists());
    }

    @Test
    public void test_json_write() throws IOException {
        File file = new File("powAuditLog.json");
        if (file.exists()){
            file.delete();
        }
        JsonFilePowAuditLogRepository powAuditLogRepository = new JsonFilePowAuditLogRepository();
        PowAuditLogger powAuditLogger = new PowAuditLogger(1, 2, powAuditLogRepository);
        powAuditLogger.addMessageToAuditLog("1");
        powAuditLogger.addMessageToAuditLog("2");
        powAuditLogger.addMessageToAuditLog("3");
        Assertions.assertEquals(powAuditLogRepository.load().size(), 3);
    }
}
