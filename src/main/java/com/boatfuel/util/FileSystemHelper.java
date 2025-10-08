package com.boatfuel.util;

import org.apache.log4j.Logger;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

import java.io.*;
import java.util.Properties;

/**
 * File system utility with hardcoded paths and vendor-specific APIs
 * Konveyor violations:
 * - Hardcoded absolute file paths (not cloud-ready)
 * - JBoss VFS API usage (vendor lock-in)
 * - File system dependencies (anti-pattern for containers)
 */
public class FileSystemHelper {

    private static final Logger logger = Logger.getLogger(FileSystemHelper.class);

    // Hardcoded absolute paths (anti-pattern - not portable)
    private static final String CONFIG_DIR = "/opt/boatfuel/config";
    private static final String LOG_DIR = "/var/log/boatfuel";
    private static final String EXPORT_DIR = "C:\\BoatFuel\\exports"; // Windows path
    private static final String TEMP_DIR = "/tmp/boatfuel";

    /**
     * Load configuration from file system (anti-pattern)
     * Konveyor will flag: File system dependency, hardcoded paths
     */
    public static Properties loadConfiguration() throws IOException {
        Properties props = new Properties();

        // Hardcoded configuration file path
        File configFile = new File(CONFIG_DIR, "application.properties");

        logger.info("Loading configuration from: " + configFile.getAbsolutePath());

        if (!configFile.exists()) {
            logger.warn("Configuration file not found, creating default");
            createDefaultConfiguration(configFile);
        }

        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);
        }

        return props;
    }

    /**
     * Create default configuration file
     */
    private static void createDefaultConfiguration(File configFile) throws IOException {
        configFile.getParentFile().mkdirs();

        Properties defaults = new Properties();
        defaults.setProperty("database.url", "jdbc:mysql://localhost:3306/boatfuel");
        defaults.setProperty("database.user", "boatfuel_user");
        defaults.setProperty("database.password", "changeme"); // Hardcoded password!

        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            defaults.store(fos, "Boat Fuel Tracker Configuration");
        }
    }

    /**
     * Export data to CSV with hardcoded path
     * Konveyor will flag: File system dependency, hardcoded path
     */
    public static File exportToCSV(String userId, String csvData) throws IOException {
        // Hardcoded export directory
        File exportDir = new File(EXPORT_DIR);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        String filename = "fuel-export-" + userId + "-" + System.currentTimeMillis() + ".csv";
        File exportFile = new File(exportDir, filename);

        logger.info("Exporting data to: " + exportFile.getAbsolutePath());

        try (FileWriter writer = new FileWriter(exportFile)) {
            writer.write(csvData);
        }

        return exportFile;
    }

    /**
     * Using JBoss VFS API (vendor lock-in)
     * Konveyor will flag: JBoss-specific API, should use standard Java NIO
     */
    public static String readFileUsingJBossVFS(String path) throws IOException {
        logger.info("Reading file using JBoss VFS: " + path);

        VirtualFile virtualFile = VFS.getChild(path);

        if (!virtualFile.exists()) {
            throw new FileNotFoundException("File not found: " + path);
        }

        try (InputStream is = virtualFile.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            return content.toString();
        }
    }

    /**
     * Write log to file system (anti-pattern)
     * Konveyor will flag: File system logging, hardcoded paths
     */
    public static void writeAuditLog(String message) {
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        File logFile = new File(logDir, "audit.log");

        try (FileWriter writer = new FileWriter(logFile, true)) {
            String timestamp = new java.util.Date().toString();
            writer.write(timestamp + " - " + message + "\n");
        } catch (IOException e) {
            logger.error("Failed to write audit log", e);
        }
    }

    /**
     * Get temp directory (hardcoded)
     */
    public static File getTempDirectory() {
        File tempDir = new File(TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return tempDir;
    }
}
