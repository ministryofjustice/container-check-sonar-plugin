package uk.gov.justice.digital.sonar.plugin.containercheck.report;

import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.JSON_REPORT_PATH_DEFAULT;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.JSON_REPORT_PATH_PROPERTY;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Configuration;
import org.sonar.api.scan.filesystem.PathResolver;

public final class JsonReportFile extends ReportFile {

    public static JsonReportFile getJsonReportFile(final Configuration config,
                                                    final FileSystem fileSystem,
                                                    final PathResolver pathResolver)
        throws FileNotFoundException {
        final String path = config.get(JSON_REPORT_PATH_PROPERTY).orElse(JSON_REPORT_PATH_DEFAULT);
        final Optional<File> checkedFile = checkReport(pathResolver.relativeFile(fileSystem.baseDir(), path));
        return checkedFile.map(JsonReportFile::new)
            .orElseThrow(() -> new FileNotFoundException("JSON-Container-Check report does not exist."));
    }

    private JsonReportFile(final File report) {
        super(report);
    }

}
