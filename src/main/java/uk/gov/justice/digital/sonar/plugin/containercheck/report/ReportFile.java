package uk.gov.justice.digital.sonar.plugin.containercheck.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public abstract class ReportFile {

    private static final Logger LOGGER = Loggers.get(ReportFile.class);

    private final File report;

    protected ReportFile(final File report) {
        this.report = report;
    }

    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(report.toPath());
    }

    protected static Optional<File> checkReport(final File report) {
        if (report == null) {
            return Optional.empty();
        }
        if (!report.exists()) {
            LOGGER.info("Container-Check {} report does not exist", report.getAbsolutePath());
            return Optional.empty();
        }
        if (!report.isFile()) {
            LOGGER.info("Container-Check {} report is not a normal file", report.getAbsolutePath());
            return Optional.empty();
        }
        if (!report.canRead()) {
            LOGGER.info("Container-Check {} report is not readable", report.getAbsolutePath());
            return Optional.empty();
        }

        return Optional.of(report);
    }

}
