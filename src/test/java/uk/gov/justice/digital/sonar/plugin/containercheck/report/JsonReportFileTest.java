package uk.gov.justice.digital.sonar.plugin.containercheck.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.scan.filesystem.PathResolver;
import uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants;


class JsonReportFileTest
{
    private FileSystem fileSystem;
    private PathResolver pathResolver;
    private MapSettings settings;
    private Configuration config;

    @BeforeEach
    void setUp()
    {
        this.fileSystem = new DefaultFileSystem(new File("./src/test/resources"));
        this.pathResolver = mock(PathResolver.class);

        settings = new MapSettings();
        settings.setProperty(ContainerCheckConstants.JSON_REPORT_PATH_PROPERTY, "trivy-sample.json");
        config = settings.asConfig();
    }

    @DisplayName("Get a file as a string")
    @Test
    void testLoadReportFile() throws IOException
    {
        // Arrange
        final File jsonFile = Paths.get("./src/test/resources/trivy-sample.json").toFile();
        when(pathResolver.relativeFile(any(File.class), anyString())).thenReturn(jsonFile);

        // Act
        final JsonReportFile jsonReportFile = JsonReportFile.getJsonReportFile(config, fileSystem, pathResolver);

        // Assert
        assertNotNull(jsonReportFile.getInputStream());
        assertThat(jsonReportFile.getReportContent().trim()).startsWith("[");
    }

    @DisplayName("Get a file which does not exist.")
    @Test
    void testLoadReportFileNotExist()
    {
        // Arrange
        final File jsonFile = Paths.get("./trivy-sample-not-exist.json").toFile();
        when(pathResolver.relativeFile(any(File.class), anyString())).thenReturn(jsonFile);

        // Act
        assertThrows(FileNotFoundException.class,
            () -> JsonReportFile.getJsonReportFile(config, fileSystem, pathResolver));
    }
}
