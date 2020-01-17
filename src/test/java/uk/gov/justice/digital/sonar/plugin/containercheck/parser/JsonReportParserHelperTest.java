package uk.gov.justice.digital.sonar.plugin.containercheck.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.FileSystem;

import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.Configuration;

import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.scan.filesystem.PathResolver;
import uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants;
import uk.gov.justice.digital.sonar.plugin.containercheck.model.Analysis;
import uk.gov.justice.digital.sonar.plugin.containercheck.report.JsonReportFile;

class JsonReportParserHelperTest {


  private FileSystem fileSystem;
  private PathResolver pathResolver;
  private Configuration config;

  @BeforeEach
  void setUp()
  {
    this.fileSystem = new DefaultFileSystem(new File("./src/test/resources"));
    this.pathResolver = mock(PathResolver.class);

    final MapSettings settings = new MapSettings();
    settings.setProperty(ContainerCheckConstants.JSON_REPORT_PATH_PROPERTY, "trivy-sample.json");
    config = settings.asConfig();
  }

  @DisplayName("Test parsing simple file to Java")
  @Test
  void testParseFile() throws IOException, ReportParserException {
    // Arrange
    final File jsonFile = Paths.get("./src/test/resources/trivy-sample.json").toFile();
    when(pathResolver.relativeFile(any(File.class), anyString())).thenReturn(jsonFile);

    // Act
    final JsonReportFile jsonReportFile = JsonReportFile.getJsonReportFile(config, fileSystem, pathResolver);
    final List<Analysis> analyses = JsonReportParserHelper.parse(jsonReportFile, Analysis.class);

    // Assert
    assertEquals(1, analyses.size());
    final Analysis analysis = analyses.get(0);
    assertThat(analysis.getTarget()).isEqualTo("mojdigitalstudio/digital-probation-java-skeleton (ubuntu 18.04)");
    assertEquals(110, analysis.getVulnerabilities().size());
  }

  @DisplayName("Test parsing simple file to Java")
  @Test
  void testParseFileNotJson() throws IOException {
    // Arrange
    final File jsonFile = Paths.get("./src/test/resources/Dockerfile").toFile();
    when(pathResolver.relativeFile(any(File.class), anyString())).thenReturn(jsonFile);
    final JsonReportFile jsonReportFile = JsonReportFile.getJsonReportFile(config, fileSystem, pathResolver);

    // Act
    assertThrows(ReportParserException.class, () -> JsonReportParserHelper.parse(jsonReportFile, Analysis.class));
  }
}
